package com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.mygdx.amusementpark.people.Guest;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Buildable extends Rectangle {

    public int prizeToUse;
    public Tiles type;
    public Texture texture;
    public int timeToUse;
    public Boolean gameOn = false;
    public Timer gameTimer;
    public int guest_capacity = 2;

    /**
     * @moodGain - Ennyi kedvet kapnak a vendégek ha használják az egységet
     * @woring - Nem romlott-e el az egység
     */
    public int moodGain = 30;
    public boolean working=true;

    public Array<Guest> que = new Array<Guest>();
    public Buildable(){}
    public Buildable(Texture texture)
    {
        this.texture = texture;
    }
    /**
     *
     * @param x - x koordináta
     * @param y - y koordiánat
     * @param width - szélesség
     * @param height -magasság
     * @param texture -textúrája
     * @param prizeToUse - mennyibekerül, a használata.
     */
    public Buildable(int x, int y, int width, int height, Texture texture, int prizeToUse, Tiles type, int timeToUse)
    {
        super(x,y,width,height);
        this.texture=texture;
        this.prizeToUse=prizeToUse;
        this.type = type;
        this.timeToUse=timeToUse;
        if(type==Tiles.ROLLER||type==Tiles.CASTLE)
        {
            gameTimer = new Timer();
            gameTimer.schedule(new gameStart(), 0, 5000);
        }
    }

    public Texture getTexture()
    {
        return texture;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }

    public Tiles getType()
    {
        return type;
    }

    public void setType(Tiles type)
    {
        this.type = type;
    }

    class gameStart extends TimerTask
    {
        public void run() {


            System.out.println("Jatek allapota"+gameOn);
            if(!gameOn)
            {
                if (working)
                {
                    if (que.size > guest_capacity)
                    {
                        for (int i = 0; i < guest_capacity; i++)
                        {
                            que.get(i).goHere(new Point(x, y));
                            que.removeIndex(i);
                        }
                    } else
                    {
                        for (int i = 0; i < que.size; i++)
                        {
                            que.get(i).goHere(new Point(x, y));
                            que.removeIndex(i);
                        }
                    }
                }
                else
                {
                    for (int i = 0; i < que.size; i++)
                    {
                        que.get(i).goToNewPlace();
                        que.removeIndex(i);
                    }
                }
            }
            gameOn = !gameOn;
        }
    }
}
