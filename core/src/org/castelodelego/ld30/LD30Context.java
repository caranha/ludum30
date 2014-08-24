package org.castelodelego.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.Gameplay.Pickup;
import java.util.EnumSet;

/**
 * Singleton containing player items/etc
 *
 * Created by caranha on 8/24/14.
 */
public enum LD30Context {
    INSTANCE;

    static final int MAX_PICKUPS = 12;
    public enum KEYS { RED, GREEN, BLUE };
    EnumSet<KEYS> hasKeys;

    Pickup[] playerCurrentPickups;
    Array<Pickup> pickupList;

    Array<Pickup> test;

    LD30Context()
    {
        hasKeys = EnumSet.noneOf(KEYS.class);
        playerCurrentPickups = new Pickup[4];
        pickupList = new Array<Pickup>();
    }

    // Static getter
    public static LD30Context getInstance()
    {
        return INSTANCE;
    }

    // public functions
    public void reset()
    {
        hasKeys.clear();
        for (int i = 0; i < 4; i++)
            playerCurrentPickups[i] = null;
        pickupList.clear();
    }

    public boolean addPickup(Pickup p)
    {
        if (p == null) {
            Gdx.app.error("LD30Context", "Tried to add a null pickup");
            return false;
        }

        if (p.getPickupType() == Pickup.PickupType.KEY) {
            hasKeys.add(p.getKeyType());
            return true;
        }

        if (pickupList.size < MAX_PICKUPS) {
            pickupList.add(p);
            return true;
        }
        else {
            Gdx.app.log("INFORMATION:","Too many Pickups, this one was dropped");
            return false;
            // TODO: Add logic to replace lower level pickups
        }
    }

    public boolean testKey(KEYS k) {
        return hasKeys.contains(k);
    }
    public void removeKey(KEYS k) {
        hasKeys.remove(k);
    }

}
