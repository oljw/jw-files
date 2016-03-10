/**
 * Created by JW on 3/4/2016.
 */

var canvas = null;
var dinoImg = null;
var groundImg = null;
var ctx = null;
var imageReady = false;
var runAnim = false;
var isPlaying = false;

Dino = {};
Dino.config = {
    SPRITE_HEIGHT: 47,
    SPRITE_WIDTH: 264,
    FRAME_WIDTH: 44,
    RUNNING_FRAME_RATE: 100,
    RUNNING_POS: [88, 132],
    POS_X: 50,
    POS_Y: 200
};
var dinoRunningIndex = 0;

Ground = {
    SPRITE_HEIGHT: 12,
    SPRITE_WIDTH: 1200,
    FRAME_WIDTH: 800,
    GROUND_MAX_WIDTH: 800,
    POS_X: 0,
    POS_Y: 242
};

window.requestAnimFrame = (function(){
    return  window.requestAnimationFrame       ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame    ||
            window.oRequestAnimationFrame      ||
            window.msRequestAnimationFrame     ||
            function( callback ){
                window.setTimeout(callback, 1000 / 60);
            };
})();

$(document).ready(function() {
    console.log("Document Ready");

    canvas = document.getElementById('gameScreen');
    ctx = canvas.getContext("2d");

    loadGroundImage();
    loadDinoImage();
    moveGround();
    dinoImg.onload = function () {
        imageReady = true;
        setTimeout(update, 1000/60);
    };

    resize();

    $(document).keydown(function() {
        console.log("Key Pressed");
        stopAnimation();
    });
});

function loadDinoImage() {
    dinoImg = new Image();
    dinoImg.src = "res/image/trex_move.png";
    $("#character").attr("src", dinoImg.src);
}

function moveGround() {
    $("#ground").animate({left: "-= 500"}, 10000);
}

function loadGroundImage() {
    groundImg = new Image();
    groundImg.src = "res/image/ground.png";
    $("#ground").attr("src", groundImg.src)
}

function redraw() {
    console.log("redraw called");
    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    if (imageReady) {
        ctx.drawImage(dinoImg, Dino.config.RUNNING_POS[dinoRunningIndex], 0, Dino.config.FRAME_WIDTH, Dino.config.SPRITE_HEIGHT,
            Dino.config.POS_X, Dino.config.POS_Y, Dino.config.FRAME_WIDTH, Dino.config.SPRITE_HEIGHT);
        ctx.drawImage(groundImg, 0, 0, Ground.FRAME_WIDTH, Ground.SPRITE_HEIGHT, Ground.POS_X, Ground.POS_Y, Ground.FRAME_WIDTH, Ground.SPRITE_HEIGHT);
    }
}

function resize() {
    //canvas.width = canvas.parentNode.clientWidth;
    //canvas.height = canvas.parentNode.clientHeight;
    canvas.width = 800;
    canvas.height = 300;
    redraw();
}

function stopAnimation() {
    window.cancelAnimationFrame(runAnim);
}

function update() {
    runAnim = requestAnimFrame(update);
    var runningIndex = getRunningDinoIndex();

    if (runningIndex !== dinoRunningIndex) {
        dinoRunningIndex = runningIndex;
        redraw();
    }
}

function getRunningDinoIndex() {
    var index = parseInt(Date.now() / Dino.config.RUNNING_FRAME_RATE) % Dino.config.RUNNING_POS.length;
    return index;
}