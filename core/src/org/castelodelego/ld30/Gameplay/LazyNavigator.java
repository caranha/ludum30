package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by caranha on 8/24/14.
 */
public class LazyNavigator implements Navigator {

    boolean hunting;
    boolean giveUp;
    float huntDistance;
    float huntSpeed = 0;
    Entity target;

    public LazyNavigator(Entity t, float range, Boolean lazy)
    {
        target = t;
        hunting = false;
        huntDistance = range*range;
        giveUp = lazy;
    }

    @Override
    public Float getTargetDirection(Entity e) {
        if (target == null)
            return null;

        if (huntSpeed == 0)
            huntSpeed = e.getMoveSpeed();

        if (target.position.dst2(e.getPosition()) < huntDistance)
            hunting = true;
        else
            hunting = (hunting && !giveUp);

        if (!hunting)
            e.setMoveSpeed(0);
        else
            e.setMoveSpeed(huntSpeed);

        Vector2 tmp = target.getPosition().cpy();
        tmp.sub(e.getPosition());
        return tmp.angle();
    }

    public void dispose()
    {
        target = null;
    }
}
