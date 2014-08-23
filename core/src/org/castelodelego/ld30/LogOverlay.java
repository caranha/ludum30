package org.castelodelego.ld30;

/**
 * Created by caranha on 8/23/14.
 */
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;

public class LogOverlay {
Map<String,String> messages;
    public LogOverlay()
    {
        messages = new HashMap<String,String>();
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
        Globals.batch.setProjectionMatrix(LD30Game.globalcam.combined);
        Globals.batch.begin();
        Globals.debugText.setColor(Color.YELLOW);

        int lineSkip = 0;
        for (String message:messages.values())
        {
            /// FIXME: Replace top height with the height of the globalcam
            Globals.debugText.draw(Globals.batch, message, 0, 800 - lineSkip);
            lineSkip += 15;
        }
        Globals.batch.end();
    }
}
