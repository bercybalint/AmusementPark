package com.mygdx.amusementpark.people;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;
import com.mygdx.amusementpark.buildable.Direction;
import com.mygdx.amusementpark.buildable.Tiles;
import com.mygdx.amusementpark.gui.GameMap;
import com.mygdx.amusementpark.gui.GameScreen;
import com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.Path;
import com.mygdx.amusementpark.pathfinding.PathFinder;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

abstract class Person extends Rectangle implements Mover
{
    /**
     * Create a new mover to be used while path finder
     */
    public GameMap map;
    public Texture texture;
    public Direction dir = Direction.NOTHING;
    public int speed = 1;

    /** The path finder we'll use to search our map */
    public PathFinder finder;
    /** The last path found for the current unit */
    public Path path;
    public int pathLength;
    public int stepIndex=0;
    public Path.Step currentStep;
    public Point p;

    int window_h;
    int window_w;
    int tile_width;
    int tile_height;
    int ind_x=10;
    int ind_y=1;

    private Timer timer;
    public Buildable destination;

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
    }

    public void move()
    {
        if(path!=null)
        {
            int go_to_x = currentStep.getX()*tile_width;
            int go_to_y = currentStep.getY()*tile_height;

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



    public Texture getTexture()
    {
        return texture;
    }

    public void setMap(GameMap map)
    {
        this.map = map;
    }



}
