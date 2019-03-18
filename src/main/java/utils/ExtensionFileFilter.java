package utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ExtensionFileFilter extends FileFilter {
    private String fileExtension;
    private String fileDescription;
    public ExtensionFileFilter(String fileExtension, String fileDescription){
        this.fileDescription = fileDescription;
        this.fileExtension = fileExtension;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getAbsolutePath().endsWith(fileExtension);
    }

    @Override
    public String getDescription() {
        return fileDescription + " (*." + fileExtension + ")";
    }
}
