package rs.pedjaapps.smc.model;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.g2d.*;

import javax.xml.soap.Text;

import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.utility.Constants;

public class Background
{
	public static final float WIDTH = Constants.CAMERA_WIDTH;
	public static final float HEIGHT = Constants.CAMERA_HEIGHT;
	public Vector2 position;
	public String textureName;
	public float width;
	public float height;
	public BackgroundColor bgColor;

	public Background(Vector2 position, String textureName, BackgroundColor bgColor)
	{
		this.bgColor = bgColor;
		this.position = position;
		this.textureName = textureName;
		width = WIDTH;
		height = HEIGHT;
	}
	
	public Background(Background bgr)
	{
		position = bgr.position;
		textureName = bgr.textureName;
		width = bgr.width;
		height = bgr.height;
	}
	
	public void render(SpriteBatch spriteBatch, OrthographicCamera cam)
	{
		bgColor.render(cam);
		spriteBatch.draw(Assets.manager.get(textureName, Texture.class), position.x, position.y, width, height);
	}
}
