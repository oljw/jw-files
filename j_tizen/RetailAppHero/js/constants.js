/**
 * Constants is used for our communication between devices and the TV. 
 * This should map the same constants on the mobile app.
 * @type {Object}
 */
var Constants = {
	"eventType": {
		"REQUEST_DEVICE_INFORMATION": "requestDeviceInformation",
        "DEVICE_INFORMATION": "deviceInformation",
        "PATH_EVENT": "pathEvent",
        "IS_MASTER": "isMaster",
        "REQUEST_MASTER": "requestMaster",
        "CHANGE_MASTER": "changeMaster",
        "CHANGE_MASTER_COMPLETE": "changeMasterComplete",
        "CHANGE_CHAPTER": "changeChapter",
        "END_OF_CHAPTER": "endOfChapter",
        "SEEK_TO_END_OF_CHAPTER": "seekToEndOfChapter",
        "CHANGE_DEMO": "changeDemo",
        "PLAY_DEMO": "playDemo",
        "PAUSE_DEMO": "pauseDemo",
        "START_DEMO": "startDemo",
        "END_DEMO": "endDemo",
        "LEGAL" : "legal",
        "PING": "ping",
        "START_OF_CHAPTER": "startOfChapter"
	},
	"eventProperty": {
		"DEVICE_TYPE": "deviceType",
        "CLIENT_ID": "clientId",
        "REQUEST_NAME": "requestName",
        "REQUEST_ID": "requestID",
        "TYPE": "type",
        "EVENT": "event",
        "DATA": "data"
	},
	"demoValues": {
		"ATTRACTOR": "Attractor_Activity",
        "DESIGN": "Design",
        "ACCESS": "Access",
        "THREEG": "3g",
        "FITNESS": "Fitness",
        "BATTERY": "Battery"
	},
	"eventPropertyValue": {},
    "wearableChannelId":                "125",
    "wearableRunTitle":                 "pzero",
    "wearableAppName":                  "pzero",
    "fileStoragePath":					"file:///opt/usr/media/Downloads/",
    "attractorFileName": 				"attractor.mp4",
    "rotation":	{
    	"iconSize": {
    		"small": .9,
    		"large": 1
    	}
    }
};