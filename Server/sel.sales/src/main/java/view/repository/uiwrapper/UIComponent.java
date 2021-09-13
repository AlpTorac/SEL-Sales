package view.repository.uiwrapper;

import java.util.ArrayList;
import java.util.List;

import view.repository.IUIComponent;

public abstract class UIComponent implements IUIComponent {
	private List<EditPart> editors = new ArrayList<EditPart>();
	private IUIComponent component;
	
	UIComponent(IUIComponent component) {
		this.component = component;
	}

	@Override
	public void show() {
		this.component.show();
	}
	
	@Override
	public void hide() {
		this.component.hide();
	}
	
	public EditPart[] getEditors() {
		return this.editors.toArray(EditPart[]::new);
	}
	
	protected boolean addEditPart(EditPart editor) {
		return this.editors.add(editor);
	}
	
	protected boolean removeEditPart(EditPart editor) {
		return this.editors.remove(editor);
	}
	
	public void removeAllEditParts() {
		this.editors.clear();
	}
	
	public IUIComponent getComponent() {
		return this.component;
	}
}
