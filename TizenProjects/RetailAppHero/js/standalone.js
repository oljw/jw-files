var StandaloneApplication = function() {
	this.standaloneMenu = $('.standaloneMenu');
	this.closeButton = $('.closeButton');
	this.menuContainer = $('.standaloneMenu .scrollerContainer');
	this.menuPosition = $('.menuPosition');
	this.menuItems = $('.standaloneMenu img').length;
};

StandaloneApplication.prototype = {
		noInteractionTimeout: null,
		menuNoInteractionTimeout: 20000,
		menuAnimationTime: 80,
		menuIncrement: 360,
		exitScreenSelector: 'exitScreen',
		videoSuffix: 'Video',
		rotaryEvent: 'rotary',
		standaloneLabelSelector: 'standaloneLabel',
		standaloneMenu: null,
		closeButton: null,
		menuContainer: null,
		menuPosition: null,
		chapterTimeout: null,
		currentDemoIndex: 0,
		currentChapterIndex: 0,
		dismissEventType: null,
		exitTimeout: 3000,
		menuItems: null,
		currentMenuItem: 0,
		
		prepareVideos: function() {
			this.prepVideo('raw/upsm_stand.mp4', false);
			this.prepVideo('raw/battery_charge_stand.mp4', false);
		},
		
		/**
		 * Get a video ready only needed for standalone.
		 * @param src
		 * @param loop
		 */
		prepVideo: function(src, loop) {
			var name = src.replace(/\//g, '_');
			
			name = name.replace(/\./g, '_');				
			
			createVideo(name + this.videoSuffix, src, loop);
		},
		
		/**
		 * Start the no activity timeout.
		 */
		startNoActivityTimeout: function() {
			APPStandalone.noInterationTimeout = setTimeout('APPStandalone.noInteraction();', APPStandalone.menuNoInteractionTimeout);
		},
		
		/**
		 * Stop the no activity timeout.
		 */
		stopNoActivityTimeout: function() {
			clearTimeout(APPStandalone.noInterationTimeout);
		},
		
		/**
		 * What to do when there was no interaction while the standalone menu was showing.
		 */
		noInteraction: function() {
			APPStandalone.standaloneMenu.hide();
			
			APP.attractor.start();
		},
		
		/**
		 * Start the standalone run through.
		 */
		start: function() {
			//APPStandalone.startStandaloneAttractor();						
		},
		
		/**
		 * Handle video clicks on the attractor.
		 */
		handleVideoClick: function() {
			APP.attractor.stop();
			
			APPStandalone.showMenu();
		},
		
		/**
		 * Show the stand alone rotary menu.
		 */
		showMenu: function() {
			unloadVideos();
			
			hideAllVideos();
			
			APP.attractor.stop();
			
			APPStandalone.standaloneMenu.show();
			
			APPStandalone.menuContainer.css({marginLeft: 0});
			
			APPStandalone.currentMenuItem = 0;
			
			APPStandalone.closeButton.on('click', function() {
				APPStandalone.exitApplication();
			});
			
			APPStandalone.menuPosition.hide();
			
			APPStandalone.closeButton.hide();
			
			APPStandalone.startNoActivityTimeout();
			
			APP.turnScreenOn();
		},
		
		/**
		 * Show a demo!
		 * @param index
		 */
		showDemo: function(index) {
			APPStandalone.stopNoActivityTimeout();
			
			APPStandalone.standaloneMenu.hide();
			
			APPStandalone.startDemo(index);
			
			APP.turnScreenOn();
		},
		
		/**
		 * Stop the standalone run through.
		 */
		stop: function() {
			$('.demo, .chapter').hide();
			
			APPStandalone.dismissEventType = null;
			
			APPStandalone.standaloneMenu.hide();
			
			APPStandalone.closeButton.hide();
			
			hideAllVideos();
			
			APPStandalone.hideLabel();
			
			APPStandalone.currentDemoIndex = 0;
			APPStandalone.currentChapterIndex = 0;
			
			APPStandalone.stopNoActivityTimeout();
			
			clearTimeout(APPStandalone.chapterTimeout);
		},
		
		/**
		 * Start the first demo or label etc.
		 */
		startFirstDemo: function() {
			APP.attractor.stop();
			
			APPStandalone.startDemo(APPStandalone.currentDemoIndex);
		},
		
		/**
		 * Show a text/html label (outside of any demo chapter).
		 * @param labelHTML
		 */
		showLabel: function(labelHTML, keepDemo, advanceOnTap) {		
			if (!keepDemo) {
				$('.demo, .chapter').hide();
			}
			
			hideAllVideos();
			
			APPStandalone.hideLabel();
			
			var advance = "";
			
			if (advanceOnTap) {
				advance="APPStandalone.gotoNextChapterOrDemo();"
			}
			
			$('body').prepend('<div class="' + APPStandalone.standaloneLabelSelector + ' demo" onClick="' + advance + '">' + labelHTML + '</div>');
		},
		
		/**
		 * Show the leaving application screen.
		 */
		showExitScreen: function() {
			$('body').prepend('<div class="' + APPStandalone.exitScreenSelector + '"></div>');
		},
		
		/**
		 * Hide the leaving application screen.
		 */
		hideExitScreen: function() {
			$('.' + APPStandalone.exitScreenSelector).remove();
		},
		
		/**
		 * Exit the application after shutting down standalone stuff..
		 */
		exitApplication: function() {
			APPStandalone.stop();
			
			APPStandalone.showExitScreen();
			
			setTimeout(function() {
				APPStandalone.hideExitScreen();
				APP.exitApplication();
			}, APPStandalone.exitTimeout);
		},
		
		/**
		 * Hide the current labeling.
		 */
		hideLabel: function() {
			$('.' + APPStandalone.standaloneLabelSelector).remove();
		},
		
		/**
		 * Start a demo (by index).
		 * @param index
		 */
		startDemo: function(index) {
			APP.turnScreenOn();
			
			APPStandalone.currentChapterIndex = 0;
			
			var itemGroup = standaloneChapters[index];
			
			itemGroup.demo.preloadVideos();
			
			if (itemGroup.demo == batteryDemo) {
				preloadVideo('raw_battery_charge_stand_mp4Video');
	        	preloadVideo('raw_upsm_stand_mp4Video');
			}
			
			if (typeof(itemGroup) != 'undefined' && typeof(itemGroup.demo) != 'undefined') {
				itemGroup.demo.show();
				
				APPStandalone.hideLabel();
				APP.currentDemo = itemGroup.demo;
				APPStandalone.currentDemoIndex = index;
				
				while (itemGroup.chapters[APPStandalone.currentChapterIndex] == -1) {
					APPStandalone.currentChapterIndex++;
				}
				
				itemGroup.demo.startChapter(APPStandalone.currentChapterIndex, {});

				clearTimeout(APPStandalone.chapterTimeout);
				
				var timeLength = itemGroup.chapters[APPStandalone.currentChapterIndex];
				
				if (typeof(timeLength.label) != 'undefined') {
					timeLength = itemGroup.chapters[APPStandalone.currentChapterIndex].duration;
					
					itemGroup.demo.element.find(itemGroup.chapters[APPStandalone.currentChapterIndex].elementID).hide();
					
					APPStandalone.showLabel(itemGroup.chapters[APPStandalone.currentChapterIndex].label, true, itemGroup.chapters[APPStandalone.currentChapterIndex].advanceOnTap);
					
					if (typeof(itemGroup.chapters[APPStandalone.currentChapterIndex].dismissOnEvent) != 'undefined') {
						APPStandalone.dismissEventType = itemGroup.chapters[APPStandalone.currentChapterIndex].dismissOnEvent;
					}												
				} else if (typeof(timeLength.video) != 'undefined') {
					$('.demo').hide();
					
					noStart = true;

					showVideo(timeLength.video);
					
					timeLength = timeLength.duration;
				}
				
				APPStandalone.chapterTimeout = setTimeout('APPStandalone.chapterTimeoutReached()', timeLength);
			}
		}, 
		
		/**
		 * What to do when a chapter is over.
		 */
		chapterTimeoutReached: function() {
			APPStandalone.gotoNextChapterOrDemo();
		},
		
		/**
		 * Go to the next chapter, our hook on manual interaction like a tap or such.
		 */
		manualChapterSeek: function() {			
			APPStandalone.gotoNextChapterOrDemo();
		},
		
		/**
		 * Go to the next chapter in this demo, or onto the next demo or label..
		 */
		gotoNextChapterOrDemo: function() {
			APP.turnScreenOn();
			
			APPStandalone.dismissEventType = null;
			
			var itemGroup = standaloneChapters[APPStandalone.currentDemoIndex];
			
			APPStandalone.currentChapterIndex++;
			
			$('.' + APPStandalone.standaloneLabelSelector).remove();
			
			if (typeof(itemGroup.demo) != 'undefined') {
				if (typeof(itemGroup.chapters[APPStandalone.currentChapterIndex]) != 'undefined') {
					clearTimeout(APPStandalone.chapterTimeout);
					
					while (itemGroup.chapters[APPStandalone.currentChapterIndex] == -1) {
						APPStandalone.currentChapterIndex++;
					}
					
					APPStandalone.hideLabel();
					
					var timeLength = itemGroup.chapters[APPStandalone.currentChapterIndex];
					
					var noStart = false;
					
					if (typeof(timeLength.label) != 'undefined') {
						timeLength = itemGroup.chapters[APPStandalone.currentChapterIndex].duration;
						
						itemGroup.demo.element.find(itemGroup.chapters[APPStandalone.currentChapterIndex].elementID).hide();
						
						APPStandalone.showLabel(itemGroup.chapters[APPStandalone.currentChapterIndex].label, true, itemGroup.chapters[APPStandalone.currentChapterIndex].advanceOnTap);
						
						if (typeof(itemGroup.chapters[APPStandalone.currentChapterIndex].dismissOnEvent) != 'undefined') {
							APPStandalone.dismissEventType = itemGroup.chapters[APPStandalone.currentChapterIndex].dismissOnEvent;
						}			
						
						if (typeof(timeLength.noStart) != 'undefined') {
							noStart = true;
						}
					} else if (typeof(timeLength.video) != 'undefined') {
						$('.demo').hide();
						
						noStart = true;

						showVideo(timeLength.video);
						
						timeLength = timeLength.duration;
					}
					
					if (!noStart) {
						itemGroup.demo.startChapter(APPStandalone.currentChapterIndex, {});
					}
					
					APPStandalone.chapterTimeout = setTimeout('APPStandalone.chapterTimeoutReached()', timeLength);
				} else {
					$('.demo, .chapter').hide();
					
					APPStandalone.showMenu();
				}
			}
		},
		
		/**
		 * Handle abstract events..
		 * @param eventType
		 */
		onEvent: function(eventType, direction) {
			if (eventType == APPStandalone.dismissEventType) {
				APPStandalone.hideLabel();
			} else if (eventType == APPStandalone.rotaryEvent) {
				var totalItems = APPStandalone.menuItems,
					container = APPStandalone.menuContainer;

				if (APPStandalone.standaloneMenu.is(':visible')) {
					APPStandalone.stopNoActivityTimeout();
					APPStandalone.startNoActivityTimeout();
					
					//clockwise
					if (direction == 'CW') {
						APPStandalone.currentMenuItem++;
						
						if (APPStandalone.currentMenuItem + 1 > totalItems) {
							APPStandalone.currentMenuItem--;
							return;
						} else {
							APPStandalone.closeButton.show();
							
							var mVal = APPStandalone.currentMenuItem * APPStandalone.menuIncrement;

							container.stop(false,true);
							container.animate({marginLeft: -mVal}, APPStandalone.menuAnimationTime);
						}
					//counter-clockwise
					} else if (direction == 'CCW') {
						APPStandalone.currentMenuItem--;
						
						if (APPStandalone.currentMenuItem < 0) {
							APPStandalone.currentMenuItem++;
							
							return;
						} else {
							
							if (APPStandalone.currentMenuItem == 0) {
								APPStandalone.closeButton.hide();
							}
							
							var mVal = APPStandalone.currentMenuItem * APPStandalone.menuIncrement;

							container.stop(false,true);
							container.animate({marginLeft: -mVal}, APPStandalone.menuAnimationTime);
						}
					}
					
					if (APPStandalone.currentMenuItem > 0) {
						APPStandalone.menuPosition.show();
						
						APPStandalone.menuPosition.attr('class', 'menuPosition menuPosition' + APPStandalone.currentMenuItem);
					} else {
						APPStandalone.menuPosition.hide();
					}
				} else {
					APPStandalone.hideLabel();
				}
			}	
		}
};

APPStandalone = new StandaloneApplication();

setTimeout(function() {
	APPStandalone.prepareVideos();
}, 2000);

var standaloneChapters = [
        {
        	demo: designDemo,
        	chapters: [
        	          -1,
        	          3000,
        	          {	
        	        	  	elementID: '.rotateOverlay',
        	        	  	label: '<img src="images/standalone/design_rotate.png"/>',
        	          		duration: 12000,
        	          		dismissOnEvent: 'rotary'
        	          }
        	]
        },
        {
        	demo: accessDemo,
        	chapters: [
        	           -1,
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/access_rotateRight.png"/>',
	       	          		duration: 8000,
	       	          		dismissOnEvent: 'rotary'
	       	           },
        	           -1,
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/access_rotateLeft.png"/>',
	       	          		duration: 8000,
	       	          		dismissOnEvent: 'rotary'
	       	           },
	       	           {	
	       	        	  	label: '<img src="images/standalone/texttransition.png"/>',
	       	          		duration: 3000
	       	           },
        	           3000,
        	           3000,
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/access_tap.png"/>',
	       	          		duration: 8000,
	       	          		advanceOnTap: true
	       	           },
        	           3000       	           
        	]
        },
        {
        	demo: threeGDemo,
        	chapters: [
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/receivetext.png"/>',
	       	          		duration: 50
	       	           },
	       	           50,
        	           50,
        	           50,
        	           -1,
        	           50,
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/3g_rotate.png"/>',
	       	          		duration: 8000,
	       	          		dismissOnEvent: 'rotary'
	       	           },
        	           50,
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/3g_tap.png"/>',
	       	          		duration: 8000,
	       	          		advanceOnTap: true
	       	           },
        	           50,
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/3g_rotateMilk.png"/>',
	       	          		duration: 50000,
	       	          		dismissOnEvent: 'rotary'
	       	           },
        	           50
        	]
        },
        {
        	demo: fitnessDemo,
        	chapters: [
        	           -1,
        	           3000,
        	           {	
	       	        	  	elementID: '.overlay',
	       	        	  	label: '<img src="images/standalone/fitness_rotate.png"/>',
	       	          		duration: 8000,
	       	          		dismissOnEvent: 'rotary'
	       	           },
        	           3000,
        	           3000,
        	           3000
        	]
        },
        {
        	demo: batteryDemo,
        	chapters: [
        	           -1,
        	           3000,
        	           {
        	        	  video: 'raw_battery_charge_stand_mp4Video',
        	        	  duration: 5000
        	           },
        	           {
        	        	  video: 'raw_upsm_stand_mp4Video',
        	        	  duration: 7000
        	           }
        	]
        }
];