/**
 * Created by JW on 3/4/2016.
 */

var canvas = null;
var dinoImg = new Image();
var groundImg = new Image();
groundImg.src = "res/image/ground.png";
var ctx = null;
var imageReady = false;
var runAnim = false;
var isPlaying = false;
var scale = 1.05;
var speed = 30;
var y = 250;
var clearX;
var clearY;
var groundW;
var groundH;
var dx = 0.75;
var x = 0;

var CANVAS_WIDTH = 800;
var CANVAS_HEIGHT = 300;

var Dino = {};
Dino.attr = {
    SPRITE_HEIGHT: 47,
    SPRITE_WIDTH: 264,
    FRAME_WIDTH: 44,
    RUNNING_FRAME_RATE: 100,
    RUNNING_POS: [88, 132],
    POS_X: 50,
    POS_Y: 200
};
var dinoRunningIndex = 0;

var Ground = {
    SPRITE_HEIGHT: 12,
    SPRITE_WIDTH: 1200,
    FRAME_WIDTH: 800,
    GROUND_MAX_WIDTH: 800,
    //GROUND_MOVING_FRAME_RATE: 200,
    POS_X: 0,
    POS_Y: 230
};

$(document).ready(function() {
    console.log("Document Ready");

    //$(document).keydown(function() {
    //    console.log("Key Pressed");
    //    stopRunAnimation();
    //});

    canvas = document.getElementById('gameScreen');
    ctx = canvas.getContext("2d");

    //loadGroundImage();
    loadDinoImage();

    dinoImg.onload = function () {
        imageReady = true;
        setTimeout(update, 1000/60);
    };

    groundImg.onload = function() {
        groundW = groundImg.width * scale;
        groundH = groundImg.height * scale;
        if (groundW > CANVAS_WIDTH) {
            x = CANVAS_WIDTH - groundW;
        }
        if (groundW > CANVAS_WIDTH) {
            clearX = groundW;
        } else {
            clearY = CANVAS_HEIGHT;
        }
        return setInterval(drawGround, speed);
    }
});

function redraw() {
    console.log("redraw called");

    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    if (imageReady) {
        ctx.drawImage(dinoImg, Dino.attr.RUNNING_POS[dinoRunningIndex], 0, Dino.attr.FRAME_WIDTH, Dino.attr.SPRITE_HEIGHT,
            Dino.attr.POS_X, Dino.attr.POS_Y, Dino.attr.FRAME_WIDTH, Dino.attr.SPRITE_HEIGHT);

        //var offsetX = parseInt(Date.now() / 6) % Ground.FRAME_WIDTH;
        //
        //ctx.drawImage(groundImg, 0, 0, Ground.SPRITE_WIDTH, Ground.SPRITE_HEIGHT,
        //    Ground.POS_X - offsetX, Ground.POS_Y, Ground.SPRITE_WIDTH, Ground.SPRITE_HEIGHT);
        //
        //ctx.drawImage(groundImg, 0, 0, Ground.SPRITE_WIDTH, Ground.SPRITE_HEIGHT,
        //    Ground.POS_X + Ground.SPRITE_WIDTH - offsetX, Ground.POS_Y, Ground.FRAME_WIDTH, Ground.SPRITE_HEIGHT);
    }
}

function drawGround() {
    ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

    if(groundW <= CANVAS_WIDTH) {
        if (x > CANVAS_WIDTH) {
            x = 0;
        }
        if (x > (CANVAS_WIDTH - groundW)) {
            ctx.drawImage(groundImg, CANVAS_WIDTH + 1, y, groundW, groundH);
        }
    }

    else {
        if(x > CANVAS_WIDTH) {
            x = CANVAS_WIDTH - groundW;
        }
        if(x > CANVAS_WIDTH - groundW) {
            ctx.drawImage(groundImg, x - groundW + 1, y, groundW, groundH);
        }
    }

    ctx.drawImage(groundImg, x, y, groundW, groundH);

    x += dx;
}

function update() {
    console.log("update called")
    //runAnim = requestAnimFrame(update);
    dinoRunningIndex = getRunningDinoIndex();

    redraw();
}

function getRunningDinoIndex() {
    var index = parseInt(Date.now() / Dino.attr.RUNNING_FRAME_RATE) % Dino.attr.RUNNING_POS.length;
    return index;
}

function loadDinoImage() {
    dinoImg.src = "res/image/trex_move.png";
    $("#character").attr("src", dinoImg.src);
}

function loadGroundImage() {
    groundImg.src = "res/image/ground.png";
    $("#ground").attr("src", groundImg.src);
}

function stopRunAnimation() {
    window.cancelAnimationFrame(runAnim);
}

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



