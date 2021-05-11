package com.mygdx.amusementpark.gui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.amusementpark.buildable.*;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.TileBasedMap;
import com.mygdx.amusementpark.people.Cleaner;
import com.mygdx.amusementpark.people.Mechanic;


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


    public Textures textures;
    boolean forTest;

    public GameMap(int window_h, int window_w, boolean forTest)
    {
        this.forTest = forTest;
        this.tile_width=window_w/WIDTH;
        this.tile_height=window_h/HEIGHT;

        textures = new Textures(forTest);
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
                    tile = new Road((i)*tile_width,(j)*tile_height,tile_width,tile_height,textures.road_down_to_up,30,type);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }
                else if(j==1 & i == 10)
                {
                    type = Tiles.ROAD;
                    tile = new Road((i)*tile_width,(j)*tile_height,tile_width,tile_height,textures.road_down_to_up,20,type);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }
                else if(j==1)
                {
                    if(i == 8 || i==12)
                    {
                        type = Tiles.BORDER;
                        tile = new Border((i)*tile_width,(j)*tile_height,tile_width,tile_height,textures.gate_texture,20,type);
                        units.get(i).add(tile);
                        terrain.get(i).add(type);
                    }
                    else if(i>8 && i<12)
                    {
                        type = Tiles.EMPTY;
                        tile = new Buildable((i)*tile_width,(j)*tile_height,tile_width,tile_height,textures.grass_texture,20,type,10);
                        units.get(i).add(tile);
                        terrain.get(i).add(type);
                    }
                    else
                    {
                        type = Tiles.BORDER;
                        tile = new Border((i)*tile_width,(j)*tile_height,tile_width,tile_height,textures.fence_texture,20,type);
                        units.get(i).add(tile);
                        terrain.get(i).add(type);
                    }

                }
                else if (i == 0|| i == WIDTH - 1 || j == HEIGHT - 1)
                {
                    type = Tiles.BORDER;
                    tile = new Border((i)*tile_width,(j)*tile_height,tile_width,tile_height,textures.wall_texture,10,type);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }
                else
                {
                    type = Tiles.EMPTY;
                    tile = new Buildable((i)*tile_width,(j)*tile_height,tile_width,tile_height,textures.grass_texture,10,type,10);
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
                        road.setTexture(textures.road_from_all);
                    }
                    else if(road.upNeigh & road.leftNeigh & road.rightNeigh) //3 irányból, felfele
                    {
                        road.setTexture(textures.road_threeway_to_up);
                    }
                    else if(road.rightNeigh & road.upNeigh & road.downNeigh) //3 irányból, jobbra
                    {
                        road.setTexture(textures.road_threeway_to_right);
                    }
                    else if(road.downNeigh & road.leftNeigh & road.rightNeigh) //3 irányból, lefele
                    {
                        road.setTexture(textures.road_threeway_to_down);
                    }
                    else if(road.leftNeigh & road.upNeigh & road.downNeigh) //3 irányból balra
                    {
                        road.setTexture(textures.road_threeway_to_left);
                    }
                    else if(road.upNeigh & road.downNeigh) //2 irány, fel-le
                    {
                        road.setTexture(textures.road_down_to_up);
                    }
                    else if(road.upNeigh & road.leftNeigh) //2irány, fel-balra
                    {
                        road.setTexture(textures.road_top_to_left);
                    }
                    else if(road.upNeigh & road.rightNeigh) //2irány, fel-jobbra
                    {
                        road.setTexture(textures.road_top_to_right);
                    }
                    else if(road.downNeigh & road.leftNeigh) //2irány, le-balra
                    {
                        road.setTexture(textures.road_down_to_left);
                    }
                    else if(road.downNeigh & road.rightNeigh) //2irány, le-jobbra
                    {
                        road.setTexture(textures.road_down_to_right);
                    }
                    else if(road.leftNeigh & road.rightNeigh) //2 irány, jobbra-balra
                    {
                        road.setTexture(textures.road_left_to_right);
                    }
                    else if(road.leftNeigh || road.rightNeigh)
                    {
                        road.setTexture(textures.road_left_to_right);
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
    public Buildable touched(Vector3 touch, Tiles chosen, Boolean isSelected)
    {
        Boolean did_place = false;
        Buildable actual = null;
        for(int i = 0; i < units.size; i++)
        {
            for(int j = 0; j < units.get(i).size; j++)
            {
                if(units.get(i).get(j).contains(touch.x,touch.y-100))
                {
                    if(isSelected==true)
                    {
                        if(units.get(i).get(j).getType()==Tiles.EMPTY)
                        {

                            Tiles type;
                            actual = new Buildable(units.get(i).get(j).x,
                                    units.get(i).get(j).y,
                                    units.get(i).get(j).width,
                                    units.get(i).get(j).height,textures.grass_texture,0, Tiles.EMPTY,0);

                            type = Tiles.ROAD;
                            switch (chosen)
                            {
                                // ROAD,BORDER,EMPTY,GAMES,STAFF,BUSH,TREE,TRASH,WATER,FOOD
                                case ROAD:
                                    actual = new Road(units.get(i).get(j).x,units.get(i).get(j).y, units.get(i).get(j).width,units.get(i).get(j).height, textures.road_down_to_up,10,Tiles.ROAD);
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
                                                    textures.roller_texture, 10, Tiles.ROLLER,5, textures.roller_texture,textures.roller_broken_texture);


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
                                                    textures.castle_texture, 10, Tiles.CASTLE,5,textures.castle_texture,textures.castle_broken);

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
                                                    textures.cleanerHouse_texture,10,Tiles.CLEANER);

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
                                                    textures.mechanicHouse_texture,10,Tiles.MECHANIC);

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
                                                    textures.hamburger_texture,10,Tiles.FOOD);

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
                                                    textures.bush_texture,10,Tiles.BUSH);

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
                                            textures.bush_texture,10,
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
                                                    textures.trashcan_texture,10,Tiles.TRASHCAN);

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
                                                    textures.water_texture,10,Tiles.WATER);

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
                                        units.get(i).get(j).height,textures.grass_texture,0, Tiles.EMPTY,0);
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
        return actual;
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
