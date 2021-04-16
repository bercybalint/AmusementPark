package com.mygdx.amusementpark.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.amusementpark.buildable.*;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.TileBasedMap;
import com.mygdx.amusementpark.people.Person;

import java.awt.*;
import java.util.Vector;

public class GameMap implements TileBasedMap
{
    /** The map width in tiles */
    public static final int WIDTH = 20;
    /** The map height in tiles */
    public static final int HEIGHT = 20;


    /** The terrain settings for each tile in the map */
    public Array<Array<Tiles>> terrain = new Array<Array<Tiles>>();


    /** The unit in each tile of the map */
    public Array<Array<Buildable>> units = new Array<Array<Buildable>>();

    /** The array of all destination points   */
    public Array<Point> destinationPoints = new Array<Point>();
    Point p = new Point();

    /** Indicator if a given tile has been visited during the search */
    public boolean[][] visited = new boolean[WIDTH][HEIGHT];

    int tile_width;
    int tile_height;

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

    Camera camera;

    public GameMap(int window_h, int window_w, Camera camera)
    {
        this.tile_width=window_w/WIDTH;
        this.tile_height=window_h/HEIGHT;

        texturesInit();
        mapInit();
    }

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
                    type = Tiles.TREE;
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
                        tile = new Buildable((i)*tile_width,(j)*tile_height,tile_width,tile_height,grass_texture,20,type);
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
                    tile = new Buildable((i)*tile_width,(j)*tile_height,tile_width,tile_height,grass_texture,10,type);
                    units.get(i).add(tile);
                    terrain.get(i).add(type);
                }

            }
        }
    }

    /**
     *
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
                            actual = new Road(units.get(i).get(j).x,units.get(i).get(j).y, units.get(i).get(j).width,units.get(i).get(j).height, road_down_to_up,10,Tiles.ROAD);
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
                                case GAMES:
                                    type = Tiles.GAMES;

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
                                                                korhinta_texture, 10, Tiles.GAMES);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;
                                            destinationPoints.add(new Point(p.x, p.y));

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
                                case STAFF:
                                    type = Tiles.STAFF;

                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new StaffBuilding(
                                                                units.get(i).get(j).x,
                                                                units.get(i).get(j).y,
                                                                units.get(i).get(j).width,
                                                                units.get(i).get(j).height,
                                                                staff_texture,10,Tiles.STAFF);


                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;
                                            destinationPoints.add(new Point(p.x, p.y));

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
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
                                            destinationPoints.add(new Point(p.x, p.y));

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
                                            destinationPoints.add(new Point(p.x, p.y));

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
                                        }
                                    }
                                    break;
                                case TREE:
                                    actual = new Park(units.get(i).get(j).x,units.get(i).get(j).y, units.get(i).get(j).width,units.get(i).get(j).height,bush_texture,10,Tiles.TREE);
                                    type = Tiles.TREE;
                                    break;
                                case TRASH:

                                    type = Tiles.TRASH;
                                    if((i>0 && j>0))
                                    {
                                        if (units.get(i).get(j).getType() == Tiles.EMPTY) {
                                            actual = new Park(
                                                    units.get(i).get(j).x,
                                                    units.get(i).get(j).y,
                                                    units.get(i).get(j).width,
                                                    units.get(i).get(j).height,
                                                    trash_texture,10,Tiles.TRASH);

                                            p.x = units.get(i).get(j).x/units.get(i).get(j).width;
                                            p.y = units.get(i).get(j).y/units.get(i).get(j).height;
                                            destinationPoints.add(new Point(p.x, p.y));

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
                                            destinationPoints.add(new Point(p.x, p.y));

                                            units.get(i).set(j, actual);
                                            terrain.get(i).set(j, type);
                                        }
                                    }
                                    break;
                                default: actual = new Road(units.get(i).get(j).x,units.get(i).get(j).y, units.get(i).get(j).width,units.get(i).get(j).height,road_down_to_up,10,Tiles.TREE);
                                    type = Tiles.ROAD;

                            }
                            units.get(i).set(j,actual);
                            terrain.get(i).set(j,type);
                            checkRoadNeighbours();

                        }
                        did_place = true;

                    }
                }
                else{
                    //Gdx.app.setLogLevel(Application.LOG_DEBUG);
                    //Gdx.app.debug("POSITION", "X touched: " + touch.x + " Y touched: " + touch.y);
                }
            }
        }
        return did_place;
    }

    public void clearVisited() {
        for (int x=0;x<getWidthInTiles();x++) {
            for (int y=0;y<getHeightInTiles();y++) {
                visited[x][y] = false;
            }
        }
    }

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
                    case GAMES:
                        s="G";
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
                    case STAFF:
                        s="S";
                        break;
                    default:
                        s="O";
                        break;
                }
                //System.out.print(s);
            }
            //System.out.println();
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

    @Override
    public boolean blocked(Mover mover, int x, int y) {
        // if theres a unit at the location, then it's blocked
        if (getTerrain(x,y) == Tiles.ROAD ||
            getTerrain(x,y) == Tiles.GAMES ||
            getTerrain(x,y) == Tiles.FOOD ||
            getTerrain(x,y) == Tiles.BUSH ||
            getTerrain(x,y) == Tiles.WATER ||
            getTerrain(x,y) == Tiles.TRASH ||
            getTerrain(x,y) == Tiles.STAFF) {
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


        korhinta_texture = new Texture(Gdx.files.internal("korhinta.png"));
        bush_texture = new Texture(Gdx.files.internal("bush.png"));
        hamburger_texture = new Texture(Gdx.files.internal("hamburger.png"));
        water_texture = new Texture(Gdx.files.internal("water.png"));
        trash_texture = new Texture(Gdx.files.internal("trashcan.png"));
        staff_texture = new Texture(Gdx.files.internal("staff.png"));

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
