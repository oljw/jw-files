package com.vishnu.custcamshake;

import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class Main extends Activity implements SurfaceHolder.Callback,SensorEventListener {
    private SurfaceView prSurfaceView;
    private Button prStartBtn;
    private boolean prRecordInProcess;
    private SurfaceHolder prSurfaceHolder;
    private Camera prCamera;
    
	private float[] mGravity;
	private float mAccel;
	private float mAccelCurrent;
	private float mAccelLast;

	private Sensor accelerometer;
	Toast toast;

	private SensorManager sensorManager;

	Boolean bool = false, bool1 = false;
	Context ctx;
	int check = 0;
	private Context prContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        prContext = this.getApplicationContext();
        setContentView(R.layout.main);
        prSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        prStartBtn = (Button) findViewById(R.id.main_btn1);
        prRecordInProcess = false;
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        prStartBtn.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				if (prRecordInProcess == false) {
					startRecording();
				} else {
					stopRecording();
				}
			}
		});
    
        prSurfaceHolder = prSurfaceView.getHolder();
        prSurfaceHolder.addCallback(this);
        prSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		prMediaRecorder = new MediaRecorder();
    }

	@Override
	public void surfaceChanged(SurfaceHolder _holder, int _format, int _width, int _height) {
		Camera.Parameters lParam = prCamera.getParameters();
		prCamera.setParameters(lParam);
		try {
			prCamera.setPreviewDisplay(_holder);
			prCamera.startPreview();
		} catch (IOException _le) {
			_le.printStackTrace();
		}
	}

	//@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		prCamera = Camera.open();
		if (prCamera == null) {
			Toast.makeText(this.getApplicationContext(), "Camera is not available!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	//@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if (prRecordInProcess) {
			stopRecording();
		} else {
			prCamera.stopPreview();
		}
		prMediaRecorder.release();
		prMediaRecorder = null;
		prCamera.release();
		prCamera = null;
	}
	
	private MediaRecorder prMediaRecorder;
	private final int cMaxRecordDurationInMs = 30000;
	private final long cMaxFileSizeInBytes = 5000000;
	
	private void updateEncodingOptions() {
		if (prRecordInProcess) {
			stopRecording();
			startRecording();
			
		} else {
			
		}
	}
	
	private boolean startRecording() {
		prCamera.stopPreview();
		try {
			prCamera.unlock();
			prMediaRecorder.setCamera(prCamera);
			//set audio source as Microphone, video source as camera
			//state: Initial=>Initialized
			prMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			prMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			CamcorderProfile camcorderProfile_HQ = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
			prMediaRecorder.setProfile(camcorderProfile_HQ);			
			prMediaRecorder.setOutputFile("/storage/emulated/0/myvideo.mp4");
			prMediaRecorder.setMaxDuration(cMaxRecordDurationInMs); // Set max duration 
			prMediaRecorder.setMaxFileSize(cMaxFileSizeInBytes); // Set max file size 
			prMediaRecorder.setPreviewDisplay(prSurfaceHolder.getSurface());
			prMediaRecorder.prepare();
			//start recording
			//state: prepared => recording
			prMediaRecorder.start();
			prStartBtn.setText("Stop");
			prRecordInProcess = true;
			return true;
		} catch (IOException _le) {
			_le.printStackTrace();
			return false;
		}
	}
	
	private void stopRecording() {
		prMediaRecorder.stop();
		prMediaRecorder.reset();
		try {
			prCamera.reconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		prStartBtn.setText("Start");
		prRecordInProcess = false;
		prCamera.startPreview();
	}
	
	private static final int REQUEST_DECODING_OPTIONS = 0;
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	switch (requestCode) {
    	case REQUEST_DECODING_OPTIONS:
    		if (resultCode == RESULT_OK) {
    			updateEncodingOptions();
    		}
    		break;
    	}
	}
	public void sendMessage() {
		final AlertDialog alertDialog = new AlertDialog.Builder(Main.this)
				.create();
		alertDialog.setTitle("video shaking !!!");
		alertDialog.setCanceledOnTouchOutside(true);
		if(!alertDialog.isShowing()){
			
		    alertDialog.show();
		
		    new Handler().postDelayed(new Runnable() {
				public void run() {
					alertDialog.dismiss();
				}
			}, 400);
		}
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(final SensorEvent event) {
		// TODO Auto-generated method stub
		  if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
		if (check == 0)
		{
			check = 1;
			new Handler().postDelayed(new Runnable()
			{
			      public void run()
			      {
			    		  String s = null;
					        mGravity = event.values.clone();
					        // Shake detection
					        float x = mGravity[0];
					        float y = mGravity[1];
					        float z = mGravity[2];
					        mAccelLast = mAccelCurrent;
					        mAccelCurrent = FloatMath.sqrt(x*x + y*y + z*z);
					        float delta = mAccelCurrent - mAccelLast;
					        mAccel = mAccel * 0.0f + delta;
					            // Make this higher or lower according to how much
					            // motion you want to detect
					         s = Float.toString(mAccel);
					        if(mAccel > 0.2){ 
					        // do something				        	
					        	sendMessage();			      	
					        }
					    	check = 0;
			      }
			}, 500);
		}
	}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        finish();
        }
		return bool;
}
}