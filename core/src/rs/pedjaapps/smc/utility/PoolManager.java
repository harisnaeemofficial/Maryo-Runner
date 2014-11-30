package rs.pedjaapps.smc.utility;

import com.badlogic.gdx.graphics.Color;
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

    public static Color obtainColor()
    {
        return colorPool.obtain();
    }
}
