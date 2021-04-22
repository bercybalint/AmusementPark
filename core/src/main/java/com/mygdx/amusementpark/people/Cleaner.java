package com.mygdx.amusementpark.people;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;
import com.mygdx.amusementpark.buildable.Direction;
import com.mygdx.amusementpark.buildable.Trash;
import com.mygdx.amusementpark.gui.GameMap;
import com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.PathFinder;
import com.mygdx.amusementpark.people.Person;
import com.badlogic.gdx.utils.Array;


import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Cleaner extends Person implements Mover {

    private int delay = 1000;
    public PathFinder finder;
    public Buildable destination;

    /**
     * éppen megy-e valahova
     */
    public Boolean isGoing = false;

    /**
     * éppen takarít-e
     */
    public Boolean isCleaning = false;

    /**
     * A szemetek amiket össze kell szednie
     */
    public Array<Trash> trashes = new Array<Trash>();
    public Timer timer;


    /**
     *
     * @param map
     * @param x
     * @param y
     * @param width
     * @param height
     * @param texture
     * @param window_h
     * @param window_w
     * @param trashes - szemetek, amiket a pályától kap meg, ha ő az első takarító
     */
    public Cleaner(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w,Array<Trash> trashes ) {
        super(map, x, y, width, height, texture, window_h, window_w);
        this.trashes = trashes;
        finder = new AStarPathFinder(map, 2000, false);
        if(trashes.size>0)
        {
            goHere(new Point(trashes.get(0).x,trashes.get(0).y));
        }
        timer = new Timer();
        timer.schedule(new cleanerBehaviour(), 0,2000);
    }

    public void addTrash(Trash t)
    {
        trashes.add(t);
    }

    /**
     *
     * @param p - erre a pontra menjen a takarító
     */
    public void goHere(Point p) {
        this.p = p;
        if(!isCleaning)
        {
            map.clearVisited();
            path = finder.findPath(this, (ind_x), (ind_y), p.x / 60, p.y / 40);

            if (path != null)
            {
                isGoing = true;
                isCleaning = true;

                System.out.println("Talaltam utat a takaritashoz");
                pathLength = path.getLength();
                stepIndex = 0;
                currentStep = path.getStep(stepIndex);
            /*System.out.println("----------");
            for (int i = 0; i < pathLength; i++) {
                System.out.println(path.getStep(i).getX() + "," + path.getStep(i).getY());
            }*/
                System.out.println("----------");
            } else
            {
                System.out.println("nem talaltam utat a szemethez");
            }
        }
        else
        {
            System.out.println("nem talaltam utat a szemethez");
        }
    }

    /**
     * mindig menjen az első elemhez a szemét tömbből
     */
    class cleanerBehaviour extends TimerTask
    {
        public void run() {
            if(trashes.size>0)
            {
                goHere(new Point(trashes.get(0).x,trashes.get(0).y));
            }

        }
    }
}
