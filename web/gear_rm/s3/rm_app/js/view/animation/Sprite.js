var RetailSolis;
(function (RetailSolis) {
	(function (view) {
		(function (animation) {
			var Sprite = (function () {
				function Sprite() {
					try {
						console.log("Sprite)+ ");

						this.spriteInterval = null;
						this.imageFolder = null;
						this.imageCount = 0;
						this.frameDuration = 0;
						this.index = 0;
						this.removeWhenTouched = true;	// default
						this.animationCount;
						this.initialAnimationCount = -1;	// -1 is infinity

//						this.init();
						
					} catch (err) {
						console.log("Sprite : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
																
				/*
				 * getInstance function - Singleton
				 */
				Sprite.getInstance = function () {
					try {
						if (!Sprite.sInstance) {
							Sprite.sInstance = new Sprite();
						}
						return Sprite.sInstance;
					} catch (err) {
						console.log("Sprite.getInstance : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				Sprite.sInstance = null;
				
				Sprite.prototype.setInfo = function (imgFolder, imgCount, frameDuration, count) {
					try {
						console.log("Sprite setImages() imgFolder: " + imgFolder + ", imgCount: " + 
								imgCount  + ", frameDuration: " + frameDuration + ", count: " + count);
						this.imageFolder = imgFolder;
						this.imageCount = imgCount;
						this.frameDuration = frameDuration;
						this.initialAnimationCount = count || -1;

						$('#spriteAnimation').css({left: 0, top: 0});

					} catch (err) {
						console.log("Sprite setInfo : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				Sprite.prototype.setPosition = function (posX, posY) {
					try {
						$('#spriteAnimation').css({left: posX, top: posY});
					} catch (err) {
						console.log("Sprite setPosition : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				/**
				 * Start sprite show
				 */
				Sprite.prototype.start = function() {
					console.log("Sprite.prototype.start");
					try {
						if (!this.imageFolder || this.imageCount == 0 || this.frameDuration == 0) {
							console.log("Sprite can't start sprite: imageFolder: " + this.imageFolder + ", imageCount: " + 
									this.imageCount  + ", frameDuration: " + this.frameDuration);
							return;
						}
						
						this.animationCount = this.initialAnimationCount;						
						
						this.index = 0;
						var that = this;
						
						var holder = $("#sprite-image-holder");
//						var holder2 = $("#sprite-image-holder_two");
						
						this.spriteInterval = setInterval(function() {
							// TODO make it better
							var src;
							if (that.index < 10) {
								src = that.imageFolder + "0" + that.index + '.png';
							} else {
								src = that.imageFolder + that.index + '.png';
							}
							
//							if (that.index % 2 == 0) {
//								holder1.attr('src', src);
//							} else {
//								holder2.attr('src', src);
//							}
							holder.attr('src', src);	
							
							if (++that.index >= that.imageCount) {
								that.index = 0;
								
								if (that.animationCount != -1) {
									that.animationCount--;
									
									if (that.animationCount == 0) {
										that.stop();
										return;
									}
								}
							}
							
							
							
						}, that.frameDuration);
					} catch (err) {
						console.log("Sprite start : exception [" + err.name + "] msg[" + err.message + "]");
					}
				};
				
				Sprite.prototype.stop = function() {
					console.log("Sprite.prototype.stop");
					try {
						if (this.spriteInterval) {
							clearInterval(this.spriteInterval);
							this.spriteInterval = null;
							
							$("#sprite-image-holder").attr('src', "");
//							$('#sprite-div-holder').css("background-image", "url('')");
						}
					} catch (err) {
						console.log("Sprite stop : exception [" + err.name + "] msg[" + err.message + "]");
					}

				};

				/**
				 * Show Sprite
				 */
				Sprite.prototype.showSprite = function() {
					$("#overlay-container").load("js/view/animation/Sprite.html");
				};
				
				return Sprite;
			})();
			animation.Sprite = Sprite;
		})(RetailSolis.view.animation || (RetailSolis.view.animation = {}));
		var animation = RetailSolis.view.animation;
	})(RetailSolis.view || (RetailSolis.view = {}));
	var view = RetailSolis.view;
})(RetailSolis || (RetailSolis = {}));


