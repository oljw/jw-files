/*
 * RetailSolis.ui.DurabilityPage class
 * [created by JW on 10/11/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var DurabilityPage = (function () {			
			function DurabilityPage() {
				try {
					console.log("DurabilityPage)+ ");
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					
					DurabilityPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					
					this.init();
				} catch (err) {
					console.log("DurabilityPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DurabilityPage.videoTextureView = null;
			
			DurabilityPage.prototype.init = function() {
				try {
					console.log("DurabilityPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.initializeFiles();
					
					var that = this;
					
					DurabilityPage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					DurabilityPage.videoTextureView.play();
				} catch (err) {
					console.log("DurabilityPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}

			DurabilityPage.prototype.startAnimation = function(anim) {
				$("#durabilityPage").animateCss(anim);
			};
			
			DurabilityPage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("DurabilityPage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/5.0_Water_Dust_Resistant_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/DurabilityPage_chap.json";
					this.frameFilePath = "images/durabilitypage/durability_frame_sa.png";
					console.log("videoFilePath: " + this.videoFilePath + " chapterFilePath: " + this.chapterFilePath);
				} else {
					console.log("DurabilityPage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/5.0_Water_Dust_Resistant_Watch.mp4";
					this.chapterFilePath = "/connected/DurabilityPage_chap.json";
					this.frameFilePath = "images/durabilitypage/durability_frame.png";
					console.log("videoFilePath: " + this.videoFilePath + " chapterFilePath: " + this.chapterFilePath);
				}
			};
			
			/*
			 * class information
			 */
			DurabilityPage.PAGE = Constants.PageInfo.DurabilityPage.PAGE;
			DurabilityPage.CLASS = Constants.PageInfo.DurabilityPage.CLASS;
			DurabilityPage.HTML = Constants.PageInfo.DurabilityPage.HTML;
			DurabilityPage.prototype.getPage = function () {
				try {
					return DurabilityPage.PAGE;
				} catch (err) {
					console.log("DurabilityPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DurabilityPage.prototype.getClass = function () {
				try {
					return DurabilityPage.CLASS;
				} catch (err) {
					console.log("DurabilityPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DurabilityPage.prototype.getHtml = function () {
				try {
					return DurabilityPage.HTML;
				} catch (err) {
					console.log("DurabilityPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Chapter functions of demo
			 */
			DurabilityPage.prototype.onStartDemo = function () {
//				DurabilityPage.videoTextureView.play();
			};
			
			/*
			 * activate / deactive function
			 */
			DurabilityPage.prototype.activate = function () {
				try {
					var element = document.getElementById("durabilityPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("DurabilityPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DurabilityPage.prototype.deactivate = function () {
				try {
					DurabilityPage.videoTextureView.stop();
				} catch (err) {
					console.log("DurabilityPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DurabilityPage.prototype.onerror = function (err) {
				console.log("DurabilityPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			DurabilityPage.prototype.getCurrentControl = function () {
				try {
					console.log("DurabilityPage.prototype.getCurrentControl)+ ");
					return this.currentControl;
				} catch (err) {
					console.log("DurabilityPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DurabilityPage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("DurabilityPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					this.currentControl = currentControl;
				} catch (err) {
					console.log("DurabilityPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return DurabilityPage;
		})();
		ui.DurabilityPage = DurabilityPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### DurabilityPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.DurabilityPage());
console.log("##### DurabilityPage Ready)- ");