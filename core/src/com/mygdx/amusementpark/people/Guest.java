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

    public Timer timer;
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
        timer = new Timer();
        goToNewPlace();
    }

    public void gainMood(int moodGain)
    {
        this.mood += moodGain;
        if (this.mood >= maxMood)
        {
            this.mood = maxMood;
        }
    }

    public void loseMood(int mood)
    {
        this.mood-=mood;
        if(this.mood<0)
        {
            this.mood=0;
        }
    }

    public void goHere(Point p)
    {
        this.p = p;
        map.clearVisited();
        finder = new AStarPathFinder(map, 200, false);
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
            timer.schedule(new Guest.personBehaviour(), 3000);
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
            if(!destination.working)
            {
                Timer re_random = new Timer();
                re_random.schedule(new personBehaviour(), 2000);

            }
            else
            {
                Point p = new Point(destination.x, destination.y);
                goHere(p);
            }

        }
        else
        {
            timer = new Timer();
            timer.schedule(new Guest.personBehaviour(), 3000);
        }

    }


    public void reachedDestination(int time_length)
    {
        isWaiting=true;
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
            leavePark();
        }
    }

    @Override
    public void move()
    {
        if (path != null)
        {
            if(mood > 0){
                ind_x = x / 60;
                ind_y = y / 40;
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

                        Random to_trash_r = new Random();
                        if(map.terrain.get(ind_x).get(ind_y)==Tiles.ROAD)
                        {
                            if (!throwingTrash)
                            {
                                int to_trash = to_trash_r.nextInt(15);
                                if (to_trash == 9)
                                {
                                    throwingTrash = true;
                                    Point trash_p = findTrashCan();
                                    System.out.println("Ki akarom dobni a szemetem");

                                    double distance = Math.sqrt((y - trash_p.y) * (y - trash_p.y) + (x - trash_p.x) * (x - trash_p.x));
                                    if (distance < 300)
                                    {
                                        System.out.println("to kuka");
                                        goHere(trash_p);
                                    } else
                                    {
                                        System.out.println("Szemetelek");
                                        throwingTrash = false;
                                        int cleanerIndex = findCleaner(new Point(x, y));
                                        if (cleanerIndex >= 0)
                                        {
                                            map.cleaners.get(cleanerIndex).addTrash(new Trash(x, y, 10, 10, trash_texture, 0, Tiles.TRASH));
                                            System.out.println("jön a takaríto a szeméthez");
                                        } else
                                        {
                                            map.trashes.add(new Trash(x, y, 10, 10, trash_texture, 0, Tiles.TRASH));
                                            System.out.println("Földre szemetelek");
                                        }

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
            }else {
                leavePark();
            }

        }
        if (path == null)
        {
            isGoing = false;
        }
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

    public void leavePark(){
        System.out.println("Elhagyom a parkot mert szar");
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
            goToNewPlace();
            timer.cancel();
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
