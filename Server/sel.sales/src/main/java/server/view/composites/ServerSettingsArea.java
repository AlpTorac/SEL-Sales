package server.view.composites;

import controller.IController;
import model.settings.SettingsField;
import view.composites.SettingsArea;
import view.repository.IRootComponent;
import view.repository.ISingleRowTextBox;
import view.repository.uiwrapper.UIComponentFactory;

public class ServerSettingsArea extends SettingsArea {
	private final String tableNumberLabelCaption = "Table Numbers";
	
	private ISingleRowTextBox tableNumberInput;
	private TableNumberUI tnui;
	
	public ServerSettingsArea(IController controller, UIComponentFactory fac, IRootComponent mainWindow) {
		super(controller, fac, mainWindow);
	}
	
	@Override
	protected void initComponents() {
		super.initComponents();
		this.addUIComponent(this.tnui = new TableNumberUI());
	}
	
	protected class TableNumberUI extends BasicSettingsInputArea {
		protected TableNumberUI() {
			super(tableNumberLabelCaption, SettingsField.TABLE_NUMBERS);
			tableNumberInput = this.getAddressBox();
		}
	}
	
	public ISingleRowTextBox getTableNumberRanges() {
		return tableNumberInput;
	}
}
