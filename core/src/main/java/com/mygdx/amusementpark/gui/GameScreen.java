package com.mygdx.amusementpark.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.amusementpark.buildable.*;
import com.mygdx.amusementpark.people.Cleaner;
import com.mygdx.amusementpark.people.Guest;
import com.mygdx.amusementpark.people.Mechanic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen implements Screen, ActionListener
{
    Timer timer = new Timer();
    Timer gameTimer = new Timer();
    private Array<Guest> guests = new Array<Guest>();
    public int ticketPrice = 50;

    /**
     * Ablak, tile-ok méreteineek beállítása
     * Camera, felület létrehozása.
     */
    final AmusementPark game;
    public GameOverScreen gameOver;
    OrthographicCamera camera;
    private final Stage stage;
    private final int window_height = 800;
    private final int window_width = 1200;

    /**
     * Gombok textúrái, méreteinek beállítása.
     */
    private TextButton startButton;
    private TextButton closeParkButton;

    private TextButton buildButton;
    private TextButton parkButton;

    private TextButton gamesButton;
    private ImageButton rollerButton;
    private Label rollerPrice;
    private ImageButton castleButton;
    private Label castlePrice;

    private TextButton plantsButton;
    private ImageButton bushButton;
    private Label bushPrice;

    private TextButton roadButton;
    private ImageButton rButton;
    private Label roadPrice;

    private TextButton staffButton;
    private  ImageButton cleanerButton;
    private Label cleanerBuildingPrice;
    private  ImageButton mechanicButton;
    private Label mechanicBuildingPrice;

    private TextButton guestButton;
    private ImageButton trashButton;
    private Label trashPrice;
    private ImageButton hamburgerButton;
    private Label hamburgerPrice;
    private ImageButton waterButton;
    private Label waterPrice;
    private TextButton closeMenuButton;

    private final int buttonWidth = 150;
    private  int buttonHeight = 50;

    /**
     * Alap elemek textúrái. d
     */


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
    private Label moneyLabel;
    private Label timeLabel;
    private Label guestsLabel;
    private Label dayLabel;
    int day = 0;
    private TimerTask task;

    int gamePrice = 2000;
    int plantPrice = 1000;
    int buildingPrice = 1500;
    int trashcanPrice = 1000;
    int roadsPrice = 100;

    private GameMap map;
    int ido = 0;

    JPanel p;
    JTextField t;
    JFrame f;
    JButton b;
    public Boolean takingInPrice = false;
    Buildable actualPlaced = null;


    public Textures textures = new Textures(false);

    /**
     * Létrehozza a képernyőn a pályát
     * Beállítja a szövegeket a helyükre.
     * elindítja az órát
     */
    public GameScreen(final AmusementPark game)
    {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        BitmapFont labelFont = skin.get("default-font", BitmapFont.class);
        labelFont.getData().markupEnabled = true;
        moneyLabel = new Label("", skin);
        moneyLabel.setPosition(70,872);

        timeLabel = new Label("", skin);
        timeLabel.setPosition(220,872);

        guestsLabel = new Label("[WHITE]Guests: 0", skin);
        guestsLabel.setPosition(370,860);

        dayLabel = new Label("[WHITE]Day: 0",skin);
        dayLabel.setPosition(670, 868);

        rollerPrice = new Label("", skin);
        castlePrice = new Label("", skin);
        bushPrice = new Label("", skin);
        roadPrice = new Label("", skin);
        hamburgerPrice = new Label("", skin);
        waterPrice = new Label("", skin);
        trashPrice = new Label("", skin);
        mechanicBuildingPrice = new Label("", skin);
        cleanerBuildingPrice = new Label("", skin);


        stage.addActor(moneyLabel);
        stage.addActor(timeLabel);
        stage.addActor(guestsLabel);
        stage.addActor(dayLabel);

        stage.addActor(rollerPrice);
        stage.addActor(castlePrice);
        stage.addActor(bushPrice);
        stage.addActor(roadPrice);
        stage.addActor(hamburgerPrice);
        stage.addActor(waterPrice);
        stage.addActor(trashPrice);
        stage.addActor(mechanicBuildingPrice);
        stage.addActor(cleanerBuildingPrice);


        task = new TimerTask() {
            public void run() {
                String time = getTime(ido);
                timeLabel.setText("[WHITE]Time: "+time);
                ido++;

            }
        };

        camera = new OrthographicCamera();
        camera.setToOrtho(false, window_width, window_height+100);
        map = new GameMap(window_height, window_width, false);
        buttonManagment();
        moneyHeist(0);
        runTimer();
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

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(textures.grass_texture,0,0,window_width,window_height+100);

        /**
         * Palya kirajzolása
         */
        for(int i = 0; i < map.units.size; i++)
        {
            for(int j = 0; j< map.units.get(i).size; j++)
            {
                Buildable act = map.units.get(i).get(j);
                game.batch.draw(act.getTexture(),act.x,act.y+100,act.width,act.height);
            }
        }

        /**
         * Szemét kirajzolása, ha nincs takarító
         */
        if(map.cleaners.size==0)
        {
            if(map.trashes.size>0)
            {
                for (int i = 0; i < map.trashes.size; i++)
                {
                    Trash t = map.trashes.get(i);
                    game.batch.draw(t.texture,t.x+20,t.y+110,t.width,t.height);

                    for (int k = 0; k < guests.size; k++)
                    {
                        if (guests.get(k).intersects(map.trashes.get(i))&&!guests.get(k).steppedInTrash)
                        {
                            guests.get(k).stepToTrash();
                        }
                    }
                }
            }
        }

        /**
         * Vendékeg kirajzolása, mozgatása
         */
        for(int i = 0; i < guests.size; i++)
        {
            Guest guest = guests.get(i);
            guest.move();
            guest.setMap(map);
            game.batch.draw(guest.getTexture(),guest.x+20,guest.y+110,guest.width,guest.height);
            for(int j = 0; j < map.destinationPoints.size; j++)
            {
                /**
                 * Ha a vendég megérkezik a célpontjára
                 */
                if(guest.intersects(map.destinationPoints.get(j)))
                {
                    if(!guest.isGoing && !guest.isWaiting)
                    {
                        /**
                         * megérkezi, jobb kedve lesz és fizet a játékért
                         */
                        guest.reachedDestination(map.destinationPoints.get(j).timeToUse);

                        if (map.destinationPoints.get(j).getType() == Tiles.FOOD)
                        {
                            guest.justAte = true;
                        }
                        else
                        {
                            guest.justAte = false;
                        }

                        money+=map.destinationPoints.get(j).prizeToUse;
                        guest.gainMood(map.destinationPoints.get(j).moodGain);

                        if(map.destinationPoints.get(j).getType()== Tiles.CASTLE||map.destinationPoints.get(j).getType()== Tiles.ROLLER)
                        {
                            /**
                             * Használat során kopik a játék
                             */
                            ((Games)map.destinationPoints.get(j)).takeDmg();
                        }
                    }
                }
            }
            /**
             * Ha szemetelhetnék-e van és van a közelben kuka, akkor a kukához megy.
             */
            if(guest.throwingTrash) {
                for (int j = 0; j < map.trashCans.size; j++) {
                    if (guest.intersects(map.trashCans.get(j))) {
                        guest.throwingTrash=false;
                        guest.goHere(new Point(guest.destination.x, guest.destination.y));
                    }
                }
            }
        }

        /**
         * Takarítók kirajzolása, mozgatása, szemetek kirajzolása ha van takarító
         */
        if(map.cleaners.size>0)
        {
            for (int i = 0; i < map.cleaners.size; i++)
            {
                map.cleaners.get(i).move();
                if(map.cleaners.get(i).trashes.size>0)
                {
                    for (int j = 0; j < map.cleaners.get(i).trashes.size; j++)
                    {
                        Trash t = map.cleaners.get(i).trashes.get(j);
                        game.batch.draw(t.texture, t.x + 20, t.y + 110, t.width, t.height);

                        if (map.cleaners.get(i).intersects(map.cleaners.get(i).trashes.get(j)))
                        {
                            map.cleaners.get(i).isCleaning = false;
                            map.cleaners.get(i).trashes.removeIndex(j);
                            if (map.cleaners.get(i).trashes.size > 0)
                            {
                                map.cleaners.get(i).goHere(new Point(map.cleaners.get(i).trashes.get(0).x, map.cleaners.get(i).trashes.get(0).y));
                                map.cleaners.get(i).isCleaning = true;
                            }
                        }
                        /**
                         * ha egy vendég szemétre lép, rosszabb lesz a kedve
                         */
                        for (int k = 0; k < guests.size; k++)
                        {
                            if (map.cleaners.get(i).trashes.size > 0 && j<map.cleaners.get(i).trashes.size)
                            {
                                if (guests.get(k).intersects(map.cleaners.get(i).trashes.get(j)) && !guests.get(k).steppedInTrash)
                                {
                                    guests.get(k).stepToTrash();
                                }
                            }
                        }
                    }
                }
                Cleaner cleaner = map.cleaners.get(i);
                game.batch.draw(cleaner.texture, cleaner.x + 20, cleaner.y + 110, cleaner.width, cleaner.height);
            }
        }

        /**
         * Szerelők kirajzolása, mozgatása
         */
        for(int i = 0; i<map.mechanics.size; i++)
        {
            Mechanic mechanic = map.mechanics.get(i);
            mechanic.move();
            game.batch.draw(mechanic.texture, mechanic.x + 20, mechanic.y + 110, mechanic.width, mechanic.height);
            if (mechanic.intersects(mechanic.destination))
            {
                if(mechanic.destination.getType()==Tiles.MECHANIC && !mechanic.isHome)
                {
                    mechanic.isHome = true;
                    mechanic.gotHome();
                }
                else
                {
                    if (!mechanic.isFixing && mechanic.destination.getType() != Tiles.MECHANIC)
                    {
                        mechanic.isFixing = true;
                        mechanic.fixIt();
                    }
                }
            }

        }
        /**
         * Az aktuálisan lerakásra kiválaszott elem privewjának a kirajzolása az egér pozíciójára.
         */
        if(isSelected)
        {
            Vector3 mouse_pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mouse_pos);

            if(chosen == Tiles.ROLLER)
            {
                game.batch.draw(textures.roller_texture,mouse_pos.x-((3*map.tile_width)/2),
                        mouse_pos.y-((3*map.tile_height)/2),map.tile_width*3,map.tile_height*3);
            }
            else if(chosen == Tiles.CASTLE)
            {
                game.batch.draw(textures.castle_texture,mouse_pos.x-((3*map.tile_width)/2),
                        mouse_pos.y-((3*map.tile_height)/2),map.tile_width*3,map.tile_height*3);
            }
            else if(chosen == Tiles.FOOD)
            {
                game.batch.draw(textures.hamburger_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.WATER)
            {
                game.batch.draw(textures.water_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.CLEANER)
            {
                game.batch.draw(textures.cleanerHouse_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.MECHANIC)
            {
                game.batch.draw(textures.mechanicHouse_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.TRASH)
            {
                game.batch.draw(textures.trashcan_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else if(chosen == Tiles.BUSH)
            {
                game.batch.draw(textures.bush_texture,mouse_pos.x-((map.tile_width)/2),
                        mouse_pos.y-((map.tile_height)/2),map.tile_width,map.tile_height);
            }
            else
            {
                game.batch.draw(textures.road_down_to_up,mouse_pos.x-map.tile_width/2,
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
            //System.out.println(touch.x + " " + " " + touch.y);
            actualPlaced = map.touched(touch,chosen,isSelected);
            if(actualPlaced!=null)
            {
                if(actualPlaced.getType()==Tiles.CASTLE||actualPlaced.getType()==Tiles.ROLLER)
                {
                    showPrizeSettingWindow();
                }
                /**
                 * Teendők ha leraktunk egy bizonyos épületet.
                 */
                switch (chosen){
                    case ROAD:
                        moneyHeist(roadsPrice);
                        break;
                    case ROLLER:
                    case CASTLE:
                        moneyHeist(gamePrice);
                        break;
                    case FOOD:
                    case WATER:
                        moneyHeist(buildingPrice);
                        break;
                    case CLEANER:
                        moneyHeist(buildingPrice);
                        /**
                         * Ha a cleaner buildinget raktunk le, akkor spawnolunk egy takarítót
                         *
                         * Ha ez az első takarító akkor oda adjuk neki a pályán lévő összes szemetet
                         */
                        if(map.cleaners.size==0)
                        {
                            map.cleaners.add(new Cleaner(map, 0,0, 20, 20, textures.cleaner_texture,window_height,window_width, map.trashes));
                            map.trashes=new Array<Trash>();
                        }
                        else
                        {
                            map.cleaners.add(new Cleaner(map, 0,0, 20, 20, textures.cleaner_texture,window_height,window_width,new Array<Trash>()));
                        }
                        break;
                    case MECHANIC:
                        /**
                         * ha mechanic buldinget rakunk le, akkor spawnolunk egy mechanikot
                         */
                        moneyHeist(buildingPrice);
                        map.mechanics.add(new Mechanic(map,0,0,20,20,textures.mechanic_texture,window_height,window_width,(new Point((int)touch.x,(int)touch.y-100))));
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

    public void actionPerformed(ActionEvent e)
    {

        String s = e.getActionCommand();
        if (s.equals("submit")) {
            if(takingInPrice)
            {
                takingInPrice=false;
                if(t.getText().length()>0)
                {
                    actualPlaced.prizeToUse = Integer.parseInt(t.getText());
                }
                f.setVisible(false);
            }
        }
        System.out.println(actualPlaced.prizeToUse);
    }

    public void showPrizeSettingWindow()
    {
        takingInPrice = true;
        parkButton.setVisible(false);
        gamesButton.setVisible(false);
        plantsButton.setVisible(false);
        roadButton.setVisible(false);
        rButton.setVisible(false);
        roadPrice.setVisible(false);
        staffButton.setVisible(false);
        guestButton.setVisible(false);
        rollerButton.setVisible(false);
        rollerPrice.setVisible(false);
        castleButton.setVisible(false);
        castlePrice.setVisible(false);
        bushButton.setVisible(false);
        bushPrice.setVisible(false);
        cleanerButton.setVisible(false);
        mechanicButton.setVisible(false);
        cleanerBuildingPrice.setVisible(false);
        mechanicBuildingPrice.setVisible(false);
        trashButton.setVisible(false);
        hamburgerButton.setVisible(false);
        waterButton.setVisible(false);
        trashPrice.setVisible(false);
        hamburgerPrice.setVisible(false);
        waterPrice.setVisible(false);

        p = new JPanel();

        t = new JTextField(5);
        b = new JButton("submit");
        f = new JFrame("textfield");
        p.add(t);
        p.add(b);
        f.add(p);
        b.addActionListener(this);
        f.setSize(200,200);
        f.setVisible(true);
    }

    public void runTimer(){
        gameTimer.schedule(task, 0, 1000 );
    }

    /**
     *
     * @param sec - aktuális másodpercek
     * @return Teljes idő
     */
    public String getTime(int sec)
    {
        int hours = 0;
        int remainderOfHours = 0;
        int minutes = 0;
        int seconds = 0;

        if (sec >= 3600)
        {
            hours = sec / 3600;
            remainderOfHours = sec % 3600;

            if (remainderOfHours >= 60)
            {
                minutes = remainderOfHours / 60;
                seconds = remainderOfHours % 60;
            }
            else
            {
                seconds = remainderOfHours;
            }
        }
        // if we have a min or more
        else if (sec >= 60)
        {
            hours = 0;
            minutes = sec / 60;
            seconds = sec % 60;
        }
        if (sec%60 == 0){
            money = money-(100*map.cleaners.size) - (100*map.mechanics.size);
            moneyLabel.setText("[WHITE]Money: " + money);
        }

        else if (sec < 60)
        {
            hours = 0;
            minutes = 0;
            seconds = sec;
        }

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
        startButton.setPosition(550, 865);
        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                day = day + 1;
                dayLabel.setText("[WHITE]Day: " + day);
                timer.cancel();
                timer = new Timer();
                new CreatePerson().run();
                startButton.setVisible(false);
                closeParkButton.setVisible(true);
            }
        });

        closeParkButton = new TextButton("Close Park", skin);
        closeParkButton.setSize(100, 30);
        closeParkButton.setPosition(550, 865);
        closeParkButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                //startButton.setVisible(false);
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
                roadPrice.setVisible(false);
                gamesButton.setVisible(false);
                plantsButton.setVisible(false);
                closeMenuButton.setVisible(true);
                rollerButton.setVisible(false);
                rollerPrice.setVisible(false);
                castleButton.setVisible(false);
                castlePrice.setVisible(false);
                bushButton.setVisible(false);
                bushPrice.setVisible(false);
                cleanerButton.setVisible(false);
                mechanicButton.setVisible(false);
                cleanerBuildingPrice.setVisible(false);
                mechanicBuildingPrice.setVisible(false);
                trashButton.setVisible(false);
                hamburgerButton.setVisible(false);
                waterButton.setVisible(false);
                trashPrice.setVisible(false);
                hamburgerPrice.setVisible(false);
                waterPrice.setVisible(false);

                if (isSelected)
                    isSelected = false;
            }
        });

        closeMenuButton = new TextButton("Close menu", skin);
        closeMenuButton.setSize(buttonWidth, buttonHeight);
        closeMenuButton.setPosition(1000, 40);
        closeMenuButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                closeMenuButton.setVisible(false);
                //ide minden gombot be kell majd írni kivéve a buildet

                parkButton.setVisible(false);
                gamesButton.setVisible(false);
                plantsButton.setVisible(false);
                roadButton.setVisible(false);
                rButton.setVisible(false);
                roadPrice.setVisible(false);
                staffButton.setVisible(false);
                guestButton.setVisible(false);
                rollerButton.setVisible(false);
                rollerPrice.setVisible(false);
                castleButton.setVisible(false);
                castlePrice.setVisible(false);
                bushButton.setVisible(false);
                bushPrice.setVisible(false);
                cleanerButton.setVisible(false);
                mechanicButton.setVisible(false);
                cleanerBuildingPrice.setVisible(false);
                mechanicBuildingPrice.setVisible(false);
                trashButton.setVisible(false);
                hamburgerButton.setVisible(false);
                waterButton.setVisible(false);
                trashPrice.setVisible(false);
                hamburgerPrice.setVisible(false);
                waterPrice.setVisible(false);

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
                roadPrice.setVisible(false);
                bushButton.setVisible(false);
                bushPrice.setVisible(false);
                cleanerButton.setVisible(false);
                mechanicButton.setVisible(false);
                cleanerBuildingPrice.setVisible(false);
                mechanicBuildingPrice.setVisible(false);

                rollerButton.setVisible(false);
                rollerPrice.setVisible(false);
                castleButton.setVisible(false);
                castlePrice.setVisible(false);

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

                rollerButton.setVisible(true);
                rollerButton.setPosition(buttonWidth * 3 + 50,40);
                rollerPrice.setVisible(true);
                rollerPrice.setText("[BLACK]" + gamePrice + "$");
                rollerPrice.setPosition(buttonWidth * 3 + 50,20);

                castleButton.setVisible(true);
                castleButton.setPosition(buttonWidth * 3 + 130,30);
                castlePrice.setVisible(true);
                castlePrice.setText("[BLACK]" + gamePrice + "$");
                castlePrice.setPosition(buttonWidth * 3 + 140,20);

                if (isSelected)
                    isSelected = false;
            }
        });

        rollerButton = new ImageButton(textures.textureRegionDrawableGameRoller);
        rollerButton.setSize(50,50);
        rollerButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.ROLLER;
                isSelected=true;
            }
        });

        castleButton = new ImageButton(textures.textureRegionDrawableGameCastle);
        castleButton.setSize(70,70);
        castleButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.CASTLE;
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
                rollerButton.setVisible(false);
                rollerPrice.setVisible(false);
                castleButton.setVisible(false);
                castlePrice.setVisible(false);

                bushButton.setVisible(true);
                bushButton.setPosition(buttonWidth*3 + 50, 40);
                bushPrice.setVisible(true);
                bushPrice.setText("[BLACK]" + plantPrice + "$");
                bushPrice.setPosition(buttonWidth*3 + 50, 20);

                if (isSelected)
                    isSelected = false;
            }
        });

        bushButton = new ImageButton(textures.textureRegionDrawablePlantBush);
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
                rollerButton.setVisible(false);
                rollerPrice.setVisible(false);
                castleButton.setVisible(false);
                castlePrice.setVisible(false);

                rButton.setVisible(true);
                rButton.setPosition(buttonWidth * 3 + 50,40);
                roadPrice.setVisible(true);
                roadPrice.setText("[BLACK]" + roadsPrice + "$");
                roadPrice.setPosition(buttonWidth * 3 + 55, 30);

                if (isSelected)
                    isSelected = false;

            }
        });

        rButton = new ImageButton(textures.textureRegionDrawableRoad);
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

                cleanerButton.setVisible(true);
                cleanerButton.setPosition(buttonWidth*2 + 50,40);
                cleanerBuildingPrice.setVisible(true);
                cleanerBuildingPrice.setText("[BLACK]" + buildingPrice + "$");
                cleanerBuildingPrice.setPosition(buttonWidth*2 + 50, 20);

                mechanicButton.setVisible(true);
                mechanicButton.setPosition(buttonWidth*2 + 120,40);
                mechanicBuildingPrice.setVisible(true);
                mechanicBuildingPrice.setText("[BLACK]" + buildingPrice + "$");
                mechanicBuildingPrice.setPosition(buttonWidth*2 + 120, 20);

                if (isSelected)
                    isSelected = false;
            }
        });

        cleanerButton = new ImageButton(textures.textureRegionDrawableCleaner);
        cleanerButton.setSize(50,50);
        cleanerButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.CLEANER;
                isSelected=true;
            }
        });

        mechanicButton = new ImageButton(textures.textureRegionDrawableMechanic);
        mechanicButton.setSize(50,50);
        mechanicButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.MECHANIC;
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
                waterButton.setPosition(buttonWidth*2 + 120, 40);
                hamburgerButton.setVisible(true);
                hamburgerButton.setPosition(buttonWidth*2 + 200, 40);


                trashPrice.setVisible(true);
                trashPrice.setText("[BLACK]" + trashcanPrice + "$");
                trashPrice.setPosition(buttonWidth*2 + 50, 20);
                waterPrice.setVisible(true);
                waterPrice.setText("[BLACK]" + buildingPrice + "$");
                waterPrice.setPosition(buttonWidth*2 + 120, 20);
                hamburgerPrice.setVisible(true);
                hamburgerPrice.setText("[BLACK]" + buildingPrice + "$");
                hamburgerPrice.setPosition(buttonWidth*2 + 200, 20);



                if (isSelected)
                    isSelected = false;
            }
        });

        trashButton = new ImageButton(textures.textureRegionDrawableTrash);
        trashButton.setSize(50,50);
        trashButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.TRASH;
                isSelected=true;
            }
        });

        waterButton = new ImageButton(textures.textureRegionDrawableWater);
        waterButton.setSize(50,50);
        waterButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.WATER;
                isSelected=true;
            }
        });

        hamburgerButton = new ImageButton(textures.textureRegionDrawableHamburger);
        hamburgerButton.setSize(50,50);
        hamburgerButton.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                chosen = Tiles.FOOD;
                isSelected=true;
            }
        });


        stage.addActor(startButton);
        stage.addActor(closeParkButton);
        stage.addActor(buildButton);
        stage.addActor(parkButton);
        stage.addActor(staffButton);
        stage.addActor(guestButton);
        stage.addActor(rollerButton);
        stage.addActor(castleButton);
        stage.addActor(gamesButton);
        stage.addActor(plantsButton);
        stage.addActor(roadButton);
        stage.addActor(rButton);
        stage.addActor(closeMenuButton);
        stage.addActor(bushButton);
        stage.addActor(cleanerButton);
        stage.addActor(mechanicButton);
        stage.addActor(trashButton);
        stage.addActor(hamburgerButton);
        stage.addActor(waterButton);



        closeParkButton.setVisible(false);
        parkButton.setVisible(false);
        staffButton.setVisible(false);
        guestButton.setVisible(false);
        gamesButton.setVisible(false);
        plantsButton.setVisible(false);
        roadButton.setVisible(false);
        rButton.setVisible(false);
        closeMenuButton.setVisible(false);
        rollerButton.setVisible(false);
        castleButton.setVisible(false);
        bushButton.setVisible(false);
        cleanerButton.setVisible(false);
        mechanicButton.setVisible(false);
        trashButton.setVisible(false);
        hamburgerButton.setVisible(false);
        waterButton.setVisible(false);

        rollerPrice.setVisible(false);
        castlePrice.setVisible(false);
        bushPrice.setVisible(false);
        hamburgerPrice.setVisible(false);
        waterPrice.setVisible(false);
        trashPrice.setVisible(false);
        mechanicBuildingPrice.setVisible(false);
        cleanerBuildingPrice.setVisible(false);
    }

    /**
     * terxtúrák beállítása.
     */
    public GameMap getMap()
    {
        return map;
    }


    /**
     *
     * @param price - ennyit vonunk le egy játék lerakásakor
     */
    public void moneyHeist(int price){

        money = money - price;
        if (money > 0) {
            moneyLabel.setText("[WHITE]Money: " + money + "$");
        }else{
            game.setScreen(new GameOverScreen(game));
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

    /**
     * Ha elindítjuk a játékot, random időközben spawnol vendégeket, 5 * annyit, mint amennyi játék van.
     */
    class CreatePerson extends TimerTask {
        public void run() {
            int delay = (new Random().nextInt(10))*1000;
            timer.schedule(new GameScreen.CreatePerson(), delay);
            if((map.destinationPoints.size)*5>guests.size)
            {

                Guest p = new Guest(map, 0, 0, 20, 20,
                        textures.guest_texture, window_height, window_width,
                        textures.happy_texture, textures.annoyed_texture, textures.angry_texture,textures.trash_texture);
                guests.add(p);
                guestsLabel.setText("[WHITE]Guests: " + guests.size);
                money = money + ticketPrice;
                moneyLabel.setText("[WHITE]Money:" + money + "$");
            }
        }
    }
}
