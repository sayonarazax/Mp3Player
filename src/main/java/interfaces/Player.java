package interfaces;

public interface Player {
    void play(String fileName);

    void pause();

    void stop();

    void setVolume(double setValue);

    void jump(double controlPosition);
}
