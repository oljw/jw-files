//Define Access Demo
accessDemo = $("#accessDemo").demo({
	name: 'AccessDemo',
	selfContained: true,
	currentIndex: 0,
	interactionOccurred: false,
	stopClick: false,

	populateChapters : function(){
		var self = this;
		
		var watch = this.element.find('.c1').chapter();
		var watchRotateCW = this.element.find('.c3').chapter();
		
		var watchRotateApps = this.element.find('.c4').chapter({
			onHandleStartOfChapter: function() {
				accessDemo.interactionOccurred = false;
				var container = this.element.find('.scrollerContainer');
				
				this.element.find('.overlay').show();
				
				container.css({marginLeft: 0});
				
				container.attr('scrollIndex', 0);
			}
		});

		var watch2 = this.element.find('.c5').chapter();
		
		var watchRotateText = this.element.find('.c6').chapter({
			onHandleStartOfChapter: function() {
				accessDemo.interactionOccurred = false;
				var container = this.element.find('.scrollerContainer');
				
				this.element.find('.overlay').show();
				
				container.css({marginLeft: -720});
				
				container.attr('scrollIndex', 2);
			}
		});
		
		
		var notification = this.element.find('.c7').chapter();
		var call = this.element.find('.c8').chapter();
		var fpo = this.element.find('.c9').chapter();
		var emoji = this.element.find('.c10').chapter({
			onHandleStartOfChapter: function() {
				accessDemo.stopClick = false;
			}
		});
		var messages = this.element.find('.c11').chapter();
		
		
		this.element.find('.emoClick').on('click', function() {
			if (!accessDemo.stopClick) {
				try {
					var MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');
					
					var tapEmoji = 
						CommonMessagingDeserializerInstance.buildChapterObject('tapEmoji');
					
					var applicationMessage = 
						CommonMessagingDeserializerInstance.getWrappedApplicationMessageForBroadcast(MasterMessage.MessageType.SEEK_TO_END_OF_CHAPTER, 'SeekToEndOfChapterMessage', {chapter: tapEmoji});
					
					SAP.sendMessage(applicationMessage);
				} catch(e) {}
				APP.manualChapterSeek();
				
				accessDemo.stopClick = true;
			}
		});
		
		var chapters = [];
		chapters.push(watchRotateCW);
		chapters.push(watchRotateApps);
		chapters.push(watch2);
		chapters.push(watchRotateText);
		chapters.push(notification);
		chapters.push(call);
		chapters.push(fpo);
		chapters.push(emoji);
		chapters.push(messages);
		
		this.chapters = $.fn.chapters(chapters);
		
		this.appsChapter = watchRotateApps;
		this.appsChapter.name = 'rotateApps';
		
		this.textChapter = watchRotateText;
		this.textChapter.name = 'rotatePages';

	},
	
	/**
	 * How to respond to bezel rotations in this demo.
	 * @param direction
	 */
	onRotaryEvent: function(direction) {
		if (this.appsChapter.element.is(":visible")) {
			chapter = this.appsChapter;
			if (!accessDemo.interactionOccurred) {
				accessDemo.interactionOccurred = true;
				APP.sendMessage(APP.buildTrackEventMessage("On Rotate", this.appsChapter.name));
			}
		} else if (this.textChapter.element.is(":visible")) {
			chapter = this.textChapter;
			if (!accessDemo.interactionOccurred) {
				accessDemo.interactionOccurred = true;
				APP.sendMessage(APP.buildTrackEventMessage("On Rotate", this.textChapter.name));
			}
		} else {
			return;
		}
		
		var container = chapter.element.find('.scrollerContainer'), 
			index = container.attr('scrollIndex'),
			totalItems = container.find('img').length;
		
		if (!index) {
			index = 0;
		}
		
		chapter.element.find('.overlay').hide();
		
		//clockwise
		if (direction == 'CW') {
			index++;
			
			if (index + 1 > totalItems) {
				return;
			} else {
				container.attr('scrollIndex', index);
				
				var mVal = index * 360;
				
				container.stop(false,true);
				container.animate({marginLeft: -mVal}, 80);
			}
		//counter-clockwise
		} else {
			index--;
			
			if (index < 0) {
				return;
			} else {
				container.attr('scrollIndex', index);
				
				var mVal = index * 360;
				
				container.stop(false,true);
				container.animate({marginLeft: -mVal}, 80);
			}
		}
	}
});

rotationPointsAccess = [
{name: 'Recent apps', deg: 26},
{name: 'Messages', deg: 60},
{name: 'Phone', deg: 90},
{name: 'S Health', deg: 122},
{name: 'Running', deg: 152},
{name: 'Settings', deg: 180},
{name: 'Schedule', deg: 208},
{name: 'S Voice', deg: 238},    
{name: 'Weather', deg: 270},
{name: 'Next page', deg: 305}
];