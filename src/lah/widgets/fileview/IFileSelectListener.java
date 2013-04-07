package lah.widgets.fileview;

import java.io.File;

/**
 * Interface for caller to register a call back function when the user select a file or navigate into a sub-directory,
 * probably for display purpose
 * 
 * @author L.A.H.
 * 
 */
public interface IFileSelectListener {

	void onFileSelected(File result);

}