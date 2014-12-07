package rs.pedjaapps.smc.controller;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.*;

import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.model.GameObject;
import rs.pedjaapps.smc.model.Maryo;
import rs.pedjaapps.smc.model.World;

public class MarioController
{

    enum Keys
    {
        /*LEFT, RIGHT,*/ UP, DOWN, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS = 150l;
    private static final float MAX_JUMP_SPEED = 9f;
    
    private World world;
    private boolean jumped;

    private long jumpClickTime;

    static Set<Keys> keys = new HashSet<Keys>();

    public MarioController(World world)
    {
        this.world = world;
    }

    // ** Key presses and touches **************** //

    /*public void leftPressed()
    {
        keys.add(Keys.LEFT);
    }

    public void rightPressed()
    {
        keys.add(Keys.RIGHT);
    }*/

    public void upPressed()
    {
        keys.add(Keys.UP);
    }

    public void downPressed()
    {
        keys.add(Keys.DOWN);
    }

    public void jumpPressed()
    {
        if(world.level.maryo.isGrounded())
        {
            keys.add(Keys.JUMP);

            if(Assets.playSounds)
            {
                Sound sound = world.level.maryo.jumpSound;
                if(sound != null)sound.play();
            }
            jumpClickTime = System.currentTimeMillis();
        }
    }

    public void firePressed()
    {
        keys.add(Keys.FIRE);
    }

    /*public void leftReleased()
    {
        keys.remove(Keys.LEFT);
    }

    public void rightReleased()
    {
        keys.remove(Keys.RIGHT);
    }*/

    public void upReleased()
    {
        keys.remove(Keys.UP);
    }

    public void downReleased()
    {
        keys.remove(Keys.DOWN);
    }

    public void jumpReleased()
    {
        keys.remove(Keys.JUMP);
        jumped = false;
    }

    public void fireReleased()
    {
        keys.remove(Keys.FIRE);
    }

    /**
     * The main update method *
     */
    public void update(float delta)
    {
        world.level.maryo.setGrounded(world.level.maryo.position.y - world.level.maryo.groundY < 0.1f);
		if(!world.level.maryo.isGrounded())
		{
            world.level.maryo.setWorldState(Maryo.WorldState.JUMPING);
		}
        processInput();
        if (world.level.maryo.isGrounded() && world.level.maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
        {
            world.level.maryo.setWorldState(Maryo.WorldState.IDLE);
        }
        world.level.maryo.setFacingLeft(false);
        if (!world.level.maryo.getWorldState().equals(Maryo.WorldState.JUMPING) && !world.level.maryo.getWorldState().equals(Maryo.WorldState.DUCKING))
        {
            world.level. maryo.setWorldState(Maryo.WorldState.WALKING);
        }
        world.level.maryo.velocity.set(world.level.maryo.velocity.x += Maryo.ACCELERATION, world.level.maryo.velocity.y, world.level.maryo.velocity.z);
	}

    /**
     * Change Mario's state and parameters based on input controls *
     */
    private boolean processInput()
    {
        Vector3 vel = world.level.maryo.velocity;
        Vector3 pos = world.level.maryo.position;
        if (keys.contains(Keys.JUMP))
        {
            if (!jumped && vel.y < MAX_JUMP_SPEED && System.currentTimeMillis() - jumpClickTime < LONG_JUMP_PRESS)
            {
                world.level.maryo.velocity.set(vel.x, vel.y += 2f, vel.z);
            }
            else
            {
                jumped = true;
            }
        }
        /*if (keys.contains(Keys.LEFT))
        {
            // left is pressed
            world.level.maryo.setFacingLeft(true);
            if (!world.level.maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                world.level.maryo.setWorldState(Maryo.WorldState.WALKING);
            }
            world.level.maryo.velocity.set(vel.x = -4f, vel.y, vel.z);
        }
        else if (keys.contains(Keys.RIGHT))
        {
            // right is pressed
            world.level.maryo.setFacingLeft(false);
            if (!world.level.maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                world.level. maryo.setWorldState(Maryo.WorldState.WALKING);
            }
            world.level.maryo.velocity.set(vel.x = +4f, vel.y, vel.z);
        }*/
        else if (keys.contains(Keys.DOWN))
        {
            if (!world.level.maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                world.level.maryo.setWorldState(Maryo.WorldState.DUCKING);
            }
        }
        else
        {
            if (!world.level.maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                world.level.maryo.setWorldState(Maryo.WorldState.IDLE);
            }
            //slowly decrease linear velocity on x axes
            world.level.maryo.velocity.set(vel.x * 0.7f, /*vel.y > 0 ? vel.y * 0.7f : */vel.y, vel.z);
        }
        return false;
    }
}
