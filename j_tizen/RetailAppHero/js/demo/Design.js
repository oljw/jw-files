//Define Design Demo
designDemo = $("#designDemo").demo({
	name: 'DesignDemo',
	selfContained: true,
	
	interactionOccurred: false,
	
	populateChapters : function(){
		var self = this;
		
		var watch = this.element.find('.c1').chapter();
		var watchFacesAnim = this.element.find('.c2').chapter();
		var watchFaces = this.element.find('.c3').chapter({
			onHandleStartOfChapter: function() {
				var container = this.element.find('.scrollerContainer');
				
				container.css({marginLeft: 0});
				
				container.attr('scrollIndex', 0);
				
				this.element.find('.stepOverlay').attr('class', 'stepOverlay');
				this.element.find('.rotateOverlay').show();
			}
		});
		
		var watchEnd = this.element.find('.c4').chapter();
		
		var chapters = [];
		
		chapters.push(watch);
		chapters.push(watchFacesAnim);
		chapters.push(watchFaces);
		chapters.push(watchEnd);
		
		this.chapters = $.fn.chapters(chapters);
		
		this.watchFacesChapter = watchFaces;
		this.watchFacesChapter.name = 'watchFaces';

	},
	
	/**
	 * Stuff to do on bezel rotation for this demo.
	 * @param direction
	 */
	onRotaryEvent: function(direction) {
		if (this.watchFacesChapter.element.is(":visible")) {
			if (!designDemo.interactionOccurred) {
				designDemo.interactionOccurred = true;
				APP.sendMessage(APP.buildTrackEventMessage("On Rotate", this.watchFacesChapter.name));
			}
			
			var container = this.watchFacesChapter.element.find('.scrollerContainer'), 
				index = container.attr('scrollIndex'),
				totalItems = container.find('img').length,
				step = this.watchFacesChapter.element.find('.stepOverlay'),
				overlay = this.watchFacesChapter.element.find('.rotateOverlay');
			
			if (!index) {
				index = 0;
			}
			
			if (overlay.is(":visible")) {
				overlay.hide();
			}
			
			//clockwise
			if (direction == 'CW') {
				index++;
				
				if (index + 1 > totalItems) {
					return;
				} else {
					container.attr('scrollIndex', index);
					
					var mVal = index * 250;
					
					container.stop(false,true);
					container.animate({marginLeft: -mVal}, 80);
					
					step.attr('class', 'stepOverlay step' + (index + 1));
				}
			//counter-clockwise
			} else {
				index--;
				
				if (index < 0) {
					return;
				} else {
					container.attr('scrollIndex', index);
					
					var mVal = index * 250;
					
					container.stop(false,true);
					container.animate({marginLeft: -mVal}, 80);
					
					step.attr('class', 'stepOverlay step' + (index + 1));
				}
			}
		}
	}
});