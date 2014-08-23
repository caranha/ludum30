package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.GPSRandom;
import org.castelodelego.ld30.Globals;

import java.util.Iterator;

/**
 * Created by caranha on 8/23/14.
 */
public class GameScreen implements com.badlogic.gdx.Screen {

    Array<Entity> entityList;

    OrthographicCamera gameCamera;
    ShapeRenderer debugRenderer;
    float worldViewWidth = 480;
    float worldViewHeight = 800;


    public GameScreen(GPSRandom creator)
    {
        entityList = new Array<Entity>(false,20);

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false,worldViewWidth,worldViewHeight);

        debugRenderer = new ShapeRenderer();


        Entity test = new Entity();
        test.setAnimation(Globals.animationManager.get("sprites/spearer"));
        test.setPosition(new Vector2(50,50));
        test.setHitBoxAnimation();
        test.setRotation(0);
        test.setRotationSpeed(-30);
        test.setMoveSpeed(100);
        addEntity(test);

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Iterator<Entity> it = entityList.iterator();
        while (it.hasNext())
        {
            Entity aux = it.next();
            aux.update(delta);
        }


        renderSprites(delta);
        renderDebug(delta);
    }


    private void renderSprites(float delta) {
        Globals.batch.setProjectionMatrix(gameCamera.combined);
        Globals.batch.begin();
        for (Entity aux:entityList)
        {
            aux.draw(Globals.batch);
        }
        Globals.batch.end();
    }
    private void renderDebug(float delta) {
        debugRenderer.setProjectionMatrix(gameCamera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity aux:entityList)
        {
            aux.drawDebug(debugRenderer);
        }
        debugRenderer.end();
    }


    public void addEntity(Entity e)
    {
        entityList.add(e);
    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
