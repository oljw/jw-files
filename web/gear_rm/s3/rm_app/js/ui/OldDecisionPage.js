/*
 * RetailSolis.ui.DecisionPage class
 * [created by Long on 09/23/2016]
 */

var RetailSolis;
(function (RetailSolis) {
	(function (ui) {
		var DecisionPage = (function () {
			function DecisionPage() {
				try {
					console.log("DecisionPage)+ ");
					this.init();
				} catch (err) {
					console.log("DecisionPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.init = function() {
				try {
					console.log("DecisionPage.prototype.init)+ ");
					
					//set active for this page
					this.activate();
					
					//start 30 seconds timer
					this.startTimeout();
				} catch (err) {
					console.log("DecisionPage.prototype.init : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * class information
			 */
			DecisionPage.PAGE = Constants.PageInfo.DecisionPage.PAGE;
			DecisionPage.CLASS = Constants.PageInfo.DecisionPage.CLASS;
			DecisionPage.HTML = Constants.PageInfo.DecisionPage.HTML;
			DecisionPage.prototype.getPage = function () {
				try {
					return DecisionPage.PAGE;
				} catch (err) {
					console.log("DecisionPage.prototype.getPage : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DecisionPage.prototype.getClass = function () {
				try {
					return DecisionPage.CLASS;
				} catch (err) {
					console.log("DecisionPage.prototype.getClass : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DecisionPage.prototype.getHtml = function () {
				try {
					return DecisionPage.HTML;
				} catch (err) {
					console.log("DecisionPage.prototype.getHtml : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.startTimeout = function() {
				try {
					this.noActionTimeout = setTimeout(function(){
						RetailSolis.App.getInstance().changePage("js/ui/AttractorPage.html");
					}, 30000);
				} catch (err) {
					console.log("DecisionPage.prototype.startTimeout : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.cancelTimeout = function() {
				try {
					clearTimeout(this.noActionTimeout);
				} catch (err) {
					console.log("DecisionPage.prototype.cancelTimeout : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			DecisionPage.prototype.restartTimeout = function() {
				try {
					this.cancelTimeout();
					this.startTimeout();
				} catch (err) {
					console.log("DecisionPage.prototype.restartTimeout : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			/*
			 * activate / deactive function
			 */
			DecisionPage.prototype.activate = function () {
				try {
					var element = document.getElementById("decisionPage");
					var page = new tau.widget.Page(element);
					page.setActive(true);
					
					var changer = document.getElementById( "circularSectionchanger" ),
					sectionLength = document.querySelectorAll("section").length,
					elPageIndicator = document.getElementById("pageIndicator"),
					pageIndicator,
					pageIndicatorHandler
					that = this;
					this.sectionChanger = null;
				
					// make PageIndicator
					pageIndicator =  tau.widget.PageIndicator(elPageIndicator, { numberOfPages: sectionLength, layout: "circular" });
					pageIndicator.setActive(0);
					
					// make SectionChanger object
					this.sectionChanger = tau.widget.SectionChanger(changer, {
						circular: true,
						orientation: "horizontal",
						useBouncingEffect: true,
						fillContent: true
					});
					
					/**
					 * sectionchange event handler
					 */
					pageIndicatorHandler = function (e) {
						pageIndicator.setActive(e.detail.active);
						that.restartTimeout();
					};
	
					changer.addEventListener("sectionchange", pageIndicatorHandler, false);
					
					$("#decision-section-container").hide();
					$("#pageIndicator").hide();
		
					$("#decision-section-container section").click(function(){
						
						// TO DO: add code to change page to specific demo here.
						switch(this.id) {
							case "decision-design":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.DesignPage.HTML);
								break;
							case "decision-calls":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.CallsAndTextsPage.HTML);
								break;
							case "decision-onboard":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.OnboardGPSPage.HTML);
								break;
							case "decision-samsung":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.SamsungPayPage.HTML);
								break;
							case "decision-durability":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.DurabilityPage.HTML);
								break;
							case "decision-power":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.BatteryPage.HTML);
								break;
							case "decision-personalization":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.PersonalizePage.HTML);
								break;
							case "decision-fitness":
								RetailSolis.App.getInstance().changePage(Constants.PageInfo.SHealthPage.HTML);
								break;
						}
					});
					
					var counter = 0;
					document.addEventListener("rotarydetent", function(event){
						
						if($("#decision-section-container").css('display') === 'none') {
							$("#circularSectionchanger").css('background-color', '#000').css('background-image', '');
							$("#decision-section-container").show();
							$("#pageIndicator").show();
						} else {
							if (event.detail.direction === "CW") { 
								console.log("counter = " + counter);
								if(that.sectionChanger.getActiveSectionIndex() < 7 && counter > 0)
									that.sectionChanger.setActiveSection(that.sectionChanger.getActiveSectionIndex() + 1, 100);
								counter++;
							} else {
								if(that.sectionChanger.getActiveSectionIndex() > 0)
									that.sectionChanger.setActiveSection(that.sectionChanger.getActiveSectionIndex() - 1, 100);
							}
						}
						
					}, false);
				} catch (err) {
					console.log("DecisionPage.prototype.activate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			DecisionPage.prototype.deactivate = function () {
				try {
					this.cancelTimeout();
					this.sectionChanger.destroy();
				} catch (err) {
					console.log("DecisionPage.prototype.deactivate : exception [" + err.name + "] msg[" + err.message + "]");
				}
			};
			
			return DecisionPage;
		})();
		ui.DecisionPage = DecisionPage;
	})(RetailSolis.ui || (RetailSolis.ui = {}));
	var ui = RetailSolis.ui;
})(RetailSolis || (RetailSolis = {}));



console.log("##### DecisionPage Ready)+ ");
RetailSolis.App.getInstance().setCurrentPage(new RetailSolis.ui.DecisionPage());
console.log("##### DecisionPage Ready)- ");