/*
 * Demo Framework
 * 
 * 
 */


;(function ( $, window, document, undefined ) {
	
	// Allows us to bind events to show and hide if needed
	$.each(['show', 'hide'], function (i, ev) {
	    var el = $.fn[ev];
	    $.fn[ev] = function () {
			this.trigger(ev);
			return el.apply(this, arguments);
		};
	});
	
	
	// Start our Chapter Object
	function Demo(element, overrides){
		var self = this; 
		
		// The index of the dom element. Must have attribute "data-index" set
		this.index = null;

		// The element associated with the Chapter instance
		this.element = element;
		
		// This allows us the flexibility for the user to pass in custom
		// method and property overrides for methods such as onClick, onHandleRequest etc.
		if ( overrides ) $.extend(this, overrides);
		
		// Setup the click event for the element, passing in the current instance 
		// of Chapter as scope (this) and requesting the prototype.click method as the callback
		this.element.on("click", this, this.click);
		
		// Setup
		this.init();
		log('demo created:' + this.name);
	};
	
	// Start prototypes for the Chapter object
	Demo.prototype = {
		
		// To run necessary setup checks
		init : function() {
			
			this.populateChapters();
			
			if (this.videoFilePath) {
				this.initVideo();
			}
		},
		
		preloadVideos: function() {
			for (var index in this.chapters.chapters) {
				var videoFile = this.chapters.chapters[index].element.attr('data-video');
	
				if (videoFile != null) {
					var loop = this.chapters.chapters[index].element.attr('data-videoLoop');
				
					var loopTag = false;
				
					if (loop != null && loop == "true") {
						loopTag = true;
					}
				
					var name = videoFile.replace(/\//g, '_');
				
					name = name.replace(/\./g, '_');				
				
					preloadVideo(name + 'Video');
				}
			}
		},
		
		initVideo: function() {
			$(this.element).append('<video width="100%" height="100%" name="' + this.name + 'Video"><source src="' + this.videoFilePath + '" type="video/mp4"></video>');
			
			this.videoObject = $(this.element).find('video')[0];
			
			this.currentVideoChapter = 0;
		},
		
		rotaryEvent: function(direction) {
			this.onRotaryEvent(direction);
		},
		
		onRotaryEvent: function(direction) { },
		
		/*
		 * Display the chapter element.
		 */
		show : function(callback) {
			// Hide all elements with the chapter class, including in this case an attractor element
			$('.demo, .chapters').hide();
			
			// Then show the element in question
			this.element.show();
			
			// If any callback is passed when show is complete, execute it.
			if (callback instanceof Function) {
				callback();
			}
			
			if (this.videoObject) {
				this.videoObject.play();
				
				var self = this;
				
				this.videoPositionInterval = setInterval(function() {
					var currentVideoTime = Math.floor(self.videoObject.currentTime);
					
					for (var index in self.videoChapters) {
						var videoChapter = self.videoChapters[index];
						
						if (videoChapter.chapterStart === currentVideoTime) {
							if (self.chapters) {
								self.startChapter(index);
								
								self.currentVideoChapter = index;
							}
						} else if (videoChapter.chapterEnd == currentVideoTime) {
							if (self.chapters) {
								self.endChapter(index);								
							}
						}
					}
				}, 1000);
			}
			
			this.onShow();
			
			// return for chaining
			return this;
		},
		
		onShow: function() { },
		
		/*
		 * Hide the demo element.
		 */
		hide : function(callback) {
			// Hide the element
			this.element.hide();
			
			// If any callback is passed when hide is complete, execute it.
			if(callback instanceof Function) callback();
			
			$(this.element).find('.chapter video').remove();
			
			if (this.videoObject) {
				this.videoObject.pause();
				this.videoObject.currentTime = 0;
				
				this.currentVideoChapter = 0;
				
				clearInterval(this.videoPositionInterval);
			}
			
			// return for chaining
			return this;
		}, 

		populateChapters : function() { },
		
		startChapter : function(chapterIndex, data) {
			if (!this.videoObject && !this.selfContained) {
				this.hide();
			}
			
			for (var index in this.chapters.chapters) {
				this.chapters.chapters[index].hide();
			}
			
			try {
				this.chapters.get(chapterIndex).handleStartOfChapter(true, data);
			} catch (e) {
				if (!this.chapters.get(chapterIndex)) {
					//log('Error was because the chapter at index ' + chapterIndex + ' is not defined');
				} else {
					if (!this.chapters.get(chapterIndex).handleStartOfChapter) {
						//log('Error was because handleStartOfChapter was not defined for a valid chapter in this demo.');
					}
				}
			}
		},
		
		endChapter : function(chapterIndex, data) {
			try {
				if (this.videoObject) {
					this.chapters.get(chapterIndex).hide();
				}
				
				this.chapters.get(chapterIndex).handleEndOfChapter(data);
			} catch (e) {
				if (!this.chapters.get(chapterIndex)) {
					//log('Error was because the chapter at index ' + chapterIndex + ' is not defined');
				} else {
					if (!this.chapters.get(chapterIndex).handleStartOfChapter) {
						//log('Error was because handleEndOfChapter was not defined for a valid chapter in this demo.');
					}
				}
			}
		},
		
		/**
		 * Seek to the start of the current chapter.
		 */
		seekToEndOfCurrentChapter: function() {
			if (this.videoObject) {
				this.startVideoAtTime(this.videoChapters[this.currentVideoChapter].chapterEnd);
			}
		},
		
		/**
		 * Seek to the end of the current chapter.
		 */
		seekToStartOfCurrentChapter: function() {
			if (this.videoObject) {
				this.startVideoAtTime(this.videoChapters[this.currentVideoChapter].chapterStart);
			}
		},
		
		/**
		 * Seek to the start of the chapter identified by supplied index.
		 * @param chapterIndex
		 */
		seekToStartOfChapter: function(chapterIndex) {
			if (this.videoObject) {
				this.endChapter(this.currentVideoChapter);
				
				this.startVideoAtTime(this.videoChapters[chapterIndex].chapterStart);
			}
		},
		
		/**
		 * Seek to the end of the chapter identified by supplied index.
		 * @param chapterIndex
		 */
		seekToEndOfChapter: function(chapterIndex) {
			if (this.videoObject) {
				if (chapterIndex != this.currentVideoChapter) {
					this.endChapter(this.currentVideoChapter);
				}
				
				this.startVideoAtTime(this.videoChapters[chapterIndex].chapterEnd);
			}
		},
		
		startVideoAtTime: function(timeInSeconds) {
			this.videoObject.currentTime = timeInSeconds;
		}
	}
	
	// This is how the chapter object is initiated; by chaining it to
	// any jQuery object. $("#shareddemo").demo();
	$.fn.demo = function ( options ) {
		return new Demo( this, options );
    };
    
    /*
  	 * Create a Chapters object for all chapters so that we may call a chapter
  	 * by its index or other methods to be later introduced
  	 */
      function Chapters(chapterArray) {
     	 this.chapters = chapterArray;
      }
      
      Chapters.prototype = {
 		 
     	/*
     	 * The get method allows us to run through all of our chapters
     	 * and return the chapter who's index matches. In this case, it's
     	 * based off of the chapter's stored index, not the index of the 
     	 * array being passed in
     	 */
 		 get : function(index) {
 			 return this.chapters[index];
 		 }
      }
      
      // Pass an array of all chapter objects and return a new Chapters object
      $.fn.chapters = function (chapterArray) {
     	 return new Chapters(chapterArray);
      };
     
})( jQuery, window, document );