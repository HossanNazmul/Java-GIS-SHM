package mainGUIPackage;

import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FileLocator {
	public static String jfilechooser() {
	    JFileChooser fileopen = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("c files", "c");
	    fileopen.addChoosableFileFilter(filter);
	    String filename="";
	    int ret = fileopen.showDialog(null, "Open file(.csv)");

	    if (ret == JFileChooser.APPROVE_OPTION) {
	      File file = fileopen.getSelectedFile();
	      filename = file.getPath();
	    }
		return filename;
	  }
	}

