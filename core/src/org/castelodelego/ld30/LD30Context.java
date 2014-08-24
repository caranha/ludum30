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

    int currentScore;
    int maxScore;

    LD30Context()
    {
        hasKeys = EnumSet.noneOf(KEYS.class);
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
        maxScore = 0;
        currentScore = 0;
    }

    public boolean addPickup(Pickup p)
    {
        if (p == null) {
            Gdx.app.error("LD30Context", "Tried to add a null pickup");
            return false;
        }

        switch (p.getPickupType())
        {
            case KEY:
                hasKeys.add(p.getKeyType());
                return true;
            case DIAMOND:
                currentScore += 100;
                return true;
            default:
                currentScore += 200;
                return true;
        }
    }

    public void doDeath()
    {
        currentScore = 0;
        hasKeys.clear();
    }

    public void doEscape()
    {
        if (currentScore > maxScore)
            maxScore = currentScore;
    }

    public boolean testKey(KEYS k) {
        return hasKeys.contains(k);
    }
    public void removeKey(KEYS k) {
        hasKeys.remove(k);
    }
    public int getCurrentScore() { return currentScore; }
    public int getMaxScore() { return maxScore; }

}
