//Define Fitness Demo
fitnessDemo = $("#fitnessDemo").demo({
	name: 'FitnessDemo',
	selfContained: true,
	currentIndex: 0,
	superTime: 0,
	iconClicked: false,
	
	populateChapters : function(){
		var self = this;
		
		var watch = this.element.find('.c1').chapter();
		
		var shealthAlt = this.element.find('.c2alt').chapter({
			onHandleStartOfChapter: function() {
				self.element.find('.indicatorContainer').css('transform', 'rotate(' + rotationPointsFitness[0].deg + 'deg)');
			}
		});
		
		var shealth = this.element.find('.c2').chapter({
			onHandleStartOfChapter: function() {
				fitnessDemo.iconClicked = false;
				
				self.element.find('.overlay').show();
				
				self.currentIndex = 0;
				
				self.element.find('.label').html(rotationPointsFitness[self.currentIndex].name);
				
				self.element.find('.indicatorContainer').css('transform', 'rotate(' + rotationPointsFitness[self.currentIndex].deg + 'deg)');

				
				self.element.find('.c2 .icon.shealth').on('click', function() {
					//Send end of chapter for this chapter
					
					if (!fitnessDemo.iconClicked) {
						fitnessDemo.iconClicked = true;
						
						try {
							var MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');
							
							var tapShealth = 
								CommonMessagingDeserializerInstance.buildChapterObject('tapSHealth');
							
							var applicationMessage = 
								CommonMessagingDeserializerInstance.getWrappedApplicationMessageForBroadcast(MasterMessage.MessageType.SEEK_TO_END_OF_CHAPTER, 'SeekToEndOfChapterMessage', {chapter: tapShealth});
							
							SAP.sendMessage(applicationMessage);
						} catch (e) {}
						
						APP.manualChapterSeek();
					}
				});
			}
		});
		
		var runTimer = this.element.find('.c3').chapter();
		var totals = this.element.find('.c4').chapter();
		var getMoving = this.element.find('.c5').chapter();
		var watchEnd = this.element.find('.c6').chapter();
		
		var chapters = [];
		
		chapters.push(watch);
		chapters.push(shealthAlt);
		chapters.push(shealth);
		chapters.push(runTimer);
		chapters.push(totals);
		chapters.push(getMoving);
		chapters.push(watchEnd);
		
		this.chapters = $.fn.chapters(chapters);
		
		this.shealthChap = shealth;
	},

	/**
	 * Stuff to do on bezel rotation for this demo.
	 * @param direction
	 */
	onRotaryEvent: function(direction) {
		if (this.shealthChap.element.is(":visible")) {
			if (this.shealthChap.element.find('.overlay').is(":visible")) {
				this.shealthChap.element.find('.overlay').hide();
			} else {
				var icons = this.element.find('.c2 .icon'),
					lastDegrees = rotationPointsFitness[this.currentIndex].deg;
					lastIndex = this.currentIndex;
					
					var currentDate = new Date();
					var currentTime = currentDate.getTime();
					console.log("time diff: " + (currentTime - this.superTime));
					var elapsedTime = currentTime - this.superTime;
					this.superTime = currentTime;
					
				//Clockwise Bezel Rotation
				if (direction === 'CW') {
					if (this.currentIndex === rotationPointsFitness.length - 1) {
						return;
					}
					
					if(elapsedTime < 50){
						this.currentIndex = rotationPointsFitness.length - 1;
					}else{
						this.currentIndex++;
					}
										
					if (this.currentIndex > rotationPointsFitness.length - 1) {
						this.currentIndex--;
						return;
					}
					
				//Counter-Clockwise Bezel Rotation
				}else if (direction === 'CCW') {
					if (this.currentIndex === 0) {
						return;
					}
					
					if(elapsedTime < 50){
						console.log("elapsedTime2 " + elapsedTime);
						this.currentIndex = 0;
					}else{
						this.currentIndex--;
						console.log("elapsedTime currentIndex2 " + elapsedTime);
					}
					
					if (this.currentIndex < 0) {
						this.currentIndex++;
						return;
					}
				}
				
				this.element.find('.label').html(rotationPointsFitness[this.currentIndex].name);
				this.element.find('.indicatorContainer').stop();
				
				AnimateRotate(this.element.find('.indicatorContainer'), rotationPointsFitness[this.currentIndex].deg);

				AnimateScale($(icons[this.currentIndex]), Constants.rotation.iconSize.large, Constants.rotation.iconSize.small);
				AnimateScale($(icons[lastIndex]), Constants.rotation.iconSize.small, Constants.rotation.iconSize.large);
			}
		}
	}
});

rotationPointsFitness = [
{name: 'Recent apps', deg: 26},
{name: 'Messages', deg: 60},
{name: 'Phone', deg: 90},
{name: 'S Health', deg: 122},
{name: 'Contacts', deg: 152},
{name: 'Settings', deg: 180},
{name: 'Schedule', deg: 208},
{name: 'S Voice', deg: 238},
{name: 'Weather', deg: 270},
{name: 'Next page', deg: 305}
];