#include <tizen.h>
#include <service_app.h>
#include <device/callback.h>
#include <device/power.h>
#include <device/display.h>
#include <app_control.h>

#include "retailnativeservice.h"

#define SIZE(s) (sizeof(s) / sizeof(s[0]))

static void onDisplayChangeCallback(device_callback_e type, void *value, void *user_data)
{
	display_state_e display_status;
	if (type != DEVICE_CALLBACK_DISPLAY_STATE) {
		return;
	}

	display_status = (display_state_e)value;
	dlog_print(DLOG_INFO, LOG_TAG, "@#@# Display status is %d", display_status);

	if (device_display_get_state(&display_status) == DEVICE_ERROR_NONE) {
		if (display_status == DISPLAY_STATE_SCREEN_OFF) {

			dlog_print(DLOG_INFO, LOG_TAG, "@#@# Launching RM App");

			// call Hero Web App
			if (LaunchRetailApp()) {
				int max_brightness;

				dlog_print(DLOG_INFO, LOG_TAG, "@#@# Succeeded to launch the RM app");
				device_power_wakeup(false);

				// lock cpu and display
//				device_power_request_lock(POWER_LOCK_CPU, 0);
//				device_power_request_lock(POWER_LOCK_DISPLAY , 0);
//
				device_display_change_state(DISPLAY_STATE_NORMAL);

				// max brightness
				device_display_get_max_brightness(0, &max_brightness);
				device_display_set_brightness(0, max_brightness);
			}
		}
	}
}

bool LaunchRetailApp() {
	int ret;
	app_control_h app_control;

	dlog_print(DLOG_INFO, LOG_TAG, "@#@# LaunchRetailApp)+ ");

	if (app_control_create(&app_control) != APP_CONTROL_ERROR_NONE) {
		dlog_print(DLOG_ERROR, LOG_TAG, "@#@# app_control_create() failed.");
		return false;
	}


//	if (app_control_set_operation(app_control, APP_CONTROL_OPERATION_DEFAULT) != APP_CONTROL_ERROR_NONE) {
//		dlog_print(DLOG_ERROR, LOG_TAG, "@#@# app_control_set_operation() failed.");
//	}

	if (app_control_set_app_id(app_control, "Ae3BvsMpgt.RetailSolis") != APP_CONTROL_ERROR_NONE) {
		dlog_print(DLOG_ERROR, LOG_TAG, "@#@# app_control_set_app_id() failed.");
		return false;
	}

	ret = app_control_send_launch_request(app_control, NULL, NULL);
	if (ret != APP_CONTROL_ERROR_NONE) {
		dlog_print(DLOG_ERROR, LOG_TAG, "@#@# Failed to launch the Hero RM app. Error : %d", ret);
		return false;
	}

	app_control_destroy(app_control);

	return true;

}

bool service_app_create(void *data)
{
	int error;

	dlog_print(DLOG_INFO, LOG_TAG, "@#@# The Hero Service create");

	// register the device display state
	error = device_add_callback(DEVICE_CALLBACK_DISPLAY_STATE, onDisplayChangeCallback, NULL);
	if (error != 0) {
		dlog_print(DLOG_ERROR, LOG_TAG, "@#@# device_add_callback error");
	}

    return true;
}

void service_app_terminate(void *data)
{
	int error;

	dlog_print(DLOG_INFO, LOG_TAG, "@#@# The Hero Service Terminate");
	error = device_remove_callback(DEVICE_CALLBACK_DISPLAY_STATE, onDisplayChangeCallback);

//	device_power_release_lock(POWER_LOCK_CPU);
//	device_power_release_lock(POWER_LOCK_DISPLAY);
    return;
}

void service_app_control(app_control_h app_control, void *data)
{
	//dlog_print(DLOG_INFO, LOG_TAG, "@#@# service_app_control");
    return;
}

static void
service_app_lang_changed(app_event_info_h event_info, void *user_data)
{
	/*APP_EVENT_LANGUAGE_CHANGED*/
	return;
}

static void
service_app_region_changed(app_event_info_h event_info, void *user_data)
{
	/*APP_EVENT_REGION_FORMAT_CHANGED*/
}

static void
service_app_low_battery(app_event_info_h event_info, void *user_data)
{
	/*APP_EVENT_LOW_BATTERY*/
}

static void
service_app_low_memory(app_event_info_h event_info, void *user_data)
{
	/*APP_EVENT_LOW_MEMORY*/
}

int main(int argc, char* argv[])
{
    char ad[50] = {0,};
	service_app_lifecycle_callback_s event_callback;
	app_event_handler_h handlers[5] = {NULL, };

	event_callback.create = service_app_create;
	event_callback.terminate = service_app_terminate;
	event_callback.app_control = service_app_control;

	service_app_add_event_handler(&handlers[APP_EVENT_LOW_BATTERY], APP_EVENT_LOW_BATTERY, service_app_low_battery, &ad);
	service_app_add_event_handler(&handlers[APP_EVENT_LOW_MEMORY], APP_EVENT_LOW_MEMORY, service_app_low_memory, &ad);
	service_app_add_event_handler(&handlers[APP_EVENT_LANGUAGE_CHANGED], APP_EVENT_LANGUAGE_CHANGED, service_app_lang_changed, &ad);
	service_app_add_event_handler(&handlers[APP_EVENT_REGION_FORMAT_CHANGED], APP_EVENT_REGION_FORMAT_CHANGED, service_app_region_changed, &ad);

	return service_app_main(argc, argv, &event_callback, ad);
}
