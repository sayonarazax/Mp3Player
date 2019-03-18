package interfaces.impl;



import interfaces.PlayControlListener;
import interfaces.Player;
import javazoom.jlgui.basicplayer.*;
import utils.FileUtils;

import java.io.File;
import java.util.Map;

public class Mp3Player implements Player {
    public static final String MP3_FILE_EXTENSION = "mp3";
    public static final String MP3_FILE_DESCRIPTION = "Файлы mp3";
    public static int MAX_VOLUME = 100;

    private long duration;
    private int bytesLen;

    private long secondsAmount;

    private PlayControlListener playControlListener;

    private BasicPlayer player = new BasicPlayer();
    private String currentFileName;
    private double currentVolume;

    public Mp3Player(PlayControlListener playControlListener){
        this.playControlListener = playControlListener;
        player.addBasicPlayerListener(new BasicPlayerListenerAdapter() {
            @Override
            public void opened(Object o, Map map) {
                duration = (long) Math.round((((Long) map.get("duration")).longValue()) / 1000000);
                bytesLen = (int) Math.round(((Integer) map.get("mp3.length.bytes")).intValue());
                String songName = map.get("title") != null ? map.get("title").toString() : FileUtils.getFileNameWithoutExtension(new File(o.toString()).getName());

                if (songName.length() > 30) {
                    songName = songName.substring(0, 30) + "...";
                }

                Mp3Player.this.playControlListener.playStarted(songName);
            }

            @Override
            public void progress(int bytesread, long l, byte[] bytes, Map map) {
                float progress = -1.0f;

                if ((bytesread > 0) && ((duration > 0))) {
                    progress = bytesread * 1.0f / bytesLen * 1.0f;
                    //        System.out.println(progress);
                }

                secondsAmount = (long) (duration * progress);

                if (duration != 0) {
                    int length = (Math.round(secondsAmount * 1000 / duration));
                    Mp3Player.this.playControlListener.processScroll(length);
                }
            }

            @Override
            public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
                int state = basicPlayerEvent.getCode();

                if (state == BasicPlayerEvent.EOM) {
                    Mp3Player.this.playControlListener.playFinished();
                }
            }
        });
    }

    public void play(String fileName){
            try {
                if(currentFileName != null && currentFileName.equals(fileName) && player.getStatus() == BasicPlayer.PAUSED) {
                    player.resume();
                    return;
                }
                currentFileName = fileName;
                player.open(new File(fileName));
                player.play();
                player.setGain(currentVolume);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
    }

    public void stop(){
        try {
            player.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void pause(){
        try {
            player.pause();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(double controlValue){
        try {
            currentVolume = calcVolume(controlValue);
            player.setGain(currentVolume);
        }catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }

    public double calcVolume(double currentVolume){
        this.currentVolume = (double) currentVolume / MAX_VOLUME;
        return this.currentVolume;
    }

    public void jump(double controlPosition){
        try {
            long skipBytes = Math.round(((Integer)bytesLen)*controlPosition);
            player.seek(skipBytes);
            player.setGain(currentVolume);
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }

    }
}
