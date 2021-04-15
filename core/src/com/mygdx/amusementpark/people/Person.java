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
    int ind_x=10;
    int ind_y=1;

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
        this.x = ind_x*tile_width;
        this.y = ind_y*tile_height;
        finder = new AStarPathFinder(map, 2000, false);
        timer = new Timer();
        timer.schedule(new personBehaviour(), 0, delay);
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

            int go_to_x = currentStep.getX()*tile_width;
            int go_to_y = currentStep.getY()*tile_height;


            System.out.println();
            System.out.println("actual_position:" + x + " , " + y);
            System.out.println("coordinates:" + curr_x + " , " + curr_y);
            System.out.println("step:" + currentStep.getX() + " , " + currentStep.getY());

            if (x < go_to_x && y == go_to_y)
            {
                dir = Direction.RIGHT;
            }
            if (x > go_to_x && y == go_to_y)
            {
                dir = Direction.LEFT;
            }
            if (x == go_to_x && y > go_to_y)
            {
                dir = Direction.DOWN;
            }
            if (x == go_to_x && y < go_to_y)
            {
                dir = Direction.UP;
            }
            if (x == go_to_x && y == go_to_y)
            {
                if (stepIndex == pathLength-1)
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
        map.writeOut();
        path = finder.findPath(this,(ind_x),(ind_y),destination.x,destination.y);

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

    public Texture getTexture()
    {
        return texture;
    }

    public void setMap(GameMap map)
    {
        this.map = map;
    }

}
