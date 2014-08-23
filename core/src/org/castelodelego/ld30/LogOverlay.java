package org.castelodelego.ld30;

/**
 * Created by caranha on 8/23/14.
 */
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class LogOverlay {
    Map<String,String> messages;
    OrthographicCamera logCamera;


    public LogOverlay()
    {
        messages = new HashMap<String,String>();
        logCamera = new OrthographicCamera();
        logCamera.setToOrtho(false, 480, 800);
    }

    public void addMessage(String key, String message)
    {
        messages.put(key, message);
    }

    public void removeMessage(String key)
    {
        messages.remove(key);
    }

    public void render()
    {
        Globals.batch.setProjectionMatrix(logCamera.combined);
        Globals.batch.begin();
        Globals.debugText.setColor(Color.YELLOW);

        int lineSkip = 0;
        for (String message:messages.values())
        {
            /// FIXME: Replace top height with the height of the globalcam
            Globals.debugText.draw(Globals.batch, message, 0, logCamera.viewportHeight - lineSkip);
            lineSkip += 15;
        }
        Globals.batch.end();
    }
}
