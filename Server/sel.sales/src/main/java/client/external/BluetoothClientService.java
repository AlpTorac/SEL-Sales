package client.external;

import java.util.concurrent.ExecutorService;

import javax.bluetooth.UUID;

import controller.IController;
import external.ServiceInfo;
import external.bluetooth.BluetoothDeviceManager;
import external.bluetooth.BluetoothService;
import external.bluetooth.BluetoothServiceConnectionManager;

public class BluetoothClientService extends BluetoothService {
	
	public BluetoothClientService(BluetoothDeviceManager deviceManager, IController controller,
		ExecutorService es, long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(new UUID(Long.decode(ServiceInfo.getInstance().getServiceID())),
				ServiceInfo.getInstance().getServiceName(), deviceManager, controller, es,
				pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	@Override
	public void publish() {
		this.scm = new BluetoothServiceConnectionManager(this, this.getDeviceManager(), this.getController(), es,
				this.getPingPongTimeout(), this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
		this.scm.makeNewConnectionThread();
	}
}
