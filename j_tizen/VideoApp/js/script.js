//$(document).ready(function(){
//    var rootView = new ContainerCreator("helloWorld");
//
//    alert(rootView.getContainer());
//});















































$(document).ready(function(){
   	video1 = document.getElementById("v1");
   	video2 = document.getElementById("v2");
   	video3 = document.getElementById("v3");
   	video4 = document.getElementById("v4");
   	video5 = document.getElementById("v5");
   	
    video1.addEventListener("ended", function () {
        window.location = '#secondpg';
        video2.play();
    }, false);
    
    video2.addEventListener("ended", function () {
        window.location = '#thirdpg';
        video3.play();
    }, false);
    
    video3.addEventListener("ended", function () {
        window.location = '#fourthpg';
        video4.play();
    }, false);
    
    video4.addEventListener("ended", function () {
        window.location = '#fifthpg';
        video5.play();
    }, false);
    
    video5.addEventListener("ended", function () {
        tizen.application.getCurrentApplication().exit();
    }, false);
})

var assignVideo = function(video) {
	
}









//		prepareVideos: function() {
//			this.prepVideo('raw/upsm_stand.mp4', false);
//			this.prepVideo('raw/battery_charge_stand.mp4', false);
//		}
//		
//		 /* Get a video ready only needed for standalone.
//		 * @param src
//		 * @param loop
//		 */
//		prepVideo: function(src, loop) {
//			var name = src.replace(/\//g, '_');
//			
//			name = name.replace(/\./g, '_');				
//			
//			createVideo(name + this.videoSuffix, src, loop);
//		}
//		
//		handleVideoClick: function() {
//			APP.attractor.stop();
//			
//			APPStandalone.showMenu();
//		}
//		
//		showMenu: function() {
//			unloadVideos();
//			
//			hideAllVideos();
//			
//			APP.attractor.stop();
//			
//			APPStandalone.standaloneMenu.show();
//			
//			APPStandalone.menuContainer.css({marginLeft: 0});
//			
//			APPStandalone.currentMenuItem = 0;
//			
//			APPStandalone.closeButton.on('click', function() {
//				APPStandalone.exitApplication();
//			});
//			
//			APPStandalone.menuPosition.hide();
//			
//			APPStandalone.closeButton.hide();
//			
//			APPStandalone.startNoActivityTimeout();
//			
//			APP.turnScreenOn();
//		}
//		
//		stop: function() {
//			$('.demo, .chapter').hide();
//			
//			APPStandalone.dismissEventType = null;
//			
//			APPStandalone.standaloneMenu.hide();
//			
//			APPStandalone.closeButton.hide();
//			
//			hideAllVideos();
//			
//			APPStandalone.hideLabel();
//			
//			APPStandalone.currentDemoIndex = 0;
//			APPStandalone.currentChapterIndex = 0;
//			
//			APPStandalone.stopNoActivityTimeout();
//			
//			clearTimeout(APPStandalone.chapterTimeout);
//		},
//		
//	startDemo: function(index) {
//
//		APP.turnScreenOn();
//		
//		APPStandalone.currentChapterIndex = 0;
//		
//		var itemGroup = standaloneChapters[index];
//		
//		itemGroup.demo.preloadVideos();
//		
//		if (itemGroup.demo == batteryDemo) {
//			preloadVideo('raw_battery_charge_stand_mp4Video');
//        	preloadVideo('raw_upsm_stand_mp4Video');
//		}
//		
//	else if (typeof(timeLength.video) != 'undefined') {
//		$('.demo').hide();
//		
//		noStart = true;
//
//		showVideo(timeLength.video);
//		
//		timeLength = timeLength.duration;
//	}
	
	
		
		
		
		