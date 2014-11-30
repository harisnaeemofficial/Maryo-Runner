package rs.pedjaapps.smc.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by pedja on 21.9.14..
 */
public class PrefsManager
{
    public static Preferences prefs = Gdx.app.getPreferences("main_prefs");

    public enum PrefsKey
    {
        sound, music, sg, debug
    }

    public static boolean isPlayMusic()
    {
        return prefs.getBoolean(PrefsKey.music.toString(), true);
    }

    public static boolean isPlaySounds()
    {
        return prefs.getBoolean(PrefsKey.sound.toString(), true);
    }

    public static void setPlayMusic(boolean playMusic)
    {
        prefs.putBoolean(PrefsKey.music.toString(), playMusic);
		flush();
    }

    public static void setPlaySounds(boolean playSounds)
    {
        prefs.putBoolean(PrefsKey.sound.toString(), playSounds);
		flush();
    }
	
	public static String getSaveGame()
    {
        return prefs.getString(PrefsKey.sg.toString(), null);
    }

    public static void setSaveGame(String saveGame)
    {
        prefs.putString(PrefsKey.sg.toString(), saveGame);
		flush();
    }

    public static boolean isDebug()
    {
        return prefs.getBoolean(PrefsKey.debug.toString(), true);
    }
	
	public static void flush()
	{
		prefs.flush();
	}
}
