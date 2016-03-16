/**
 * Created by JW on 3/4/2016.
 */

var canvas = null;
var dinoImg = null;
var groundImg = null;
var smObstaImg = null;
var scoreImg = null;
var ctx = null;
var imageReady = false;
var runAnim = false;
var isPlaying = false;
var scale = 1.01;
var goingDown = false;
var dinoRunningIndex = 0;
var obstacleIndex = 0;
var jumping = null;
var inAir = false;
var jumpVelocity = 0;

var startTime = 0;

var Dino = {
    SPRITE_HEIGHT: 47,
    SPRITE_WIDTH: 264,
    FRAME_WIDTH: 44,
    RUNNING_FRAME_RATE: 100,
    RUNNING_POS: [88, 132],
    JUMP_POS: 0,
    POS_X: 50,
    POS_Y: 200,
    JUMP_LIMIT: 100,
    JUMP_VELOCITY: jumpVelocity += 8,
    JUMP_GRAVITY: 0.33
};

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

var Score = {
    SPRITE_HEIGHT: 21,
    SPRITE_WIDTH: 3000, // update
    NUMBER_WIDTH: 20
};

var Obstacle = {

};

Obstacle.sm = {
//    SM_OBSTA1 = function() {
//    smObstaImg = new Image();
//    //smObstaImg.src = "res/image/obstacle_sm.png";
//}
    SPRITE_HEIGHT: 35,
    SPRITE_WIDTH: 102,
    FRAME_WIDTH: 35,
    OBSTACLE_START_X: 1000,
    OBSTACLE_FRAME_POS: [35, 17.5, 52.5],
    OBSTACLE_POS: [0, 35, 52.5],
    OBSTACLE_FRAME_RATE: 1000,
    POS_X: 0,
    POS_Y: 215,
    OBSTACLE_NEW_WIDTH: 0,
    OBSTACLE_NEW_HEIGHT: 0
};

//Obstacle.series = {
//    obsta1: ctx.drawImage(smObstaImg, this.sm.OBSTACLE_POS[currentIndex], 0, this.sm.OBSTACLE_FRAME_POS[currentIndex], this.sm.SPRITE_HEIGHT,
//        this.sm.POS_X - this.sm.OBSTACLE_NEW_WIDTH, this.sm.POS_Y, this.sm.OBSTACLE_FRAME_POS[currentIndex], this.sm.OBSTACLE_NEW_HEIGHT),
//
//    obsta2: ctx.drawImage(smObstaImg, this.sm.OBSTACLE_POS[currentIndex], 0, this.sm.OBSTACLE_FRAME_POS[currentIndex], this.sm.SPRITE_HEIGHT,
//        this.sm.POS_X - this.sm.OBSTACLE_NEW_WIDTH, this.sm.POS_Y, this.sm.OBSTACLE_FRAME_POS[currentIndex], this.sm.OBSTACLE_NEW_HEIGHT),
//
//    obsta3: ctx.drawImage(smObstaImg, this.sm.OBSTACLE_POS[currentIndex], 0, this.sm.OBSTACLE_FRAME_POS[currentIndex], this.sm.SPRITE_HEIGHT,
//        this.sm.POS_X - this.sm.OBSTACLE_NEW_WIDTH, this.sm.POS_Y, this.sm.OBSTACLE_FRAME_POS[currentIndex], this.sm.OBSTACLE_NEW_HEIGHT)
//};



//Obstacle.lg = {
//
//};



$(document).ready(function() {
    console.log("Document Ready");

    canvas = document.getElementById('gameScreen');
    ctx = canvas.getContext("2d");

    $(document).keydown(function(e) {
        console.log("Key Pressed");

        switch (e.keyCode) {
            case 80 || 27:
                console.log("Stop");
                stopRunAnimation();
                break;

            case 32 || 0:
                if(!isPlaying){
                    console.log("Play");
                    isPlaying = true;

                    loadAllImages();

                    startTime = Date.now();

                    dinoImg.onload = function () {
                        imageReady = true;
                        setTimeout(update, 1000/60);
                    };

                    groundImg.onload = function() {
                        Ground.GROUND_NEW_WIDTH = groundImg.width * scale;
                        Ground.GROUND_NEW_HEIGHT = groundImg.height * scale;
                    };

                    smObstaImg.onload = function() {
                        Obstacle.sm.OBSTACLE_NEW_WIDTH = smObstaImg.width * scale;
                        Obstacle.sm.OBSTACLE_NEW_HEIGHT = smObstaImg.height * scale;
                    };

                    resize();
                    break;
                } else {
                    //Jump
                    if(inAir){ return }
                    jumping = setInterval(jump, 1000/60);
                    break;
                }

        }
    });
});

var obstaVisible = false;
var currentIndex = null;
var obstaState = 0;

function redraw() {

    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    if (imageReady) {
        if(Dino.POS_Y >= 200) {
            ctx.drawImage(dinoImg, Dino.RUNNING_POS[dinoRunningIndex], 0, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT,
                Dino.POS_X, Dino.POS_Y, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT);
        } else {
            ctx.drawImage(dinoImg, Dino.JUMP_POS, 0, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT, Dino.POS_X,
            Dino.POS_Y, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT);
        }

        //Ground Movement
        if(Ground.POS_X < 0) {
            Ground.POS_X = Ground.GROUND_NEW_WIDTH;
        }
        if(Ground.POS_X > 0) {
            ctx.drawImage(groundImg, Ground.POS_X - Ground.GROUND_NEW_WIDTH, Ground.POS_Y, Ground.GROUND_NEW_WIDTH, Ground.GROUND_NEW_HEIGHT);
        }
        ctx.drawImage(groundImg, Ground.POS_X, Ground.POS_Y, Ground.GROUND_NEW_WIDTH, Ground.GROUND_NEW_HEIGHT);

        Ground.POS_X -= Ground.GROUND_MOVING_SPEED;

        //Obstacle small
        if(Obstacle.sm.POS_X < 0) {
            Obstacle.sm.POS_X = 1000;
            obstaVisible = false;
            currentIndex = null;
        }

        if (Obstacle.sm.POS_X > 0) {
            if(currentIndex == null) {
                if(obstaVisible) { return; }
                obstaState = parseInt(Math.random() * 3);
            }
            currentIndex = obstaState;

            ctx.drawImage(smObstaImg, Obstacle.sm.OBSTACLE_POS[currentIndex], 0, Obstacle.sm.OBSTACLE_FRAME_POS[currentIndex], Obstacle.sm.SPRITE_HEIGHT,
                Obstacle.sm.POS_X - Obstacle.sm.OBSTACLE_NEW_WIDTH, Obstacle.sm.POS_Y, Obstacle.sm.OBSTACLE_FRAME_POS[currentIndex], Obstacle.sm.OBSTACLE_NEW_HEIGHT);

            obstaVisible = true;
        }
        Obstacle.sm.POS_X -= Ground.GROUND_MOVING_SPEED;



        //Time board
        var elapsedTime = Date.now() - startTime;
        var intElapsed = parseInt(elapsedTime / 100) % 10;
        var scorePosition = 20;
        ctx.drawImage(scoreImg, intElapsed * Score.NUMBER_WIDTH, 0, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT,
            scorePosition, scorePosition, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT);
    }
}

function jump() {
    inAir = true;
    //console.log("jumping " + "Pos_Y: " + Dino.POS_Y);
    if(Dino.POS_Y > Dino.JUMP_LIMIT && !goingDown) {
        Dino.POS_Y -= Dino.JUMP_VELOCITY;
        Dino.JUMP_VELOCITY -= Dino.JUMP_GRAVITY;
    } else {
        goingDown = true;
        Dino.POS_Y += Dino.JUMP_VELOCITY;
        Dino.JUMP_VELOCITY += Dino.JUMP_GRAVITY;
        if(Dino.POS_Y >= 200) {
            clearInterval(jumping);
            goingDown = false;
            inAir = false;
            Dino.JUMP_VELOCITY = 8;
        }
    }
    //console.log("velocity: " +  jumpVelocity);
}

function update() {
    runAnim = requestAnimFrame(update);
    dinoRunningIndex = getRunningDinoIndex();

    redraw();
}

function getRunningDinoIndex() {
    var index = parseInt(Date.now() / Dino.RUNNING_FRAME_RATE) % Dino.RUNNING_POS.length;
    return index;
}

function resize() {
    canvas.width = 800;
    canvas.height = 300;
    redraw();
}

function loadAllImages() {
    dinoImg = new Image();
    dinoImg.src = "res/image/dino_move.png";
    $("#character").attr("src", dinoImg.src);

    groundImg = new Image();
    groundImg.src = "res/image/ground.png";
    $("#ground").attr("src", groundImg.src);

    smObstaImg = new Image();
    smObstaImg.src = "res/image/obstacle_sm.png";

    scoreImg = new Image();
    scoreImg.src = "res/image/time.png";
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

