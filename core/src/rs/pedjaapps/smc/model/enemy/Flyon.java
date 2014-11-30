package rs.pedjaapps.smc.model.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.model.World;
import rs.pedjaapps.smc.utility.Constants;
import rs.pedjaapps.smc.utility.Utility;

/**
 * Created by pedja on 18.5.14..
 */
public class Flyon extends Enemy
{
    public static final float FLYON_VELOCITY = 3f;
    private boolean goingUp = true, topReached, bottomReached;
    private long maxPositionReachedTs = 0;
    private long minPositionReachedTs = 0;
    private static final long STAY_TOP_TIME = 300;//2 seconds
    private static final long STAY_BOTTOM_TIME = 2500;//3 seconds

    public Flyon(World world, Vector2 size, Vector3 position)
    {
        super(world, size, position);
    }

    @Override
    public void loadTextures()
    {
        TextureAtlas atlas = Assets.manager.get(textureAtlas);
        Array<TextureAtlas.AtlasRegion> frames = atlas.getRegions();
        //frames.add(atlas.findRegion(TKey.two.toString()));

        Assets.animations.put(textureAtlas, new Animation(0.25f, frames));
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        TextureRegion frame = Assets.animations.get(textureAtlas).getKeyFrame(stateTime, true);

		Utility.draw(spriteBatch, frame, bounds.x, bounds.y, bounds.height);
    }

    public void update(float deltaTime)
    {
		/*// Setting initial vertical acceleration 
        acceleration.y = Constants.GRAVITY;

        // Convert acceleration to frame time
        acceleration.scl(deltaTime);

        // apply acceleration to change velocity
        velocity.add(acceleration);*/
		
        stateTime += deltaTime;

        long timeNow = System.currentTimeMillis();
        if((topReached && timeNow - maxPositionReachedTs < STAY_TOP_TIME))
        {
			setVelocity(0, -Constants.GRAVITY);
            return;
        }
        else
        {
            if(position.y > 5)
            {
                maxPositionReachedTs = System.currentTimeMillis();
                goingUp = false;
                topReached = true;
            }
            else
            {
                topReached = false;
                maxPositionReachedTs = 0;
            }
        }
        if((bottomReached && timeNow - minPositionReachedTs < STAY_BOTTOM_TIME))
        {
            setVelocity(0, 0);
            return;
        }
        else
        {
            if(position.y <= 1.5f)
            {
                minPositionReachedTs = System.currentTimeMillis();
                goingUp = true;
                bottomReached = true;
            }
            else
            {
                bottomReached = false;
                minPositionReachedTs = 0;
            }
        }
        if(goingUp)
        {
            setVelocity(0, velocity.y =+((Constants.CAMERA_HEIGHT - position.y)/3f));
        }
        else
        {
			setVelocity(0, velocity.y =-((Constants.CAMERA_HEIGHT - position.y)/3f));
        }
		
		updatePosition(deltaTime);
    }
}
