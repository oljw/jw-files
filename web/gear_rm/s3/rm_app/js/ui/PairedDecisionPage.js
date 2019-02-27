/*
 * RetailSolis.ui.PairedDecisionPage class
 * [created by JW on 10/11/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var PairedDecisionPage = (function () {			
			function PairedDecisionPage() {
				try {
					console.log("PairedDecisionPage)+ ");
					
					this.videoFilePath = null;
					this.frameFilePath = null;
					
					PairedDecisionPage.videoTextureView = retail.videomanager.VideoTextureView.getInstance();
					
					this.init();
				} catch (err) {
					console.log("PairedDecisionPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PairedDecisionPage.videoTextureView = null;
			
			PairedDecisionPage.prototype.init = function() {
				try {
					console.log("PairedDecisionPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					
					PairedDecisionPage.videoTextureView.setVideo("raw/connected/HeroWatch12Hour.mp4", "images/paireddecisionpage/HeroWatch12Hour_frame.png", true);
					PairedDecisionPage.videoTextureView.play();
					
				} catch (err) {
					console.log("PairedDecisionPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			}
			
			PairedDecisionPage.prototype.startAnimation = function(anim) {
				$("#pairedDecisionPage").animateCss(anim);
			};
			
			/*
			 * class information
			 */
			PairedDecisionPage.PAGE = Constants.PageInfo.PairedDecisionPage.PAGE;
			PairedDecisionPage.CLASS = Constants.PageInfo.PairedDecisionPage.CLASS;
			PairedDecisionPage.HTML = Constants.PageInfo.PairedDecisionPage.HTML;
			PairedDecisionPage.prototype.getPage = function () {
				try {
					return PairedDecisionPage.PAGE;
				} catch (err) {
					console.log("PairedDecisionPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PairedDecisionPage.prototype.getClass = function () {
				try {
					return PairedDecisionPage.CLASS;
				} catch (err) {
					console.log("PairedDecisionPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PairedDecisionPage.prototype.getHtml = function () {
				try {
					return PairedDecisionPage.HTML;
				} catch (err) {
					console.log("PairedDecisionPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			PairedDecisionPage.prototype.activate = function () {
				try {
					var element = document.getElementById("pairedDecisionPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
				} catch (err) {
					console.log("PairedDecisionPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			PairedDecisionPage.prototype.deactivate = function () {
				try {
					PairedDecisionPage.videoTextureView.stop();
				} catch (err) {
					console.log("PairedDecisionPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			PairedDecisionPage.prototype.onerror = function (err) {
				console.log("PairedDecisionPage.prototype.onerror : exception [" + err.name + "] msg[" + err.message + "]");
			};
			
			return PairedDecisionPage;
		})();
		ui.PairedDecisionPage = PairedDecisionPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));


console.log("##### PairedDecisionPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.PairedDecisionPage());
console.log("##### PairedDecisionPage Ready)- ");