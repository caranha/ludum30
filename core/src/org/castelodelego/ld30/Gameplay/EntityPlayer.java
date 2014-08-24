package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.castelodelego.ld30.Globals;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class EntityPlayer extends Entity {

    float xLimit;
    float yLimit;
    boolean escaped = false;

    boolean hasArmor = false;
    boolean hasSpeed = false;
    boolean hasTurn = false;
    boolean hasWeapon = false;

    Animation layer_armor;
    Animation layer_shoot;
    Animation layer_speed;
    Animation layer_turn;

    PlayerGun playerGun;

    public EntityPlayer(float maxWidth, float maxHeight)
    {
        super();
        xLimit = maxWidth;
        yLimit = maxHeight;

        layer_armor = Globals.animationManager.get("sprites/player_armor");
        layer_shoot = Globals.animationManager.get("sprites/player_shoot");
        layer_speed = Globals.animationManager.get("sprites/player_speed");
        layer_turn = Globals.animationManager.get("sprites/player_turn");
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (position.x < 0) position.x = 0;
        if (position.x > xLimit) position.x = xLimit;
        if (position.y < 0) position.y = 0;
        if (position.y > yLimit) position.y = yLimit;

        if (playerGun != null)
            playerGun.update(delta,this);
    }


    public void addPickup(Pickup goodies) {
        switch (goodies.getPickupType())
        {

            case KEY:
                LD30Context.getInstance().addPickup(goodies);
                switch(goodies.keyType)
                {
                    case RED:
                        TextRenderer.addMessage("Red Key Standing By!");
                        break;
                    case GREEN:
                        TextRenderer.addMessage("You got a Green Key!");
                        break;
                    case BLUE:
                        TextRenderer.addMessage("You got a Blue Key");
                        break;
                }
                break;

            case SHOOTER:
                if (!hasWeapon) {
                    TextRenderer.addMessage("Pew! Pew! Pew!");
                    playerGun = new PlayerGun();
                }
                else {
                    LD30Context.getInstance().addPickup(goodies);
                    TextRenderer.addMessage("You can't handle two guns!");
                }
                break;

            case SPEED_UP:
                if (!hasSpeed) {
                    TextRenderer.addMessage("Go Faster!");
                    hasSpeed = true;
                    moveSpeed += 50;
                }
                else {
                    LD30Context.getInstance().addPickup(goodies);
                    TextRenderer.addMessage("You're already fast!");
                }
                break;

            case TURN_UP:
                if (!hasTurn) {
                    hasTurn = true;
                    rotationSpeed += 90;
                    TextRenderer.addMessage("Wheeeee!");
                }
                else {
                    LD30Context.getInstance().addPickup(goodies);
                    TextRenderer.addMessage("I'm getting sick...");
                }
                break;

            case SHIELD:
                if (!hasArmor) {
                    hasArmor = true;
                    hitPoints += 5;
                    TextRenderer.addMessage("No one can touch you now!");
                } else {
                    LD30Context.getInstance().addPickup(goodies);
                    TextRenderer.addMessage("What are you, a Shuckle?");
                }
                break;

            case DIAMOND:
                LD30Context.getInstance().addPickup(goodies);
                switch(Globals.behaviorDice.nextInt(5))
                {
                    case 0:
                        TextRenderer.addMessage("You found diamonds!");
                        break;
                    case 1:
                        TextRenderer.addMessage("Gibe me Monies! HueHue!");
                        break;
                    case 2:
                        TextRenderer.addMessage("Rich! You're Rich!");
                        break;
                    case 3:
                        TextRenderer.addMessage("Ka-CHING!");
                        break;
                    case 4:
                        TextRenderer.addMessage("You found diamonds!");
                        break;
                }
                break;

            case EXIT:
                escaped = true;
                break;
        }
    }


    public boolean getEscaped() { return escaped; }

    public void reset() {
        escaped = false;
        lifeTime = 0;
        hitPoints = 3;
        if (hasArmor)
            hitPoints = 8;
        rotation = 0;
    }

    @Override
    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
        if (hasArmor) {
            TextureRegion sprite = layer_armor.getKeyFrame(lifeTime);
            Vector2 offset = new Vector2(sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2);
            batch.draw(sprite, (position.x - offset.x), (position.y - offset.y), offset.x, offset.y, sprite.getRegionWidth(), sprite.getRegionHeight(), 1, 1, rotation - 90);
        }

        if (hasSpeed) {
            TextureRegion sprite = layer_speed.getKeyFrame(lifeTime);
            Vector2 offset = new Vector2(sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2);
            batch.draw(sprite, (position.x - offset.x), (position.y - offset.y), offset.x, offset.y, sprite.getRegionWidth(), sprite.getRegionHeight(), 1, 1, rotation - 90);
        }

        if (hasWeapon) {
            TextureRegion sprite = layer_shoot.getKeyFrame(lifeTime);
            Vector2 offset = new Vector2(sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2);
            batch.draw(sprite, (position.x - offset.x), (position.y - offset.y), offset.x, offset.y, sprite.getRegionWidth(), sprite.getRegionHeight(), 1, 1, rotation - 90);
        }

        if (hasTurn) {
            TextureRegion sprite = layer_turn.getKeyFrame(lifeTime);
            Vector2 offset = new Vector2(sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2);
            batch.draw(sprite, (position.x - offset.x), (position.y - offset.y), offset.x, offset.y, sprite.getRegionWidth(), sprite.getRegionHeight(), 1, 1, rotation - 90);
        }



    }
}
