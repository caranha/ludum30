package org.castelodelego.ld30;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.castelodelego.ld30.Gameplay.GameScreen;


public class MainScreen implements Screen {

    OrthographicCamera menuCamera;
    Screen lastGameScreen;

    BitmapFont menuFont;
    BitmapFont buttonFont;

    float timeout;

    public MainScreen()
    {
        menuCamera = new OrthographicCamera();
        menuCamera.setToOrtho(false, 480,800);

        menuFont = Globals.assetManager.get("Joystix40.ttf",BitmapFont.class);
        buttonFont = Globals.assetManager.get("Joystix20.ttf",BitmapFont.class);

    }





	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeout += delta;

        Globals.batch.begin();
        menuFont.setColor(Color.RED);
        menuFont.draw(Globals.batch, "GeoQuest", 100, menuCamera.viewportHeight-100);
        buttonFont.draw(Globals.batch, "Click here to Start", 100, menuCamera.viewportHeight-200);
        buttonFont.setColor(Color.RED);
        buttonFont.draw(Globals.batch, LD30Game.gps.getLocationString(), 10, 30);
        Globals.batch.end();



        // TODO: Main screen should take the player to: about, scores (achievements), embark here!
        // TODO: Main screen should report the coordinates
        if (timeout > 0.2 && Gdx.input.isTouched())
        {
            lastGameScreen = new GameScreen(new GPSRandom(LD30Game.gps.getLocation()));
            ((Game) Gdx.app.getApplicationListener()).setScreen(lastGameScreen);
        }

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
