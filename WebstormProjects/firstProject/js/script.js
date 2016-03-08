/**
 * Created by JW on 3/4/2016.
 */
$(document).ready(function() {
    $(document).keydown(function() {
            console.log("hello world");
        $("#character").animate({top: "-=100px"}, 300,
            function(){
                $("#character").animate({top: "+=100px"}, 300)
        });
    });
});