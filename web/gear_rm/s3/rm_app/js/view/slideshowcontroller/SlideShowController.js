/*
 * SlideShowController.js class
 * [created by JW on 12/05/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (view) {
		(function (slideshowcontroller) {
			var SlideShowController = (function () {
				function SlideShowController() {
					try {
						console.log("SlideShowController)+ ");
						this.init();

                        SlideShowController.slideIndex = 1;

                    } catch (err) {
						console.log("SlideShowController : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};

				/**
				 * Initialize SlideShowController
				 */
				SlideShowController.prototype.init = function() {
					try {
						console.log("SlideShowController.prototype.init");
						this.activate();

                        this.showSlides(SlideShowController.slideIndex);

                    } catch (err) {
						console.log("SlideShowController.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};

				/*
				 * getInstance function - Singleton
				 */
				SlideShowController.getInstance = function () {
					try {
						if (!SlideShowController.sInstance) {
							SlideShowController.sInstance = new SlideShowController();
						}
						return SlideShowController.sInstance;
					} catch (err) {
						console.log("SlideShowController.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				SlideShowController.sInstance = null;

				/**
				 * Activate SlideShowController
				 */
				SlideShowController.prototype.activate = function() {
					try {
						var element = document.getElementById("slideshow-container");
						var page = new tau.widget.Page(element);
						page.setActive(true);
					} catch (err) {
						console.log("SlideShowController.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};

                SlideShowController.prototype.showSlides = function(n) {
                    var i;
                    var slides = document.getElementsByClassName("mySlides");
                    var dots = document.getElementsByClassName("dot");

                    if (n > slides.length) {SlideShowController.slideIndex= 1}
                    if (n < 1) {SlideShowController.slideIndex= slides.length}
                    for (i = 0; i < slides.length; i++) {
                        slides[i].style.display = "none";
                    }
                    for (i = 0; i < dots.length; i++) {
                        dots[i].className = dots[i].className.replace(" active", "");
                    }
                    slides[SlideShowController.slideIndex-1].style.display = "block";
                    dots[SlideShowController.slideIndex-1].className += " active";
                };

                SlideShowController.prototype.plusSlides = function(n) {
                    this.showSlides(SlideShowController.slideIndex += n);
                };

                SlideShowController.prototype.currentSlide = function(n) {
                    this.showSlides(SlideShowController.slideIndex = n);
                };

				// /**
				//  * Set the list of image thumbnails
				//  * @param imgHolder
				//  */
				// SlideShowController.prototype.setThumbnails = function(imgHolder) {
				// 	try {
				// 		var tnHtml = null;
                //
				// 		for (var i=0; i < imgHolder.length; i++) {
				// 			if (i==0)
				// 				tnHtml = '<section class="ui-section-active"><div class="thumbnail" id="tn' + i + '"></div></section>';
				// 			else
				// 				tnHtml = '<section><div class="thumbnail" id="tn' + i + '"></div></section>';
                //
				// 			$("#thumbnailContainer").append(tnHtml);
				// 		}
				// 	} catch (err) {
				// 		console.log("SlideShowController.prototype.setThumbnails : exception [" + err.name + "] msg[" + err.message + "]");
				// 	}
				// };

				/**
				 * Show SlideShowController
				 */
				SlideShowController.prototype.showSlideShowController = function() {
					$("#content-container").load("js/view/slideshowcontroller/SlideShowController.html");
				};

				return SlideShowController;
			})();
			slideshowcontroller.SlideShowController = SlideShowController;
		})(RetailSolis.view.slideshowcontroller || (RetailSolis.view.slideshowcontroller = {}));
		var slideshowcontroller = RetailSolis.view.slideshowcontroller;
	})(RetailSolis.view || (RetailSolis.view = {}));
	var view = RetailSolis.view;
})(RetailSolis || (RetailSolis = {}));

console.log("##### ThumbnailPage Ready)+ ");
RetailSolis.App.getInstance().getCurrentPage().setCurrentControl(new RetailSolis.view.slideshowcontroller.SlideShowController());
console.log("##### ThumbnailPage Ready)- ");
