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

	@Override
	public File showSaveDialog(IRootComponent rc) {
		return this.wrapee.showSaveDialog(rc);
	}

	@Override
	public void setInitialDirectory(File f) {
		this.wrapee.setInitialDirectory(f);
	}

	@Override
	public void setInitialFileName(String name) {
		this.wrapee.setInitialFileName(name);
	}
}
