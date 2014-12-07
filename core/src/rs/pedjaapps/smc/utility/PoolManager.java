package rs.pedjaapps.smc.utility;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by pedja on 30.11.14..<br>
 * Pool for all dynamically generated objects to minimize allocation while game is running
 */
public class PoolManager
{
    private static Pool<Color> colorPool = new Pool<Color>()
    {
        @Override
        protected Color newObject()
        {
            return new Color();
        }
    };

    private static Pool<Rectangle> rectPool = new Pool<Rectangle>()
    {
        @Override
        protected Rectangle newObject()
        {
            return new Rectangle();
        }
    };

    private static Pool<Vector2> vect2Pool = new Pool<Vector2>()
    {
        @Override
        protected Vector2 newObject()
        {
            return new Vector2();
        }
    };

    private static Pool<Vector3> vect3Pool = new Pool<Vector3>()
    {
        @Override
        protected Vector3 newObject()
        {
            return new Vector3();
        }
    };

    public static Color obtainColor()
    {
        return colorPool.obtain();
    }

    public static Rectangle obtainRect()
    {
        return rectPool.obtain();
    }

    public static Vector2 obtainVector2()
    {
        return vect2Pool.obtain();
    }

    public static Vector3 obtainVector3()
    {
        return vect3Pool.obtain();
    }
}
