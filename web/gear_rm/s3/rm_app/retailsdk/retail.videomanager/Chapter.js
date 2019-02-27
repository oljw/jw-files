/* 
 * Chapter.js class
 * [created by icanmobile on 10/03/2016]
 */

var retail;
(function (retail) {
	(function (videomanager) {
		var Chapter = (function () {
			/**
			 * Get Chapter instance.
			 */
			Chapter.getInstance = function () {
				try {
					if (!Chapter.sInstance) {
						Chapter.sInstance = new Chapter();
					}
					return Chapter.sInstance;
				} catch (err) {
					console.log("Chapter.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			Chapter.sInstance = null;
			
			function Chapter() {
				try {
					console.log("Chapter)+ ");
					
					if (Chapter.sInstance)
						return Chapter.sInstance;
					Chapter.sInstance = this;
					
					this.demo = null;
					this.chapterFile = null;
					this.chapter = null;
					this.chapterEvents = [];
					this.chapterIndex = 0;
					
					this.currentTime = 0;
					this.chapterTimeout = null;
					
					this.currentTimeCallback = null;
				} catch (err) {
					console.log("Chapter : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * ready for json file and event array
			 */
			Chapter.prototype.ready = function (demo, chapterFile, onready, onerror, onreadychapter) {
				try {
					this.demo = demo;
					this.chapterFile = chapterFile;
					
					retail.appmanager.util.FuncUtil.getInstance().getStringFromFile(chapterFile, 
							function(jsonString) {
								if (jsonString == null || jsonString.length == 0) {
									onerror("jsonString is null");
									return;
								}
								
								//get chapter from json file
								var chapter = Chapter.getInstance().chapter = JSON.parse(jsonString);
								
								console.log("Chapter.prototype.ready : chapter.length = " + chapter.length);
								
								//save chapter events
								for(var i=0; i<chapter.length; i++) {
									//push a event for chapterStart
									Chapter.getInstance().chapterEvents.push({eventTime:chapter[i].chapterStart, chapterIndex:chapter[i].chapterIndex, isChapterStart:true});
									
									//push a event for chapterEnd
									Chapter.getInstance().chapterEvents.push({eventTime:chapter[i].chapterEnd, chapterIndex:chapter[i].chapterIndex, isChapterStart:false});
								}
								
								//sort events based on eventTime
								Chapter.getInstance().chapterEvents.sort(function(e1, e2){
									return e1.eventTime - e2.eventTime;
								});
								for(var i=0; i<Chapter.getInstance().chapterEvents.length; i++) {
									console.log("Chapter.getInstance().chapterEvents["+i+"] = " 
											+ Chapter.getInstance().chapterEvents[i].eventTime + ", " 
											+ Chapter.getInstance().chapterEvents[i].chapterIndex + ", " 
											+ Chapter.getInstance().chapterEvents[i].isChapterStart);
								}
								
								onreadychapter();
								onready();
							});
				}catch (err) {
					onerror(err);
					console.log("Chapter.prototype.ready : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * start timer for next chapter 
			 */
			Chapter.prototype.start = function () {
				console.log("Chapter.prototype.start)+ ");				
				try {
					if (this.chapterEvents == null ) return;
					if (this.chapterEvents.length == 0) {
						this.stop();
						return;
					}

					var nextTime = this.chapterEvents[0].eventTime;
					var duration = nextTime - this.currentTime;
					this.currentTime = nextTime;
					
					console.log("Chapter.prototype.start : duration = " + duration);
				
					this.chapterTimeout = setTimeout(this.chapterTimedOut, duration);
				} catch (err) {
					console.log("Chapter.prototype.start : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * stop timer and deinit
			 */
			Chapter.prototype.stop = function () {
				console.log("Chapter.prototype.stop)+ ");
				try {
					clearTimeout(this.chapterTimeout);
					
					if (this.chapterEvents.length > 0)
						this.chapterEvents.splice(0, this.chapterEvents.length);
					
					this.demo = null;
					this.chapterFile = null;
					this.chapter = null;
					
					this.currentTime = 0;
					this.chapterTimeout = null;
				} catch (err) {
					console.log("Chapter.prototype.stop : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

			/*
			 * timer timed out
			 */
			Chapter.prototype.chapterTimedOut = function () {
				console.log("Chapter.prototype.chapterTimedOut)+ ");
				try {
					Chapter.getInstance().dispatchEvent();
					Chapter.getInstance().start();
				} catch (err) {
					console.log("Chapter.prototype.chapterTimedOut : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

			/*
			 * dispatch current time events
			 */
			Chapter.prototype.dispatchEvent = function () {
				console.log("Chapter.prototype.dispatchEvent)+ ");
				try {
					clearTimeout(this.chapterTimeout);
					
					//Run events
					for (var i=0; i<this.chapterEvents.length; i++) {
						if (this.chapterEvents[i].eventTime == this.currentTime) {
							//dispatch events
							var chapterFuncName = null;
							if (this.chapterEvents[i].isChapterStart) {	//for chapterStart
								chapterFuncName = this.demo + ".onChapter_" + (this.chapterEvents[i].chapterIndex);
								
								Chapter.getInstance().chapterIndex = this.chapterEvents[i].chapterIndex;
								
//								var packet = RetailSolis.App.getInstance().packetBuilder.notifyChapterChange(Constants.CHAPTER_TRANSITION.START, this.chapterEvents[i].chapterIndex).build();
//								RetailSolis.App.getInstance().fetchPacket(packet);
							}
							else	//for chapterEnd
							{
								chapterFuncName = this.demo + ".onChapterEnded_" + (this.chapterEvents[i].chapterIndex);
								
//								var packet = RetailSolis.App.getInstance().packetBuilder.notifyChapterChange(Constants.CHAPTER_TRANSITION.END, this.chapterEvents[i].chapterIndex).build();
//								RetailSolis.App.getInstance().fetchPacket(packet);
							}
							var chapterFunc = retail.appmanager.util.FuncUtil.getInstance().stringToFunction(chapterFuncName);
							chapterFunc(this.chapter[this.chapterEvents[i].chapterIndex]);
							
							//remove dispatched event
							this.chapterEvents.splice(i, 1);
							i--;							
						}
						else	//events are ascending order.
							break;
					}
					
					//adjust media player time
					this.adjustVideoTime(this.currentTime);
				} catch (err) {
					console.log("Chapter.prototype.dispatchEvent : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * seek to chapter based on chapterIndex and "chapterStart" or "chapterEnd"
			 */
			Chapter.prototype.seekToChapter = function (chapterIndex, isChapterStart) {
				console.log("Chapter.prototype.seekToChapter)+ ");
				try {
					
					// COMMENTED OUT BECAUSE OF CHAPTER END ISSUE
//					console.log("Chapter.prototype.seekToChapter: " + (chapterIndex > this.chapterEvents.length));
//					if (chapterIndex > this.chapterEvents.length){
//						this.stop();
//						console.log("Chapter.prototype.seekToChapter: " + chapterIndex);
//						console.log("Chapter.prototype.seekToChapter: " + this.chapterEvents.length);
//						console.log("Chapter.prototype.seekToChapter: stop called!~");
//						return;
//					}
					
					clearTimeout(this.chapterTimeout);				
					
					//find the chapter from chapterEvents array
					var eventIndex = 0;
					for (var i=0; i<this.chapterEvents.length; i++) {
						if (this.chapterEvents[i].chapterIndex == chapterIndex && 
							this.chapterEvents[i].isChapterStart == isChapterStart) {
							eventIndex = i;
							break;
						}
					}
					
					console.log("Chapter.prototype.seekToChapter : eventIndex = " + eventIndex);
					
					//Run events
					for (var i=0; i<this.chapterEvents.length; i++) {
						if (this.chapterEvents[i].eventTime < this.chapterEvents[eventIndex].eventTime) {
							//dispatch "chapterEnd" events for previous started chapter items
							if (this.chapterEvents[i].isChapterStart == false) {
								var chapterFuncName = this.demo + ".onChapterEnded_" + (this.chapterEvents[i].chapterIndex);
								var chapterFunc = retail.appmanager.util.FuncUtil.getInstance().stringToFunction(chapterFuncName);
								chapterFunc(this.chapter[this.chapterEvents[i].chapterIndex]);
							}
							
							//remove passed event
							this.chapterEvents.splice(i, 1);
							i--;
						}
						else {
							break;
						}
					}
					
					//set currentTime for dispatching the events
					this.currentTime = this.chapterEvents[eventIndex].eventTime;
					this.chapterTimedOut();
				} catch (err) {
					console.log("Chapter.prototype.start : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * adjust video time
			 */
			Chapter.prototype.adjustVideoTime = function (currentTime) {
				try {
					if (this.currentTimeCallback == null) return;
					console.log("Chapter.prototype.adjustVideoTime)+ ");
					// http://stackoverflow.com/questions/11387127/how-to-pass-prototype-function
					// Need a context, but null also works.
					//this.currentTimeCallback(currentTime);
					this.currentTimeCallback.apply(null, [currentTime]);
				} catch (err) {
					console.log("Chapter.prototype.adjustVideoTime : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			Chapter.prototype.setCurrentTimeCallback = function (currentTimeCallback) {
				try {
					this.currentTimeCallback = currentTimeCallback;
				} catch (err) {
					console.log("Chapter.prototype.setCurrentTimeCallback : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return Chapter;
		})();
		videomanager.Chapter = Chapter;
	})(retail.videomanager || (retail.videomanager = {}));
	var videomanager = retail.videomanager;
})(retail || (retail = {}));
