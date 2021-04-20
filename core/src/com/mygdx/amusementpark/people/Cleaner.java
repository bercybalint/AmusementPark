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

public class Cleaner extends Person implements Mover {

    private int delay = 1000;
    public PathFinder finder;
    public Buildable destination;

    public Boolean isGoing = false;
    public Boolean isCleaning = false;

    Array<Trash> trashes = new Array<Trash>();

    public Cleaner(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w) {
        super(map, x, y, width, height, texture, window_h, window_w);

    }

    public void addTrash(Trash t) {
        trashes.add(t);
    }


    public void goHere(Point p) {

        map.clearVisited();
        path = finder.findPath(this, (ind_x), (ind_y), p.x / 60, p.y / 40);

        if (path != null) {
            isGoing = true;
            isCleaning = true;

            System.out.println("Talaltam utat a takaritashoz");
            //System.out.println("ide:" + destination.x / 60 + "," + destination.y / 40);
            pathLength = path.getLength();
            stepIndex = 0;
            currentStep = path.getStep(stepIndex);
            System.out.println("----------");
            for (int i = 0; i < pathLength; i++) {
                System.out.println(path.getStep(i).getX() + "," + path.getStep(i).getY());
            }
            System.out.println("----------");
        }
    }

public void Cleaning(){
        for(int i = 0; i<trashes.size;i++){
            goHere(new Point((int)trashes.get(i).getX(),(int)trashes.get(i).getY()));
        }
        //move meghivasa??
        //szemétfelvevő metódus, kitörli a map.trashes-ből, saját trashes-ből és textura vált
}

}
