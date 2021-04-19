package com.mygdx.amusementpark.people;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;
import com.mygdx.amusementpark.buildable.Direction;
import com.mygdx.amusementpark.gui.GameMap;
import com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.PathFinder;

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
    public PathFinder finder;
    Buildable destination;

    Timer moodTimer;

    Texture happy_texture;
    Texture annoyed_texture;
    Texture angry_texture;

    public Boolean isGoing = false;
    public Boolean isWaiting = false;


    public Guest(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w, Texture happy, Texture annoyed, Texture angry)
    {
        super(map, x, y, width, height, texture, window_h, window_w);
        this.moodTimer = new Timer();
        this.moodTimer.schedule(new moodTask(),0,delay);

        this.mood = this.maxMood;
        this.happy_texture=happy;
        this.annoyed_texture=annoyed;
        this.angry_texture=angry;
        this.texture=happy_texture;
        finder = new AStarPathFinder(map, 2000, false);
        behaviour();
    }

    public void gainMood(int moodGain)
    {
        this.mood+=moodGain;
        if(this.mood>=maxMood)
        {
            this.mood = maxMood;
        }
    }

    public void goToNewPlace()
    {
        Random random = new Random();
        int randInt = 0;
        if(map.destinationPoints.size>0) {
            randInt = random.nextInt(map.destinationPoints.size);


            destination = map.destinationPoints.get(randInt);
            System.out.println("db:" + map.destinationPoints.size);
            map.clearVisited();
            path = finder.findPath(this, (ind_x), (ind_y), destination.x / 60, destination.y / 40);
        }
        if(path!=null)
        {
            isGoing=true;
            isWaiting=false;
            System.out.println("new path");
            System.out.println("ide:"+destination.x/60+","+destination.y/40);
            pathLength = path.getLength();
            stepIndex=0;
            currentStep=path.getStep(stepIndex);
        }
        else
        {
            timer=new Timer();
            System.out.println("ujra probalom");
            timer.schedule(new personBehaviour(), 2000);

        }

    }
    public void behaviour()
    {
        goToNewPlace();
    }

    public void reachedDestination(int time_length)
    {
        timer=new Timer();
        System.out.println("varok: "+time_length*1000);
        timer.schedule(new personBehaviour(), time_length*1000);
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
    }

    @Override
    public void move()
    {
        if(path!=null)
        {
            int go_to_x = currentStep.getX()*tile_width;
            int go_to_y = currentStep.getY()*tile_height;
            System.out.println("kovi lepesre");
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
                    isGoing=false;
                    path=null;
                }
                else if (stepIndex < pathLength - 1)
                {
                    stepIndex++;
                    currentStep = path.getStep(stepIndex);
                    dir = Direction.NOTHING;
                    ind_x = destination.x/60;
                    ind_y = destination.y/40;
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
        if(path==null)
        {
            isGoing=false;
        }
    }

    public int getMood() {
        return mood;
    }

    class personBehaviour extends TimerTask
    {
        public void run() {
            System.out.println("Utat keresek");
            behaviour();
            //timer.cancel(); //Terminate the timer thread
        }
    }
    class moodTask extends TimerTask
    {
        public void run() {
            moodChange();
        }
    }
}
