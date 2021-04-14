package com.mygdx.amusementpark.gui;

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
import com.mygdx.amusementpark.buildable.*;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.TileBasedMap;

public class GameScreen implements Screen, TileBasedMap
{

    /**
     * Ablak, tile-ok méreteineek beállítása
     * Camera, felület létrehozása.
     */
    final AmusementPark game;
    OrthographicCamera camera;
    private final Stage stage;
    private final int window_height = 800;
    private final int window_width = 1200;
    private final int x_size=20;
    private final int y_size=20;
    final int tile_width = (window_width)/x_size;
    final int tile_height = (window_height)/y_size;

    /**
     * Gombok textúrái, méreteinek beállítása.
     */
    private TextButton buildButton;
    private TextButton parkButton;
    private TextButton gamesButton;
    private ImageButton korhintaButton;
    private TextButton plantsButton;
    private ImageButton bushButton;
    private TextButton roadButton;
    private ImageButton rButton;
    private TextButton staffButton;
    private  ImageButton sButton;
    private TextButton guestButton;
    private ImageButton trashButton;
    private ImageButton hamburgerButton;
    private ImageButton waterButton;
    private TextButton closeButton;
    private final int buttonWidth = 150;
    private  int buttonHeight = 50;

    /**
     * Alap elemek textúrái. d
     */
    Texture wall_texture;
    Texture grass_texture;
    Texture gate_texture;
    Texture fence_texture;
    Texture korhinta_texture;
    Texture staff_texture;
    Texture bush_texture;
    Texture water_texture;
    Texture hamburger_texture;
    Texture trash_texture;


    /**
     * d Különböző út textúrák beállítása, szomszédoktól függően.
     */
    Texture road_down_to_up;
    Texture road_down_to_left;
    Texture road_down_to_right;
    Texture road_left_to_right;
    Texture road_top_to_right;
    Texture road_top_to_left;
    Texture road_threeway_to_up;
    Texture road_threeway_to_down;
    Texture road_threeway_to_right;
    Texture road_threeway_to_left;
    Texture road_from_all;

    TextureRegion textureRegionRoad;
    TextureRegionDrawable textureRegionDrawableRoad;
    TextureRegion textureRegionGameKorhinta;
    TextureRegionDrawable textureRegionDrawableGameKorhinta;
    TextureRegion textureRegionPlantBush;
    TextureRegionDrawable textureRegionDrawablePlantBush;
    TextureRegion textureRegionStaff;
    TextureRegionDrawable textureRegionDrawableStaff;
    TextureRegion textureRegionTrash;
    TextureRegionDrawable textureRegionDrawableTrash;
    TextureRegion textureRegionHamburger;
    TextureRegionDrawable textureRegionDrawableHamburger;
    TextureRegion textureRegionWater;
    TextureRegionDrawable textureRegionDrawableWater;

    /**
     * chosen - a kiválaszott textúrát ebben tárojuk, és ezzel rakjuk le.
     */
    Tiles chosen; //ezt állítjuk be a gomb lenyomásával.
    Boolean isSelected=false; //ez mondja meg, hogy van-e vmi kiválsztva építésre.
    Skin skin;


    private GameMap map = new GameMap();


    public GameScreen(final AmusementPark game)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, window_width, window_height+100);

        texturesInit();
        buttonManagment();
    }

    /**
     * minden kirajzolása
     * egy batchen belül.
     * inputok kezelése
     */

    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(.181f, .230f, .29f, 0.5f);

        //Gdx.gl.glClearColor(0, 0, 0, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(grass_texture,0,0,window_width,window_height);
        //draw dolgok

        for(int i = 0; i < map.units.size; i++)
        {
            for(int j = 0; j< map.units.get(i).size; j++)
            {
                Buildable act = map.units.get(i).get(j);
                game.batch.draw(act.getTexture(),act.x,act.y+100,act.width,act.height);
            }
        }
        game.batch.end();

        stage.act(delta);
        stage.draw();


        /**
         * Inputok kezelése
         */
        if(Gdx.input.justTouched())
        {
            Vector3 touch = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            map.touched(touch,chosen,isSelected);
            //föggvény hívás (touch, chosen, isSelected)
        }
    }

    /**
     * gombok létrehozáse, kezelése, eventListenerek beállítása.
     */
    public void buttonManagment()
    {
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
                gamesButton.setVisible(false);
                plantsButton.setVisible(false);
                closeButton.setVisible(true);
                korhintaButton.setVisible(false);
                bushButton.setVisible(false);
                sButton.setVisible(false);
                trashButton.setVisible(false);
                hamburgerButton.setVisible(false);
                waterButton.setVisible(false);

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
                korhintaButton.setVisible(false);
                bushButton.setVisible(false);
                sButton.setVisible(false);
                trashButton.setVisible(false);
                hamburgerButton.setVisible(false);
                waterButton.setVisible(false);

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
                bushButton.setVisible(false);
                sButton.setVisible(false);

                //chosen = Tiles.EMPTY;
                if (isSelected)
                    isSelected = false;

            }
        });

        gamesButton = new TextButton("Games", skin);
        gamesButton.setSize(buttonWidth, buttonHeight);
        gamesButton.setPosition(buttonWidth*2 + 30, 40);
        gamesButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                plantsButton.setVisible(false);
                roadButton.setVisible(false);

                korhintaButton.setVisible(true);
                korhintaButton.setPosition(buttonWidth * 3 + 50,40);

                if (isSelected)
                    isSelected = false;
            }
        });

        korhintaButton = new ImageButton(textureRegionDrawableGameKorhinta);
        korhintaButton.setSize(50,50);
        korhintaButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.GAMES;
                isSelected=true;
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

                bushButton.setVisible(true);
                bushButton.setPosition(buttonWidth*3 + 50, 40);

                if (isSelected)
                    isSelected = false;
            }
        });

        bushButton = new ImageButton(textureRegionDrawablePlantBush);
        bushButton.setSize(50,50);
        bushButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.BUSH;
                isSelected=true;
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
                korhintaButton.setVisible(false);

                rButton.setVisible(true);
                rButton.setPosition(buttonWidth * 3 + 50,40);

                if (isSelected)
                    isSelected = false;

            }
        });

        rButton = new ImageButton(textureRegionDrawableRoad);
        rButton.setSize(50,50);
        rButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.ROAD;
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

                sButton.setVisible(true);
                sButton.setPosition(buttonWidth*2 + 50,40);

                if (isSelected)
                    isSelected = false;
            }
        });

        sButton = new ImageButton(textureRegionDrawableStaff);
        sButton.setSize(50,50);
        sButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.STAFF;
                isSelected=true;
            }
        });

        guestButton = new TextButton("Guest", skin);
        guestButton.setSize(buttonWidth, buttonHeight);
        guestButton.setPosition(buttonWidth*3 + 40, 40);
        guestButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                guestButton.setPosition(buttonWidth + 20, 40);
                parkButton.setVisible(false);
                staffButton.setVisible(false);
                trashButton.setVisible(true);
                trashButton.setPosition(buttonWidth*2 + 50, 40);
                waterButton.setVisible(true);
                waterButton.setPosition(buttonWidth*2 + 110, 40);
                hamburgerButton.setVisible(true);
                hamburgerButton.setPosition(buttonWidth*2 + 170, 40);



                if (isSelected)
                    isSelected = false;
            }
        });

        trashButton = new ImageButton(textureRegionDrawableTrash);
        trashButton.setSize(50,50);
        trashButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.TRASH;
                isSelected=true;
            }
        });

        waterButton = new ImageButton(textureRegionDrawableWater);
        waterButton.setSize(50,50);
        waterButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.WATER;
                isSelected=true;
            }
        });

        hamburgerButton = new ImageButton(textureRegionDrawableHamburger);
        hamburgerButton.setSize(50,50);
        hamburgerButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.FOOD;
                isSelected=true;
            }
        });


        stage.addActor(buildButton);
        stage.addActor(parkButton);
        stage.addActor(staffButton);
        stage.addActor(guestButton);
        stage.addActor(korhintaButton);
        stage.addActor(gamesButton);
        stage.addActor(plantsButton);
        stage.addActor(roadButton);
        stage.addActor(rButton);
        stage.addActor(closeButton);
        stage.addActor(bushButton);
        stage.addActor(sButton);
        stage.addActor(trashButton);
        stage.addActor(hamburgerButton);
        stage.addActor(waterButton);


        parkButton.setVisible(false);
        staffButton.setVisible(false);
        guestButton.setVisible(false);
        gamesButton.setVisible(false);
        plantsButton.setVisible(false);
        roadButton.setVisible(false);
        rButton.setVisible(false);
        closeButton.setVisible(false);
        korhintaButton.setVisible(false);
        bushButton.setVisible(false);
        sButton.setVisible(false);
        trashButton.setVisible(false);
        hamburgerButton.setVisible(false);
        waterButton.setVisible(false);
    }

    /**
     * terxtúrák beállítása.
     */
    public void texturesInit()
    { //d
        wall_texture = new Texture(Gdx.files.internal("tile.png"));
        grass_texture = new Texture(Gdx.files.internal("grass.png"));
        gate_texture = new Texture(Gdx.files.internal("gate.png"));
        fence_texture = new Texture(Gdx.files.internal("fence.png"));

        road_down_to_up = new Texture(Gdx.files.internal("road_down.png"));
        road_down_to_left = new Texture(Gdx.files.internal("road_left_to_down.png"));
        road_down_to_right = new Texture(Gdx.files.internal("road_down_to_right.png"));
        road_left_to_right = new Texture(Gdx.files.internal("road_right.png"));
        road_top_to_right = new Texture(Gdx.files.internal("road_turn.png"));
        road_top_to_left = new Texture(Gdx.files.internal("road_down_to_left.png"));
        road_threeway_to_up = new Texture(Gdx.files.internal("road_three_up.png"));
        road_threeway_to_down = new Texture(Gdx.files.internal("road_three_down.png"));
        road_threeway_to_right = new Texture(Gdx.files.internal("road_three_right.png"));
        road_threeway_to_left = new Texture(Gdx.files.internal("road_three_left.png"));
        road_from_all = new Texture(Gdx.files.internal("road_inter.png"));


        korhinta_texture = new Texture(Gdx.files.internal("korhinta.png"));
        bush_texture = new Texture(Gdx.files.internal("bush.png"));
        hamburger_texture = new Texture(Gdx.files.internal("hamburger.png"));
        water_texture = new Texture(Gdx.files.internal("water.png"));
        trash_texture = new Texture(Gdx.files.internal("trashcan.png"));
        staff_texture = new Texture(Gdx.files.internal("staff.png"));

        //-d

        textureRegionRoad = new TextureRegion(road_down_to_up);
        textureRegionDrawableRoad = new TextureRegionDrawable(textureRegionRoad);
        textureRegionGameKorhinta = new TextureRegion(korhinta_texture);
        textureRegionDrawableGameKorhinta = new TextureRegionDrawable(textureRegionGameKorhinta);
        textureRegionPlantBush = new TextureRegion(bush_texture);
        textureRegionDrawablePlantBush = new TextureRegionDrawable(textureRegionPlantBush);
        textureRegionHamburger = new TextureRegion(hamburger_texture);
        textureRegionDrawableHamburger = new TextureRegionDrawable(textureRegionHamburger);
        textureRegionWater = new TextureRegion(water_texture);
        textureRegionDrawableWater = new TextureRegionDrawable(textureRegionWater);
        textureRegionTrash = new TextureRegion(trash_texture);
        textureRegionDrawableTrash = new TextureRegionDrawable(textureRegionTrash);
        textureRegionStaff = new TextureRegion(staff_texture);
        textureRegionDrawableStaff = new TextureRegionDrawable(textureRegionStaff);
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

    @Override
    public int getWidthInTiles()
    {
        return 0;
    }

    @Override
    public int getHeightInTiles()
    {
        return 0;
    }

    @Override
    public void pathFinderVisited(int x, int y)
    {

    }

    @Override
    public boolean blocked(Mover mover, int x, int y)
    {
        return false;
    }

    @Override
    public float getCost(Mover mover, int sx, int sy, int tx, int ty)
    {
        return 0;
    }
}
