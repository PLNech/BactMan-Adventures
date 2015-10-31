/*
=======================================================================
BactMan Adventures | Scientific popularisation through mini-games
Copyright (C) 2015 IONIS iGEM Team
Distributed under the GNU GPLv3 License.
(See file LICENSE.txt or copy at https://www.gnu.org/licenses/gpl.txt)
=======================================================================
*/

/**
 * Created by Robert Green on 08/03/2009.
 */

package fr.plnech.igem.ui.model;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import fr.plnech.igem.R;

import java.util.Collection;
import java.util.HashMap;

public class MusicManager {
    private static final String TAG = "MusicManager";

    private static final int MUSIC_PREVIOUS = -1;
    public static final int MUSIC_MENU = 0;
    private static final float DEFAULT_VOLUME = 0.25f;

    private static final HashMap<Integer, MediaPlayer> players = new HashMap<>();
    private static int currentMusic = -1;
    private static int previousMusic = -1;

    public static void start(Context context, int music) {
        start(context, music, false);
    }

    public static void start(Context context, int music, boolean force) {
        Log.d(TAG, "starting music " + music + ", force=" + force);
        if (!force && currentMusic > -1) {
            // already playing some music and not forced to change
            return;
        }
        if (music == MUSIC_PREVIOUS) {
            Log.d(TAG, "Using previous music [" + previousMusic + "]");
            music = previousMusic;
        }
        if (currentMusic == music) {
            // already playing this music
            return;
        }
        if (currentMusic != -1) {
            previousMusic = currentMusic;
            Log.d(TAG, "Previous music was [" + previousMusic + "]");
            // playing some other music, pause it and change
            pause();
        }
        currentMusic = music;
        Log.d(TAG, "Current music is now [" + currentMusic + "]");
        MediaPlayer mp = players.get(music);
        if (mp != null) {
            if (!mp.isPlaying()) {
                mp.setVolume(DEFAULT_VOLUME, DEFAULT_VOLUME);
                mp.start();
            }
        } else {
            if (music == MUSIC_MENU) {
                mp = MediaPlayer.create(context, R.raw.menus);
            } else {
                Log.e(TAG, "unsupported music number - " + music);
                return;
            }
            players.put(music, mp);
            if (mp == null) {
                Log.e(TAG, "player was not created successfully");
            } else {
                try {
                    mp.setLooping(true);
                    mp.start();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    public static void pause() {
        Log.d(TAG, "pausing music.");
        Collection<MediaPlayer> mps = players.values();
        for (MediaPlayer p : mps) {
            if (p.isPlaying()) {
                p.pause();
            }
        }
        // previousMusic should always be something valid
        if (currentMusic != -1) {
            previousMusic = currentMusic;
            Log.d(TAG, "Previous music was [" + previousMusic + "]");
        }
        currentMusic = -1;
        Log.d(TAG, "Current music is now [" + currentMusic + "]");
    }

    public static void release() {
        Log.d(TAG, "Releasing media players");
        Collection<MediaPlayer> mps = players.values();
        for (MediaPlayer mp : mps) {
            try {
                if (mp != null) {
                    if (mp.isPlaying()) {
                        mp.stop();
                    }
                    mp.release();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        mps.clear();
        if (currentMusic != -1) {
            previousMusic = currentMusic;
            Log.d(TAG, "Previous music was [" + previousMusic + "]");
        }
        currentMusic = -1;
        Log.d(TAG, "Current music is now [" + currentMusic + "]");
    }
}
