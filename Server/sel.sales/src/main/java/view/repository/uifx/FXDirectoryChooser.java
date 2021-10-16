package view.repository.uifx;

import java.io.File;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import view.repository.IDirectoryChooser;
import view.repository.IRootComponent;

public class FXDirectoryChooser implements IDirectoryChooser {

	private DirectoryChooser chooser;
	
	FXDirectoryChooser() {
		this.chooser = new DirectoryChooser();
	}
	
	@Override
	public File showDialog(IRootComponent rc) {
		return this.chooser.showDialog((Window) rc);
	}

}
