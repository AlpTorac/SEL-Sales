package view.repository.uiwrapper;

public abstract class EditPart {
	EditPart() {
	}
	
	public abstract void action();
	public abstract void action(Object[] parameters);
	public abstract void action(int numberOfInputEvents);
	public abstract void action(int numberOfInputEvents, Object[] parameters);
}
