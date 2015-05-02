package rs.pedjaapps.smc.utility;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.controller.MarioController;
import rs.pedjaapps.smc.model.Background;
import rs.pedjaapps.smc.model.BackgroundColor;
import rs.pedjaapps.smc.model.Box;
import rs.pedjaapps.smc.model.GameObject;
import rs.pedjaapps.smc.model.Level;
import rs.pedjaapps.smc.model.Maryo;
import rs.pedjaapps.smc.model.Sprite;
import rs.pedjaapps.smc.model.World;
import rs.pedjaapps.smc.model.enemy.Enemy;
import rs.pedjaapps.smc.model.enemy.EnemyStopper;
import rs.pedjaapps.smc.model.items.Item;
import com.badlogic.gdx.math.*;

/**
 * Created by pedja on 2/2/14.
 */

/**
 * This class loads level from json
 */
public class LevelGenerator
{
	public World world;

	float groundBlockY = -0.265625f;

	//RandomUtils random = new RandomUtils();

	int currentCloudSetIndex = 0;

	static final Array<Array<String>> clouds = new Array<Array<String>>();
	static final Array<Array<SpriteDescriptor>> groundDecoration = new Array<Array<SpriteDescriptor>>();

	static
	{
		Array<String> mDefault = new Array<String>();
		mDefault.add("default-left");
		mDefault.add("default-middle");
		mDefault.add("default");
		clouds.add(mDefault);

		Array<String> lightYellow = new Array<String>();
		lightYellow.add("lightyellow-1");
		lightYellow.add("lightyellow-2");
		lightYellow.add("lightyellow-3");
		lightYellow.add("lightyellow-4");
		lightYellow.add("lightyellow-5");
		lightYellow.add("lightyellow-6");
		lightYellow.add("lightyellow-7");
		lightYellow.add("lightyellow-8");
		lightYellow.add("lightyellow-9");
		lightYellow.add("lightyellow-10");
		clouds.add(lightYellow);

		Array<String> cloudy = new Array<String>();
		cloudy.add("cloudy-middle");
		clouds.add(cloudy);

		Array<String> grey = new Array<String>();
		grey.add("grey");
		clouds.add(grey);
		
		Array<SpriteDescriptor> green1 = new Array<SpriteDescriptor>();
		SpriteDescriptor sd = new SpriteDescriptor();
		sd.pack = "data/ground/green_1.pack";
		sd.region = "big1";
		
	}

	private enum ObjectClass
	{
		sprite, item, box, player, enemy, moving_platform, enemy_stopper, level_entry, level_exit,
	}
	
	private static final float m_pos_z_passive_start = 0.01f;
	private static final  float m_pos_z_massive_start = 0.08f;
	private static final  float m_pos_z_front_passive_start = 0.1f;
	private static final  float m_pos_z_halfmassive_start = 0.04f;

    public LevelGenerator(World world)
    {
		this.world = world;
    }

	/**
	 * Preload level
	 * Loads all starting data for level*/
	public void preLoad(OrthographicCamera cam)
	{
		//load player at starting position

		Maryo maryo = new Maryo(world, new Vector3(2, 2, 0.0999f), new Vector2(0.9f, 0.9f));
		maryo.loadTextures();
		world.level.maryo = maryo;
		world.level.gameObjects.add(maryo);

		//load first music
		world.level.music = "data/music/land/land_5.ogg";

		//load first background
		BackgroundColor backgroundColor = new BackgroundColor();
		Color color1 = PoolManager.obtainColor();
		color1.set(0.117647059f, 0.705882353f, 0.050980392f, 0);
		backgroundColor.color1 = color1;
		Color color2 = PoolManager.obtainColor();
		color2.set(0, 0.392156863f, 0, 0);
		backgroundColor.color2 = color2;
		world.level.background = new Background(cam, "data/game/background/green_junglehills.png", backgroundColor);

		//load ground for visible area(screen width)
		float groundStartX = -0.328125f;
		loadGround(groundStartX, cam.viewportWidth);
		addClouds(cam.position.x - cam.viewportWidth / 2, cam.viewportWidth, cam.viewportHeight);
	}

	/**
	 * Load ground for one camera viewport width*/
	private void loadGround(float groundStartX, float width)
	{
		for(int i = 0; i < width; i++)
		{
			float posx = groundStartX + i;
			Sprite sprite = new Sprite(world, new Vector2(1, 1), new Vector3(posx, groundBlockY, m_pos_z_massive_start));
			sprite.textureAtlas = "data/ground/green_3/green_3.pack";
			sprite.textureName = sprite.textureAtlas + ":" + "top";
			sprite.type = Sprite.Type.massive;
			sprite.loadTextures();
			world.level.gameObjects.add(sprite);
		}
	}

	/**
	 * Called from screens render method to generate more data for level*/
	public void update(OrthographicCamera cam)
	{
		float camWidth = cam.viewportWidth;
		float camHeight = cam.viewportHeight;
		float camStartX = cam.position.x - camWidth / 2;
		float camStartY = cam.position.y;

		Rectangle rect = PoolManager.obtainRect();
		rect.set(camStartX + camWidth - 0.1f, 0, 0.1f, camHeight);

		boolean foundGroundAtEndOfViewport = false;
		float lastGroundBlockEnd = 0;
		for(GameObject go : world.level.gameObjects)
		{
			if(go instanceof Sprite && go.position.y == groundBlockY)
			{
				if(rect.overlaps(go.bounds))
				{
					foundGroundAtEndOfViewport = true;
				}
				if(go.position.x + go.bounds.width > lastGroundBlockEnd)
				{
					lastGroundBlockEnd = go.position.x + go.bounds.width;
				}
			}
		}

		if(!foundGroundAtEndOfViewport)
		{
			if(lastGroundBlockEnd != 0)
			{
				loadGround(lastGroundBlockEnd, camWidth);
			}

			addClouds(lastGroundBlockEnd, camWidth, camHeight);
		}


	}

	private void addClouds(float camStartX, float camWidth, float camHeight)
	{
		//add 1-5 clouds on each screen on random elevations not too low
		int cloudNum = MathUtils.random(3, 5);
		Array<String> currentCloudSet = clouds.get(currentCloudSetIndex);
		for(int i = 0; i < cloudNum; i++)
		{
			String cloudTxt = currentCloudSet.get(MathUtils.random(currentCloudSet.size - 1));
			float height = MathUtils.random(0.5f, 1.1f);
			float x = MathUtils.random(camStartX, camStartX + camWidth + 2);
			float y = MathUtils.random(3.5f, camHeight);
			Sprite sprite = new Sprite(world, PoolManager.obtainVector2().set(0, height), PoolManager.obtainVector3().set(x, y, m_pos_z_passive_start));
			sprite.type = Sprite.Type.halfmassive;
			sprite.textureAtlas = "data/clouds/clouds.pack";
			sprite.textureName = sprite.textureAtlas + ":" + cloudTxt;
			sprite.loadTextures();
			world.level.gameObjects.add(sprite);
		}
	}

	static class SpriteDescriptor
	{
		float width, height;
		String pack, region;
	}
	
	/*private void parseGameObjects(World world, MarioController controller, JSONObject level) throws JSONException
	{
		JSONArray jObjects =  level.getJSONArray(KEY.objects.toString());
		for (int i = 0; i < jObjects.length(); i++)
		{
			JSONObject jObject = jObjects.getJSONObject(i);
			switch (ObjectClass.valueOf(jObject.getString(KEY.obj_class.toString())))
			{
				case sprite:
					parseSprite(world, jObject);
					break;
				case player:
					parsePlayer(jObject, world, controller);
					break;
				case item:
					parseItem(world, jObject);
					break;
				case enemy:
					parseEnemy(world, jObject);
					break;
				case enemy_stopper:
					parseEnemyStopper(world, jObject);
					break;
				case box:
					parseBox(world, jObject);
					break;
			}
		}
		//this.level.getGameObjects().sort(new ZSpriteComparator());
		Collections.sort(this.level.getGameObjects(), new ZSpriteComparator());
	}

	public Array<String[]> parseLevelData(String levelData)
	{
		if (levelData == null)
		{
			throw new IllegalArgumentException("levelData cannot be null");
		}
		levelData = levelData.trim();//clean spaces and new lines from start and end
		if (levelData.isEmpty())
		{
			throw new IllegalArgumentException("levelData cannot be empty");
		}

		Array<String[]> data = new Array<String[]>();
		String[] lines = levelData.split("\n");
		for (String line : lines)
		{
			if (line.startsWith("#"))continue;//skip comments
			String[] item = line.split(":");
			if (item[0] == null || item[1] == null)// both value and key must not be null
			{
				throw new IllegalArgumentException("failed to read data, null");
			}
			else if (!isDataKeyValid(item[0]))// check if we recognize key
			{
				throw new IllegalArgumentException("invalid key found in data: " + item[0]);
		 	}
			else if (item[1].trim().isEmpty())//check if value is empty string
			{
				throw new IllegalArgumentException("value with key:" + item[0] + "is invalid.");
			}
			data.add(item);
		}
		return data;
	}

	private boolean isDataKeyValid(String key)
	{
		for (DATA_KEY dk : DATA_KEY.values())
		{
			if (key.equals(dk.toString()))
			{
				return true;
			}
		}
		return false;
	}

    private void parseInfo(JSONObject jLevel) throws JSONException
    {
        JSONObject jInfo = jLevel.getJSONObject(KEY.info.toString());
        float width = (float) jInfo.getDouble(KEY.level_width.toString());
        float height = (float) jInfo.getDouble(KEY.level_height.toString());
        level.setWidth(width);
        level.setHeight(height);
        if (jInfo.has(KEY.level_music.toString()))
        {
            JSONArray jMusic = jInfo.getJSONArray(KEY.level_music.toString());
            Array<String> music = new Array<String>();
            for (int i = 0; i < jMusic.length(); i++)
            {
                music.add(jMusic.getString(i));
            }
            level.setMusic(music);
        }
    }

    private void parseBg(JSONObject jLevel) throws JSONException
    {
		if (jLevel.has(KEY.background.toString()))
		{
			JSONObject jBg = jLevel.getJSONObject(KEY.background.toString());
			String textureName = jBg.getString(KEY.texture_name.toString());
			if (Assets.manager.isLoaded(textureName))
			{
				Texture bgTexture = Assets.manager.get(textureName);
				Background bg = new Background(new Vector2(0, 0), bgTexture);
				level.setBackgrounds(bg);
				bg = new Background(new Vector2(Background.WIDTH, 0), bgTexture);
				level.setBg2(bg);
				//TODO this is stupid, we should dinamically repeat background
			}
			else
			{
				throw new IllegalArgumentException("Texture not found in AssetManager. Every Texture used" 
												   + "in [level].smclvl must also be included in [level].data (" + textureName + ")");
			}
			float r1 = (float) jBg.getDouble(KEY.r_1.toString()) / 255;//convert from 0-255 range to 0-1 range
			float r2 = (float) jBg.getDouble(KEY.r_2.toString()) / 255;
			float g1 = (float) jBg.getDouble(KEY.g_1.toString()) / 255;
			float g2 = (float) jBg.getDouble(KEY.g_2.toString()) / 255;
			float b1 = (float) jBg.getDouble(KEY.b_1.toString()) / 255;
			float b2 = (float) jBg.getDouble(KEY.b_2.toString()) / 255;

			BackgroundColor bgColor = new BackgroundColor();
			bgColor.color1 = new Color(r1, g1, b1, 0f);//color is 0-1 range where 1 = 255
			bgColor.color2 = new Color(r2, g2, b2, 0f);
			level.setBgColor(bgColor);
		}
		else
		{
			throw new IllegalStateException("level must have \"background\" object");
		}
    }

    private void parsePlayer(JSONObject jPlayer, World world, MarioController controller) throws JSONException
    {
        float x = (float) jPlayer.getDouble(KEY.posx.toString());
        float y = (float) jPlayer.getDouble(KEY.posy.toString());
        level.setSpanPosition(new Vector3(x, y, 0.0999f));
        if (controller != null)
        {
            Maryo maryo = new Maryo(world, level.getSpanPosition(), new Vector2(0.9f, 0.9f));
            maryo.loadTextures();
            world.setMario(maryo);
            level.getGameObjects().add(maryo);
            controller.setMaryo(maryo);
        }
    }

    private void parseSprite(World world, JSONObject jSprite) throws JSONException
    {
		
		Vector3 position = new Vector3((float) jSprite.getDouble(KEY.posx.toString()), (float) jSprite.getDouble(KEY.posy.toString()), 0);
		Sprite.Type sType = null;
		if(jSprite.has(KEY.massive_type.toString()))
		{
			sType = Sprite.Type.valueOf(jSprite.getString(KEY.massive_type.toString()));
		switch(sType)
		{
			case massive:
				position.z = m_pos_z_massive_start;
				break;
			case passive:
				position.z = m_pos_z_passive_start;
				break;
			case halfmassive:
				position.z = m_pos_z_halfmassive_start;
				break;
			case front_passive:
				position.z = m_pos_z_front_passive_start;
				break;
			case climbable:
				position.z = m_pos_z_halfmassive_start;
				break;
		}
		}
		else
		{
			position.z = m_pos_z_front_passive_start;
		}

		Sprite sprite = new Sprite(world, new Vector2((float) jSprite.getDouble(KEY.width.toString()), (float) jSprite.getDouble(KEY.height.toString())), position);
		sprite.setType(sType);

		sprite.setTextureName(jSprite.getString(KEY.texture_name.toString()));
		if (sprite.getTextureName() == null || sprite.getTextureName().isEmpty())
		{
			throw new IllegalArgumentException("texture name is invalid: \"" + sprite.getTextureName() + "\"");
		}
		if (jSprite.has(KEY.is_front.toString()))
		{
			sprite.setFront(jSprite.getBoolean(KEY.is_front.toString()));
		}

		//load all assets
		TextureAtlas atlas = null;
		if (jSprite.has(KEY.texture_atlas.toString()))
		{
			sprite.setTextureAtlas(jSprite.getString(KEY.texture_atlas.toString()));
			if (Assets.manager.isLoaded(sprite.getTextureAtlas()))
			{
				atlas = Assets.manager.get(sprite.getTextureAtlas());
			}
			else
			{
				throw new IllegalArgumentException("Atlas not found in AssetManager. Every TextureAtlas used" 
												   + "in [level].smclvl must also be included in [level].data ("
												   + sprite.getTextureAtlas() + ")");
			}
		}
		boolean hasFlipData = jSprite.has(KEY.flip_data.toString());

		if (hasFlipData)
		{
			JSONObject flipData = jSprite.getJSONObject(KEY.flip_data.toString());
			boolean flipX = flipData.getBoolean(KEY.flip_x.toString());
			boolean flipY = flipData.getBoolean(KEY.flip_y.toString());
			String newTextureName = null;
			if (flipX && !flipY)
			{
				newTextureName = sprite.getTextureName() + "-flip_x";
			}
			else if (flipY && !flipX)
			{
				newTextureName = sprite.getTextureName() + "-flip_y";
			}
			else if (flipY && flipX)
			{
				newTextureName = sprite.getTextureName() + "-flip_xy";
			}

			if(newTextureName != null)if (Assets.loadedRegions.get(newTextureName) == null)
			{
				TextureRegion orig;
				if (Assets.loadedRegions.get(sprite.getTextureName()) == null)
				{
					if (atlas == null)
					{
						if (Assets.manager.isLoaded(sprite.getTextureName()))
						{
							orig = new TextureRegion(Assets.manager.get(sprite.getTextureName(), Texture.class));
						}
						else
						{
							throw new IllegalArgumentException("Texture(" + sprite.getTextureName() + ") not found in AssetManager. Every Texture used" 
															   + "in [level].smclvl must also be included in [level].data (" + sprite.getTextureName() + ")");
						}
					}
					else
					{
						orig = atlas.findRegion(sprite.getTextureName().split(":")[1]);
					}
					Assets.loadedRegions.put(sprite.getTextureName(), orig);
				}
				else
				{
					orig = Assets.loadedRegions.get(sprite.getTextureName());
				}
				TextureRegion flipped = new TextureRegion(orig);
				flipped.flip(flipX, flipY);
				sprite.setTextureName(newTextureName);
				Assets.loadedRegions.put(newTextureName, flipped);
				
			}
			else
			{
				sprite.setTextureName(newTextureName);
			}
		}
		else
		{
			if (Assets.loadedRegions.get(sprite.getTextureName()) == null)
			{
				TextureRegion textureRegion;
				if (atlas == null)
				{
					if (Assets.manager.isLoaded(sprite.getTextureName()))
					{
						textureRegion = new TextureRegion(Assets.manager.get(sprite.getTextureName(), Texture.class));
					}
					else
					{
						throw new IllegalArgumentException("Texture (" + sprite.getTextureName() + ") not found in AssetManager. Every Texture used"
														   + "in [level].smclvl must also be included in [level].data ( " + sprite.getTextureName() + " )");
					}
				}
				else
				{
					textureRegion = atlas.findRegion(sprite.getTextureName().split(":")[1]);
				}
				Assets.loadedRegions.put(sprite.getTextureName(), textureRegion);
			}
		}

		level.getGameObjects().add(sprite);

    }

    

    public Level getLevel()
    {
        return level;
    }

    public static Class getTextureClassForKey(String key)
    {
        if (key.equals(DATA_KEY.txt.toString()))
        {
            return Texture.class;
        }
        else if (key.equals(DATA_KEY.atl.toString()))
        {
            return TextureAtlas.class;
        }
        else if (key.equals(DATA_KEY.mus.toString()))
        {
            return Music.class;
        }
        else if (key.equals(DATA_KEY.snd.toString()))
        {
            return Sound.class;
        }
        else
        {
            throw new IllegalArgumentException("Key: " + key + " is invalid!");
        }
    }

    public static boolean isTexture(String key)
    {
        return key.equals(DATA_KEY.txt.toString());
    }

    private void parseEnemy(World world, JSONObject jEnemy) throws JSONException
    {
        Vector3 position = new Vector3((float) jEnemy.getDouble(KEY.posx.toString()), (float) jEnemy.getDouble(KEY.posy.toString()), 0);

            Enemy enemy = Enemy.initEnemy(world, jEnemy.getString(KEY.enemy_class.toString()), new Vector2((float) jEnemy.getDouble(KEY.width.toString()), (float) jEnemy.getDouble(KEY.height.toString())), position, jEnemy.optInt(KEY.max_downgrade_count.toString()));
            if (enemy == null)return;//TODO this has to go aways after levels are fixed
            if (jEnemy.has(KEY.texture_atlas.toString()))
            {
                enemy.setTextureAtlas(jEnemy.getString(KEY.texture_atlas.toString()));
                if (Assets.manager.isLoaded(enemy.getTextureAtlas()))
                {
                    enemy.loadTextures();
                }
                else
                {
                    throw new IllegalArgumentException("Atlas not found in AssetManager. Every TextureAtlas used"
													   + "in [level].smclvl must also be included in [level].data (" + enemy.getTextureAtlas() + ")");
                }
            }
            level.getGameObjects().add(enemy);
    }

	private void parseEnemyStopper(World world, JSONObject jEnemyStopper) throws JSONException
    {
        Vector3 position = new Vector3((float) jEnemyStopper.getDouble(KEY.posx.toString()), (float) jEnemyStopper.getDouble(KEY.posy.toString()), 0);
        float width =  (float) jEnemyStopper.getDouble(KEY.width.toString());
		float height =  (float) jEnemyStopper.getDouble(KEY.height.toString());
		
		EnemyStopper stopper = new EnemyStopper(world, new Vector2(width, height), position);
		
		level.getGameObjects().add(stopper);
    }
	
    private void parseItem(World world, JSONObject jItem) throws JSONException
    {
            Vector3 position = new Vector3((float) jItem.getDouble(KEY.posx.toString()), (float) jItem.getDouble(KEY.posy.toString()), 0);

            Item item = Item.initObject(world, jItem.getString(KEY.type.toString()), new Vector2((float) jItem.getDouble(KEY.width.toString()), (float) jItem.getDouble(KEY.height.toString())), position);
            if(item == null) return;
            if (jItem.has(KEY.texture_atlas.toString()))
            {
                item.setTextureAtlas(jItem.getString(KEY.texture_atlas.toString()));
                if (Assets.manager.isLoaded(item.getTextureAtlas()))
                {
                    item.loadTextures();
                }
                else
                {
                    throw new IllegalArgumentException("Atlas not found in AssetManager. Every TextureAtlas used"
													   + "in [level].smclvl must also be included in [level].data (" + item.getTextureAtlas() + ")");
                }
            }
            level.getGameObjects().add(item);
    }

	private void parseBox(World world, JSONObject jBox) throws JSONException
    {
		Box box = Box.initBox(world, jBox, this);
		level.getGameObjects().add(box);
    }*/
	
	/** Comparator used for sorting, sorts in ascending order (biggset z to smallest z).
	 * @author mzechner */
	public class ZSpriteComparator implements Comparator<GameObject>
	{
		@Override
		public int compare (GameObject sprite1, GameObject sprite2)
		{
			if(sprite1.position.z > sprite2.position.z) return 1;
			if(sprite1.position.z < sprite2.position.z) return -1;
			return 0;
		}
	}

}
