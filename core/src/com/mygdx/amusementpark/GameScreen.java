package com.mygdx.amusementpark;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import org.w3c.dom.css.Rect;

import java.awt.*;

public class GameScreen implements Screen
{
    final AmusementPark game;
    OrthographicCamera camera;
    private int window_height = 900;
    private int window_width = 1200;
    private int x_size=20;
    private int y_size=20;
    private int tile_width = window_width/x_size;
    private int tile_height = window_height/y_size;
    Texture tile_texture;

    private Array<Array<Rectangle>> tiles = new Array<Array<Rectangle>>(20);
    //Ebben tárolódnak

    Rectangle tile;

    public GameScreen(final AmusementPark game)
    {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, window_width, window_height);
        tile_texture = new Texture(Gdx.files.internal("tile.png"));

        for(int i = 0; i < y_size; i++)
        {
            Array<Rectangle> asd = new Array<Rectangle>();
            tiles.add(asd);
            for(int j = 0; j < x_size; j++)
            {
                tile = new Rectangle();
                tile.x=i*tile_width;
                tile.y=j*tile_height;
                tile.width=tile_width;
                tile_height=tile_height;
                tiles.get(i).add(tile);
            }
        }
    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        //draw dolgok

        for(Array<Rectangle> til : tiles)
        {
            for(Rectangle t : til)
            {
                game.batch.draw(tile_texture,t.x,t.y);
            }
        }


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
