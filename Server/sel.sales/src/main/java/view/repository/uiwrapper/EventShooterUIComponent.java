package view.repository.uiwrapper;

import view.repository.IEventShooterUIComponent;

public abstract class EventShooterUIComponent extends UIComponent implements IEventShooterUIComponent {

	EventShooterUIComponent(IEventShooterUIComponent component) {
		super(component);
	}

	@Override
	public IEventShooterUIComponent getComponent() {
		return (IEventShooterUIComponent) super.getComponent();
	}
	
	public void addClickListener(ClickEventListener l) {
		this.addEditPart(l);
		this.getComponent().addClickListener(l);
	}
	
	public void removeClickListener(ClickEventListener l) {
		this.removeEditPart(l);
		this.getComponent().removeClickListener(l);
	}
}
