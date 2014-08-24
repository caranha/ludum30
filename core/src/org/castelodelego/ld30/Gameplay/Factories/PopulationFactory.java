package org.castelodelego.ld30.Gameplay.Factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.GPSRandom;
import org.castelodelego.ld30.Gameplay.Entity;

/**
 * Created by caranha on 8/24/14.
 */
public class PopulationFactory {

    static public Array<Entity> populateEntrance()
    {
        Array<Entity> ret = new Array<Entity>();
        Entity w = EntityFactory.exit();
        w.setPosition(new Vector2(300,50));
        ret.add(w);
        return ret;
    }

    static public Array<Entity> populateEnemies(GPSRandom dice, int color, Entity player, int difficulty)
    {
        Array<Entity> ret = new Array<Entity>();
        int total = 4+(difficulty/2)+combo(dice,difficulty);

        for (int i = 0; i < total; i++)
        {
            Entity w = EntityFactory.lazyFollower(player,difficulty);
            w.setPosition(new Vector2(dice.nextInt(600),dice.nextInt(600)));
            ret.add(w);
        }

        //TODO add pickups
        return ret;
    }

    static public Array<Entity> populateOpenKey(GPSRandom dice, int color, Entity player, int difficulty)
    {
        Array<Entity> ret = new Array<Entity>();
        int total = 4+(difficulty/2)+combo(dice,difficulty);

        for (int i = 0; i < total/3; i++)
        {
            Entity w = EntityFactory.lazyFollower(player,difficulty);
            w.setPosition(new Vector2(dice.nextInt(600),dice.nextInt(600)));
            ret.add(w);
        }

        Entity w = EntityFactory.key(color+1);
        w.setPosition(new Vector2(dice.nextInt(300+150),dice.nextInt(300+150)));
        ret.add(w);
        return ret;
    }

    static public Array<Entity> populateVault(GPSRandom dice, int color, Entity player, int difficulty)
    {
        Array<Entity> ret = new Array<Entity>();

        Entity w;

        w = EntityFactory.key(color+1);
        w.setPosition(new Vector2(dice.nextInt(300+150),dice.nextInt(300+150)));
        ret.add(w);

        w = EntityFactory.key(color+2);
        w.setPosition(new Vector2(dice.nextInt(300+150),dice.nextInt(300+150)));
        ret.add(w);

        return ret;
    }

    static private int combo(GPSRandom dice, int difficulty)
    {
        float prob = 1;
        int ret = 0;
        float delta = 0.5f + 0.1f*difficulty;
        if (delta > 0.9f)
            delta = 0.9f;

        while (dice.nextDouble() < prob)
        {
            ret++;
            prob = prob*delta;
            if (ret > 20)
                return ret;
        }
        return ret;
    }


}
