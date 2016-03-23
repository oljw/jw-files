//Define 3G Demo
threeGDemo = $("#threeGDemo").demo({
	name: '3GDemo',
	selfContained: true,
	currentIndex: 0,
	currentMenuIndex: 0,
	interactionOccurred: false,
	iconClicked: false,

	populateChapters : function(){
		var self = this;

		var watch = this.element.find('.c1').chapter();
		var call = this.element.find('.c2').chapter();
	
		var keypad = this.element.find('.c4').chapter();
		var message = this.element.find('.c5').chapter();
		var milkPreview = this.element.find('.milkPreview').chapter();
		var rotateAlt = this.element.find('.c6alt').chapter({
			onHandleStartOfChapter: function() {
				self.element.find('.indicatorContainer').css('transform', 'rotate(' + rotationPointsFitness[0].deg + 'deg)');
				self.element.find('.label').html(rotationPoints3G[0].name);
			}
		});
		
		var rotate = this.element.find('.c6').chapter({
			onHandleStartOfChapter: function() {
				threeGDemo.interactionOccurred = false;
				threeGDemo.iconClicked = false;
				self.element.find('.overlay').show();

				self.currentIndex = 0;

				self.element.find('.indicatorContainer').css('transform', 'rotate(' + rotationPoints3G[self.currentIndex].deg + 'deg)');

				self.element.find('.label').html(rotationPoints3G[0].name);
				
				self.element.find('.c6 .icon.milk').on('click', function() {
					if (!threeGDemo.iconClicked) {
						threeGDemo.iconClicked = true;

						try {
							var MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');
	
							var tapMilk =
								CommonMessagingDeserializerInstance.buildChapterObject('tapMilk');
	
							var applicationMessage =
								CommonMessagingDeserializerInstance.getWrappedApplicationMessageForBroadcast(MasterMessage.MessageType.SEEK_TO_END_OF_CHAPTER, 'SeekToEndOfChapterMessage', {chapter: tapMilk});
	
							SAP.sendMessage(applicationMessage);
						} catch (e) {}
	
						APP.manualChapterSeek();
					}
				});
			}
		});

		var milk = this.element.find('.c7').chapter();
		var milk2 = this.element.find('.c8').chapter();

		milk2.element.on('click', function() {
			try {
				var MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');

				var tapMilk =
					CommonMessagingDeserializerInstance.buildChapterObject('tapMilkNext');

				var applicationMessage =
					CommonMessagingDeserializerInstance.getWrappedApplicationMessageForBroadcast(MasterMessage.MessageType.SEEK_TO_END_OF_CHAPTER, 'SeekToEndOfChapterMessage', {chapter: tapMilk});

				SAP.sendMessage(applicationMessage);
			} catch (e) {}

			APP.manualChapterSeek();
		});

		var milk3 = this.element.find('.c9').chapter();
		var milk4 = this.element.find('.c10').chapter({
			onHandleStartOfChapter: function() {
				threeGDemo.interactionOccurred = false;

				//init menu
				var menu = this.element.find('.menuTextContainer');

				for (var index in milkStations) {
					menu.append('<div class="menuItem">' + milkStations[index].subgenreName + '</div>');
				}

				threeGDemo.currentMenuIndex = 1;
				threeGDemo.setLabelText(threeGDemo.currentMenuIndex);
				threeGDemo.setMenuPosition(threeGDemo.currentMenuIndex);
				threeGDemo.setIndicator(threeGDemo.currentMenuIndex, 0, true);
				threeGDemo.setBackground(threeGDemo.currentMenuIndex);
			}
		});


		var milk5 = this.element.find('.c11').chapter({
			onHandleStartOfChapter: function() {
				$(this.element.find('img')[0]).attr('src', 'images/demo/3g/milk_genre' + (threeGDemo.currentMenuIndex + 1) + '.png');
			}
		});

		var chapters = [];
		chapters.push(watch);
		chapters.push(call);

		chapters.push(keypad);
		chapters.push(message);
		chapters.push(milkPreview);
		chapters.push(rotateAlt);
		chapters.push(rotate);
		chapters.push(milk);
		chapters.push(milk2);
		chapters.push(milk3);
		chapters.push(milk4);
		chapters.push(milk5);

		this.chapters = $.fn.chapters(chapters);

		this.rotaryChapter = rotate;
		this.rotaryChapter.name = 'tapMilk';

		this.menuChapter = milk4;
		this.menuChapter.name = 'changeStation';

	},

	/**
	 * Send a gotoGenreMessage to the droid.
	 * @param index
	 */
	sendGotoGenre: function(index) {
		try {
			var MasterMessage = CommonMessagingDeserializerInstance.getMessageObject('MasterMessage');

			SAP.sendMessage(CommonMessagingDeserializerInstance.getWrappedApplicationMessageForBroadcast(MasterMessage.MessageType.GOTO_SONG, 'GotoSongMessage', {genreIndex: index, songIndex: 0}));
		} catch (e) { }
	},

	/**
	 * Set the milk genre background.
	 * @param index
	 */
	setBackground: function(index) {
		this.element.find('.background').attr('src', 'images/demo/3g/milk_frame4_background' + (index + 1) + '.png');
	},

	/**
	 * Set the milk top genre label.
	 * @param index
	 */
	setLabelText: function(index) {
		this.element.find('.label').html(milkStations[index].genreName);
	},

	/**
	 * Set the menuing text position.
	 * @param index
	 */
	setMenuPosition: function(index) {
		var pos = 65;

		pos -= index * 65;

		this.element.find('.menuTextContainer').stop();
		this.element.find('.menuTextContainer').animate({marginTop: pos}, 200);

		this.element.find('.menuItem').css({color: '#999'});
		$(this.element.find('.menuItem')[index]).css({color: '#fff'});
	},

	/**
	 * Get the degree for the indicator by genre index.
	 * @param index
	 * @returns
	 */
	getDegreeForGenre: function(index) {
		if (index == 0) {
			return 0;
		}

		return Math.round((360 / milkStations.length) * index);
	},

	/**
	 * Set the dot indicator position.
	 * @param index
	 * @param increment
	 * @param justSet
	 */
	setIndicator: function(index, increment, justSet) {
		if (!justSet) {
			var lastDegree = this.getDegreeForGenre(index - 1);

			if (increment) {
				lastDegree = this.getDegreeForGenre(index + 1);
			}

			this.element.find('.indicatorContainer').stop();
			AnimateRotate(	this.element.find('.indicatorContainer'),
							this.getDegreeForGenre(index));
		} else {
//			this.element.find('.indicatorContainer').stop();
			this.element.find('.indicatorContainer').css({transform: 'rotate(' + this.getDegreeForGenre(index) + 'deg)'});
		}
	},

	/**
	 * Stuff to do on bezel rotation for this demo.
	 * @param direction
	 */
	onRotaryEvent: function(direction) {
		if (this.menuChapter.element.is(":visible")) {
			if (!threeGDemo.interactionOccurred) {
				threeGDemo.interactionOccurred = true;
				APP.sendMessage(APP.buildTrackEventMessage("On Rotate", "ThreeG Demo" + " - " + this.menuChapter.name));
			}
			
//			var currentDate = new Date();
//			var currentTime = currentDate.getTime();
//			console.log("time diff: " + (currentTime - this.superTime));
//			var elapsedTime = currentTime - this.superTime;
//			this.superTime = currentTime;
//			
			if (direction == 'CW') {
				this.currentMenuIndex++;

				if (this.currentMenuIndex > milkStations.length - 1) {
					this.currentMenuIndex--;
					return;
				}

				threeGDemo.sendGotoGenre(this.currentMenuIndex);

				this.setLabelText(this.currentMenuIndex);
				this.setMenuPosition(this.currentMenuIndex);
				this.setIndicator(this.currentMenuIndex, false);
				this.setBackground(this.currentMenuIndex);
			} else {
				this.currentMenuIndex--;

				if (this.currentMenuIndex < 0) {
					this.currentMenuIndex++;
					return;
				}

				threeGDemo.sendGotoGenre(this.currentMenuIndex);

				this.setLabelText(this.currentMenuIndex);
				this.setMenuPosition(this.currentMenuIndex);
				this.setIndicator(this.currentMenuIndex, true);
				this.setBackground(this.currentMenuIndex);
			}
		} else if (this.rotaryChapter.element.is(":visible")) {
			if (!threeGDemo.interactionOccurred) {
				threeGDemo.interactionOccurred = true;
				APP.sendMessage(APP.buildTrackEventMessage("On Rotate", "ThreeG Demo" + " - " + this.rotaryChapter.name));
			}
			if (this.rotaryChapter.element.find('.overlay').is(":visible")) {
				this.rotaryChapter.element.find('.overlay').hide();
			} else {
				var icons = this.element.find('.c6 .icon'),
					lastDegrees = rotationPoints3G[this.currentIndex].deg;
					lastIndex = this.currentIndex;

				if (direction == 'CW') {
					this.currentIndex++;

					if (this.currentIndex > rotationPoints3G.length - 1) {
						this.currentIndex--;
						return;
					}
				} else {
					this.currentIndex--;

					if (this.currentIndex < 0) {
						this.currentIndex++;
						return;
					}
				}

				this.element.find('.label').html(rotationPoints3G[this.currentIndex].name);
				this.element.find('.indicatorContainer').stop();

				AnimateRotate(this.element.find('.indicatorContainer'), rotationPoints3G[this.currentIndex].deg);

				AnimateScale($(icons[this.currentIndex]), Constants.rotation.iconSize.large, Constants.rotation.iconSize.small);
				AnimateScale($(icons[lastIndex]), Constants.rotation.iconSize.small, Constants.rotation.iconSize.large);
			}
		}
	}
});

rotationPoints3G = [
{name: 'Previous pages', deg: 26},
{name: 'Alarm Clock', deg: 60},
{name: 'Timer', deg: 90},
{name: 'Stop watch', deg: 122},
{name: 'ESPN', deg: 152},
{name: 'Twitter', deg: 180},
{name: 'Uber', deg: 208},
{name: 'Milk Music', deg: 238},
{name: 'Buddy', deg: 270},
{name: 'Next page', deg: 305}
];

milkStations = [
          {
        	  genreName: "MY STATIONS",
        	  subgenreName: "My Stations: Top Hits Radio"
          },
          {
        	  genreName: "POP",
        	  subgenreName: "Pop Hits"
          },
          {
        	  genreName: "HIP HOP",
        	  subgenreName: "Hip Hop"
          },
          {
        	  genreName: "ROCK",
        	  subgenreName: "Rock"
          },
          {
        	  genreName: "ELECTRONIC",
        	  subgenreName: "Electronic"
          },
          {
        	  genreName: "LATIN",
        	  subgenreName: "Latin"
          },
          {
        	  genreName: "COUNTRY",
        	  subgenreName: "Country"
          },
          {
        	  genreName: "ALTERNATIVE",
        	  subgenreName: "Alternative"
          },
          {
        	  genreName: "DANCE",
        	  subgenreName: "Dance"
          },
          {
        	  genreName: "SPOTLIGHT",
        	  subgenreName: "Spotlight: Pop Hits"
          }
];
