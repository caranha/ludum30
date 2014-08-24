package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.Globals;
import org.castelodelego.ld30.LD30Context;

/**
 * Created by caranha on 8/24/14.
 */
public class TextRenderer {

    BitmapFont smallFont;
    BitmapFont mediumFont;
    BitmapFont marqueeFont;
    Animation keySprite;
    Color Teal = new Color(0,1f,1f,1f);


    String marqueeText;
    static Array<String> messageList;

    float messageTimeOut = 3;
    float messageTimer;

    public TextRenderer()
    {
        keySprite = Globals.animationManager.get("props/minikey");
        smallFont = Globals.assetManager.get("Joystix10.ttf",BitmapFont.class);
        mediumFont = Globals.assetManager.get("Joystix20.ttf",BitmapFont.class);
        marqueeFont = Globals.assetManager.get("Joystix40.ttf",BitmapFont.class);

        marqueeText = null;
        messageTimer = 0;
        messageList = new Array<String>(true,10);
    }

    public void update(float delta)
    {
        if (messageList.size > 0)
            messageTimer += delta;

        if (messageTimer > messageTimeOut) {
            messageTimer = 0;
            messageList.removeIndex(0);
        }
    }

    public static void addMessage(String message)
    {
        messageList.add(message);
    }
    public void setMarquee(String message) { marqueeText = message; }

    public void render(Camera uiCamera, Batch batch, ShapeRenderer renderer)
    {
        LD30Context context = LD30Context.getInstance();
        Entity p = context.getPlayer();


        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderer.setProjectionMatrix(uiCamera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        // TOP_BOX
        renderer.setColor(Teal);
        renderer.rect(13,uiCamera.viewportHeight-38,480-26,28);
        renderer.setColor(Color.BLACK);
        renderer.rect(15,uiCamera.viewportHeight-36,480-30,24);

        // MARQUEE BOX
        if (marqueeText != null)
        {
            BitmapFont.TextBounds bounds = marqueeFont.getMultiLineBounds(marqueeText);
            renderer.setColor(Teal);
            renderer.rect((uiCamera.viewportWidth-(bounds.width+16))/2,
                          (uiCamera.viewportHeight-(bounds.height+16))/2,
                          bounds.width+16,
                          bounds.height+16);
            renderer.setColor(Color.BLACK);
            renderer.rect((uiCamera.viewportWidth-(bounds.width+10))/2,
                    (uiCamera.viewportHeight-(bounds.height+10))/2,
                    bounds.width+10,
                    bounds.height+10);
        }

        // MESSAGE BOX
        if (messageList.size > 0)
        {
            BitmapFont.TextBounds bounds = mediumFont.getMultiLineBounds(messageList.first());
            if (messageTimer < 0.5f)
                renderer.setColor(Teal.cpy().mul(1,1,1,messageTimer*2));
            else if (messageTimeOut - messageTimer < 0.5f)
                renderer.setColor(Teal.cpy().mul(1,1,1,(messageTimeOut - messageTimer)*2));
            else
                renderer.setColor(Teal);

            renderer.rect((uiCamera.viewportWidth - (bounds.width + 16)) / 2,
                    20,
                    bounds.width + 16,
                    bounds.height + 16);

            if (messageTimer < 0.5f)
                renderer.setColor(Color.BLACK.cpy().mul(1,1,1,messageTimer*2));
            else if (messageTimeOut - messageTimer < 0.5f)
                renderer.setColor(Color.BLACK.cpy().mul(1,1,1,(messageTimeOut - messageTimer)*2));
            else
                renderer.setColor(Color.BLACK);

            renderer.rect((uiCamera.viewportWidth-(bounds.width+10))/2,
                    23,
                    bounds.width+10,
                    bounds.height+10);
        }
        renderer.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        // Top status;
        smallFont.setColor(Teal);
        smallFont.draw(batch,"Armor: "+p.hitPoints,200,uiCamera.viewportHeight-20);
        smallFont.draw(batch,"Score: "+context.getCurrentScore(),20,uiCamera.viewportHeight-20);
        smallFont.draw(batch,"Keys: ",370,uiCamera.viewportHeight-20);

        if (context.testKey(LD30Context.KEYS.RED)) {
            batch.setColor(Color.RED);
            batch.draw(keySprite.getKeyFrame(0),420,uiCamera.viewportHeight-35);
        }

        if (context.testKey(LD30Context.KEYS.GREEN)) {
            batch.setColor(Color.GREEN);
            batch.draw(keySprite.getKeyFrame(0),435,uiCamera.viewportHeight-35);
        }

        if (context.testKey(LD30Context.KEYS.BLUE)) {
            batch.setColor(Color.BLUE);
            batch.draw(keySprite.getKeyFrame(0),450,uiCamera.viewportHeight-35);
        }


        // marquee;

        if (marqueeText != null)
        {
            BitmapFont.TextBounds bounds = marqueeFont.getMultiLineBounds(marqueeText);
            marqueeFont.drawMultiLine(batch,marqueeText,
                    (uiCamera.viewportWidth)/2,
                    (uiCamera.viewportHeight+bounds.height)/2,
                    0f, BitmapFont.HAlignment.CENTER);
        }

        // messages;
        if (messageList.size > 0) {
            BitmapFont.TextBounds bounds = mediumFont.getMultiLineBounds(messageList.first());
            mediumFont.drawMultiLine(batch,messageList.first(),
                    uiCamera.viewportWidth/2,
                    27+bounds.height,
                    0f, BitmapFont.HAlignment.CENTER);

        }


        batch.end();




    }


}
