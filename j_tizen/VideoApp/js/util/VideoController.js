
var videoHTMLs = {};


// investigate
// xpreloadx <== what is it? need for this?
// test!!!
// 1. create a constructor
// 2. add 5 videos
// 3. playAllVideo
// 4. playVideoById (play the given id video)
// 5. playVideoByIds (play one or more videos (the given video ids))


// add a constructor.  parameter should be the root view of videos.
// constructor(r) { rootView = r;};
function ContainerCreator(containerId) {
    var conId = "'#" + containerId + "'";
    
    this.getContainer = function () {
        return conId;
    }
}


/**
 * Create a video resource to use in demo or attractor or..by SRC we will only create one unique video element per video, demos share them.
 */
var createVideo = function(id, src) {
	// if this video is available, do not create again.
	if ($('#' + id).length) {
		return;
	}

	// for attract loop page
	var loopTag = "";
	if (loop) {
		loopTag = "loop";
	}
	
	//var html = '<video xpreloadx="auto" style="display:none;" id="' + id + '" width="360" height="360" ' + loopTag + ' src="' + src + '"></video>';
	var html = '<video style="display:none;" id="' + id + '" width="360" height="360" ' + loopTag +  ' src="' + src + '"></video>';

//	if (src.indexOf('/home_clock.mp4') > -1 || src.indexOf('/rotate.mp4') > -1) {
//		$('#videoContainer').append(html);
//	} else {
//		videoHTMLs[id] = html;
//	}
	videoHTMLs[id] = html;	
	return $('#' + id);
}

var preloadVideo = function(id) {
	// j: what if there is a video???? or add the same video?
	// j: !$('#' + id).length <== the element is available
	if (videoHTMLs[id] && !$('#' + id).length) {
		//$('#videoContainer').append(videoHTMLs[id]);
		 $('#' + rootView).append(videoHTMLs[id]);
	}
}

var unloadVideos = function() {
	$('video').each(function() {
		var vid = $(this), src = vid.attr('src');
		
//		if (src.indexOf('/home_clock.mp4') == -1 && src.indexOf('/rotate.mp4') == -1) {
//			vid.remove();
//		}
		vid.remove();
	});
}

/**
 * Show a video by its ID.
 */
var showVideo = function(id) {
	hideAllVideos();

	var videoElementx = $('#' + id);
	
	if (videoElementx && videoElementx.length > 0) {
		try {
			videoElementx.show()
			videoElementx[0].currentTime = 0;
			videoElementx[0].play();

			return true;
		} catch (e) {
			return false;
		}
	} else {
		return false;
	}
}

/**
 * Hide all the videos, dont pause attractor to keep screen on.
 */
var hideAllVideos = function() {
	$('video').each(function(v) {
		$('video')[v].pause();
	});
	
	$('video').hide();
}

/**
 * Hide a video by its ID.
 */
var hideVideo = function(id) {
	var videoElement = $('#' + id);
	
	if (videoElement.length) {
		videoElement[0].pause();
		videoElement.hide();
	}
}

// j: do we need to call hideAllVideo?  or pause video first?
var removeVideos = function() {
	//cachedVideoHTML = $('#videoContainer').html();
	
	//$('#videoContainer').html('');
	$('#' + rootView).html('');
}

//var reinstateVideos = function() {
	//$('#videoContainer').html(cachedVideoHTML);
//}
