package com.mygdx.amusementpark.people;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;
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
    private int delay = 1000;
    private Random rand;
    private int mood;
    private int maxMood=100;

    Timer behavTimer;

    Texture happy_texture;
    Texture annoyed_texture;
    Texture angry_texture;


    public Guest(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w, Texture happy, Texture annoyed, Texture angry)
    {
        super(map, x, y, width, height, texture, window_h, window_w);
        finder = new AStarPathFinder(map, 2000, false);
        timer = new Timer();
        timer.schedule(new moodTimer(),0,delay);

        behavTimer = new Timer();
        behavTimer.schedule(new personBehaviour(), 0);

        this.mood = this.maxMood;
        this.happy_texture=happy;
        this.annoyed_texture=annoyed;
        this.angry_texture=angry;
        this.texture=happy_texture;
    }

    public void goToNewPlace()
    {
        Random random = new Random();
        int randInt = random.nextInt(map.destinationPoints.size);
        System.out.println(map.destinationPoints.size);
        System.out.println(randInt);

        for (int i = 0; i<map.destinationPoints.size; i++) {
            System.out.println(map.destinationPoints.get(i));
        }

        //itt valami nem jo => rosszul addolok a tombbe
        Buildable destination = map.destinationPoints.get(randInt);
        map.clearVisited();
        map.writeOut();
        path = finder.findPath(this,(ind_x),(ind_y),destination.x/60,destination.y/40);

        if(path!=null)
        {
            pathLength = path.getLength();
            currentStep=path.getStep(stepIndex);
        }
        else
        {
            //System.out.println("nem talalt");
        }

    }
    public void behaviour()
    {
        goToNewPlace();
    }

    public void moodChange()
    {
        mood--;
        if(mood>=70)
        {
            this.texture=happy_texture;
        }
        else if(mood>=20 && mood<=50)
        {
            this.texture=annoyed_texture;
        }
        else if(mood<20) {
            this.texture = angry_texture;
        }
        if(mood<=0)
        {
            //leave the park
        }
        System.out.println(mood);
    }

    class personBehaviour extends TimerTask
    {
        public void run() {
            //System.out.println("Person Timer");
            behaviour();

            //timer.cancel(); //Terminate the timer thread
        }
    }
    class moodTimer extends TimerTask
    {
        public void run() {
            moodChange();
        }
    }


    public int getMood() {
        return mood;
    }
}
