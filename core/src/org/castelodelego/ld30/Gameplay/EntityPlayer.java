package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class EntityPlayer extends Entity {

    float xLimit;
    float yLimit;
    boolean escaped = false;

    public EntityPlayer(float maxWidth, float maxHeight)
    {
        super();
        xLimit = maxWidth;
        yLimit = maxHeight;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (position.x < 0) position.x = 0;
        if (position.x > xLimit) position.x = xLimit;
        if (position.y < 0) position.y = 0;
        if (position.y > yLimit) position.y = yLimit;
    }


    // TODO -- send pickup to pickup array if not full
    // TODO -- send pickup to pickup array if an upgrade
    public void addPickup(Pickup goodies) {
        if (goodies.getPickupType() == Pickup.PickupType.EXIT)
            escaped = true;
        else
            LD30Context.getInstance().addPickup(goodies);
    }


    public boolean getEscaped() { return escaped; }
}
