/*
 * ViewController.js class
 * [created by JW on 10/10/2016]
 */

// THIS CLASS WILL BE IMPLEMENTED TO CONTROL DIFFERENT KINDS OF CONTROLLERS IN THE FUTURE. NOT ENOUGH TIME TO IMPLEMENT FULLY YET. //

var RetailSolis;
(function (RetailSolis) {
	(function (view) {
		(function (viewcontroller) {
			var ViewController = (function () {
				function ViewController() {
					try {
						console.log("ViewController)+ ");
						
						if (ViewController.sInstance)
							return ViewController.sInstance;
						ViewController.sInstance = this;
					} catch (err) {
						console.log("ViewController : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Get ViewController instance.
				 */
				ViewController.getInstance = function () {
					try {
						if (!ViewController.sInstance) {
							ViewController.sInstance = new ViewController();
						}
						return ViewController.sInstance;
					} catch (err) {
						console.log("ViewController.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				ViewController.sInstance = null;
				
			    /**
			     * Retrieve the ThumbnailChanger.
			     */
				ViewController.prototype.getThumbnailChanger = function() {
					try {
						if (this.thumbnailChanger)
							return this.thumbnailChanger;
						this.thumbnailChanger = new RetailSolis.view.thumbnailchanger.ThumbnailChanger();
						return this.thumbnailChanger;
					} catch (err) {
						console.log("ViewController.prototype.getThumbnailChanger : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				ViewController.thumbnailChanger = null;
				/**
				 * Show ThumbnailChanger.
				 */
				ViewController.prototype.showThumbnailChanger = function() {
					this.getThumbnailChanger().showThumbnailChanger();
				};
				
				/**
				 * Create html tags.
				 */
				ViewController.prototype.createHtmlTags = function(imgHolder) {
					this.getThumbnailChanger().setThumbnails(imgHolder);
				};
				
				/**
				 * Set Background Images.
				 */
				ViewController.prototype.setBackgroundImages = function(imgHolder) {
					this.getThumbnailChanger().setCssAttributes(imgHolder);
				};
				
				
				
				
				return ViewController;
			})();
			viewcontroller.ViewController = ViewController;
		})(RetailSolis.view.viewcontroller || (RetailSolis.view.viewcontroller = {}));
		var viewcontroller = RetailSolis.view.viewcontroller;
	})(RetailSolis.view || (RetailSolis.view = {}));
	var view = RetailSolis.view;
})(RetailSolis || (RetailSolis = {}));
