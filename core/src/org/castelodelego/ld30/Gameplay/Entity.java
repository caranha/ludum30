package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.castelodelego.ld30.Globals;

/**
 * Created by caranha on 8/23/14.
 */
public class Entity {


    public enum MobType { MOB, BULLET, PICKUP, WALL};
    MobType collisionType;

    Animation animation;
    Color hue;

    Rectangle hitBox;
    Vector2 position;

    // timed Attributes
    float lifeTime;
    float maxLife;

    // measured per second;
    float moveSpeed;
    float rotationSpeed;
    float rotation;
    Float targetRotation;

    // AI
    Navigator navigator;


    boolean destroyFlag;


    public Entity()
    {
        lifeTime = 0;
        maxLife = 30;

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
        lifeTime += delta;
        if (lifeTime > maxLife)
            destroyFlag = true;

        if (navigator != null)
            targetRotation = navigator.getTargetDirection(this);

        doMove(delta);
    }

    public void destroy()
    {

    }

    // TODO: Drawing Routines
    public void draw(SpriteBatch batch) {
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
        return position;
    }

    public void setHitBoxSize(float width, float height) {
        hitBox.setHeight(height);
        hitBox.setWidth(width);
    }
    public void setHitBoxAnimation() {
        hitBox.setWidth(animation.getKeyFrame(lifeTime).getRegionWidth());
        hitBox.setHeight(animation.getKeyFrame(lifeTime).getRegionHeight());
    }
    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setAnimation(Animation anim) {
        System.out.println(anim);
        animation = anim;
    }
    public Animation getAnimation() {
        return animation;
    }

    public void setCollisionType(MobType t) {
        collisionType = t;
    }
    public MobType getCollisionType() {
        return collisionType;
    }

    public void setNavigator(Navigator n) { navigator = n; }

    public void setColor(Color c) { hue = c; }
    public Color getColor() { return hue; }

    public boolean isDestroyed() {
        return destroyFlag;
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

        Globals.log.addMessage("currRotation","curr: "+rotation);
        Globals.log.addMessage("targetRotation","target: "+targetRotation);
        Globals.log.addMessage("deltaRotation","delta: "+((rotation - targetRotation)%360));
    }
    void doMove(float delta) {
        calculateRotation(delta);
        position.x += moveSpeed*delta*MathUtils.cosDeg(rotation);
        position.y += moveSpeed*delta*MathUtils.sinDeg(rotation);
        hitBox.setCenter(position);
    }

}

