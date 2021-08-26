package controller;

public interface IBusinessEventShooter {
	default public void fireBusinessEvent(IController controller) {
		controller.handleBusinessEvent(this.getBusinessEvent(), this.getArgs());
	}
	public Object[] getArgs();
	public BusinessEvent getBusinessEvent();
}
