package controller;

public interface IApplicationEventShooter {
	default public void fireApplicationEvent(IController controller) {
		controller.handleApplicationEvent(this.getApplicationEvent(), this.getArgs());
	}
	public Object[] getArgs();
	public IApplicationEvent getApplicationEvent();
}
