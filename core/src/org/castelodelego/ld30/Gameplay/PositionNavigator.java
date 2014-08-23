package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.math.Vector2;
import org.castelodelego.ld30.Globals;

/**
 * Created by caranha on 8/24/14.
 */
public class PositionNavigator implements Navigator {

    Vector2 target;

    public void setTarget(Vector2 t)
    {
        if (target == null)
            target = new Vector2();

        target.set(t);
    }


    @Override
    public Float getTargetDirection(Entity e) {
        if (target == null)
            return null;

        if (target.dst(e.getPosition()) < 10) {
            target = null;
            return null;
        }

        Vector2 tmp = target.cpy();
        tmp.sub(e.position);
        return tmp.angle();

    }

    public void dispose()
    {
        target = null;
    }
}
