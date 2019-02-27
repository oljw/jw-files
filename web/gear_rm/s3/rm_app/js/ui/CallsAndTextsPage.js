/*
 * RetailSolis.ui.CallsAndTextsPage class
 * [created by Long on 10/06/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var CallsAndTextsPage = (function () {			
			function CallsAndTextsPage() {
				try {
					console.log("CallsAndTextsPage)+ ");
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					CallsAndTextsPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();

					this.init();
				} catch (err) {
					console.log("CallsAndTextsPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			CallsAndTextsPage.videoTextureView = null;

			
			CallsAndTextsPage.prototype.init = function() {
				try {
					console.log("CallsAndTextsPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.initializeFiles();
					
					var that = this;
					
					CallsAndTextsPage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					CallsAndTextsPage.videoTextureView.setChapter(CallsAndTextsPage.CLASS, Constants.CHAPTER_FOLDER + this.chapterFilePath, 
							function() {
								CallsAndTextsPage.videoTextureView.play();
							}, 
							function(err) {
								CallsAndTextsPage.videoTextureView.onerror(err);
							}
					);
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			CallsAndTextsPage.prototype.startAnimation = function(anim) {
				$("#callsAndTextsPage").animateCss(anim);
			};
			
			CallsAndTextsPage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("CallsAndTextsPage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/2.0_Calls_and_Texts_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/CallsAndTextsPage_chap.json";
					this.frameFilePath = "images/callsandtextspage/callsandtext_frame_sa.png";
				} else {
					console.log("CallsAndTextsPage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/2.0_Calls_and_Texts_Watch.mp4";
					this.chapterFilePath = "/connected/CallsAndTextsPage_chap.json";
					this.frameFilePath = "images/callsandtextspage/callsandtext_frame.png";
				}
			};
			
			/*
			 * class information
			 */
			CallsAndTextsPage.PAGE = Constants.PageInfo.CallsAndTextsPage.PAGE;
			CallsAndTextsPage.CLASS = Constants.PageInfo.CallsAndTextsPage.CLASS;
			CallsAndTextsPage.HTML = Constants.PageInfo.CallsAndTextsPage.HTML;
			CallsAndTextsPage.prototype.getPage = function () {
				try {
					return CallsAndTextsPage.PAGE;
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			CallsAndTextsPage.prototype.getClass = function () {
				try {
					return CallsAndTextsPage.CLASS;
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			CallsAndTextsPage.prototype.getHtml = function () {
				try {
					return CallsAndTextsPage.HTML;
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			CallsAndTextsPage.prototype.activate = function () {
				try {
					var element = document.getElementById("callsAndTextsPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			CallsAndTextsPage.prototype.deactivate = function () {
				try {
					CallsAndTextsPage.videoTextureView.stop();
					document.removeEventListener('rotarydetent', rotaryHandler);
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			
			CallsAndTextsPage.prototype.onerror = function (err) {
				console.log("CallsAndTextsPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			CallsAndTextsPage.prototype.getCurrentControl = function () {
				try {
					console.log("CallsAndTextsPage.prototype.getCurrentControl)+ ");
					return this.currentControl;
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			CallsAndTextsPage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("CallsAndTextsPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					this.currentControl = currentControl;
				} catch (err) {
					console.log("CallsAndTextsPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Chapter functions of demo
			 */
			CallsAndTextsPage.prototype.onStartDemo = function () {
				//CallsAndTextsPage.videoTextureView.play();
			};
			
			CallsAndTextsPage.onChapter_1 = function (chapter) {
				try {
					console.log("CallsAndTextsPage.onChapter_1)+ ");
					document.addEventListener('rotarydetent', rotaryHandler);
				} catch (err) {
					console.log("CallsAndTextsPage.onChapter_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
				
			};
			CallsAndTextsPage.onChapterEnded_1 = function (chapter) {
				try {
					console.log("CallsAndTextsPage.onChapterEnded_1)+ ");
					document.removeEventListener('rotarydetent', rotaryHandler);
				} catch (err) {
					console.log("CallsAndTextsPage.onChapterEnded_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			var rotaryHandler = function(event) {
				try {
					if(event.detail.direction === "CW") {
						CallsAndTextsPage.videoTextureView.seekToChapter(1, false);	//go to chapter 1 end
						var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(CallsAndTextsPage.PAGE, Constants.INTERACTIONS.ROTARY, "1", "ROTATE_BEZEL").build();
						RetailSolis.App.getInstance().fetchPacket(packet);
					}
				} catch (err) {
					console.log("CallsAndTextsPage.rotaryHandler : exception [" + err.name + "] msg[" + err.message + "]");
				}

			}
			
			return CallsAndTextsPage;
		})();
		ui.CallsAndTextsPage = CallsAndTextsPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### CallsAndTextsPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.CallsAndTextsPage());
console.log("##### CallsAndTextsPage Ready)- ");