/*
 * RetailSolis.ui.SamsungPayPage class
 * [created by JW on 10/11/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var SamsungPayPage = (function () {
			function SamsungPayPage() {
				try {
					console.log("SamsungPayPage)+ ");
					SamsungPayPage.imageHolder = ["images/samsungpaypage/pay1.png", "images/samsungpaypage/pay2.png", 
					                             "images/samsungpaypage/pay3.png", "images/samsungpaypage/pay4.png"];
					
					SamsungPayPage.imageWidth = null;
					SamsungPayPage.imageHeight = null;
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					
					SamsungPayPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					
					this.init();
				} catch (err) {
					console.log("SamsungPayPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SamsungPayPage.videoTextureView = null;
			SamsungPayPage.overlaySpriteView = null;
			
			/*
			 * Init function
			 */
			SamsungPayPage.prototype.init = function() {
				try {
					console.log("SamsungPayPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.setChangerImages(SamsungPayPage.imageHolder);
					this.setChangerImageSize(360, 360);
					this.initializeFiles();
					
					var that = this;

					SamsungPayPage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					SamsungPayPage.videoTextureView.setChapter(SamsungPayPage.CLASS, Constants.CHAPTER_FOLDER + this.chapterFilePath,
							function() {
								SamsungPayPage.videoTextureView.play();
							}, 
							function(err) {
								SamsungPayPage.videoTextureView.onerror(err);
							}
					);
					
					$("#overlay-container").load("js/view/animation/Sprite.html");
				} catch (err) {
					console.log("SamsungPayPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			SamsungPayPage.prototype.startAnimation = function(anim) {
				$("#samsungPayPage").animateCss(anim);
			};
			
			SamsungPayPage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("SamsungPayPage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/3.0_Samsung_Pay_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/SamsungPayPage_chap.json";
					this.frameFilePath = "images/samsungpaypage/samsungpay_frame_sa.png";
					console.log("videoFilePath: " + this.videoFilePath + " chapterFilePath: " + this.chapterFilePath);
				} else {
					console.log("SamsungPayPage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/3.0_Samsung_Pay_Watch.mp4";
					this.chapterFilePath = "/connected/SamsungPayPage_chap.json";
					this.frameFilePath = "images/samsungpaypage/samsungpay_frame.png";
					console.log("videoFilePath: " + this.videoFilePath + " chapterFilePath: " + this.chapterFilePath);
				}
			};
			
			/*
			 * Set ThumbnailChanger properties.
			 */
			SamsungPayPage.prototype.setChangerImages = function(imgHolder) {
				try {
					SamsungPayPage.imageHolder = imgHolder;
				} catch(err) {
					console.log("SamsungPayPage.prototype.setChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.prototype.getChangerImages = function() {
				try {
					return SamsungPayPage.imageHolder;
				} catch(err) {
					console.log("SamsungPayPage.prototype.getChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SamsungPayPage.prototype.setChangerImageSize = function(width, height) {
				try {
					SamsungPayPage.imageWidth = width;
					SamsungPayPage.imageHeight = height;
				} catch(err) {
					console.log("SamsungPayPage.prototype.setChangerImageSize : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.prototype.getChangerImageWidth = function() {
				try {
					return SamsungPayPage.imageWidth;
				} catch(err) {
					console.log("SamsungPayPage.prototype.getChangerImageWidth : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SamsungPayPage.prototype.getChangerImageHeight = function() {
				try {
					return SamsungPayPage.imageHeight;
				} catch(err) {
					console.log("SamsungPayPage.prototype.getChangerImageHeight : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * class information
			 */
			SamsungPayPage.PAGE = Constants.PageInfo.SamsungPayPage.PAGE;
			SamsungPayPage.CLASS = Constants.PageInfo.SamsungPayPage.CLASS;
			SamsungPayPage.HTML = Constants.PageInfo.SamsungPayPage.HTML;
			SamsungPayPage.prototype.getPage = function () {
				try {
					return SamsungPayPage.PAGE;
				} catch (err) {
					console.log("SamsungPayPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.prototype.getClass = function () {
				try {
					return SamsungPayPage.CLASS;
				} catch (err) {
					console.log("SamsungPayPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.prototype.getHtml = function () {
				try {
					return SamsungPayPage.HTML;
				} catch (err) {
					console.log("SamsungPayPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			SamsungPayPage.prototype.activate = function () {
				try {
					var element = document.getElementById("samsungPayPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("SamsungPayPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.prototype.deactivate = function () {
				try {
					SamsungPayPage.videoTextureView.stop();
					RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();
					if (SamsungPayPage.overlaySpriteView) {
						SamsungPayPage.overlaySpriteView.stop();
					}
				} catch (err) {
					console.log("SamsungPayPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SamsungPayPage.prototype.onerror = function (err) {
				console.log("SamsungPayPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			SamsungPayPage.prototype.getCurrentControl = function () {
				try {
					console.log("SamsungPayPage.prototype.getCurrentControl)+ ");
					return SamsungPayPage.currentControl;
				} catch (err) {
					console.log("SamsungPayPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("SamsungPayPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					SamsungPayPage.currentControl = currentControl;
				} catch (err) {
					console.log("SamsungPayPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.currentControl = null;

			/*
			 * Chapter functions of demo
			 */
			SamsungPayPage.prototype.onStartDemo = function () {
//				SamsungPayPage.videoTextureView.play();
			};
			
			SamsungPayPage.onChapter_1 = function (chapter) {
				try {
					$("#content-container").load("js/view/thumbnailchanger/ThumbnailChanger.html");
					$("#content-container").on("click", function() {
						SamsungPayPage.videoTextureView.seekToChapter(2, false);
						var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(SamsungPayPage.PAGE, Constants.INTERACTIONS.TAP, "2", "CARD_SELECTION").build();
						RetailSolis.App.getInstance().fetchPacket(packet);
					});
				}
				catch (err) {
					console.log("SamsungPayPage.onChapter_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.onChapterEnded_1 = function (chapter) {
				try {
					$("#content-container").hide();
				} catch (err) {
					console.log("SamsungPayPage.onChapterEnded_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SamsungPayPage.onChapter_2 = function (chapter) {
				try {
					console.log("SamsungPayPage.onChapter_2)+ ");
					
					SamsungPayPage.overlaySpriteView = RetailSolis.view.animation.Sprite.getInstance();
					SamsungPayPage.overlaySpriteView.setInfo(
							Constants.ANIMATION_INFO.TAP.PATH,
							Constants.ANIMATION_INFO.TAP.COUNT,
							Constants.ANIMATION_INFO.TAP.FRAME_DURATION);
					SamsungPayPage.overlaySpriteView.setPosition(0, 120);
					SamsungPayPage.overlaySpriteView.start();
					
				} catch (err) {
					console.log("SamsungPayPage.onChapter_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.onChapterEnded_2 = function (chapter) {
				try {
					console.log("SamsungPayPage.onChapterEnded_2)+ ")
					
					if (SamsungPayPage.overlaySpriteView) {
						SamsungPayPage.overlaySpriteView.stop();
					}
					
				} catch (err) {
					console.log("SamsungPayPage.onChapterEnded_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SamsungPayPage.onChapter_3 = function (chapter) {
				try {
					console.log("SamsungPayPage.onChapter_3)+ ");
					RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();
					SamsungPayPage.videoTextureView.showOverlayImage("images/samsungpaypage/shield.png");
				} catch (err) {
					console.log("SamsungPayPage.onChapter_3 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SamsungPayPage.onChapterEnded_3 = function (chapter) {
				try {
					console.log("SamsungPayPage.onChapterEnded_3)+ ")
					SamsungPayPage.videoTextureView.hideOverlayImage();
				} catch (err) {
					console.log("SamsungPayPage.onChapterEnded_3 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return SamsungPayPage;
		})();
		ui.SamsungPayPage = SamsungPayPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### SamsungPayPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.SamsungPayPage());
console.log("##### SamsungPayPage Ready)- ");