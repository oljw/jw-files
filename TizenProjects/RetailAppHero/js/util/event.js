/**
 * Event module
 */

var EventModuleInstance;
 
var EventModule = function() {}; 
 

EventModule.prototype = {
		listeners: {},
		
		init: function() {
			this.listeners = this.addEventListeners;
	        this.fire = this.dispatchEvent;
	        this.listen = this.addEventListener;
	        this.die = this.removeEventListener;
		},

        /**
         * Gets listeners name for event.
         * @param {string} eventName Event name.
         */
        getListenersNames: function(eventName) {
            var key,
                names = [],
                handlers = listeners[eventName];

            for (key in handlers) {
                names.push(handlers[key].name);
            }
            
            return names;
        },

        /**
         * Gets listeners for event.
         * @param {string} eventName Event name.
         */
        getListeners: function(eventName) {
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
        },

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
        dispatchEvent: function(eventName, data) {
            var customEvent = new CustomEvent(eventName, {
                detail: data,
                cancelable: true
            });
            
            return window.dispatchEvent(customEvent);
        },

        /**
         * Adds event listener for event name.
         * @param {string} eventName Event name.
         * @param {function?} handler Handler function.
         */
        addEventListener: function(eventName, handler) {
            listeners[eventName] = listeners[eventName] || [];
            listeners[eventName].push(handler);
            
            window.addEventListener(eventName, handler);
        },

        /**
         * Removes event listener.
         * @param {string} eventName Event name.
         * @param {function?} handler Handler function.
         */
        removeEventListener: function(eventName, handler) {
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
        },

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
        addEventListeners: function(listeners) {
            var eventName;
            for (eventName in listeners) {
                if (listeners.hasOwnProperty(eventName)) {
                    addEventListener(eventName, listeners[eventName]);
                }
            }
        }
}

EventModuleInstance = new EventModule();

EventModuleInstance.init();

