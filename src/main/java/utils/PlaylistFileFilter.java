package utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class PlaylistFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        return false;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
