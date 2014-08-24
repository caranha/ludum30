package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by caranha on 8/23/14.
 */
public class Entity {

    public enum CollisionType { PLAYER, ENEMY, PLAYERBULLET, ENEMYBULLET, WALL, PICKUP, NONE};
    CollisionType collisionType;

    Animation animation;
    Animation deathAnimation = null;
    Color hue;

    Rectangle hitBox;
    Vector2 position;
    Pickup pickup;

    // timed Attributes
    float lifeTime;
    float maxLife;
    int hitPoints;

    // measured per second;
    float moveSpeed;
    float rotationSpeed;
    float rotation;
    Float targetRotation;

    // AI
    Navigator navigator;
    Array<Entity> childArray = null;

    Sound deathSound = null;


    boolean destroyFlag;


    public Entity()
    {
        lifeTime = 0;
        maxLife = 30;
        hitPoints = 1;

        hue = Color.RED;
        position = new Vector2();
        hitBox = new Rectangle(position.x,position.y,5,5);

        moveSpeed = 0;
        rotationSpeed = 0;

        targetRotation = null;
        navigator = null;
    }

    public void update(float delta)
    {
        if (getDestroyed()) {
            Gdx.app.log("Entity","Entity tried to update while destroyed: "+this);
            return;
        }

        lifeTime += delta;
        if ((lifeTime > maxLife) || (hitPoints <= 0))
            setDestroyed();

        if (navigator != null)
            targetRotation = navigator.getTargetDirection(this);

        doMove(delta);
    }

    public void draw(SpriteBatch batch) {
        if (animation == null)
            return;
        TextureRegion sprite = animation.getKeyFrame(lifeTime);
        Vector2 offset = new Vector2(sprite.getRegionWidth()/2,sprite.getRegionHeight()/2);

        batch.setColor(hue); //BR
        batch.draw(sprite, (position.x - offset.x),(position.y - offset.y),offset.x,offset.y,sprite.getRegionWidth(),sprite.getRegionHeight(),1,1,rotation-90);
        batch.setColor(Color.WHITE);
    }
    public void drawDebug(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(hue);
        renderer.rect(hitBox.getX(),hitBox.getY(),hitBox.getWidth(),hitBox.getHeight());
    }

    public void doHit() {
        hitPoints--;
    }
    public void doRepulse(Rectangle hit) {
        //FIXME: Sometimes jump-glitches happen
        Rectangle repulse = new Rectangle();
        Intersector.intersectRectangles(this.hitBox,hit,repulse);

        if (repulse.width < repulse.height) {
            if (hitBox.x < hit.x)
                position.x -= repulse.width;
            else
                position.y += repulse.width;
        } else {
            if (hitBox.y < hit.y)
                position.y -= repulse.height;
            else
                position.y += repulse.height;
        }
        hitBox.setCenter(position);
    }


    public void setHitPoints(int hit) {
        hitPoints = hit;
    }
    public int getHitPoints() {
        return hitPoints;
    }

    public void setMoveSpeed(float speed) {
        moveSpeed = speed;
    }
    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setRotationSpeed(float speed) {
        rotationSpeed = speed;
    }
    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotation(float degrees) {
        rotation = degrees;
    }
    public float getRotation() {
        return rotation;
    }

    public void setPosition(Vector2 pos) {
        position.x = pos.x;
        position.y = pos.y;
        hitBox.setCenter(position);
    }
    public Vector2 getPosition()
    {
        //FIXME: This should be an immutable
        return position;
    }

    public void setHitBoxSize(float width, float height) {
        hitBox.setHeight(height);
        hitBox.setWidth(width);
    }
    public void setHitBoxAnimation() {
        hitBox.setWidth(animation.getKeyFrame(lifeTime).getRegionHeight());
        hitBox.setHeight(animation.getKeyFrame(lifeTime).getRegionWidth());
    }
    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setAnimation(Animation anim) {
        animation = anim;
    }
    public Animation getAnimation() {
        return animation;
    }

    public void setCollisionType(CollisionType t) {
        collisionType = t;
    }
    public CollisionType getCollisionType() {
        return collisionType;
    }

    public void setNavigator(Navigator n) { navigator = n; }
    public void setMaxLife(float t) { maxLife = t; }

    public void addChild(Entity e) {
        if (childArray == null)
            childArray = new Array<Entity>(false,10);
        childArray.add(e);
    }
    public Array<Entity> getChildArray() {
        return childArray;
    }

    public void setColor(Color c) { hue = c; }
    public Color getColor() { return hue; }

    public void setDestroyed() {
        if ((deathSound != null) && (destroyFlag==false))
            deathSound.play();
        destroyFlag = true;

        if (deathAnimation!= null)
        {
            Entity w = new Entity();
            w.setAnimation(deathAnimation);
            w.setHitBoxAnimation();
            w.setMoveSpeed(this.moveSpeed/3);
            w.setRotation(this.rotation);
            w.setPosition(this.position);
            w.setColor(getColor());
            w.setMaxLife(0.4f);
            w.setCollisionType(CollisionType.NONE);
            addChild(w);
        }



    }
    public boolean getDestroyed() {
        return destroyFlag;
    }
    public void setDeathAnim(Animation death) {
        deathAnimation = death;
    }


    public void setPickup(Pickup p) { pickup = p;}
    public Pickup getPickup() { return pickup; }

    public void setDeathSound(Sound s) { deathSound = s;}

    public void dispose() {
        if (navigator != null)
            navigator.dispose();
        if (childArray != null)
            childArray.clear();
        animation = null;
    }

    void calculateRotation(float delta) {
        if (targetRotation == null)
            return;

        float deltaRotation = targetRotation - rotation;
        deltaRotation += (deltaRotation>180) ? -360 : (deltaRotation<-180) ? 360 : 0;

        if (deltaRotation > 0)
            rotation = (rotation + rotationSpeed*delta + 360)%360;
        else
            rotation = (rotation - rotationSpeed*delta + 360)%360;
    }
    void doMove(float delta) {
        calculateRotation(delta);
        position.x += moveSpeed*delta*MathUtils.cosDeg(rotation);
        position.y += moveSpeed*delta*MathUtils.sinDeg(rotation);
        hitBox.setCenter(position);
    }

}

