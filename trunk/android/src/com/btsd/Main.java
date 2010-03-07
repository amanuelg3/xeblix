package com.btsd;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.btsd.BTScrewDriverStateMachine.States;
import com.btsd.util.MessagesEnum;

public class Main extends Activity implements CallbackActivity{
	
	private static final String TAG = "Main";
	
	public static final String TYPE = "type";
	public static final String VALUE = "value";
	public static final String STATUS = "status";
	public static final String HOST_ADDRESS = "address";
	public static final String KEY_CODES = "keycodes";
	public static final String PINCODE = "pincode";
	public static final String REMOTE = "remote";
	public static final String SEND_COUNT = "count";
	public static final String MESSAGE_ID = "MESSAGE_ID";
	
	private BTScrewDriverCallbackHandler callbackHandler;
	private BTScrewDriverAlert pauseAlert;
	private KeyguardManager mKeyguardManager = null;
	private KeyguardManager.KeyguardLock mKeyguardLock;

	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		Log.e(TAG, "onRestart");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.e(TAG, "onPause");
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		Log.e(TAG, "onPause");
		//enableKeyguard();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		Log.e(TAG, "onResume");
		//make sure the callback handler is initialized
		//callbackHandler = new BTBrokerCallbackHandler(this);
		
		//application is potentially going to be displayed again, start 
		//contacting the server, hopefully by the time the window is
		//displayed communication will be established
		/*bluetoothBroker.connectToBluetoothServer();
		showConnectingDialog = true;
		Log.e(TAG, "showConnectinDialog=true; onResume");
		*/
		
		//TODO: fix disableKeyguard support
		//disableKeyguard();
		
		BTSDApplication application =  (BTSDApplication)getApplication();
		application.getStateMachine().registerActivity(this);
		application.getStateMachine().getBTConnectionState(this);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		BTScrewDriverCallbackHandler.focusChangedEvent(this, hasFocus);
		
		//check if we are still waiting for communication to be 
		//established, if so put up a connecting dialog
		/*Log.e(TAG, "showConnectinDialog=" + showConnectingDialog +  "; onWindowFocusChanged");
		if(showConnectingDialog){
			showDialog(this, R.string.CONNECTING_TO_BT_SERVER, false);
			showConnectingDialog = false;
		}*/
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.e(TAG, "onActivityResult");
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Log.e(TAG, "onCreate");
        
        Button button = (Button) findViewById(R.id.SonyButton);
        button.setOnClickListener(new OnClickListener() {
        	//private static final int EVENT_INITIALIZE=1;
			public void onClick(View arg0) {
				Log.i(TAG, "Sony Button clicked");
				Intent intent = new Intent("com.btsd.sonyBravia");
				startActivity(intent);
			}

		});
        
        button = (Button) findViewById(R.id.MotorolaUverse);
        button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Log.i(TAG, "Motorola Button clicked");
				Intent intent = new Intent("com.btsd.vip1200");
				startActivity(intent);
			}

		});
        
        button = (Button) findViewById(R.id.HID);
        button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Log.i(TAG, "Bluetooth HID clicked");
				Intent intent = new Intent("com.btsd.BluetoothHID");
				startActivity(intent);
			}

		});
        
        button = (Button) findViewById(R.id.MCE);
        button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Log.i(TAG, "Media Center clicked");
				Intent intent = new Intent("com.btsd.MCEActivity");
				startActivity(intent);
			}

		});
        
        button = (Button) findViewById(R.id.RemotePrototype);
        button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Log.i(TAG, "Remote Prototype clicked");
				Intent intent = new Intent("com.btsd.RemotePrototypeActivity");
				startActivity(intent);
			}

		});
        
        button = (Button) findViewById(R.id.RootActivity);
        button.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Log.i(TAG, "Root Activity clicked");
				Intent intent = new Intent("com.btsd.RootActivity");
				startActivity(intent);
			}

		});
        
        if(callbackHandler == null){
			callbackHandler = new BTScrewDriverCallbackHandler(this);
		}
		
        
        pauseAlert = new BTScrewDriverAlert(R.string.CONNECTING_TO_BT_SERVER, false);
        //TODO: comment out for testing
        pauseAlert.showAlert(this);
        
		BTSDApplication application =  (BTSDApplication)getApplication();
		application.getStateMachine().registerActivity(this);
		
		
		application.getStateMachine().getBTConnectionState(this);
		
		mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    }
    
    @Override
    public final void sendMessage(Message message) {
    	callbackHandler.sendMessage(message);
    }
    
    @Override
    public void onBTSDMessage(String message) {
    	Log.e(TAG, "Received message from BTSD Server: " + message);
    }
    
    @Override
    public void onMessage(MessagesEnum messagesEnum, Object message) {
    	
    	if(messagesEnum  == MessagesEnum.BT_CONNECTION_STATE){
	    	
    		if(pauseAlert != null){
	    		pauseAlert.dismisAlert();
	    		pauseAlert = null;
	    	}
	    	
	    	States state = (States)message;
	    	if(state == States.BLUETOOTH_DISABLED){
	    		BTScrewDriverAlert alert = new BTScrewDriverAlert(
					R.string.BT_NOT_ENABLED, true);
	    		alert.showAlert(this);
	    	}else if(state == States.DISCONNECTED || 
				state == States.CONNECTION_FAILED){
	    		
	    		BTScrewDriverAlert alert = new BTScrewDriverAlert(
					R.string.BT_SERVER_CONNECT_FAILED, true);
	    		//TODO: comment out for testing
	    		alert.showAlert(this);
	    	}else{
	    		//ok we are connected, lets make sure we got the right version
	    		BTSDApplication application =  (BTSDApplication)getApplication();
	    		application.getStateMachine().messageToServer(ServerMessages.getVersionRequest());
	    	}
    	}else if(messagesEnum == MessagesEnum.MESSAGE_FROM_SERVER){
    		
    		JSONObject response = (JSONObject)message;
    		try{
	    		if(response.getString(TYPE).equalsIgnoreCase("VersionRequest")){
	    			if(!response.getString(VALUE).equalsIgnoreCase("1.0")){
	    				BTScrewDriverAlert alert = new BTScrewDriverAlert(
	    						R.string.SERVER_VERSION_MISMATCH, true);
	    				alert.showAlert(this);
	    			}
	    		}else{
	    			Log.w(TAG, "Unexpected message from server: " + message.toString());
	    		}
    		}catch(JSONException ex){}
    	}
    	
    }
 
    private synchronized void enableKeyguard() {
        if (mKeyguardLock != null) {
            mKeyguardLock.reenableKeyguard();
            mKeyguardLock = null;
        }
    }
    private synchronized void disableKeyguard() {
        if (mKeyguardLock == null) {
            mKeyguardLock = mKeyguardManager.newKeyguardLock(TAG);
            mKeyguardLock.disableKeyguard();
        }
    }
 
    @Override
    public void showCancelableDialog(int title, String message) {
    	// TODO Auto-generated method stub
    	
    }    
    
    @Override
    public Activity getActivity() {
    	return this;
    }   
    
    
}