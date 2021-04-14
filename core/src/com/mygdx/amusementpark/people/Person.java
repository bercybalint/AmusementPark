package com.mygdx.amusementpark.people;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Direction;
import com.mygdx.amusementpark.buildable.Tiles;
import com.mygdx.amusementpark.gui.GameMap;
import com.mygdx.amusementpark.gui.GameScreen;
import com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.Path;
import com.mygdx.amusementpark.pathfinding.PathFinder;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Person extends Rectangle implements Mover
{
    /**
     * Create a new mover to be used while path finder
     */
    private GameMap map;
    private Texture texture;
    private Direction dir = Direction.UP;
    private Timer timer;
    private int delay = 20000;
    private int speed = 2;

    /** The path finder we'll use to search our map */
    private PathFinder finder;
    /** The last path found for the current unit */
    private Path path;
    private int pathLength;
    int window_h;
    int window_w;
    int tile_width;
    int tile_height;

    public Person(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w)
    {
        this.map = map;
        this.width=width;
        this.height = height;
        this.texture = texture;
        this.window_h=window_h;
        this.window_w=window_w;
        this.tile_width=window_w/20;
        this.tile_height=window_h/20;
        this.x = 10*tile_width;
        this.y = 2*tile_height;
        timer = new Timer();
        timer.schedule(new personBehaviour(), 0, delay);
        finder = new AStarPathFinder(map, 2000, false);

    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setMap(GameMap map)
    {
        this.map = map;
    }

    public Point findDestination()
    {
        Point p = new Point();
        for(int i = 0; i < map.terrain.size; i++)
        {
            for (int j = 0; j < map.terrain.get(i).size; j++)
            {
                if(map.terrain.get(i).get(j) == Tiles.GAMES)
                {
                    p.x=i;
                    p.y=j;
                }
                String s;
                switch (map.terrain.get(i).get(j))
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
                    default:
                        s="O";
                        break;
                }
                System.out.print(s);
            }
            System.out.println();
        }
        return p;
    }
    public void move()
    {

        switch (dir)
        {
            case UP:
                y+=speed;
                break;
            case DOWN:
                y-=speed;
                break;
            case LEFT:
                x-=speed;
                break;
            case RIGHT:
                x+=speed;
                break;
            case NOTHING:
                x=x;
                y=y;
                break;
            default:break;
        }
    }

    public void behaviour()
    {
        Point destination = findDestination();
        map.clearVisited();

        path = finder.findPath(this,(x+50)/60,(y/40)+1,destination.x,destination.y);
        System.out.println(1/40);
        System.out.println((x+50)/60);


        if(path!=null)
        {
            pathLength = path.getLength();
            for(int i = 0; i < path.getLength(); i++)
            {
                int x = path.getStep(i).getX();
                int y = path.getStep(i).getY();
                System.out.println("x:"+x+",y:"+y);
            }
            System.out.println("találtam utat");
            System.out.println(path);
        }
        else
        {
            System.out.println("nem talalt");
        }
        timer.cancel();
        timer.purge();
    }

    class personBehaviour extends TimerTask
    {
        public void run() {
            System.out.println("Person Timer");
            behaviour();

            //timer.cancel(); //Terminate the timer thread
        }
    }

}
