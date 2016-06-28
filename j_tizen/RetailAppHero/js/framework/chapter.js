/*
 * Chapter Framework
 * 
 * Author: Rob Graham
 * Version: 1.3
 * 
 * Description: A useful jQuery based chapter controller to bind show, hide, click and other events
 * to the elements passed in. This allows us to control on a page by page basis and hook
 * into the sap.js code for communication between the Samsung Gear and other Samsung connected device
 * 
 * Override methods: While all prototype methods could potentially be overwritten, keep to these method
 * overrides unless necessary
 * 
 * onHandleRequest
 * onClick
 * 
 * Override properties: vibrate
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
	function Chapter(element, overrides){
		
		var self = this; 
		
		// The index of the dom element. Must have attribute "data-index" set
		//this.index = null;

		// The element associated with the Chapter instance
		this.element = element;
		
		// This allows us the flexibility for the user to pass in custom
		// method and property overrides for methods such as onClick, onHandleRequest etc.
		if ( overrides ) $.extend(this, overrides);
		
		// Setup the click event for the element, passing in the current instance 
		// of Chapter as scope (this) and requesting the prototype.click method as the callback
		this.element.on("click", this, this.click)
		
		// Setup
		this.init();

	};
	
	// Start prototypes for the Chapter object
	Chapter.prototype = {
		
		// To run necessary setup checks
		init : function(){
			
			var videoFile = this.element.attr('data-video');
			
			if (videoFile != null) {
				var loop = this.element.attr('data-videoLoop');
				
				var loopTag = false;
				
				if (loop != null && loop == "true") {
					loopTag = true;
				}
				
				var name = videoFile.replace(/\//g, '_');
				
				this.name = name.replace(/\./g, '_');				
				
				createVideo(this.name + 'Video', videoFile, loopTag);
			}
		},
		
		// Customize the vibrate time if necessary for the Chapter. Default 1 second / 1000ms
		vibrateLength : 1000,

		/*
		 * The click method associated with the local element.
		 * We must pass in the event, which also carries the Chapter instance to 
		 * obtain the correct scope. This allows us to call the Chapter properties.
		 */
		click : function(event){
			// Call our onClick method which allows the user to override and construct
			// additional instructions which are passed during chapter initialization.
			event.data.onClick();

		},
		
		// Overwritable callback method tied to click().  
		onClick: function() {
			// Do something
		},
		
		/*
		 * Display the chapter element.
		 */
		show: function(callback) {
			
			// Hide all elements with the chapter class, including in this case an attractor element
			$(".chapter").hide();
			
			// Then show the element in question
			this.element.show();
			
			// If the element has the data-user-interaction attribute, lets perform it's value
			var userInteraction = this.element.attr("data-user-interaction");
			
			// In this case, we only have one behavior thats supported, which is vibrate 
			if (userInteraction) {
				navigator.vibrate(this.vibrateLength);
			}
			
			// If any callback is passed when show is complete, execute it.
			if(callback instanceof Function) callback();
			
			var videoFile = this.element.attr('data-video');
			
			if (videoFile != null) {
				showVideo(this.name + 'Video');
			}
			
			// return for chaining
			return this;
		},
		
		
		/*
		 * Hide the chapter element.
		 */
		hide : function(callback){
			// Hide the element
			this.element.hide();
			
			// If any callback is passed when hide is complete, execute it.
			if(callback instanceof Function) callback();
			
			if (this.element.attr('data-video') != null) {
				hideVideo(this.name + 'Video');
			}
			
			// return for chaining
			return this;
		},
		
		/*
		 * The handleRequest can be used to execute any request. At this time,
		 * it has no default functionality apart from showing the element if true
		 * is passed to the method.
		 */
		handleStartOfChapter : function(showChapter, data){
			showChapter = showChapter || true;
			
			if (showChapter == true) {
				this.show();
			}
			
			// Execute custom method if user passes override after 
			// handleRequest is complete
			this.onHandleStartOfChapter(data);
		},
		
		// Overwritable callback method tied to handleRequest().  
		onHandleStartOfChapter : function(data){
			// Do something
		},
		
		handleEndOfChapter : function(data) {
			this.onHandleEndOfChapter(data);
		},
		
		onHandleEndOfChapter : function(data) {
			
		}
	}
	
	// This is how the chapter object is initiated; by chaining it to
	// any jQuery object. $("#chapter1").chapter();
	$.fn.chapter = function ( options ) {
		return new Chapter( this, options );
    };
     
})( jQuery, window, document );