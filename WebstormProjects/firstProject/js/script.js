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
var scale = 1.01;

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
    GROUND_MAX_WIDTH: 800,
    GROUND_MOVING_SPEED: 5,
    GROUND_NEW_WIDTH: 0,
    GROUND_NEW_HEIGHT: 0,
    POS_X: 0,
    POS_Y: 230
};

$(document).ready(function() {
    console.log("Document Ready");

    canvas = document.getElementById('gameScreen');
    ctx = canvas.getContext("2d");

    loadGroundImage();
    loadDinoImage();

    dinoImg.onload = function () {
        imageReady = true;
        setTimeout(update, 1000/60);
    };

    groundImg.onload = function() {
        console.log("test 0");

        Ground.GROUND_NEW_WIDTH = groundImg.width * scale;
        Ground.GROUND_NEW_HEIGHT = groundImg.height * scale;
    };
    resize();
});

function redraw() {
    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    if (imageReady) {
        ctx.drawImage(dinoImg, Dino.attr.RUNNING_POS[dinoRunningIndex], 0, Dino.attr.FRAME_WIDTH, Dino.attr.SPRITE_HEIGHT,
            Dino.attr.POS_X, Dino.attr.POS_Y, Dino.attr.FRAME_WIDTH, Dino.attr.SPRITE_HEIGHT);

        if(Ground.GROUND_NEW_WIDTH <= Ground.GROUND_MAX_WIDTH) {
        //    console.log("##test 2");
        //
        //    if (Ground.POS_X > CANVAS_WIDTH) {
        //        console.log("###test 3");
        //        Ground.POS_X = 0;
        //    }
        //    if (Ground.POS_X > (CANVAS_WIDTH - GROUND_NEW_WIDTH)) {
        //        console.log("####test 4");
        //
        //        ctx.drawImage(groundImg, CANVAS_WIDTH + 1, Ground.POS_Y, GROUND_NEW_WIDTH, GROUND_NEW_HEIGHT);
        //    }
        }

        else {
            if(Ground.POS_X < 0) {
                Ground.POS_X = Ground.GROUND_NEW_WIDTH;
            }
            if(Ground.POS_X > 0) {
                ctx.drawImage(groundImg, Ground.POS_X - Ground.GROUND_NEW_WIDTH, Ground.POS_Y, Ground.GROUND_NEW_WIDTH, Ground.GROUND_NEW_HEIGHT);
            }
        }
        ctx.drawImage(groundImg, Ground.POS_X, Ground.POS_Y, Ground.GROUND_NEW_WIDTH, Ground.GROUND_NEW_HEIGHT);

        Ground.POS_X -= Ground.GROUND_MOVING_SPEED;
    }
}

function update() {
    runAnim = requestAnimFrame(update);
    dinoRunningIndex = getRunningDinoIndex();

    redraw();
}

function getRunningDinoIndex() {
    var index = parseInt(Date.now() / Dino.attr.RUNNING_FRAME_RATE) % Dino.attr.RUNNING_POS.length;
    return index;
}

function resize() {
    canvas.width = 800;
    canvas.height = 300;
    redraw();
}

function loadDinoImage() {
    dinoImg = new Image();
    dinoImg.src = "res/image/trex_move.png";
    $("#character").attr("src", dinoImg.src);
}

function loadGroundImage() {
    groundImg = new Image();
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

$(document).keydown(function() {
    console.log("Key Pressed");
    stopRunAnimation();
});

