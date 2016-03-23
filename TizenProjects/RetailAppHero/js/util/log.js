//lets log to the screen as well as the console..
function log(text, level) {
	return; //performance, meh!
	
	if (!level) {
		level = debug;
	}
	
	console.log(text + ',' + level);	
	
	$('#log').append('<p>' + text + '</p>');
	
	$('#log').scrollTop($('#log').prop("scrollHeight"));
}

var error = 'error';
var debug = 'debug';