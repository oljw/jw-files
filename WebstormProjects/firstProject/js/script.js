/**
 * Created by JW on 3/4/2016.
 */

var canvas = null;
var dinoImg = null;
var groundImg = null;
var smObstaImg = null;
var lgObstaImg = null;
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
var lastObsta = 0;
var nextObsta = 0;
var obstaArray = [];

var startTime = 0;

var Game = {
    CANVAS_WIDTH: 800,
    CANVAS_HEIGHT: 300,
    GAME_SPEED: 5,
    INITIAL_Y: 200,
    INITIAL_X: 0,
    END_X: 800,
    MAX_OBSTA_ON_SCREEN: 5,
    FPS: 60
};

var Dino = {
    SPRITE_HEIGHT: 47,
    SPRITE_WIDTH: 264,
    FRAME_WIDTH: 44,
    RUNNING_FRAME_RATE: 100,
    RUNNING_POS: [88, 132],
    JUMP_POS: 0,
    JUMP_LIMIT: 100,
    JUMP_VELOCITY: jumpVelocity += 8,
    JUMP_INITIAL_VELOCITY: 8,
    JUMP_GRAVITY: 0.33,
    POS_X: 50,
    POS_Y: 200
};

var Ground = {
    SPRITE_HEIGHT: 12,
    SPRITE_WIDTH: 1200,
    GROUND_NEW_WIDTH: 0,
    GROUND_NEW_HEIGHT: 0,
    POS_X: 0,
    POS_Y: 230
};

var Obstacle = {};
Obstacle.sm = {
    SPRITE_HEIGHT: 35,
    SPRITE_WIDTH: 102,
    FRAME_WIDTH: 35,
    OBSTACLE_START_X: 1000,
    OBSTACLE_X_FRAME_SIZE_POS: [17, 34, 51],
    OBSTACLE_POS: [0, 35, 52.5],
    OBSTACLE_FRAME_RATE: 1000,
    OBSTACLE_NEW_WIDTH: 0,
    OBSTACLE_NEW_HEIGHT: 0,
    POS_X: 0,
    POS_Y: 215
};

Obstacle.lg = {
    SPRITE_HEIGHT: 50,
    SPRITE_WIDTH: 150,
    FRAME_WIDTH: 35,
    OBSTACLE_START_X: 1000,
    OBSTACLE_X_FRAME_SIZE_POS: [25, 50, 75],
    OBSTACLE_POS: [0, 25, 75],
    OBSTACLE_FRAME_RATE: 1000,
    OBSTACLE_NEW_WIDTH: 0,
    OBSTACLE_NEW_HEIGHT: 0,
    POS_X: 0,
    POS_Y: 0
};

var Score = {
    SPRITE_HEIGHT: 21,
    SPRITE_WIDTH: 3000, // update
    NUMBER_WIDTH: 20
};

function setY(element) {
    var y = (Game.CANVAS_HEIGHT/2) - element.SPRITE_HEIGHT + 100;
    //element.POS_Y = y;
    //return y;
    //var y = Game.CANVAS_HEIGHT - element.SPRITE_HEIGHT - 48;
    element.POS_Y = y;
    return y;
}
function getY(element) {

}

$(document).ready(function() {
    console.log("Document Ready");

    console.log(setY(Dino));
    console.log(Dino.POS_Y);
    console.log(setY(Obstacle.lg) + Obstacle.lg.SPRITE_HEIGHT);


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
                    imagesOnload();
                    reSize();
                    break;
                } else {
                    //Jump
                    if(inAir){ return }
                    jumping = setInterval(dinoJump, 1000/Game.FPS);
                    break;
                }
        }
    });
});

function reDraw() {

    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    if (imageReady) {
        if(Dino.POS_Y >= 200) {
            ctx.drawImage(dinoImg, Dino.RUNNING_POS[dinoRunningIndex], 0, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT,
                Dino.POS_X, setY(Dino), Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT);
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

        Ground.POS_X -= Game.GAME_SPEED;



        //Obstacles
        for(var obsta in obstaArray){
            var temp = obstaArray[obsta];
            var obstaState = temp["obstaState"];
            if(temp["chooseOneBro"] === 0) {
                //Obstacle Small
                ctx.drawImage(smObstaImg, Obstacle.sm.OBSTACLE_POS[obstaState], 0, Obstacle.sm.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.sm.SPRITE_HEIGHT,
                    temp["posX"], setY(Obstacle.sm), Obstacle.sm.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.sm.OBSTACLE_NEW_HEIGHT);

                Obstacle.sm.POS_X -= Game.GAME_SPEED;
            } else {
                //Obstacle Large
                ctx.drawImage(lgObstaImg, Obstacle.lg.OBSTACLE_POS[obstaState], 0, Obstacle.lg.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.lg.SPRITE_HEIGHT,
                    temp["posX"], setY(Obstacle.lg), Obstacle.lg.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.lg.OBSTACLE_NEW_HEIGHT);

                Obstacle.lg.POS_X -= Game.GAME_SPEED;
            }
        }

        if(Dino.POS_X < temp["posX"] + Obstacle.sm.FRAME_WIDTH &&
            Dino.POS_X + Dino.FRAME_WIDTH > temp["posX"] &&
            Dino.POS_Y < setY(Obstacle.sm) + temp["posX"] &&
            Dino.SPRITE_HEIGHT + Dino.POS_Y > setY(Obstacle.sm)) {

            console.log("what");
        }

        //Time board
        var elapsedTime = Date.now() - startTime;
        var intElapsed = parseInt(elapsedTime / 100) % 10;
        var scorePosition = 20;
        ctx.drawImage(scoreImg, intElapsed * Score.NUMBER_WIDTH, 0, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT,
            scorePosition, scorePosition, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT);
    }
}

function update() {
    runAnim = requestAnimFrame(update);
    dinoRunningIndex = getRunningDinoIndex();

    if(isPlaying) {
        for(var obsta in obstaArray){
            var temp = obstaArray[obsta];
            temp["posX"] -= Game.GAME_SPEED;

             //Limit 4 obstacles on screen)
            if(obstaArray.length > Game.MAX_OBSTA_ON_SCREEN){
               obstaArray.shift();
            }
        }
    }
    //Removing the first obstacle
    delete obstaArray[0];

    if(nextObsta <= Date.now()){
        var chooseOneBro = Math.floor(Math.random() * 2);
        var obstaCount;
        if (chooseOneBro === 0) {
            obstaCount = Obstacle.sm.OBSTACLE_POS.length;
        } else {
            obstaCount = Obstacle.lg.OBSTACLE_POS.length;
        }

        var obstaState = parseInt(Math.random() * obstaCount);
        obstaArray.push(
            {"posX": Game.END_X, "obstaState": obstaState, "chooseOneBro": chooseOneBro}
        );

        //distance between obstacles. need update these numbers to constant.
        var obstaWidth = Math.floor((Math.random() * 2000) + 500);
        nextObsta = Date.now() + obstaWidth;
    }
    reDraw();
}

function getRunningDinoIndex() {
    var index = parseInt(Date.now() / Dino.RUNNING_FRAME_RATE) % Dino.RUNNING_POS.length;
    return index;
}

function reSize() {
    canvas.width = Game.CANVAS_WIDTH;
    canvas.height = Game.CANVAS_HEIGHT;
    reDraw();
}

function dinoJump() {
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
            Dino.JUMP_VELOCITY = Dino.JUMP_INITIAL_VELOCITY;
        }
    }
    //console.log("velocity: " +  jumpVelocity);
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

    lgObstaImg = new Image();
    lgObstaImg.src = "res/image/obstacle_lg.png";

    scoreImg = new Image();
    scoreImg.src = "res/image/time.png";
}

function imagesOnload() {
    dinoImg.onload = function () {
        imageReady = true;
        setTimeout(update, 1000/Game.FPS);
    };

    groundImg.onload = function() {
        Ground.GROUND_NEW_WIDTH = groundImg.width * scale;
        Ground.GROUND_NEW_HEIGHT = groundImg.height * scale;
    };

    smObstaImg.onload = function() {
        Obstacle.sm.OBSTACLE_NEW_WIDTH = smObstaImg.width * scale;
        Obstacle.sm.OBSTACLE_NEW_HEIGHT = smObstaImg.height * scale;
    };

    lgObstaImg.onload = function() {
        Obstacle.lg.OBSTACLE_NEW_WIDTH = lgObstaImg.width * scale;
        Obstacle.lg.OBSTACLE_NEW_HEIGHT = lgObstaImg.height * scale;
    };
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
            window.setTimeout(callback, 1000 / Game.FPS);
        };
})();

