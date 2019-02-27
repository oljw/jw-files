/*
 * ThumbnailChanger.js class
 * [created by JW on 09/30/2016]
 */

/* NOTE: THIS CONTROL NEEDS SERIOUS RECONSTRUCTION - JW */
/* WE SHOULD CREATE A VIEWCONTROLLER THAT CONTROLS MANY VIEWS */

var RetailSolis;
(function (RetailSolis) {
	(function (view) {
		(function (thumbnailchanger) {
			var ThumbnailChanger = (function () {
				function ThumbnailChanger() {
					try {
						console.log("ThumbnailChanger)+ ");
						this.init();
						
						this.imageHolder = null;
						this.imageWidth = null;
						this.imageHeight = null;
						
//						this.sectionChanger = null;
						
						this.isTextsEnabled = false;
												
						// FOR Personalization.. UGLY CODE TO BE CHANGED LATER
						if (RetailSolis.App.getInstance().getCurrentPage().getPage() === Constants.PageInfo.PersonalizePage.PAGE) {
							//("section").css('background-color', '#282828');
							this.texts = ['Steps Callenge 2','Chronogra+','Frontier','Urban Class','Active Class','Minimal', 'Gear Dashboard'];
							this.isTextsEnabled = true;
							$("#thumbnail-text").text(this.texts[0]);
							//$(".ui-footer").show();		
							$("#dummyBg").show();		
						} else {
							//$(".ui-footer").hide();
						}
					} catch (err) {
						console.log("ThumbnailChanger : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				var sectionChanger = null;
				var defaultPageIndex = null;	// bad.  it should be local variable
				
				/**
				 * Initialize ThumbnailChanger
				 */
				ThumbnailChanger.prototype.init = function() {
					try {
						console.log("ThumbnailChanger.prototype.init");
						this.activate();
						
						this.imageHolder = RetailSolis.App.getInstance().getCurrentPage().getChangerImages();
						this.imageWidth = RetailSolis.App.getInstance().getCurrentPage().getChangerImageWidth();
						this.imageHeight = RetailSolis.App.getInstance().getCurrentPage().getChangerImageHeight();
						
						this.setThumbnails(this.imageHolder);
						this.setCssAttributes(this.imageHolder, this.imageWidth, this.imageHeight);
						this.createThumbnailChanger();						
					} catch (err) {
						console.log("ThumbnailChanger.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * getInstance function - Singleton
				 */
				ThumbnailChanger.getInstance = function () {
					try {
						if (!ThumbnailChanger.sInstance) {
							ThumbnailChanger.sInstance = new ThumbnailChanger();
						}
						return ThumbnailChanger.sInstance;
					} catch (err) {
						console.log("ThumbnailChanger.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				ThumbnailChanger.sInstance = null;
				
				/**
				 * Activate ThumbnailChanger
				 */
				ThumbnailChanger.prototype.activate = function() {
					try {
						var element = document.getElementById("pageIndicatorCirclePage");
						var page = new tau.widget.Page(element);
						page.setActive(true);
					} catch (err) {
						console.log("ThumbnailChanger.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Create the ThumbnailChanger
				 */
				ThumbnailChanger.prototype.createThumbnailChanger = function() {
					try {
						var changer = document.getElementById("sectionChanger");
						var sections = document.querySelectorAll("section");
						
						var elPageIndicator = document.getElementById("pageIndicator");
						var pageIndicator;
						var pageIndicatorHandler;
						
						var that = this;
						defaultPageIndex = null;
						
						// Make PageIndicator
						pageIndicator = tau.widget.PageIndicator(elPageIndicator, { numberOfPages: sections.length });
						pageIndicator.setActive(0);
						
						// Make SectionChanger object
						sectionChanger = new tau.widget.SectionChanger(changer, {
							circular: false,
							orientation: "horizontal",
							useBouncingEffect: true,
							fillContent: false
						});
						
						// Rotary event
						document.addEventListener("rotarydetent", this.rotaryEventHandler);

						// FOR SAMSUNG PAY.. UGLY CODE TO BE CHANGED LATER
						if (RetailSolis.App.getInstance().getCurrentPage().getPage() == Constants.PageInfo.SamsungPayPage.PAGE) {
							elPageIndicator.style.display = "none";
							changer.addEventListener("click", function() {
								sectionChanger.setActiveSection(sectionChanger.getActiveSectionIndex());
								ThumbnailChanger.getInstance().removeRotaryEvent();
							});
						}
						
						// FOR DESIGN PAGE.. UGLY CODE TO BE CHANGED LATER						
						if (RetailSolis.App.getInstance().getCurrentPage().getPage() == Constants.PageInfo.DesignPage.PAGE) {
							sectionChanger.setActiveSection(4);
							pageIndicator.setActive(4);
							defaultPageIndex = 4;
						}
						
						if(RetailSolis.App.getInstance().getCurrentPage().getPage() === Constants.PageInfo.PersonalizePage.PAGE) {
							changer.addEventListener('click', function(){
								$("#tn" + sectionChanger.getActiveSectionIndex()).animate({
									height: '+=30px',
									width: '+=30px'
								}).delay(500).animate({
									height: '-=30px',
									width: '-=30px'
								});
							});
						}
						

						// Sectionchange event handler
						pageIndicatorHandler = function (e) {
							pageIndicator.setActive(e.detail.active);
							if(RetailSolis.App.getInstance().getCurrentPage().getPage() === Constants.PageInfo.PersonalizePage.PAGE) {
								$("#thumbnail-text").text(that.texts[sectionChanger.getActiveSectionIndex()]);
							}
						};
						
						// if defaultPageIndex is available, show the check dot.  Otherwise, hide it.
						if (defaultPageIndex) {
							$("#pageIndicatorMark").show();
						} else {
							$("#pageIndicatorMark").hide();
						}

						changer.addEventListener("sectionchange", pageIndicatorHandler, false);
					} catch (err) {
						console.log("ThumbnailChanger.prototype.createThumbnailChanger : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				ThumbnailChanger.prototype.rotaryEventHandler = function(event) {
					try {
						var chapterIndex = retail.videomanager.Chapter.getInstance().chapterIndex;
						
						if( event.detail.direction === "CW") {
							sectionChanger.setActiveSection(sectionChanger.getActiveSectionIndex() + 1, 500);

							// Rotary interaction packet for Analytics.
							var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(RetailSolis.App.getInstance().currentPage.getPage(), Constants.INTERACTIONS.ROTARY, chapterIndex, "BEZEL_TO_RIGHT").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
						} else {
							sectionChanger.setActiveSection(sectionChanger.getActiveSectionIndex() - 1, 500);

							// Rotary interaction packet for Analytics.
							var packet = RetailSolis.App.getInstance().packetBuilder.notifyInteraction(RetailSolis.App.getInstance().currentPage.getPage(), Constants.INTERACTIONS.ROTARY, chapterIndex, "BEZEL_TO_LEFT").build();
							RetailSolis.App.getInstance().fetchPacket(packet);
						}
						
						if (defaultPageIndex) {
							var selectedIndex = sectionChanger.getActiveSectionIndex();
							var src;
							if (selectedIndex == defaultPageIndex) {
								src = "images/thumbnailchanger/selected_dot.png";
							} else {
								src = "images/thumbnailchanger/unselected_dot.png";
							}
							$("#pageIndicatorMark").attr('src', src);
						}
					} catch (err) {
						console.log("ThumbnailChanger.prototype.rotaryEventHandler : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				ThumbnailChanger.prototype.removeRotaryEvent = function() {
					try {
						document.removeEventListener("rotarydetent", this.rotaryEventHandler);
					} catch (err) {
						console.log("ThumbnailChanger.prototype.removeRotaryEvent : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
//				/**
//				 * Set true/false to disable rotary event
//				 * @param disable
//				 */
//				ThumbnailChanger.prototype.setDisableRotaryEvent = function(disable) {
//					ThumbnailChanger.disableRotaryEvent = disable;					
//				}
				
				/**
				 * Set the list of image thumbnails
				 * @param imgHolder
				 */
				ThumbnailChanger.prototype.setThumbnails = function(imgHolder) {
					try {
						var tnHtml = null;
						
						for (var i=0; i < imgHolder.length; i++) {
							if (i==0) 
								tnHtml = '<section class="ui-section-active"><div class="thumbnail" id="tn' + i + '"></div></section>';
							else 
								tnHtml = '<section><div class="thumbnail" id="tn' + i + '"></div></section>';
							
							$("#thumbnailContainer").append(tnHtml);
						};
					} catch (err) {
						console.log("ThumbnailChanger.prototype.setThumbnails : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Modify and set the CSS attributes of the current ThumbnailChanger
				 * @param imgHolder
				 * @param imgWidth
				 * @param imgHeight
				 */
				ThumbnailChanger.prototype.setCssAttributes = function(imgHolder, imgWidth, imgHeight) {
					try {
						$(".thumbnail").css({"width": imgWidth + "px"});
						$(".thumbnail").css({"height": imgHeight + "px"});
						
						for (var i=0; i < imgHolder.length; i++) {
							$("#tn" + i).css("background-image", 'url("' + imgHolder[i] + '")');
							$("#tn" + i).css("background-size", 'contain');
							$("#tn" + i).css("z-index", '16000');
						}
					} catch (err) {
						console.log("ThumbnailChanger.prototype.setCssAttributes : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Show ThumbnailChanger
				 */
				ThumbnailChanger.prototype.showThumbnailChanger = function() {
					$("#content-container").load("js/view/thumbnailchanger/ThumbnailChanger.html");
				};
				
				return ThumbnailChanger;
			})();
			thumbnailchanger.ThumbnailChanger = ThumbnailChanger;
		})(RetailSolis.view.thumbnailchanger || (RetailSolis.view.thumbnailchanger = {}));
		var thumbnailchanger = RetailSolis.view.thumbnailchanger;
	})(RetailSolis.view || (RetailSolis.view = {}));
	var view = RetailSolis.view;
})(RetailSolis || (RetailSolis = {}));

console.log("##### ThumbnailPage Ready)+ ");
RetailSolis.App.getInstance().getCurrentPage().setCurrentControl(new RetailSolis.view.thumbnailchanger.ThumbnailChanger());
console.log("##### ThumbnailPage Ready)- ");
