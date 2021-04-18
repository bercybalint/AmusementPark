package com.mygdx.amusementpark.people;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.gui.GameMap;
import com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import com.mygdx.amusementpark.pathfinding.Mover;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Guest extends Person implements Mover
{

    private Timer timer;
    private int delay = 50000;

    public Guest(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w)
    {
        super(map, x, y, width, height, texture, window_h, window_w);
        finder = new AStarPathFinder(map, 2000, false);
        timer = new Timer();
        timer.schedule(new personBehaviour(), 0, delay);
    }

    public void behaviour()
    {
        Random random = new Random();
        int randInt = random.nextInt(map.destinationPoints.size);
        System.out.println(map.destinationPoints.size);
        System.out.println(randInt);
        //System.out.println(map.destinationPoints.get(randInt));

        for (int i = 0; i<map.destinationPoints.size; i++) {
            System.out.println(map.destinationPoints.get(i));
        }

        //itt valami nem jo => rosszul addolok a tombbe
        Point destination = map.destinationPoints.get(randInt);
        map.clearVisited();
        map.writeOut();
        path = finder.findPath(this,(ind_x),(ind_y),destination.x,destination.y);

        if(path!=null)
        {
            pathLength = path.getLength();
            currentStep=path.getStep(stepIndex);
        }
        else
        {
            //System.out.println("nem talalt");
        }
        timer.cancel();
        timer.purge();
    }

    class personBehaviour extends TimerTask
    {
        public void run() {
            //System.out.println("Person Timer");
            behaviour();

            //timer.cancel(); //Terminate the timer thread
        }
    }
}
