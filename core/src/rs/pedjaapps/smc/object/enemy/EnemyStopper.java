package rs.pedjaapps.smc.model.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import rs.pedjaapps.smc.model.GameObject;
import rs.pedjaapps.smc.model.World;

public class EnemyStopper extends GameObject
{
	public EnemyStopper(World world, Vector2 size, Vector3 position)
    {
        super(world, size, position);
    }

    /*public Body createBody(World world, Vector3 position, float width, float height)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x + width / 2, position.y + height / 2);

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();

        polygonShape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1062;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        body.setUserData(this);

        polygonShape.dispose();
        return body;
    }*/


    @Override
	public void render(SpriteBatch spriteBatch)
	{
		// this object is invisible
	}

    @Override
    public void update(float delta)
    {

    }

    @Override
	public void loadTextures()
	{
		// TODO: Implement this method
	}
	
}
