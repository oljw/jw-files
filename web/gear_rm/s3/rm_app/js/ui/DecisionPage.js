/*
 * RetailSolis.ui.DecisionPage class
 * [created by Long on 10/10/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var DecisionPage = (function () {
			function DecisionPage() {
				try {
					console.log("DecisionPage)+ ");
					
					DecisionPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();

					DecisionPage.videoTextureView.showOverlayImage('images/decisionpage/start_img.png');
					
					this.init();
				} catch (err) {
					console.log("DecisionPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DecisionPage.self = null;
			DecisionPage.videoTextureView = null;
			DecisionPage.overlaySpriteView = null;
			
			DecisionPage.prototype.init = function() {
				try {
					console.log("DecisionPage.prototype.init)+ ");
					
					DecisionPage.self = this;
					this.activeItem = 0;
					this.clockwise = true;
					
					this.itemList = [
					                 $(".design-icon"),
					                 $(".call-icon"),
					                 $(".pay-icon"),
					                 $(".gps-icon"),
					                 $(".durability-icon"),
					                 $(".battery-icon"),
					                 $(".personalization-icon"),
					                 $(".s-health-icon")
					               ];
					
					this.itemTitleList = [
				                           'design',
				                           'call',
				                           'pay',
				                           'gps',
				                           'durability',
				                           'battery',
				                           'personalization',
				                           's_health'
			                             ];
					
					this.itemSecondaryTitleList = [
					                               'Access apps with steel bezel',
					                               'Make and answer calls and text',
					                               'Leave your phone and wallet at home',
					                               'Head out with onboard GPS',
					                               'Get military-grade performance',
					                               'Go for days without slowing down',
					                               'Customize your watch face',
					                               'Keep track of your workouts'
					                               ];
					
					$("#overlay-container").load("js/view/animation/Sprite.html");
					
					//set active for this page
					this.activate();
					
					//start 30 seconds timer
					this.startTimeout();
				} catch (err) {
					console.log("DecisionPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.startAnimation = function(anim) {
				$("#decisionPage").animateCss(anim);
			};
			
			/*
			 * class information
			 */
			DecisionPage.PAGE = Constants.PageInfo.DecisionPage.PAGE;
			DecisionPage.CLASS = Constants.PageInfo.DecisionPage.CLASS;
			DecisionPage.HTML = Constants.PageInfo.DecisionPage.HTML;
			DecisionPage.prototype.getPage = function () {
				try {
					return DecisionPage.PAGE;
				} catch (err) {
					console.log("DecisionPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DecisionPage.prototype.getClass = function () {
				try {
					return DecisionPage.CLASS;
				} catch (err) {
					console.log("DecisionPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DecisionPage.prototype.getHtml = function () {
				try {
					return DecisionPage.HTML;
				} catch (err) {
					console.log("DecisionPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Start 30 seconds timeout.
			 */
			DecisionPage.prototype.startTimeout = function() {
				try {
					if(!RetailSolis.App.getInstance().decisionTimeout) {
						RetailSolis.App.getInstance().decisionTimeout = setTimeout(function(){
							console.log("GOING BACK TO ATTRACTOR LOOP FROM 30 TIMEOUT");
							RetailSolis.App.getInstance().changePage("js/ui/AttractorPage.html");
						}, 30000);
					}
				} catch (err) {
					console.log("DecisionPage.prototype.startTimeout : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Cancel 30 seconds timeout.
			 */
			DecisionPage.prototype.cancelTimeout = function() {
				try {
					if(RetailSolis.App.getInstance().decisionTimeout) {
						clearTimeout(RetailSolis.App.getInstance().decisionTimeout);
						RetailSolis.App.getInstance().decisionTimeout = null;
					}
				} catch (err) {
					console.log("DecisionPage.prototype.cancelTimeout : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Restart 30 seconds timeout.
			 */
			DecisionPage.prototype.restartTimeout = function() {
				try {
					this.cancelTimeout();
					this.startTimeout();
				} catch (err) {
					console.log("DecisionPage.prototype.restartTimeout : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			DecisionPage.prototype.activate = function () {
				try {
					var element = document.getElementById("decisionPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
					
					this.selector = document.getElementById("selector");
					this.selectorComponent = null;

					var that = this;
					this.selectorComponent = tau.widget.Selector(this.selector, {'data-indicator-auto-control': 'false'});
					
					this.startInterval();
					
					this.startSlideAnimation();
					
					that.itemList[that.activeItem].css('background-image'
							, 'url("images/decisionpage/' + that.itemTitleList[that.activeItem] + '_icon_selected.png")');
					$("#decision-secondary-text").text(that.itemSecondaryTitleList[that.activeItem]);
					
					this.selector.addEventListener("click", function(event){
						
						that.stopInterval();
						
						var target = event.target;
						
						switch(target.getAttribute('data-title')) {
						case 'Design':
							that.decisionHandler(0);
							break;
						case 'Calls and text':
							that.decisionHandler(1);
							break;
						case 'Samsung Pay':
							that.decisionHandler(2);
							break;
						case 'Onboard GPS':
							that.decisionHandler(3);
							break;
						case 'Durability':
							that.decisionHandler(4);
							break;
						case 'Battery':
							that.decisionHandler(5);
							break;
						case 'Personalization':
							that.decisionHandler(6);
							break;
						case 'S Health':
							that.decisionHandler(7);
							break;								
						}
						
						if (target.classList.contains("ui-selector-indicator")) {
							that.decisionHandler(that.activeItem);
						}
						
					});
					
					this.selector.addEventListener("selectoritemchange", function(e){
						that.itemList[that.activeItem].css('background-image'
								, 'url("images/decisionpage/' + that.itemTitleList[that.activeItem] + '_icon.png")');
						that.activeItem = e.detail.index;
						that.itemList[that.activeItem].css('background-image'
								, 'url("images/decisionpage/' + that.itemTitleList[that.activeItem] + '_icon_selected.png")');
						$("#decision-secondary-text").text(that.itemSecondaryTitleList[that.activeItem]);					
					});
					
					document.addEventListener("rotarydetent", this.rotaryHandler);
					
				} catch (err) {
					console.log("DecisionPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.rotaryHandler = function(event) {
				try {
					// workaround - Keep showing the image and animation when we rotate
					if (!DecisionPage.self.selectorComponent.isEnabled()) {
						DecisionPage.videoTextureView.hideOverlayImage();
						if (DecisionPage.overlaySpriteView) {
							DecisionPage.overlaySpriteView.stop();
						}
						DecisionPage.self.selectorComponent.changeItem(0);
						DecisionPage.self.selectorComponent._enable();
					}

					DecisionPage.self.restartInterval();
					DecisionPage.self.restartTimeout();
				} catch (err) {
					console.log("DecisionPage.prototype.rotaryHandler : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			DecisionPage.prototype.deactivate = function () {
				try {
					this.cancelTimeout();
					this.stopInterval();
					this.selector.removeEventListener("click", this.clickBound, false);
					this.selectorComponent.destroy();
					document.removeEventListener("rotarydetent", this.rotaryHandler);
				} catch (err) {
					console.log("DecisionPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.startSlideAnimation = function(){
				try {
					var that = this;
					var items = document.getElementsByClassName('ui-item'), degree;
					
					for (i = 1; i < items.length; i++) {
						this.setItemTransform(items[i], -105, 140.4, -105, "scale(1)");
					}				
					
					setTimeout(function(){
						DecisionPage.videoTextureView.hideOverlayImage();
						for (var i = 1; i < items.length; i++) {
							degree = -105 + (30 * i);
							that.setItemTransform(items[i], degree, 140.4, -degree, "scale(1)");
						}
						that.selectorComponent.changeItem(0);
					},250);
				} catch (err) {
					console.log("DecisionPage.prototype.startSlideAnimation : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.setItemTransform = function(element, degree, radius, selfDegree, scale) {
				try {
					element.style.transform = "";
					element.style.transform = "rotate(" + degree + "deg) " +
					"translate3d(0, " + -radius + "px, 0) " +
					"rotate(" + selfDegree + "deg) " +
					scale;
				} catch (err) {
					console.log("DecisionPage.prototype.setItemTransform : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			DecisionPage.prototype.startInterval = function() {
				try {
					var counter = this.activeItem;
					var that = this;
					if(!RetailSolis.App.getInstance().pairedDecisionInterval) {
						RetailSolis.App.getInstance().pairedDecisionInterval = setInterval(function(){
							counter++;
							if(counter > 7) {
								DecisionPage.videoTextureView.showOverlayImage('images/decisionpage/menu_text.png');
								$("#image-container").animateCss('fadeIn');
								DecisionPage.overlaySpriteView = RetailSolis.view.animation.Sprite.getInstance();
								DecisionPage.overlaySpriteView.setInfo(
										Constants.ANIMATION_INFO.ARROW.PATH,
										Constants.ANIMATION_INFO.ARROW.COUNT,
										Constants.ANIMATION_INFO.ARROW.FRAME_DURATION, 1);
								DecisionPage.overlaySpriteView.start();
								counter = -1;
								that.selectorComponent._disable();
							} else if(that.selectorComponent) {
								// workaround - Keep showing the image and animation when we rotate
								if (!that.selectorComponent.isEnabled()) {
									that.selectorComponent.changeItem(0);
									that.selectorComponent._enable();
								}
								DecisionPage.videoTextureView.hideOverlayImage();
								that.selectorComponent.changeItem(counter);
							}
						}, 1500);
					}
				} catch (err) {
					console.log("DecisionPage.prototype.startInterval : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.restartInterval = function() {
				try {
					if(RetailSolis.App.getInstance().pairedDecisionInterval) {
						this.stopInterval();
						this.startInterval();
					}
				} catch (err) {
					console.log("DecisionPage.prototype.restartInterval : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.stopInterval = function() {
				try {
					if(RetailSolis.App.getInstance().pairedDecisionInterval) {
						clearInterval(RetailSolis.App.getInstance().pairedDecisionInterval);
						RetailSolis.App.getInstance().pairedDecisionInterval = null;
					}
				} catch (err) {
					console.log("DecisionPage.prototype.startInterval : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * This function helps to find the right demo to go to when users click
			 * the icons or title text in the middle of the screen.
			 */
			DecisionPage.prototype.decisionHandler = function(index) {
				try {
					switch(index) {
					case 0:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.DesignPage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.DesignPage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.DesignPage.HTML);
						}
						break;
					case 1:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.CallsAndTextsPage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.CallsAndTextsPage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.CallsAndTextsPage.HTML);
						}
						break;
					case 2:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.SamsungPayPage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.SamsungPayPage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.SamsungPayPage.HTML);
						}
						break;
					case 3:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.OnboardGPSPage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.OnboardGPSPage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.OnboardGPSPage.HTML);
						}
						break;
					case 4:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.DurabilityPage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.DurabilityPage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.DurabilityPage.HTML);
						}
						break;
					case 5:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.BatteryPage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.BatteryPage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.BatteryPage.HTML);
						}
						break;
					case 6:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.PersonalizePage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.PersonalizePage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.PersonalizePage.HTML);
						}
						break;
					case 7:
						if(RetailSolis.App.getInstance().isStandaloneMode()) {
							RetailSolis.App.getInstance().changePage(Constants.PageInfo.SHealthPage.HTML);
						} else {
							var packet = RetailSolis.App.getInstance().packetBuilder.changePage(Constants.PageInfo.SHealthPage.PAGE, "FORWARD").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
							this.startDemoTimer(Constants.PageInfo.SHealthPage.HTML);
						}
						break;
				}
				} catch (err) {
					console.log("DecisionPage.prototype.decisionHandler : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			DecisionPage.prototype.startDemoTimer = function(src) {
				if(!RetailSolis.App.getInstance().demoTimeout) {
					RetailSolis.App.getInstance().demoTimeout = setTimeout(function(){
						RetailSolis.App.getInstance().changePage(src);
					},2000);
				}
			}
			
			return DecisionPage;
		})();
		ui.DecisionPage = DecisionPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));



console.log("##### DecisionPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.DecisionPage());
console.log("##### DecisionPage Ready)- ");