package lah.widgets.fileview;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator to compare files, for display purpose. Rule: Directory always
 * smaller than file. Otherwise, they should be compared by names. This
 * comparator is used to compare files.
 * 
 * @author L.A.H.
 * 
 */
public class FileComparator implements Comparator<File> {

	/**
	 * Compare two files, two directory or a file and a directory.
	 */
	public int compare(File arg0, File arg1) {
		assert (arg0 != null);
		assert (arg1 != null);
		if (arg0.isDirectory())
			return arg1.isDirectory() ? arg0.getName()
					.compareTo(arg1.getName()) : -1;
		else
			return arg1.isDirectory() ? 1 : arg0.getName().compareTo(
					arg1.getName());
	}

}
