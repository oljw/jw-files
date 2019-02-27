/*
 * retail.appmanager.EventBus class
 * [created by icanmobile on 09/26/2016]
 */

var retail;
(function (retail) {
	(function (appmanager) {
		var EventBus = (function () {
			/*
			 * getInstance function - Singleton
			 */
			EventBus.getInstance = function () {
				try {
					if (!EventBus.sInstance) {
						EventBus.sInstance = new EventBus();
					}
					return EventBus.sInstance;
				} catch (err) {
					console.log("EventBus.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			EventBus.sInstance = null;

			/*
			 * construct function
			 */
			function EventBus() {
				try {
					if (EventBus.sInstance)
						return EventBus.sInstance;
					EventBus.sInstance = this;
					
					this.init();
				} catch (err) {
					console.log("EventBus : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			EventBus.listeners = {};
			EventBus.prototype.init = function() {
				try {
					console.log("EventBus.prototype.init)+ ");
					
					this.listeners = this.addEventListeners;
					this.fire = this.dispatchEvent;
					this.listen = this.addEventListener;
					this.die = this.removeEventListener;
				} catch (err) {
					console.log("EventBus.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
	        /**
	         * Gets listeners name for event.
	         * @param {string} eventName Event name.
	         */
	        EventBus.prototype.getListenersNames = function(eventName) {
	        	try {
		        	console.log("EventBus.prototype.getListenersNames)+ eventName = " + eventName);		        	
		            var key,
		                names = [],
		                handlers = listeners[eventName];
	
		            for (key in handlers) {
		                names.push(handlers[key].name);
		            }
		            
		            return names;
	        	} catch (err) {
	        		console.log("EventBus.prototype.getListenersNames : exception [" + err.name + "] msg[" + err.message + "]");
	        	}
	        };

	        /**
	         * Gets listeners for event.
	         * @param {string} eventName Event name.
	         */
	        EventBus.prototype.getListeners = function(eventName) {
	        	try {
		        	console.log("EventBus.prototype.getListeners)+ eventName = " + eventName);
		        	
		            var evName,
		                names = {};
	
		            if (eventName) {
		                names[eventName] = getListenersNames(eventName);
		            } else {
		                for (evName in listeners) {
		                    names[evName] = getListenersNames(evName);
		                }
		            }
		            return names;
	        	} catch (err) {
	        		console.log("EventBus.prototype.getListeners : exception [" + err.name + "] msg[" + err.message + "]");
	        	}
	        	
	        };

	        /**
	         * Dispatch an event of given name and detailed data.
	         *
	         * The return value is false if at least one of the event handlers which
	         * handled this event called Event.preventDefault().
	         * Otherwise it returns true.
	         *
	         * @param {string} eventName Event name.
	         * @param {*} data Detailed data.
	         * @return {bool} If the event was cancelled.
	         */
	        EventBus.prototype.dispatchEvent = function(eventName, data) {
	        	try {
		        	console.log("EventBus.prototype.dispatchEvent)+ eventName = " + eventName);
		            var customEvent = new CustomEvent(eventName, {
		                detail: data,
		                cancelable: true
		            });
		            
		            return window.dispatchEvent(customEvent);
	        	} catch (err) {
	        		console.log("EventBus.prototype.dispatchEvent : exception [" + err.name + "] msg[" + err.message + "]");
	        	}
	        };

	        /**
	         * Adds event listener for event name.
	         * @param {string} eventName Event name.
	         * @param {function?} handler Handler function.
	         */
	        EventBus.prototype.addEventListener = function(eventName, handler) {
	        	try {
		        	console.log("EventBus.prototype.addEventListener)+ eventName = " + eventName);
		            listeners[eventName] = listeners[eventName] || [];
		            listeners[eventName].push(handler);
		            
		            window.addEventListener(eventName, handler);
	        	} catch (err) {
	        		console.log("EventBus.prototype.addEventListener : exception [" + err.name + "] msg[" + err.message + "]");
	        	}
	        };

	        /**
	         * Removes event listener.
	         * @param {string} eventName Event name.
	         * @param {function?} handler Handler function.
	         */
	        EventBus.prototype.removeEventListener = function(eventName, handler) {
	        	try {
		        	console.log("EventBus.prototype.removeEventListener)+ eventName = " + eventName);
		            var i, handlerIndex, listenersLen;
		            if (handler !== undefined) {
		                // remove only this specific handler
		                window.removeEventListener(eventName, handler);
	
		                // find it in the array and clear the reference
		                handlerIndex = listeners[eventName].indexOf(handler);
		                if (handlerIndex !== -1) {
		                    listeners[eventName].splice(handlerIndex, 1);
		                }
		            } else {
		                // removes all listeners we know of
		                listenersLen = listeners[eventName].length;
		                for (i = 0; i < listenersLen; i += 1) {
		                    window.removeEventListener(
		                        eventName,
		                        listeners[eventName][i]
		                    );
		                }
		                // clear the references
		                listeners[eventName] = [];
		            }
	        	} catch (err) {
	        		console.log("EventBus.prototype.removeEventListener : exception [" + err.name + "] msg[" + err.message + "]");
	        	}
	        };

	        /**
	         * Adds event listeners.
	         * @param {object} listeners Listeners object.
	         *
	         * Example:
	         * addEventListeners({
	         *   'foo.event.name': function fooEventHandler(evData) {},
	         *   'bar.event.name': function barventHandler(evData) {},
	         * });
	         */
	        EventBus.prototype.addEventListeners = function(listeners) {
	        	try {
		        	console.log("EventBus.prototype.addEventListeners)+ ");
		            var eventName;
		            for (eventName in listeners) {
		                if (listeners.hasOwnProperty(eventName)) {
	//	                	console.log("EventBus.prototype.addEventListeners : eventName = " + eventName + ", listeners[eventName] = " + listeners[eventName]);
		                    addEventListener(eventName, listeners[eventName]);
		                }
		            }
	        	} catch (err) {
	        		console.log("EventBus.prototype.addEventListeners : exception [" + err.name + "] msg[" + err.message + "]");
	        	}
	        };
			
			return EventBus;
		})();
		appmanager.EventBus = EventBus;
	})(retail.appmanager || (retail.appmanager = {}));
	var appmanager = retail.appmanager;
})(retail || (retail = {}));
		
