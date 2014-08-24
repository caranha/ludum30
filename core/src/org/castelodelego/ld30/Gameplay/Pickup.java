package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.graphics.Color;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class Pickup {

    public enum PickupType {KEY, SHOOTER, SPEED_UP, TURN_UP, SHIELD, EXIT}

    PickupType pickupType;
    LD30Context.KEYS keyType;
    int pickupLevel;

    public Pickup(PickupType type, int level, LD30Context.KEYS ktype)
    {
        pickupType = type;
        pickupLevel = level;
        keyType = ktype;
    }

    public PickupType getPickupType() { return pickupType; }
    public LD30Context.KEYS getKeyType() { return keyType; }
    public int getPickupLevel() { return pickupLevel; }
}
