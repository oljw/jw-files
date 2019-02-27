/*
 * retail.sapmanager.SAP class
 * [created by icanmobile on 09/21/2016]
 */
var retail;
(function (retail) {
	(function (sapmanager) {
		var SAP = (function () {
			/*
			 * getInstance function - Singleton
			 */
			SAP.getInstance = function () {
				try {
					if (!SAP.sInstance)
						SAP.sInstance = new SAP();
					return SAP.sInstance;
				} catch (err) {
					console.log("SAP.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			SAP.sInstance = null;

			/*
			 * construct function
			 */
			function SAP() {
				try {
					if (SAP.sInstance)
						return SAP.sInstance;
					SAP.sInstance = this;
					
					this.onsapconnected = null;
					this.onsaperror = null;
					this.input = null; 		//for PacketBuilder in order to send packet
					this.output = null;		//for PacketParser in order to parse packet
					
					
					this.SAAgent = null;
					this.SASocket = null;
					this.CHANNELID = 132;
					this.ProviderAppName = "RetailHero";
					this.connectionAttemptIntervalTime = 5000;
					this.RetryTimerId = null;
				} catch (err) {
					console.log("SAP : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			/*
			 * setCallback function
			 */
			SAP.prototype.setCallback = function(onsapconnected, onsaperror) {
				SAP.getInstance().onsapconnected = onsapconnected;
				SAP.getInstance().onsaperror = onsaperror;
			};
			
			/*
			 * StartRetryConnectionTimer function
			 */
			SAP.prototype.StartRetryConnectionTimer = function(timeDelay) {
				if (SAP.getInstance().RetryTimerId != null) {
					clearTimeout(SAP.getInstance().RetryTimerId);
				}
				SAP.getInstance().RetryTimerId = setTimeout(function(){
			 		if (!SAP.getInstance().SASocket) {
			 			SAP.getInstance().connect();
					}
				}, timeDelay);
			};
			
			/*
			 * connected function
			 */
			SAP.prototype.connected = function () {
				try {
					console.log("SAP.prototype.connected: connected");
					SAP.getInstance().onsapconnected(true);
				} catch (err) {
					console.log("SAP.prototype.connected : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * disconnected function
			 */
			SAP.prototype.disconnected = function () {
				try {
					console.log("SAP.prototype.connected: disconnected");
					if (SAP.getInstance().SASocket != null) {
						SAP.getInstance().SASocket.close();
					}
					
					SAP.getInstance().SASocket = null;
					
					SAP.getInstance().onsapconnected(false);
				} catch(err) {
					console.log("SAP.prototype.disconnected : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * disconnect the socket
			 */
			SAP.prototype.disconnect = function () {
				try {
					console.log("SAP.prototype.disconnect: force disconnect");
					if (SAP.getInstance().SASocket != null) {
						SAP.getInstance().SASocket.close();
					}
				} catch(err) {
					console.log("SAP.prototype.disconnect : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};

			/*
			 * connect()
			 */
			SAP.prototype.connect = function () {
				console.log("SAP.prototype.connect");
				
				try {
					if (SAP.getInstance().SASocket) {
						console.log("SAP.prototype.connect : Already Connected");
				        return false;
				    }
					
					// JW: NOT 100%
//					webapis.sa.setDeviceStatusListener(function(type,status){
//						if (status ==='ATTACHED'){
//							console.log("SAP.prototype.connect: SAP Connected");
//							SAP.getInstance().onsapconnected(true);
//						} else if (status === 'DETACHED') {
//							console.log("SAP.prototype.connect: SAP Disconnected");
//							SAP.getInstance().onsapconnected(false);
//							SAP.getInstance().onsaperror();
//						}
//					});
					
					console.log("SAP : requestSAAgent()");
					webapis.sa.requestSAAgent(SAP.getInstance().OnRequestSAAgentSuccess, SAP.getInstance().OnRequestSAAgentError);	
				} catch (err) {
					try {
						var error = "SAP.prototype.connect : exception [" + err.name + "] msg[" + err.message + "]";
						SAP.getInstance().onsaperror(error);
					} catch (err) {
						console.log("SAP.prototype.connect : exception [" + err.name + "] msg[" + err.message + "]");
					}
				}
			};
			
			/*
			 * 
			 * OnRequestSAAgentSuccess()
			 * - Callback function for the requestSAAgent()
			 * 
			 */
			SAP.prototype.OnRequestSAAgentSuccess = function (agents) {
				console.log("SAP.prototype.OnRequestSAAgentSuccess)+ : agent count - " + agents.length + ", " + agents);
				
				try {
					if (agents.length > 0) {
						console.log("SAP.prototype.OnRequestSAAgentSuccess : OK ");
						SAP.getInstance().SAAgent = agents[0];
						
						// Set the ServiceConnectionListener
						SAP.getInstance().SAAgent.setServiceConnectionListener(SAP.getInstance().OnServiceConnectionCallback);
						
						// Set the PeerAgentFindListener
						SAP.getInstance().SAAgent.setPeerAgentFindListener(SAP.getInstance().OnPeerAgentFindCallback);
						
						console.log("SAP : findPeerAgents()");
						SAP.getInstance().SAAgent.findPeerAgents();
					} else {
						var error = "SAP.prototype.OnRequestSAAgentSuccess : Not found SAAgent!!";
						SAP.getInstance().onsaperror(error);
					}
				} catch(err) {
					var error = "SAP.prototype.OnRequestSAAgentSuccess : exception [" + err.name + "] msg[" + err.message + "]";
					SAP.getInstance().onsaperror(error);
				}
			};
			
			/*
			 * OnRequestSAAgentError()
			 * - Callback function for the requestSAAgent()
			 */
			SAP.prototype.OnRequestSAAgentError = function(err) {
				console.log("SAP.prototype.OnRequestSAAgentError)+");
				
				// retry
				console.log("Retry 01 connection..(OnRequestSAAgentError)");
				SAP.getInstance().StartRetryConnectionTimer(SAP.getInstance().connectionAttemptIntervalTime);
				
				var error = "SAP.prototype.connect.requestSAAgent : err [" + err.name + "] msg[" + err.message + "]";
				SAP.getInstance().onsaperror(error);
			};
			
			/*
			 * OnPeerAgentFindCallback()
			 * - Callback function for the setPeerAgentFindListener()
			 */
			SAP.prototype.OnPeerAgentFindCallback = {
				
				onpeeragentfound : function(peerAgent) {
					console.log("OnPeerAgentFindCallback.onpeeragentfound)+ ");
					
					try {
						if (peerAgent.appName == SAP.getInstance().ProviderAppName) {
							console.log("OnPeerAgentFindCallback.onpeeragentfound : This App name is the same");
							
							console.log("SAP : requestServiceConnection");
							SAP.getInstance().SAAgent.requestServiceConnection(peerAgent);
						} else {
							console.log("OnPeerAgentFindCallback.onpeeragentfound [Warring] : This App name Does not match");
							var error = "SAP.prototype.peerAgentFindCallback.onpeeragentfound : Not expected app!! : " + peerAgent.appName;
							SAP.getInstance().onsaperror(error);
						}
					} catch(err) {
						try {
							var error = "SAP.prototype.peerAgentFindCallback.onpeeragentfound : exception [" + err.name + "] msg[" + err.message + "]";
							SAP.getInstance().onsaperror(error);
						} catch (err) {
							console.log("SAP.prototype.peerAgentFindCallback.onpeeragentfound : exception [" + err.name + "] msg[" + err.message + "]");
						}
					}
				},
				onerror : function() {
					console.log("SAP.prototype.OnPeerAgentFindCallback.onerror)+ ");
					
					// retry
					console.log("Retry 02 connection..(OnPeerAgentFindCallback)");
					SAP.getInstance().StartRetryConnectionTimer(SAP.getInstance().connectionAttemptIntervalTime);
					
					try {
						var error = "SAP.prototype.peerAgentFindCallback.onerror";
						SAP.getInstance().onsaperror(error);
					} catch (err) {
						console.log("SAP.prototype.peerAgentFindCallback.onerror : exception [" + err.name + "] msg[" + err.message + "]");
					}
				}
			};
			
			
			/*
			 * OnServiceConnectionCallback()
			 * - Callback function for the setServiceConnectionListener()
			 */
			SAP.prototype.OnServiceConnectionCallback = {
				onconnect : function(socket) {
					console.log("OnServiceConnectionCallback.onconnect)+ ");
					
					try {
						SAP.getInstance().SASocket = socket;
						SAP.getInstance().SASocket.setSocketStatusListener(SAP.getInstance().OnSocketConnectionLost);
//						SAP.getInstance().SASocket.setSocketStatusListener(function(reason){
//							console.log("SAP.prototype.agentCallback.onconnect : Service connection lost, Reason : [" + reason + "]");
//							SAP.getInstance().disconnected();
//						});
						SAP.getInstance().SASocket.setDataReceiveListener(SAP.getInstance().receive);
						SAP.getInstance().connected();
					} catch (err) {
						console.log("OnServiceConnectionCallback.onconnect : exception [" + err.name + "] msg[" + err.message + "]");
					}
				},
				onerror : function() {
					console.log("OnServiceConnectionCallback.onerror)+ ");
					
					try {
						var error = "SAP.prototype.agentCallback.error";
						SAP.getInstance().onsaperror(error);
						
						// after 3000ms, try to reconnecting
						console.log("Retry 03 connection..(OnServiceConnectionCallback)");
						SAP.getInstance().StartRetryConnectionTimer(SAP.getInstance().connectionAttemptIntervalTime);
						
					} catch (err) {
						console.log("OnServiceConnectionCallback.onerror : exception [" + err.name + "] msg[" + err.message + "]");
					}
				},
				onrequest : function(peerAgent) {
					console.log("OnServiceConnectionCallback.onrequest)+ : PeerAgent - " + peerAgent);
					
					try {						
			    		if (SAP.getInstance().SASocket != null && SAP.getInstance().SASocket.isConnected()) {
			    			console.log('OnServiceConnectionCallback.onrequest - The current Socket is closing connection!');
			    			SAP.getInstance().SASocket.close();
			    			SAP.getInstance().SASocket = null;
			    		}

			    		if (peerAgent.appName == SAP.getInstance().ProviderAppName) {
			    			console.log('SAP : accept the service connection request');
			    			SAP.getInstance().SAAgent.acceptServiceConnectionRequest(peerAgent);
			    		} else {
			    			console.log('SAP : reject the service connection request');
			    			SAP.getInstance().SAAgent.rejectServiceConnectionRequest(peerAgent);
			    		}
					} catch (err) {
						console.log("OnServiceConnectionCallback.onrequest : exception [" + err.name + "] msg[" + err.message + "]");
					}
				}
			};
			
			
			/*
			 * On connection lost : callback method of setSocketStatusListener
			 * @param {string} errorCode
			 */
			SAP.prototype.OnSocketConnectionLost = function(errorCode) {
				console.log('SAP.prototype.OnSocketConnectionLost+ : Service socket connection lost [' + errorCode + ']');
			
				//SAP.SASocket = null;
				SAP.getInstance().disconnected();
				
				// retry
				console.log("Retry 04 connection..(OnSocketConnectionLost)");
				SAP.getInstance().StartRetryConnectionTimer(SAP.getInstance().connectionAttemptIntervalTime);
			};

			/*
			 * static receive function
			 */
			SAP.prototype.receive = function (channel, jsonString) {
				try {
					if (SAP.getInstance().output == null) return;
				    SAP.getInstance().output(channel, jsonString);
				} catch (e) {
					console.log("SAP.prototype.receive : error = " + e.message);
				}
		    };
		    SAP.prototype.setOutput = function (output) {
		    	try {
				this.output = output;
		    	} catch (err) {
		    		console.log("SAP.prototype.setOutput : exception [" + err.name + "] msg[" + err.message + "]");
		    	}
			};
		    
			/*
			 * fetch function
			 */
			SAP.prototype.fetch = function (packet) {
				try {
					console.log("SAP.prototype.fetch)+ packet = " + packet);
					//jk send when SASocket is available
					if (SAP.getInstance().SASocket != null) {
						SAP.getInstance().SASocket.sendData(SAP.getInstance().CHANNELID, JSON.stringify(packet));						
					} else {
						console.log("SAP.prototype.fetch: SAP.getInstance().SASocket: " + SAP.getInstance().SASocket);
					}
//					SAP.getInstance().SASocket.sendData(SAP.getInstance().CHANNELID, JSON.stringify(packet));
				} catch(err) {
					console.log("SAP.prototype.fetch : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
						
			return SAP;
		})();
		sapmanager.SAP = SAP;
	})(retail.sapmanager || (retail.sapmanager = {}));
	var sapmanager = retail.sapmanager;
})(retail || (retail = {}));