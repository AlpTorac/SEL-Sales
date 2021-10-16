package view.composites;

import java.io.File;

import controller.IController;
import controller.StatusEvent;
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
	
	private FileAddressUI menuFolderAddress;
	private FileAddressUI orderFolderAddress;
	
	public SettingsArea(IController controller, UIComponentFactory fac, IRootComponent mainWindow) {
		super(fac.createVBoxLayout().getComponent());
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
	
	protected IButton initButton() {
		IButton button = fac.createButton();
		button.setCaption("Apply");
		button.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				controller.handleApplicationEvent(StatusEvent.DISH_MENU_FOLDER_CHANGED, new Object[] {menuFolderAddress.getAddress()});
				controller.handleApplicationEvent(StatusEvent.ORDER_FOLDER_CHANGED, new Object[] {orderFolderAddress.getAddress()});
			}
		});
		
		return button;
	}
	
	public IButton getApplyButton() {
		return this.applyButton;
	}
	
	private class FileAddressArea extends UIVBoxLayout {
		FileAddressArea() {
			super(fac.createVBoxLayout().getComponent());
			this.setSpacing(5);
			this.addUIComponents(new IUIComponent[] {
					menuFolderAddress = new FileAddressUI("Menu folder address"),
					orderFolderAddress = new FileAddressUI("Order folder address")
			});
		}
	}
	
	private class ConnectivityArea extends UIVBoxLayout {
		ConnectivityArea() {
			super(fac.createVBoxLayout().getComponent());
			this.setSpacing(5);
			this.addUIComponents(new IUIComponent[] {
					new ConnectivityUI("Ping-Pong timeout"),
					new ConnectivityUI("Ping-Pong minimal delay"),
					new ConnectivityUI("Ping-Pong resend limit"),
					new ConnectivityUI("Send timeout")
			});
		}
	}
	
	private class FileAddressUI extends UIHBoxLayout {
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
		
		private FileAddressUI(String labelCaption) {
			super(fac.createHBoxLayout().getComponent());
			this.labelCaption = labelCaption + descriptionEnd;
			this.init();
		}
		
		private void init() {
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
		
		private String getAddress() {
			return this.addressBox.getText();
		}
	}

	private class ConnectivityUI extends UIHBoxLayout {
		private ILabel descriptionLabel;
		private ITextBox addressBox;
		
		private String labelCaption;
		
		private ConnectivityUI(String labelCaption) {
			super(fac.createHBoxLayout().getComponent());
			this.labelCaption = labelCaption + descriptionEnd;
			this.init();
		}
		
		private void init() {
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
	}
}
