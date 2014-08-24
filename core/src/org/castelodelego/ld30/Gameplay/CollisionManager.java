package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by caranha on 8/24/14.
 */
public class CollisionManager {

    Array<Entity> allies;
    Array<Entity> enemies;
    Array<Entity> pickups;
    Array<Entity> walls;

    public CollisionManager() {
        allies = new Array<Entity>(false,50);
        enemies = new Array<Entity>(false,50);
        pickups = new Array<Entity>(false,50);
        walls = new Array<Entity>(false,50);
    }

    public void addEntity(Entity e)
    {
        switch(e.getCollisionType())
        {

            case PLAYER:
            case PLAYERBULLET:
                allies.add(e);
                break;
            case ENEMY:
            case ENEMYBULLET:
                enemies.add(e);
                break;
            case WALL:
                walls.add(e);
                break;
            case PICKUP:
                pickups.add(e);
                break;
            case NONE:
                break;
        }
    }

    public void removeEntity(Entity e)
    {
        boolean success=false;
        switch(e.getCollisionType())
        {
            case PLAYER:
            case PLAYERBULLET:
                success = allies.removeValue(e,true);
                break;
            case ENEMY:
            case ENEMYBULLET:
                success = enemies.removeValue(e,true);
                break;
            case WALL:
                success = walls.removeValue(e,true);
                break;
            case PICKUP:
                success = pickups.removeValue(e,true);
                break;
            case NONE:
                break;
        }

        if (!success)
            Gdx.app.log("Collision","Failed to remove entity from CollisionMananger");
    }

    public void dispose() {
        allies.clear();
        enemies.clear();
        walls.clear();
        pickups.clear();
    }

    public void doCollisions()
    {
        for (Entity aux1:allies) {
            for (Entity aux2:enemies) {
                if (aux1.getHitBox().overlaps(aux2.getHitBox()))
                {
                    aux1.doHit();
                    aux2.doHit();
                }
            }
            for (Entity aux2:walls) {
                if (aux1.getHitBox().overlaps(aux2.getHitBox()))
                {
                    // FIXME: I can send directly the bounding box here
                    if (aux1.getCollisionType() == Entity.CollisionType.PLAYER)
                        aux1.doRepulse(aux2.getHitBox());
                    else
                        aux1.doHit();
                }
            }

            if (aux1.getCollisionType() == Entity.CollisionType.PLAYER)
                for (Entity aux2:pickups)
                    if (aux1.getHitBox().overlaps(aux2.getHitBox())) {
                        ((EntityPlayer) aux1).addPickup(aux2.getPickup());
                        aux2.setDestroyed();
                    }
        }

        for (Entity aux1:enemies) {
            for (Entity aux2:walls)
            {
                if (aux1.getHitBox().overlaps(aux2.getHitBox()))
                {
                    if (aux1.getCollisionType() == Entity.CollisionType.ENEMY)
                        aux1.doRepulse(aux2.getHitBox());
                    else
                        aux1.doHit();
                }
            }
        }
    }
}
