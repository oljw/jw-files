/**
 * ConvergenceMessagingDeserializer class
 *
 * Author: ituuri
 * Version: 0.1
 */

//global instance
var ApplicationMessagingDeserializerInstance;
 
var ProjectZeroMessagingDeserializer = function() {}; 
 

ProjectZeroMessagingDeserializer.prototype = {
	masterMessage: null,
	protoBuf: null,
	builder: null,
	
	/**
	 * Initialize an instance of this class.
	 */
	init: function() {
		//get a reference to ProtoBuf
		this.protoBuf = dcodeIO.ProtoBuf;
		
		//get a builder
		this.builder = this.protoBuf.newBuilder();
		
		//load up the proto objects for later usage
		this.protoBuf.loadProtoFile('proto/mastermessage.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/changedemomessage.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/chapter.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/demo.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/endchaptermessage.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/enddemomessage.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/endlegalmessage.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/pausedemomessage.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/playdemomessage.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/trackevent.proto', this.builder);
		this.protoBuf.loadProtoFile('proto/gotosongmessage.proto', this.builder);

		this.masterMessage = this.builder.build('MasterMessage');
	},
	
	/**
	 * Fetch the underlying application message from a master message.
	 * 
	 * @param MasterMessage
	 *
	 * @returns *Application*Message
	 */
	getApplicationMessage: function(masterMessage) {
		if (masterMessage.messageType === this.masterMessage.MessageType.CHANGE_DEMO) {
			return masterMessage.changeDemoMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.END_CHAPTER) {
			return masterMessage.endChapterMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.END_DEMO) {
			return masterMessage.endDemoMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.SEEK_TO_END_OF_CHAPTER) {
			return masterMessage.seekToEndOfChapterMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.SHOW_LEGAL) {
			return masterMessage.showLegalMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.START_ATTRACTOR) {
			return masterMessage.startAttractorMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.START_CHAPTER) {
			return masterMessage.startChapterMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.START_DEMO) {
			return masterMessage.startDemoMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.PLAY_DEMO) {
			return masterMessage.playDemoMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.PAUSE_DEMO) {
			return masterMessage.pauseDemoMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.TRACK_EVENT) {
			return masterMessage.trackEventMessage;
		} else if (masterMessage.messageType === this.masterMessage.MessageType.GOTO_SONG) {
			return masterMessage.gotoSongMessage;
		}
	},
	
	/**
	 * Get a basic application message wrapped in a master message.
	 * 
	 * @param String
	 * @param String
	 * @param Map
	 *
	 * @returns MasterMessage
	 */
	getWrappedApplicationMessage: function (messageType, objectType, paramMap) {
		var MasterMessage = new this.masterMessage(messageType, null, null, null, null, null, null, null, null, null, null, null);	

		var AppMessage = this.builder.build(objectType);
		var newMessage = new AppMessage();
		
		if (paramMap != null) {
			for (var key in paramMap) {
				newMessage[key] = paramMap[key];
			}
		}
		
		MasterMessage[objectType.charAt(0).toLowerCase() + objectType.slice(1)] = newMessage;
		
		return MasterMessage;			
	}
};


ApplicationMessagingDeserializerInstance = new ProjectZeroMessagingDeserializer();

ApplicationMessagingDeserializerInstance.init();