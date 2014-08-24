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
import com.badlogic.gdx.utils.Array;
import org.castelodelego.ld30.GPSRandom;
import org.castelodelego.ld30.Gameplay.Factories.ManualRoomFactory;
import org.castelodelego.ld30.Gameplay.Factories.StageFactory;
import org.castelodelego.ld30.Globals;
import org.castelodelego.ld30.LD30Context;
import org.castelodelego.ld30.LD30Game;
import org.castelodelego.ld30.parallax.ParallaxBackground;
import org.castelodelego.ld30.parallax.ParallaxFactory;

import java.util.Iterator;

/**
 * Created by caranha on 8/23/14.
 */
public class GameScreen implements com.badlogic.gdx.Screen {

    static Color BorderWhite = new Color(1f,1f,1f,.7f);
    static Color BorderBlack = new Color(1f,1f,1f,0f);

    enum GameState { PLAYING, ENDING }
    GameState gameState;
    float playTime;
    float deathTime;

    Array<Entity> entityList;
    CollisionManager collisionManager = new CollisionManager();
    BitmapFont joystixMedium = Globals.assetManager.get("Joystix20.ttf",BitmapFont.class);

    PositionNavigator playerControl;
    EntityPlayer player;
    GPSRandom gpsDice;

    OrthographicCamera uiCamera;
    OrthographicCamera gameCamera;

    float worldViewWidth = 480;
    float worldViewHeight = 800;

    //ParallaxBackground stars;


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
        Array<Entity> props = StageFactory.createLevel(creator,player);
        for (Entity aux:props)
            addEntity(aux);

        //stars = ParallaxFactory.getDefaultBackground();

    }

    void setupPlayer()
    {
        player = new EntityPlayer(600,2400);
        player.setAnimation(Globals.animationManager.get("sprites/player"));
        player.setPosition(new Vector2(300,300));
        player.setHitBoxSize(25,25);
        player.setRotation(0);
        player.setRotationSpeed(120);
        player.setMoveSpeed(140);
        player.setColor(Color.WHITE);
        player.setMaxLife(60);
        player.setHitPoints(5); //DEBUG
        playerControl = new PositionNavigator();
        player.setNavigator(playerControl);
        player.setCollisionType(Entity.CollisionType.PLAYER);
        addEntity(player);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        playTime = playTime+delta;

        Globals.log.addMessage("Keys","Red: "+LD30Context.getInstance().testKey(LD30Context.KEYS.RED)+
                                      "  Green: "+LD30Context.getInstance().testKey(LD30Context.KEYS.GREEN)+
                                      "  Blue: "+LD30Context.getInstance().testKey(LD30Context.KEYS.BLUE));

        Globals.log.addMessage("Score", "Score: "+LD30Context.getInstance().getCurrentScore());

        if (Gdx.input.isTouched())
        {
            Vector3 touchPos = gameCamera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
            playerControl.setTarget(new Vector2(touchPos.x,touchPos.y));
        }

        switch (gameState)
        {
            case PLAYING:
                if (player.getDestroyed()||player.getEscaped()) {
                    collisionManager.removeEntity(player);
                    entityList.removeValue(player,true);
                    deathTime = playTime;
                    gameState = GameState.ENDING;

                    if (player.getEscaped())  {
                        LD30Context.getInstance().doEscape();
                    } else {
                        LD30Context.getInstance().doDeath();
                    }
                }
                break;
            case ENDING:
                if (playTime - deathTime > 1) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(LD30Game.getMainScreen());
                }

                break;
        }

        processEntities(delta);


        //stars.render(delta);
        renderText(delta);
        renderSprites(delta);
        //renderDebug(delta);
        renderBorders(delta);

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

        gameCamera.position.x = player.position.x;
        gameCamera.position.y = player.position.y;

        if (gameCamera.position.x < worldViewWidth/2 - 50) gameCamera.position.x = worldViewWidth/2 - 50;
        if (gameCamera.position.x > 650-worldViewWidth/2) gameCamera.position.x = 650-worldViewWidth/2;
        if (gameCamera.position.y < worldViewHeight/2 - 50) gameCamera.position.y = worldViewHeight/2 - 50;
        if (gameCamera.position.y > 2450-worldViewHeight/2) gameCamera.position.y = 2450-worldViewHeight/2;

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

    private void renderBorders(float delta) {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Globals.debugRenderer.setProjectionMatrix(gameCamera.combined);
        Globals.debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Globals.debugRenderer.rect(-50,-50,700,50,BorderWhite,BorderWhite,BorderBlack,BorderBlack);
        Globals.debugRenderer.rect(-50,2400,700,50,BorderBlack,BorderBlack,BorderWhite,BorderWhite);

        Globals.debugRenderer.rect(-50,-50,50,2500,BorderWhite,BorderBlack,BorderBlack,BorderWhite);
        Globals.debugRenderer.rect(600,-50,50,2500,BorderBlack,BorderWhite,BorderWhite,BorderBlack);

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
