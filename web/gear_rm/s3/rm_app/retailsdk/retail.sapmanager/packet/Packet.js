/*
 * retail.sapmanager.packet.Packet class
 * [created by icanmobile on 09/22/2016]
 */
var PacketConstants = {
    "PACKET": {
    	"HEADER": {
    		"TYPE": {
    			"CHANGE_PAGE": "CHANGE_PAGE",
    			"READY_TO_DEMO_TO_MOBILE": "READY_TO_DEMO_TO_MOBILE",
    			"START_DEMO_FROM_MOBILE": "START_DEMO_FROM_MOBILE",
    			"NOTIFY_INTERACTION_TO_MOBILE": "NOTIFY_INTERACTION_TO_MOBILE",
    			"CHAPTER_ITEM_FROM_MOBILE": "CHAPTER_ITEM_FROM_MOBILE",
    			"NOTIFY_CHAPTER_CHANGE_TO_MOBILE": "NOTIFY_CHAPTER_CHANGE_TO_MOBILE",
    			"CHANGE_PAGE_ANALYTICS_TO_MOBILE": "CHANGE_PAGE_ANALYTICS_TO_MOBILE",
    			"NOTIFY_INTERACTION_ANALYTICS_TO_MOBILE": "NOTIFY_INTERACTION_ANALYTICS_TO_MOBILE"
    		}
    	}
    }
};

var retail;
(function (retail) {
	(function (sapmanager) {
		(function (packet) {
			var Packet = (function () {
				/*
				 * construct function
				 */
				function Packet(header, data) {
					try {
						this.header = header;
						this.data = data;
					} catch (err) {
						console.log("Packet : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return Packet;
			})();
			packet.Packet = Packet;
						
			/*
			 * packet header
			 */
			var Header = (function() {
				function Header(type) {
					try {
						this.type = type;
					} catch (err) {
						console.log("Header : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return Header;
			})();
			packet.Header = Header;
			
			/*
			 * change page packet data (mobile <-> wearable)
			 */
			var ChangePage = (function() {
				function ChangePage(nextPage, transactionDir) {
					try {
						this.nextPage = nextPage;
						this.transactionDir = transactionDir;
					} catch (err) {
						console.log("ChangePage : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return ChangePage;
			})();
			packet.ChangePage = ChangePage;
			
			/*
			 * ready to demo (mobile <- wearable)
			 */
			var ReadyToDemo = (function () {
				function ReadyToDemo(currentPage) {
					try {
						this.currentPage = currentPage;
					} catch (err) {
						console.log("ReadyToDemo : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return ReadyToDemo;
			})();
			packet.ReadyToDemo = ReadyToDemo;
			
			/*
			 * start demo (mobile -> wearable)
			 */
			var StartDemo = (function () {
				function StartDemo (currentPage) {
					try {
						this.currentPage = currentPage;
					} catch (err) {
						console.log("StartDemo : exception [" + err.name + "] msg[" + err.message + "]");
					}
				}
			})();
			packet.StartDemo = StartDemo;
			
			/*
			 * notify interaction packet data (mobile <- wearable)
			 */
			var NotifyInteraction = (function() {
				function NotifyInteraction(currentPage, interaction, chapterIndex, extra) {
					try {
						this.currentPage = currentPage;
						this.interaction = interaction;
						this.chapterIndex = chapterIndex;
						this.extra = extra;
					} catch (err) {
						console.log("NotifyInteraction : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return NotifyInteraction;
			})();
			packet.NotifyInteraction = NotifyInteraction;
			
			/*
			 * notify chapterStart packet data (mobile <- wearable)
			 */
			var NotifyChapterChange = (function() {
				function NotifyChapterChange(chapterChange, chapterIndex) {
					try {
						this.chapterChange = chapterChange;
						this.chapterIndex = chapterIndex;
					} catch (err) {
						console.log("NotifyChapterChange : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return NotifyChapterChange;
			})();
			packet.NotifyChapterChange = NotifyChapterChange;

			/*
			 * chapter item packet data (mobile -> wearable)
			 */
			var ChapterItem = (function() {
				function ChapterItem(chapterIndex, chapterStart, chapterEnd, chapterAction, chapterActionMessage) {
					try {
						this.chapterIndex = chapterIndex;
						this.chapterStart = chapterStart;
						this.chapterEnd = chapterEnd;
						this.chapterAction = chapterAction;
						this.chapterActionMessage = chapterActionMessage;
					} catch (err) {
						console.log("ChapterItem : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return ChapterItem;
			})();
			packet.ChapterItem = ChapterItem;
			
			/*
			 * change page anaytics packet data (mobile <- wearable)
			 */
			var ChangePageAnalytics = (function() {
				function ChangePageAnalytics(currentPage, nextPage, interaction, duration) {
					try {
//						"currentPage": "CURRENT PAGE",
//						"nextPage": "NEXT PAGE",
//						"interaction": "USER INTERACTION TYPE",
//						"duration": "DURATION"
						this.currentPage = currentPage;
						this.nextPage = nextPage;
						this.interaction = interaction;
						this.duration = duration;
					} catch (err) {
						console.log("ChangePageAnalytics : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return ChangePageAnalytics;
			})();
			packet.ChangePageAnalytics = ChangePageAnalytics;
			
			/*
			 * notify interaction analytics packet data (mobile <- wearable)
			 */
			var NotifyInteractionAnalytics = (function() {
				function NotifyInteractionAnalytics(currentPage, interaction, extra) {
					try {
//						"currentPage": "CURRENT PAGE",
//						"interaction": "USER INTERACTION TYPE",
//						"extra": "EXTRA INFORMATION"
						this.currentPage = currentPage;
						this.interaction = interaction;
						this.extra = extra;
					} catch (err) {
						console.log("NotifyInteractionAnalytics : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				return NotifyInteractionAnalytics;
			})();
			packet.NotifyInteractionAnalytics = NotifyInteractionAnalytics;
			
			
		})(retail.sapmanager.packet || (retail.sapmanager.packet = {}));
		var packet = retail.sapmanager.packet;
	})(retail.sapmanager || (retail.sapmanager = {}));
	var sapmanager = retail.sapmanager;
})(retail || (retail = {}));
