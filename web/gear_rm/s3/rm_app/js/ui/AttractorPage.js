/*
 * RetailSolis.ui.AttractorPage class
 * [created by Long on 09/23/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var AttractorPage = (function () {
			
			function AttractorPage() {
				try {
					console.log("AttractorPage)+ ");
					this.init();
				} catch (err) {
					console.log("AttractorPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			AttractorPage.prototype.init = function() {
				try {
					console.log("AttractorPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
//					var videoTextureView = retail.videomanager.VideoTextureView.getInstance();
//					videoTextureView.setVideo("raw/GearS3_v3.9_ScreenburnFix.mp4", true);
//					videoTextureView.play();
//					
//					if (RetailSolis.App.getInstance().isStandaloneMode()) {
//						$("#video-container").click(function(){
//							RetailSolis.App.getInstance().changePage("js/ui/DecisionPage.html");
//						});
//					} else {
//						$("#video-container").click(function(){
//							RetailSolis.App.getInstance().changePage("js/ui/PairedDecisionPage.html");
//						});						
//					}
					
					// start attractor loop
					this.startAttractorLoop();
				    
					//set click listener
					var content = document.getElementById("attractor-content");				
					content.addEventListener("click", function(){
						var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(AttractorPage.PAGE, Constants.INTERACTIONS.TAP, null).build();
						RetailSolis.App.getInstance().fetchPacket(packet);
						
						RetailSolis.App.getInstance().changePage("js/ui/DecisionPage.html");
						
					});
					
					document.addEventListener("rotarydetent", this.rotaryEventHandler);
				} catch (err) {
					console.log("AttractorPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

            AttractorPage.prototype.rotaryEventHandler = function(event) {
				var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(AttractorPage.PAGE, Constants.INTERACTIONS.ROTARY, null).build();
				RetailSolis.App.getInstance().fetchPacket(packet);
				
				document.removeEventListener("rotarydetent", this.rotaryEventHandler);
				RetailSolis.App.getInstance().changePage("js/ui/DecisionPage.html");
			};
			
			AttractorPage.prototype.startAnimation = function(anim) {
				$('#attractorPage').animateCss(anim);
			};
			
			/*
			 * class information
			 */
			AttractorPage.PAGE = Constants.PageInfo.AttractorPage.PAGE;
			AttractorPage.CLASS = Constants.PageInfo.AttractorPage.CLASS;
			AttractorPage.HTML = Constants.PageInfo.AttractorPage.HTML;
			AttractorPage.prototype.getPage = function () {
				try {
					return AttractorPage.PAGE;
				} catch (err) {
					console.log("AttractorPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			AttractorPage.prototype.getClass = function () {
				try {
					return AttractorPage.CLASS;
				} catch (err) {
					console.log("AttractorPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			AttractorPage.prototype.getHtml = function () {
				try {
					return AttractorPage.HTML;
				} catch (err) {
					console.log("AttractorPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			AttractorPage.prototype.activate = function () {
				try {
					var element = document.getElementById("attractorPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("AttractorPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			AttractorPage.prototype.deactivate = function () {
				try {
					document.removeEventListener("rotarydetent", this.rotaryEventHandler);
					this.cancelAttractorLoop();
				} catch (err) {
					console.log("AttractorPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Start attractor loop.
			 */
			AttractorPage.prototype.startAttractorLoop = function() {
				try {
					if(!RetailSolis.App.getInstance().attractorInterval) {
						
						var i = 2;
						RetailSolis.App.getInstance().attractorInterval = setInterval(function() {
							src = 'images/attractorpage/' + i + '.png';
							$("#attractor-image-holder").attr('src', src);
							i++;
							if(i > 41) i = 1;
						}, 2000);
						
//						var i = 0;
//						var src = '';
//						var that = this;
//						RetailSolis.App.getInstance().attractorInterval = setInterval(function(){
//							if(i < 10) {
//								src = 'images/attractorpage/Full__0000' + i + '.png';
//							} else if(i < 100) {
//								src = 'images/attractorpage/Full__000' + i + '.png';
//							} else if(i < 1000) {
//								src = 'images/attractorpage/Full__00' + i + '.png';
//							} else {
//								src = 'images/attractorpage/Full__0' + i + '.png';
//							}
//							that.ImageExist(src);
//							i++;
//							if(i > 2594) i = 0;
//						}, 35);
					}
				} catch (err) {
					console.log("AttractorPage.prototype.startAttractorLoop : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Cancel attractor loop.
			 */
			AttractorPage.prototype.cancelAttractorLoop = function() {
				try {
					if(RetailSolis.App.getInstance().attractorInterval) {
						console.log("AttractorPage.prototype.cancelAttractorLoop: cancelling");
						clearInterval(RetailSolis.App.getInstance().attractorInterval);
						RetailSolis.App.getInstance().attractorInterval = null;
						console.log("AttractorPage.prototype.cancelAttractorLoop: cancelled");
					}
				} catch (err) {
					console.log("AttractorPage.prototype.cancelAttractorLoop : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
//			AttractorPage.prototype.ImageExist = function(url) {
//				$('<img src="'+ url +'">').load(function() {
//					$("#attractor-image-holder").attr('src', url);
//				    return true;
//				}).bind('error', function() {
//					console.log("FALSE");
//				    return false;
//				});
//			};
			
			
			return AttractorPage;
		})();
		ui.AttractorPage = AttractorPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));



console.log("##### AttractorPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.AttractorPage());
console.log("##### AttractorPage Ready)- ");