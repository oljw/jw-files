/*
 * app.js class
 * created by icanmobile on 09/30/2016
 */

var RetailSolis;
(function (RetailSolis) {
	var App = (function () {
		function App() {
			try {
				if (App.sInstance)
					return App.sInstance;
				App.sInstance = this;
	
				this.powerInterval = null;
				this.retryCount = 0;
				this.currentPage = null;
				this.duration = null;
				
				this.isStandalone = true;
				
				this.attractorInterval = null;
				this.pairedDecisionInterval = null;
				this.decisionTimeout = null;
				this.demoTimeout = null;
				
				/**** disable/enable animation ****/
				/** this will apply to all pages except attractor loop. */
				this.enableAnimation = false;
				this.pageEnterAnim = 'fadeIn';
				this.pageExitAnim = 'fadeOut';
				/************************************************/
	
				this.sap = retail.sapmanager.SAP.getInstance();
				this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
			} catch (err) {
				console.log("App : exception [" + err.name + "] msg[" + err.message + "]");
			}
		}
		
		/*
		 * getInstance function - Singleton
		 */
		App.getInstance = function () {
			try {
				if (!App.sInstance) {
					App.sInstance = new App();
				}
				return App.sInstance;
			} catch (err) {
				console.log("App.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};
		App.sInstance = null;
		
		/*
		 * Turn screen on
		 */
	    App.prototype.turnScreenOn = function() {
	    	try {
	    		console.log("App.turnScreenOn)+ ");
	            tizen.power.turnScreenOn();
	            tizen.power.setScreenBrightness(1);
	    	} catch (e) { 
	    		console.log("App.prototype.turnScreenOn : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    App.prototype.startPowerInterval = function() {
	    	try {
		    	this.stopPowerInterval();
		    	this.turnScreenOn();
		    	
	    		tizen.power.request("SCREEN", "SCREEN_NORMAL");
	    		tizen.power.request("CPU", "CPU_AWAKE");
		    	
		    	this.powerInterval = setInterval(this.turnScreenOn, 5000);
	    	} catch (err) {
	    		console.log("App.prototype.startPowerInterval : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    App.prototype.stopPowerInterval = function() {
	    	try {
		    	if (this.powerInterval != null) {
		    		clearInterval(this.powerInterval);
		    	}
	    	} catch (err) {
	    		console.log("App.prototype.stopPowerInterval : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
		/*
		 * init function
		 */
		App.prototype.init = function () {
			try {
				console.log("App.prototype.init");
				this.startPowerInterval();
				
				this.bindVisibilityListeners();
				this.initialDate = new Date();
				
				//service start function
				retail.servicemanager.RetailService.getInstance().start();
				
				//[[ START : smheo - for test : insert the bindEventBus and register code for the sap callback, sap connect 
				App.bindEventBus();
				this.sap.setCallback(App.connectedSAP, App.errorSAP);
				this.sap.connect();
				// removed original code of below
				//this.sap.connect(App.connectedSAP, App.errorSAP);
				//]] END
				
			} catch (err) {
				console.log("App.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};
		
		/*
		 * exit function
		 */
        App.prototype.exit = function () {
        	try {
	        	console.log("App.prototype.exit)+ ");
	    		tizen.power.release("SCREEN");
	    		tizen.power.release("CPU");
	        	tizen.application.getCurrentApplication().exit();
        	} catch (err) {
        		console.log("App.prototype.exit : exception [" + err.name + "] msg[" + err.message + "]");
        	}
        };
		
        /*
         * get / set mode
         */
        App.prototype.isStandaloneMode = function () {
        	try {
        		return this.isStandalone;
        	} catch (err) {
        		console.log("App.prototype.isStandaloneMode : exception [" + err.name + "] msg[" + err.message + "]");
        	}
        };
        App.prototype.setStandaloneMode = function (isStandalone) {
        	try {
        		this.isStandalone = isStandalone;
        	} catch (err) {
        		console.log("App.prototype.setStandaloneMode : exception [" + err.name + "] msg[" + err.message + "]");
        	}
        };
        
        /*
		 * SAP functions
		 */
		App.connectedSAP = function (connected) {
			try {
				App.getInstance().retryCount = 0;
				if (connected) { //SAP is connected
					console.log("App.connectedSAP connected");
					//set paired mode
					App.getInstance().setStandaloneMode(false);
					
					App.getInstance().changePage(Constants.PageInfo.AttractorPage.HTML);
					
					//connect sap output to PacketParser input for sending packet to mobile
					App.getInstance().sap.setOutput(retail.sapmanager.packet.PacketParser.getInstance().input);
					
					// launch the app again when SAP is disconnected and connected again.
					App.getInstance().turnScreenOn();
					tizen.application.launch(tizen.application.getCurrentApplication().appInfo.id);
				}
				else { //SAP is disconnected
					console.log("App.connectedSAP not connected");
					//set standalone mode
					App.getInstance().setStandaloneMode(true);
					
					//go to attractor loop
					App.getInstance().changePage(Constants.PageInfo.AttractorPage.HTML);
				}
			} catch (err) {
				console.log("App.connectedSAP : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};
		
		App.errorSAP = function (error) {
			try {
				//[[ START : smheo - for test
				console.log("App.errorSAP : " + error);
				
				
				// removed original code.				
//				App.getInstance().retryCount++;
//				if (App.getInstance().retryCount < App.getInstance().MAX_HOST_CONNECT_RETRY) 
//				{
//					// CHANGE App.getInstance().MAX_HOST_CONNECT_RETRY to bigger number or remove it to keep on try to connect when in detached mode.
//					console.log("Trying to reconnect....");
//					setTimeout(function() {
//						App.getInstance().sap.connect(App.connectedSAP, App.errorSAP);
//					}, 3000);
//				}
//				else
//				{	//set standalone mode
//					App.getInstance().setStandaloneMode(true);
//					App.getInstance().retryCount = 0;
//					
//					App.bindEventBus();
//					
//					/* why doing this when it's in standalone mode? */ 
//					//connect sap output to PacketParser input for sending packet to mobile
////					App.getInstance().sap.setOutput(retail.sapmanager.packet.PacketParser.getInstance().input);
//					
//				}
				//]] END
			} catch (err) {
				console.log("App.errorSAP : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};
		
		/*
		 * Event functions
		 */
		App.bindEventBus = function () {
			try {
				//add basic events
				var listeners = {
					'rotarydetent': App.onRotaryEvent,
					'tizenhwkey': App.onHardwareKeys,
				};
				
				//add "Change Page" packet (mobile <-> wearable)
				listeners['sap.packet.changepage'] = function(customEvent) {App.onChangePageEvent(customEvent.detail.message);};
				//add "Start Demo" packet (mobile -> wearable)
				listeners['sap.packet.startdemo'] = function(customEvent) {App.onStartDemoEvent(customEvent.detail.message);};
				//add "Notify Interaction to Mobile" packet (mobile <- wearable)
				listeners['sap.packet.notifyinteraction'] = function(customEvent) {App.onNotifyInteraction(customEvent.detail.message);};
				//add "Chapter Item from Mobile" packet (mobile -> wearable)
				listeners['sap.packet.chapteritem'] = function(customEvent) {App.onChapterItemEvent(customEvent.detail.message);};
				
				//enroll all of events to EventBus
				retail.appmanager.EventBus.getInstance().listeners(listeners);
			} catch (err) {
				console.log("App.bindEventBus : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};

		App.onRotaryEvent = function(event) {
	    	try {
	    		console.log("App.onRotaryEvent)+ event.detail.direction = " + event.detail.direction);
//	    		if(RetailSolis.App.getInstance().getCurrentPage().getPage() === Constants.PageInfo.AttractorPage.PAGE) {
//					var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(Constants.PageInfo.AttractorPage.PAGE, Constants.INTERACTIONS.ROTARY, null).build();
//					RetailSolis.App.getInstance().fetchPacket(packet);
//					
//					RetailSolis.App.getInstance().changePage("js/ui/DecisionPage.html");
//				}
	    	} catch (err) {
	    		console.log("App.onRotaryEvent : exception [" + err.name + "] msg[" + err.message + "]");
	    	}	    	
//	    	APPStandalone.onEvent('rotary', event.detail.direction);
	    };
	    
	    App.onHardwareKeys = function(ev) {
	    	try {
		    	console.log("App.onHardwareKeys)+ ev.keyName = " + ev.keyName);
		        if (ev.keyName === 'back') {
		        	
		        	// send back pressed analytics data to phone.
					var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(RetailSolis.App.getInstance().currentPage.getPage(), Constants.INTERACTIONS.BACK_PRESSED, null, "BACK_KEY").build();
					RetailSolis.App.getInstance().fetchPacket(packet);
		        	
		        	switch( App.getInstance().currentPage.getPage() ) {
		        	case Constants.PageInfo.AttractorPage.PAGE:
		        		console.log("App.onHardwareKeys : " + Constants.PageInfo.AttractorPage.PAGE);
		        		App.getInstance().exit();
		        		break;
		        		
		        	case Constants.PageInfo.DecisionPage.PAGE:
		        		console.log("App.onHardwareKeys : " + Constants.PageInfo.DecisionPage.PAGE);
		        		var nextPageInfo = App.getInstance().getPageInfo(Constants.PageInfo.AttractorPage.PAGE);
		        		if (nextPageInfo == null) return;
		        		
						var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.AttractorPage.PAGE, "BACKWARD").build();
						RetailSolis.App.getInstance().fetchPacket(packet);

		        		App.getInstance().backAnimationHandler(nextPageInfo.HTML);
		        		break;
		        		
		        	case Constants.PageInfo.PairedDecisionPage.PAGE:
		        		console.log("Back key is not allowed");
		        		break;
		        		
		        	default:
		        		console.log("App.onHardwareKeys : default");
		        		
		        		var nextPageInfo = App.getInstance().getPageInfo(Constants.PageInfo.DecisionPage.PAGE);
		        		if (nextPageInfo == null) return;

						var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.DecisionPage.PAGE, "BACKWARD").build();
						RetailSolis.App.getInstance().fetchPacket(packet);
						
		        		App.getInstance().backAnimationHandler(nextPageInfo.HTML);
	
	        			break;
		        	}
		        }
	    	} catch (err) {
	    		console.log("App.onHardwareKeys : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    App.prototype.backAnimationHandler = function(prevPage) {
	    	if(this.enableAnimation) {
    			this.getCurrentPage().startAnimation(this.pageExitAnim);
        		setTimeout(function(){
        			App.getInstance().changePage(prevPage);
        		},500);
			} else {
				this.changePage(prevPage);
			}
	    }
	    
	    App.onChangePageEvent = function (data) {
	    	console.log("App.onChangePageEvent)+ ");
	    	
	    	try {
	    		if (data == null || data.nextPage == null) return;
	    		var nextPageInfo = App.getInstance().getPageInfo(data.nextPage);
	    		if (nextPageInfo == null) return;
	    		App.getInstance().changePage(nextPageInfo.HTML);
	    	} catch (err) {
	    		console.log("App.onChangePageEvent : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    App.onStartDemoEvent = function (data) {
	    	try {
	    		if (data == null || data.currentPage == null) return;
	    		if (data.currentPage != App.getInstance().getCurrentPage().getPage()) return;
	    		
	    		App.getInstance().getCurrentPage().onStartDemo();
	    	} catch (err) {
	    		console.log("App.onStartDemoEvent : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    /*
	     * THIS FUNCTION SHOULD BE INSIDE OF THE HIGHEST PAGE OF DEMO PAGES. - JW
	     */
//	    App.prototype.setDemoFinishedAction = function () {
//	    	console.log("############### app setdemofinishedaction");
//				var videoTextureView = retail.videomanager.VideoTextureView.getInstance();
//				if (App.getInstance().isStandaloneMode()) {
//					videoTextureView.setOnVideoEnded(App.getInstance().changePage("js/ui/DecisionPage.html"));
//				} else {
//					videoTextureView.setOnVideoEnded(App.getInstance().changePage("js/ui/PairedDecisionPage.html"));
//				}
//	    	} catch (err) {
//	    		console.log("App.onStartDemoEvent : exception [" + err.name + "] msg[" + err.message + "]");
//	    	}
//	    };
		
	    App.onNotifyInteraction = function (data) {
	    	console.log("App.onNotifyInteraction)+ data.currentPage = " + data.currentPage + ", data.interaction = " + data.interaction);
	    	try {
				var packet = App.getInstance().packetBuilder
											  .header("NOTIFY_INTERACTION_TO_MOBILE")
											  .notifyInteraction(data.currentPage, data.interaction)
											  .build();
				App.getInstance().sap.fetch(packet);
	    	} catch (err) {
	    		console.log("App.onNotifyInteraction : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    	    
	    App.onChapterItemEvent = function (chapter) {
	    	console.log("App.onChapterItemEvent)+ ");
	    	try {
				var chapterFuncName = App.getInstance().currentPage.getClass() + ".onChapter_" + chapter.chapterIndex;
				var chapterFunc = retail.appmanager.util.FuncUtil.getInstance().stringToFunction(chapterFuncName);
				chapterFunc(chapter);
	    	} catch (err) {
	    		console.log("App.onChapterItemEvent : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    /*
	     * Attach visibility event handlers.
	     */
	    App.prototype.bindVisibilityListeners = function() {
	    	try {
		        // DOM
		        var visibilityChange;
	
		        // DOM
		        if (typeof document.hidden !== "undefined") {
		            App.hiddenConstant = "hidden";
		            visibilityChange = "visibilitychange";
		        } else if (typeof document.webkitHidden !== "undefined") {
		            App.hiddenConstant = "webkitHidden";
		            visibilityChange = "webkitvisibilitychange";
		        }
	
		        document.addEventListener(visibilityChange, App.onVisibilityChanged, false);
		        tizen.power.setScreenStateChangeListener(App.onScreenStateChanged);
	    	} catch (err) {
	    		console.log("App.prototype.bindVisibilityListeners : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    App.hiddenConstant = null;
	    
	    /*
	     * Handles visibility change event. Minimize / Maximize
	     */
	    App.onVisibilityChanged = function(data){
	    	try {
		        if (document[App.hiddenConstant]) { // Going background
		        	if (App.getInstance().isStandaloneMode()) { // Standalone
		        		$("#ui-container").empty();
			        	App.getInstance().stopPowerInterval();
		        	} else { // Paired
		        		if (App.getInstance().currentPage.getPage() == Constants.PageInfo.AttractorPage.PAGE) {
		        			$("#ui-container").empty();
		        			App.getInstance().sap.disconnect();
		        			App.getInstance().exit();
		        		} else {
					        App.getInstance().turnScreenOn();
					        tizen.application.launch(tizen.application.getCurrentApplication().appInfo.id);
		        		}
		        	}
		        } else { // Coming foreground
		        	if (App.getInstance().isStandaloneMode()) {	// Standalone
			    		App.getInstance().startPowerInterval();
						App.getInstance().changePage(Constants.PageInfo.AttractorPage.HTML);
		        	} else { // Paired
		        		if (App.getInstance().currentPage.getPage() == Constants.PageInfo.AttractorPage.PAGE) {
		        			App.getInstance().sap.connect();
				    		App.getInstance().startPowerInterval();
							App.getInstance().changePage(Constants.PageInfo.AttractorPage.HTML);
		        		}
		        	}
		        }
	    	} catch (err) {
	    		console.log("App.onVisibilityChanged : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    /*
	     * Stuff to do if the screen goes off while we doing our demo stuff.
	     */
	    App.onScreenStateChanged = function() {
	    	console.log("onScreenStateChanged");
	    	try {
	    		App.getInstance().sap.disconnect();
    		/////// Won't be used because this should be done in service ///////////////////////
//		        RetailSolis.App.getInstance().turnScreenOn();
//		        tizen.application.launch(tizen.application.getCurrentApplication().appInfo.id);
	    	////////////////////////////////////////////////////////////////////////////////////
	    	} catch (err) {
	    		console.log("App.onScreenStateChanged : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    
	    /*
	     * send packet from demo pages to mobile
	     */
	    App.prototype.fetchPacket = function(packet) {
	    	try {
	    		App.getInstance().sap.fetch(packet);
	    	} catch (err) {
	    		console.log("App.prototype.fetchPacket : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    }
	    
		/*
		 * get/set current page
		 */
		App.prototype.getCurrentPage = function () {
			try {
				return App.getInstance().currentPage;
			} catch (err) {
				console.log("App.prototype.getCurrentPage : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};
		App.prototype.setCurrentPage = function (currentPage) {
			try {
				App.getInstance().currentPage = currentPage;
				
				/**** start forward animation ****/
				if(this.enableAnimation) {
					currentPage.startAnimation(App.getInstance().pageEnterAnim);
				}
			} catch (err) {
				console.log("App.prototype.setCurrentPage : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};

		App.prototype.changePage = function (nextPageHtml) {
			try {
				console.log("App.prototype.changePage)+ nextPageHtml = " + nextPageHtml);				
				
				if (nextPageHtml == null) return;				
				
				var currentPage = null;
				if (this.getCurrentPage() == null)
					currentPage = Constants.PageInfo.AttractorPage.PAGE;
				else
					currentPage = this.getCurrentPage().getPage();
				var nextPage = this.getPageInfo(nextPageHtml).PAGE;		

				switch (currentPage) {
					case Constants.PageInfo.AttractorPage.PAGE:
						break;
					default:
						if (currentPage == nextPage) return;		
				}

				if(this.demoTimeout) {
					clearInterval(this.demoTimeout);
					this.demoTimeout = null;
				}				
				
				if (this.getCurrentPage() != null)
					this.getCurrentPage().deactivate();				
				
				$("#ui-container").load(nextPageHtml);
			} catch (err) {
				console.log("App.prototype.changePage : exception [" + err.name + "] msg[" + err.message + "]");
			}
		};
		
		/*
		 * get page infomation
		 */
	    App.prototype.getPageInfo = function (page) {
	    	try {
		    	switch (page) {
			    	case Constants.PageInfo.AttractorPage.PAGE:
			    	case Constants.PageInfo.AttractorPage.CLASS:
			    	case Constants.PageInfo.AttractorPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.AttractorPage.PAGE, 
									CLASS: Constants.PageInfo.AttractorPage.CLASS, 
									HTML: Constants.PageInfo.AttractorPage.HTML
								};
			    	case Constants.PageInfo.DecisionPage.PAGE:
			    	case Constants.PageInfo.DecisionPage.CLASS:
			    	case Constants.PageInfo.DecisionPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.DecisionPage.PAGE, 
									CLASS: Constants.PageInfo.DecisionPage.CLASS, 
									HTML: Constants.PageInfo.DecisionPage.HTML
								};
			    	case Constants.PageInfo.CallsAndTextsPage.PAGE:
			    	case Constants.PageInfo.CallsAndTextsPage.CLASS:
			    	case Constants.PageInfo.CallsAndTextsPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.CallsAndTextsPage.PAGE, 
									CLASS: Constants.PageInfo.CallsAndTextsPage.CLASS, 
									HTML: Constants.PageInfo.CallsAndTextsPage.HTML
			    				};
			    	case Constants.PageInfo.OnboardGPSPage.PAGE:
			    	case Constants.PageInfo.OnboardGPSPage.CLASS:
			    	case Constants.PageInfo.OnboardGPSPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.OnboardGPSPage.PAGE, 
									CLASS: Constants.PageInfo.OnboardGPSPage.CLASS, 
									HTML: Constants.PageInfo.OnboardGPSPage.HTML
			    				};
			    	case Constants.PageInfo.BatteryPage.PAGE:
			    	case Constants.PageInfo.BatteryPage.CLASS:
			    	case Constants.PageInfo.BatteryPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.BatteryPage.PAGE, 
									CLASS: Constants.PageInfo.BatteryPage.CLASS, 
									HTML: Constants.PageInfo.BatteryPage.HTML
			    				};
			    	case Constants.PageInfo.PersonalizePage.PAGE:
			    	case Constants.PageInfo.PersonalizePage.CLASS:
			    	case Constants.PageInfo.PersonalizePage.HTML:
			    		return {
									PAGE: Constants.PageInfo.PersonalizePage.PAGE, 
									CLASS: Constants.PageInfo.PersonalizePage.CLASS, 
									HTML: Constants.PageInfo.PersonalizePage.HTML
			    				};
			    	case Constants.PageInfo.DesignPage.PAGE:
			    	case Constants.PageInfo.DesignPage.CLASS:
			    	case Constants.PageInfo.DesignPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.DesignPage.PAGE, 
									CLASS: Constants.PageInfo.DesignPage.CLASS, 
									HTML: Constants.PageInfo.DesignPage.HTML
			    				};
			    	case Constants.PageInfo.DurabilityPage.PAGE:
			    	case Constants.PageInfo.DurabilityPage.CLASS:
			    	case Constants.PageInfo.DurabilityPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.DurabilityPage.PAGE, 
									CLASS: Constants.PageInfo.DurabilityPage.CLASS, 
									HTML: Constants.PageInfo.DurabilityPage.HTML
			    				};
			    	case Constants.PageInfo.SamsungPayPage.PAGE:
			    	case Constants.PageInfo.SamsungPayPage.CLASS:
			    	case Constants.PageInfo.SamsungPayPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.SamsungPayPage.PAGE, 
									CLASS: Constants.PageInfo.SamsungPayPage.CLASS, 
									HTML: Constants.PageInfo.SamsungPayPage.HTML
			    				};
			    	case Constants.PageInfo.SHealthPage.PAGE:
			    	case Constants.PageInfo.SHealthPage.CLASS:
			    	case Constants.PageInfo.SHealthPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.SHealthPage.PAGE, 
									CLASS: Constants.PageInfo.SHealthPage.CLASS, 
									HTML: Constants.PageInfo.SHealthPage.HTML
			    				};
			    	case Constants.PageInfo.PairedDecisionPage.PAGE:
			    	case Constants.PageInfo.PairedDecisionPage.CLASS:
			    	case Constants.PageInfo.PairedDecisionPage.HTML:
			    		return {
									PAGE: Constants.PageInfo.PairedDecisionPage.PAGE, 
									CLASS: Constants.PageInfo.PairedDecisionPage.CLASS, 
									HTML: Constants.PageInfo.PairedDecisionPage.HTML
			    				};
		    	}
		    	return null;
	    	} catch (err) {
	    		console.log("App.prototype.getPageInfo : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
		
		/*
		 * send interaction packet to sap module.
		 */
	    App.prototype.notifyInteraction = function (currentPage, interaction) {
	    	try {
		    	console.log("App.prototype.notifyInteraction)+ currentPage = " + currentPage + ", interaction = " + interaction);
				var packet = App.getInstance().packetBuilder
											  .header("NOTIFY_INTERACTION_TO_MOBILE")
											  .notifyInteraction(currentPage, interaction)
											  .build();
				App.getInstance().sap.fetch(packet);
	    	} catch (err) {
	    		console.log("App.prototype.notifyInteraction : exception [" + err.name + "] msg[" + err.message + "]");
	    	}
	    };
	    	    
		return App;
	})();
	RetailSolis.App = App;
})(RetailSolis || (RetailSolis = {}));

/*
 * start application 
 */
$(document).ready(function() {
	console.log("document.ready)+ ");
});