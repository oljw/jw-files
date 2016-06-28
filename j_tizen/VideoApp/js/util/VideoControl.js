var mainVideoElement = null;
var videoHTMLs = {};

/**
 * Create a video resource to use in demo or attractor or..by SRC we will only create one unique video element per video, demos share them.
 */
var createVideo = function(id, src, loop) {
	if ($('#' + id).length || cachedVideoHTML.length > 1) {
		return;
	}

	var loopTag = "";

	if (loop) {
		loopTag = "loop";
	}
	
	if (mainVideoElement) {
		$(mainVideoElement).remove();
	}
	
	var html = '<video xpreloadx="auto" style="display:none;" id="' + id + '" width="360" height="360" ' + loopTag + ' src="' + src + '"></video>';
	
	if (src.indexOf('/home_clock.mp4') > -1 || src.indexOf('/rotate.mp4') > -1) {
		$('#videoContainer').append(html);
	} else {
		videoHTMLs[id] = html;
	}
	
	return $('#' + id);
}

var preloadVideo = function(id) {
	if (videoHTMLs[id] && !$('#' + id).length) {
		$('#videoContainer').append(videoHTMLs[id]);
	}
}

var unloadVideos = function() {
	$('video').each(function() {
		var vid = $(this), src = vid.attr('src');
		
		if (src.indexOf('/home_clock.mp4') == -1 && src.indexOf('/rotate.mp4') == -1) {
			vid.remove();
		}
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

			return 1;
		} catch (e) {
			return 0;
		}
	} else {
		return 0;
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

var cachedVideoHTML = '';

var removeVideos = function() {
	//cachedVideoHTML = $('#videoContainer').html();
	
	//$('#videoContainer').html('');
}

var reinstateVideos = function() {
	//$('#videoContainer').html(cachedVideoHTML);
}
