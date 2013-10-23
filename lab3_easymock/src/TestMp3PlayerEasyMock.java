
import junit.framework.TestCase;
import org.junit.Before;

import static org.easymock.EasyMock.*;
import java.util.ArrayList;

public class TestMp3PlayerEasyMock extends TestCase {

  protected Mp3Player mp3;
  protected ArrayList list = new ArrayList();

    @Before
    public void setUp() {
        mp3= createMock(Mp3Player.class); // 1
        list = new ArrayList();
        list.add("Bill Chase -- Open Up Wide");
        list.add("Jethro Tull -- Locomotive Breath");
        list.add("The Boomtown Rats -- Monday");
        list.add("Carl Orff -- O Fortuna");
    }

    public void testPlay() {
        mp3.loadSongs(list);
        expect(mp3.isPlaying()).andReturn(false);
        mp3.play();
        expect(mp3.isPlaying()).andReturn(true);
        expect(mp3.currentPosition()).andReturn(0.1);
        mp3.pause();
        expect(mp3.currentPosition()).andReturn(0.2);
        mp3.stop();
        expect(mp3.currentPosition()).andReturn(0.0);

        replay(mp3);


        mp3.loadSongs(list);
        assertFalse(mp3.isPlaying());
        mp3.play();
        assertTrue(mp3.isPlaying());
        assertTrue(mp3.currentPosition() != 0.0);
        mp3.pause();
        assertTrue(mp3.currentPosition() != 0.0);
        mp3.stop();
        assertEquals(mp3.currentPosition(), 0.0, 0.1);

        verify(mp3);
    }

    public void testPlayNoList() {

        expect(mp3.isPlaying()).andReturn(false);
        mp3.play();
        expect(mp3.isPlaying()).andReturn(false);
        expect(mp3.currentPosition()).andReturn(0.0);
        mp3.pause();
        expect(mp3.currentPosition()).andReturn(0.0);
        expect(mp3.isPlaying()).andReturn(false);
        mp3.stop();
        expect(mp3.currentPosition()).andReturn(0.0);
        expect(mp3.isPlaying()).andReturn(false);

        replay(mp3);

        // Don't set the list up
        assertFalse(mp3.isPlaying());
        mp3.play();
        assertFalse(mp3.isPlaying());
        assertEquals(mp3.currentPosition(), 0.0, 0.1);
        mp3.pause();
        assertEquals(mp3.currentPosition(), 0.0, 0.1);
        assertFalse(mp3.isPlaying());
        mp3.stop();
        assertEquals(mp3.currentPosition(), 0.0, 0.1);
        assertFalse(mp3.isPlaying());

        verify();
    }

    public void testAdvance() {
        mp3.loadSongs(list);

        mp3.play();

        expect(mp3.isPlaying()).andReturn(true);

        mp3.prev();
        expect(mp3.currentSong()).andReturn((String) list.get(0));
        expect(mp3.isPlaying()).andReturn(true);

        mp3.next();
        expect(mp3.currentSong()).andReturn((String) list.get(1));
        mp3.next();
        expect(mp3.currentSong()).andReturn((String) list.get(2));
        mp3.prev();

        expect(mp3.currentSong()).andReturn((String) list.get(1));
        mp3.next();
        expect(mp3.currentSong()).andReturn((String) list.get(2));
        mp3.next();
        expect(mp3.currentSong()).andReturn((String) list.get(3));
        mp3.next();
        expect(mp3.currentSong()).andReturn((String) list.get(3));
        expect(mp3.isPlaying()).andReturn(true);


        replay(mp3);

        //
        mp3.loadSongs(list);

        mp3.play();

        assertTrue(mp3.isPlaying());

        mp3.prev();
        assertEquals(mp3.currentSong(), list.get(0));
        assertTrue(mp3.isPlaying());

        mp3.next();
        assertEquals(mp3.currentSong(), list.get(1));
        mp3.next();
        assertEquals(mp3.currentSong(), list.get(2));
        mp3.prev();

        assertEquals(mp3.currentSong(), list.get(1));
        mp3.next();
        assertEquals(mp3.currentSong(), list.get(2));
        mp3.next();
        assertEquals(mp3.currentSong(), list.get(3));
        mp3.next();
        assertEquals(mp3.currentSong(), list.get(3));
        assertTrue(mp3.isPlaying());

        verify(mp3);
    }


}
