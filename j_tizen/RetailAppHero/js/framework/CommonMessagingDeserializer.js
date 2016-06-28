/**
 * CommonMessagingDeserializer class
 *
 * Author: ituuri
 * Version: 0.1
 */

//global instance
var CommonMessagingDeserializerInstance;
 
var CommonMessagingDeserializer = function() {}; 
 

CommonMessagingDeserializer.prototype = {
	masterMessage: null,
	commonMessage: null,
	appDeserializer: null,
	protoBuf: null,
	builder: null,
	senderId: null,
	
	/**
	 * Initialize an instance of this class.
	 */
	init: function() {
		//get a reference to ProtoBuf
		this.protoBuf = dcodeIO.ProtoBuf;
		
		//get a builder
		this.builder = this.protoBuf.newBuilder();
		
		//load up the proto objects for later usage
		this.protoBuf.loadProtoFile("proto/commonmessage.proto", this.builder);
		this.protoBuf.loadProtoFile("proto/mastermessage.proto", this.builder);
		this.protoBuf.loadProtoFile("proto/contentupdatecompletemessage.proto", this.builder);
		
		this.commonMessage = this.builder.build('CommonMessage');
		this.masterMessage = this.builder.build('MasterMessage');
		
		//for debugging only..
		this.protoBuf.loadProtoFile("proto/milkdialerchangemessage.proto", this.builder);
		
		this.appDeserializer = ApplicationMessagingDeserializerInstance;
	},
	
	/**
	 * Deserialize a CommonMessage object from a byteBuffer.
	 *
	 * @param base64 string representing a byte buffer.
	 *
	 * @returns CommonMessage
	 */
	deserializeCommonMessage: function(byteBuffer) {
		return this.commonMessage.decode64(byteBuffer);
	},
	
	/**
	 * Deserialize a MasterMessage object from a byteBuffer.
	 *
	 * @param base64 string representing a byte buffer.
	 *
	 * @returns MasterMessage
	 */
	deserializeMasterMessage: function(byteBuffer) {
		return this.masterMessage.decode64(byteBuffer);
	},
	
	/**
	 * Pull the underling specific message object from a MasterMessage object. (labor is passed to the app specific deserializer).
	 * 
	 * @param MasterMessage
	 *
	 * @returns *Application*Message
	 */
	getApplicationMessage: function (masterMessage) {
		return this.appDeserializer.getApplicationMessage(masterMessage);
	},
	
	/**
	 * Serialize a CommonMessage object to a byteBuffer.
	 *
	 * @param CommonMessage
	 *
	 * @returns byteBuffer
	 */
	serializeCommonMessage: function(commonMessage) {
		return commonMessage.toBase64();
	},
	
	/**
	 * Get a class object for a given object type
	 *
	 * @param String
	 *
	 * @returns *message* Class Object
	 */
	getMessageObject: function(type) {
		return this.builder.build(type);
	},
	
	/**
	 * Creates a random ID that consists of 3, 4 
	 * sets of hex values 0-f separated by a hyphen.
	 * xxx-xxx-xxx-xxx
	 *
	 * @return {number}
	 */
	randomId: function() {
		// Due to how often messages will be sent, this needs
		// to be as efficient as possible unfortunately making it
		// unreadable.
	    return 'xxx-xxx-xxx-xxx'.replace(/x/g, function(c) {
	    	var r = Math.random()*16|0,
	    	v = c === 'x' ? r : (r&0x3|0x8);
	    	return v.toString(16);
	    });
	},
	
	/**
	 * Get the host device ID (TV).
	 * 
	 * @return int
	 */
	getSenderId: function() {
		if (this.senderId == null) {
			this.senderId = "defaultSenderId";
		}
		return this.senderId;
	},
	
	setSenderId: function(localDeviceId) {
		this.senderId = localDeviceId;
	},
	
	/**
	 * Gets an application MasterMessage wrapped in a CommonMessage.
	 * 
	 * @param String
	 * @param String
	 * @param Map
	 *
	 * @returns CommonMessage
	 */
	getWrappedApplicationMessage: function (messageType, objectType, paramMap) {
		var MasterMessage = this.appDeserializer.getWrappedApplicationMessage(messageType, objectType, paramMap);
		
		//TODO: recieverId ?
		return new this.commonMessage(this.randomId(), Date.now(), this.getSenderId(), "dummyID", MasterMessage.toBase64(), this.commonMessage.MessageType.APPLICATION);
	},
	
	/**
	 * Gets an application MasterMessage wrapped in a CommonMessage meant for broadcast. The receiverID will be
	 * an empty string.
	 * 
	 * @param String
	 * @param String
	 * @param Map
	 *
	 * @returns CommonMessage
	 */
	getWrappedApplicationMessageForBroadcast: function (messageType, objectType, paramMap) {
		var MasterMessage = this.appDeserializer.getWrappedApplicationMessage(messageType, objectType, paramMap);
		//Empty string "" in recieverID will be treated as a broadcast if this message is propagated.
		return new this.commonMessage(this.randomId(), 1234, this.getSenderId(), "dummyID", MasterMessage.toBase64(), this.commonMessage.MessageType.APPLICATION);
		//return new this.commonMessage("fooId", 1234, "fooSenderId", "fooRecieverId", MasterMessage.toBase64(), this.commonMessage.MessageType.APPLICATION);
	},
	
	getDemoNameFromIndex: function(demoIndex) {
		for(var demoEnumName in this.masterMessage.DemoValue) {
			if(this.masterMessage.DemoValue[demoEnumName] == demoIndex) {
				return demoEnumName;
			}
		}
		
		return null;
	},
	
	getCurrentDemoObject: function (demoIndex) {
		var Chapter = this.builder.build('Chapter');
		
		var chapter = new Chapter('default', 0, 0, 0, false, false);
		
		var Demo = this.builder.build('Demo');
		
		var demoName = this.getDemoNameFromIndex(demoIndex);
		
		return new Demo(demoName, demoIndex, chapter);
	},
	
	buildChapterObject: function(chapterName) {
		var Chapter = this.builder.build('Chapter');
		
		return new Chapter(chapterName, 0, 0, 0, false, false);
	},
	
	buildDemoObject: function(demoName) {
		var Demo = this.builder.build('Demo');
		
		return new Demo(demoName, 0, null);
	}
};


CommonMessagingDeserializerInstance = new CommonMessagingDeserializer();

CommonMessagingDeserializerInstance.init();