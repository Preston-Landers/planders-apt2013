import java.util.ArrayList;

public class MockMp3Player implements Mp3Player {

    private int currentSongPos;
    private ArrayList<String> songList;
    private double currentPosition;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    @Override
    public void play() {
        if (songList == null) {
            return;
        }
        isPaused = false;
        isPlaying = true;
        currentPosition += 0.1;
    }

    @Override
    public void pause() {
        if (songList == null) {
            return;
        }
        isPaused = true;
        // isPlaying = true;
    }

    @Override
    public void stop() {
        if (songList == null) {
            return;
        }
        isPlaying = false;
        isPaused = false;
    }

    @Override
    public double currentPosition() {
        return currentPosition;
    }

    @Override
    public String currentSong() {
        if (songList == null) {
            return null;
        }
        return songList.get(currentSongPos);
    }

    @Override
    public void next() {
        if (songList == null) {
            return;
        }
        if (currentSongPos < songList.size() - 1) {
            currentSongPos++;
        }
    }

    @Override
    public void prev() {
        if (songList == null) {
            return;
        }
        if (currentSongPos > 0) {
            currentSongPos--;
        }
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void loadSongs(ArrayList names) {
        songList = names;
        currentSongPos = 0;
    }
}
