/*
 * RetailSolis.ui.BatteryPage class
 * [created by Long on 10/06/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var BatteryPage = (function () {			
			function BatteryPage() {
				try {
					console.log("BatteryPage)+ ");
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					BatteryPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					
					this.init();
					
				} catch (err) {
					console.log("BatteryPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			BatteryPage.videoTextureView = null;

			
			BatteryPage.prototype.init = function() {
				try {
					console.log("BatteryPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.initializeFiles();
					
					var that = this;
					
					BatteryPage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					BatteryPage.videoTextureView.setChapter(BatteryPage.CLASS, Constants.CHAPTER_FOLDER + this.chapterFilePath, 
							function() {
								BatteryPage.videoTextureView.play();
							}, 
							function(err) {
								BatteryPage.videoTextureView.onerror(err);
							}
					);
				} catch (err) {
					console.log("BatteryPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			BatteryPage.prototype.startAnimation = function(anim) {
				$("#batteryPage").animateCss(anim);
			};
			
			BatteryPage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("BatteryPage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/6.0_Battery_Wireless_Charger_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/BatteryPage_chap.json";
					this.frameFilePath = "images/batterypage/battery_frame_sa.png";
				} else {
					console.log("BatteryPage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/6.0_Battery_Wireless_Charger_Watch.mp4";
					this.chapterFilePath = "/connected/BatteryPage_chap.json";
					this.frameFilePath = "images/batterypage/battery_frame.png";
					}
			};
			
			/*
			 * class information
			 */
			BatteryPage.PAGE = Constants.PageInfo.BatteryPage.PAGE;
			BatteryPage.CLASS = Constants.PageInfo.BatteryPage.CLASS;
			BatteryPage.HTML = Constants.PageInfo.BatteryPage.HTML;
			BatteryPage.prototype.getPage = function () {
				try {
					return BatteryPage.PAGE;
				} catch (err) {
					console.log("BatteryPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			BatteryPage.prototype.getClass = function () {
				try {
					return BatteryPage.CLASS;
				} catch (err) {
					console.log("BatteryPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			BatteryPage.prototype.getHtml = function () {
				try {
					return BatteryPage.HTML;
				} catch (err) {
					console.log("BatteryPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			BatteryPage.prototype.activate = function () {
				try {
					var element = document.getElementById("batteryPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("BatteryPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			BatteryPage.prototype.deactivate = function () {
				try {
					BatteryPage.videoTextureView.stop();
				} catch (err) {
					console.log("BatteryPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			BatteryPage.prototype.onerror = function (err) {
				console.log("BatteryPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			BatteryPage.prototype.getCurrentControl = function () {
				try {
					console.log("BatteryPage.prototype.getCurrentControl)+ ");
					return this.currentControl;
				} catch (err) {
					console.log("BatteryPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			BatteryPage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("BatteryPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					this.currentControl = currentControl;
				} catch (err) {
					console.log("BatteryPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			
			/*
			 * Chapter functions of demo
			 */
			BatteryPage.prototype.onStartDemo = function () {
				//BatteryPage.videoTextureView.play();
			};
			
			BatteryPage.onChapter_1 = function (chapter) {
				try {
					console.log("BatteryPage.onChapter_1)+ ");
					// add click event
					$("#video-container").on('click',function(ev){
						if(ev.clientX >= 250) {
							BatteryPage.videoTextureView.seekToChapter(1, false);
							var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(BatteryPage.PAGE, Constants.INTERACTIONS.TAP, "1", "CHECKMARK_BUTTON").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
						}
					});
				} catch (err) {
					console.log("BatteryPage.onChapter_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			BatteryPage.onChapterEnded_1 = function (chapter) {
				try {
					console.log("BatteryPage.onChapterEnded_1)+ ");
					$("#video-container").off('click');
				} catch (err) {
					console.log("BatteryPage.onChapterEnded_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return BatteryPage;
		})();
		ui.BatteryPage = BatteryPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### BatteryPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.BatteryPage());
console.log("##### BatteryPage Ready)- ");