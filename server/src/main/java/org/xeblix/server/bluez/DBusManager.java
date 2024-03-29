package org.xeblix.server.bluez;

import java.util.List;

import org.xeblix.server.util.ActiveThread;

public interface DBusManager {

	public void setDeviceDiscoverable();
	
	public void setDeviceNotDiscoverable();
	
	public void registerAgent(ActiveThread mainActiveObject) ;
	
	public BluezAuthenticationAgent getAgent();
	
	public void registerSDPRecord();
	
	/**
	 * Returns a list of all bluetooth devices the host knows about. 
	 * This will return Xeblix clients and HIDHosts 
	 * @return
	 */
	public List<DeviceInfo> listDevices();
	
	/**
	 * Returns the DeviceInfo associated with the given path or null
	 * if no device could be found.
	 * @param path
	 * @return
	 */
	public DeviceInfo getDeviceInfo(String path);
	
	/**
	 * Un pairs the specified address and returns true if successful or false otherwise.
	 * @param address
	 */
	public boolean removePairedDevice(String address);
}
