package com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Buildable extends Rectangle {

    public int prizeToUse;
    public Tiles type;
    public Texture texture;
    public int timeToUse;

    /**
     * @moodGain - Ennyi kedvet kapnak a vendégek ha használják az egységet
     * @woring - Nem romlott-e el az egység
     */
    public int moodGain = 30;
    public boolean working=true;

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




}
