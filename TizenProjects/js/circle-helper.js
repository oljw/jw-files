/*global tau */
/*jslint unparam: true */
(function(tau) {
	var page,
		elScroller;

	if (tau.support.shape.circle) {
		document.addEventListener("pagebeforeshow", function (e) {
			page = e.target;
			elScroller = page.querySelector(".ui-scroller");

			if (elScroller) {
				elScroller.setAttribute("tizen-circular-scrollbar", "");
			}
		});

		document.addEventListener("pagebeforehide", function (e) {
			if(elScroller) {
				elScroller.removeAttribute("tizen-circular-scrollbar");
			}
		});
	}
}(tau));
