/*
 * RetailSolis.ui.OnboardGPSPage class
 * [created by Long on 10/06/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var OnboardGPSPage = (function () {			
			function OnboardGPSPage() {
				try {
					console.log("OnboardGPSPage)+ ");
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					OnboardGPSPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					
					this.init();
					
				} catch (err) {
					console.log("OnboardGPSPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			OnboardGPSPage.videoTextureView = null;

			
			OnboardGPSPage.prototype.init = function() {
				try {
					console.log("OnboardGPSPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.initializeFiles();
					
					var that = this;
					
					OnboardGPSPage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					OnboardGPSPage.videoTextureView.setChapter(OnboardGPSPage.CLASS, Constants.CHAPTER_FOLDER + this.chapterFilePath, 
							function() {
								OnboardGPSPage.videoTextureView.play();
							}, 
							function(err) {
								OnboardGPSPage.videoTextureView.onerror(err);
							}
					);
				} catch (err) {
					console.log("OnboardGPSPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			OnboardGPSPage.prototype.startAnimation = function(anim) {
				$("#onboardGPSPage").animateCss(anim);
			};
			
			OnboardGPSPage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("OnboardGPSPage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/4.0_Onboard_GPS_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/OnboardGPSPage_chap.json";
					this.frameFilePath = "images/onboardgpspage/onboardgps_frame_sa.png";
				} else {
					console.log("OnboardGPSPage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/4.0_Onboard_GPS_Watch.mp4";
					this.chapterFilePath = "/connected/OnboardGPSPage_chap.json";
					this.frameFilePath = "images/onboardgpspage/onboardgps_frame.png";
				}
			};
			
			/*
			 * class information
			 */
			OnboardGPSPage.PAGE = Constants.PageInfo.OnboardGPSPage.PAGE;
			OnboardGPSPage.CLASS = Constants.PageInfo.OnboardGPSPage.CLASS;
			OnboardGPSPage.HTML = Constants.PageInfo.OnboardGPSPage.HTML;
			OnboardGPSPage.prototype.getPage = function () {
				try {
					return OnboardGPSPage.PAGE;
				} catch (err) {
					console.log("OnboardGPSPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			OnboardGPSPage.prototype.getClass = function () {
				try {
					return OnboardGPSPage.CLASS;
				} catch (err) {
					console.log("OnboardGPSPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			OnboardGPSPage.prototype.getHtml = function () {
				try {
					return OnboardGPSPage.HTML;
				} catch (err) {
					console.log("OnboardGPSPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			OnboardGPSPage.prototype.activate = function () {
				try {
					var element = document.getElementById("onboardGPSPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("OnboardGPSPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			OnboardGPSPage.prototype.deactivate = function () {
				try {
					OnboardGPSPage.videoTextureView.stop();
				} catch (err) {
					console.log("OnboardGPSPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			OnboardGPSPage.prototype.onerror = function (err) {
				console.log("OnboardGPSPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			OnboardGPSPage.prototype.getCurrentControl = function () {
				try {
					console.log("OnboardGPSPage.prototype.getCurrentControl)+ ");
					return this.currentControl;
				} catch (err) {
					console.log("OnboardGPSPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			OnboardGPSPage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("OnboardGPSPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					this.currentControl = currentControl;
				} catch (err) {
					console.log("OnboardGPSPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			
			/*
			 * Chapter functions of demo
			 */
			OnboardGPSPage.prototype.onStartDemo = function () {
				//OnboardGPSPage.videoTextureView.play();
			};
			
			OnboardGPSPage.onChapter_1 = function (chapter) {
				try {
					console.log("OnboardGPSPage.onChapter_1)+ ");
					// add click event
					$("#video-container").on('click',function(ev){
						if(ev.clientY >= 250) {
							OnboardGPSPage.videoTextureView.seekToChapter(1, false);
							var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(OnboardGPSPage.PAGE, Constants.INTERACTIONS.TAP, "1", "CHECKMARK_BUTTON_1").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
						}
					});
				} catch (err) {
					console.log("OnboardGPSPage.onChapter_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			OnboardGPSPage.onChapterEnded_1 = function (chapter) {
				try {
					console.log("OnboardGPSPage.onChapterEnded_1)+ ");
					$("#video-container").off('click');
				} catch (err) {
					console.log("OnboardGPSPage.onChapterEnded_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			OnboardGPSPage.onChapter_2 = function (chapter) {
				try {
					console.log("OnboardGPSPage.onChapter_2)+ ");
					// add click event
					$("#video-container").on('click',function(ev){
						if(ev.clientX >= 250) {
							OnboardGPSPage.videoTextureView.seekToChapter(2, false);
							var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(OnboardGPSPage.PAGE, Constants.INTERACTIONS.TAP, "2", "CHECKMARK_BUTTON_2").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
						}
					});
				} catch (err) {
					console.log("OnboardGPSPage.onChapter_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			OnboardGPSPage.onChapterEnded_2 = function (chapter) {
				try {
					console.log("OnboardGPSPage.onChapterEnded_2)+ ");
					$("#video-container").off('click');
				} catch (err) {
					console.log("OnboardGPSPage.onChapterEnded_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			
			return OnboardGPSPage;
		})();
		ui.OnboardGPSPage = OnboardGPSPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### OnboardGPSPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.OnboardGPSPage());
console.log("##### OnboardGPSPage Ready)- ");