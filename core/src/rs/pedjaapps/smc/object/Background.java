package rs.pedjaapps.smc.model;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import rs.pedjaapps.smc.*;
import rs.pedjaapps.smc.utility.*;

public class Background
{
	public Array<Vector2> positions;
	public Array<Vector2> trashPositions;
	public String textureName;
	public float width;
	public float height;
	public BackgroundColor bgColor;

	public Background(Camera cam, String textureName, BackgroundColor bgColor)
	{
		this.bgColor = bgColor;
		Texture texture = Assets.manager.get(textureName);
		height = cam.viewportHeight;
		width = height * texture.getWidth() / texture.getHeight();
		
		int initCap = Math.max(1, (int)Math.ceil(cam.viewportWidth / width));
		positions = new Array<Vector2>(true, initCap);
		
		for(int i = 0; i < initCap; i++)
		{
			positions.add(new Vector2(i * width, 0));
		}
		
		
		trashPositions = new Array<Vector2>(false, 2);
		this.textureName = textureName;
	}
	
	public Background(Background bgr)
	{
		positions = bgr.positions;
		textureName = bgr.textureName;
		width = bgr.width;
		height = bgr.height;
	}
	
	public void renderColor(Camera camera)
	{
		bgColor.render(camera);
	}
	
	public void render(Camera cam, SpriteBatch spriteBatch)
	{
		Texture texture = Assets.manager.get(textureName);

		Vector2 lastPosition = positions.size == 0 ? null : positions.get(positions.size - 1);
		if(lastPosition != null && lastPosition.x < cam.position.x + cam.viewportWidth / 2)
		{
			positions.add(PoolManager.obtainVector2().set(lastPosition.x + width, lastPosition.y));
		}
		
		for(Vector2 position : positions)
		{
			if(position.x + width < cam.position.x - cam.viewportWidth / 2)
			{
				trashPositions.add(position);
				continue;
			}
			spriteBatch.draw(texture, position.x, position.y, width, height);
		}
		
		positions.removeAll(trashPositions, false);
	}
}
