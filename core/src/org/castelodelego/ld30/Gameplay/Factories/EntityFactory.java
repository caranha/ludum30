package org.castelodelego.ld30.Gameplay.Factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.castelodelego.ld30.GPSRandom;
import org.castelodelego.ld30.Gameplay.*;
import org.castelodelego.ld30.Globals;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class EntityFactory {
    static final int FOREVER = 3600*24;
    static final Color[] colorList = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE, Color.PURPLE};
    static final LD30Context.KEYS[] keyList = {LD30Context.KEYS.RED, LD30Context.KEYS.GREEN, LD30Context.KEYS.BLUE};

    private static Color pickColor(int i) { return colorList[i%colorList.length]; }
    private static LD30Context.KEYS pickKey(int i) { return keyList[i%keyList.length];}

    public static Entity simpleWall(int c) {
        Entity ret = new Entity();
        ret.setAnimation(Globals.animationManager.get("props/simplewall_30x30"));
        ret.setHitBoxAnimation();
        ret.setColor(pickColor(c));
        ret.setMaxLife(FOREVER);
        ret.setCollisionType(Entity.CollisionType.WALL);
        return ret;
    }

    public static Entity lazyFollower(Entity p, int difficulty) {
        Entity ret = new Entity();
        ret.setAnimation(Globals.animationManager.get("sprites/spearer"));
        ret.getHitBox().setSize(15,15);
        ret.setRotationSpeed(90+5*difficulty);
        ret.setMoveSpeed(90+5*difficulty);
        ret.setColor(pickColor(difficulty));
        ret.setMaxLife(FOREVER);
        ret.setNavigator(new LazyNavigator(p,200+40*difficulty,(difficulty > 2?true:false)));
        ret.setCollisionType(Entity.CollisionType.ENEMY);
        return ret;
    }

    public static Entity key(int c) {
        Entity ret = new Entity();
        ret.setAnimation(Globals.animationManager.get("sprites/key"));
        ret.setHitBoxSize(38,38);
        ret.setColor(pickColor(c%3));
        ret.setPickup(new Pickup(Pickup.PickupType.KEY,0,pickKey(c)));
        ret.setMaxLife(FOREVER);
        ret.setCollisionType(Entity.CollisionType.PICKUP);
        return ret;
    }

    public static Entity diamond() {
        Entity ret = new Entity();
        ret.setAnimation(Globals.animationManager.get("sprites/diamond"));
        ret.setHitBoxSize(14,14);
        ret.setColor(Color.WHITE);
        ret.setPickup(new Pickup(Pickup.PickupType.DIAMOND,0,null));
        ret.setMaxLife(FOREVER);
        ret.setCollisionType(Entity.CollisionType.PICKUP);
        return ret;
    }

    public static Entity powerUp(GPSRandom dice)
    {
        int type = dice.nextInt(4);
        Entity ret = new Entity();
        ret.setHitBoxSize(18,18);
        ret.setCollisionType(Entity.CollisionType.PICKUP);
        ret.setMaxLife(FOREVER);

        switch(type)
        {
            case 0:
                ret.setAnimation(Globals.animationManager.get("sprites/pup_defense"));
                ret.setColor(Color.WHITE);
                ret.setPickup(new Pickup(Pickup.PickupType.SHIELD,0,null));
                break;
            case 1:
                ret.setAnimation(Globals.animationManager.get("sprites/pup_shoot"));
                ret.setColor(Color.PURPLE);
                ret.setPickup(new Pickup(Pickup.PickupType.SHOOTER,0,null));
                break;
            case 2:
                ret.setAnimation(Globals.animationManager.get("sprites/pup_speed"));
                ret.setColor(Color.GRAY);
                ret.setPickup(new Pickup(Pickup.PickupType.SPEED_UP,0,null));
                break;
            case 3:
                ret.setAnimation(Globals.animationManager.get("sprites/pup_turn"));
                ret.setColor(Color.PINK);
                ret.setPickup(new Pickup(Pickup.PickupType.TURN_UP,0,null));
                break;
        }

        return ret;
    }

    public static Entity door(int c, boolean vertical, Entity p) {
        Entity ret = new EntityDoor(p,pickKey(c));
        if (vertical)
            ret.setAnimation(Globals.animationManager.get("props/door_30x60"));
        else
            ret.setAnimation(Globals.animationManager.get("props/door_60x30"));
        ret.setHitBoxAnimation();
        ret.setColor(pickColor(c%3));
        ret.setMaxLife(FOREVER);
        ret.setCollisionType(Entity.CollisionType.WALL);
        return ret;
    }

    public static Entity exit()
    {
        Entity ret = new Entity();
        ret.setAnimation(Globals.animationManager.get("props/exit"));
        ret.setHitBoxSize(60,60);
        ret.setColor(Color.WHITE);
        ret.setMaxLife(FOREVER);
        ret.setCollisionType(Entity.CollisionType.PICKUP);
        ret.setPickup(new Pickup(Pickup.PickupType.EXIT,0,null));
        return ret;
    }

}
