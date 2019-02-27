var tizen = (function() {
	function tizen() {
		var filesystem = (function () {
			// TODO FileStream in FileSystem?? take it out?
			var FileStream = (function () {
				function FileStream() {
					try {
						console.log("FileStream)+ ");
					} catch (err) {
						console.log("FileStream : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				FileStream.getInstance = function () {
						try {
							if (!FileStream.sInstance) {
								FileStream.sInstance = new FileStream();
							}
							return FileStream.sInstance;
						} catch (err) {
							console.log("FileStream.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
						}
				};
				FileStream.sInstance = null;

				FileStream.prototype.init = function(content) {
					this.content = content;
					console.log("FileStream init)+ " + this.content);
				};

				FileStream.prototype.read = function() {
					console.log("FileStream read)+ " + this.content);
					return this.content;
				};
				FileStream.prototype.close = function() {
					console.log("FileStream close)+ ");
					// don't have to anything.
				};
				
				return FileStream;
			})();
			filesystem.FileStream = FileStream;
			// ========================== FileStream ==========================

			
			// FileItem
			var FileItem = (function () {
				function FileItem() {
					try {
						console.log("FileItem)+ ");
					} catch (err) {
						console.log("FileItem : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				FileItem.prototype.init = function(fileName, content) {
					this.name = fileName;
					this._content = content;
					this.fileSize = 0;
				};

				FileItem.prototype.openStream = function(mode, callback) {
					var fileStream = FileStream.getInstance();
					fileStream.init(this._content);
					callback(fileStream);
				};
				
				return FileItem;
			})();
			filesystem.FileItem = FileItem;
			// ========================== FileItem ==========================
			
			function filesystem() {
				try {
					console.log("filesystem)+ ");
				} catch (err) {
					console.log("filesystem : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			filesystem.getInstance = function () {
				try {
					if (!filesystem.sInstance) {
						filesystem.sInstance = new filesystem();
					}
					return filesystem.sInstance;
				} catch (err) {
					console.log("filesystem.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			filesystem.sInstance = null;
			
			filesystem.prototype.init = function(tizenChapterFolder) {
				try {
					this.tizenChapterFolder = tizenChapterFolder;
					this.chapters = [];
	
					reader = new FileReader();
					// file names
					var chapterNames = ["BatteryPage_chap.json",
								"CallsAndTextsPage_chap.json",
								"DemoVideoPage_chap.json",
								"DesignPage_chap.json",
								"OnboardGPSPage_chap.json",
								"PersonalizePage_chap.json",
								"SamsungPayPage_chap.json",
								"SHealthPage_chap.json"];
	
					var localFolder = this.convertToLocal(tizenChapterFolder);
	
					var i, placeHolderContent;
					for (i = 0; i < chapterNames.length; i++) { 
					    placeHolderContent = this.readText(localFolder, chapterNames[i]);
						var fileItem = new FileItem();
					    fileItem.init(chapterNames[i], placeHolderContent);
					    this.chapters.push(fileItem);
					}

				} catch (err) {
					console.log("filesystem.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
	
			};
	
			filesystem.prototype.convertToLocal = function(tizenChapterFolder) {
				// remove 'wgt-package' from path "wgt-package/chapter/..."
				return tizenChapterFolder.substring(12);
			};
	
	//		filesystem.prototype.readText = function(localFolderPath, fileName) {
	//			if (!reader) {
	//				alert("Error, reader was not initialized");
	//				return;
	//			}
	//			var filePath = localFolderPath + "/" + fileName;
	//	        var output = ""; //placeholder for text output
	//            reader.onload = function (e) {
	//                output = e.target.result;
	//            };//end onload()
	//            reader.readAsText(filePath);
	//	        return "";
	//		}
			filesystem.prototype.readText = function(localFolderPath, fileName) {
				var filePath = localFolderPath + "/" + fileName;
				var id = this.convertToId(filePath);
				var jqueryId = "#" + id;
				var content = $(jqueryId).contents().contents().find("body").contents().html();				
			    return content;
			}
	
			filesystem.resolve = function(folderPath, callback) {
				var chaptersOfFileSystem = filesystem.getInstance().chapters;
				var obj = {
						listFiles: function (callback) {
							callback(chaptersOfFileSystem);
						}
				}
				callback(obj);
			};
			
			filesystem.prototype.convertToId = function(path) {
				// convert from "chapter/standalone/BatteryPage_chap.json" to "chapter_standalone_BatteryPage_chap_json"
				return path.replace(/[\/.]/ig,"_"); 
			};
			
			return filesystem;
		})();
		tizen.filesystem = filesystem;
	}
	
	tizen.getInstance = function () {
		try {
			if (!tizen.sInstance) {
				tizen.sInstance = new tizen();
			}
			return tizen.sInstance;
		} catch (err) {
			console.log("tizen.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
		}
	};
	tizen.sInstance = null;

	tizen.prototype.showTizen = function() {
        $('#tizen_placeholder').append(tizen.htmlFileList);
    };

	tizen.htmlFileList = [
		'<div id="chapter_standalone_BatteryPage_chap_json"><iframe src="chapter/standalone/BatteryPage_chap.json"></iframe></div>',
    	'<div id="chapter_standalone_CallsAndTextsPage_chap_json"><iframe src="chapter/standalone/CallsAndTextsPage_chap.json"></iframe></div>',
        '<div id="chapter_standalone_DesignPage_chap_json"><iframe src="chapter/standalone/DesignPage_chap.json"></iframe></div>',
        '<div id="chapter_standalone_OnboardGPSPage_chap_json"><iframe src="chapter/standalone/OnboardGPSPage_chap.json"></iframe></div>',
        '<div id="chapter_standalone_PersonalizePage_chap_json"><iframe src="chapter/standalone/PersonalizePage_chap.json"></iframe></div>',
        '<div id="chapter_standalone_SamsungPayPage_chap_json"><iframe src="chapter/standalone/SamsungPayPage_chap.json"></iframe></div>',
        '<div id="chapter_standalone_SHealthPage_chap_json"><iframe src="chapter/standalone/SHealthPage_chap.json"></iframe></div>'
	];

	return tizen;
})();


tizen.getInstance().showTizen();

// wait some time to load
setTimeout(function(){
	tizen.filesystem.getInstance().init("wgt-package/chapter/standalone");
}, 5000);


var rotateMap = {
	37 : "CCW",	// left
	39 : "CW" // right
};
var keyMap = {
	187 : 'back' // '='
}

window.addEventListener('keydown', function (event) {
	var newKey = rotateMap[event.keyCode];
	console.log("tizen hw event " + event.keyCode + ", rotateMap: " + newKey);
	if (newKey) {
		if (RetailSolis.view && RetailSolis.view.thumbnailchanger) {
			RetailSolis.view.thumbnailchanger.ThumbnailChanger.getInstance().rotaryEventHandler({
				detail: {
					direction : newKey
				}
			});
		}
        if (RetailSolis.App.getInstance().currentPage.getPage() == Constants.PageInfo.AttractorPage.PAGE) {
            RetailSolis.App.getInstance().currentPage.rotaryEventHandler({
            detail: {
                    direction : newKey
                }
            });
        }
		return;
	}

	newKey = keyMap[event.keyCode];
	if (newKey) {
		RetailSolis.App.onHardwareKeys({
			keyName: newKey
		});
		return;
	}
},false);





