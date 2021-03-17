package com.mygdx.amusementpark;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class GameScreen implements Screen
{
    final AmusementPark game;
    OrthographicCamera camera;
    private Stage stage;
    private final int window_height = 800;
    private final int window_width = 1200;
    private final int x_size=20;
    private final int y_size=20;
    private final int tile_width = (window_width)/x_size;
    private final int tile_height = (window_height)/y_size;

    private TextButton buildButton;
    private TextButton parkButton;
    private TextButton staffButton;
    private TextButton guestButton;
    private TextButton RoadsButton;
    private int buttonWidth = 150;
    private int buttonHeight = 50;

    Texture wall_texture;
    Texture grass_texture;
    Texture gate_texture;
    Texture fence_texture;
    Texture actual;


    private final Array<Array<Rectangle>> tiles = new Array<Array<Rectangle>>();
    //Ebben tárolódnak

    Rectangle tile;

    public GameScreen(final AmusementPark game)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

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

        buildButton = new TextButton("Build", skin);
        buildButton.setSize(buttonWidth, buttonHeight);
        buildButton.setPosition(10, 48);
        buildButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                stage.addActor(parkButton);
                stage.addActor(staffButton);
                staffButton.setVisible(true);
                stage.addActor(guestButton);
            }
        });

        parkButton = new TextButton("Park", skin);
        parkButton.setSize(buttonWidth, buttonHeight);
        parkButton.setPosition(160, 48);
        parkButton.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int point, int button) {
                guestButton.setVisible(false);
                staffButton.setVisible(false);
            }
        });

        staffButton = new TextButton("Staff", skin);
        staffButton.setSize(buttonWidth, buttonHeight);
        staffButton.setPosition(320, 48);

        guestButton = new TextButton("Guest", skin);
        guestButton.setSize(buttonWidth, buttonHeight);
        guestButton.setPosition(480, 48);


        stage.addActor(buildButton);

    }

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(.135f, .206f, .235f, 1);

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

        stage.act(delta);
        stage.draw();

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
