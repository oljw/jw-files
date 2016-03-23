/* globals tizen, webapis */

// Load requireJS config
var APP;

/* dummy structure to enable local browser debugging */
if (typeof(tizen) == 'undefined') {
	tizen = {
		application: {
			getCurrentApplication: function() {
				return 1;
			}
		},
		
		power: {
			isScreenOn: function() {
				return 1;
			},
			
			getScreenBrightness: function() {
				return 1;
			},
			
			request: function() {
				return 1;
			},
			
			setScreenStateChangeListener: function() {}
		}
	}
}

/**
 * Main application
 */
var app = function() {}; 

app.prototype = {
	useVideoAttractor: false,
	standalone:	false,
	standaloneTimer: null,
	
	//[[ START : smheo - change timeout from 50000ms to 3000ms
	standaloneTimeout: 2000,
	// original source
	//standaloneTimeout: 50000,
	//]] END
	
    legal: $("#legal").chapter(),
    home: $('#home').chapter(),
    chapterIndex: -1,
    currentDemo: null,
    currentDemoName: null,
    attractorFrameTime: 2000,
    attractorFrameInterval: null,
    attractorFrameIndex: 0,  
    attractorImageCount: 0,
    hiddenConstant: null, //checks if webkit or normal hidden type. see https://developer.tizen.org/documentation/articles/application-visibility for explanation.

    appControl: {
        app: tizen.application.getCurrentApplication(),
        
        hideApplication: function(){        
            if (!document[hiddenConstant]) {
            	
            	//[[ START : smheo - fixed bug
            	var currentApp = tizen.application.getCurrentApplication();
                currentApp.hide();
                // original source
                //app.hide();
                //]] END
            }
        },
        
        showApplication : function(){
            if (document[hiddenConstant]) {
            	if (!APP.isStandalone()) {
            		tizen.application.launch(this.app.appInfo.id);
            	}
            }
        },
        
        //[[ START : smheo - add a new function
        exitApp : function(){
        	app.exit();
        }
        //]] END
    },
    
    attractorFrameHandler: function() {
    	APP.attractorFrameIndex++;
    	
    	if (APP.attractorFrameIndex > APP.attractorFrameCount - 1) {
    		APP.attractorFrameIndex = 0;
    	}
    	
    	APP.attractorFrames.hide();
    	
    	$(APP.attractorFrames[APP.attractorFrameIndex]).show();
    },

    //defines attractor object and all associated methods
    attractor: {   
        init: function(el){
        	if (APP.useVideoAttractor) {
        		createVideo('attractorVideo', 'raw/' + Constants.attractorFileName, true);
        		
        		$('#attractorVideo').on('click', function() {
        			APP.handleVideoClick();
        		})
        	}
        },
        
        start: function(forceRestart) {
        	$('.demo, .chapter').hide();
        	
        	unloadVideos();
        	hideAllVideos();
        	
        	APP.currentDemo = null;
        	
        	if (APP.useVideoAttractor) {
        		$('.attractor').hide();
        		
        		var ret = showVideo('attractorVideo');
	        	
	        	if (!ret && forceRestart) {
	        		setTimeout("APP.attractor.start(true);", 2000);
	        	}
        	} else {
        		$('.attractor').show();
		        	
	        	clearInterval(APP.attractorFrameInterval);
	        	
	        	APP.attractorFrames = $('.attractor img');
	        	
	        	APP.attractorFrames.hide();
	        	
	        	APP.attractorFrameCount = APP.attractorFrames.length;
	        	
	        	$(APP.attractorFrames[0]).show();
	        	
	        	APP.attractorFrameIndex = 0;
	        	
	        	APP.attractorFrameInterval = setInterval(APP.attractorFrameHandler, APP.attractorFrameTime);	
        	}
        },
        
        stop: function() {
        	if (APP.useVideoAttractor) {
        		hideVideo('attractorVideo');
        	} else {
	        	$('.attractor').hide();
	        	
	        	try {
	        		APP.attractorFrames.hide();
	        	} catch (e) { }
	        	
	        	APP.attractorFrameIndex = 0;
	        	
	        	clearInterval(APP.attractorFrameInterval);
        	}
        }
    },
    
    /**
     * App initialization. Sets up App properties that relate to the SAP framework.
     */
    init: function()	{
        APP.reset();        //only calling this in init at the moment, seems too expensive to call every time the screen visibility changes. HelloAccessoryConsumer.wgtMay move to after the attractor starts playing...        
        APP.chapterIndex = -1;
        
        //deserialize the MasterMessage for application usage
        this.MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');
        
        try {
        	APP.bindEventListeners();
        } catch (e) { }
		
		try {
			APP.attractor.init();
		} catch (e) { }

		APP.startStandaloneTimer();
		
        // Start SAP
        SAP.connect();

        APP.turnScreenOn();
        
        try {
        	webapis.motion.start("PALM_TOUCH", APP.onPalmTouch);
        } catch (e) { }
        
        tizen.power.request("SCREEN", "SCREEN_NORMAL");
        tizen.power.request("CPU", "CPU_AWAKE");
        
        setTimeout('APP.attractor.start(true)', 3000);
    },
    
    /**
     * What to do on the palming of the watches.
     */
    onPalmTouch: function() {
    	$('body').scrollTop(0);
    },

    /**
     * Attach event handlers.
     */
    bindEventListeners: function() {
        // Window
        var listeners = {
        	'rotarydetent': APP.onRotaryEvent,
            'tizenhwkey': APP.onHardwareKeys,
            'sap.init': APP.onConnectionChange,
            'sap.peerAgentFailure': APP.onPeerAgentFailure,
            'sap.device.detached': APP.onPeerAgentFailure,
            'sap.connection.lost' : APP.onConnectionLost
        };

        // Custom listeners invoked from SAP onDataReceiveSuccess by type
        listeners['sap.eventType.' + this.MasterMessage.MessageType.END_CHAPTER] = function(customEvent) { APP.receivedEndOfChapter(customEvent.detail.message)};
        listeners['sap.eventType.' + this.MasterMessage.MessageType.START_CHAPTER] = function(e) { APP.receivedStartOfChapter(e.detail.message)};
        listeners['sap.eventType.' + this.MasterMessage.MessageType.SEEK_TO_END_OF_CHAPTER] = function(customEvent) { APP.receivedEndOfChapter(customEvent.detail.message)};
        listeners['sap.eventType.' + this.MasterMessage.MessageType.END_DEMO] = function() {APP.showLegal();}
        listeners['sap.eventType.' + this.MasterMessage.MessageType.SHOW_LEGAL] = function() {APP.showLegal();}
        listeners['sap.eventType.' + this.MasterMessage.MessageType.CHANGE_DEMO] = function(customEvent) { APP.receivedStartDemo(customEvent.detail.message)};
        listeners['sap.eventType.' + this.MasterMessage.MessageType.START_DEMO] = function(customEvent) { APP.receivedStartDemo(customEvent.detail.message)};
		
		EventModuleInstance.listeners(listeners);

        // DOM
        var visibilityChange;

        // DOM
        if (typeof document.hidden !== "undefined") {
            hiddenConstant = "hidden";
            visibilityChange = "visibilitychange";
        } else if (typeof document.webkitHidden !== "undefined") {
            hiddenConstant = "webkitHidden";
            visibilityChange = "webkitvisibilitychange";
        }

        document.addEventListener(visibilityChange, this.onVisibilityChange, false);
        tizen.power.setScreenStateChangeListener(this.onScreenStateChanged);
    },
    
    /**
     * Stuff to do when we get a rotary event.
     * @param event
     */
    onRotaryEvent: function(event) {
    	try {
    		//[[ START : smheo - check the currentDemo
    		if (APP.currentDemo != null){
    			APP.currentDemo.rotaryEvent(event.detail.direction);
    		}
    		// Original source
    		//APP.currentDemo.rotaryEvent(event.detail.direction);
    		//]] END
    	} catch (e) { }
    	
    	APPStandalone.onEvent('rotary', event.detail.direction);
    },

    /**
     * Show the legal info screen.
     */
    showLegal: function() {
    	APP.currentDemo = APP.legal;
    	APP.currentDemoName = this.MasterMessage.MessageType.SHOW_LEGAL;

        APP.attractor.stop();
        
        APP.legal.show();
    },
    
    /**
     * Show the home screen (static image).
     */
    showHome: function() {
    	//console.log('showHome()++');
    	
    	APP.currentDemo = APP.home;
    	APP.currentDemoName = this.MasterMessage.DemoValue.HOME;

        APP.attractor.stop();
        
        APP.home.show();
    },

    /**
     * Stuff to do on end of demo.
     */
    endDemo: function() {
    	//console.log('endDemo()++');
    	
    	if (APP.currentDemo) {
    		//console.log('endDemo()- hide demo : ' + APP.currentDemo);
            APP.currentDemo.hide();
            APP.currentDemo = null;
         	APP.currentDemoName = this.MasterMessage.DemoValue.ATTRACTOR;
    	}
    	
        APP.attractor.start();
    },

    /**
     * Stuff to do when we get told to go to another demo.
     * @param message
     */
    receivedChangeDemo: function(message) {
    	//console.log('receivedChangeDemo()++');
    	
        APP.receivedStartDemo(message);
    },

    /**
     * Same as change demo..
     * @param startDemoMessage
     */
    receivedStartDemo: function(startDemoMessage) {
        var demoName = startDemoMessage.demo.name;
        
        //console.log('receivedStartDemo()++');

        if (!demoName) {
        	//console.log('receivedStartDemo() : demoName is null');
			return;
		}
		
		//MasterMessage.DemoValue representation of the demoName as key
		var demoValue = this.MasterMessage.DemoValue[demoName];
		
		//console.log('receivedStartDemo() - demoName : ' + demoName + '[' + demoValue + ']');

		//Update currentDemoName. Don't change demo if we are on the same demo.
		if (APP.currentDemoName === demoValue) {
			return;
		} else {
			APP.currentDemoName = demoValue;
		}

		if (document[hiddenConstant]) {
        	APP.appControl.showApplication();
        }
        
        if (demoName != null) {
            switch (demoValue) {
                case this.MasterMessage.DemoValue.DESIGN:
                	//console.log('receivedStartDemo : DESIGN');
                	
                	//[[ START : smheo - add
                	unloadVideos();
                	//]] END
                    
                	APP.chapterIndex = 0;
                    
                    APP.currentDemo = designDemo;
                    
                    designDemo.preloadVideos();
                    designDemo.show();
                    break;
                case this.MasterMessage.DemoValue.ACCESS:
                	//console.log('receivedStartDemo : ACCESS');
                	
                	//[[ START : smheo - add
                	unloadVideos();
                	//]] END
                	
                	APP.chapterIndex = 0;
                    
                    APP.currentDemo = accessDemo;
                    
                    accessDemo.preloadVideos();
                    accessDemo.show();
                    break;
                case this.MasterMessage.DemoValue.THREEG:
                	//console.log('receivedStartDemo : THREEG');
                	
                	//[[ START : smheo - fixed bug
                	hideAllVideos();
                	unloadVideos();
                	//]] END
                	
                	APP.chapterIndex = 0;
                    
                    APP.currentDemo = threeGDemo;
                    
                    threeGDemo.preloadVideos();
                    threeGDemo.show();
                    break;
                case this.MasterMessage.DemoValue.FITNESS:
                	//console.log('receivedStartDemo : FITNESS');
                	
                	//[[ START : smheo - fixed bug
                	hideAllVideos();
                	unloadVideos();
                	//]] END
                	
                	APP.chapterIndex = 0;
                    
                    APP.currentDemo = fitnessDemo;
                    
                    fitnessDemo.preloadVideos();
                    fitnessDemo.show();
                    break;
                case this.MasterMessage.DemoValue.BATTERY:
                	//console.log('receivedStartDemo : BATTERY');
                	
                	//[[ START : smheo - add
                	unloadVideos();
                	//]] END
                    
                	APP.chapterIndex = 0;
                    
                    APP.currentDemo = batteryDemo;                  
                    
                    batteryDemo.preloadVideos();
                    batteryDemo.show();
                    break;
                case this.MasterMessage.DemoValue.ATTRACTOR:
                	//console.log('receivedStartDemo : ATTRACTOR');
                	
                	if (APP.currentDemo) {
                    	hideAllVideos();
                        $('.demo, .chapters').hide();
                        
                        APP.chapterIndex = -1;
                        
                        try {
                        	APP.currentDemo.hide();
                        } catch (e) {}
                        
                        APP.currentDemo = null;
                    }
                    
                    APP.attractor.start();
                    break;
                case this.MasterMessage.DemoValue.HOME:
                	//console.log('receivedStartDemo : HOME');
                	
                	APP.showHome();
                	
                    break;
            }
            
            APP.turnScreenOn();
        }
    },

    /**
     * Chapter advancing while in demo..
     * @param startChapterMessage
     */
    receivedStartOfChapter: function(startChapterMessage) {
    	APP.chapterIndex = startChapterMessage.chapter.index;
    	
    	//console.log('receivedStartOfChapter : Chapter Index : ' + APP.chapterIndex);
		
		if (APP.chapterIndex >= 0) {
        	if (APP.currentDemo.startChapter) {
            	try {
            		APP.currentDemo.startChapter(this.chapterIndex, startChapterMessage.data);
            	} catch (e) { }
            }
        }
		
        APP.turnScreenOn();
    },

    /**
     * End of chapter handling while in a demo..
     * @param endChapterMessage
     */
    receivedEndOfChapter: function(endChapterMessage) {
    	//console.log('receivedEndOfChapter()++');
		if (endChapterMessage != null) {
			if (APP.currentDemo.endChapter) {
				APP.currentDemo.endChapter(endChapterMessage.chapter.index, endChapterMessage);
			}
		}
	},

	/**
	 * Turn the device screen on from off/dimmed.
	 */
    turnScreenOn: function() {
    	try {
    		//console.log('turnScreenOn()++');
            tizen.power.turnScreenOn();
            tizen.power.setScreenBrightness(1);
    	} catch (e) { }
    },

    /**
     * EVENT HANDLER
     * App minimizes event
     */
    onHardwareKeys: function(ev) {
        if (ev.keyName === 'back') {
        }
    },

    // Lets clear any old user data and ensure bluetooth is screen are on
    reset: function(){
    	//[[ START : smheo
    	console.log('RESET');
    	//]] END
    	
    	var cameraDir = 'file:///opt/usr/media/DCIM/Camera';

        APP.turnScreenOn();

        // Incase a user has taken pictures on the device, lets remove them
        try{
            tizen.filesystem.resolve(cameraDir, removeDeviceImages, function(e){}, "rw");
        } catch(e) {}

        function removeDeviceImages(dir) {
            if (dir.isDirectory) {
                dir.listFiles(function(files) {
                    var file, i,
                        len = files.length;

                    // Loop through every potential picture and remove it
                    for (i = 0; i < len; i++) {
                        file = files[i];

                        if (file.isFile)	{
                            dir.deleteFile(cameraDir + file.name);
                        }
                    }

                });
            }
        }

        // Bluetooth
        // Lets turn it on
        try {
            var adapter = tizen.bluetooth.getDefaultAdapter();

            if (!adapter.powered) {
                adapter.setPowered(true);
            }

        } catch (e) { }
    },

    /**
     * Stuff to do if the screen goes off while we doing our demo stuff.
     * @param previousState
     * @param changedState
     */
    onScreenStateChanged: function(previousState, changedState) {
    	if (changedState == "SCREEN_OFF") {
        	tizen.application.launch(tizen.application.getCurrentApplication().appInfo.id);
        }

        APP.turnScreenOn();
    },

    /**
     * EVENT HANDLER
     * Handles visibility change event. Minimize / Maximize
     */
    onVisibilityChange: function(data){
        if (document[hiddenConstant]) {
        	//console.log('onVisibilityChange : hide');
        	
        	stopPowerInterval();
        	tizen.power.release("SCREEN");
            tizen.power.release("CPU");
            removeVideos();
        	
        	//If presently in current demo, then re-launch application!
        	
        	if (!APP.isStandalone()) {
        		//console.log('onVisibilityChange : hide - not standalone');
        		
	        	if (APP.currentDemo) {
	        		APP.appControl.showApplication();
	            } else {
	            	APP.attractor.stop();
	            }
        	} else {
        		APPStandalone.stop();
        	}

        } else {
        	
        	startPowerInterval();
        	tizen.power.request("SCREEN", "SCREEN_NORMAL");
            tizen.power.request("CPU", "CPU_AWAKE");
            reinstateVideos();
        	
        	if (APP.isStandalone()) {
        		APPStandalone.stop();
        		APPStandalone.start();
        		APP.attractor.start();
        	} else {
        	
	            if (!APP.currentDemo) {
	            	APP.attractor.start();
	            } else {
	                APP.attractor.stop();
	                
	                if (APP.chapterIndex != 0) {
	                	try {
	                		APP.currentDemo.startChapter(APP.chapterIndex, data);
	                	} catch (e) { }
	                }
	            }
        	}
        }
        
        APP.turnScreenOn();
    },

    /**
     * EVENT HANDLER
     * Handles SAP.connection.lost event
     */
    onConnectionLost: function(data) {
    	//console.log('onConnectionLost()++');
    	
        if (APP.currentDemo) {
	        APP.currentDemo.hide();
	        APP.currentDemo = null;
        }
        
        APP.chapterIndex = -1;

        SAP.connect();        
        
        APP.startStandaloneTimer();
        
        APP.attractor.start();
    },
    
    /**
     * @return Boolean indicating if we arein stand alone mode
     */
    isStandalone: function() {
    	//console.log('isStandalone() : ' + APP.standalone);
    	
    	return APP.standalone;
    },
    
    /**
     * Handle video/attractor click.
     */
    handleVideoClick: function() {
    	if (APP.isStandalone()) {
    		//console.log('handleVideoClick() : APPStandalone.handleVideoClick()');
			APPStandalone.handleVideoClick();
		} else {
			try {
				//console.log('handleVideoClick() : send mastermessage and make Home');
				var MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');
	
				var demoObj =
					CommonMessagingDeserializerInstance.buildDemoObject('Home');
				
				var applicationMessage =
					CommonMessagingDeserializerInstance.getWrappedApplicationMessageForBroadcast(MasterMessage.MessageType.CHANGE_DEMO, 'ChangeDemoMessage', {demo: demoObj});
	
				SAP.sendMessage(applicationMessage);
			} catch (e) { }
		}
    },
    
    /**
     * Go to the next chapter if in standalone mode.
     */
    manualChapterSeek: function() {
    	//console.log('manualChapterSeek()++');
    	if (APP.isStandalone()) {
			APPStandalone.manualChapterSeek();
		}
    },
    
    /**
     * Start a timer to go into standalone if we don't connect inside the timeout.
     */
    startStandaloneTimer: function() {
    	APPStandalone.stop();
    	
    	APP.stopStandaloneTimer();
    	
    	APP.standaloneTimer = setTimeout(function() {
    		APP.standalone = true;
    		APPStandalone.start();
    	}, APP.standaloneTimeout);
    },
    
    /**
     * Stop the timer that would start standalone if unconnected.
     */
    stopStandaloneTimer: function() {
    	clearTimeout(APP.standaloneTimer);
    },
    
    /**
     * Exit this application and go to native UI.
     */
    exitApplication: function() {    	
    	APP.appControl.hideApplication();
    	
    	//[[ START : smheo - fixed bug
    	APP.turnScreenOn();
    	//]] END
    },    

    /**
     * EVENT HANDLER
     * Connection change event handler.
     * @param {CustomEvent} data
     */
    onConnectionChange: function(data) {
        // if there was a problem with connection
        if (!data.detail.status) {
        	
        	//console.log('onConnectionChange()++ : disconnect');
        	
            SAP.connect();
            
            APP.startStandaloneTimer();
            
            APP.attractor.start();
        } else {
        	
        	//console.log('onConnectionChange()++ : connect');
        	
            APP.standalone = false;
            
            APPStandalone.stop();
            
            APP.stopStandaloneTimer();

            APP.attractor.start();
        }
    },
    
    /**
     * What to do when a peer agent failure (SAP error) occurs.
     */
    onPeerAgentFailure: function() {
    	
    	//console.log('onPeerAgentFailure()++');
    	
    	APP.startStandaloneTimer();
        
        APP.attractor.start();
    },
    
    /**
     * Build a GA tracking event message to kick back to the droid.
     * @param action
     * @param label
     * @returns
     */
    buildTrackEventMessage: function(action, label) {
    	try {
	    	var MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');
	
			return CommonMessagingDeserializerInstance.getWrappedApplicationMessageForBroadcast(MasterMessage.MessageType.TRACK_EVENT, 'TrackEventMessage', {action: action, label: label });
    	} catch (e) {
    		return null;
    	}
    },
    
    sendMessage: function(message) {
		SAP.sendMessage(message);
    }
}

var APP = new app();

var powerInterval = null;

var startPowerInterval = function() {
	stopPowerInterval();
	
	powerInterval = setInterval("APP.turnScreenOn();", 8000);
}

var stopPowerInterval = function() {
	if (powerInterval != null) {
		clearInterval(powerInterval);
	}
}




$(document).ready(function() {
	APP.init();	
	
	startPowerInterval();
});
