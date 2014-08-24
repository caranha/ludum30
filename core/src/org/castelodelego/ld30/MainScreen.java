package org.castelodelego.ld30;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import org.castelodelego.ld30.Gameplay.GameScreen;


public class MainScreen implements Screen {

    OrthographicCamera menuCamera;
    Screen lastGameScreen;

    BitmapFont bigFont;
    BitmapFont mediumFont;
    BitmapFont smallFont;

    float timeout;

    float titleWidth;
    float locationWidth;

    Color darkRed = new Color(0.5f,0f,0f,1f);

    Rectangle startButton = new Rectangle(40,530,400,60);
    Rectangle shipButton = new Rectangle(40,302,400,60);
    Rectangle helpButton = new Rectangle(40,202,400,60);
    Rectangle aboutButton = new Rectangle(40,102,400,60);

    boolean readingState = false;
    String readingText;

    public MainScreen()
    {
        menuCamera = new OrthographicCamera();
        menuCamera.setToOrtho(false, 480,800);

        bigFont = Globals.assetManager.get("Joystix40.ttf",BitmapFont.class);
        mediumFont = Globals.assetManager.get("Joystix20.ttf",BitmapFont.class);
        smallFont = Globals.assetManager.get("Joystix10.ttf",BitmapFont.class);

        titleWidth = bigFont.getBounds("GeoQuest").width;

    }


	public void render(float delta) {
        timeout += delta;

        basicRender();
        if (readingState)
            readingRender();


        if (Gdx.input.isTouched())
        {
            if (timeout < 0.5f) return;

            if (readingState) {
                readingState = false;
                timeout = 0;
                return;
            }

            Vector3 touchPos = menuCamera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));

            if (startButton.contains(touchPos.x,touchPos.y)) {
                lastGameScreen = new GameScreen(new GPSRandom(LD30Game.gps.getLocation()));
                ((Game) Gdx.app.getApplicationListener()).setScreen(lastGameScreen);
                return;
            }

            if (shipButton.contains(touchPos.x,touchPos.y)) {
                readingState = true;
                timeout = 0;
                readingText = "\nParts Listing:\n\n\n";
                if (LD30Context.getInstance().testKey(LD30Context.KEYS.RED))
                    readingText = readingText + " * The Red Key\n";
                if (LD30Context.getInstance().testKey(LD30Context.KEYS.GREEN))
                    readingText = readingText + " * The Green Key\n";
                if (LD30Context.getInstance().testKey(LD30Context.KEYS.BLUE))
                    readingText = readingText + " * The Blue Key\n";

                if (LD30Context.getInstance().getPlayer().hasWeapon)
                    readingText = readingText + " * Pea Shooter\n";
                if (LD30Context.getInstance().getPlayer().hasArmor)
                    readingText = readingText + " * Structural Armor\n";
                if (LD30Context.getInstance().getPlayer().hasSpeed)
                    readingText = readingText + " * Speed Booster\n";
                if (LD30Context.getInstance().getPlayer().hasTurn)
                    readingText = readingText + " * Turn Buster\n";
                return;
            }

            if (helpButton.contains(touchPos.x,touchPos.y))
            {
                readingState = true;
                timeout = 0;
                readingText = "\n\nHow to Play\n\nThe goal of this game is to get a high score by collecting diamonds." +
                        "\n\n When you touch the screen, your ship will move in that direction.\n\n* Avoid enemies\n" +
                        "* Get keys to open same color doors\n* Get power ups like guns or armor\n\n" +
                        "Remember, areas are created based on your GPS coordinates. Walk around " +
                        "to find areas with more loot, or less enemies.";
                return;
            }

            if (aboutButton.contains(touchPos.x,touchPos.y))
            {
                readingState = true;
                timeout = 0;
                readingText = "\nAbout the game\n\n\nThis game was made in 48 hours for the Ludum Dare game jam. " +
                        "All the code and assets were made from scratch. Feel free to modify and distribute.\n\n\n" +
                        "This game was made in Java using the LibGDX library. Graphics were made on GIMP, and sounds " +
                        "on bfxr and BoscaCeoil.\n\n\nLearn more at http://www.ludumdare.com/compo";
            }

        }

	}

    void basicRender()
    {

        Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Globals.debugRenderer.setProjectionMatrix(menuCamera.combined);
        Globals.debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Globals.debugRenderer.setColor(Color.LIGHT_GRAY);
        Globals.debugRenderer.rect(startButton.getX(),startButton.getY(),startButton.width,startButton.height);


        Globals.debugRenderer.rect(shipButton.getX(),shipButton.getY(),shipButton.width,shipButton.height);
        Globals.debugRenderer.setColor(Color.GRAY);
        Globals.debugRenderer.rect(helpButton.getX(),helpButton.getY(),helpButton.width,helpButton.height);
        Globals.debugRenderer.setColor(Color.DARK_GRAY);
        Globals.debugRenderer.rect(aboutButton.getX(),aboutButton.getY(),aboutButton.width,aboutButton.height);

        Globals.debugRenderer.setColor(Color.LIGHT_GRAY);
        Globals.debugRenderer.rect(20,430,190,10);
        Globals.debugRenderer.rect(270,430,190,10);
        Globals.debugRenderer.rect(40,530,400,60);

        Globals.debugRenderer.end();

        Globals.batch.setProjectionMatrix(menuCamera.combined);
        Globals.batch.begin();
        bigFont.setColor(Color.RED);
        bigFont.drawMultiLine(Globals.batch, "GeoQuest",240-titleWidth/2,menuCamera.viewportHeight-60);
        bigFont.setColor(darkRed);
        bigFont.drawMultiLine(Globals.batch, "GeoQuest",240-titleWidth/2+2,menuCamera.viewportHeight-62);

        mediumFont.setColor(Color.RED);
        mediumFont.drawMultiLine(Globals.batch, "Your Location:\n" + LD30Game.gps.getLocationString(),
                240, menuCamera.viewportHeight - 110, 0f, BitmapFont.HAlignment.CENTER);

        mediumFont.setColor(Color.RED);
        mediumFont.drawMultiLine(Globals.batch, "Touch here and\nconnect to this world",
                240, menuCamera.viewportHeight - 220, 0f, BitmapFont.HAlignment.CENTER);
        mediumFont.setColor(darkRed);
        mediumFont.drawMultiLine(Globals.batch, "Touch here and\nconnect to this world",
                242, menuCamera.viewportHeight - 222, 0f, BitmapFont.HAlignment.CENTER);

        mediumFont.setColor(Color.RED);
        mediumFont.drawMultiLine(Globals.batch, "Best Score: "+LD30Context.getInstance().getMaxScore(),
                240, menuCamera.viewportHeight - 300, 0f, BitmapFont.HAlignment.CENTER);

        Globals.batch.setColor(Color.WHITE);
        Globals.batch.draw(LD30Context.getInstance().getPlayer().getAnimation().getKeyFrame(0),
                225,420,0,0,30,30,1,1,0);


        smallFont.setColor(1f, 0.3f, 0.3f, 1f);
        smallFont.drawMultiLine(Globals.batch, "A game by caranha, for Ludum Dare #30",
                470, 10, 0f, BitmapFont.HAlignment.RIGHT);

        mediumFont.setColor(Color.RED);
        mediumFont.drawMultiLine(Globals.batch, "Check your ship's parts",
                240, menuCamera.viewportHeight - 460, 0f, BitmapFont.HAlignment.CENTER);
        mediumFont.setColor(darkRed);
        mediumFont.drawMultiLine(Globals.batch, "Check your ship's parts",
                242, menuCamera.viewportHeight - 462, 0f, BitmapFont.HAlignment.CENTER);

        mediumFont.setColor(Color.RED);
        mediumFont.drawMultiLine(Globals.batch, "How to Play",
                240, menuCamera.viewportHeight - 560, 0f, BitmapFont.HAlignment.CENTER);
        mediumFont.setColor(darkRed);
        mediumFont.drawMultiLine(Globals.batch, "How to Play",
                242, menuCamera.viewportHeight - 562, 0f, BitmapFont.HAlignment.CENTER);

        mediumFont.setColor(Color.RED);
        mediumFont.drawMultiLine(Globals.batch, "About this Game",
                240, menuCamera.viewportHeight - 660, 0f, BitmapFont.HAlignment.CENTER);
        mediumFont.setColor(darkRed);
        mediumFont.drawMultiLine(Globals.batch, "About this Game",
                242, menuCamera.viewportHeight - 662, 0f, BitmapFont.HAlignment.CENTER);

        Globals.batch.end();

    }

    void readingRender()
    {
        Globals.debugRenderer.setProjectionMatrix(menuCamera.combined);
        Globals.debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Globals.debugRenderer.setColor(Color.RED);
        Globals.debugRenderer.rect(50,50,380,700);
        Globals.debugRenderer.setColor(Color.BLACK);
        Globals.debugRenderer.rect(54,54,372,692);
        Globals.debugRenderer.end();


        Globals.batch.setProjectionMatrix(menuCamera.combined);
        Globals.batch.begin();
        mediumFont.drawWrapped(Globals.batch,readingText,58,730,368f, BitmapFont.HAlignment.CENTER);
        Globals.batch.end();

    }

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {
        timeout = 0;
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

}
