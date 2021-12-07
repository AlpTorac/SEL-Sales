package external.connection;

import model.settings.ISettings;
import model.settings.SettingsField;

public interface IHasConnectionSettings {
	default void receiveSettings(ISettings settings) {
		if (settings != null) {
			String ppMinDelay = settings.getSetting(SettingsField.PING_PONG_MINIMAL_DELAY);
			String ppTimeout = settings.getSetting(SettingsField.PING_PONG_TIMEOUT);
			String ppResendLimit = settings.getSetting(SettingsField.PING_PONG_RESEND_LIMIT);
			String sbTimeout = settings.getSetting(SettingsField.SEND_TIMEOUT);
			
			if (ppTimeout != null) {
				this.setPingPongTimeoutInMillis(Long.valueOf(ppTimeout));
			}
			if (ppMinDelay != null) {
				this.setMinimalPingPongDelay(Long.valueOf(ppMinDelay));
			}
			if (ppResendLimit != null) {
				this.setPingPongResendLimit(Integer.valueOf(ppResendLimit));
			}
			if (sbTimeout != null) {
				this.setSendTimeoutInMillis(Long.valueOf(sbTimeout));
			}
			this.notifyInnerConstructs(settings);
		}
		
//		if (settings.settingExists(SettingsField.PING_PONG_MINIMAL_DELAY)) {
//			this.setMinimalPingPongDelay(Long.valueOf(settings.getSetting(SettingsField.PING_PONG_MINIMAL_DELAY)));
//		}
//		if (settings.settingExists(SettingsField.PING_PONG_RESEND_LIMIT)) {
//			this.setPingPongResendLimit(Integer.valueOf(settings.getSetting(SettingsField.PING_PONG_RESEND_LIMIT)));
//		}
//		if (settings.settingExists(SettingsField.PING_PONG_TIMEOUT)) {
//			this.setPingPongTimeoutInMillis(Long.valueOf(settings.getSetting(SettingsField.PING_PONG_TIMEOUT)));
//		}
//		if (settings.settingExists(SettingsField.SEND_TIMEOUT)) {
//			this.setSendTimeoutInMillis(Long.valueOf(settings.getSetting(SettingsField.SEND_TIMEOUT)));
//		}
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
	
	default long getEstimatedPPCloseTime() {
		return this.getPingPongTimeoutInMillis() * (this.getPingPongResendLimit() + 1);
	}
}
