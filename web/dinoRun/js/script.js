/**
 * Created by JW on 3/4/2016.
 */
var canvas = null;
var dinoImg = null;
var groundImg = null;
var smObstaImg = null;
var lgObstaImg = null;
var scoreImg = null;
var cloudImg = null;
var ctx = null;
var jumping = null;
var imageReady = false;
var gameAnimation = false;
var isPlaying = false;
var isGameOver = false;
var goingDown = false;
var inAir = false;
var obstaArray = [];
var scale = 1.01;
var dinoRunningIndex = 0;
var obstacleIndex = 0;
var jumpVelocity = 0;
var lastObsta = 0;
var nextObsta = 0;
var dinoCut = 7;
var bigCut = 7;
var smallCut = 4;
var startTime = 0;
var score = 0;
var highScore = 0;

var Game = {
    CANVAS_WIDTH: 360,
    CANVAS_HEIGHT: 360,
    GAME_SPEED: 3.5,
    BASE_GAME_SPEED: 3.5,
    INITIAL_Y: 100,
    INITIAL_X: 0,
    END_X: 360,
    MAX_OBSTA_ON_SCREEN: 4,
    FPS: 60,
    ACCELERATION: 0.0002,
    ACCELERATION_INTERVAL_TIME: 10
};

var Dino = {
    SPRITE_HEIGHT: 53,
    SPRITE_WIDTH: 263,
    FRAME_WIDTH: 44,
    RUNNING_FRAME_RATE: 100,
    RUNNING_POS: [88, 132],
    JUMP_POS: 0,
    DEAD_POS: 176,
    JUMP_LIMIT: 59.04,
    JUMP_VELOCITY: jumpVelocity += 8,
    JUMP_INITIAL_VELOCITY: 8,
    JUMP_GRAVITY: 0.33,
    INITIAL_POS_Y: 159,
    POS_X: 20,
    POS_Y: 159			
};
Dino.collisionBox = {
    POS_X: Dino.POS_X - dinoCut,
    POS_Y: Dino.POS_Y + dinoCut,
    FRAME_WIDTH: Dino.FRAME_WIDTH - (dinoCut * 2),
    FRAME_HEIGHT: Dino.SPRITE_HEIGHT - (dinoCut * 2)
};

var Ground = {
    SPRITE_HEIGHT: 12,
    SPRITE_WIDTH: 300,
    GROUND_NEW_WIDTH: 0,
    GROUND_NEW_HEIGHT: 0,
    POS_X: 360,
    POS_Y: 200
};

var Obstacle = {};
Obstacle.sm = {
    SPRITE_HEIGHT: 35,
    SPRITE_WIDTH: 102,
    FRAME_WIDTH: 35,
    OBSTACLE_START_X: 360,
    OBSTACLE_X_FRAME_SIZE_POS: [17, 34, 51],
    OBSTACLE_POS: [0, 34, 51],
    OBSTACLE_FRAME_RATE: 1000,
    OBSTACLE_NEW_WIDTH: 0,
    OBSTACLE_NEW_HEIGHT: 0,
    POS_X: 0,
    POS_Y: 177
};

Obstacle.lg = {
    SPRITE_HEIGHT: 50,
    SPRITE_WIDTH: 150,
    FRAME_WIDTH: 35,
    OBSTACLE_START_X: 360,
    OBSTACLE_X_FRAME_SIZE_POS: [25, 50, 75],
    OBSTACLE_POS: [0, 25, 75],
    OBSTACLE_FRAME_RATE: 1000,
    OBSTACLE_NEW_WIDTH: 0,
    OBSTACLE_NEW_HEIGHT: 0,
    POS_X: 0,
    POS_Y: 162
};

var Score = {
	SPRITE_HEIGHT: 24,
	SPRITE_WIDTH: 382,
	HI_TEXT_X_POS: 101.5,
	HI_TEXT_Y_POS: 35.5,
	HI_TEXT_WIDTH: 40,
	HI_TEXT_IMG_POS: 200,
	HIGH_SCORE_X_POS: 155.5,
	HIGH_SCORE_Y_POS: 35.5,
	SCORE_X_POS: 133.5,
	SCORE_Y_POS: 71,
	NUMBER_WIDTH: 20,
	HIGHSCORE_VAL_KEY : "highScoreVal"
};

var GameOverText = {
	SPRITE_HEIGHT: 23,
	SPRITE_WIDTH: 206,
	GAME_OVER_TEXT_X_POS: 77,
	GAME_OVER_TEXT_Y_POS: 168.5,
	START_Y_ON_IMG: 24
}

function DrawBox(x, y, w, h) {
    ctx.strokeStyle = "transparent";
    this.x = x;
    this.y = y;
    this.width = w;
    this.height = h;

	box = ctx.strokeRect(x, y, w, h);
    return box;
}

window.addEventListener('tizenhwkey', function(e) {
    if (e.keyName == "back") {
        $.mobile.back();
        window.cancelAnimationFrame(gameAnimation);

        cleanGame();
    }
});

document.addEventListener("DOMContentLoaded", function() {
    canvas = document.getElementById('gameScreen');
    startButton = document.getElementById('startButton');
    ctx = canvas.getContext("2d");
    tau.event.disableGesture(canvas);

    startButton.onclick = function() {
        resetGame();
    }
    
    canvas.addEventListener("touchstart", function(e) {
    	console.log(Game.GAME_SPEED)
    	if (isPlaying && !isGameOver) {
            if (inAir) {return;}
            jumping = setInterval(dinoJump, 1000 / (Game.FPS * 1.65));
        } else if (isGameOver) {
            resetGame();
        }
    }, false);
    loadScore();
});

//document.addEventListener("rotarydetent", function(ev) {
//    var direction = ev.detail.direction;
//    /* Add behavior for detent detected event with a direction value */
//
//	   if (isPlaying && !isGameOver) {
//		   if (direction == 'CW') {
//	            if (inAir) {return;}
//	            jumping = setInterval(dinoJump, 1000 / (Game.FPS * 1.65));
//	            jumping.stop();
//		   } else {
//	            if (inAir) {return;}
//	            jumping = setInterval(dinoJump, 1000 / (Game.FPS * 1.65));
//	            jumping.stop();
//		   }
//	   }
//});

function reDraw() {
    ctx.fillStyle = '#99ccff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    if (imageReady) {
    	
        //Ground Movement
        if (Ground.POS_X < 0) {
            Ground.POS_X = Ground.GROUND_NEW_WIDTH;
        }
        if (Ground.POS_X > 0) {
            ctx.drawImage(groundImg, Ground.POS_X - Ground.GROUND_NEW_WIDTH, Ground.POS_Y, Ground.GROUND_NEW_WIDTH, Ground.GROUND_NEW_HEIGHT);
        }
        ctx.drawImage(groundImg, Ground.POS_X, Ground.POS_Y, Ground.GROUND_NEW_WIDTH, Ground.GROUND_NEW_HEIGHT);

        Ground.POS_X -= Game.GAME_SPEED;    

        //DINO
        if (Dino.POS_Y >= Dino.INITIAL_POS_Y) {
            ctx.drawImage(dinoImg, Dino.RUNNING_POS[dinoRunningIndex], 0, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT, Dino.POS_X, Dino.POS_Y, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT);

            var dinoBox = new DrawBox(Dino.POS_X + dinoCut, Dino.POS_Y + dinoCut, Dino.FRAME_WIDTH - dinoCut * 2, Dino.SPRITE_HEIGHT - dinoCut * 2);
        } else {
            ctx.drawImage(dinoImg, Dino.JUMP_POS, 0, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT, Dino.POS_X, Dino.POS_Y, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT);

            var dinoBox = new DrawBox(Dino.POS_X + dinoCut, Dino.POS_Y + dinoCut, Dino.FRAME_WIDTH - dinoCut * 2, Dino.SPRITE_HEIGHT - dinoCut * 2);
        }
        
        //Obstacles
        for (var obsta in obstaArray) {
            var temp = obstaArray[obsta];
            var obstaState = temp["obstaState"];
            if (temp["chooseOneBro"] === 0) {
                //Obstacle Small
                ctx.drawImage(smObstaImg, Obstacle.sm.OBSTACLE_POS[obstaState], 0, Obstacle.sm.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.sm.SPRITE_HEIGHT,
                    temp["posX"], Obstacle.sm.POS_Y, Obstacle.sm.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.sm.OBSTACLE_NEW_HEIGHT);

                var smObstaBox = new DrawBox(temp["posX"] + smallCut, Obstacle.sm.POS_Y + smallCut, Obstacle.sm.OBSTACLE_X_FRAME_SIZE_POS[obstaState] - smallCut * 2, Obstacle.sm.OBSTACLE_NEW_HEIGHT - smallCut);

                Obstacle.sm.POS_X -= Game.GAME_SPEED;

                if (dinoBox.x < smObstaBox.x + smObstaBox.width &&
                    dinoBox.x + dinoBox.width > smObstaBox.x &&
                    dinoBox.y < smObstaBox.y + smObstaBox.height &&
                    dinoBox.y + dinoBox.height > smObstaBox.y) {

                    gameOver();
                    console.log("Dead");
                }
            } else {
                //Obstacle Large
                ctx.drawImage(lgObstaImg, Obstacle.lg.OBSTACLE_POS[obstaState], 0, Obstacle.lg.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.lg.SPRITE_HEIGHT,
                    temp["posX"], Obstacle.lg.POS_Y, Obstacle.lg.OBSTACLE_X_FRAME_SIZE_POS[obstaState], Obstacle.lg.OBSTACLE_NEW_HEIGHT);

                var lgObstaBox = new DrawBox(temp["posX"] + bigCut, Obstacle.lg.POS_Y + bigCut, Obstacle.lg.OBSTACLE_X_FRAME_SIZE_POS[obstaState] - bigCut * 2, Obstacle.lg.OBSTACLE_NEW_HEIGHT - bigCut);

                Obstacle.lg.POS_X -= Game.GAME_SPEED;

                if (dinoBox.x < lgObstaBox.x + lgObstaBox.width &&
                    dinoBox.x + dinoBox.width > lgObstaBox.x &&
                    dinoBox.y < lgObstaBox.y + lgObstaBox.height &&
                    dinoBox.y + dinoBox.height > lgObstaBox.y) {

                    gameOver();
                    console.log("Dead");
                }
            }
        }
        drawScoreBoard();
    }
}

function update() {
    gameAnimation = requestAnimFrame(update);
    dinoRunningIndex = getRunningDinoIndex();

    if (isPlaying) {
        for (var obsta in obstaArray) {
            var temp = obstaArray[obsta];
            temp["posX"] -= Game.GAME_SPEED;

            //Limit 4 obstacles on screen
            if (obstaArray.length > Game.MAX_OBSTA_ON_SCREEN) {
                obstaArray.shift();
            }
        }
    }
    //Removing the first obstacle
    delete obstaArray[0];

    if (nextObsta <= Date.now()) {
        var chooseOneBro = Math.floor(Math.random() * 2);
        var obstaCount;
        if (chooseOneBro === 0) {
            obstaCount = Obstacle.sm.OBSTACLE_POS.length;
        } else {
            obstaCount = Obstacle.lg.OBSTACLE_POS.length;
        }

        var obstaState = parseInt(Math.random() * obstaCount);
        obstaArray.push({
            "posX": Game.END_X,
            "obstaState": obstaState,
            "chooseOneBro": chooseOneBro
        });

        //distance between obstacles.
        if (Game.GAME_SPEED < 8) {
            var obstaWidth = Math.floor((Math.random() * 2000) + 700);
        } else if (Game.GAME_SPEED > 8 && Game.GAME_SPEED < 13) {
            var obstaWidth = Math.floor((Math.random() * 1000) + 250);
        } else if (Game.GAME_SPEED > 13 && Game.GAME_SPEED < 20) {
            var obstaWidth = Math.floor((Math.random() * 500) + 125);
        } else {
            var obstaWidth = Math.floor((Math.random() * 250) + 62.5);
        }
        nextObsta = Date.now() + obstaWidth;
    }
    reDraw();
}

function gameOver() {
    isGameOver = true;
    window.cancelAnimationFrame(gameAnimation);
    
    //game over text TODO Change it smaller
  //game over text TODO Change it smaller
    ctx.drawImage(scoreImg, 0, GameOverText.START_Y_ON_IMG, GameOverText.SPRITE_WIDTH, GameOverText.SPRITE_HEIGHT,
    		GameOverText.GAME_OVER_TEXT_X_POS, GameOverText.GAME_OVER_TEXT_Y_POS, GameOverText.SPRITE_WIDTH, GameOverText.SPRITE_HEIGHT);
    
    //dead dino face
    ctx.drawImage(dinoImg, Dino.DEAD_POS, 0, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT, 
    		Dino.POS_X, Dino.POS_Y, Dino.FRAME_WIDTH, Dino.SPRITE_HEIGHT);
    
    // TODO save it if new highscore
    saveScore();
}

function getRunningDinoIndex() {
    var index = parseInt(Date.now() / Dino.RUNNING_FRAME_RATE) % Dino.RUNNING_POS.length;
    return index;
}

function drawScoreBoard() {
    var elapsedTime = Date.now() - startTime;
    score = parseInt(elapsedTime / 100);

    if (score > highScore) {
        highScore = score;
    }
    
    drawScore(highScore, Score.HIGH_SCORE_X_POS, Score.HIGH_SCORE_Y_POS); 
    drawScore(score, Score.SCORE_X_POS, Score.SCORE_Y_POS); 
    
    //HI
    ctx.drawImage(scoreImg, Score.HI_TEXT_IMG_POS, 0, Score.HI_TEXT_WIDTH, Score.SPRITE_HEIGHT,
    		Score.HI_TEXT_X_POS, Score.HI_TEXT_Y_POS, Score.HI_TEXT_WIDTH, Score.SPRITE_HEIGHT);
}

function saveScore() {
    localStorage.setItem(Score.HIGHSCORE_VAL_KEY, highScore);
}

function loadScore() {
    highScore = localStorage.getItem(Score.HIGHSCORE_VAL_KEY);
    // TODO better way to check?
    if (highScore == null) {
        highScore = 0;
    }
}

function drawScore(score, scorePositionX, scorePositionY) {
    var intElapsed = score;

    var thousands = parseInt(intElapsed / 1000);
    ctx.drawImage(scoreImg, thousands * Score.NUMBER_WIDTH, 0, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT,
		scorePositionX, scorePositionY, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT);
    intElapsed -= thousands * 1000;
    var hundreds = parseInt(intElapsed / 100);
    ctx.drawImage(scoreImg, hundreds * Score.NUMBER_WIDTH, 0, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT,
		scorePositionX + 22, scorePositionY, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT);
    intElapsed -= hundreds * 100;
    var tens = parseInt(intElapsed / 10);
    ctx.drawImage(scoreImg, tens * Score.NUMBER_WIDTH, 0, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT,
		scorePositionX + 44, scorePositionY, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT);
    intElapsed -= tens * 10;
    ctx.drawImage(scoreImg, intElapsed * Score.NUMBER_WIDTH, 0, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT,
		scorePositionX + 66, scorePositionY, Score.NUMBER_WIDTH, Score.SPRITE_HEIGHT);
}

function reSize() {
    canvas.width = Game.CANVAS_WIDTH;
    canvas.height = Game.CANVAS_HEIGHT;
    reDraw();
}

function dinoJump() {
    inAir = true;
    if (Dino.POS_Y > Dino.JUMP_LIMIT && !goingDown) {
        Dino.POS_Y -= Dino.JUMP_VELOCITY;
        Dino.JUMP_VELOCITY -= Dino.JUMP_GRAVITY;
    } else {
        goingDown = true;
        Dino.POS_Y += Dino.JUMP_VELOCITY;
        Dino.JUMP_VELOCITY += Dino.JUMP_GRAVITY;
        if (Dino.POS_Y >= Dino.INITIAL_POS_Y) {
            clearInterval(jumping);
            goingDown = false;
            inAir = false;
            Dino.JUMP_VELOCITY = Dino.JUMP_INITIAL_VELOCITY;
            Dino.POS_Y = Dino.INITIAL_POS_Y;
        }
    }
}

function resetGame() {
    window.cancelAnimationFrame(gameAnimation);
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    startTime = Date.now();
    isPlaying = true;
    obstaArray.splice(0, obstaArray.length);
    loadAllImages();
    imagesOnload();
    reSize();
    isGameOver = false;
    Game.GAME_SPEED = Game.BASE_GAME_SPEED;
    (function raiseSpeed() {
        Game.GAME_SPEED += Game.ACCELERATION;
        setTimeout(raiseSpeed, Game.ACCELERATION_INTERVAL_TIME);
        return Game.GAME_SPEED;
    })();
}

function cleanGame() {
	if(isPlaying){return;}
	Game.GAME_SPEED = null;
	startTime = null;
    obstaArray.splice(0, obstaArray.length);
}

function loadAllImages() {
    dinoImg = new Image();
    dinoImg.src = "images/dino_move.png";

    groundImg = new Image();
    groundImg.src = "images/ground.png";

    smObstaImg = new Image();
    smObstaImg.src = "images/obstacle_sm.png";

    lgObstaImg = new Image();
    lgObstaImg.src = "images/obstacle_lg.png";

    scoreImg = new Image();
    scoreImg.src = "images/time.png";
}

function imagesOnload() {
    groundImg.onload = function() {
        imageReady = true;

        Ground.GROUND_NEW_WIDTH = groundImg.width * scale;
        Ground.GROUND_NEW_HEIGHT = groundImg.height * scale;
    };
    
    dinoImg.onload = function() {
        setTimeout(update, 1000 / Game.FPS);
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

window.requestAnimFrame = (function() {
    return window.requestAnimationFrame ||
        window.webkitRequestAnimationFrame ||
        window.mozRequestAnimationFrame ||
        window.oRequestAnimationFrame ||
        window.msRequestAnimationFrame ||
        function(callback) {
            window.setTimeout(callback, 1000 / Game.FPS);
        };
})();