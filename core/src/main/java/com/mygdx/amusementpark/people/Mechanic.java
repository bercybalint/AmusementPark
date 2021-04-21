package main.java.com.mygdx.amusementpark.people;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import main.java.com.mygdx.amusementpark.buildable.Buildable;
import main.java.com.mygdx.amusementpark.buildable.Games;
import main.java.com.mygdx.amusementpark.buildable.Tiles;
import main.java.com.mygdx.amusementpark.buildable.Trash;
import main.java.com.mygdx.amusementpark.gui.GameMap;
import main.java.com.mygdx.amusementpark.pathfinding.AStarPathFinder;
import main.java.com.mygdx.amusementpark.pathfinding.Mover;
import main.java.com.mygdx.amusementpark.pathfinding.PathFinder;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Mechanic extends Person implements Mover
{

    private int delay = 1000;
    public PathFinder finder;
    public Buildable destination;

    public Boolean isGoing = false;
    public Boolean isFixing = false;
    public Boolean isHome = false;

    public Timer timer;
    Timer fixing_time;
    Timer homeTime;
    private Buildable home;

    public Mechanic(GameMap map, int x, int y, int width, int height, Texture texture, int window_h, int window_w, Point home)
    {
        super(map, x, y, width, height, texture, window_h, window_w);
        this.home = lookForHome(home);
        finder = new AStarPathFinder(map, 2000, false);
        timer=new Timer();
        homeTime=new Timer();
        new mechanicBehaviour().run();
    }

    public Buildable lookForHome(Point home)
    {
        Buildable search = null;
        for(int i =0 ; i < map.staffBuildings.size; i++)
        {
            if(i<map.staffBuildings.size)
            {
                System.out.println("megtaláltam hazát");
                search=map.staffBuildings.get(i);
            }
        }

        return search;
    }

    public void goHere(Point p)
    {

        this.p = p;
        map.clearVisited();
        path = finder.findPath(this, (ind_x), (ind_y), p.x / 60, p.y / 40);
        if (path != null)
        {
            isGoing = true;
            pathLength = path.getLength();
            stepIndex = 0;
            currentStep = path.getStep(stepIndex);
            System.out.println("Tudom hova megyek");
        }
        else
        {
            timer = new Timer();
            timer.schedule(new mechanicBehaviour(), 2000);
            System.out.println("Nem találtam utat");
        }
    }

    public void fixIt()
    {
        fixing_time = new Timer();
        fixing_time.schedule(new fixThings(),10000);
    }

    private Buildable lookForWork()
    {
        Buildable p = null;
        for(int i = 0; i < map.destinationPoints.size; i++)
        {
            if(map.destinationPoints.get(i).working==false)
            {
                p=map.destinationPoints.get(i);
            }
        }
        return p;
    }

    public void gotHome()
    {
        isHome=true;
        homeTime= new Timer();
        homeTime.schedule(new mechanicBehaviour(),5000);
    }

    class mechanicBehaviour extends TimerTask
    {
        public void run() {
            isHome=false;
            destination = lookForWork();
            if(destination!=null)
            {
                goHere(new Point(destination.x,destination.y));
            }
            else
            {
                destination=home;
                goHere(new Point(home.x,home.y));
            }
            timer.cancel();
            homeTime.cancel();
        }
    }

    class fixThings extends TimerTask
    {
        public void run() {
            ((Games)destination).fixed();
            isFixing=false;
            new mechanicBehaviour().run();
            fixing_time.cancel();
        }
    }
}
