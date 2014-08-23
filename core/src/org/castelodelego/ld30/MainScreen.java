package org.castelodelego.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class MainScreen implements Screen {

    ShapeRenderer debugrender = new ShapeRenderer();



	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugrender.setProjectionMatrix(LD30Game.globalcam.combined);
        debugrender.begin(ShapeRenderer.ShapeType.Filled);
        debugrender.setColor(Color.BLACK);
        debugrender.rect(10, 10, 50, 50);
        debugrender.rect(480-60,800-60,50,50);
        debugrender.end();

        double[] location = LD30Game.gps.getLocation();
        Globals.log.addMessage("Longitute","lon: "+location[0]);
        Globals.log.addMessage("Latitude","lat:"+location[1]);
        Globals.log.addMessage("Provider",LD30Game.gps.toString());

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
		// TODO Auto-generated method stub

	}

}
