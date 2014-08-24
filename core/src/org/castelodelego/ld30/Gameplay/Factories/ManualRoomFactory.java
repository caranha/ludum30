package org.castelodelego.ld30.Gameplay.Factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.GPSRandom;
import org.castelodelego.ld30.Gameplay.Entity;

/**
 * All rooms are 600x600
 *
 * FIXME: Implement this as an enum with an interface
 *
 * Created by caranha on 8/24/14.
 */
public class ManualRoomFactory {

    // empty,arrow
    static public Array<Entity> getCombatRoom(GPSRandom dice, int color)
    {
        switch (dice.nextInt(7))
        {
            case 0:
                return arrowRoom(dice,color);
            case 1:
            case 2:
                return forestRoom(dice,color);
            case 3:
            case 4:
                return pillarRoom(dice,color);
            case 5:
            case 6:
                return dangerRoom(dice,color);
        }
        return null;
    }

    static public Array<Entity> getStartRoom(GPSRandom dice, int color)
    {
        switch (dice.nextInt(2))
        {
            case 0:
                return emptyRoom(dice,color);
            case 1:
                return arrowRoom(dice,color);
        }
        return null;
    }

    static public Array<Entity> getDoorRoom(GPSRandom dice, int color, Entity player)
    {
        return doorRoom1(dice, color, player);
    }


    //FIXME: Set position sets the position of the CENTER of the block, need to add another offset


    static Array<Entity> forestRoom(GPSRandom dice, int color)
    {
        Array<Entity> ret = new Array<Entity>();
        int nTrees = dice.nextInt(5)+5;
        for (int i = 0; i < nTrees; i++)
        {
            int offSetX = dice.nextInt(550)+25;
            int offSetY = dice.nextInt(550)+25;

            Entity w;
            w = EntityFactory.simpleWall(color+1);
            w.setPosition(new Vector2(30+offSetX,30+offSetY));
            ret.add(w);

            w = EntityFactory.simpleWall(color);
            w.setPosition(new Vector2(offSetX,30+offSetY));
            ret.add(w);

            w = EntityFactory.simpleWall(color);
            w.setPosition(new Vector2(60+offSetX,30+offSetY));
            ret.add(w);

            w = EntityFactory.simpleWall(color);
            w.setPosition(new Vector2(30+offSetX,offSetY));
            ret.add(w);

            w = EntityFactory.simpleWall(color);
            w.setPosition(new Vector2(30+offSetX,60+offSetY));
            ret.add(w);
        }
        return ret;
    }

    static Array<Entity> pillarRoom(GPSRandom dice, int color)
    {
        boolean disco = false;
        if (dice.nextDouble() < 0.3) disco = true;

        Array<Entity> ret = new Array<Entity>();
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
            {
                Entity w = EntityFactory.simpleWall(color+(disco?dice.nextInt(6):0));
                w.setPosition(new Vector2((3*i+2)*30,(3*j+2)*30));
                ret.add(w);
            }
        return ret;
    }

    static Array<Entity> emptyRoom(GPSRandom dice, int color)
    {
        return new Array<Entity>();
    }

    static Array<Entity> dangerRoom(GPSRandom dice, int color)
    {
        boolean disco = false;
        if (dice.nextDouble() < 0.3) disco = true;

        int[] positionX = {0,1,2,3,4,5,6,7,12,13,14,15,16,17,18,19, 0, 1, 2, 3, 4, 5,14,15,16,17,18,19};
        int[] positionY = {3,3,3,3,4,5,6,7, 7, 6, 5, 4, 3, 3, 3, 3,14,14,14,14,14,14,14,14,14,14,14,14};

        Array<Entity> ret = new Array<Entity>();
        for (int i = 0; i < positionX.length;i++)
        {
            Entity w = EntityFactory.simpleWall(color + (disco?dice.nextInt(6):0));
            w.setPosition(new Vector2(positionX[i]*30,positionY[i]*30));
            ret.add(w);
        }

        return ret;
    }

    static Array<Entity> arrowRoom(GPSRandom dice, int color)
    {
        boolean disco = false;
        if (dice.nextDouble() < 0.3) disco = true;

        int[] positionX = { 4, 5, 5,5,5,5, 6,16,15,15,15,15,15,14};
        int[] positionY = {10,11,10,9,8,7,10,10,11,10, 9, 8, 7,10};

        Array<Entity> ret = new Array<Entity>();
        for (int i = 0; i < positionX.length;i++)
        {
            Entity w = EntityFactory.simpleWall(color + (disco?dice.nextInt(6):0));
            w.setPosition(new Vector2(positionX[i]*30,positionY[i]*30));
            ret.add(w);
        }

        return ret;
    }

    static Array<Entity> doorRoom1(GPSRandom dice, int color, Entity p)
    {
        boolean disco = false;
        if (dice.nextDouble() < 0.3) disco = true;

        int[] positionX = {  -2,-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,12,13,14,15,16,17,18,19,20,21};
        int[] positionY = { 14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14,14};

        Array<Entity> ret = new Array<Entity>();
        for (int i = 0; i < positionX.length;i++)
        {
            Entity w = EntityFactory.simpleWall(color + (disco?dice.nextInt(6):0));
            w.setPosition(new Vector2(positionX[i]*30,positionY[i]*30));
            ret.add(w);
        }

        Entity w = EntityFactory.door(color,true,p);
        w.setPosition(new Vector2(10.5f*30,14*30));
        ret.add(w);

        return ret;
    }


}
