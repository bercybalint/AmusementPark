package com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;

public class Games extends Buildable
{
    public Games(Texture texture){super(texture);}
    /**
     * @param x            - x koordináta
     * @param y            - y koordiánat
     * @param width        - szélesség
     * @param height       -magasság
     * @param texture      -textúrája
     * @param prizeToBuild - mennyibeerül, a megépítése.
     * @param type
     */

    public Games(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type, int timeToUse)
    {
        super(x-width, y-height, width*3, height*3, texture, prizeToBuild, type, timeToUse);
    }
}
