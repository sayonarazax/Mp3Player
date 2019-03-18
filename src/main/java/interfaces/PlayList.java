package interfaces;

import java.io.File;

public interface PlayList {
    void playFile();

    void selectPreviousSonf();

    void selectNextSong();

    boolean search(String fileName);

    boolean openPlaylist(File file);

    boolean savePlayList(File file);

    boolean addSong(File[] file);

    void delete();

    void clear();
}
