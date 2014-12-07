package rs.pedjaapps.smc.utility;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by pedja on 7.12.14..
 */
public class MyTextureAtlasLoader extends SynchronousAssetLoader<TextureAtlas, MyTextureAtlasLoader.TextureAtlasParameter>
{
    public MyTextureAtlasLoader(FileHandleResolver resolver)
    {
        super(resolver);
    }

    TextureAtlas.TextureAtlasData data;

    @Override
    public TextureAtlas load(AssetManager assetManager, String fileName, FileHandle file, TextureAtlasParameter parameter)
    {
        for (TextureAtlas.TextureAtlasData.Page page : data.getPages())
        {
            page.texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
        }

        return new TextureAtlas(data);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle atlasFile, TextureAtlasParameter parameter)
    {
        FileHandle imgDir = atlasFile.parent();

        if (parameter != null)
            data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, parameter.flip);
        else
        {
            data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, false);
        }

        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
        for (TextureAtlas.TextureAtlasData.Page page : data.getPages())
        {
            TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
            params.format = parameter == null || parameter.format == null ? page.format : parameter.format;
            params.genMipMaps = page.useMipMaps;
            params.minFilter = parameter == null || parameter.minFilter == null ? page.minFilter : parameter.minFilter;
            params.magFilter = parameter == null || parameter.magFilter == null ? page.magFilter : parameter.magFilter;
            dependencies.add(new AssetDescriptor<Texture>(page.textureFile, Texture.class, params));
        }
        return dependencies;
    }

    static public class TextureAtlasParameter extends AssetLoaderParameters<TextureAtlas>
    {
        /**
         * whether to flip the texture atlas vertically *
         */
        public boolean flip = false;

        public Pixmap.Format format = null;

        public Texture.TextureFilter minFilter = null;
        public Texture.TextureFilter magFilter = null;

        public TextureAtlasParameter()
        {
        }

        public TextureAtlasParameter(boolean flip)
        {
            this.flip = flip;
        }
    }
}
