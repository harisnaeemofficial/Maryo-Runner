package rs.pedjaapps.smc.model;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.json.JSONException;
import org.json.JSONObject;


import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.model.items.Item;
import rs.pedjaapps.smc.utility.Constants;
import rs.pedjaapps.smc.utility.LevelGenerator;
import rs.pedjaapps.smc.utility.Utility;

public class Box extends Sprite
{
	//visibility type
	public static final int
	// always visible
	BOX_VISIBLE = 0,
	// visible after activation
	BOX_INVISIBLE_MASSIVE = 1,
	// only visible in ghost mode
	BOX_GHOST = 2,
	// visible after activation and only touchable in the activation direction
	BOX_INVISIBLE_SEMI_MASSIVE = 3;
	
	
	
	public static final float SIZE = 0.67f;
	String goldColor, animation, boxType, text;
	boolean forceBestItem, invisible;
	int usableCount, item;
	
	protected float stateTime;
	
	TextureRegion txDisabled;
	
	boolean hitByPlayer;
	float originalPosY;
	
	//item that pops out when box is hit by player
	Item itemObject;
	
	public Box(World world, Vector2 size, Vector3 position)
    {
        super(world, size, position);
		type = Type.massive;
		originalPosY = position.y;
    }

	@Override
	public void loadTextures()
	{
        txDisabled = new TextureRegion(Assets.manager.get("data/game/box/brown1_1.png", Texture.class));
		if(animation == null || "default".equalsIgnoreCase(animation))
		{
			if(textureName == null)
			{
				textureName = "data/game/box/yellow/default.png";
			}
			return;
		}
        if(textureAtlas == null)return;
		TextureAtlas atlas = Assets.manager.get(textureAtlas);
        Array<TextureRegion> frames = new Array<TextureRegion>();
		float animSpeed = 0;
		if("bonus".equalsIgnoreCase(animation))
		{
			frames.add(atlas.findRegion("1"));
			frames.add(atlas.findRegion("2"));
			frames.add(atlas.findRegion("3"));
			frames.add(atlas.findRegion("4"));
			frames.add(atlas.findRegion("5"));
			frames.add(atlas.findRegion("6"));
			animSpeed = 0.15f;
		}
		if("power".equalsIgnoreCase(animation))
		{
			frames.add(atlas.findRegion("power-1"));
			frames.add(atlas.findRegion("power-2"));
			frames.add(atlas.findRegion("power-3"));
			frames.add(atlas.findRegion("power-4"));
			animSpeed = 0.2f;
		}
		if("spin".equalsIgnoreCase(boxType) || "spin".equalsIgnoreCase(animation))
		{
			frames.add(new TextureRegion(Assets.manager.get("data/game/box/yellow/default.png", Texture.class)));
			frames.add(atlas.findRegion("1"));
			frames.add(atlas.findRegion("2"));
			frames.add(atlas.findRegion("3"));
			frames.add(atlas.findRegion("4"));
			frames.add(atlas.findRegion("5"));
			txDisabled = atlas.findRegion("6");
			animSpeed = 0.13f;
		}
		Assets.animations.put(textureAtlas, new Animation(animSpeed, frames));
	}

	@Override
	public void render(SpriteBatch spriteBatch)
	{
		if(invisible)
		{
			return;
		}
        if(usableCount == 0)
        {
            Utility.draw(spriteBatch, txDisabled, position.x, position.y, bounds.height);
        }
        else
        {
            if (textureName != null)
            {
                Texture tx = Assets.manager.get(textureName);
                Utility.draw(spriteBatch, tx, position.x, position.y, bounds.height);
            }
            if (textureAtlas != null && animation != null && !"default".equalsIgnoreCase(animation))
            {
                TextureRegion frame = Assets.animations.get(textureAtlas).getKeyFrame(stateTime, true);
                Utility.draw(spriteBatch, frame, position.x, position.y, bounds.height);
            }
        }
	}
	
	/*public static Box initBox(World world, JSONObject jBox, LevelGenerator loader) throws JSONException
	{
		Vector3 position = new Vector3((float) jBox.getDouble(LevelGenerator.KEY.posx.toString()), (float) jBox.getDouble(LevelGenerator.KEY.posy.toString()), 0);
		Vector2 size = new Vector2(SIZE, SIZE);
		
		Box box = new Box(world, size, position);
		
		box.goldColor = jBox.optString(LevelGenerator.KEY.gold_color.toString());
		box.animation = jBox.optString(LevelGenerator.KEY.animation.toString(), null);
		box.boxType = jBox.optString(LevelGenerator.KEY.type.toString());
		box.text = jBox.optString(LevelGenerator.KEY.text.toString());
		box.forceBestItem = jBox.optInt(LevelGenerator.KEY.force_best_item.toString(), 0) == 1;
		box.invisible = jBox.optInt(LevelGenerator.KEY.invisible.toString(), 0) == 1;
		box.usableCount = jBox.optInt(LevelGenerator.KEY.useable_count.toString(), -1);
		box.item = jBox.optInt(LevelGenerator.KEY.item.toString(), 0);
		
		box.textureName = jBox.optString(LevelGenerator.KEY.texture_name.toString(), null);
		if (jBox.has(LevelGenerator.KEY.texture_atlas.toString()))
		{
			box.setTextureAtlas(jBox.getString(LevelGenerator.KEY.texture_atlas.toString()));
			if (!Assets.manager.isLoaded(box.getTextureAtlas()))
			{
                throw new IllegalArgumentException("Atlas not found in AssetManager. Every TextureAtlas used"
                        + "in [level].smclvl must also be included in [level].data (" + box.getTextureAtlas() + ")");
			}
		}
        box.loadTextures();
		//create item contained in box
		switch(box.item)
		{
			case Item.TYPE_GOLDPIECE:
				createCoin(box, loader);
				break;
		}
		return box;
	}*/
	
	public static void createCoin(Box box, LevelGenerator loader)
	{
		Coin coin = new Coin(box.world, new Vector2(Coin.DEF_SIZE, Coin.DEF_SIZE), new Vector3(box.position));
		String ta = Coin.DEF_ATL;
		coin.textureAtlas = ta;
		if (Assets.manager.isLoaded(coin.textureAtlas))
		{
			coin.loadTextures();
		}
		else
		{
			throw new IllegalArgumentException("Atlas not found in AssetManager. Every TextureAtlas used"
											   + "in [level].smclvl must also be included in [level].data (" + coin.textureAtlas + ")");
		}
		coin.collectable = false;
		coin.visible = false;
		
		box.itemObject = coin;
		loader.world.level.gameObjects.add(coin);
	}
	
	public void handleHitByPlayer()
	{
        if(hitByPlayer)return;
        Sound sound = null;
		if((usableCount == -1 || usableCount > 0))//is disabled(no more items)
		{
            if(usableCount != -1)usableCount--;
			hitByPlayer = true;
			velocity.y = 3f;
			if(itemObject != null)
            {
                itemObject.popOutFromBox();
                if(itemObject instanceof Coin)
                {
                    if(itemObject.textureAtlas.contains("yellow"))
                    {
                        sound = Assets.manager.get("data/sounds/item/goldpiece_1.ogg");
                    }
                    else
                    {
                        sound = Assets.manager.get("data/sounds/item/goldpiece_red.wav");
                    }
                }
            }
		}
        else
        {
            sound = Assets.manager.get("data/sounds/wall_hit.wav");
        }
        if(sound != null && Assets.playSounds)sound.play();
	}
	
	@Override
    public void update(float delta)
    {
		if(hitByPlayer)
		{
			// Setting initial vertical acceleration 
			acceleration.y = Constants.GRAVITY;

			// Convert acceleration to frame time
			acceleration.scl(delta);

			// apply acceleration to change velocity
			velocity.add(acceleration);

			// scale velocity to frame units 
			velocity.scl(delta);
			
			// update position
			position.add(velocity);
			body.y = position.y;
			updateBounds();

			// un-scale velocity (not in frame time)
			velocity.scl(1 / delta);
			
			if(position.y <= originalPosY)
			{
				hitByPlayer = false;
				position.y = originalPosY;
				body.y = position.y;
				updateBounds();
			}
		}
        stateTime += delta;
    }
}
