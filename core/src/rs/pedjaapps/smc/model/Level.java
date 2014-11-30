package rs.pedjaapps.smc.model;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedja on 1/31/14.
 */
public class Level
{
    public List<GameObject> gameObjects;
	public Background background;
    public String music;

	public Level()
	{
		this.gameObjects = new ArrayList<GameObject>();
	}
}
