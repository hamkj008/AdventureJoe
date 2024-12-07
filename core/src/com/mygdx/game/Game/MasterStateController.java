package com.mygdx.game.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


/* */
public class MasterStateController {

    private static Music music = null;

    // ===================================================================================================================

    public MasterStateController() {

        music = Gdx.audio.newMusic(Gdx.files.internal("Audio/Music/back.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
//        music.play();
    }

    // ===================================================================================================================

    public static void toggleMusic(boolean isPlaying) {
        if (isPlaying) {
            music.stop();
        }
        else {
//            music.play();
        }
    }

    // ===================================================================================================================

    public void dispose() {
        Gdx.app.log("dispose", "masterstatedispose");
        music.dispose();
    }

    public static Music getMusic() { return music; }
}
