/*
 * RetailSolis.ui.SHealthPage class
 * [created by JW on 10/11/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var SHealthPage = (function (_super) {
            __extends(SHealthPage, _super);
            function SHealthPage() {
				try {
					console.log("SHealthPage)+ ");

                    _super.apply(this, arguments);

                    SHealthPage.imageHolder = ["images/shealthpage/shealth1.png", "images/shealthpage/shealth2.png",
					                           "images/shealthpage/shealth3.png", "images/shealthpage/shealth4.png",
					                           "images/shealthpage/shealth5.png", "images/shealthpage/shealth6.png"];
					
					SHealthPage.imageWidth = null;
					SHealthPage.imageHeight = null;
					
					this.videoFilePath = null;
					this.chapterFilePath = null;
					this.frameFilePath = null;
					this.currentControl = null;
					this.packetBuilder = retail.sapmanager.packet.PacketBuilder.getInstance();
					
					SHealthPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					
					this.init();
				} catch (err) {
					console.log("SHealthPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SHealthPage.videoTextureView = null;
			SHealthPage.overlaySpriteView = null;
			
			/*
			 * Init function
			 */
			SHealthPage.prototype.init = function() {
				try {
					console.log("SHealthPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					this.setChangerImages(SHealthPage.imageHolder);
					this.setChangerImageSize(360, 360);
					this.initializeFiles();
					
					var that = this;

					SHealthPage.videoTextureView.setVideo(this.videoFilePath, this.frameFilePath);
					SHealthPage.videoTextureView.setChapter(SHealthPage.CLASS, Constants.CHAPTER_FOLDER + this.chapterFilePath,
							function() {
								SHealthPage.videoTextureView.play();
							}, 
							function(err) {
								SHealthPage.videoTextureView.onerror(err);
							}
					);

					$("#overlay-container").load("js/view/animation/Sprite.html");
				} catch (err) {
					console.log("SHealthPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SHealthPage.prototype.startAnimation = function(anim) {
				$("#sHealthPage").animateCss(anim);
			};
			
			SHealthPage.prototype.initializeFiles = function() {
				if(RetailSolis.App.getInstance().isStandaloneMode()) {
					console.log("SHealthPage - isStandaloneMode: true");
					this.videoFilePath = "raw/standalone/8.0_Fitness_Tracking_Watch_sa.mp4";
					this.chapterFilePath = "/standalone/SHealthPage_chap.json";
					this.frameFilePath = "images/shealthpage/shealth_frame_sa.png";
				} else {
					console.log("SHealthPage - isStandaloneMode: false");
					this.videoFilePath = "raw/connected/8.0_Fitness_Tracking_Watch.mp4";
					this.chapterFilePath = "/connected/SHealthPage_chap.json";
					this.frameFilePath = "images/shealthpage/shealth_frame.png";
				}
			};
			
			/*
			 * Set ThumbnailChanger properties.
			 */
			SHealthPage.prototype.setChangerImages = function(imgHolder) {
				try {
					SHealthPage.imageHolder = imgHolder;
				} catch(err) {
					console.log("SHealthPage.prototype.setChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.prototype.getChangerImages = function() {
				try {
					return SHealthPage.imageHolder;
				} catch(err) {
					console.log("SHealthPage.prototype.getChangerImages : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SHealthPage.prototype.setChangerImageSize = function(width, height) {
				try {
					SHealthPage.imageWidth = width;
					SHealthPage.imageHeight = height;

				} catch(err) {
					console.log("SHealthPage.prototype.setChangerImageSize : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.prototype.getChangerImageWidth = function() {
				try {
					return SHealthPage.imageWidth;
				} catch(err) {
					console.log("SHealthPage.prototype.getChangerImageWidth : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SHealthPage.prototype.getChangerImageHeight = function() {
				try {
					return SHealthPage.imageHeight;
				} catch(err) {
					console.log("SHealthPage.prototype.getChangerImageHeight : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * class information
			 */
			SHealthPage.PAGE = Constants.PageInfo.SHealthPage.PAGE;
			SHealthPage.CLASS = Constants.PageInfo.SHealthPage.CLASS;
			SHealthPage.HTML = Constants.PageInfo.SHealthPage.HTML;
			SHealthPage.prototype.getPage = function () {
				try {
					return SHealthPage.PAGE;
				} catch (err) {
					console.log("SHealthPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.prototype.getClass = function () {
				try {
					return SHealthPage.CLASS;
				} catch (err) {
					console.log("SHealthPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.prototype.getHtml = function () {
				try {
					return SHealthPage.HTML;
				} catch (err) {
					console.log("SHealthPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			SHealthPage.prototype.activate = function () {
				try {
					var element = document.getElementById("sHealthPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("SHealthPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.prototype.deactivate = function () {
				try {
					SHealthPage.videoTextureView.stop();
					RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();
					
					if (SHealthPage.overlaySpriteView) {
						SHealthPage.overlaySpriteView.stop();
					}
				} catch (err) {
					console.log("SHealthPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SHealthPage.prototype.onerror = function (err) {
				console.log("SHealthPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			/*
			 * get/set currentControl
			 */
			SHealthPage.prototype.getCurrentControl = function () {
				try {
					console.log("SHealthPage.prototype.getCurrentControl)+ ");
					return this.currentControl;
				} catch (err) {
					console.log("SHealthPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.prototype.setCurrentControl = function (currentControl) {
				try {
					console.log("SHealthPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
					this.currentControl = currentControl;
				} catch (err) {
					console.log("SHealthPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * Chapter functions of demo
			 */
			SHealthPage.prototype.onStartDemo = function () {
//				SHealthPage.videoTextureView.play();
			};
			
			
			SHealthPage.onChapter_1 = function (chapter) {
				try {
					console.log("SHealthPage.onChapter_1)+ ");
					SHealthPage.videoTextureView.hideVideo();
					$("#content-container").load("js/view/thumbnailchanger/ThumbnailChanger.html");
					
					SHealthPage.overlaySpriteView = RetailSolis.view.animation.Sprite.getInstance();
					SHealthPage.overlaySpriteView.setInfo(
							Constants.ANIMATION_INFO.ARROW.PATH,
							Constants.ANIMATION_INFO.ARROW.COUNT,
							Constants.ANIMATION_INFO.ARROW.FRAME_DURATION,
							1);	// 1 means looping once
					SHealthPage.overlaySpriteView.start();

				}
				catch (err) {
					console.log("SHealthPage.onChapter_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.onChapterEnded_1 = function (chapter) {
				try {
					console.log("SHealthPage.onChapterEnded_1)+ ")
					$("#content-container").hide();
					SHealthPage.videoTextureView.showVideo();
					RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().removeRotaryEvent();
					
					if (SHealthPage.overlaySpriteView) {
						SHealthPage.overlaySpriteView.stop();
					}
				} catch (err) {
					console.log("SHealthPage.onChapterEnded_1 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			SHealthPage.onChapter_2 = function (chapter) {
				try {
					console.log("SHealthPage.onChapter_2)+ ");
					$("#video-container").one("click", function(ev){
						SHealthPage.videoTextureView.seekToChapter(2, false);
						var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(SHealthPage.PAGE, Constants.INTERACTIONS.TAP, "2", "HEARTRATE_BUTTON").build();
						RetailSolis.App.getInstance().fetchPacket(packet);
					});

				} catch (err) {
					console.log("SHealthPage.onChapter_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SHealthPage.onChapterEnded_2 = function (chapter) {
				try {
					console.log("SHealthPage.onChapterEnded_2)+ ")
				} catch (err) {
					console.log("SHealthPage.onChapterEnded_2 : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return SHealthPage;
		})(app.manager.ui.BasePage);
		ui.SHealthPage = SHealthPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### SHealthPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.SHealthPage());
console.log("##### SHealthPage Ready)- ");