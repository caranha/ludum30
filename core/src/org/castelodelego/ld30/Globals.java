package org.castelodelego.ld30;

import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;


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
    static OrthographicCamera globalcam;

	public static Random behaviorDice;

	public static BitmapFont debugText;
    public static LogOverlay log;
	
	
	static void init()
	{
		debugText = new BitmapFont();
		batch = new SpriteBatch();
		animationManager = new AnimationManager();
		assetManager = new AssetManager();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		behaviorDice = new Random();
        double[] pos = LD30Game.gps.getLocation();

        log = new LogOverlay();
	}		
}
