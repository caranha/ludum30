package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import org.castelodelego.ld30.Globals;

/**
 * Created by caranha on 8/25/14.
 */
public class PlayerGun {

    final float shootTimeOut = 0.4f;
    float shootTimer = 0;
    Animation bulletAnimation;

    public PlayerGun()
    {
        bulletAnimation = Globals.animationManager.get("sprites/playerbullet");
    }

    public void update(float delta, Entity p)
    {
        shootTimer+= delta;
        if (shootTimer > shootTimeOut)
        {
            shootTimer = 0;
            p.addChild(createBullet(p));
        }
    }

    Entity createBullet(Entity p)
    {
        Entity ret = new Entity();
        ret.setAnimation(bulletAnimation);
        ret.getHitBox().setSize(6, 6);
        ret.setRotationSpeed(0);
        ret.setMoveSpeed(p.moveSpeed * 1.8f);
        ret.setRotation(p.rotation);
        ret.setMaxLife(4);
        ret.setColor(Color.WHITE);
        ret.setPosition(p.getPosition());
        ret.setCollisionType(Entity.CollisionType.PLAYERBULLET);
        return ret;
    }
}
