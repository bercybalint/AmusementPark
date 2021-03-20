package com.mygdx.amusementpark;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen
{
    final AmusementPark game;
    OrthographicCamera camera;
    private final Stage stage;
    private final int window_height = 800;
    private final int window_width = 1200;
    private final int x_size=20;
    private final int y_size=20;
    private final int tile_width = (window_width)/x_size;
    private final int tile_height = (window_height)/y_size;

    private TextButton buildButton;
    private TextButton parkButton;
    private TextButton gamesButton;
    private TextButton plantsButton;
    private TextButton roadButton;
    private ImageButton rButton;
    private TextButton staffButton;
    private TextButton guestButton;
    private TextButton closeButton;
    private final int buttonWidth = 150;
    private final int buttonHeight = 50;


    Texture wall_texture;
    Texture grass_texture;
    Texture gate_texture;
    Texture fence_texture;
    Texture road_texture;
    TextureRegion textureRegion;
    TextureRegionDrawable textureRegionDrawable;
    Texture actual;
    Texture chosen; //ezt állítjuk be a gomb lenyomásával.
    Boolean isSelected=false; //ez mondja meg, hogy van-e vmi kiválsztva építésre.


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
        textureRegion = new TextureRegion(road_texture);
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);



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
        buildButton.setPosition(10, 40);
        buildButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                parkButton.setVisible(true);
                staffButton.setVisible(true);
                staffButton.setPosition(buttonWidth*2 + 30, 40);
                guestButton.setVisible(true);
                guestButton.setPosition(buttonWidth*3 + 40,40);
                roadButton.setVisible(false);
                rButton.setVisible(false);
                closeButton.setVisible(true);

                if (isSelected)
                    isSelected = false;
            }
        });

        closeButton = new TextButton("Close menu", skin);
        closeButton.setSize(buttonWidth, buttonHeight);
        closeButton.setPosition(1000, 40);
        closeButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                closeButton.setVisible(false);
                //ide minden gombot be kell majd írni kivéve a buildet

                parkButton.setVisible(false);
                gamesButton.setVisible(false);
                plantsButton.setVisible(false);
                roadButton.setVisible(false);
                rButton.setVisible(false);
                staffButton.setVisible(false);
                guestButton.setVisible(false);

                if (isSelected)
                    isSelected = false;

            }

        });


        parkButton = new TextButton("Park", skin);
        parkButton.setSize(buttonWidth, buttonHeight);
        parkButton.setPosition(buttonWidth + 20, 40);
        parkButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                guestButton.setVisible(false);
                staffButton.setVisible(false);

                gamesButton.setVisible(true);
                gamesButton.setPosition(buttonWidth*2 + 30,40);
                plantsButton.setVisible(true);
                plantsButton.setPosition(buttonWidth*3 + 40, 40);
                roadButton.setVisible(true);
                roadButton.setPosition(buttonWidth*4 + 50, 40);
                rButton.setVisible(false);


                chosen = grass_texture;
                isSelected=true;

            }
        });

        gamesButton = new TextButton("Games", skin);
        gamesButton.setSize(buttonWidth, buttonHeight);
        gamesButton.setPosition(buttonWidth*2 + 30, 40);
        gamesButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                plantsButton.setVisible(false);
                roadButton.setVisible(false);

                if (isSelected)
                    isSelected = false;
            }
        });

        plantsButton = new TextButton("Plants", skin);
        plantsButton.setSize(buttonWidth, buttonHeight);
        plantsButton.setPosition(buttonWidth * 3 + 40, 40);
        plantsButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                plantsButton.setPosition(buttonWidth*2 + 30, 40);
                gamesButton.setVisible(false);
                roadButton.setVisible(false);

                if (isSelected)
                    isSelected = false;
            }
        });

        roadButton = new TextButton("Road", skin);
        roadButton.setSize(buttonWidth, buttonHeight);
        roadButton.setPosition(buttonWidth*4 + 50, 40);
        roadButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                roadButton.setPosition(buttonWidth*2 + 30, 40);
                staffButton.setVisible(false);
                gamesButton.setVisible(false);
                plantsButton.setVisible(false);

                rButton.setVisible(true);
                rButton.setPosition(buttonWidth * 3 + 50,40);

                if (isSelected)
                    isSelected = false;

            }
        });

        rButton = new ImageButton(textureRegionDrawable);
        rButton.setSize(50,50);
        rButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = road_texture;
                isSelected=true;
            }
        });

        staffButton = new TextButton("Staff", skin);
        staffButton.setSize(buttonWidth, buttonHeight);
        staffButton.setPosition(buttonWidth* 2 +30, 40);
        staffButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                parkButton.setVisible(false);
                guestButton.setVisible(false);
                staffButton.setPosition(buttonWidth + 20, 40);

                if (isSelected)
                    isSelected = false;
            }
        });

        guestButton = new TextButton("Guest", skin);
        guestButton.setSize(buttonWidth, buttonHeight);
        guestButton.setPosition(buttonWidth*3 + 40, 40);
        guestButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                parkButton.setVisible(false);
                staffButton.setVisible(false);

                guestButton.setPosition(buttonWidth + 20, 40);

                if (isSelected)
                    isSelected = false;
            }
        });


        stage.addActor(buildButton);
        stage.addActor(parkButton);
        stage.addActor(staffButton);
        stage.addActor(guestButton);
        stage.addActor(gamesButton);
        stage.addActor(plantsButton);
        stage.addActor(roadButton);
        stage.addActor(rButton);
        stage.addActor(closeButton);
        parkButton.setVisible(false);
        staffButton.setVisible(false);
        guestButton.setVisible(false);
        gamesButton.setVisible(false);
        plantsButton.setVisible(false);
        roadButton.setVisible(false);
        rButton.setVisible(false);
        closeButton.setVisible(false);

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
            System.out.println(isSelected);
            System.out.println(chosen);
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            for (Array<Buildable> arr_buildable : tiles)
            {
                for(Buildable build : arr_buildable)
                {
                    if(build.contains(touch.x,touch.y-100))
                    {
                        if(isSelected==true)
                        {
                            build.texture = chosen;
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
