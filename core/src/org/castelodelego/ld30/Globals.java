package org.castelodelego.ld30;

import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


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

	public static Random behaviorDice;
    public static GPSRandom creationDice;
		
	public static BitmapFont debugText;
    public static LogOverlay log;
	
	
	static void init()
	{
		debugText = new BitmapFont();
		batch = new SpriteBatch();
		animationManager = new AnimationManager();
		assetManager = new AssetManager();
		
		behaviorDice = new Random();
        creationDice = new GPSRandom();
        double[] pos = LD30Game.gps.getLocation();
        creationDice.reset(pos[0],pos[1]);

        log = new LogOverlay();
	}		
}
