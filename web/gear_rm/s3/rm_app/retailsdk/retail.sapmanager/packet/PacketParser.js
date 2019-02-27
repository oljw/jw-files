/*
 * retail.sapmanager.packet.PacketAnalyzer class
 * [created by icanmobile on 09/22/2016]
 */

var retail;
(function (retail) {
	(function (sapmanager) {
		(function (packet) {
			
			/*
			 * PacketParser Class
			 */
			var PacketParser = (function () {
				/*
				 * getInstance function - Singleton
				 */
				PacketParser.getInstance = function () {
					try {
						if (!PacketParser.sInstance)
							PacketParser.sInstance = new PacketParser();
						return PacketParser.sInstance;
					} catch (err) {
						console.log("PacketParser.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				PacketParser.sInstance = null;

				/*
				 * construct function
				 */
				function PacketParser() {
					try {
						if (PacketParser.sInstance)
							return PacketParser.sInstance;
						PacketParser.sInstance = this;
					} catch (err) {
						console.log("PacketParser : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
								
				/*
				 * input listener
				 */
				PacketParser.prototype.input = function (channel, jsonString) {
					try {
						console.log("PacketParser.prototype.input)+ jsonString = " + jsonString);
						try {						
							PacketParser.getInstance().parse(channel, JSON.parse(jsonString));
						} catch (err) {
							console.log("PacketParser.prototype.input : exception [" + err.name + "] msg[" + err.message + "]");
						}
					} catch (err) {
						console.log("PacketParser.prototype.input : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				PacketParser.prototype.parse = function (channel, packet) {
					try {
						console.log("PacketParser.prototype.parse)+ packet = " + packet);
						
						switch (packet.header.type) {
						case PacketConstants.PACKET.HEADER.TYPE.CHANGE_PAGE:
							console.log("PacketParser.prototype.parse : CHANGE_PAGE");
							retail.appmanager.EventBus.getInstance().fire('sap.packet.changepage', {
						 		channel: channel,
						 		message: packet.data
						 	});
							break;
						
						case PacketConstants.PACKET.HEADER.TYPE.START_DEMO_FROM_MOBILE:
							console.log("PacketParser.prototype.parse : START_DEMO_FROM_MOBILE");
							retail.appmanager.EventBus.getInstance().fire('sap.packet.startdemo', {
						 		channel: channel,
						 		message: packet.data
						 	});
							break;
														
						case PacketConstants.PACKET.HEADER.TYPE.CHAPTER_ITEM_FROM_MOBILE:
							console.log("PacketParser.prototype.parse : CHAPTER_ITEM_FROM_MOBILE");
							retail.appmanager.EventBus.getInstance().fire('sap.packet.chapteritem', {
						 		channel: channel,
						 		message: packet.data.chapter
						 	});
							break;
						}
					} catch (err) {
						console.log("PacketParser.prototype.parse : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				return PacketParser;
			})();
			packet.PacketParser = PacketParser;
		})(retail.sapmanager.packet || (retail.sapmanager.packet = {}));
		var packet = retail.sapmanager.packet;
	})(retail.sapmanager || (retail.sapmanager = {}));
	var sapmanager = retail.sapmanager;
})(retail || (retail = {}));
