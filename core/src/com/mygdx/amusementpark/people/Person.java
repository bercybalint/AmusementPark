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
    private Direction dir = Direction.NOTHING;
    private Timer timer;
    private int delay = 20000;

    /** The path finder we'll use to search our map */
    private PathFinder finder;
    /** The last path found for the current unit */
    private Path path;

    public Person(GameMap map, int x, int y, int width, int height, Texture texture)
    {
        this.map = map;
        this.x = 628;
        this.y = 159;
        this.width=width;
        this.height = height;
        this.texture = texture;
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
            System.out.println();
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
        }
        return p;
    }

    public void behaviour()
    {
        Point destination = findDestination();
        map.clearVisited();

        path = finder.findPath(this,11,1,destination.x,destination.y);

        if(path!=null)
        {
            for(int i = 0; i < path.getLength(); i++)
            {
                int x = path.getStep(i).getX();
                int y = path.getStep(i).getY();
                System.out.println("x:"+x+",y:"+y);
            }
            System.out.println("találtam utat");
            System.out.println(path);
        }
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
