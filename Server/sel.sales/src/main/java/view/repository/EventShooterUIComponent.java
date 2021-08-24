package view.repository;

public abstract class EventShooterUIComponent extends UIComponent implements IEventShooterUIComponent {

	EventShooterUIComponent(IEventShooterUIComponent component) {
		super(component);
	}

	@Override
	public IEventShooterUIComponent getComponent() {
		return (IEventShooterUIComponent) super.getComponent();
	}
	
	@Override
	public void addClickListener(ClickEventListener l) {
		this.addEditPart(l);
		this.getComponent().addClickListener(l);
	}
	
	@Override
	public void removeClickListener(ClickEventListener l) {
		this.removeEditPart(l);
		this.getComponent().removeClickListener(l);
	}

}
