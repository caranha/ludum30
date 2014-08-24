package org.castelodelego.ld30.Gameplay.Factories;

import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.GPSRandom;
import org.castelodelego.ld30.Gameplay.Entity;

/**
 * Created by caranha on 8/24/14.
 */
public class StageFactory {




    public static Array<Entity> createLevel(GPSRandom dice, Entity player)
    {
        int baseColor = dice.nextInt(3);
        int pattern = dice.nextInt(2);
        int difficulty = 0;
        switch(dice.nextInt(6))
        {
            case 0:
            case 1:
            case 2:
                difficulty = 0;
                break;
            case 3:
            case 4:
                difficulty = 1;
                break;
            case 5:
                difficulty = 2;
                break;
        }

        Array<Entity> ret = new Array<Entity>();
        Array<Entity> tmp;
        // stage 1, entrance
        tmp = ManualRoomFactory.getStartRoom(dice,baseColor);
        ret.addAll(tmp);
        tmp = PopulationFactory.populateEntrance();
        ret.addAll(tmp);

        // stage 2, combat
        tmp = ManualRoomFactory.getCombatRoom(dice,baseColor);
        updatePositions(tmp,1);
        ret.addAll(tmp);

        tmp = PopulationFactory.populateEnemies(dice,baseColor,player,difficulty);
        updatePositions(tmp,1);
        ret.addAll(tmp);

        if (pattern == 0)
        {
            tmp = ManualRoomFactory.getCombatRoom(dice,baseColor);
            updatePositions(tmp,2);
            ret.addAll(tmp);

            tmp = PopulationFactory.populateEnemies(dice,baseColor,player,difficulty+1);
            updatePositions(tmp,2);
            ret.addAll(tmp);

            tmp = ManualRoomFactory.getCombatRoom(dice,baseColor);
            updatePositions(tmp,3);
            ret.addAll(tmp);

            tmp = PopulationFactory.populateOpenKey(dice,baseColor,player,difficulty+2);
            updatePositions(tmp,3);
            ret.addAll(tmp);
        }
        else
        {
            tmp = ManualRoomFactory.getDoorRoom(dice,baseColor,player);
            updatePositions(tmp,2);
            ret.addAll(tmp);

            tmp = PopulationFactory.populateEnemies(dice,baseColor,player,difficulty+1);
            updatePositions(tmp,2);
            ret.addAll(tmp);

            tmp = ManualRoomFactory.getStartRoom(dice,baseColor);
            updatePositions(tmp,3);
            ret.addAll(tmp);

            tmp = PopulationFactory.populateVault(dice,baseColor,player,difficulty+2);
            updatePositions(tmp,3);
            ret.addAll(tmp);
        }

        return ret;
    }


    static void updatePositions(Array<Entity> w, int pos)
    {
        for (Entity aux:w)
        {
            aux.getPosition().add(0,pos*600);
        }
    }


}
