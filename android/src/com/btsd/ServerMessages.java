package com.btsd;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ServerMessages {

	private static JSONObject pairMode;
	private static JSONObject connectToHostCancel;
	private static JSONObject hidStatus;
	private static JSONObject hidHosts;
	private static JSONObject pairModeCancel;
	private static JSONObject pincodeCancel;
	private static JSONObject versionRequest;

	public static JSONObject getConnectToHostCancel() {
		if(connectToHostCancel == null){
			connectToHostCancel = createHIDJSONObject("CONNECT_TO_HOST_CANCEL");
		}
		return connectToHostCancel;
	}

	public static JSONObject getPairMode() {
		if(pairMode == null){
			pairMode = createHIDJSONObject("PAIR_MODE");
		}
		return pairMode;
	}
	
	public static JSONObject getConnectToHost(String address){
		JSONObject toReturn = createHIDJSONObject("CONNECT_TO_HOST");
		try{
			toReturn.put(Main.HOST_ADDRESS, address);
		}catch(JSONException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return toReturn;
	}
	
	public static JSONObject getDisconnectFromHost(){
		return createHIDJSONObject("DISCONNECT_FROM_HOST");
	}
	
	public static JSONObject getKeyCode(Integer keyCode){
		
		ArrayList<Integer> keyCodes = new ArrayList<Integer>();
		keyCodes.add(keyCode);
		return getKeycodes(keyCodes, null, null);
	}
	
	public static JSONObject getKeyCode(Integer keyCode,
		ArrayList<Integer> keyModifiersDown,ArrayList<Integer> keyModifiersUp){
		
		ArrayList<Integer> keyCodes = new ArrayList<Integer>();
		keyCodes.add(keyCode);
		return getKeycodes(keyCodes, keyModifiersDown, keyModifiersUp);
	}
	
	
	public static JSONObject getKeycodes(ArrayList<Integer> keycodes){
		
		return getKeycodes(keycodes, null, null);
		
		
	}
	
	public static JSONObject getKeycodes(ArrayList<Integer> keycodes, 
		ArrayList<Integer> keyModifiersDown, ArrayList<Integer> keyModifiersUp){
		
		JSONObject toReturn = createHIDJSONObject("KEYCODE");
		try{
			JSONArray keyCodesArray = new JSONArray();
			for(Integer keyCode: keycodes){
				keyCodesArray.put(keyCode);
			}
			toReturn.put(Main.KEY_CODES, keyCodesArray);
			JSONArray keyModifiersArray = new JSONArray();
			if(keyModifiersDown != null){
				for(Integer modifier: keyModifiersDown){
					keyModifiersArray.put(modifier);
				}
			}
			toReturn.put(Main.KEY_MODIFIERS_DOWN, keyModifiersArray);
			
			keyModifiersArray = new JSONArray();
			if(keyModifiersUp != null){
				for(Integer modifier: keyModifiersUp){
					keyModifiersArray.put(modifier);
				}
			}
			toReturn.put(Main.KEY_MODIFIERS_UP, keyModifiersArray);
			
		}catch(JSONException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return toReturn;
	}
	
	public static JSONObject getKeycodes(int[] keycodes){
		
		ArrayList<Integer> keyCodes = new ArrayList<Integer>();
		for(int keycode: keycodes){
			keyCodes.add(keycode);
		}
		return getKeycodes(keyCodes, null, null);
	}
	
	public static JSONObject getKeycodes(int[] keycodes,
		ArrayList<Integer> keyModifiersDown, ArrayList<Integer> keyModifiersUp){
		
		ArrayList<Integer> keyCodes = new ArrayList<Integer>();
		for(int keycode: keycodes){
			keyCodes.add(keycode);
		}
		return getKeycodes(keyCodes, keyModifiersDown, keyModifiersUp);
		
	}
	
	public static JSONObject getConfigurationCommand(){
		return createJSONObject("ConfigCommand");
	}
	
	public static JSONObject getLIRCCommand(String remote, String command, 
		Integer count){
		
		JSONObject toReturn = createJSONObject("LIRCCommand");
		try{
			toReturn.put(Main.REMOTE, remote);
			toReturn.put(Main.KEY_CODES, command);
			toReturn.put(Main.SEND_COUNT, count.toString());
		}catch(JSONException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return toReturn;
		
	}
	
	public static JSONObject getHidStatus() {
		if(hidStatus == null){
			hidStatus = createHIDJSONObject("HID_STATUS");
		}
		return hidStatus;
	}

	public static JSONObject getHidHosts() {
		if(hidHosts == null){
			hidHosts = createHIDJSONObject("HID_HOSTS");
		}
		return hidHosts;
	}

	public static JSONObject getPairModeCancel() {
		if(pairModeCancel == null){
			pairModeCancel = createHIDJSONObject("PAIR_MODE_CANCEL");
		}
		return pairModeCancel;
	}

	public static JSONObject getPincodeResponse(String pincodeResponse) {
		JSONObject toReturn = createHIDJSONObject("PINCODE_RESPONSE");
		try{
			toReturn.put(Main.PINCODE, pincodeResponse);
		}catch(JSONException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return toReturn;
	}
	
	public static JSONObject getPincodeCancel() {
		if(pincodeCancel == null){
			pincodeCancel = createHIDJSONObject("PINCODE_CANCEL");
		}
		return pincodeCancel;
	}

	public static JSONObject getRemovePairedHost(String hostAddress){
		JSONObject toReturn = createHIDJSONObject("UNPAIR_DEVICE");
		try{
			toReturn.put(Main.HOST_ADDRESS, hostAddress);
		}catch(JSONException ex){
			
		}
		return toReturn;
	}
	
	public static JSONObject getVersionRequest() {
		if(versionRequest == null){
			versionRequest = createJSONObject("VersionRequest");
		}
		return versionRequest;
	}

	private static JSONObject createHIDJSONObject(String value){
		JSONObject toReturn = new JSONObject();
		try{
			toReturn.put(Main.TYPE, "HIDCommand");
			toReturn.put(Main.VALUE, value);
		}catch(JSONException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return toReturn;
	}
	
	private static JSONObject createJSONObject(String type){
		JSONObject toReturn = new JSONObject();
		try{
			toReturn.put(Main.TYPE, type);
		}catch(JSONException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return toReturn;
	}
	
}
