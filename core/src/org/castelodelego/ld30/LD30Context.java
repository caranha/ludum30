package org.castelodelego.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.Gameplay.Entity;
import org.castelodelego.ld30.Gameplay.EntityPlayer;
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

    EntityPlayer player;

    LD30Context()
    {
        hasKeys = EnumSet.noneOf(KEYS.class);
        loadFromPreferences();
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
        player = getNewPlayer();
        saveToPreferences();
    }

    public void doEscape()
    {
        if (currentScore > maxScore)
            maxScore = currentScore;
        resetPlayer();
        saveToPreferences();
    }

    public boolean testKey(KEYS k) {
        return hasKeys.contains(k);
    }
    public void removeKey(KEYS k) {
        hasKeys.remove(k);
    }
    public int getCurrentScore() { return currentScore; }
    public int getMaxScore() { return maxScore; }

    public void resetPlayer()
    {
        player.reset();
        player.setPosition(new Vector2(300, 300));
    }

    public EntityPlayer getNewPlayer()
    {
        EntityPlayer ret = new EntityPlayer(600,2400);
        ret.setAnimation(Globals.animationManager.get("sprites/player"));
        ret.setPosition(new Vector2(300, 300));
        ret.setHitBoxSize(25, 25);
        ret.setRotation(0);
        ret.setRotationSpeed(120);
        ret.setMoveSpeed(140);
        ret.setColor(Color.WHITE);
        ret.setMaxLife(3600);
        ret.setHitPoints(3);
        ret.setCollisionType(Entity.CollisionType.PLAYER);
        ret.setDeathSound(Globals.assetManager.get("sounds/Death.ogg", Sound.class));
        ret.setDeathAnim(Globals.animationManager.get("sprites/player_death"));
        return ret;
    }

    public EntityPlayer getPlayer() {
        if (player == null)
            player = getNewPlayer();
        return player;
    }

    private void saveToPreferences() {
        Preferences pref = Gdx.app.getPreferences("GameContext");
        pref.putBoolean("RedKey",testKey(KEYS.RED));
        pref.putBoolean("GreenKey",testKey(KEYS.GREEN));
        pref.putBoolean("BlueKey",testKey(KEYS.BLUE));
        pref.putInteger("CurrentScore",currentScore);
        pref.putInteger("MaxScore",maxScore);
        pref.flush();
    }

    private void loadFromPreferences() {
        Preferences pref = Gdx.app.getPreferences("GameContext");
        if (pref.getBoolean("RedKey",false))
            hasKeys.add(KEYS.RED);
        if (pref.getBoolean("GreenKey",false))
            hasKeys.add(KEYS.GREEN);
        if (pref.getBoolean("BlueKey",false))
            hasKeys.add(KEYS.BLUE);

        currentScore = pref.getInteger("CurrentScore",0);
        maxScore = pref.getInteger("MaxScore",0);
    }

}
