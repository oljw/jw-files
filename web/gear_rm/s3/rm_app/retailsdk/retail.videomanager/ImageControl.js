/*
 * ImageControl.js class
 * [created by JW on 09/30/2016]
 */

var retail;
(function (retail) {
	(function (videomanager) {
			var ImageControl = (function () {
				function ImageControl() {
					try {
						console.log("ImageControl)+ ");
					} catch (err) {
						console.log("ImageControl : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				var imageContainer = null;

				/**
				 * Show overlay image on screen
				 * Ex) showOverlayImage("overlay_test.png");
				 * @param src
				 */
				ImageControl.prototype.showOverlayImage = function (src) {
					try {
						imageContainer = $("#image-container");
						imageContainer.empty();
						
					    $('<img src="' + src + '" id="overlayImage" width="360px" height="360px">').appendTo(imageContainer);
					    console.log('showOverlayImage: <img src="' + src + '" id="overlayImage">');

					    if(!imageContainer.is(":visible"))
					    	imageContainer.show();
					} catch (err) {
						console.log("ImageControl.prototype.showOverlayImage : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Hide overlay image from screen
				 */
				ImageControl.prototype.hideOverlayImage = function () {
					try {
						imageContainer.hide();
					} catch (err) {
						console.log("ImageControl.prototype.hideOverlayImage : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Register the bezel rotary event
				 * @param cw
				 * @param ccw
				 */
				ImageControl.prototype.registerRotaryEvent = function(cw, ccw) {
					try {
						var rotated =  false;
						document.addEventListener("rotarydetent", function(event){
							if(event.detail.direction === "CW") {
								// Clockwise
								if (rotated) return;
								rotated = true;
								cw();
							} else if (event.detail.direction === "CCW"){
								// Counter-Clockwise
								if (rotated) return;
								rotated = true;
								ccw();
							}
						});
					    
					    if(!imageContainer.is(":visible")) {
					    	imageContainer.show();
					    }
					} catch (err) {
						console.log("ImageControl.prototype.registerRotaryEvent : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Register the tap event of current overlay image
				 * @param callBack
				 */
				ImageControl.prototype.registerTapEvent = function(callBack) {
					try {
						imageContainer.on("click", function() {
							callBack();
						});
					} catch (err) {
						console.log("ImageControl.prototype.registerTapEvent : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Register the tap and hold event on current overlay image
				 * @param callBack
				 */
				ImageControl.prototype.registerHoldEvent = function(callBack) {
					try {
						var touchduration = 100; 
						var timerInterval;

						function timer(interval) {
						    interval--;

						    if (interval >= 0) {
						        timerInterval = setTimeout(function() {
		                            timer(interval);
		                        });
						    } else {
						        callBack();
						    }
						}

						function touchStart() {
						    timer(touchduration);
						}
						function touchEnd() {
						    clearTimeout(timerInterval);
						}
						imageContainer.on('touchstart',touchStart);
						imageContainer.on('touchend',touchEnd);
					} catch (err) {
						console.log("ImageControl.prototype.registerHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Deregister/unregister the current tap and hold event
				 */
				ImageControl.prototype.unregisterHoldEvent = function() {
					try {
					imageContainer.off('touchstart');
					imageContainer.off('touchend');
					} catch (err) {
						console.log("ImageControl.prototype.unregisterHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				return ImageControl;
			})();
			videomanager.ImageControl = ImageControl;
	})(retail.videomanager || (retail.videomanager = {}));
	var videomanager = retail.videomanager;
})(retail || (retail = {}));