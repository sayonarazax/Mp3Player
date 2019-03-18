package interfaces.impl;

import interfaces.PlayList;
import interfaces.Player;
import mp3.Song;
import utils.FileUtils;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class Mp3PlayList implements PlayList {
    public static final String PLAYLIST_FILE_EXTENSION = "pls";
    public static final String PLAYLIST_FILE_DESCRIPTION = "файлы плэйлист";

    private static final String EMPTY_STRING = "";

    private DefaultListModel mp3listModel = new DefaultListModel();

    private Player player;
    private JList songList;

    public Mp3PlayList(Player player, JList list){
        this.player = player;
        songList = list;
        initPlaylist();
    }

    @Override
    public void playFile() {
        int[] indexPlayList = songList.getSelectedIndices();// получаем выбранные индексы(порядковый номер) песен
        if (indexPlayList.length > 0) {// если выбрали хотя бы одну песню
            Object selectedSong = mp3listModel.getElementAt(indexPlayList[0]);// находим первую выбранную песню (т.к. несколько песен нельзя проиграть одновременно
            if (!(selectedSong instanceof Song))
                return;
            Song song = (Song) selectedSong;
            player.play(song.getPath());
        }
    }

    @Override
    public void selectPreviousSonf() {
        int nextIndex = songList.getSelectedIndex() - 1;
        if (nextIndex >= 0) {// если не вышли за пределы плейлиста
            songList.setSelectedIndex(nextIndex);
            playFile();
        }
    }

    @Override
    public void selectNextSong() {
        int nextIndex = songList.getSelectedIndex() + 1;
        if (nextIndex <= songList.getModel().getSize() - 1) {// если не вышли за пределы плейлиста
            songList.setSelectedIndex(nextIndex);
            playFile();
        }
    }

    @Override
    public boolean search(String fileName) {
        if(fileName == null || fileName.trim().equals(EMPTY_STRING))
            return false;

        ArrayList<Integer> findedIndexes = new ArrayList<>();
        for (int i = 0; i < mp3listModel.size(); i++) {
            Song song = (Song) mp3listModel.getElementAt(i);
            if (song.getName().toLowerCase().contains(fileName.toLowerCase())) {
                findedIndexes.add(i);
            }
        }
        int[] indexes = new int[findedIndexes.size()];
        if (indexes.length == 0) {
            return false;
        }

        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = findedIndexes.get(i).intValue();
        }

        songList.setSelectedIndices(indexes);
        return true;
    }

    @Override
    public boolean openPlaylist(File file) {
        try {
            mp3listModel = (DefaultListModel) FileUtils.deserialize(file.getPath());
            songList.setModel(mp3listModel);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean savePlayList(File file) {
        try {
            String fileExtension = FileUtils.getFileExtension(file);
            String playlistName = (fileExtension != null && fileExtension.equals(PLAYLIST_FILE_EXTENSION)) ? file.getPath() : file.getPath() + '.' + PLAYLIST_FILE_EXTENSION;
            FileUtils.serialize(mp3listModel, playlistName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addSong(File[] files) {
        boolean status = false;
        for(File file : files){
            Song song = new Song(file.getName(), file.getPath());
            if(!mp3listModel.contains(song)){
                mp3listModel.addElement(song);
                status = true;
            }
        }
        return status;
    }

    @Override
    public void delete() {
        int[] indexSongList = songList.getSelectedIndices();
        if (indexSongList.length > 0) {
            ArrayList<Song> songListForRemove = new ArrayList<>();
            for (int i = 0; i < indexSongList.length; i++) {
                Song song = (Song) mp3listModel.getElementAt(indexSongList[i]);
                songListForRemove.add(song);
            }
            for (Song song : songListForRemove) {
                mp3listModel.removeElement(song);
            }
        }
    }

    @Override
    public void clear() {

    }

    public void initPlaylist(){
        songList.setModel(mp3listModel);
        songList.setToolTipText("Список песен");

        songList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // если нажали левую кнопку мыши 2 раза
                if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
                    playFile();
                }
            }
        });

        songList.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                int key = evt.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    playFile();
                }
            }
        });
    }
}
