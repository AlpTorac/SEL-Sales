package view.composites;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.IController;
import controller.GeneralEvent;
import model.settings.ISettings;
import model.settings.SettingsField;
import view.repository.IButton;
import view.repository.IHBoxLayout;
import view.repository.ILabel;
import view.repository.ILayout;
import view.repository.IRootComponent;
import view.repository.ISingleRowTextBox;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UIVBoxLayout;

public class SettingsArea extends UIVBoxLayout {
	private final static String descriptionEnd = ":";
	
	private IController controller;
	private UIComponentFactory fac;
	private IRootComponent mainWindow;
	private IButton applyButton;
	
	private Collection<ISettingsInputArea> settingAreas;
	
	// the following attributes are there for future access, if necessary
	
	private FileAddressArea faa;
	private ConnectivityArea ca;
	
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
		this.initComponents();
		this.addApplyButtonToLayout();
	}
	
	protected void initComponents() {
		this.addUIComponents(new IUIComponent[] {
				this.faa = this.initFAA(),
				this.ca = this.initCA(),
		});
	}
	
	protected void addApplyButtonToLayout() {
		this.addUIComponent(this.applyButton = this.initApplyButton());
	}
	
	protected FileAddressArea initFAA() {
		return new FileAddressArea();
	}
	
	protected ConnectivityArea initCA() {
		return new ConnectivityArea();
	}
	
	protected String[][] getAllSettings() {
		return this.settingAreas.stream()
				.map(sia -> sia.getSettingEntry())
				.toArray(String[][]::new);
	}
	
	protected IButton initApplyButton() {
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
	
	protected abstract class SettingsGroupArea extends UILayout {
		protected SettingsGroupArea(UILayout layout) {
			super(layout.getComponent());
			this.initialSetup();
			this.initComponents();
		}
		
		protected abstract void initialSetup();
		protected abstract void initComponents();
	}
	
	protected class FileAddressArea extends SettingsGroupArea {
		protected FileAddressArea() {
			super(fac.createVBoxLayout());
		}
		
		
		@Override
		protected void initComponents() {
			this.getComponent().addUIComponents(new IUIComponent[] {
					menuFolderAddress = new FileAddressUI("Menu folder address", SettingsField.DISH_MENU_FOLDER),
					orderFolderAddress = new FileAddressUI("Order folder address", SettingsField.ORDER_FOLDER)
			});
		}
		
		@Override
		public IVBoxLayout getComponent() {
			return (IVBoxLayout) super.getComponent();
		}

		@Override
		protected void initialSetup() {
			this.getComponent().setSpacing(5);
		}
	}
	
	protected class ConnectivityArea extends SettingsGroupArea {
		protected ConnectivityArea() {
			super(fac.createVBoxLayout());
		}
		
		@Override
		protected void initialSetup() {
			this.getComponent().setSpacing(5);
		}
		
		@Override
		public IVBoxLayout getComponent() {
			return (IVBoxLayout) super.getComponent();
		}
		
		@Override
		protected void initComponents() {
			this.getComponent().addUIComponents(new IUIComponent[] {
					ppTimeout = new ConnectivityUI("Ping-Pong timeout", SettingsField.PING_PONG_TIMEOUT),
					ppMinimalDelay = new ConnectivityUI("Ping-Pong minimal delay", SettingsField.PING_PONG_MINIMAL_DELAY),
					ppResendLimit = new ConnectivityUI("Ping-Pong resend limit", SettingsField.PING_PONG_RESEND_LIMIT),
					sendTimeout = new ConnectivityUI("Send timeout", SettingsField.SEND_TIMEOUT)
			});
		}
	}
	
	protected class ConnectivityUI extends BasicSettingsInputArea {
		protected ConnectivityUI(String labelCaption, SettingsField sf) {
			super(labelCaption, sf);
		}
	}
	
	protected class FileAddressUI extends BasicSettingsInputArea {
		private IButton fileChooserButton;
		
		private ClickEventListener cel = new ClickEventListener() {
			@Override
			public void clickAction() {
				File f = fac.createDirectoryChooser().showDialog(mainWindow);
				getAddressBox().setCaption(f.getPath());
			}
		};
		
		protected FileAddressUI(String labelCaption, SettingsField sf) {
			super(labelCaption, sf);
//			this.labelCaption = labelCaption + descriptionEnd;
//			this.sf = sf;
//			this.init();
		}
		
		@Override
		public void initComponents() {
			super.initComponents();
			
			this.fileChooserButton = this.initFileChooseButton();
			this.addUIComponent(this.fileChooserButton);
		}
		
		private IButton initFileChooseButton() {
			IButton button = fac.createButton();
			button.addClickListener(cel);
			return button;
		}
	}

	protected abstract class BasicSettingsInputArea extends UIHBoxLayout implements ISettingsInputArea {
		private ILabel descriptionLabel;
		private ISingleRowTextBox addressBox;
		
		private String placeholderCaption;
		private SettingsField sf;
		
		protected BasicSettingsInputArea(String placeholderCaption, SettingsField sf) {
			super(fac.createHBoxLayout().getComponent());
			this.placeholderCaption = placeholderCaption;
			this.sf = sf;
			this.init();
		}
		
		public String getPlaceholderCaption() {
			return this.placeholderCaption;
		}
		
		public String getLabelCaption() {
			return this.placeholderCaption + descriptionEnd;
		}
		
		public ILabel getDescriptionLabel() {
			return this.descriptionLabel;
		}
		
		public ISingleRowTextBox getAddressBox() {
			return this.addressBox;
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
			label.setCaption(this.getLabelCaption());
			return label;
		}
		
		private ISingleRowTextBox initAddressBox() {
			ISingleRowTextBox tb = fac.createSingleRowTextBox();
			tb.setPlaceholderText(this.getPlaceholderCaption());
			return tb;
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

	public ISingleRowTextBox getMenuFolderAddress() {
		return menuFolderAddress.getAddressBox();
	}

	public ISingleRowTextBox getOrderFolderAddress() {
		return orderFolderAddress.getAddressBox();
	}

	public ISingleRowTextBox getPpTimeout() {
		return ppTimeout.getAddressBox();
	}

	public ISingleRowTextBox getPpMinimalDelay() {
		return ppMinimalDelay.getAddressBox();
	}

	public ISingleRowTextBox getPpResendLimit() {
		return ppResendLimit.getAddressBox();
	}

	public ISingleRowTextBox getSendTimeout() {
		return sendTimeout.getAddressBox();
	}
}
