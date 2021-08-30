package view.repository.uiwrapper;

import view.repository.IEventShooterOnClickUIComponent;

public abstract class EventShooterUIComponent extends UIComponent implements IEventShooterOnClickUIComponent {

	EventShooterUIComponent(IEventShooterOnClickUIComponent component) {
		super(component);
	}

	@Override
	public IEventShooterOnClickUIComponent getComponent() {
		return (IEventShooterOnClickUIComponent) super.getComponent();
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
