package view.repository.uifx;

import view.repository.IFileChooser;
import view.repository.IRootComponent;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class FXFileChooser implements IFileChooser {

	private FileChooser fc;
	
	FXFileChooser() {
		this.fc = new FileChooser();
	}

	@Override
	public void addExtensionFilter(String description, String namePattern, String extension) {
		this.fc.getExtensionFilters().add(new ExtensionFilter(description, namePattern+"."+extension));
	}
	
	@Override
	public File showOpenDialog(IRootComponent rc) {
		return this.fc.showOpenDialog((Window) rc);
	}

	@Override
	public File showSaveDialog(IRootComponent rc) {
		return this.fc.showSaveDialog((Window) rc);
	}

	@Override
	public void setInitialDirectory(File f) {
		this.fc.setInitialDirectory(f);
	}

	@Override
	public void setInitialFileName(String name) {
		this.fc.setInitialFileName(name);
	}
	
}
