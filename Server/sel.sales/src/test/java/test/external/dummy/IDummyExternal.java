package test.external.dummy;

import java.io.Closeable;

import external.connection.IConnection;
import external.device.DeviceDiscoveryStrategy;

public interface IDummyExternal extends Closeable {
	IConnection getConnection(String deviceAddress);
	void setDiscoveryStrategy(DeviceDiscoveryStrategy cds);
	void close();
}
