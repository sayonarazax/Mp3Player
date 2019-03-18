package listeners;

import mp3.Song;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ButtonSearchListener implements ActionListener {
    private JTextField searchName;
    private JButton searchButton;
    private ArrayList<Song> songlist;
    public  ButtonSearchListener(JButton button, JTextField field, ArrayList songlist){
        searchButton = button;
        searchName = field;
        this.songlist = songlist;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        for(Song song: songlist){
            if (song.getName().equals(searchName.getText()))
                System.out.println(song.getName());
        }
    }
}
