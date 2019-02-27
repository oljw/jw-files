/* 
 * VideoTextureView.js class
 * [created by JW on 09/30/2016]
 */

var retail;
(function (retail) {
	(function (videomanager) {
		var VideoTextureView = (function () {
			function VideoTextureView() {
				try {
					console.log("VideoTextureView)+ ");
									
					if (VideoTextureView.sInstance)
						return VideoTextureView.sInstance;
					VideoTextureView.sInstance = this;
					
					this.getMediaPlayer();
					this.getImageControl();
	
					this.chapter = retail.videomanager.Chapter.getInstance();
				} catch (err) {
					console.log("VideoTextureView : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/**
			 * Get VideoTextureView instance.
			 */
			VideoTextureView.getInstance = function () {
				try {
					if (!VideoTextureView.sInstance) {
						VideoTextureView.sInstance = new VideoTextureView();
					}
					return VideoTextureView.sInstance;
				} catch (err) {
					console.log("VideoTextureView.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			VideoTextureView.sInstance = null;
			
		    /**
		     * Retrieve the MediaPlayer instance.
		     */
			VideoTextureView.prototype.getMediaPlayer = function() {
				try {
					if (this.mediaPlayer)
						return this.mediaPlayer;
					this.mediaPlayer = new retail.videomanager.MediaPlayer();
					return this.mediaPlayer;
				} catch (err) {
					console.log("VideoTextureView.prototype.getMediaPlayer : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			VideoTextureView.mediaPlayer = null;
			
		    /**
		     * Retrieve the ImageControl instance.
		     */
			VideoTextureView.prototype.getImageControl = function() {
				try {
					if (this.imageControl)
						return this.imageControl;
					this.imageControl = new retail.videomanager.ImageControl();
					return this.imageControl;
				} catch (err) {
					console.log("VideoTextureView.prototype.getImageControl : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			VideoTextureView.imageControl = null;
			
			
			/*
			 * Video functions
			 */
			/**
			 * Play function that plays both video and chapter
			 */
			VideoTextureView.prototype.play = function() {
				try {
					this.playVideo();
					this.playChapter();
				} catch (err) {
					console.log("VideoTextureView.prototype.play : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			/**
			 * Stopping the current VideoTextureView
			 */
			VideoTextureView.prototype.stop = function() {
				try {
					this.removeVideo();
					this.stopChapter();
				} catch (err) {
					console.log("VideoTextureView.prototype.stop : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

			/**
			 * Set video file to a MediaPlayer
			 * @param videoSrc
			 * @param frameSrc
			 * @param loop
			 */
			VideoTextureView.prototype.setVideo = function(videoSrc, frameSrc, loop) {
				try {
					this.getMediaPlayer().prepareVideo(videoSrc, frameSrc, loop);
				} catch (err) {
					console.log("VideoTextureView.prototype.setVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Play the video.
		     * Ex) playVideo("raw/test_video.mp4", loopable);
		     */
			VideoTextureView.prototype.playVideo = function() {
				try {
					this.getMediaPlayer().playVideo(this.getMediaPlayer().currentVideoId);
				} catch (err) {
					console.log("VideoTextureView.prototype.playVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Pause the current video.
		     */
			VideoTextureView.prototype.pauseVideo = function() {
				try {
					this.getMediaPlayer().pauseVideo();
				} catch (err) {
					console.log("VideoTextureView.prototype.pauseVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Hide the current video.
		     */
			VideoTextureView.prototype.hideVideo = function() {
				try {
					this.getMediaPlayer().hideVideo();
				} catch (err) {
					console.log("VideoTextureView.prototype.hideVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			/**
			 * Show the current video.
			 */
			VideoTextureView.prototype.showVideo = function() {
				try {
					this.getMediaPlayer().showVideo();
				} catch (err) {
					console.log("VideoTextureView.prototype.hideVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Resume the current video.
		     */
			VideoTextureView.prototype.resumeVideo = function() {
				try {
					this.getMediaPlayer().resumeVideo();
				} catch (err) {
					console.log("VideoTextureView.prototype.resumeVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Seek to the desired time of the video. (NOTE: TIME VALUE IS IN SECONDS; NOT MILLISECONDS)
		     * Ex) seekToTime(time_value_in_seconds);
		     * @param timeValue
		     */
			VideoTextureView.prototype.seekToTime = function(timeValue) {
				try {
					this.getMediaPlayer().seekToTime(timeValue);
				} catch (err) {
					console.log("VideoTextureView.prototype.seekToTime : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Remove the current video.
		     */
			VideoTextureView.prototype.removeVideo = function() {
				try {
					this.getMediaPlayer().removeVideo();
				} catch (err) {
					console.log("VideoTextureView.prototype.removeVideo : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			
			/*
			 * Image control functions
			 */
		    /**
		     * Show overlay image.
		     * Ex) showOverlayImage("overlay_test.png");
		     * @param src
		     */
			VideoTextureView.prototype.showOverlayImage = function (src) {
				try {
					this.getImageControl().showOverlayImage(src);
				} catch (err) {
					console.log("VideoTextureView.prototype.showOverlayImage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Hide the current overlay image.
		     */
			VideoTextureView.prototype.hideOverlayImage = function () {
				try {
					this.getImageControl().hideOverlayImage();
				} catch (err) {
					console.log("VideoTextureView.prototype.hideOverlayImage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			
			/*
			 * Event register/deregister functions
			 */
		    /**
		     * Register the rotary event on the current overlay image.
		     * @param cw
		     * @param ccw
		     */
			VideoTextureView.prototype.registerRotaryEvent = function (cw, ccw) {
				try {
					this.getImageControl().registerRotaryEvent(cw, ccw);
				} catch (err) {
					console.log("VideoTextureView.prototype.registerRotaryEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			 /**
		     * Register the rotary event on the current video.
		     * @param cw
		     * @param ccw
		     */
			VideoTextureView.prototype.registerVideoRotaryEvent = function (cw, ccw) {
				try {
					this.getMediaPlayer().registerVideoRotaryEvent(cw, ccw);
				} catch (err) {
					console.log("VideoTextureView.prototype.registerRotaryEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
 			/**
		     * Register the tap and hold event on the current video.
		     * @param callback
		     */
			VideoTextureView.prototype.registerVideoHoldEvent = function (callback) {
				try {
					this.getMediaPlayer().registerVideoHoldEvent(callback);
				} catch (err) {
					console.log("VideoTextureView.prototype.registerHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			/**
		     * Deregister/unregister the hold event for video.
		     */
			VideoTextureView.prototype.unregisterVideoHoldEvent = function () {
				try {
					this.getMediaPlayer().unregisterVideoHoldEvent();
				} catch (err) {
					console.log("VideoTextureView.prototype.unregisterHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
 			};
		    /**
		     * Register the tap event on the current overlay image.
		     * @param callback
		     */
			VideoTextureView.prototype.registerTapEvent = function (callback) {
				try {
					this.getImageControl().registerTapEvent(callback);
				} catch (err) {
					console.log("VideoTextureView.prototype.registerTapEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Register the tap event on the current overlay image.
		     * @param callback
		     */
			VideoTextureView.prototype.deregisterTapEvent = function () {
				try {
					this.getImageControl().deregisterTapEvent();
				} catch (err) {
					console.log("VideoTextureView.prototype.deregisterTapEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Register the tap and hold event on the current overlay image.
		     * @param callback
		     */
			VideoTextureView.prototype.registerHoldEvent = function (callback) {
				try {
					this.getImageControl().registerHoldEvent(callback);
				} catch (err) {
					console.log("VideoTextureView.prototype.registerHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		    /**
		     * Deregister/unregister the hold event on the current overlay image.
		     */
			VideoTextureView.prototype.unregisterHoldEvent = function () {
				try {
					this.getImageControl().unregisterHoldEvent();
				} catch (err) {
					console.log("VideoTextureView.prototype.unregisterHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
 			};
 			
 			
 		    /*
 		     * THIS FUNCTION SHOULD BE INSIDE OF THE HIGHEST PAGE OF DEMO PAGES.
 		     */
// 			VideoTextureView.prototype.setOnVideoEnded = function (callback) {
// 				try {
//	 				console.log("############### vtv setonvideoended");
//	 				console.log("############### this.getMediaPlayer().onVideoEnded: " + this.getMediaPlayer().onVideoEnded);
//	 				console.log("############### this.getMediaPlayer: " + this.getMediaPlayer);
//	 				this.getMediaPlayer().onVideoEnded(callback);
//				} catch (err) {
//					console.log("VideoTextureView.prototype.unregisterHoldEvent : exception [" + err.name + "] msg[" + err.message + "]");
//				}
// 			};
			
			/*
			 * Chapter functions
			 */
			VideoTextureView.prototype.setChapter = function (demo, filePath, onready, onerror) {
				try {
					this.chapter.ready(demo, filePath, onready, onerror, this.onReadyChapter);
				} catch (err) {
					console.log("VideoTextureView.prototype.setChapter: exception [" + err.name + "] msg[" + err.message + "]");
				} 
			};
			VideoTextureView.prototype.onReadyChapter = function () {
				try {
					console.log("VideoTextureView.prototype.onReadyChapter)+");
					VideoTextureView.getInstance().chapter.setCurrentTimeCallback(VideoTextureView.getInstance().onAdjustVideoTimeFromChapter);
				} catch (err) {
					console.log("VideoTextureView.prototype.onReadyChapter : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			VideoTextureView.prototype.playChapter = function () {
				try {
					this.chapter.start();
				} catch (err) {
					console.log("VideoTextureView.prototype.playChapter : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			VideoTextureView.prototype.stopChapter = function () {
				try {
					this.chapter.stop();
				} catch (err) {
					console.log("VideoTextureView.prototype.stopChapter : exception [" + err.name + "] msg[" + err.message + "]");
				}				
			};
			VideoTextureView.prototype.seekToChapter = function (chapterIndex, isChapterStart) {
				try {
					this.chapter.seekToChapter(chapterIndex, isChapterStart);
				} catch (err) {
					console.log("VideoTextureView.prototype.seekToChapter : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			VideoTextureView.prototype.onAdjustVideoTimeFromChapter = function (currentTime) {
				try {
					console.log("VideoTextureView.prototype.onAdjustVideoTimeFromChapter)+ currentTime = " + currentTime);
					var sec = currentTime/1000;
					console.log("VideoTextureView.prototype.onAdjustVideoTimeFromChapter : sec.toFixed(3) = " + sec.toFixed(3));
					VideoTextureView.getInstance().seekToTime(sec.toFixed(3));
				} catch (err) {
					console.log("VideoTextureView.prototype.onAdjustVideoTimeFromChapter : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return VideoTextureView;
		})();
		videomanager.VideoTextureView = VideoTextureView;
	})(retail.videomanager || (retail.videomanager = {}));
	var videomanager = retail.videomanager;
})(retail || (retail = {}));

