package view.repository.uiwrapper;

import java.io.File;

import view.repository.IFileChooser;
import view.repository.IRootComponent;

public class FileChooserWrapper implements IFileChooser {
	
	private IFileChooser wrapee;
	
	public FileChooserWrapper(IFileChooser wrapee) {
		this.wrapee = wrapee;
	}
	
	@Override
	public void addExtensionFilter(String description, String namePattern, String extension) {
		this.wrapee.addExtensionFilter(description, namePattern, extension);
	}
	@Override
	public File showOpenDialog(IRootComponent rc) {
		return this.wrapee.showOpenDialog(rc);
	}
}
