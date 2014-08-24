package org.castelodelego.ld30.Gameplay;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.GPSRandom;
import org.castelodelego.ld30.Globals;
import org.castelodelego.ld30.LD30Game;

import javax.swing.text.Position;
import java.util.Iterator;

/**
 * Created by caranha on 8/23/14.
 */
public class GameScreen implements com.badlogic.gdx.Screen {

    enum GameState { PLAYING, ENDING }
    GameState gameState;
    float playTime;
    float deathTime;

    Array<Entity> entityList;
    CollisionManager collisionManager = new CollisionManager();
    BitmapFont joystixMedium = Globals.assetManager.get("Joystix20.ttf",BitmapFont.class);

    PositionNavigator playerControl;
    Entity player;
    GPSRandom gpsDice;

    OrthographicCamera uiCamera;
    OrthographicCamera gameCamera;

    float worldViewWidth = 480;
    float worldViewHeight = 800;


    public GameScreen(GPSRandom creator)
    {
        deathTime = 0;
        playTime = 0;
        gameState = GameState.PLAYING;
        gpsDice = creator;


        entityList = new Array<Entity>(false,20);
        gameCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false,worldViewWidth,worldViewHeight);
        uiCamera.setToOrtho(false,worldViewWidth,worldViewHeight);

        setupPlayer();
        setupEnemies();

    }

    void setupPlayer()
    {
        player = new Entity();
        player.setAnimation(Globals.animationManager.get("sprites/player"));
        player.setPosition(new Vector2(0, 0));
        player.setHitBoxAnimation();
        player.setRotation(0);
        player.setRotationSpeed(120);
        player.setMoveSpeed(120);
        player.setColor(Color.WHITE);
        player.setMaxLife(60);
        playerControl = new PositionNavigator();
        player.setNavigator(playerControl);
        player.setCollisionType(Entity.CollisionType.PLAYER);
        addEntity(player);
    }

    void setupEnemies()
    {
        for (int i = 0; i < 1; i++)
        {
            Entity test2 = new Entity();
            test2.setAnimation(Globals.animationManager.get("sprites/spearer"));
            test2.setPosition(new Vector2(player.getPosition().x+gpsDice.nextInt(500)-250, player.getPosition().y+gpsDice.nextInt(500)-250));
            test2.setHitBoxAnimation();
            test2.setRotationSpeed(100);
            test2.setMoveSpeed(100);
            test2.setColor(Color.BLUE);
            test2.setMaxLife(30);
            TargetNavigator playerFollow = new TargetNavigator();
            playerFollow.setTarget(player);
            test2.setNavigator(playerFollow);
            test2.setCollisionType(Entity.CollisionType.ENEMY);
            addEntity(test2);
        }

        for (int i = 0; i < 4; i++)
        {
            Entity test2 = new Entity();
            test2.setAnimation(Globals.animationManager.get("props/wall_1x2"));
            test2.setPosition(new Vector2(player.getPosition().x + gpsDice.nextInt(500) - 250, player.getPosition().y + gpsDice.nextInt(500) - 250));
            test2.setHitBoxAnimation();
            test2.setColor(Color.GREEN);
            test2.setMaxLife(8000);
            test2.setRotation(90);
            test2.setCollisionType(Entity.CollisionType.WALL);
            addEntity(test2);
        }

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        playTime = playTime+delta;

        if (Gdx.input.isTouched())
        {
            Vector3 touchPos = gameCamera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
            playerControl.setTarget(new Vector2(touchPos.x,touchPos.y));
        }

        switch (gameState)
        {
            case PLAYING:
                if (player.getDestroyed()) {
                    deathTime = playTime;
                    gameState = GameState.ENDING;
                }
                break;
            case ENDING:
                if (playTime - deathTime > 1) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(LD30Game.getMainScreen());
                }

                break;
        }

        processEntities(delta);
        renderText(delta);
        renderSprites(delta);
        renderDebug(delta);

    }

    private void processEntities(float delta) {
        Array<Entity> newEntities = new Array<Entity>();

        collisionManager.doCollisions();

        for (Entity aux:entityList) {
            aux.update(delta);
            Array<Entity> children = aux.getChildArray();
            if (children != null) {
                for (Entity aux2 : children)
                    newEntities.add(aux2);
                children.clear();
            }
        }

        Iterator<Entity> it = entityList.iterator();
        while (it.hasNext())
        {
            Entity aux = it.next();
            if (aux.getDestroyed())
            {
                collisionManager.removeEntity(aux);
                aux.dispose();
                it.remove();
            }
        }

        for (Entity aux:newEntities)
            addEntity(aux);

        gameCamera.translate(player.position.x - gameCamera.position.x, player.position.y - gameCamera.position.y);
        gameCamera.update();
    }


    private void renderText(float delta) {
        Globals.batch.setProjectionMatrix(uiCamera.combined);
        Globals.batch.begin();
            if (player.getDestroyed())
                joystixMedium.draw(Globals.batch,"You're Dead",100,uiCamera.viewportHeight/4);
        Globals.batch.end();
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
        Globals.debugRenderer.setProjectionMatrix(gameCamera.combined);
        Globals.debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity aux:entityList)
        {
            aux.drawDebug(Globals.debugRenderer);
        }
        Globals.debugRenderer.end();
    }


    public void addEntity(Entity e)
    {
        entityList.add(e);
        collisionManager.addEntity(e);
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
