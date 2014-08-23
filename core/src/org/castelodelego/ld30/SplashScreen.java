package org.castelodelego.ld30;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SplashScreen implements Screen {
	
	public int ID;

	// Variables for drawing the splash screen
	OrthographicCamera camera;
	ShapeRenderer lineDrawer;
	Texture splashImg;
	float time;
	float fade;

    enum Stage { LOAD_ASSETS, LOAD_ANIMATION, LOAD_SCREENS, FADE_OUT, DONE};
    Stage currentStage;

	public SplashScreen()
	{
        currentStage = Stage.LOAD_ASSETS;
		lineDrawer = new ShapeRenderer();
		
		// Loads the splash image (Chooses between the horizontal one and the vertical one)
		if (Gdx.app.getGraphics().getHeight() > Gdx.app.getGraphics().getWidth())
		{
			splashImg = new Texture(Gdx.files.internal("images/backgrounds/splash_vert.png"));
		}
		else
		{
			splashImg = new Texture(Gdx.files.internal("images/backgrounds/splash_hor.png"));
		}
		camera = new OrthographicCamera();
		camera.setToOrtho(false, splashImg.getWidth(), splashImg.getHeight());
		fade = 0;
	}
	
	
	@Override
	public void render(float delta) {
		
		// Clear the screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        time = time + delta;

        switch (currentStage)
        {

            case LOAD_ASSETS:
                fade = (fade >= 1?1:time*2);

                if (Globals.assetManager.update() && fade >= 1)
                    currentStage = Stage.LOAD_ANIMATION;

                break;
            case LOAD_ANIMATION:
                if (Globals.animationManager.loadAnimations(Globals.assetManager.get("images/pack.atlas", TextureAtlas.class),delta))
                    currentStage = Stage.LOAD_SCREENS;
                break;
            case LOAD_SCREENS:
                    LD30Game.loadScreens();
                    if (time > 1.5)
                        currentStage = Stage.FADE_OUT;
                break;
            case FADE_OUT:
                    fade = fade - delta*3;
                    if (fade <= 0)
                        {
                            fade = 0;
                            currentStage = Stage.DONE;
                        }
                break;
            case DONE:
                    ((Game) Gdx.app.getApplicationListener()).setScreen(LD30Game.mainScreen);
                break;
        }

		Globals.batch.setProjectionMatrix(camera.combined);
		Globals.batch.begin();
		Globals.batch.draw(splashImg, 0,0);
		Globals.batch.end();

		if (fade < 1.0f)
		{
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			lineDrawer.setProjectionMatrix(camera.combined);
			lineDrawer.begin(ShapeType.Filled);
			lineDrawer.setColor(1f, 1f, 1f, 1-fade);
			lineDrawer.rect(0, 0, splashImg.getWidth(), splashImg.getHeight());
			lineDrawer.end();
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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
		splashImg.dispose();
	}

}
