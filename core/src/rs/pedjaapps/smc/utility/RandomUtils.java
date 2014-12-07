package rs.pedjaapps.smc.utility;

import java.util.Random;

/**
 * Created by pedja on 7.12.14..
 */
public class RandomUtils extends Random
{
    /**
     * @param min generated value. Can't be > then max
     * @param max generated value
     * @return values in closed range [min, max].
     */
    public int nextInt(int min, int max)
    {
        if (min == max)
        {
            return max;
        }

        return nextInt(max - min + 1) + min;
    }

    public float nextFloat(float min, float max)
    {
        return nextFloat() * (min - max) + min;
    }
}
