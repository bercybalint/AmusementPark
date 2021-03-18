package com.mygdx.amusementpark;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
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
    Texture road_texture;
    Texture actual;
    Texture choosen; //ezt állítjuk be a gomb lenyomásával.
    Boolean isScelected=false; //ez mondja meg, hogy van-e vmi kiválsztva építésre.


    private final Array<Array<Buildable>> tiles = new Array<Array<Buildable>>();
    //Ebben tárolódnak

    Buildable tile;

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
        road_texture = new Texture(Gdx.files.internal("road.png"));



        for(int i = 0; i < y_size; i++)
        {
            Array<Buildable> asd = new Array<Buildable>();
            tiles.add(asd);
            for(int j = 0; j < x_size; j++)
            {
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
                    else
                    {
                        actual = fence_texture;
                    }

                }
                else if (i == 0|| i == x_size - 1 || j == y_size - 1)
                {
                    actual = wall_texture;
                }
                else
                {
                    actual = grass_texture;
                }
                tile = new Buildable((i)*tile_width,(j)*tile_height,tile_width,tile_height,actual,10);
                tiles.get(i).add(tile);
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


                choosen = road_texture;
                isScelected=true;
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


                choosen = grass_texture;
                isScelected=true;

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
                Buildable act = tiles.get(i).get(j);
                game.batch.draw(act.texture,act.x,act.y+100,act.width,act.height);


            }
        }
        game.batch.end();

        stage.act(delta);
        stage.draw();

        //user inputok Gdx.input.isKeyPressed(Keys.LEFT)

        if(Gdx.input.justTouched())
        {
            System.out.println(isScelected);
            System.out.println(choosen);
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            for (Array<Buildable> arr_buildable : tiles)
            {
                for(Buildable build : arr_buildable)
                {
                    if(build.contains(touch.x,touch.y-100))
                    {
                        if(isScelected==true)
                        {
                            build.texture = choosen;
                        }
                    }
                    else{
                        Gdx.app.setLogLevel(Application.LOG_DEBUG);
                        Gdx.app.debug("POSITION", "X touched: " + touch.x + " Y touched: " + touch.y);
                    }
                }
            }
        }

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
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void dispose()
    {

    }

}
