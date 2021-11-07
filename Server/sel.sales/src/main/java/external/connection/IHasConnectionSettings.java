package external.connection;

import model.settings.ISettings;
import model.settings.SettingsField;

public interface IHasConnectionSettings {
	default void receiveSettings(ISettings settings) {
		if (settings.settingExists(SettingsField.PING_PONG_MINIMAL_DELAY)) {
			this.setMinimalPingPongDelay(Long.valueOf(settings.getSetting(SettingsField.PING_PONG_MINIMAL_DELAY)));
		}
		if (settings.settingExists(SettingsField.PING_PONG_RESEND_LIMIT)) {
			this.setPingPongResendLimit(Integer.valueOf(settings.getSetting(SettingsField.PING_PONG_RESEND_LIMIT)));
		}
		if (settings.settingExists(SettingsField.PING_PONG_TIMEOUT)) {
			this.setPingPongTimeoutInMillis(Long.valueOf(settings.getSetting(SettingsField.PING_PONG_TIMEOUT)));
		}
		if (settings.settingExists(SettingsField.SEND_TIMEOUT)) {
			this.setSendTimeoutInMillis(Long.valueOf(settings.getSetting(SettingsField.SEND_TIMEOUT)));
		}
		this.notifyInnerConstructs(settings);
	}
	
	/**
	 * Use this to notify further constructs about the settings changes, override if needed
	 */
	void notifyInnerConstructs(ISettings settings);
	
	void setMinimalPingPongDelay(long minimalPingPongDelay);
	void setSendTimeoutInMillis(long sendTimeoutInMillis);
	void setPingPongTimeoutInMillis(long pingPongTimeoutInMillis);
	void setPingPongResendLimit(int pingPongResendLimit);
	
	long getMinimalPingPongDelay();
	long getSendTimeoutInMillis();
	long getPingPongTimeoutInMillis();
	int getPingPongResendLimit();
}
