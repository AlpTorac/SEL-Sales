package view.repository.uiwrapper;

import java.io.File;

import view.repository.IDirectoryChooser;
import view.repository.IFileChooser;
import view.repository.IRootComponent;

public class DirectoryChooserWrapper implements IDirectoryChooser {

	private IDirectoryChooser wrapee;
	
	public DirectoryChooserWrapper(IDirectoryChooser wrapee) {
		this.wrapee = wrapee;
	}
	
	@Override
	public File showDialog(IRootComponent rc) {
		return this.wrapee.showDialog(rc);
	}
}
