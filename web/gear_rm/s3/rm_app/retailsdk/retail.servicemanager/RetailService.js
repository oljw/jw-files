/*
 * retail.servicemanager.RetailService class
 * [created by icanmobile on 09/21/2016]
 * This class has functions in order to start Tizen Native Service.
 */

var retail;
(function (retail) {
	(function (servicemanager) {
		var RetailService = (function () {
			/*
			 * getInstance function - Singleton
			 */
			RetailService.getInstance = function () {
				try {
					if (!RetailService.sInstance) {
						RetailService.sInstance = new RetailService();
					}
					return RetailService.sInstance;
				} catch (err) {
					console.log("RetailService.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			RetailService.sInstance = null;

			
			/*
			 * construct function
			 */
			function RetailService() {
				try {
					if (RetailService.sInstance)
						return RetailService.sInstance;
					RetailService.sInstance = this;
					
					this.serviceId = 'Ae3BvsMpgt.retailnativeservice';
				} catch (err) {
					console.log("RetailService : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			/*
			 * set Service Id function
			 */
			RetailService.prototype.setServiceId = function (serviceId) {
				try {
					this.serviceId = serviceId;
				} catch (err) {
					console.log("RetailService.prototype.setServiceId : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * start service function
			 */
			RetailService.prototype.start = function () {
				try {
					console.log("RetailService.prototype.start)+ ");
					tizen.application.getAppsContext(this.onGetAppsContextSuccess, this.onGetAppContextError);
				} catch (err) {
					console.log("RetailService.prototype.start : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * success listener
			 */
			RetailService.prototype.onGetAppsContextSuccess = function (contexts) {
				try {
					console.log("RetailService.prototype.onGetAppsContextSuccess)+ ");
					var index = 0;
					for (index = 0 ; index < contexts.length ; index++) {		
						console.log(contexts[index].appId);
						
						if (contexts[index].appId == this.serviceId)
							break;
					}
					
					if (index >= contexts.length)
						RetailService.getInstance().launch();
				} catch (err) {
					console.log("RetailService.prototype.onGetAppsContextSuccess : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
		
			/*
			 * error listener
			 */
			RetailService.prototype.onGetAppContextError = function (err) {
				console.log("RetailService.prototype.onGetAppContextError : " + err.message);
			};
			
			/*
			 * luanch service function
			 */
			RetailService.prototype.launch = function () {
				try {
					console.log("RetailService.prototype.launch)+ ");
					tizen.application.launch(this.serviceId, this.onsuccesslaunch, this.onerrorlaunch);
				} catch (err) {
					console.log("RetailService.prototype.launch : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			RetailService.prototype.onsuccesslaunch = function () {
				console.log("RetailService.prototype.onsuccesslaunch)+ ");
			};
			RetailService.prototype.onerrorlaunch = function (err) {
				console.log("RetailService.prototype.onerrorlaunch)+ ");
				console.log("RetailService.prototype.onerrorlaunch : exception [" + err.name + "] msg[" + err.message + "]");
				this.start();
			};

			return RetailService;
		})();
		servicemanager.RetailService = RetailService;
	})(retail.servicemanager || (retail.servicemanager = {}));
	var servicemanager = retail.servicemanager;
})(retail || (retail = {}));