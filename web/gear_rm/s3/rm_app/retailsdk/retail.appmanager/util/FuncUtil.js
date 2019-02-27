/*
 * retail.appmanager.util.FuncUtil class
 * [created by icanmobile on 09/29/2016]
 */

var retail;
(function (retail) {
	(function (appmanager) {
		(function (util) {
			var FuncUtil = (function () {
				/*
				 * getInstance function - Singleton
				 */
				FuncUtil.getInstance = function () {
					try {
						if (!FuncUtil.sInstance) {
							FuncUtil.sInstance = new FuncUtil();
						}
						return FuncUtil.sInstance;
					} catch (err) {
						console.log("FuncUtil.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				FuncUtil.sInstance = null;

				/*
				 * construct function
				 */
				function FuncUtil() {
					try {
						if (FuncUtil.sInstance)
							return FuncUtil.sInstance;
						FuncUtil.sInstance = this;
					} catch (err) {
						console.log("FuncUtil : exception [" + err.name + "] msg[" + err.message + "]");
					}
				}
								
				/*
				 * string to function using function name and namespace
				 */
				FuncUtil.prototype.stringToFunction = function(str) {
					try {
						var arr = str.split(".");
						
						var fn = (window || this);
						for (var i = 0, len = arr.length; i < len; i++) {
							fn = fn[arr[i]];
						}
						
						if (typeof fn !== "function") {
							throw new Error("FuncUtil.prototype.stringToFunction : function not found");
						}
						return  fn;
					} catch (err) {
						console.log("FuncUtil.prototype.stringToFunction : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
								
				/*
				 * get string from file
				 */
				FuncUtil.prototype.getStringFromFile = function (filePath, callback) {
					try {
						console.log("FuncUtil.prototype.getStringFromFile)+ filePath = " + filePath);
						
						var folderPath = this.getFolderPath(filePath);
						console.log("FuncUtil.prototype.getStringFromFile : folderPath = " + folderPath);
						
						this.fileName = this.getFileName(filePath);
						tizen.filesystem.resolve(folderPath, function(dir) {
					        dir.listFiles(function(files) {
								console.log("FuncUtil.prototype.getStringFromFile.onsuccess)+ ");
								console.log("FuncUtil.prototype.getStringFromFile.onsuccess : FuncUtil.getInstance().fileName = " + FuncUtil.getInstance().fileName);
								for (var i = 0; i < files.length; i++) {
							        if (files[i].name == FuncUtil.getInstance().fileName) {
							            files[i].openStream("r", function(fs) {
							                var fileString = fs.read(files[i].fileSize);
							                fs.close();
							                console.log("FuncUtil.prototype.getStringFromFile.onsuccess : fileString: " + fileString);
							                callback(fileString);
							            }, 
							            function(err) {
							            	console.log("FuncUtil.prototype.getStringFromFile.onerror : exception [" + err.name + "] msg[" + err.message + "]");
							                callback(null);
							            }, "UTF-8");
							            break;
							        }
							    }
					        },
						    function (err) {
					        	console.log("FuncUtil.prototype.getStringFromFile.onerror : exception [" + err.name + "] msg[" + err.message + "]");
								callback(null);
							});
					    }, 
					    function(err) {
					    	console.log("FuncUtil.prototype.getStringFromFile.resolve : exception [" + err.name + "] msg[" + err.message + "]");
					        callback(null);
					    }, "r"); // make sure to use 'r' mode as 'wgt-package' is read-only folder
					} catch (err) {
						console.log("FuncUtil.prototype.getStringFromFile : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * get folder path from file path
				 */
				FuncUtil.prototype.getFolderPath = function (filePath) {
					try {
						if (filePath == null || filePath.length == 0) return;
						try {
							return filePath.match(/(.*)[\/\\]/)[1]||'';
						} catch (err) {
							console.log("FuncUtil.prototype.getFolderPath : exception [" + err.name + "] msg[" + err.message + "]");
						}
					} catch (err) {
						console.log("FuncUtil.prototype.getFolderPath : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * get file name with extention from full path
				 */
				FuncUtil.prototype.getFileName = function (filePath) {
					try {
						if (filePath == null || filePath.length == 0) return;
						try {
							return filePath.replace(/^.*(\\|\/|\:)/, '');
						} catch (err) {
							console.log("FuncUtil.prototype.getFileName : exception [" + err.name + "] msg[" + err.message + "]");
						}
					} catch (err) {
						console.log("FuncUtil.prototype.getFileName : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * get file name without extention from full path
				 */
				FuncUtil.prototype.getFileNameWithNoExt = function (fileName) {
					try {
						if (fileName == null || fileName.length == 0) return;
						return fileName.substring(0,fileName.lastIndexOf('.'));
					} catch (err) {
						console.log("FuncUtil.prototype.getFileNameWithNoExt : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				return FuncUtil;
			})();
			util.FuncUtil = FuncUtil;
		})(retail.appmanager.util || (retail.appmanager.util = {}));
		var util = retail.appmanager.util;
	})(retail.appmanager || (retail.appmanager = {}));
	var appmanager = retail.appmanager;
})(retail || (retail = {}));
