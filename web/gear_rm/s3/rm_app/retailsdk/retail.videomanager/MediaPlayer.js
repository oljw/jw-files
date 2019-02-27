/* 
 * MediaPlayer.js class
 * [created by JW on 09/30/2016]
 *
 * THE SEQUENCE OF PLAYING A VIDEO:
 * 1. Prepare Video
 * 2. Create Video
 * 3. Preload Video
 * 4. Show Video
 * 5. Unload (remove) Video
 */

var retail;
(function (retail) {
	(function (videomanager) {
		var MediaPlayer = (function () {
			function MediaPlayer() {
				try {
					console.log("MediaPlayer)+ ");
	
					this.videoHTMLs = {};
					this.videoElement = null;
					this.currentVideoId = null;
					this.cachedVideoHTML = '';
					this.videoSuffix = 'Video';
				} catch (err) {
					console.log("MediaPlayer : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Create a video to play.
		     * Ex) createVideo('videoId', 'raw/src', 'boolean loopable'); 
		     * @param videoId
		     * @param src
		     * @param loop
		     */
			MediaPlayer.prototype.createVideo = function(videoId, src, frameSrc, loop) {
				try {
					if ($('#' + videoId).length || this.cachedVideoHTML.length > 1) {
						return;
					}
	
					var loopTag = "";
	
					if (loop) {
						loopTag = "loop";
					}
					
					if (this.videoElement) {
						$(this.videoElement).remove();
					}
					
					this.currentVideoId = videoId;
					var html = '<video xpreloadx="auto" style="display:none;" id="' + videoId + '" width="360" height="360" '+ 'poster="' + frameSrc + '" ' + loopTag + ' src="' + src + '"></video>';
					
					$('#video-container').click(false);
					
					this.videoHTMLs[videoId] = html;
	
					return $('#' + videoId);
				} catch (err) {
					console.log("MediaPlayer.prototype.createVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Prepare the video to play.
		     * This function invokes the createVideo inside and takes src and loop that is associated to createVideo().
		     * Replaces the src name into: ('raw_src_mp4Video')
		     * Ex) prepareVideo('raw/src', boolean loopable);
		     * @param src
		     * @param loop
		     */
			MediaPlayer.prototype.prepareVideo = function(src, frameSrc, loop) {
				try {
					var name = src.replace(/\//g, '_');
					name = name.replace(/\./g, '_');
					this.createVideo(name + this.videoSuffix, src, frameSrc, loop);
					this.preloadVideo(name + this.videoSuffix);
				} catch (err) {
					console.log("MediaPlayer.prototype.prepareVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Preload the video to play.
		     * Ex) preloadVideo('raw_src_mp4Video');
		     * @param videoId
		     */
			MediaPlayer.prototype.preloadVideo = function(videoId) {
				try {
					if (this.videoHTMLs[videoId] && !$('#' + videoId).length) {
						$('#video-container').append(this.videoHTMLs[videoId]);
					}
				} catch (err) {
					console.log("MediaPlayer.prototype.preloadVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Remove the video.
		     */
			MediaPlayer.prototype.removeVideo = function() {
				try {
					$('#video-container').empty();
				} catch (err) {
					console.log("MediaPlayer.prototype.removeVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Play the video.
		     * Ex) playVideo(currentVideoId);
		     * @param videoId
		     */
			MediaPlayer.prototype.playVideo = function(videoId) {
				try {
					this.videoElement = $('#' + videoId);
	
					if (this.videoElement && this.videoElement.length > 0) {
						try {
							this.videoElement.show();
							if(this.videoElement[0].currentTime > 0) this.videoElement[0].currentTime = 0;
							this.videoElement[0].play();
							
							//// TEST PURPOSE - Check the Time ////
//							this.videoElement[0].addEventListener("timeupdate", function(){
//								console.log("currentTime : "+ this.currentTime);
//							});
							///////////////////////////////
							
							this.onVideoEnded();
	
							return 1;
						} catch (e) {
							console.log("MediaPlayer => playVideo(): " + e.message);
							return 0;
						}
					} else {
						return 0;
					}
				} catch (err) {
					console.log("MediaPlayer.prototype.playVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

		    /**
		     * Seeks to the desired time of the video. (NOTE: TIME VALUE IS IN SECONDS; NOT MILLISECONDS)
		     * Ex) seekToTime(time_value_in_seconds);
		     * @param value
		     */
			MediaPlayer.prototype.seekToTime = function(targetTime) {
				try {
				    if( targetTime < 0 || targetTime > this.videoElement[0].duration) return;
				    this.videoElement[0].currentTime = targetTime;
				} catch (err) {
					console.log("MediaPlayer.prototype.seekToTime : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

		    /**
		     * Hide the current video.
		     */
			MediaPlayer.prototype.hideVideo = function() {
				try {
					this.videoElement.hide();
				} catch (err) {
					console.log("MediaPlayer.prototype.hideVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Show the current video.
		     */
			MediaPlayer.prototype.showVideo = function() {
				try {
					this.videoElement.show();
				} catch (err) {
					console.log("MediaPlayer.prototype.showVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Pause the current video.
		     */
			MediaPlayer.prototype.pauseVideo = function() {
				try {
					this.videoElement[0].pause();
				} catch (err) {
					console.log("MediaPlayer.prototype.pauseVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

		    /**
		     * Resume the current video.
		     */
			MediaPlayer.prototype.resumeVideo = function() {
				try {
					this.videoElement.show();
					this.videoElement[0].play();
				} catch (err) {
					console.log("MediaPlayer.prototype.resumeVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
		    /**
		     * Video Ended listener.
		     */
			MediaPlayer.prototype.onVideoEnded = function() {
				try {
					this.videoElement[0].addEventListener("ended", function(){
						/***** for animation testing purposes, can be removed later ******************/		        		
		        		if (RetailSolis.App.getInstance().isStandaloneMode()) {
							var nextPageInfo = RetailSolis.App.getInstance().getPageInfo(Constants.PageInfo.DecisionPage.PAGE);
			        		if (nextPageInfo == null) return;
			        		RetailSolis.App.getInstance().backAnimationHandler(nextPageInfo.HTML);
		        		} else {
		        			RetailSolis.App.getInstance().backAnimationHandler(Constants.PageInfo.AttractorPage.HTML);
		        		}
						/******************************************************************************/
					});
				} catch (err) {
					console.log("MediaPlayer.prototype.onVideoEnded : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/**
			 * Register tap and hold event on the current video
			 * @param callBack
			 */
			MediaPlayer.prototype.registerVideoHoldEvent = function(callBack) {
				try {
					var touchduration = 600; 
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
					 $("#video-container").on('touchstart',touchStart);
					 $("#video-container").on('touchend',touchEnd);
				} catch (err) {
					console.log("MediaPlayer.prototype.registerVideoHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/**
			 * Deregister/unregister the current tap and hold event
			 */
			MediaPlayer.prototype.unregisterVideoHoldEvent = function() {
				try {
					$("#video-container").off('touchstart');
					$("#video-container").off('touchend');
				} catch (err) {
					console.log("MediaPlayer.prototype.unregisterVideoHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/**
			 * Register a bezel rotary event on the current video
			 * @param cw
			 * @param ccw
			 */
			MediaPlayer.prototype.registerVideoRotaryEvent = function(cw, ccw) {
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
				    
//				    if(!imageContainer.is(":visible")) {
//				    	imageContainer.show();
//				    }
				} catch (err) {
					console.log("ImageControl.prototype.registerRotaryEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return MediaPlayer;
		})();
		videomanager.MediaPlayer = MediaPlayer;
	})(retail.videomanager || (retail.videomanager = {}));
	var videomanager = retail.videomanager;
})(retail || (retail = {}));

