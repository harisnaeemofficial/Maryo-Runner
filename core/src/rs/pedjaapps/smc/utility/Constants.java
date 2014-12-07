package rs.pedjaapps.smc.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * Created by pedja on 2/15/14.
 */
public class Constants
{
    public static final String DEFAULT_FONT_FILE_NAME = "data/fonts/Roboto-Regular.ttf";
    public static final String DEFAULT_FONT_BOLD_FILE_NAME = "data/fonts/Roboto-Bold.ttf";


    public static float CAMERA_WIDTH/* = 10f*/;
    public static final float CAMERA_HEIGHT = 7f;
    public static float ASPECT_RATIO;
    public static final float DRAW_WIDTH = 12.444f;
    public static final float BACKGROUND_SCROLL_SPEED = 0.12f;
    public static final int GRAVITY = -20;
    static
    {
        initCamera();
    }

    public static void initCamera()
    {
        ASPECT_RATIO = (float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight();
        CAMERA_WIDTH = CAMERA_HEIGHT * ASPECT_RATIO;
    }

    public static final String[] SOUNDS = new String[]{"data/sounds/player/pickup_item.wav", "data/sounds/item/fireball_explode.wav",
            "data/sounds/item/fireball_repelled.wav", "data/sounds/item/iceball.wav",
            "data/sounds/item/ice_kill.wav", "data/sounds/item/fireball_explosion.wav",
            "data/sounds/item/iceball_explosion.wav", "data/sounds/item/empty_box.wav",
            "data/sounds/item/mushroom_blue.wav", "data/sounds/item/goldpiece_red.wav",
            "data/sounds/wall_hit.wav", "data/sounds/enemy/turtle/stand_up.wav",
            "data/sounds/enemy/boss/furball/hit_failed.wav", "data/sounds/enemy/boss/furball/hit.wav",
            "data/sounds/enemy/rokko/hit.wav", "data/sounds/enemy/rokko/activate.wav",
            "data/sounds/waypoint_reached.ogg", "data/sounds/player/ghost_end.ogg",
            "data/sounds/player/dead.ogg", "data/sounds/player/jump_big_power.ogg",
            "data/sounds/player/powerdown.ogg", "data/sounds/player/jump_ghost.ogg",
            "data/sounds/player/run_stop.ogg", "data/sounds/player/jump_small_power.ogg",
            "data/sounds/player/jump_big.ogg", "data/sounds/player/jump_small.ogg",
            "data/sounds/enter_pipe.ogg", "data/sounds/sprout_1.ogg", "data/sounds/babble/long_1.ogg",
            "data/sounds/leave_pipe.ogg", "data/sounds/itembox_set.ogg", "data/sounds/item/live_up_2.ogg",
            "data/sounds/item/star_kill.ogg", "data/sounds/item/mushroom_ghost.ogg",
            "data/sounds/item/live_up.ogg", "data/sounds/item/fireball.ogg", "data/sounds/item/mushroom.ogg",
            "data/sounds/item/moon.ogg", "data/sounds/item/fireplant.ogg", "data/sounds/item/goldpiece_1.ogg",
            "data/sounds/savegame_load.ogg", "data/sounds/itembox_get.ogg", "data/sounds/error.ogg",
            "data/sounds/stomp_1.ogg", "data/sounds/wood_1.ogg", "data/sounds/enemy/turtle/shell/hit.ogg",
            "data/sounds/enemy/turtle/hit.ogg", "data/sounds/enemy/gee/die.ogg",
            "data/sounds/enemy/furball/die.ogg", "data/sounds/enemy/boss/turtle/big_hit.ogg",
            "data/sounds/enemy/boss/turtle/hit.ogg", "data/sounds/enemy/boss/turtle/shell_attack.ogg",
            "data/sounds/enemy/boss/turtle/power_up.ogg", "data/sounds/enemy/thromp/die.ogg",
            "data/sounds/enemy/thromp/hit.ogg", "data/sounds/enemy/flyon/die.ogg",
            "data/sounds/enemy/krush/die.ogg", "data/sounds/enemy/spika/move.ogg",
            "data/sounds/enemy/eato/die.ogg", "data/sounds/audio_on.ogg", "data/sounds/savegame_save.ogg",
            "data/sounds/stomp_4.ogg", "data/sounds/ambient/hum_metallic_1.ogg",
            "data/sounds/ambient/machine_1.ogg", "data/sounds/ambient/hum_medium_pitch_1.ogg",
            "data/sounds/ambient/wind_1.ogg", "data/sounds/ambient/thunder_1.ogg",
            "data/sounds/ambient/rain_thunder_1.ogg", "data/sounds/ambient/thunder_muffled_short_1.ogg",
            "data/sounds/ambient/wind_desert_1.ogg", "data/sounds/ambient/thunder_muffled_1.ogg",
            "data/sounds/waterdrop_1.ogg"};

    public static final String[] MUSIC = new String[]{"data/music/story/default_story_1.ogg", "data/music/land/land_5.ogg",
            "data/music/land/snow_1.ogg.ogg",
            "data/music/land/hyper_1.ogg", "data/music/land/waterway_1.ogg", "data/music/land/jungle_1.ogg",
            "data/music/land/land_3.ogg", "data/music/land/water_1.ogg",
            "data/music/land/land_4.ogg", "data/music/land/land_2.ogg", "data/music/land/land_1.ogg",
            "data/music/game/lost_1.ogg", "data/music/game/bossbattle_3.ogg", "data/music/game/menu.ogg",
            "data/music/game/bossbattle_1.ogg", "data/music/game/star.ogg"};

    public static final String[] ATLASES = new String[] {"data/signs/signs.pack", "data/ground/green_2/green_2.pack",
            "data/ground/jungle/jungle.pack", "data/ground/green_1/green_1.pack", "data/ground/ghost/ghost.pack",
            "data/ground/green_3/green_3.pack", "data/ground/castle/castle.pack", "data/ground/snow/snow.pack",
            "data/ground/desert/desert.pack", "data/ground/sand/sand.pack", "data/hud/controls.pack",
            "data/hud/SMCLook512.pack", "data/game/items/items.pack", "data/game/box/box.pack",
            "data/clouds/clouds.pack", "data/animation/light.pack", "data/animation/fireball.pack",
            "data/windows/windows.pack", "data/hills/hills.pack", "data/loading/loading.pack",
            "data/enemy/turtle/green.pack", "data/enemy/turtle/red.pack", "data/enemy/gee/electro.pack",
            "data/enemy/gee/lava.pack", "data/enemy/gee/venom.pack", "data/enemy/furball/brown.pack",
            "data/enemy/furball/blue.pack", "data/enemy/furball/boss.pack", "data/enemy/thromp/thromp.pack",
            "data/enemy/flyon/blue.pack", "data/enemy/flyon/orange.pack", "data/enemy/krush/krush.pack",
            "data/enemy/static/static.pack", "data/enemy/eato/brown.pack", "data/enemy/eato/green.pack",
            "data/enemy/spikeball/grey.pack", "data/pipes/pipes.pack", "data/maryo/ice.pack",
            "data/maryo/big.pack", "data/maryo/flying.pack", "data/maryo/small.pack", "data/maryo/fire.pack",
            "data/maryo/ghost.pack", "data/blocks/blocks.pack"};

    public static final String[] TEXTURES = new String[]{"data/game/background/beach-island.png",
            "data/game/background/low-sand.png", "data/game/background/green_junglehills.png",
            "data/game/background/moon_big_1.png", "data/game/background/blue-waterhills-1.png",
            "data/game/background/bottom-sand-fog.png", "data/game/background/ghost-hills-1.png",
            "data/game/background/sand-hill-1.png", "data/game/background/desert-dunes-1.png",
            "data/game/background/green-hills-2.png", "data/game/background/darkening.png",
            "data/game/background/puffy-clouds.png", "data/game/background/small-green-ballhills-1.png",
            "data/game/background/desert-hills-1.png", "data/game/background/snow-hills-1.png",
            "data/game/background/forest-2.png", "data/game/background/blue-mountains-1.png",
            "data/game/background/forest-1.png", "data/game/background/stars_1.png",
            "data/game/background/stone-columns-1.png", "data/game/background/blue_hills_1.png",
            "data/game/background/green-hills-1.png", "data/game/background/more_hills.png",
            "data/game/background/small-green-ballhills-2.png"};
}
