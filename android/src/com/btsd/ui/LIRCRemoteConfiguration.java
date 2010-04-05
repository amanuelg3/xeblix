package com.btsd.ui;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.btsd.CallbackActivity;
import com.btsd.Main;
import com.btsd.R;
import com.btsd.ServerMessages;

public final class LIRCRemoteConfiguration extends RemoteConfiguration {
	
	private int repeatCount;
	
	public LIRCRemoteConfiguration(int repeatCount){
		this.repeatCount = repeatCount;
	}
	
	public final int getRepeatCount() {
		return repeatCount;
	}
	
	@Override
	public JSONObject getCommand(ScreensEnum screen, UserInputTargetEnum target,
			Map<String, Object> remoteCache, CallbackActivity activity) {
		
		ButtonConfiguration remoteName = getButtonConfiguration(ScreensEnum.ROOT, 
				UserInputTargetEnum.REMOTE_NAME);
		
		ButtonConfiguration buttonConfiguration = getButtonConfiguration(screen, target);
		
		return ServerMessages.getLIRCCommand(remoteName.getCommand().toString(), 
				buttonConfiguration.getCommand().toString(), getRepeatCount());
	}

	@Override
	public JSONObject getCommand(ButtonConfiguration buttonConfiguration,
		Map<String, Object> remoteCache, CallbackActivity activity) {
		
		ButtonConfiguration remoteName = getButtonConfiguration(ScreensEnum.ROOT, 
				UserInputTargetEnum.REMOTE_NAME);
		
		return ServerMessages.getLIRCCommand(remoteName.getCommand().toString(), 
				buttonConfiguration.getCommand().toString(), getRepeatCount());
	}
	
	@Override
	public JSONObject serverInteraction(JSONObject messageFromServer,
			Map<String, Object> remoteCache, CallbackActivity activity) {
		
		String type = null;
		try{
			type = (String)messageFromServer.get(Main.TYPE);
		}catch(JSONException ex){
			throw new RuntimeException(ex.getMessage(), ex);
		}
		if(Main.TYPE_UNPAIR_HID_HOST.equalsIgnoreCase(type)){
			String address = null;
			try{
				address = messageFromServer.getString(Main.HOST_ADDRESS);
			}catch(JSONException ex){
				throw new RuntimeException(ex.getMessage(), ex);
			}
			activity.showCancelableDialog(R.string.INFO, R.string.REMOVING_HID_HOST);
			return ServerMessages.getRemovePairedHost(address);
		}
		
		return null;
	}
	
	@Override
	public JSONObject alertClicked(int button, Map<String, Object> remoteCache,
			CallbackActivity activity) {
		
		return null;
	}
	
	@Override
	public JSONObject remoteConfigurationRefreshed(
			List<ButtonConfiguration> remoteConfigNames,
			Map<String, Object> remoteCache, CallbackActivity activity) {
		
		activity.hideDialog();
		return null;
	}
}