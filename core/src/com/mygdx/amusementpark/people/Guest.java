package com.mygdx.amusementpark.people;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.*;
import com.mygdx.amusementpark.gui.GameMap;
import com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.Path;
import com.mygdx.amusementpark.pathfinding.PathFinder;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Guest extends Person implements Mover {

    private Timer timer;
    private int delay = 1000;
    private Random rand;
    private int mood;
    private int maxMood = 100;
    public PathFinder finder;
    public Buildable destination;

    Timer moodTimer;

    Texture happy_texture;
    Texture annoyed_texture;
    Texture angry_texture;

    Texture trash_texture;

    public Boolean isGoing = false;
    public Boolean isWaiting = false;



    public Guest(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w, Texture happy, Texture annoyed, Texture angry, Texture trash) {
        super(map, x, y, width, height, texture, window_h, window_w);
        this.moodTimer = new Timer();
        this.moodTimer.schedule(new moodTask(), 0, delay);

        this.mood = this.maxMood;
        this.happy_texture = happy;
        this.annoyed_texture = annoyed;
        this.angry_texture = angry;
        this.texture = happy_texture;
        this.trash_texture=trash;
        finder = new AStarPathFinder(map, 200, false);

        behaviour();
    }

    public void gainMood(int moodGain) {
        this.mood += moodGain;
        if (this.mood >= maxMood) {
            this.mood = maxMood;
        }
    }

    public void goHere(Point p)
    {
        map.clearVisited();
        path = finder.findPath(this, (ind_x), (ind_y), p.x / 60, p.y / 40);

        if (path != null)
        {
            isGoing = true;
            isWaiting = false;
            System.out.println("new path");
            System.out.println("ide:" + destination.x / 60 + "," + destination.y / 40);
            pathLength = path.getLength();
            stepIndex = 0;
            currentStep = path.getStep(stepIndex);
            System.out.println("path:");
            for(int i = 0; i < pathLength; i++)
            {
                System.out.println(path.getStep(i).getX()+","+path.getStep(i).getY());
            }
        } else {
            timer = new Timer();
            System.out.println("ujra probalom");
            timer.schedule(new personBehaviour(), 2000);

        }

    }
    public void goToNewPlace() {
        Random random = new Random();
        int randInt = 0;
        if (map.destinationPoints.size > 0)
        {
            randInt = random.nextInt(map.destinationPoints.size);
            destination = map.destinationPoints.get(randInt);
            System.out.println("db:" + map.destinationPoints.size);
            Point p = new Point(destination.x,destination.y);
            goHere(p);
        }
    }

    public void behaviour() {
        goToNewPlace();
    }

    public void reachedDestination(int time_length) {
        timer = new Timer();
        System.out.println("varok: " + time_length * 1000);
        timer.schedule(new personBehaviour(), time_length * 1000);
    }

    public void moodChange() {
        mood--;
        if (mood >= 70) {
            this.texture = happy_texture;
        } else if (mood >= 20 && mood <= 50) {
            this.texture = annoyed_texture;
        } else if (mood < 20) {
            this.texture = angry_texture;
        }
        if (mood <= 0) {
            //leave the park
        }
    }

    @Override
    public void move() {
        if (path != null) {
            System.out.println("mozgok");
            int go_to_x = currentStep.getX() * tile_width;
            int go_to_y = currentStep.getY() * tile_height;
            if (x < go_to_x && y == go_to_y) {
                dir = Direction.RIGHT;
            }
            if (x > go_to_x && y == go_to_y) {
                dir = Direction.LEFT;
            }
            if (x == go_to_x && y > go_to_y) {
                dir = Direction.DOWN;
            }
            if (x == go_to_x && y < go_to_y) {
                dir = Direction.UP;
            }
            if (x == go_to_x && y == go_to_y) {
                if (stepIndex == pathLength - 1) {
                    dir = Direction.NOTHING;
                    isGoing = false;
                    path = null;
                } else if (stepIndex < pathLength - 1) {
                    stepIndex++;
                    currentStep = path.getStep(stepIndex);
                    dir = Direction.NOTHING;
                    ind_x = x / 60;
                    ind_y = y / 40;

                    Random to_trash_r = new Random();
                    int to_trash = to_trash_r.nextInt(15);
                    if (to_trash == 9) {
                        System.out.println("keresek kukat");
                        Point trash_p = findTrash();
                        System.out.println("legközelebbi kuka "+trash_p.x+","+trash_p.y);

                        double distance = Math.sqrt((y - trash_p.y) * (y - trash_p.y) + (x - trash_p.x) * (x - trash_p.x));
                        System.out.println("kuka tavolsage"+distance);
                        if(distance<300)
                        {
                            System.out.println("van a kozelben kuka");
                            path = null;
                            goHere(trash_p);
                        }
                        else
                        {
                            System.out.println("Szemetelek");
                            map.trashes.add(new Trash(x,y,10,10,trash_texture,0,Tiles.TRASH));
                            //ide kell valahogy hívni a takarítót
                        }
                    }
                }
            }
            switch (dir) {
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
        if (path == null) {
            isGoing = false;
        }
    }

    public Point findTrash()
    {
        double minDistance = 10000;
        Point trash_point = new Point(-100,-100);

        if(map.trashCans.size>0)
        {
            for(int i = 0; i < map.trashCans.size; i++) {
                double y2 = map.trashCans.get(i).y;
                double y1 = y;

                double x2 = map.trashCans.get(i).x;
                double x1 = x;
                double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

                if(minDistance>distance)
                {
                    minDistance=distance;
                    trash_point = new Point(map.trashCans.get(i).x,map.trashCans.get(i).y);
                }
                System.out.println("tavolsag:"+distance);
            }
        }

        return trash_point;
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
