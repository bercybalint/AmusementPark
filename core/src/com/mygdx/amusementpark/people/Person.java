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
    private int speed = 2;

    /** The path finder we'll use to search our map */
    private PathFinder finder;
    /** The last path found for the current unit */
    private Path path;
    private int pathLength;
    private int stepIndex=0;
    private Path.Step currentStep;
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
        this.y = 3*tile_height;
        finder = new AStarPathFinder(map, 2000, false);
        timer = new Timer();
        timer.schedule(new personBehaviour(), 0, delay);

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
            }
        }
        return p;
    }
    public void move()
    {
        if(path!=null)
        {
            int curr_x = (x / tile_width);
            int curr_y = (y / tile_height);
            System.out.println();
            System.out.println("actual_position:" + x + " , " + y);
            System.out.println("coordinates:" + curr_x + " , " + curr_y);
            System.out.println("step:" + currentStep.getX() + " , " + currentStep.getY());
            if (curr_x < currentStep.getX() && curr_y == currentStep.getY())
            {
                dir = Direction.RIGHT;
            }
            if (curr_x > currentStep.getX() && curr_y == currentStep.getY())
            {
                dir = Direction.LEFT;
            }
            if (curr_x == currentStep.getX() && curr_y > currentStep.getY())
            {
                dir = Direction.DOWN;
            }
            if (curr_x == currentStep.getX() && curr_y < currentStep.getY())
            {
                dir = Direction.UP;
            }
            if (curr_x == currentStep.getX() && curr_y == currentStep.getY())
            {
                if (stepIndex == 14)
                {
                    dir = Direction.NOTHING;
                } else if (stepIndex < pathLength - 1)
                {
                    stepIndex++;
                    currentStep = path.getStep(stepIndex);
                    dir = Direction.NOTHING;
                }

            }
            switch (dir)
            {
                case UP:
                    y += speed;
                    break;
                case DOWN:
                    y -= speed;
                    break;
                case LEFT:
                    x -= speed;
                    break;
                case RIGHT:
                    x += speed;
                    break;
                case NOTHING:
                    x = x;
                    y = y;
                    break;
                default:
                    break;
            }
        }
    }

    public void behaviour()
    {
        Point destination = findDestination();
        map.clearVisited();

        path = finder.findPath(this,(x/tile_width),(y/tile_height),destination.x,destination.y);

        if(path!=null)
        {
            pathLength = path.getLength();
            currentStep=path.getStep(stepIndex);
            System.out.println("A talalt ut:");

            for(int i =0; i < pathLength; i++)
            {
                System.out.println(path.getStep(i).getX() + " ," +path.getStep(i).getY());
            }
            System.out.println("A talalt ut vege");

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
