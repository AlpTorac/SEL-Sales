package view.repository;

import java.io.File;

public interface IFileChooser {
	/*
	 * An option looks like: description (namePattern.extension)
	 */
	void addExtensionFilter(String description, String namePattern, String extension);
	default void addExtensionFilter(String description, String extension) {
		this.addExtensionFilter(description, "*", extension);
	}
	File showOpenDialog(IRootComponent rc);
}
