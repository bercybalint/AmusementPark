package com.mygdx.amusementpark;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Buildable extends Rectangle {
    private int prizeToBuild;
    Texture texture;

    public Buildable(int x, int y, int width, int height, Texture texture, int prizeToBuild)
    {
        super(x,y,width,height);
        this.texture=texture;
        this.prizeToBuild=prizeToBuild;
    }
}
