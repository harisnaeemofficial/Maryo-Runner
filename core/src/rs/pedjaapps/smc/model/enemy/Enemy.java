package rs.pedjaapps.smc.model.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import rs.pedjaapps.smc.model.DynamicObject;
import rs.pedjaapps.smc.model.World;

/**
 * Created by pedja on 18.5.14..
 */
public abstract class Enemy extends DynamicObject
{
    protected String textureAtlas;
    private String textureName;//name of texture from pack
	protected Direction direction = Direction.right;
    public boolean handleCollision = true;

	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	public Direction getDirection()
	{
		return direction;
	}

    public void hitByPlayer()
    {
        //TODO implement this in subclasses and dont call super
        handleCollision = false;
        world.trashObjects.add(this);
    }

    public enum Direction
	{
		right, left
	}

	public void setTextureAtlas(String textureAtlas)
	{
		this.textureAtlas = textureAtlas;
	}

	public String getTextureAtlas()
	{
		return textureAtlas;
	}

	public void setTextureName(String textureName)
	{
		this.textureName = textureName;
	}

	public String getTextureName()
	{
		return textureName;
	}
	
    enum CLASS
    {
        eato, flyon, furball, turtle, gee, krush, rokko, spika, spikeball, thromp, turtleboss
    }
	
	public enum ContactType
	{
		stopper, player, enemy
	}

    WorldState worldState = WorldState.IDLE;

    protected Enemy(World world, Vector2 size, Vector3 position)
    {
        super(world, size, position);
    }

    /*public Body createBody(World world, Vector3 position, float width, float height)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = getBodyType();
        bodyDef.position.set(position.x + width / 2, position.y + height / 2);

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1062;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.1f;

        body.createFixture(fixtureDef);
		body.setUserData(this);

        polygonShape.dispose();
        return body;
    }*/

    public static Enemy initEnemy(World world, String enemyClassString, Vector2 size, Vector3 position, int maxDowngradeCount)
    {
        CLASS enemyClass = CLASS.valueOf(enemyClassString);
        Enemy enemy = null;
        switch (enemyClass)
        {
            case eato:
                enemy = new Eato(world, size, position);
                break;
            case flyon:
                enemy = new Flyon(world, size, position);
                break;
			case furball:
                position.z = Furball.POS_Z;
                enemy = new Furball(world, size, position, maxDowngradeCount);
                break;
            case turtle:
                position.z = Turtle.POS_Z;
                enemy = new Turtle(world, size, position);
                break;
        }
        return enemy;
    }

    @Override
    public void update(float delta)
    {
        stateTime += delta;
    }

	public void handleCollision(ContactType ContactType)
	{
		// subclasses should implement this
	}
	
	@Override
	public float maxVelocity()
	{
		return DEF_MAX_VEL;
	}
}
