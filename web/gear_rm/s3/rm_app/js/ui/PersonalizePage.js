/*
 * RetailSolis.ui.PersonalizePage class
 * [created by Long on 09/30/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var PersonalizePage = (function () {			
			function PersonalizePage() {
				try {
					console.log("PersonalizePage)+ ");
					
					PersonalizePage.imageHolder = ["images/personalizepage/01.png", "images/personalizepage/02.png", 
					                               "images/personalizepage/03.png", "images/personalizepage/04.png",
					                               "images/personalizepage/05.png", "images/personalizepage/06.png",
					                               "images/personalizepage/07.png"];
					
					PersonalizePage.imageWidth = null;
					PersonalizePage.imageHeight = null;
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					PersonalizePage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					this.init();
					
				} catch (err) {
					console.log("PersonalizePage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.videoTextureView = null;
			PersonalizePage.overlaySpriteView = null;

			PersonalizePage.prototype.init = function() {
				try {
					console.log("PersonalizePage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.initializeFiles();
					this.setChangerImages(PersonalizePage.imageHolder);
					this.setChangerImageSize(216, 216);
					
					var that = this;
					PersonalizePage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					PersonalizePage.videoTextureView.setChapter(PersonalizePage.CLASS, Constants.CHAPTER_FOLDER +this.chapterFilePath, 
							function() {
								PersonalizePage.videoTextureView.play();
							}, 
							function(err) {
								PersonalizePage.videoTextureView.onerror(err);
							}
					);
					
					
					$("#overlay-container").load("js/view/animation/Sprite.html");
					
				} catch (err) {
					console.log("PersonalizePage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			PersonalizePage.prototype.setChangerImages = function(imgHolder) {
				try {
					PersonalizePage.imageHolder = imgHolder;
				} catch(err) {
					console.log("PersonalizePage.prototype.setChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.prototype.getChangerImages = function() {
				try {
					return PersonalizePage.imageHolder;
				} catch(err) {
					console.log("PersonalizePage.prototype.getChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PersonalizePage.prototype.setChangerImageSize = function(width, height) {
				try {
					PersonalizePage.imageWidth = width;
					PersonalizePage.imageHeight = height;

				} catch(err) {
					console.log("PersonalizePage.prototype.setChangerImageSize : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PersonalizePage.prototype.getChangerImageWidth = function() {
				try {
					return PersonalizePage.imageWidth;
				} catch(err) {
					console.log("PersonalizePage.prototype.getChangerImageWidth : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PersonalizePage.prototype.getChangerImageHeight = function() {
				try {
					return PersonalizePage.imageHeight;
				} catch(err) {
					console.log("PersonalizePage.prototype.getChangerImageHeight : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PersonalizePage.prototype.startAnimation = function(anim) {
				$("#personalizePage").animateCss(anim);
			};
			
			PersonalizePage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("PersonalizePage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/7.0_Personalize_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/PersonalizePage_chap.json";
					this.frameFilePath = "images/personalizepage/personalize_frame_sa.png";
				} else {
					console.log("PersonalizePage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/7.0_Personalize_Watch.mp4";
					this.chapterFilePath = "/connected/PersonalizePage_chap.json";
					this.frameFilePath = "images/personalizepage/personalize_frame.png";
				}
			};
			
			/*
			 * class information
			 */
			PersonalizePage.PAGE = Constants.PageInfo.PersonalizePage.PAGE;
			PersonalizePage.CLASS = Constants.PageInfo.PersonalizePage.CLASS;
			PersonalizePage.HTML = Constants.PageInfo.PersonalizePage.HTML;
			PersonalizePage.prototype.getPage = function () {
				try {
					return PersonalizePage.PAGE;
				} catch (err) {
					console.log("PersonalizePage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.prototype.getClass = function () {
				try {
					return PersonalizePage.CLASS;
				} catch (err) {
					console.log("PersonalizePage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.prototype.getHtml = function () {
				try {
					return PersonalizePage.HTML;
				} catch (err) {
					console.log("PersonalizePage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			PersonalizePage.prototype.activate = function () {
				try {
					var element = document.getElementById("personalizePage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("PersonalizePage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.prototype.deactivate = function () {
				try {
					PersonalizePage.videoTextureView.stop();
					if (PersonalizePage.overlaySpriteView) {
						PersonalizePage.overlaySpriteView.stop();
					}
					RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();
					
					if (PersonalizePage.overlaySpriteView) {
						PersonalizePage.overlaySpriteView.stop();
					}
				} catch (err) {
					console.log("PersonalizePage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PersonalizePage.prototype.onerror = function (err) {
				console.log("PersonalizePage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			PersonalizePage.prototype.getCurrentControl = function () {
				try {
					console.log("PersonalizePage.prototype.getCurrentControl)+ ");
					return this.currentControl;
				} catch (err) {
					console.log("PersonalizePage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("PersonalizePage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					this.currentControl = currentControl;
				} catch (err) {
					console.log("PersonalizePage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			
			/*
			 * Chapter functions of demo
			 */
			PersonalizePage.prototype.onStartDemo = function () {
				//PersonalizePage.videoTextureView.play();
			};
			
			PersonalizePage.onChapter_1 = function (chapter) {
				try {
					console.log("PersonalizePage.onChapter_1)+ ");
					PersonalizePage.videoTextureView.showOverlayImage("images/personalizepage/hold_tap.png");
					PersonalizePage.videoTextureView.registerHoldEvent(function(){
						PersonalizePage.videoTextureView.seekToChapter(1, false);	//go to chapter 1 end
						var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(PersonalizePage.PAGE, Constants.INTERACTIONS.HOLD, "1", "TAP_HOLD_WATCHFACE").build();
						RetailSolis.App.getInstance().fetchPacket(packet);

						if (PersonalizePage.overlaySpriteView) {
							PersonalizePage.overlaySpriteView.stop();
						}
					});

					// better place?
					PersonalizePage.overlaySpriteView = RetailSolis.view.animation.Sprite.getInstance();
					PersonalizePage.overlaySpriteView.setInfo(
							Constants.ANIMATION_INFO.TAP.PATH,
							Constants.ANIMATION_INFO.TAP.COUNT,
							Constants.ANIMATION_INFO.TAP.FRAME_DURATION);
					PersonalizePage.overlaySpriteView.start();
		
				}
				catch (err) {
					console.log("PersonalizePage.onChapter_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.onChapterEnded_1 = function (chapter) {
				try {					
					PersonalizePage.videoTextureView.hideOverlayImage();
					PersonalizePage.videoTextureView.unregisterHoldEvent();
					PersonalizePage.videoTextureView.resumeVideo();

					if (PersonalizePage.overlaySpriteView) {
						PersonalizePage.overlaySpriteView.stop();
					}
				} catch (err) {
					console.log("PersonalizePage.onChapterEnded_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PersonalizePage.onChapter_2 = function (chapter) {
				try {
					console.log("PersonalizePage.onChapter_2)+ ");
					PersonalizePage.videoTextureView.hideVideo();
					$("#content-container").load("js/view/thumbnailchanger/ThumbnailChanger.html");
					
					// TODO is there better way?
					// getInstance again? <= for now (just in case that chapter 1 is skpped)
					PersonalizePage.overlaySpriteView = RetailSolis.view.animation.Sprite.getInstance();					
					PersonalizePage.overlaySpriteView.setInfo(
							Constants.ANIMATION_INFO.ARROW.PATH,
							Constants.ANIMATION_INFO.ARROW.COUNT,
							Constants.ANIMATION_INFO.ARROW.FRAME_DURATION,
							1);	// 1 means looping once
					PersonalizePage.overlaySpriteView.start();
				
				} catch (err) {
					console.log("PersonalizePage.onChapter_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PersonalizePage.onChapterEnded_2 = function (chapter) {
				try {
					console.log("PersonalizePage.onChapterEnded_2)+ ");
					PersonalizePage.videoTextureView.resumeVideo();
					$("#content-container").hide();
					RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();

					if (PersonalizePage.overlaySpriteView) {
						PersonalizePage.overlaySpriteView.stop();
					}
				} catch (err) {
					console.log("PersonalizePage.onChapterEnded_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return PersonalizePage;
		})();
		ui.PersonalizePage = PersonalizePage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### DemoVideoPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.PersonalizePage());
console.log("##### DemoVideoPage Ready)- ");