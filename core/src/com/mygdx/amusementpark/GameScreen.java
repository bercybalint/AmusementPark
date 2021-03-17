package com.mygdx.amusementpark;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen
{
    final AmusementPark game;
    OrthographicCamera camera;

    public GameScreen(final AmusementPark game)
    {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 900);
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        //draw dolgok

        game.batch.end();

        //user inputok Gdx.input.isKeyPressed(Keys.LEFT)

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
