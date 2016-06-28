/* global: tizen, webapis */

/**
 * Samsung Accessory Protocol
 *
 * Author: rgraham, joe, ike, michael, santa claus and papa smurf
 * Verison: 1.3
 */

var SAP;
 
var sap = function() {}; 

sap.prototype = {
		connectionAttemptIntervalTime: 2000,
		socket: null,
		agent: null,
		fileTransfer: null,
		channelId: Constants.wearableChannelId,
		cmDeserializer: CommonMessagingDeserializerInstance,
		
		//TODO: Update with Proto Device model.
		localDevice: {"id" : "0123456789"},
			
 		init: function() {
 			CommonMessagingDeserializerInstance.setSenderId(SAP.localDevice.id);
 			
 			EventModuleInstance.listeners({
 				'application.unload': SAP.applicationUnload
 			});
 		},

		/**
		 * Check if SAP socket is connected
		 */
		isConnected: function() {
		 	return (SAP.socket !== null);
		},
		
		/**
		 * Connection init
		 */
		connect: function() {
		 	if (SAP.socket) {
		 		console.log('Already connected!');
		 		return false;
		 	}

		 	try {
			 	webapis.sa.setDeviceStatusListener(SAP.onDeviceStatusChanged);
		 	} catch (e) { }

		 	try {
		 		webapis.sa.requestSAAgent(SAP.onConnectSuccess, SAP.onConnectError);
		 	} catch (e) { }
		 },


		/**
		 * On data receive success, invoke listeners
		 * @param {number} channel
		 * @param {object} messageJSON
		 */
		onMessageReceived: function(channel, messageJSON) {
			var commonMessage = SAP.cmDeserializer.deserializeCommonMessage(messageJSON);
			
			if (commonMessage.messageType === SAP.cmDeserializer.getMessageObject('CommonMessage').MessageType.APPLICATION) {
				var masterMessage = SAP.cmDeserializer.deserializeMasterMessage(commonMessage.data);
				
			 	var message = SAP.cmDeserializer.getApplicationMessage(masterMessage);
			 	
			 	// Using the message eventType from the message object
			 	// we can invoke any listeners and pass the data to them
			 	EventModuleInstance.fire('sap.eventType.' + masterMessage.messageType, {
			 		channel: channel,
			 		message: message
			 	});
			} else if (commonMessage.messageType === SAP.cmDeserializer.getMessageObject('CommonMessage').MessageType.CONTENT_UPDATE_COMPLETE) {
				EventModuleInstance.fire('sap.contentUpdateComplete', {
					channel: channel,
					message: message
				});
			}
		},

		/**
		 * On connection lost
		 * @param {string} errorCode
		 */
		onConnectionLost: function(errorCode) {
			SAP.socket = null;
		 	EventModuleInstance.fire('sap.connection.lost', errorCode);
		 	
		 	setTimeout(function(){
		 		if (!SAP.socket) {
					SAP.connect();
				}
			}, 15000);
		},

		/**
		 * On service connection response
		 * @param {object} sock
		 */
		onServiceConnect: function(sock) {
			SAP.socket = sock;
			
			SAP.socket.setDataReceiveListener(function(channel, messageJSON) {SAP.onMessageReceived(channel, messageJSON);});
			SAP.socket.setSocketStatusListener(function(e) {SAP.onConnectionLost(e);});
			
			EventModuleInstance.fire('sap.init', {status: true});
		},
		
		onServiceRequest: function(peerAgent) {
    		if (SAP.socket != null && SAP.socket.isConnected()) {
    			console.log('closing connection!');
    			SAP.socket.close();
    			SAP.socket == null;
    		}

    		SAP.agent.acceptServiceConnectionRequest(peerAgent);
	    },

		/**
		 * On service connection error.
		 * @param {string} error
		 */
	    onServiceConnectionError: function(error) {
		 	setTimeout('SAP.connect();', SAP.connectionAttemptIntervalTime);
		},
		
		onFileReceive: function(transferId, fileName) {	
			try {	
				SAP.fileTransfer.receiveFile(transferId, Constants.fileStoragePath + fileName);
			} catch (e) { }
		},

		onFileReceiveProgress: function(transferId, progress) { },
		
		onFileReceiveComplete: function(transferId, localPath) {
			EventModuleInstance.fire('sap.fileTransferComplete', {'transferId': transferId, 'localPath': localPath});
		},
		
		onFileReceiveError: function(errorCode, transferId) { },
		
		/**
		 * On connect success
		 * @param {array} agents
		 */
		onConnectSuccess: function(agents) {
			connectionTrysUsed = 0;
			
		 	SAP.agent = agents[0];
		 	
		 	SAP.fileTransfer = SAP.agent.getSAFileTransfer();
		 	
		 	SAP.fileTransfer.setFileReceiveListener({
				onreceive : SAP.onFileReceive,
				onprogress : SAP.onFileReceiveProgress,
				oncomplete : SAP.onFileReceiveComplete,
				onerror : SAP.onFileReceiveError
			});
		 	
		 	SAP.agent.setServiceConnectionListener({
		 		onconnect: SAP.onServiceConnect,
		 		onerror: SAP.onServiceConnectionError,
		 		onrequest : SAP.onServiceRequest
		 	});

		 	SAP.agent.setPeerAgentFindListener({
		 		onpeeragentfound: function(peerAgent) {
		 			if (peerAgent.appName == Constants.wearableAppName) {
						SAP.agent.requestServiceConnection(peerAgent);
					}
		 		},
		 		onerror: function() {
		 			EventModuleInstance.fire('sap.peerAgentFailure', {});
		 		}
		 	});
		 	
		 	SAP.agent.findPeerAgents();
		},

		/**
		 * On connect error
		 * @param {object} err
		 */
		onConnectError: function(err) {
			EventModuleInstance.fire('sap.init', {
		 		status: false,
		 		data: err
		 	});
		},

		/**
		 * Executes when device status has changed (attached, detached).
		 * @param {string} type
		 * @param {string} status
		 */
		onDeviceStatusChanged: function(type, status) {
		 	if (status === 'ATTACHED') {
		 		EventModuleInstance.fire('sap.device.attached');
		 		SAP.connect();
		 	} else if (status === 'DETACHED') {
		 		EventModuleInstance.fire('sap.device.detached');
		 	}
		},


		/**
		 * Handles application.unload event.
		 */
		applicationUnload: function() {
		 	if (SAP.socket) {
		 		SAP.socket.close();
		 	}
		},
		 
		/**
		 * Since all messages being sent from the gear 
		 * are basically the same, lets use DRY for regular
		 * and broadcast messages
		 * @param  {string} receiver [description]
		 * @param  {string} msgType  [description]
		 * @param  {object} msg      [description]
		 */
		defaultMessage: function(receiver, message) {
			if (SAP.isConnected()) {
				try {	
					if (SAP.localDevice) {
						//console.log("Going to construct message with localDevice.id:" + SAP.localDevice.id);
					} else {
						return;
					}

					SAP.socket.sendData(SAP.channelId, SAP.cmDeserializer.serializeCommonMessage(message));
			 	} catch(e) { }
			 }
		},

		/**
		 * Send a message to the connected device to forward to all devices
		 * @param  {string} message
		 */
		sendBroadcast: function(message){
			//TODO: Sending "" for receiver. I removed CommonMessagingModel.
			SAP.defaultMessage("", message);
		},

		 /**
	     * Sends a message to the connected device. 
	     * @param  {string|object} message
	     */
		sendMessage: function(message){
			//TODO: Sending "" for receiver. I removed CommonMessagingModel.
			SAP.defaultMessage("", message);
		}		
}

var SAP = new sap();

SAP.init();