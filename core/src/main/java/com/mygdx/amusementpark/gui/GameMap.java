package main.java.com.mygdx.amusementpark.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import main.java.com.mygdx.amusementpark.buildable.*;
import main.java.com.mygdx.amusementpark.pathfinding.Mover;
import main.java.com.mygdx.amusementpark.pathfinding.TileBasedMap;
import main.java.com.mygdx.amusementpark.people.Cleaner;
import main.java.com.mygdx.amusementpark.people.Mechanic;


import java.awt.*;

public class GameMap implements TileBasedMap
{
    /** A játék felületen egy sorban hány Tile van */
    public static final int WIDTH = 20;
    /** Egy oszlopban hány Tile van */
    public static final int HEIGHT = 20;

    /** Tartalmazza, hogy egy helyen milyen típusú egység van */
    public Array<Array<Tiles>> terrain = new Array<Array<Tiles>>();

    /** Tárolja az összes épületet a pályán */
    public Array<Array<Buildable>> units = new Array<Array<Buildable>>();

    /** Tárolja a helyeket ahova a vendégek mehetnek.   */
    public Array<Buildable> destinationPoints = new Array<Buildable>();

    /**
     * Kukákat tároló tömb
     */
    public Array<Buildable> trashCans = new Array<Buildable>();

    /** szemeteket tárolása */
    public Array<Trash> trashes = new Array<Trash>();

    /** Takarítók tárolása */
    public Array<Cleaner> cleaners = new Array<Cleaner>();

    /** Szerelők tárolása */
    public Array<Mechanic> mechanics = new Array<Mechanic>();

    /** Személyzet épületeinek tárolása */
    public Array<StaffBuilding> staffBuildings = new Array<StaffBuilding>();


    /** Aktuális cépont - lehet, hogy törölhető */
    Point p = new Point();


    /** Indicator if a given tile has been visited during the search */
    public boolean[][] visited = new boolean[WIDTH][HEIGHT];

    public int tile_width;
    public int tile_height;


    /**
     * Alap elemek textúrái. d
     */
    Texture wall_texture;
    Texture grass_texture;
    Texture gate_texture;
    Texture fence_texture;
    Texture roller_texture;
    Texture roller_broken_texture;
    Texture castle_texture;
    Texture castle_broken;
    Texture cleanerHouse_texture;
    Texture mechanicHouse_texture;
    Texture bush_texture;
    Texture water_texture;
    Texture hamburger_texture;
    Texture trashcan_texture;
    Texture trash_texture;
    Texture cleaner_texture;

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

    Camera camera;

    public GameMap(int window_h, int window_w, Camera camera)
    {
        this.tile_width=window_w/WIDTH;
        this.tile_height=window_h/HEIGHT;

        texturesInit();
        mapInit();
    }

    /**
     * Pálya kezdőállapotba állítása
     */
    public void mapInit()
    {
        Buildable tile;
        Tiles type = Tiles.EMPTY;
        for(int i = 0; i < HEIGHT; i++)
        {
            Array<Buildable> empty = new Array<Buildable>();
            units.add(empty);
            Array<Tiles> empty_tiles = new Array<Tiles>();
            terrain.add(empty_tiles);


            for(int j = 0; j < WIDTH; j++)
            {
                if(j==0 & i == 10)
                {
                    type = Tiles.EMPTY;
                    tile = new Road((i)*tile_width,(j)*tile_height,tile_width,tile_height,road_down_to_up,30,type);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }
                else if(j==1 & i == 10)
                {
                    type = Tiles.ROAD;
                    tile = new Road((i)*tile_width,(j)*tile_height,tile_width,tile_height,road_down_to_up,20,type);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }
                else if(j==1)
                {
                    if(i == 8 || i==12)
                    {
                        type = Tiles.BORDER;
                        tile = new Border((i)*tile_width,(j)*tile_height,tile_width,tile_height,gate_texture,20,type);
                        units.get(i).add(tile);
                        terrain.get(i).add(type);
                    }
                    else if(i>8 && i<12)
                    {
                        type = Tiles.EMPTY;
                        tile = new Buildable((i)*tile_width,(j)*tile_height,tile_width,tile_height,grass_texture,20,type,10);
                        units.get(i).add(tile);
                        terrain.get(i).add(type);
                    }
                    else
                    {
                        type = Tiles.BORDER;
                        tile = new Border((i)*tile_width,(j)*tile_height,tile_width,tile_height,fence_texture,20,type);
                        units.get(i).add(tile);
                        terrain.get(i).add(type);
                    }

                }
                else if (i == 0|| i == WIDTH - 1 || j == HEIGHT - 1)
                {
                    type = Tiles.BORDER;
                    tile = new Border((i)*tile_width,(j)*tile_height,tile_width,tile_height,wall_texture,10,type);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }
                else
                {
                    type = Tiles.EMPTY;
                    tile = new Buildable((i)*tile_width,(j)*tile_height,tile_width,tile_height,grass_texture,10,type,10);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }
            }
        }
    }
    /**
     *  Megnézi, hogy egy útnak vannak-e szomszédai
     */
    public void checkRoadNeighbours()
    {
        for(int i = 0; i<units.size; i++)
        {
            for(int j = 0; j<units.get(i).size; j++)
            {
                if(units.get(i).get(j).getType() == Tiles.ROAD)
                {
                    Road road = (Road) units.get(i).get(j);
                    if(units.get(i-1).get(j).getType()!=Tiles.EMPTY)
                    {
                        road.leftNeigh = true;
                    }
                    else
                    {
                        road.leftNeigh = false;
                    }
                    if(units.get(i+1).get(j).getType()!=Tiles.EMPTY)
                    {
                        road.rightNeigh = true;
                    }
                    else
                    {
                        road.rightNeigh = false;
                    }
                    if(units.get(i).get(j-1).getType()!=Tiles.EMPTY)
                    {
                        road.downNeigh = true;
                    }
                    else
                    {
                        road.downNeigh = false;
                    }
                    if(units.get(i).get(j+1).getType()!=Tiles.EMPTY)
                    {
                        road.upNeigh = true;
                    }
                    else
                    {
                        road.upNeigh = false;
                    }

                    //megfelelő textúrák beállítása
                    if (road.upNeigh & road.rightNeigh & road.leftNeigh & road.downNeigh) //mind a 4 irányból jön út
                    {
                        road.setTexture(road_from_all);
                    }
                    else if(road.upNeigh & road.leftNeigh & road.rightNeigh) //3 irányból, felfele
                    {
                        road.setTexture(road_threeway_to_up);
                    }
                    else if(road.rightNeigh & road.upNeigh & road.downNeigh) //3 irányból, jobbra
                    {
                        road.setTexture(road_threeway_to_right);
                    }
                    else if(road.downNeigh & road.leftNeigh & road.rightNeigh) //3 irányból, lefele
                    {
                        road.setTexture(road_threeway_to_down);
                    }
                    else if(road.leftNeigh & road.upNeigh & road.downNeigh) //3 irányból balra
                    {
                        road.setTexture(road_threeway_to_left);
                    }
                    else if(road.upNeigh & road.downNeigh) //2 irány, fel-le
                    {
                        road.setTexture(road_down_to_up);
                    }
                    else if(road.upNeigh & road.leftNeigh) //2irány, fel-balra
                    {
                        road.setTexture(road_top_to_left);
                    }
                    else if(road.upNeigh & road.rightNeigh) //2irány, fel-jobbra
                    {
                        road.setTexture(road_top_to_right);
                    }
                    else if(road.downNeigh & road.leftNeigh) //2irány, le-balra
                    {
                        road.setTexture(road_down_to_left);
                    }
                    else if(road.downNeigh & road.rightNeigh) //2irány, le-jobbra
                    {
                        road.setTexture(road_down_to_right);
                    }
                    else if(road.leftNeigh & road.rightNeigh) //2 irány, jobbra-balra
                    {
                        road.setTexture(road_left_to_right);
                    }
                    else if(road.leftNeigh || road.rightNeigh)
                    {
                        road.setTexture(road_left_to_right);
                    }
                }
            }
        }
    }

    /**
     * kezeli ha kattintott valahova a játékos
     *
     * @param touch - a kattintás helye
     * @param chosen - a kiválaszott elem, amit le akarunk rakni
     * @param isSelected - van-e kiválasztva valami.
     * @return - sikerült-e lerakni egy új elemet.
     */
    public Boolean touched(Vector3 touch, Tiles chosen, Boolean isSelected)
    {
        Boolean did_place = false;
        for(int i = 0; i < units.size; i++)  //units.size => mennyi buildable van a tombben eppen
        {
            for(int j = 0; j < units.get(i).size; j++)
            {
                if(units.get(i).get(j).contains(touch.x,touch.y-100))
                {
                    if(isSelected==true)
                    {
                        if(units.get(i).get(j).getType()==Tiles.EMPTY)
                        {
                            Buildable actual;
                            Tiles type;
                            actual = new Buildable(units.get(i).get(j).x,
                                    units.get(i).get(j).y,
                                    units.get(i).get(j).width,
                                    units.get(i).get(j).height,grass_texture,0, Tiles.EMPTY,0);

                            type = Tiles.ROAD;
                            switch (chosen)
                            {
                                // ROAD,BORDER,EMPTY,GAMES,STAFF,BUSH,TREE,TRASH,WATER,FOOD
                                case ROAD:
                                    actual = new Road(units.get(i).get(j).x,units.get(i).get(j).y, units.get(i).get(j).width,units.get(i).get(j).height, road_down_to_up,10,Tiles.ROAD);
                                    type = Tiles.ROAD;
                                    break;

                                /**
                                 * Játék lerakás, 9 mezőt foglal el a kijelölt mező körül.
                                 */
                                case ROLLER:
                                    type = Tiles.ROLLER;

                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i + 1).get(j + 1).getType() == Tiles.EMPTY &&
                                            units.get(i).get(j + 1).getType() == Tiles.EMPTY &&
                                            units.get(i - 1).get(j + 1).getType() == Tiles.EMPTY &&
                                            units.get(i+1).get(j).getType() == Tiles.EMPTY &&
                                            units.get(i).get(j - 1).getType() == Tiles.EMPTY &&
                                            units.get(i - 1).get(j - 1).getType() == Tiles.EMPTY &&
                                            units.get(i - 1).get(j).getType() == Tiles.EMPTY &&
                                            units.get(i + 1).get(j - 1).getType() == Tiles.EMPTY
                                        )
                                        {
                                            actual = new Games( units.get(i).get(j).x,
                                                                units.get(i).get(j).y,
                                                                units.get(i).get(j).width,
                                                                units.get(i).get(j).height,
                                                    roller_texture, 10, Tiles.ROLLER,5, roller_texture,roller_broken_texture);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;
                                            destinationPoints.add(actual);

                                            units.get(i + 1).set(j + 1, actual);
                                            units.get(i + 1).set(j, actual);
                                            units.get(i + 1).set(j - 1, actual);
                                            units.get(i).set(j + 1, actual);
                                            units.get(i).set(j - 1, actual);
                                            units.get(i - 1).set(j + 1, actual);
                                            units.get(i - 1).set(j - 1, actual);
                                            units.get(i - 1).set(j, actual);

                                            terrain.get(i + 1).set(j + 1, type);
                                            terrain.get(i + 1).set(j, type);
                                            terrain.get(i + 1).set(j - 1, type);
                                            terrain.get(i).set(j + 1, type);
                                            terrain.get(i).set(j - 1, type);
                                            terrain.get(i - 1).set(j + 1, type);
                                            terrain.get(i - 1).set(j - 1, type);
                                            terrain.get(i - 1).set(j, type);
                                        }
                                    }
                                    break;
                                case CASTLE:
                                    type = Tiles.CASTLE;

                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i + 1).get(j + 1).getType() == Tiles.EMPTY &&
                                                units.get(i).get(j + 1).getType() == Tiles.EMPTY &&
                                                units.get(i - 1).get(j + 1).getType() == Tiles.EMPTY &&
                                                units.get(i+1).get(j).getType() == Tiles.EMPTY &&
                                                units.get(i).get(j - 1).getType() == Tiles.EMPTY &&
                                                units.get(i - 1).get(j - 1).getType() == Tiles.EMPTY &&
                                                units.get(i - 1).get(j).getType() == Tiles.EMPTY &&
                                                units.get(i + 1).get(j - 1).getType() == Tiles.EMPTY
                                        )
                                        {
                                            actual = new Games( units.get(i).get(j).x,
                                                                units.get(i).get(j).y,
                                                                units.get(i).get(j).width,
                                                                units.get(i).get(j).height,
                                                                castle_texture, 10, Tiles.CASTLE,5,castle_texture,castle_broken);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;
                                            destinationPoints.add(actual);

                                            units.get(i + 1).set(j + 1, actual);
                                            units.get(i + 1).set(j, actual);
                                            units.get(i + 1).set(j - 1, actual);
                                            units.get(i).set(j + 1, actual);
                                            units.get(i).set(j - 1, actual);
                                            units.get(i - 1).set(j + 1, actual);
                                            units.get(i - 1).set(j - 1, actual);
                                            units.get(i - 1).set(j, actual);

                                            terrain.get(i + 1).set(j + 1, type);
                                            terrain.get(i + 1).set(j, type);
                                            terrain.get(i + 1).set(j - 1, type);
                                            terrain.get(i).set(j + 1, type);
                                            terrain.get(i).set(j - 1, type);
                                            terrain.get(i - 1).set(j + 1, type);
                                            terrain.get(i - 1).set(j - 1, type);
                                            terrain.get(i - 1).set(j, type);
                                        }
                                    }
                                    break;
                                case CLEANER:
                                    type = Tiles.CLEANER;
                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new StaffBuilding(
                                                                units.get(i).get(j).x,
                                                                units.get(i).get(j).y,
                                                                units.get(i).get(j).width,
                                                                units.get(i).get(j).height,
                                                    cleanerHouse_texture,10,Tiles.CLEANER);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);

                                            staffBuildings.add((StaffBuilding) actual);
                                        }
                                    }


                                    break;
                                case MECHANIC:
                                    type = Tiles.MECHANIC;
                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new StaffBuilding(
                                                    units.get(i).get(j).x,
                                                    units.get(i).get(j).y,
                                                    units.get(i).get(j).width,
                                                    units.get(i).get(j).height,
                                                    mechanicHouse_texture,10,Tiles.MECHANIC);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
                                            staffBuildings.add((StaffBuilding) actual);
                                        }
                                    }
                                    break;
                                case FOOD:
                                    type = Tiles.FOOD;

                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new Catering(
                                                    units.get(i).get(j).x,
                                                    units.get(i).get(j).y,
                                                    units.get(i).get(j).width,
                                                    units.get(i).get(j).height,
                                                    hamburger_texture,10,Tiles.FOOD);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;
                                            destinationPoints.add(actual);

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
                                        }
                                    }

                                    break;
                                case BUSH:
                                    type = Tiles.BUSH;

                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new Park(
                                                        units.get(i).get(j).x,
                                                        units.get(i).get(j).y,
                                                        units.get(i).get(j).width,
                                                        units.get(i).get(j).height,
                                                        bush_texture,10,Tiles.BUSH);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
                                        }
                                    }
                                    break;
                                case TREE:
                                    actual = new Park(units.get(i).get(j).x,
                                            units.get(i).get(j).y,
                                            units.get(i).get(j).width,
                                            units.get(i).get(j).height,
                                            bush_texture,10,
                                            Tiles.TREE);
                                    type = Tiles.TREE;

                                    units.get(i).set(j, actual);
                                    terrain.get(i).set(j, type);
                                    break;
                                case TRASH:

                                    type = Tiles.TRASH;
                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new TrashCan(
                                                    units.get(i).get(j).x,
                                                    units.get(i).get(j).y,
                                                    units.get(i).get(j).width,
                                                    units.get(i).get(j).height,
                                                    trashcan_texture,10,Tiles.TRASHCAN);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;

                                            trashCans.add(actual);

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
                                        }
                                    }
                                    break;
                                case WATER:

                                    type = Tiles.WATER;
                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new Park(
                                                    units.get(i).get(j).x,
                                                    units.get(i).get(j).y,
                                                    units.get(i).get(j).width,
                                                    units.get(i).get(j).height,
                                                    water_texture,10,Tiles.WATER);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;
                                            destinationPoints.add(actual);

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
                                        }
                                    }
                                    break;
                                default: actual = new Buildable(units.get(i).get(j).x,
                                        units.get(i).get(j).y,
                                        units.get(i).get(j).width,
                                        units.get(i).get(j).height,grass_texture,0, Tiles.EMPTY,0);
                                    type = Tiles.EMPTY;

                            }
                            units.get(i).set(j,actual);
                            terrain.get(i).set(j,type);
                            checkRoadNeighbours();
                            did_place = true;
                        }
                    }
                }
            }
        }
        return did_place;
    }

    /**
     * A pathFindingot segíti elő, törli a már általa bejárt mezőket.
     */
    public void clearVisited() {
        for (int x=0;x<getWidthInTiles();x++) {
            for (int y=0;y<getHeightInTiles();y++) {
                visited[x][y] = false;
            }
        }
    }

    /**
     * Kiírja az aktuális állípotát a pályának
     */
    public void writeOut()
    {
        for (int x=0;x<getWidthInTiles();x++)
        {
            for (int y=0;y<getHeightInTiles();y++)
            {
                String s;
                switch (terrain.get(x).get(y))
                {
                    case ROAD:
                        s="R";
                        break;
                    case BORDER:
                        s="B";
                        break;
                    case EMPTY:
                        s="E";
                        break;
                    case ROLLER:
                        s="Ro";
                        break;
                    case CASTLE:
                        s="Ca";
                        break;
                    case FOOD:
                        s="F";
                        break;
                    case WATER:
                        s="W";
                        break;
                    case BUSH:
                        s="BU";
                        break;
                    case TRASH:
                        s="T";
                        break;
                    case CLEANER:
                        s="C";
                        break;
                    default:
                        s="O";
                        break;
                }
            }
        }
    }

    public boolean visited(int x, int y) {
        return visited[x][y];
    }

    public Tiles getTerrain(int x, int y) {
        return terrain.get(x).get(y);
    }

    public Buildable getUnit(int x, int y) {
        return units.get(x).get(y);
    }

    public void setUnit(int x, int y, Buildable unit) {
        units.get(x).set(y,unit);
    }

    /**
     *
     * @param mover The mover that is potentially moving through the specified
     * tile.
     * @param x The x coordinate of the tile to check
     * @param y The y coordinate of the tile to check
     * @return tud-e a bizonyos mezőre lépni a mover.
     */
    @Override
    public boolean blocked(Mover mover, int x, int y) {
        // if theres a unit at the location, then it's blocked
        if (getTerrain(x,y) == Tiles.ROAD ||
            getTerrain(x,y) == Tiles.ROLLER ||
            getTerrain(x,y) == Tiles.CASTLE ||
            getTerrain(x,y) == Tiles.FOOD ||
            getTerrain(x,y) == Tiles.WATER ||
            getTerrain(x,y) == Tiles.TRASH ||
            getTerrain(x,y) == Tiles.CLEANER ||
            getTerrain(x,y) == Tiles.MECHANIC ||
            getTerrain(x,y) == Tiles.TRASHCAN) {
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * terxtúrák beállítása.
     */
    public void texturesInit()
    {
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

        roller_texture = new Texture(Gdx.files.internal("roller.png"));
        castle_texture = new Texture(Gdx.files.internal("castle.png"));
        roller_broken_texture = new Texture(Gdx.files.internal("hullamvasut_rossz.png"));
        castle_broken = new Texture(Gdx.files.internal("castle_broken.png"));
        bush_texture = new Texture(Gdx.files.internal("bush.png"));
        hamburger_texture = new Texture(Gdx.files.internal("hamburger.png"));
        water_texture = new Texture(Gdx.files.internal("water.png"));
        trashcan_texture = new Texture(Gdx.files.internal("trashcan.png"));
        trash_texture = new Texture(Gdx.files.internal("trash.png"));
        cleanerHouse_texture = new Texture(Gdx.files.internal("cleanerhouse.png"));
        mechanicHouse_texture = new Texture(Gdx.files.internal("mechanichouse.png"));
        cleaner_texture = new Texture(Gdx.files.internal("cleaner.png"));
    }

    @Override
    public int getWidthInTiles()
    {
        return WIDTH;
    }

    @Override
    public int getHeightInTiles()
    {
        return WIDTH;
    }

    @Override
    public void pathFinderVisited(int x, int y)
    {
        visited[x][y] = true;
    }

    @Override
    public float getCost(Mover mover, int sx, int sy, int tx, int ty)
    {
        return 1;
    }
}