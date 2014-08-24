package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class EntityDoor extends Entity {

    LD30Context.KEYS doorColor;
    Entity target;
    float warningTimeStamp;
    float warningTimeOut;
    float openingDistance;

    public EntityDoor(Entity opener, LD30Context.KEYS color)
    {
        super();
        if (opener.getCollisionType() != CollisionType.PLAYER)
            Gdx.app.error("DOOR","Door initialized with a non-player as target");

        target = opener;
        doorColor = color;
        warningTimeOut = 5;
        warningTimeStamp = -10;
        openingDistance = 50*50;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);

        if (position.dst2(target.getPosition()) < openingDistance)
            if (LD30Context.getInstance().testKey(doorColor)) {
                LD30Context.getInstance().removeKey(doorColor);
                this.setDestroyed();
            } else {
                if (lifeTime - warningTimeStamp > warningTimeOut) {
                    warningTimeStamp = lifeTime;
                    Gdx.app.log("DOOR","Player does not have the right key to open the door");
                }
            }
    }
}
