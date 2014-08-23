package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by caranha on 8/24/14.
 */
public class TargetNavigator implements Navigator {

    Entity target = null;

    public void setTarget(Entity t) {
        target = t;
    }

    @Override
    public Float getTargetDirection(Entity e) {
        if (target == null)
            return null;
        else {
            Vector2 tmp = target.getPosition().cpy();
            tmp.sub(e.getPosition());
            return tmp.angle();
        }
    }

    public void dispose()
    {
        target = null;
    }
}
