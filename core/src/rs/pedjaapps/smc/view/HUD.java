package rs.pedjaapps.smc.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.HashSet;
import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.model.World;
import rs.pedjaapps.smc.screen.GameScreen;
import rs.pedjaapps.smc.utility.Constants;
import rs.pedjaapps.smc.utility.GameSaveUtility;
import rs.pedjaapps.smc.utility.Utility;

public class HUD
{
	World world;
	
	TextureRegion pause, play, fire, jump, up, down, right,
		left, soundOn, soundOff, musicOn, musicOff, pauseP, playP, fireP, jumpP, upP, 
		downP, leftP, rightP, soundOnP, soundOffP, musicOnP, musicOffP;
    public Rectangle pauseR, playR, fireR, jumpR, upR, downR, rightR,
		leftR, soundR, musicR;
	Texture itemBox, maryoL, goldM;
	Rectangle itemBoxR, maryoLR;
    public Array<Vector2> leftPolygon = new Array<Vector2>(5);
    public Array<Vector2> rightPolygon = new Array<Vector2>(5);
    public Array<Vector2> upPolygon = new Array<Vector2>(5);
    public Array<Vector2> downPolygon = new Array<Vector2>(5);

	public OrthographicCamera cam;
	SpriteBatch batch;

	ShapeRenderer shapeRenderer = new ShapeRenderer();

	public BitmapFont font, tts;

	public static float C_W = Gdx.graphics.getWidth();
	public static float C_H = Gdx.graphics.getHeight();

	public enum Key
	{
		none, pause, fire, jump, left, right, up, down, play, sound, music
	}

	public HashSet<Key> pressedKeys = new HashSet<Key>();
	
	float stateTime;
	public boolean updateTimer = true;
	
	String ttsText = "TOUCH ANYWHERE TO START";
	BitmapFont.TextBounds ttsBounds;
	boolean ttsFadeIn;
	float ttsAlpha = 1;

	public HUD(World world)
	{
		this.world = world;
		cam = new OrthographicCamera(C_W, C_H);
		cam.position.set(new Vector2(C_W / 2, C_H / 2), 0);
		cam.update();
		batch = new SpriteBatch();
        setBounds();
	}

    private void setBounds()
    {
        float width = C_H / 10f;
        float height = width;
        float x = C_W - width * 1.5f;
        float y = C_H - height * 1.5f;
        pauseR = new Rectangle(x, y, width, height);

        width = C_H / 7f;
        height = width;
        x = C_W - width * 1.5f;
        y = C_H - height * 5f;
        fireR = new Rectangle(x, y, width, height);

        x = x - width;
        y = y - height;
        jumpR = new Rectangle(x, y, width, height);

        x = width / 2f;
        y = height * 1.5f;
        width = width * 1.24f;
        leftR = new Rectangle(x, y, width, height);
        leftPolygon.clear();
        leftPolygon.add(new Vector2(x, y + height));
        leftPolygon.add(new Vector2(x + width - x / 100 * 23.25f, y + height));
        leftPolygon.add(new Vector2(x + width, y + height / 2));
        leftPolygon.add(new Vector2(x + width - x / 100 * 23.25f, y));
        leftPolygon.add(new Vector2(x, y));

        x = x + width + width / 4f;
        rightR = new Rectangle(x, y, width, height);
        rightPolygon.clear();
        rightPolygon.add(new Vector2(x, y + height / 2));
        rightPolygon.add(new Vector2(x + x / 100 * 23.25f, y + height));//x / 100 * 23.25%
        rightPolygon.add(new Vector2(x + width, y + height));
        rightPolygon.add(new Vector2(x + width, y));
        rightPolygon.add(new Vector2(x + x / 100 * 23.25f, y));

        width = C_H / 7f;
        height = width * 1.24f;
        x = x - width / 2f - width / 8f;
        y = y + height / 2f;
        upR = new Rectangle(x, y, width, height);
        upPolygon.clear();
        upPolygon.add(new Vector2(x, y + height));
        upPolygon.add(new Vector2(x + width, y + height));
        upPolygon.add(new Vector2(x + width, y + y / 100 * 23.25f));
        upPolygon.add(new Vector2(x + width / 2, y));
        upPolygon.add(new Vector2(x, y + y / 100 * 23.25f));

        y = y - height - height / 4f;
        downR = new Rectangle(x, y, width, height);
        downPolygon.clear();
        downPolygon.add(new Vector2(x + width / 2, y + width));
        downPolygon.add(new Vector2(x + width, y + height - y / 100 * 23.25f));
        downPolygon.add(new Vector2(x + width, y));
        downPolygon.add(new Vector2(x, y));
        downPolygon.add(new Vector2(x, y + height - y / 100 * 23.25f));

		width = C_H / 10f;
		height = width;
		x = C_W / 2 - width / 2;
		y = height * 2.1f + height;
		soundR = new Rectangle(x, y, width, height);
		
		x = x * 1.20f;
		musicR = new Rectangle(x, y, width, height);
		
		x = soundR.x * 0.80f;
		playR = new Rectangle(x, y, width, height);
		
		float ibSize = C_W / 12;
		itemBoxR = new Rectangle(C_W / 2 - ibSize, C_H - ibSize - ibSize / 5, ibSize, ibSize);
		
		float maryoLSize = itemBoxR.height / 2;
		maryoLR = new Rectangle(pauseR.x - maryoLSize * 3, itemBoxR.y + itemBoxR.height - maryoLSize - maryoLSize / 2, maryoLSize * 2, maryoLSize);
    }

    public void resize(int width, int height)
    {
        C_W = Gdx.graphics.getWidth();
        C_H = Gdx.graphics.getHeight();
        cam = new OrthographicCamera(C_W, C_H);
        cam.position.set(new Vector2(C_W / 2, C_H / 2), 0);
        cam.update();
        setBounds();
    }
	
	public void loadAssets()
	{
		Assets.manager.load("data/hud/controls.pack", TextureAtlas.class);
        Assets.manager.load("data/hud/pause.png", Texture.class);
		Assets.manager.load("data/hud/itembox.png", Texture.class);
        Assets.manager.load("data/hud/maryo_l.png", Texture.class);
        Assets.manager.load("data/hud/gold_m.png", Texture.class);
		Assets.manager.load("data/hud/game_over.png", Texture.class);
		
		FreetypeFontLoader.FreeTypeFontLoaderParameter ttsTextParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        ttsTextParams.fontFileName = Constants.DEFAULT_FONT_BOLD_FILE_NAME;
        ttsTextParams.fontParameters.size = (int) C_H / 15;
        ttsTextParams.fontParameters.characters = "TOUCHANYWERS";
        Assets.manager.load("touch_to_start.ttf", BitmapFont.class, ttsTextParams);
		
	}

    public void afterLoadAssets()
	{
		TextureAtlas atlas = Assets.manager.get("data/hud/controls.pack", TextureAtlas.class);
		pause = atlas.findRegion("pause");
		pauseP = atlas.findRegion("pause-pressed");
		play = atlas.findRegion("play");
		playP = atlas.findRegion("play-pressed");
		musicOn = atlas.findRegion("music-on");
        musicOnP = atlas.findRegion("music-on-pressed");
        musicOff = atlas.findRegion("music-off");
        musicOffP = atlas.findRegion("music-off-pressed");
        
		soundOn = atlas.findRegion("sound-on");
        soundOnP = atlas.findRegion("sound-on-pressed");
        soundOff = atlas.findRegion("sound-off");
        soundOffP = atlas.findRegion("sound-off-pressed");
        
		fire = atlas.findRegion("fire");
		fireP = atlas.findRegion("fire-pressed");
		jump = atlas.findRegion("jump");
		jumpP = atlas.findRegion("jump-pressed");
	    left = atlas.findRegion("dpad-left");
		leftP = atlas.findRegion("dpad-left-pressed");
		right = new TextureRegion(left);
		right.flip(true, false);
		rightP = new TextureRegion(leftP);
		rightP.flip(true, false);
		up = atlas.findRegion("dpad-up");
		upP = atlas.findRegion("dpad-up-pressed");
		down = new TextureRegion(up);
		down.flip(false, true);
		downP = new TextureRegion(upP);
		downP.flip(false, true);
		
		itemBox = Assets.manager.get("data/hud/itembox.png");
		maryoL = Assets.manager.get("data/hud/maryo_l.png");
		goldM = Assets.manager.get("data/hud/gold_m.png");
		
		Texture.TextureFilter filter = Texture.TextureFilter.Linear;
		itemBox.setFilter(filter, filter);
		maryoL.setFilter(filter, filter);
		goldM.setFilter(filter, filter);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/Roboto-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) C_H / 20;
        parameter.characters = "0123456789TimePontsx:";
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);
		font.setColor(1, 1, 1, 1);//white
		
		generator.dispose();
		
		tts = Assets.manager.get("touch_to_start.ttf");
		tts.setColor(1, 1, 1, 1);
		ttsBounds = tts.getBounds(ttsText);
		
	}

	public void render(GameScreen.GAME_STATE gameState, float deltaTime)
	{
		if (gameState == GameScreen.GAME_STATE.GAME_READY)
		{
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			if(ttsAlpha >= 1)
			{
				ttsAlpha = 1;
				ttsFadeIn = false;
			}
			else if(ttsAlpha <= 0.3f)
			{
				ttsFadeIn = true;
			}
			tts.setColor(1, 1, 1, ttsFadeIn ? (ttsAlpha += 0.02f) : (ttsAlpha -= 0.02f));
			
			tts.draw(batch, ttsText, C_W / 2 - ttsBounds.width / 2, C_H / 2 + ttsBounds.height / 2);
			batch.end();
		}
		else if (gameState == GameScreen.GAME_STATE.GAME_PAUSED)
		{
			drawPauseOverlay();
		}
		else
		{
			if(updateTimer)stateTime += deltaTime;
			batch.setProjectionMatrix(cam.combined);
			batch.begin();
			batch.draw(pressedKeys.contains(Key.pause) ? pauseP : pause, pauseR.x, pauseR.y, pauseR.width, pauseR.height);
			batch.draw(pressedKeys.contains(Key.fire) ? fireP : fire, fireR.x, fireR.y , fireR.width, fireR.height);
			batch.draw(pressedKeys.contains(Key.jump) ? jumpP : jump, jumpR.x, jumpR.y , jumpR.width, jumpR.height);
			batch.draw(pressedKeys.contains(Key.left) ? leftP : left, leftR.x, leftR.y , leftR.width, leftR.height);
			batch.draw(pressedKeys.contains(Key.right) ? rightP : right, rightR.x, rightR.y , rightR.width, rightR.height);
			batch.draw(pressedKeys.contains(Key.up) ? upP : up, upR.x, upR.y, upR.width, upR.height);
			batch.draw(pressedKeys.contains(Key.down) ? downP : down, downR.x, downR.y, downR.width, downR.height);
			batch.draw(itemBox, itemBoxR.x, itemBoxR.y, itemBoxR.width, itemBoxR.height);
			batch.draw(maryoL, maryoLR.x, maryoLR.y, maryoLR.width, maryoLR.height);
			
			// points
			String points = String.format("Points %08d", GameSaveUtility.getInstance().save.points);
			BitmapFont.TextBounds bounds = font.getBounds(points);
			float pointsX = C_W * 0.03f;
			float pointsY = bounds.height / 2 + maryoLR.y + maryoLR.height / 2;
			font.setColor(0, 0, 0, 1);
			font.draw(batch, points, pointsX + C_W * 0.001f, pointsY - C_H * 0.001f);
			font.setColor(1, 1, 1, 1);
			font.draw(batch, points, pointsX, pointsY);
			
			//coins
			float goldHeight = bounds.height * 1.1f;
			float goldX = pointsX + bounds.width + goldHeight;
			batch.draw(goldM, goldX, pointsY - bounds.height, goldHeight * 2, goldHeight);
			
			String coins = GameSaveUtility.getInstance().save.coins + "";
			font.setColor(0, 0, 0, 1);
			font.draw(batch, coins, goldX + goldHeight * 2 + C_W * 0.001f, pointsY - C_H * 0.001f);
			font.setColor(1, 1, 1, 1);
			font.draw(batch, coins, goldX + goldHeight * 2, pointsY);
			
			//time
			String time = Utility.millisToString(stateTime);
			bounds = font.getBounds("Time " + time);
			float timeX = (itemBoxR.x + itemBoxR.width) + (maryoLR.x - (itemBoxR.x + itemBoxR.width)) / 2 - bounds.width / 2;
			font.setColor(0, 0, 0, 1);
			font.draw(batch, "Time " + time, timeX + C_W * 0.001f, pointsY - C_H * 0.001f);
			font.setColor(1, 1, 1, 1);
			font.draw(batch, "Time " + time, timeX, pointsY);
			
			//lifes
			String lifes = Math.max(GameSaveUtility.getInstance().save.lifes, 0) + "x";
			bounds = font.getBounds(lifes);
			float lifesX = maryoLR.x - bounds.width;
			font.setColor(0, 0, 0, 1);
			font.draw(batch, lifes, lifesX + C_W * 0.001f, pointsY - C_H * 0.001f);
			font.setColor(0, 1, 0, 1);
			font.draw(batch, lifes, lifesX, pointsY);
			
			batch.end();

            //drawDebug();
		}
	}

    private void drawDebug()
    {
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1);
        float[] vertices = new float[rightPolygon.size * 2];
        int offset = 0;
        for(Vector2 vect : rightPolygon)
        {
            vertices[offset] = vect.x;
            vertices[offset + 1] = vect.y;
            offset += 2;
        }
        shapeRenderer.polygon(vertices);
        shapeRenderer.end();
    }

    private void drawPauseOverlay()
	{
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL20.GL_BLEND);

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(0, 0, C_W, C_H);
		shapeRenderer.end();


		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		//pause
		float pHeight = C_H / 6;
		Texture pause = Assets.manager.get("data/hud/pause.png");
		float pWidth = pHeight * pause.getWidth() / pause.getHeight();

		batch.draw(pause, C_W / 2 - pWidth / 2, C_H / 1.9f, pWidth, pHeight);
		
		batch.draw(pressedKeys.contains(Key.play) ? playP : play, playR.x, playR.y, playR.width, playR.height);
		
		batch.draw(pressedKeys.contains(Key.sound) ? (Assets.playSounds ? soundOnP : soundOffP) : (Assets.playSounds ? soundOn : soundOff), soundR.x, soundR.y, soundR.width, soundR.height);
        batch.draw(pressedKeys.contains(Key.music) ? (Assets.playMusic ? musicOnP : musicOffP) : (Assets.playMusic ? musicOn : musicOff), musicR.x, musicR.y, musicR.width, musicR.height);
		
		//batch.draw(pressedKeys.contains(Key.sound) ? soundP : sound, soundR.x, soundR.y, soundR.width, soundR.height);
		

		batch.end();
	}

    public void leftPressed()
    {
        pressedKeys.add(Key.left);
    }

    public void leftReleased()
    {
        pressedKeys.remove(Key.left);
    }

    public void rightPressed()
    {
        pressedKeys.add(Key.right);
    }

    public void rightReleased()
    {
        pressedKeys.remove(Key.right);
    }

    public void upPressed()
    {
        pressedKeys.add(Key.up);
    }

    public void upReleased()
    {
        pressedKeys.remove(Key.up);
    }

    public void downPressed()
    {
        pressedKeys.add(Key.down);
    }

    public void downReleased()
    {
        pressedKeys.remove(Key.down);
    }

    public void firePressed()
    {
        pressedKeys.add(Key.fire);
    }

    public void fireReleased()
    {
        pressedKeys.remove(Key.fire);
    }

    public void jumpPressed()
    {
        pressedKeys.add(Key.jump);
    }

    public void jumpReleased()
    {
        pressedKeys.remove(Key.jump);
    }

    public void pausePressed()
    {
        pressedKeys.add(Key.pause);
    }

    public void pauseReleased()
    {
        pressedKeys.remove(Key.pause);
    }
	
	public void playPressed()
    {
        pressedKeys.add(Key.play);
    }

    public void playReleased()
    {
        pressedKeys.remove(Key.play);
    }
	
	public void soundPressed()
    {
        pressedKeys.add(Key.sound);
    }

    public void soundReleased()
    {
        pressedKeys.remove(Key.sound);
    }
	
	public void musicPressed()
    {
        pressedKeys.add(Key.music);
    }

    public void musicReleased()
    {
        pressedKeys.remove(Key.music);
    }
}
