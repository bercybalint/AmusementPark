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

public class Guest extends Person implements Mover
{

    private Timer timer;
    private Timer parkTimer;
    private int delay = 1000;
    private int mood;
    private int maxMood = 100;
    public PathFinder finder;

    Timer moodTimer;

    Texture happy_texture;
    Texture annoyed_texture;
    Texture angry_texture;

    Texture trash_texture;

    public Boolean isGoing = false;
    public Boolean isWaiting = false;
    public Boolean throwingTrash = false;

    public Guest(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w, Texture happy, Texture annoyed, Texture angry, Texture trash)
    {
        super(map, x, y, width, height, texture, window_h, window_w);
        this.moodTimer = new Timer();
        this.moodTimer.schedule(new moodTask(), 0, delay);
        this.parkTimer = new Timer();
        this.parkTimer.schedule(new parkBehaviour(),0,1000);
        this.mood = this.maxMood;
        this.happy_texture = happy;
        this.annoyed_texture = annoyed;
        this.angry_texture = angry;
        this.texture = happy_texture;
        this.trash_texture = trash;
        finder = new AStarPathFinder(map, 200, false);

        behaviour();
    }

    public void gainMood(int moodGain)
    {
        this.mood += moodGain;
        if (this.mood >= maxMood)
        {
            this.mood = maxMood;
        }
    }

    public void goHere(Point p)
    {
        this.p = p;
        map.clearVisited();
        path = finder.findPath(this, (ind_x), (ind_y), p.x / 60, p.y / 40);

        if (path != null)
        {
            isGoing = true;
            //System.out.println("ide:" + destination.x / 60 + "," + destination.y / 40);
            pathLength = path.getLength();
            stepIndex = 0;
            currentStep = path.getStep(stepIndex);
            /*System.out.println("----------");
            for (int i = 0; i < pathLength; i++)
            {
                System.out.println(path.getStep(i).getX() + "," + path.getStep(i).getY());
            }
            System.out.println("----------");*/
        } else
        {
            timer = new Timer();
            //System.out.println("ujra probalom");
            timer.schedule(new Guest.personBehaviour(), 2000);
        }
    }

    public void goToNewPlace()
    {
        Random random = new Random();
        int randInt = 0;
        if (map.destinationPoints.size > 0)
        {
            randInt = random.nextInt(map.destinationPoints.size);
            destination = map.destinationPoints.get(randInt);

            Point p = new Point(destination.x, destination.y);
            goHere(p);
        } else
        {
            timer = new Timer();
            timer.schedule(new Guest.personBehaviour(), 2000);
        }

    }

    public void behaviour()
    {
        goToNewPlace();
    }

    public void reachedDestination(int time_length)
    {
        timer = new Timer();
        timer.schedule(new personBehaviour(), time_length * 1000);
    }

    public void moodChange()
    {
        mood--;
        if (mood >= 70)
        {
            this.texture = happy_texture;
        } else if (mood >= 20 && mood <= 50)
        {
            this.texture = annoyed_texture;
        } else if (mood < 20)
        {
            this.texture = angry_texture;
        }
        if (mood <= 0)
        {
            //leave the park
        }
    }

    @Override
    public void move()
    {
        if (path != null)
        {
            int go_to_x = currentStep.getX() * tile_width;
            int go_to_y = currentStep.getY() * tile_height;
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
                if (stepIndex == pathLength - 1)
                {
                    dir = Direction.NOTHING;
                    isGoing = false;
                    path = null;
                } else if (stepIndex < pathLength - 1)
                {
                    stepIndex++;
                    currentStep = path.getStep(stepIndex);
                    dir = Direction.NOTHING;
                    ind_x = x / 60;
                    ind_y = y / 40;

                    Random to_trash_r = new Random();
                    if (!throwingTrash) ;
                    {
                        int to_trash = to_trash_r.nextInt(15);
                        if (to_trash == 9)
                        {
                            throwingTrash = true;
                            Point trash_p = findTrashCan();
                            System.out.println("Kidobom a szemet");

                            double distance = Math.sqrt((y - trash_p.y) * (y - trash_p.y) + (x - trash_p.x) * (x - trash_p.x));
                            if (distance < 300)
                            {
                                goHere(trash_p);
                            } else
                            {
                                System.out.println("Szemetelek");
                                map.trashes.add(new Trash(x, y, 10, 10, trash_texture, 0, Tiles.TRASH));
                                throwingTrash = false;
                                int cleanerIndex = findCleaner(new Point(x,y));
                                if(cleanerIndex>=0)
                                {
                                    map.cleaners.get(cleanerIndex).addTrash(new Trash(x, y, 10, 10, trash_texture, 0, Tiles.TRASH));
                                }

                            }
                        }
                    }
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
        if (path == null)
        {
            isGoing = false;
        }
    }

    private void cleanUP() {
    }

    public Boolean isParkNearby()
    {
        Boolean retu = false;
        for(int i = 0; i< map.terrain.size; i++)
        {
            for(int j = 0; j < map.terrain.get(i).size; j++)
            {
                if(map.terrain.get(i).get(j)==Tiles.BUSH||map.terrain.get(i).get(j)==Tiles.TREE)
                {
                    double distance = Math.sqrt(((j*40) - y) * ((j*40) - y) + ((i*60) - x) * ((i*60) - x));
                    if(distance<100)
                    {
                        retu = true;
                    }
                }
            }
        }
        return retu;
    }

    public Point findTrashCan()
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



    public int findCleaner(Point p)
    {
        double minDistance = 10000;
        Point cleaner_point = new Point(-100,-100);
        int cleanerInd = -1;

        if(map.cleaners.size>0)
        {
            for(int i = 0; i < map.cleaners.size; i++) {
                double y2 = map.cleaners.get(i).y;
                double y1 = p.y;

                double x2 = map.cleaners.get(i).x;
                double x1 = p.x;
                double distance = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

                if(minDistance>distance)
                {
                    minDistance=distance;
                    cleaner_point = new Point(map.cleaners.get(i).x,map.cleaners.get(i).y);
                    cleanerInd=i;
                }
                System.out.println("tavolsag:"+distance);
            }
        }

        return cleanerInd;
    }




    public int getMood() {
        return mood;
    }

    class moodTask extends TimerTask
    {
        public void run() {
            moodChange();
        }
    }

    class personBehaviour extends TimerTask
    {
        public void run() {
            isWaiting=false;
            behaviour();
            //timer.cancel(); //Terminate the timer thread
        }
    }

    class parkBehaviour extends TimerTask
    {
        public void run()
        {
            if(isParkNearby())
            {
                System.out.println("van termeszet a kozelben");
                gainMood(2);
            }
        }
    }

}
