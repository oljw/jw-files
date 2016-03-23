//Define Battery Demo
batteryDemo = $("#batteryDemo").demo({
	name: 'BatteryDemo',
	selfContained: true,
	
	populateChapters : function(){
		var self = this;
		
		var watch = this.element.find('.c1').chapter();
		var btconnected = this.element.find('.c2').chapter();
		var charging = this.element.find('.c3').chapter();
		var saving = this.element.find('.c4').chapter();
		
		var chapters = [];
		
		chapters.push(watch);
		chapters.push(btconnected);
		chapters.push(charging);
		chapters.push(saving);
		
		this.chapters = $.fn.chapters(chapters);
	}
});