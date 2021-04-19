package com.mygdx.amusementpark.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.amusementpark.buildable.*;
import com.mygdx.amusementpark.pathfinding.*;
import com.mygdx.amusementpark.people.Guest;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.*;
import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen implements Screen
{
    Timer timer = new Timer();
    Timer gametimer = new Timer();
    Boolean first_building_placed=false;
    private Array<Guest> guests = new Array<Guest>();



    /**
     * Ablak, tile-ok méreteineek beállítása
     * Camera, felület létrehozása.
     */
    final AmusementPark game;
    OrthographicCamera camera;
    private final Stage stage;
    private final int window_height = 800;
    private final int window_width = 1200;

    /**
     * Gombok textúrái, méreteinek beállítása.
     */
    private TextButton startButton;
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
    Texture trashcan_texture;
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

    Texture guest_texture;

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

    Texture happy_texture;
    Texture annoyed_texture;
    Texture angry_texture;

    /**
     * chosen - a kiválaszott textúrát ebben tárojuk, és ezzel rakjuk le.
     */
    Tiles chosen; //ezt állítjuk be a gomb lenyomásával.
    Boolean isSelected=false; //ez mondja meg, hogy van-e vmi kiválasztva építésre.
    Skin skin;

    /**
     * Különböző elemek árai.
     */
    private int money = 100000;
    private Label label;
    private Label timelabel;
    private TimerTask task;



    int gamePrice = 2000;
    int plantPrice = 1000;
    int buildingPrice = 1500;
    int trashcanPrice = 1000;
    int roadPrice = 100;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    private GameMap map;
    int ido = 0;
    public GameScreen(final AmusementPark game)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        BitmapFont labelFont = skin.get("default-font", BitmapFont.class);
        labelFont.getData().markupEnabled = true;
        label = new Label("[RED]Money:" + money + "$",skin);
        timelabel = new Label("",skin);
        stage.addActor(label);
        stage.addActor(timelabel);


        task = new TimerTask() {
            public void run() {
                String time = getTime(ido);
                timelabel.setText("[BLACK]Time: "+time);
                timelabel.setPosition(220,832);
                ido++;

            }
        };

        camera = new OrthographicCamera();
        camera.setToOrtho(false, window_width, window_height+100);
        map = new GameMap(window_height, window_width, camera);
        texturesInit();
        buttonManagment();
        moneyHeist(0);
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
        for(int i = 0; i < guests.size; i++)
        {
            Guest guest = guests.get(i);
            guest.move();
            guest.setMap(map);
            game.batch.draw(guest.getTexture(),guest.x+20,guest.y+110,guest.width,guest.height);
            for(int j = 0; j < map.destinationPoints.size; j++)
            {
                if(guest.intersects(map.destinationPoints.get(j)))
                {
                    if(!guest.isGoing && !guest.isWaiting)
                    {
                        guest.isWaiting=true;
                        System.out.println("megjottem");
                        guest.reachedDestination(map.destinationPoints.get(j).timeToUse);
                        guest.gainMood(map.destinationPoints.get(j).moodGain);
                    }
                }
            }
            if(guest.throwingTrash) {
                for (int j = 0; j < map.trashCans.size; j++) {
                    if (guest.intersects(map.trashCans.get(j))) {
                        guest.throwingTrash=false;
                        guest.goHere(new Point(guest.destination.x, guest.destination.y));
                    }
                }
            }
        }

        //szemetek kirajzolasa
        if(map.trashes.size>0)
        {
            for (int i = 0; i < map.trashes.size; i++)
            {
                Trash t = map.trashes.get(i);
                game.batch.draw(t.texture,t.x+20,t.y+110,t.width,t.height);
            }
        }

        /**
         * Az aktuálisan lerakásra kiválaszott elem privewjának a kirajzolása az egér pozíciójára.
        */
        if(isSelected)
        {
            Vector3 mouse_pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mouse_pos);

            if(chosen == Tiles.GAMES)
            {
                game.batch.draw(korhinta_texture,mouse_pos.x-((3*map.tile_width)/2),
                        mouse_pos.y-((3*map.tile_height)/2),map.tile_width*3,map.tile_height*3);
            }
            else if(chosen == Tiles.FOOD)
            {
                game.batch.draw(hamburger_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.WATER)
            {
                game.batch.draw(water_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.STAFF)
            {
                game.batch.draw(staff_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.TRASHCAN)
            {
                game.batch.draw(trashcan_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.BUSH)
            {
                game.batch.draw(bush_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else
            {
                game.batch.draw(road_down_to_up,mouse_pos.x-map.tile_width/2,
                        mouse_pos.y-map.tile_height/2,map.tile_width ,map.tile_height);
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
            System.out.println(touch.x + " " + " " + touch.y);
            if(map.touched(touch,chosen,isSelected))
            {
                /**
                 * pénz levonás, elem lerakásánál.
                 */
                switch (chosen){
                    case ROAD:
                        moneyHeist(roadPrice);
                        break;
                    case GAMES:
                        moneyHeist(gamePrice);
                        break;
                    case FOOD:
                    case WATER:
                    case STAFF:
                        moneyHeist(buildingPrice);
                        break;
                    case BUSH:
                    case TREE:
                        moneyHeist(plantPrice);
                        break;
                    case TRASH:
                        moneyHeist(trashcanPrice);
                        break;
                    default:
                        moneyHeist(0);
                }
            }
        }
    }

        public void runTimer(){
            gametimer.schedule(task, 0, 1000 );
        }

        public String getTime(int sec)
        {
            int hours = 0;
            int remainderOfHours = 0;
            int minutes = 0;
            int seconds = 0;

            if (sec >= 3600)
            {
                hours = sec / 3600;
                remainderOfHours = sec % 3600;        // could be more or less than a min

                if (remainderOfHours >= 60)   //check if remainder is more or equal to a min
                {
                    minutes = remainderOfHours / 60;
                    seconds = remainderOfHours % 60;
                }
                else
                {                       // if it's less than a min
                    seconds = remainderOfHours;
                }
            }
            // if we have a min or more
            else if (sec >= 60)
            {
                hours = 0;               //62
                minutes = sec / 60;
                seconds = sec % 60;
            }
            //if we have just seconds
            else if (sec < 60)
            {
                hours = 0;
                minutes = 0;
                seconds = sec;
            }
//i get integer hour minuite second. i want to transform them to strings:

            String strHours;
            String strMins;
            String strSecs;

            if(seconds < 10)
                strSecs = "0" + Integer.toString(seconds);
            else
                strSecs = Integer.toString(seconds);

            if(minutes < 10)
                strMins = "0" + Integer.toString(minutes);
            else
                strMins = Integer.toString(minutes);

            if(hours < 10)
                strHours = "0" + Integer.toString(hours);
            else
                strHours = Integer.toString(hours);


            String time = strHours + ":" + strMins + ":" + strSecs;
            return time;
        }


    /**
     * gombok létrehozáse, kezelése, eventListenerek beállítása.
     */
    public void buttonManagment(){

        startButton = new TextButton("Open Park", skin);
        startButton.setSize(100, 30);
        startButton.setPosition(550, 870);
        startButton.addListener(new ClickListener() {
                                    public void clicked(InputEvent e, float x, float y) {
                                        timer.cancel();
                                        timer = new Timer();
                                        new CreatePerson().run();
                                        runTimer();
                                    }
                                });

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


        stage.addActor(startButton);
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
    public GameMap getMap()
    {
        return map;
    }

    public void texturesInit(){ //d
        guest_texture = new Texture(Gdx.files.internal("people.png"));
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
        trash_texture = new Texture(Gdx.files.internal("trash.png"));
        trashcan_texture = new Texture(Gdx.files.internal("trashcan.png"));

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
        textureRegionTrash = new TextureRegion(trashcan_texture);
        textureRegionDrawableTrash = new TextureRegionDrawable(textureRegionTrash);
        textureRegionStaff = new TextureRegion(staff_texture);
        textureRegionDrawableStaff = new TextureRegionDrawable(textureRegionStaff);

        happy_texture = new Texture(Gdx.files.internal("guestHappy.png"));
        annoyed_texture = new Texture(Gdx.files.internal("guestAnnoyed.png"));
        angry_texture = new Texture(Gdx.files.internal("guestAngry.png"));
    }

    public void moneyHeist(int price){

        money = money - price;
        label.setText("[BLACK]Money: " + money + "$");
        label.setPosition(70,820);
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

    class CreatePerson extends TimerTask {
        public void run() {

            //System.out.println(delay + " sec is up");
            int delay = (new Random().nextInt(10))*1000;
            timer.schedule(new GameScreen.CreatePerson(), delay);
            Guest p = new Guest(map,0,0,20,20, guest_texture, window_height, window_width,happy_texture,annoyed_texture,angry_texture,trash_texture);
            guests.add(p);
            /*
            Guest p = new Guest(map,0,0,20,20, guest_texture, window_height, window_width,happy_texture,annoyed_texture,angry_texture,trash_texture);
            guests.add(p);
*/
        }
    }
}
