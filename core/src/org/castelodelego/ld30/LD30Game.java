package org.castelodelego.ld30;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

public class LD30Game extends Game {

    static Screen splashScreen;
    static Screen mainScreen;
    static LocationServer gps;

    public LD30Game(LocationServer g)
    {
        super();
        gps = g;
    }


    @Override
    public void create() {

        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Globals.init(); // TODO: Send an instance of myself to "globals"

        // Creating global resource managers
        queueAssets();

        splashScreen = new SplashScreen();
        setScreen(splashScreen);
    }

    /**
     * Add all assets for loading here.
     *
     */
    private void queueAssets()
    {
        // Loading Images
        Globals.assetManager.load("images/pack.atlas", TextureAtlas.class); // packed images

        FreeTypeFontLoaderParameter fontParams;
        // Loading fonts and generating size variations

        fontParams = new FreeTypeFontLoaderParameter();
        fontParams.fontFileName = "fonts/Joystix.ttf";
        fontParams.fontParameters.size = 20;
        Globals.assetManager.load("Joystix20.ttf", BitmapFont.class, fontParams);

        fontParams = new FreeTypeFontLoaderParameter();
        fontParams.fontFileName = "fonts/Joystix.ttf";
        fontParams.fontParameters.size = 40;
        Globals.assetManager.load("Joystix40.ttf", BitmapFont.class, fontParams);

        fontParams = new FreeTypeFontLoaderParameter();
        fontParams.fontFileName = "fonts/Joystix.ttf";
        fontParams.fontParameters.size = 10;
        Globals.assetManager.load("Joystix10.ttf", BitmapFont.class, fontParams);

        Globals.assetManager.load("sounds/Coin.ogg", Sound.class);
        Globals.assetManager.load("sounds/Death_Enemy.ogg", Sound.class);
        Globals.assetManager.load("sounds/Death.ogg", Sound.class);
        Globals.assetManager.load("sounds/Laser.ogg", Sound.class);
        Globals.assetManager.load("sounds/Teleport.ogg", Sound.class);
        Globals.assetManager.load("sounds/Unlock.ogg", Sound.class);
    }

    public static void loadScreens()
    {
        mainScreen = new MainScreen();
    }

    public static Screen getMainScreen() {
        return mainScreen;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {

        super.render();

        // Rendering here renders above everything else
        // Good for rendering debug info

        // Uncomment for FPS
        Globals.log.addMessage("FPS", "FPS: "+Gdx.graphics.getFramesPerSecond());
        Globals.log.render();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}


