/**
 * Animate a rotation.
 * @param element
 * @param angle
 */
function AnimateRotate(element, angle) {
    element.animate({deg: angle}, {
        duration: 200,
        step: function(now) {
            element.css({
                transform: 'rotate(' + now + 'deg)'
            });
        }
    });
}

/**
 * Animate scaling up/down of an element.
 * @param element
 * @param newScale
 * @param lastScale
 */
function AnimateScale(element, newScale, lastScale) {
	console.log(element + ',' + newScale + ',' + lastScale);
    $({scale: lastScale}).animate({scale: newScale}, {
        duration: 300,
        step: function(now) {
            element.css({
                transform: 'scale(' + now + ', ' + now + ')'
            });
        }
    });
}