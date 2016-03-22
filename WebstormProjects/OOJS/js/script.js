/**
 * Created by JW on 3/8/2016.
 */

function a() {
    alert("A");
    return function b() {
        alert("B");
    }
}

$(document).ready(function() {
    a();
    a();
    a();
});