package org.castelodelego.ld30;

import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Contains all the statically accessible global variables.
 * 
 * @author caranha
 *
 */
public class Globals {
		
	public static AssetManager assetManager;
	public static AnimationManager animationManager;

    public static SpriteBatch batch;
    public static ShapeRenderer debugRenderer;


	public static Random behaviorDice;
    public static LogOverlay log;

    public static Music song;

    public static void playTitle()
    {
        if (song != null)
            song.stop();
        song = assetManager.get("sounds/title.ogg",Music.class);
        song.play();
    }

    public static void playGame1()
    {
        if (song != null)
            song.stop();
        song = assetManager.get("sounds/Game1.ogg",Music.class);
        song.play();
    }

    public static void playGame2()
    {
        if (song != null)
            song.stop();
        song = assetManager.get("sounds/Game2.ogg",Music.class);
        song.play();
    }

	
	static void init()
	{
		animationManager = new AnimationManager();
		assetManager = new AssetManager();

        batch = new SpriteBatch();
        debugRenderer = new ShapeRenderer();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		behaviorDice = new Random();
        double[] pos = LD30Game.gps.getLocation();

        log = new LogOverlay();

        LD30Context.getInstance();
	}		
}
