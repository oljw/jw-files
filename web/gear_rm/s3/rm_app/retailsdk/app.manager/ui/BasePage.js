/*
 * RetailSolis.ui.BasePage class
 * [created by JW on 10/17/2016]
 */

var __extends = this.__extends || function (d, b) {
	for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
	function __() { this.constructor = d; }
	__.prototype = b.prototype;
	d.prototype = new __();
};

var PAGE = null;
var CLASS = null;
var HTML = null;
var pageId = null;

var app;
(function (app) {
	(function (manager) {
		(function (ui) {
			var BasePage = (function () {
				function BasePage() {
					console.log("BasePage)+ ");

                    this.currentControl = null;
                }
				
				BasePage.prototype.whoAmI = function() {
					console.log("################# I am a BasePage");
				};

                BasePage.prototype.activate = function () {
                    try {
                        console.log("################# ACTIVATE!!!!!!!!!!!!");
                        var element = document.getElementById(pageId);
                        var page = new tau.widget.Page(element);
                        page.setActive(true);
                    } catch (err) {
                        console.log("DesignPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
                    }
                };
                BasePage.prototype.deactivate = function () {
                    try {
                        console.log("################# DEACTIVATE!!!!!!!!!!!!");

                    } catch (err) {
                        console.log("DesignPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
                    }
                };

                // BasePage.prototype.getCurrentControl = function () {
                //     try {
                //         console.log("DesignPage.prototype.getCurrentControl)+ ");
                //         return this.currentControl;
                //     } catch (err) {
                //         console.log("DesignPage.prototype.getCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
                //     }
                // };
                // BasePage.prototype.setCurrentControl = function (currentControl) {
                //     try {
                //         console.log("DesignPage.prototype.setCurrentControl)+ currentControl = " + currentControl);
                //         this.currentControl = currentControl;
                //     } catch (err) {
                //         console.log("DesignPage.prototype.setCurrentControl : exception [" + err.name + "] msg[" + err.message + "]");
                //     }
                // };

                BasePage.prototype.onStartDemo = function () {
                    // BasePage.videoTextureView.play();
                };

				/*
				 * class information
				 */
                BasePage.prototype.getPage = function () {
                	try {
                        console.log("################# BASEPAGE " + PAGE);
                        return PAGE;
                	} catch (err) {
                		console.log("DesignPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
                	}
                };
                BasePage.prototype.getClass = function () {
                	try {
                        console.log("################# BASEPAGE " + CLASS);
                        return CLASS;
                    } catch (err) {
                		console.log("DesignPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
                	}
                };
                BasePage.prototype.getHtml = function () {
                	try {
                        console.log("################# BASEPAGE " + HTML);
                        return HTML;
                    } catch (err) {
                		console.log("DesignPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
                	}
                };

				return BasePage;
			})();
			ui.BasePage = BasePage;
		})(app.manager.ui || (app.manager.ui = {}));
		var ui = app.manager.ui;
	})(app.manager || (app.manager = {}));
	var manager = app.manager;
})(app || (app = {}));

console.log("##### BasePage Ready)+ ");
