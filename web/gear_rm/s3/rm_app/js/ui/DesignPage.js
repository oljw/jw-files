/*
 * RetailSolis.ui.DesignPage class
 * [created by JW on 10/11/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var DesignPage = (function (_super) {
            __extends(DesignPage, _super);
            function DesignPage() {
				try {
					console.log("DesignPage)+ ");

                    _super.apply(this, arguments);

                    DesignPage.imageHolder = [
					                          	"images/designpage/design_noti4.png", "images/designpage/design_noti3.png",
					                          	"images/designpage/design_noti2.png", "images/designpage/design_noti1.png",
					                          	"images/designpage/design_frame.png",
					                            "images/designpage/design1.png", "images/designpage/design2.png",
					                            "images/designpage/design3.png", "images/designpage/design4.png"
					                          ];
					
					DesignPage.imageWidth = null;
					DesignPage.imageHeight = null;

					/* BASEPAGE USAGE EXAMPLE */
                    // PAGE = Constants.PageInfo.DesignPage.PAGE;
                    // CLASS = Constants.PageInfo.DesignPage.CLASS;
                    // HTML = Constants.PageInfo.DesignPage.HTML;
                    // pageId = "designPage";
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					
					DesignPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					
					this.init();

					/* BASEPAGE USAGE EXAMPLE */
                    // this.getPage();
                    // this.getClass();
                    // this.getHtml();
				} catch (err) {
					console.log("DesignPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

			DesignPage.videoTextureView = null;
			DesignPage.overlaySpriteView = null;

			DesignPage.prototype.init = function() {
				try {
					console.log("DesignPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.setChangerImages(DesignPage.imageHolder);
					this.setChangerImageSize(360, 360);
					this.initializeFiles();

					var that = this;
					
					DesignPage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					DesignPage.videoTextureView.setChapter(DesignPage.CLASS, Constants.CHAPTER_FOLDER + this.chapterFilePath,
							function() {
								DesignPage.videoTextureView.play();
							}, 
							function(err) {
								DesignPage.videoTextureView.onerror(err);
							}
					);
					
					$("#overlay-container").load("js/view/animation/Sprite.html");

				} catch (err) {
					console.log("DesignPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			DesignPage.prototype.startAnimation = function(anim) {
				$("#designPage").animateCss(anim);
			};
			
			DesignPage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("DesignPage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/1.0_Design_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/DesignPage_chap.json";
					this.frameFilePath = "images/designpage/design_frame_sa.png";
					console.log("videoFilePath: " + this.videoFilePath + " chapterFilePath: " + this.chapterFilePath);
				} else {
					console.log("DesignPage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/1.0_Design_Watch.mp4";
					this.chapterFilePath = "/connected/DesignPage_chap.json";
					this.frameFilePath = "images/designpage/design_frame.png";
					console.log("videoFilePath: " + this.videoFilePath + " chapterFilePath: " + this.chapterFilePath);
				}
			};
			
			/*
			 * Set ThumbnailChanger properties.
			 */
			DesignPage.prototype.setChangerImages = function(imgHolder) {
				try {
					DesignPage.imageHolder = imgHolder;
				} catch(err) {
					console.log("DesignPage.prototype.setChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DesignPage.prototype.getChangerImages = function() {
				try {
					return DesignPage.imageHolder;
				} catch(err) {
					console.log("DesignPage.prototype.getChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DesignPage.prototype.setChangerImageSize = function(width, height) {
				try {
					DesignPage.imageWidth = width;
					DesignPage.imageHeight = height;

				} catch(err) {
					console.log("DesignPage.prototype.setChangerImageSize : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DesignPage.prototype.getChangerImageWidth = function() {
				try {
					return DesignPage.imageWidth;
				} catch(err) {
					console.log("DesignPage.prototype.getChangerImageWidth : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DesignPage.prototype.getChangerImageHeight = function() {
				try {
					return DesignPage.imageHeight;
				} catch(err) {
					console.log("DesignPage.prototype.getChangerImageHeight : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * class information
			 */
			DesignPage.PAGE = Constants.PageInfo.DesignPage.PAGE;
			DesignPage.CLASS = Constants.PageInfo.DesignPage.CLASS;
			DesignPage.HTML = Constants.PageInfo.DesignPage.HTML;
			DesignPage.prototype.getPage = function () {
				try {
					return DesignPage.PAGE;
				} catch (err) {
					console.log("DesignPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DesignPage.prototype.getClass = function () {
				try {
					return DesignPage.CLASS;
				} catch (err) {
					console.log("DesignPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DesignPage.prototype.getHtml = function () {
				try {
					return DesignPage.HTML;
				} catch (err) {
					console.log("DesignPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			DesignPage.prototype.activate = function () {
				try {
					var element = document.getElementById("designPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("DesignPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DesignPage.prototype.deactivate = function () {
				try {
					DesignPage.videoTextureView.stop();
                    console.log("################# DEACTIVATE - DESIGNPAGE!!!!!!!!!!!!");

					RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();
					

					if (DesignPage.overlaySpriteView) {
						DesignPage.overlaySpriteView.stop();
					}
				} catch (err) {
					console.log("DesignPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DesignPage.prototype.onerror = function (err) {
				console.log("DesignPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			DesignPage.prototype.getCurrentControl = function () {
				try {
					console.log("DesignPage.prototype.getCurrentControl)+ ");
					return this.currentControl;
				} catch (err) {
					console.log("DesignPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DesignPage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("DesignPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					this.currentControl = currentControl;
				} catch (err) {
					console.log("DesignPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Chapter functions of demo
			 */
			DesignPage.prototype.onStartDemo = function () {
//				DesignPage.videoTextureView.play();
			};
			
			DesignPage.onChapter_1 = function (chapter) {
				try {
					console.log("DesignPage.onChapter_1)+ ");
					if (RetailSolis.App.getInstance().isStandaloneMode()) {
						DesignPage.videoTextureView.hideVideo();
						$("#content-container").load("js/view/thumbnailchanger/ThumbnailChanger.html");

						DesignPage.overlaySpriteView = RetailSolis.view.animation.Sprite.getInstance();					
						DesignPage.overlaySpriteView.setInfo(
								Constants.ANIMATION_INFO.ARROW.PATH,
								Constants.ANIMATION_INFO.ARROW.COUNT,
								Constants.ANIMATION_INFO.ARROW.FRAME_DURATION,
								1);	// 1 means looping once
						DesignPage.overlaySpriteView.start();

					} else {
						DesignPage.videoTextureView.showOverlayImage("images/designpage/tap_msg.png");
						DesignPage.videoTextureView.registerTapEvent(function() {
							DesignPage.videoTextureView.seekToChapter(1, false);
							var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(DesignPage.PAGE, Constants.INTERACTIONS.TAP, "1", "MESSAGE_BUTTON").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
						});
					}
				}
				catch (err) {
					console.log("DesignPage.onChapter_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DesignPage.onChapterEnded_1 = function (chapter) {
				try {
					console.log("DesignPage.onChapterEnded_1)+ ")
					if(RetailSolis.App.getInstance().isStandaloneMode()) {
						RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();
						$("#content-container").hide();
						DesignPage.videoTextureView.showVideo();
						
						if (DesignPage.overlaySpriteView) {
							DesignPage.overlaySpriteView.stop();
						}
					} else {
						DesignPage.videoTextureView.hideOverlayImage();
					}
				} catch (err) {
					console.log("DesignPage.onChapterEnded_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DesignPage.onChapter_2 = function (chapter) {
				try {
					console.log("DesignPage.onChapter_2)+ ");
					if(RetailSolis.App.getInstance().isStandaloneMode()) {
						//nothing
					} else {
						DesignPage.videoTextureView.hideVideo();
						$("#content-container").load("js/view/thumbnailchanger/ThumbnailChanger.html");
						
						DesignPage.overlaySpriteView = RetailSolis.view.animation.Sprite.getInstance();					
						DesignPage.overlaySpriteView.setInfo(
								Constants.ANIMATION_INFO.ARROW.PATH,
								Constants.ANIMATION_INFO.ARROW.COUNT,
								Constants.ANIMATION_INFO.ARROW.FRAME_DURATION,
								1);	// 1 means looping once
						DesignPage.overlaySpriteView.start();
					}
				}
				catch (err) {
					console.log("DesignPage.onChapter_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DesignPage.onChapterEnded_2 = function (chapter) {
				try {
					console.log("DesignPage.onChapterEnded_2)+ ")
					if(RetailSolis.App.getInstance().isStandaloneMode()) {
						//nothing
					} else {
						$("#content-container").hide();
						DesignPage.videoTextureView.showVideo();
						RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();

						if (DesignPage.overlaySpriteView) {
							DesignPage.overlaySpriteView.stop();
						}
					}
				} catch (err) {
					console.log("DesignPage.onChapterEnded_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return DesignPage;
		})(app.manager.ui.BasePage);
		ui.DesignPage = DesignPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### DesignPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.DesignPage());
console.log("##### DesignPage Ready)- ");