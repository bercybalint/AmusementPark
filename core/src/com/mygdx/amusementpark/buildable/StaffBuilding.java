package com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;

public class StaffBuilding extends Buildable
{

    public StaffBuilding(Texture texture)
    {
        super(texture);
    }

    public StaffBuilding(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type)
    {
        super(x, y, width, height, texture, prizeToBuild, type);
    }
}
