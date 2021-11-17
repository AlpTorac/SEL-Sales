package view.composites;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import controller.GeneralEvent;
import model.settings.ISettings;
import model.settings.SettingsField;
import view.repository.IButton;
import view.repository.ILabel;
import view.repository.IRootComponent;
import view.repository.ITextBox;
import view.repository.IUIComponent;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;
import view.repository.uiwrapper.UIVBoxLayout;

public class SettingsArea extends UIVBoxLayout {
	private final static String descriptionEnd = ":";
	
	private IController controller;
	private UIComponentFactory fac;
	private IRootComponent mainWindow;
	private IButton applyButton;
	
	private Collection<ISettingsInputArea> settingAreas;
	
	// the following attributes are there for future access, if necessary
	
//	@SuppressWarnings("unused")
	private FileAddressUI menuFolderAddress;
//	@SuppressWarnings("unused")
	private FileAddressUI orderFolderAddress;
//	@SuppressWarnings("unused")
	private ConnectivityUI ppTimeout;
//	@SuppressWarnings("unused")
	private ConnectivityUI ppMinimalDelay;
//	@SuppressWarnings("unused")
	private ConnectivityUI ppResendLimit;
//	@SuppressWarnings("unused")
	private ConnectivityUI sendTimeout;
	
	public SettingsArea(IController controller, UIComponentFactory fac, IRootComponent mainWindow) {
		super(fac.createVBoxLayout().getComponent());
		this.settingAreas = new CopyOnWriteArrayList<ISettingsInputArea>();
		this.controller = controller;
		this.fac = fac;
		this.mainWindow = mainWindow;
		this.setSpacing(20);
		this.addUIComponents(new IUIComponent[] {
				new FileAddressArea(),
				new ConnectivityArea(),
				this.applyButton = this.initButton()
		});
	}
	
	protected String[][] getAllSettings() {
		return this.settingAreas.stream()
				.map(sia -> sia.getSettingEntry())
				.toArray(String[][]::new);
	}
	
	protected IButton initButton() {
		IButton button = fac.createButton();
		button.setCaption("Apply");
		button.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				controller.handleApplicationEvent(GeneralEvent.SETTINGS_CHANGED, new Object[] {getAllSettings()});
				controller.handleApplicationEvent(GeneralEvent.SAVE_SETTINGS, null);
			}
		});
		
		return button;
	}
	
	public IButton getApplyButton() {
		return this.applyButton;
	}
	
	protected interface ISettingsInputArea {
		SettingsField getSettingsField();
		String getSetting();
		void setSetting(String settingValue);
		void initComponents();
		default void init() {
			this.initComponents();
			this.addToSettingAreas();
		}
		void addToSettingAreas();
		default String[] getSettingEntry() {
			return new String[] {this.getSettingsField().toString(), this.getSetting()};
		}
	}
	
	protected class FileAddressArea extends UIVBoxLayout {
		FileAddressArea() {
			super(fac.createVBoxLayout().getComponent());
			this.setSpacing(5);
			this.addUIComponents(new IUIComponent[] {
					menuFolderAddress = new FileAddressUI("Menu folder address", SettingsField.DISH_MENU_FOLDER),
					orderFolderAddress = new FileAddressUI("Order folder address", SettingsField.ORDER_FOLDER)
			});
		}
	}
	
	protected class ConnectivityArea extends UIVBoxLayout {
		ConnectivityArea() {
			super(fac.createVBoxLayout().getComponent());
			this.setSpacing(5);
			this.addUIComponents(new IUIComponent[] {
					ppTimeout = new ConnectivityUI("Ping-Pong timeout", SettingsField.PING_PONG_TIMEOUT),
					ppMinimalDelay = new ConnectivityUI("Ping-Pong minimal delay", SettingsField.PING_PONG_MINIMAL_DELAY),
					ppResendLimit = new ConnectivityUI("Ping-Pong resend limit", SettingsField.PING_PONG_RESEND_LIMIT),
					sendTimeout = new ConnectivityUI("Send timeout", SettingsField.SEND_TIMEOUT)
			});
		}
	}
	
	protected class FileAddressUI extends UIHBoxLayout implements ISettingsInputArea {
		private ILabel descriptionLabel;
		private ITextBox addressBox;
		private IButton fileChooserButton;
		
		private ClickEventListener cel = new ClickEventListener() {
			@Override
			public void clickAction() {
				File f = fac.createDirectoryChooser().showDialog(mainWindow);
				addressBox.setCaption(f.getPath());
			}
		};
		
		private String labelCaption;
		private SettingsField sf;
		
		private FileAddressUI(String labelCaption, SettingsField sf) {
			super(fac.createHBoxLayout().getComponent());
			this.labelCaption = labelCaption + descriptionEnd;
			this.sf = sf;
			this.init();
		}
		
		@Override
		public void initComponents() {
			this.descriptionLabel = this.initLabel();
			this.addressBox = this.initAddressBox();
			this.fileChooserButton = this.initFileChooseButton();
			
			this.addUIComponents(new IUIComponent[] {
				this.descriptionLabel,
				this.addressBox,
				this.fileChooserButton
			});
		}
		
		private ILabel initLabel() {
			ILabel label = fac.createLabel();
			label.setCaption(labelCaption);
			return label;
		}
		
		private ITextBox initAddressBox() {
			return fac.createTextBox();
		}
		
		private IButton initFileChooseButton() {
			IButton button = fac.createButton();
			button.addClickListener(cel);
			return button;
		}
		
		@Override
		public String getSetting() {
			return this.addressBox.getText();
		}
		
		@Override
		public void setSetting(String newText) {
			this.addressBox.setCaption(newText);
		}

		@Override
		public SettingsField getSettingsField() {
			return this.sf;
		}

		@Override
		public void addToSettingAreas() {
			settingAreas.add(this);
		}
	}

	protected class ConnectivityUI extends UIHBoxLayout implements ISettingsInputArea {
		private ILabel descriptionLabel;
		private ITextBox addressBox;
		
		private String labelCaption;
		private SettingsField sf;
		
		private ConnectivityUI(String labelCaption, SettingsField sf) {
			super(fac.createHBoxLayout().getComponent());
			this.labelCaption = labelCaption + descriptionEnd;
			this.sf = sf;
			this.init();
		}
		
		@Override
		public void initComponents() {
			this.descriptionLabel = this.initLabel();
			this.addressBox = this.initAddressBox();
			
			this.addUIComponents(new IUIComponent[] {
				this.descriptionLabel,
				this.addressBox
			});
		}
		
		private ILabel initLabel() {
			ILabel label = fac.createLabel();
			label.setCaption(labelCaption);
			return label;
		}
		
		private ITextBox initAddressBox() {
			return fac.createTextBox();
		}

		@Override
		public SettingsField getSettingsField() {
			return this.sf;
		}

		@Override
		public String getSetting() {
			return this.addressBox.getText();
		}

		@Override
		public void setSetting(String settingValue) {
			this.addressBox.setCaption(settingValue);
		}
		
		@Override
		public void addToSettingAreas() {
			settingAreas.add(this);
		}
	}

	public void refreshSettings(ISettings settings) {
		this.settingAreas.stream().forEach(sia -> {
			if (settings.settingExists(sia.getSettingsField())) {
				sia.setSetting(settings.getSetting(sia.getSettingsField()));
			}
		});
	}

	public ITextBox getMenuFolderAddress() {
		return menuFolderAddress.addressBox;
	}

	public ITextBox getOrderFolderAddress() {
		return orderFolderAddress.addressBox;
	}

	public ITextBox getPpTimeout() {
		return ppTimeout.addressBox;
	}

	public ITextBox getPpMinimalDelay() {
		return ppMinimalDelay.addressBox;
	}

	public ITextBox getPpResendLimit() {
		return ppResendLimit.addressBox;
	}

	public ITextBox getSendTimeout() {
		return sendTimeout.addressBox;
	}
}
