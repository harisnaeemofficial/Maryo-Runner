package rs.pedjaapps.smc.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.utility.Utility;

public class Sprite extends GameObject
{
    public String textureAtlas;
    public String textureName;//name of texture from pack or png
    public Type type = null;

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        if(textureAtlas != null)
        {
            TextureRegion region = Assets.loadedRegions.get(textureName);
            Utility.draw(spriteBatch, region, position.x, position.y, bounds.height);
        }
        else
        {
            Texture texture = Assets.manager.get(textureName);
            Utility.draw(spriteBatch, texture, position.x, position.y, bounds.height);
        }
        
    }

    @Override
    public void update(float delta)
    {

    }

    @Override
    public void loadTextures()
    {
        if(textureAtlas != null)
        {
            TextureAtlas atlas = Assets.manager.get(textureAtlas, TextureAtlas.class);
            Assets.loadedRegions.put(textureName, atlas.findRegion(textureName.split(":")[1]));
        }
		if(bounds.width == 0)
		{
		if(textureAtlas != null)
        {
            TextureRegion region = Assets.loadedRegions.get(textureName);
            bounds.width = bounds.height * region.getRegionWidth()/region.getRegionHeight();
        }
        else
        {
            Texture texture = Assets.manager.get(textureName);
            bounds.width = bounds.height * texture.getWidth()/texture.getHeight();
		}
		}
    }

    /**
     * Type of the block
     * massive = player cant pass by it
     * passive = player passes in front of it
     * front_passive = player passes behind it
     * */
    public enum Type
    {
        massive, passive, front_passive, halfmassive, climbable
    }

    public Sprite(World world, Vector2 size, Vector3 position)
    {
        super(world, size, position);
        this.position = position;
    }

    @Override
    public String toString()
    {
        return textureName;
    }
}
