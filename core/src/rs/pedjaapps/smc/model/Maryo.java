package rs.pedjaapps.smc.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.model.enemy.Eato;
import rs.pedjaapps.smc.model.enemy.Enemy;
import rs.pedjaapps.smc.model.enemy.Flyon;
import rs.pedjaapps.smc.screen.GameScreen;
import rs.pedjaapps.smc.utility.GameSaveUtility;

public class Maryo extends DynamicObject
{
    public enum MaryoState
    {
        small, big, fire, ice, ghost, flying
    }

    private static final float RUNNING_FRAME_DURATION = 0.08f;
	
	protected static final float MAX_VEL          = 4f;
	private static final float GOD_MOD_TIMEOUT = 3000;//3 sec
	
    WorldState worldState = WorldState.JUMPING;
    private MaryoState maryoState = GameSaveUtility.getInstance().save.playerState;
    boolean facingLeft = false;
    boolean longJump = false;

    public float groundY = 0;

	private boolean handleCollision = true;
	DyingAnimation dyingAnim = new DyingAnimation();

    public Sound jumpSound = null;

    public Rectangle debugRayRect = new Rectangle();
	
	/**
	 * Makes player invincible and transparent for all enemies
	 * Used (for limited time) when player is downgraded (or if you hack the game :D
	 */
	boolean godMode = false;
	long godModeActivatedTime;
    
    public Maryo(World world, Vector3 position, Vector2 size)
    {
        super(world, size, position);
        setupBoundingBox();
		
		position.y = body.y = bounds.y += 0.5f;
		
    }

    private void setupBoundingBox()
    {
		switch(maryoState)
		{
			case small:
				bounds.width = 0.9f;
				bounds.height = 0.9f;
				break;
			case big:
			case fire:
			case ghost:
			case ice:
				bounds.height = 1.09f;
				bounds.width = 1.09f;
				break;
			case flying:
				break;
		}
		body.x = bounds.x + bounds.width / 4;
		body.width = bounds.width / 2;
		position.x = body.x;
		
        if(worldState == WorldState.DUCKING)
		{
			body.height = bounds.height / 2;
		}
		else
		{
			body.height = bounds.height * 0.9f;
		}
    }

	@Override
    public void updateBounds()
    {
        bounds.x = body.x - bounds.width / 4;
        bounds.y = body.y;
    }

    public void loadTextures()
    {
        MaryoState[] states = new MaryoState[]{MaryoState.small, MaryoState.big, MaryoState.fire, MaryoState.ghost, MaryoState.ice};
        for(MaryoState ms : states)
        {
            loadTextures(ms.toString());
        }
        setJumpSound();
    }

    private void loadTextures(String state)
    {
        TextureAtlas atlas = Assets.manager.get("data/maryo/" + state + ".pack");

        Assets.loadedRegions.put(TKey.stand_right + ":" + state, atlas.findRegion(TKey.stand_right.toString()));
        TextureRegion tmp = new TextureRegion(Assets.loadedRegions.get(TKey.stand_right + ":" + state));
        tmp.flip(true, false);
        Assets.loadedRegions.put(TKey.stand_left + ":" + state, tmp);

        TextureRegion[] walkRightFrames = new TextureRegion[4];
        walkRightFrames[0] = Assets.loadedRegions.get(TKey.stand_right + ":" + state);
        walkRightFrames[1] = atlas.findRegion(TKey.walk_right_1 + "");
        walkRightFrames[2] = atlas.findRegion(TKey.walk_right_2 + "");
        walkRightFrames[3] = walkRightFrames[1];
        Assets.animations.put(AKey.walk_right + ":" + state, new Animation(RUNNING_FRAME_DURATION, walkRightFrames));

        TextureRegion[] walkLeftFrames = new TextureRegion[4];
        for (int i = 0; i < 4; i++)
        {
            walkLeftFrames[i] = new TextureRegion(walkRightFrames[i]);
            walkLeftFrames[i].flip(true, false);
        }
        Assets.animations.put(AKey.walk_left + ":" + state, new Animation(RUNNING_FRAME_DURATION, walkLeftFrames));

        Assets.loadedRegions.put(TKey.jump_right + ":" + state, atlas.findRegion(TKey.jump_right.toString()));
        tmp = new TextureRegion(Assets.loadedRegions.get(TKey.jump_right.toString() + ":" + state));
        tmp.flip(true, false);
        Assets.loadedRegions.put(TKey.jump_left + ":" + state, tmp);

        Assets.loadedRegions.put(TKey.fall_right + ":" + state, atlas.findRegion(TKey.fall_right.toString()));
        tmp = new TextureRegion(Assets.loadedRegions.get(TKey.fall_right.toString() + ":" + state));
        tmp.flip(true, false);
        Assets.loadedRegions.put(TKey.fall_left + ":" + state, tmp);

        if (MaryoState.small.toString().equals(state))
        {
            Assets.loadedRegions.put(TKey.dead_right + ":" + state, atlas.findRegion(TKey.dead_right.toString()));
            tmp = new TextureRegion(Assets.loadedRegions.get(TKey.dead_right.toString() + ":" + state));
            tmp.flip(true, false);
            Assets.loadedRegions.put(TKey.dead_left + ":" + state, tmp);
        }

        Assets.loadedRegions.put(TKey.duck_right + ":" + state, atlas.findRegion(TKey.duck_right.toString()));
        tmp = new TextureRegion(Assets.loadedRegions.get(TKey.duck_right.toString() + ":" + state));
        tmp.flip(true, false);
        Assets.loadedRegions.put(TKey.duck_left + ":" + state, tmp);
    }

    public void render(SpriteBatch spriteBatch)
    {
        TextureRegion marioFrame = isFacingLeft() ? Assets.loadedRegions.get(TKey.stand_left + ":" + maryoState) : Assets.loadedRegions.get(TKey.stand_right + ":" + maryoState);
        if (worldState.equals(WorldState.WALKING))
        {
            marioFrame = isFacingLeft() ? Assets.animations.get(AKey.walk_left + ":" + maryoState).getKeyFrame(getStateTime(), true) : Assets.animations.get(AKey.walk_right + ":" + maryoState).getKeyFrame(getStateTime(), true);
        }
        else if(worldState == WorldState.DUCKING)
        {
            marioFrame = isFacingLeft() ? Assets.loadedRegions.get(TKey.duck_left + ":" + maryoState) : Assets.loadedRegions.get(TKey.duck_right + ":" + maryoState);
        }
        else if (getWorldState().equals(WorldState.JUMPING))
        {
            if (velocity.y > 0)
            {
                marioFrame = isFacingLeft() ? Assets.loadedRegions.get(TKey.jump_left + ":" + maryoState) : Assets.loadedRegions.get(TKey.jump_right + ":" + maryoState);
            }
            else
            {
                marioFrame = isFacingLeft() ? Assets.loadedRegions.get(TKey.fall_left + ":" + maryoState) : Assets.loadedRegions.get(TKey.fall_right + ":" + maryoState);
            }
        }
		else if(worldState == WorldState.DYING)
		{
			marioFrame = isFacingLeft() ? Assets.loadedRegions.get(TKey.dead_left + ":" + maryoState) : Assets.loadedRegions.get(TKey.dead_right + ":" + maryoState);
		}
		
		//if god mode, make player half-transparent
		if(godMode)
		{
			Color color = spriteBatch.getColor();
			float oldA = color.a;
			
			color.a = 0.5f;
			spriteBatch.setColor(color);
			
			spriteBatch.draw(marioFrame, bounds.x, bounds.y, bounds.width, bounds.height);
			
			color.a = oldA;
			spriteBatch.setColor(color);
		}
		else
		{
        	spriteBatch.draw(marioFrame, bounds.x, bounds.y, bounds.width, bounds.height);
		}
    }

	@Override
	public void update(float delta)
	{
		//disable godmod after timeot
		if(godMode && System.currentTimeMillis() - godModeActivatedTime > GOD_MOD_TIMEOUT)
		{
			godMode = false;
		}
		if(worldState == WorldState.DYING)
		{
			stateTime += delta;
			if(dyingAnim.update(delta))super.update(delta);
		}
		else
		{
			super.update(delta);
            //check where ground is
            Array<GameObject> objects = world.getVisibleObjects();
            Rectangle rect = world.rectPool.obtain();
            debugRayRect = rect;
            rect.set(position.x, 0, body.width, position.y);
            float tmpGroundY = 0;
            float distance = body.y;
			GameObject closestObject = null;
            for(GameObject go : objects)
            {
                if(go == null)continue;
                if(go instanceof Sprite
                        && (((Sprite)go).getType() == Sprite.Type.massive || ((Sprite)go).getType() == Sprite.Type.halfmassive)
                        && rect.overlaps(go.getBody()))
                {
					if(((Sprite)go).getType() == Sprite.Type.halfmassive && body.y < go.body.y + go.body.height)
					{
						continue;
					}
                    float tmpDistance = body.y - (go.body.y + go.body.height);
                    if(tmpDistance < distance)
                    {
                        distance = tmpDistance;
                        tmpGroundY = go.body.y + go.body.height;
						closestObject = go;
                    }
                }
            }
            groundY = tmpGroundY;
			if(closestObject != null 
				&& closestObject instanceof Sprite 
				&& ((Sprite)closestObject).getType() == Sprite.Type.halfmassive
				&& worldState == WorldState.DUCKING)
			{
				position.y -= 0.1f;
			}
		}
	}

	@Override
	protected void handleCollision(GameObject object, boolean vertical)
	{
		if(!handleCollision)return;
		super.handleCollision(object, vertical);
		if(object instanceof Coin)
		{
			Coin coin = (Coin)object;
			if(!coin.playerHit)coin.hitPlayer();
		}
		else if(object instanceof Enemy && ((Enemy)object).handleCollision)
		{
            boolean deadAnyway = isDeadByJumpingOnTopOfEnemy(object);
            if(deadAnyway)
            {
                if(!godMode)downgradeOrDie(false);
            }
            else
            {
                if(velocity.y < 0 && vertical && body.y > object.body.y)//enemy death from above
                {
                    velocity.y = 5f * Gdx.graphics.getDeltaTime();
                    ((Enemy)object).hitByPlayer();
                }
                else
                {
                    if(!godMode)downgradeOrDie(false);
                }
            }
		}
		else if(object instanceof Box && position.y + body.height <= object.position.y)
		{
			((Box)object).handleHitByPlayer();
		}
	}

    private boolean isDeadByJumpingOnTopOfEnemy(GameObject object)
    {
        //TODO update this when you add new enemy classes
        return object instanceof Flyon || object instanceof Eato;
    }

    public boolean isFacingLeft()
    {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft)
    {
        this.facingLeft = facingLeft;
    }

    public WorldState getWorldState()
    {
        return worldState;
    }

    public void setWorldState(WorldState newWorldState)
    {
		if(worldState == WorldState.DYING)return;
        this.worldState = newWorldState;
		if(worldState == WorldState.DUCKING)
		{
			body.height = bounds.height / 2;
		}
		else
		{
			body.height = bounds.height * 0.9f;
		}
    }

    public boolean isLongJump()
    {
        return longJump;
    }

    public void setLongJump(boolean longJump)
    {
        this.longJump = longJump;
    }

    public void setStateTime(float stateTime)
    {
        this.stateTime = stateTime;
    }


    public float getStateTime()
    {
        return stateTime;
    }
	
	public boolean checkGrounded()
    {
        grounded = velocity.y == 0;
		return grounded;
    }
	
	public void setGrounded(boolean grounded)
	{
		this.grounded = grounded;
	}
	
	public boolean isGrounded()
	{
		return grounded;
	}

	@Override
	public float maxVelocity()
	{
		return MAX_VEL;
	}
	
	public void downgradeOrDie(boolean forceDie)
	{
		if(maryoState == MaryoState.small || forceDie)
		{
			worldState = WorldState.DYING;
			dyingAnim.start();
		}
		else
		{
			godMode = true;
			godModeActivatedTime = System.currentTimeMillis();
			//for now only make it small no matter the current state
			maryoState = MaryoState.small;
			GameSaveUtility.getInstance().save.playerState = maryoState;
			setupBoundingBox();
		}
	}
	
	public class DyingAnimation
	{
		private float diedTime;
		boolean upAnimFinished, dyedReset, firstDelayFinished;
		Vector3 diedPosition;
		boolean upBoost;
		
		public void start()
		{
			diedTime = stateTime;
			handleCollision = false;
			diedPosition = new Vector3(position);
            if(Assets.playSounds)
            {
                Sound sound = Assets.manager.get("data/sounds/player/dead.ogg");
                sound.play();
            }
			((GameScreen)world.screen).setGameState(GameScreen.GAME_STATE.PLAYER_DEAD);
			GameSaveUtility.getInstance().save.lifes--;
		}
		
		public boolean update(float delat)
		{
			velocity.x = 0;
			position.x = diedPosition.x;
			if(bounds.y + bounds.height < 0)//first check if player is visible
			{
				((GameScreen)world.screen).setGameState(GameScreen.GAME_STATE.GAME_OVER);
				world.trashObjects.add(Maryo.this);
				return false;
			}
			
			if(!firstDelayFinished && stateTime - diedTime < 0.5f)//delay 500ms
			{
				return false;
			}
            else
            {
                firstDelayFinished = true;
            }

            if (!upBoost)
            {
                //animate player up a bit
				velocity.y = 8f;
				upBoost = true;
            }
           
			return true;
		}
	}

    @Override
    protected void handleDroppedBelowWorld()
    {
		if(worldState != WorldState.DYING)
		{
        	downgradeOrDie(true);
		}
    }

    private void setJumpSound()
    {
        switch (maryoState)
        {
            case small:
                jumpSound = Assets.manager.get("data/sounds/player/jump_small.ogg");
                break;
            case big:
            case fire:
            case ice:
                jumpSound = Assets.manager.get("data/sounds/player/jump_big.ogg");
                break;
            case ghost:
                jumpSound = Assets.manager.get("data/sounds/player/jump_ghost.ogg");
                break;
        }
    }

    public MaryoState getMarioState()
    {
        return maryoState;
    }

    public void setMarioState(MaryoState marioState)
    {
        this.maryoState = marioState;
        setJumpSound();
    }
}
