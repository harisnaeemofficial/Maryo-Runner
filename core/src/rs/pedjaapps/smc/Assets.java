package rs.pedjaapps.smc;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import java.util.HashMap;
import rs.pedjaapps.smc.utility.MyFileHandleResolver;
import rs.pedjaapps.smc.utility.PrefsManager;

/**
 * Created by pedja on 2/15/14.
 */
public class Assets
{
    public static AssetManager manager;
    public static TextureLoader.TextureParameter textureParameter;
    public static HashMap<String, TextureRegion> loadedRegions;
    public static HashMap<String, Animation> animations;
    public static boolean playMusic;
    public static boolean playSounds;

    static
    {

        textureParameter = new TextureLoader.TextureParameter();
        //textureParameter.genMipMaps = true;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        textureParameter.minFilter = Texture.TextureFilter.Linear;
        MyFileHandleResolver resolver = new MyFileHandleResolver();
        manager = new AssetManager(resolver);


        // set the loaders for the generator and the fonts themselves
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        loadedRegions = new HashMap<String, TextureRegion>();
        animations = new HashMap<String, Animation>();

        playMusic = PrefsManager.isPlayMusic();
        playSounds = PrefsManager.isPlaySounds();
    }

    public static void dispose()
    {
        loadedRegions.clear();
		manager.clear();
    }

    public enum TR
    {

    }

    public enum Asset
    {

    }

}
