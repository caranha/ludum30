package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import org.castelodelego.ld30.Globals;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class EntityPlayer extends Entity {

    float xLimit;
    float yLimit;
    boolean escaped = false;

    public EntityPlayer(float maxWidth, float maxHeight)
    {
        super();
        xLimit = maxWidth;
        yLimit = maxHeight;
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if (position.x < 0) position.x = 0;
        if (position.x > xLimit) position.x = xLimit;
        if (position.y < 0) position.y = 0;
        if (position.y > yLimit) position.y = yLimit;
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
                TextRenderer.addMessage("Pew! Pew! Pew!");
                break;
            case SPEED_UP:
                TextRenderer.addMessage("Go Faster!");
                break;
            case TURN_UP:
                TextRenderer.addMessage("Wheeeee!");
                break;
            case SHIELD:
                TextRenderer.addMessage("No one can touch you now!");
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
        rotation = 0;
    }
}
