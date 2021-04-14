package com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Buildable extends Rectangle {

    private int prizeToBuild;
    private Tiles type;
    private Texture texture;


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
     * @param prizeToBuild - mennyibeerül, a megépítése.
     */
    public Buildable(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type)
    {
        super(x,y,width,height);
        this.texture=texture;
        this.prizeToBuild=prizeToBuild;
        this.type = type;
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