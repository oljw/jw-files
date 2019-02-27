/*
 * retail.sapmanager.packet.PacketBuilder class
 * [created by icanmobile on 09/22/2016]
 */

var retail;
(function (retail) {
	(function (sapmanager) {
		(function (packet) {
			var PacketBuilder = (function () {
				/*
				 * getInstance function - Singleton
				 */
				PacketBuilder.getInstance = function () {
					try {
						if (!PacketBuilder.sInstance)
							PacketBuilder.sInstance = new PacketBuilder();
						return PacketBuilder.sInstance;
					} catch (err) {
						console.log("PacketBuilder.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				PacketBuilder.sInstance = null;

				/*
				 * construct function
				 */
				function PacketBuilder() {
					try {
						if (PacketBuilder.sInstance)
							return PacketBuilder.sInstance;
						PacketBuilder.sInstance = this;
						
						this.packetHeader = null;
						this.packetData = null;
					} catch (err) {
						console.log("PacketBuilder : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				
				/*
				 * "CHANGE_PAGE" packet
				 */
				PacketBuilder.prototype.changePage = function(nextPage, transactionDir) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.CHANGE_PAGE);
						this.packetData = new retail.sapmanager.packet.ChangePage(nextPage, transactionDir);
						return PacketBuilder.getInstance();
					} catch (err) {
						console.log("PacketBuilder.prototype.changePage : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * "READY_TO_DEMO_TO_MOBILE" packet
				 */
				PacketBuilder.prototype.readyToDemo = function(currentPage) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.READY_TO_DEMO_TO_MOBILE);
						this.packetData = new retail.sapmanager.packet.ReadyToDemo(currentPage);
						return PacketBuilder.getInstance();
					} catch (err) {
						console.log("PacketBuilder.prototype.readyToDemo : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * "START_DEMO_FROM_MOBILE" packet
				 */
				PacketBuilder.prototype.startDemo = function (currentPage) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.START_DEMO_FROM_MOBILE);
						this.packetData = new retail.sapmanager.packet.StartDemo(currentPage);
						return PacketBuilder.getInstance();						
					} catch (err) {
						console.log("PacketBuilder.prototype.startDemo : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * "NOTIFY_INTERACTION_TO_MOBILE" packet
				 */
				PacketBuilder.prototype.notifyInteraction = function(currentPage, interaction, chapterIndex, extra) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.NOTIFY_INTERACTION_TO_MOBILE);
						this.packetData = new retail.sapmanager.packet.NotifyInteraction(currentPage, interaction, chapterIndex, extra);
						return PacketBuilder.getInstance();
					} catch (err) {
						console.log("PacketBuilder.prototype.notifyInteraction : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * "CHAPTER_ITEM_FROM_MOBILE" packet
				 */
				PacketBuilder.prototype.chapterItem = function(chapterIndex, chapterStart, chapterEnd, chapterAction, chapterActionMessage) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.CHAPTER_ITEM_FROM_MOBILE);
						this.packetData = new retail.sapmanager.packet.ChapterItem(chapterIndex, chapterStart, chapterEnd, chapterAction, chapterActionMessage);
						return PacketBuilder.getInstance();						
					} catch (err) {
						console.log("PacketBuilder.prototype.chapterItem : exception [" + err.name + "] msg[" + err.message + "]");
					}
				}

				/*
				 * "NOTIFY_CHAPTER_CHANGE_TO_MOBILE" packet
				 */
				PacketBuilder.prototype.notifyChapterChange = function(chapterChange, chapterIndex) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.NOTIFY_CHAPTER_CHANGE_TO_MOBILE);
						this.packetData = new retail.sapmanager.packet.NotifyChapterChange(chapterChange, chapterIndex);
						return PacketBuilder.getInstance();
					} catch (err) {
						console.log("PacketBuilder.prototype.notifyChapterChange : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * "CHANGE_PAGE_ANALYTICS_TO_MOBILE" packet
				 */
				PacketBuilder.prototype.changePageAnalytics = function(currentPage, nextPage, interaction, duration) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.CHANGE_PAGE_ANALYTICS_TO_MOBILE);
						this.packetData = new retail.sapmanager.packet.ChangePageAnalytics(currentPage, nextPage, interaction, duration);
						return PacketBuilder.getInstance();
					} catch (err) {
						console.log("PacketBuilder.prototype.changePageAnalytics : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * "NOTIFY_INTERACTION_ANALYTICS_TO_MOBILE" packet
				 */
				PacketBuilder.prototype.notifyInteractionAnalytics = function(currentPage, interaction, extra) {
					try {
						this.packetHeader = new retail.sapmanager.packet.Header(PacketConstants.PACKET.HEADER.TYPE.NOTIFY_INTERACTION_ANALYTICS_TO_MOBILE);
						this.packetData = new retail.sapmanager.packet.NotifyInteractionAnalytics(currentPage, interaction, extra);
						return PacketBuilder.getInstance();
					} catch (err) {
						console.log("PacketBuilder.prototype.notifyInteractionAnalytics : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/*
				 * build a packet
				 */
				PacketBuilder.prototype.build = function() {
					try {
						return new retail.sapmanager.packet.Packet(this.packetHeader, this.packetData);
					} catch (err) {
						console.log("PacketBuilder.prototype.build : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				return PacketBuilder;
			})();
			packet.PacketBuilder = PacketBuilder;
			
		})(retail.sapmanager.packet || (retail.sapmanager.packet = {}));
		var packet = retail.sapmanager.packet;
	})(retail.sapmanager || (retail.sapmanager = {}));
	var sapmanager = retail.sapmanager;
})(retail || (retail = {}));
