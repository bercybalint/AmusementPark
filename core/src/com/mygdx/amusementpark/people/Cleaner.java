package com.mygdx.amusementpark.people;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;
import com.mygdx.amusementpark.gui.GameMap;
import com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.PathFinder;
import com.mygdx.amusementpark.people.Person;

import java.util.Timer;

public class Cleaner extends Person implements Mover {

    private int delay = 1000;
    private int mood;
    private int maxMood = 100;
    public PathFinder finder;
    public Buildable destination;

    public Boolean isGoing = false;
    public Boolean isCleaning = false;

    public Cleaner(GameMap map,int x, int y, int width, int height, Texture texture, int window_h, int window_w) {
        super(map, x, y, width, height, texture, window_h, window_w);

    }

/*
public void cleanUp(){
    map.clearVisited();
    path = finder.findPath(this, (ind_x), (ind_y), p.x / 60, p.y / 40);

    if (path != null)
    {
        isGoing = true;
        isWaiting = false;
        System.out.println("Talaltam utat");
        //System.out.println("ide:" + destination.x / 60 + "," + destination.y / 40);
        pathLength = path.getLength();
        stepIndex = 0;
        currentStep = path.getStep(stepIndex);
        System.out.println("----------");
        for(int i = 0; i < pathLength; i++)
        {
            System.out.println(path.getStep(i).getX()+","+path.getStep(i).getY());
        }
        System.out.println("----------");
    } else {
        timer = new Timer();
        System.out.println("ujra probalom");
        timer.schedule(new Guest.personBehaviour(), 2000);

    }
}*/

}
