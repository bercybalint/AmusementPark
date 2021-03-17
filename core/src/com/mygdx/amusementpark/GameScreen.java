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
    private int window_height = 800;
    private int window_width = 1200;
    private int x_size=20;
    private int y_size=20;
    private int tile_width = (window_width)/x_size;
    private int tile_height = (window_height)/y_size;
    Texture wall_texture;
    Texture grass_texture;
    Texture gate_texture;
    Texture fence_texture;
    Texture actual;


    private Array<Array<Rectangle>> tiles = new Array<Array<Rectangle>>();
    //Ebben tárolódnak

    Rectangle tile;

    public GameScreen(final AmusementPark game)
    {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, window_width, window_height+100);
        wall_texture = new Texture(Gdx.files.internal("tile.png"));
        grass_texture = new Texture(Gdx.files.internal("grass.png"));
        gate_texture = new Texture(Gdx.files.internal("gate.png"));
        fence_texture = new Texture(Gdx.files.internal("fence.png"));


        for(int i = 0; i < y_size; i++)
        {
            Array<Rectangle> asd = new Array<Rectangle>();
            tiles.add(asd);
            for(int j = 0; j < x_size; j++)
            {
                tile = new Rectangle();
                tile.x=(i)*tile_width;
                tile.y=(j)*tile_height;
                tile.width=tile_width;
                tile.height=tile_height;
                tiles.get(i).add(tile);
                System.out.println(tile.width + " " + tile.height);
                System.out.println("x: "+tile.x + " y:" + tile.y);

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

        for(int i = 0; i < tiles.size; i++)
        {
            for(int j = 0; j< tiles.get(i).size; j++)
            {
                Rectangle act = tiles.get(i).get(j);
                if(j==1)
                {
                    if(i == 8 || i==12)
                    {
                        actual = gate_texture;
                    }
                    else if(i>8 && i<12)
                    {
                        actual = grass_texture;
                    }
                    else {
                        actual = fence_texture;
                    }

                }
                else if (i == 0|| i == tiles.size - 1 || j == tiles.get(i).size - 1)
                {
                    actual = wall_texture;
                }
                else
                {
                    actual = grass_texture;
                }
                game.batch.draw(actual,act.x,act.y+100,act.width,act.height);

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
