package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class EntityPlayer extends Entity {

    public void addPickup(Pickup goodies) {
        // TODO -- send pickup to pickup array if not full
        // TODO -- send pickup to pickup array if an upgrade

        LD30Context.getInstance().addPickup(goodies);
    }

}
