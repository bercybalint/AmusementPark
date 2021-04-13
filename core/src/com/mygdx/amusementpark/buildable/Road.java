package com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;

/**
 * neighbours - a szomszédok száma
 * majd, hogy van-e bizonyos irányba szomszéd út
 */
public class Road extends Buildable
{
    private int neighbours;
    public boolean upNeigh;
    public boolean downNeigh;
    public boolean leftNeigh;
    public boolean rightNeigh;

    public Road(Texture texture)
    {
        super(texture);
    }
    /**
     * @param x            - x koordináta
     * @param y            - y koordiánat
     * @param width        - szélesség
     * @param height       -magasság
     * @param texture      -textúrája
     * @param prizeToBuild - mennyibeerül, a megépítése.
     * @param type
     */
    public Road(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type)
    {
        super(x, y, width, height, texture, prizeToBuild, type);
        upNeigh = false;
        downNeigh = false;
        leftNeigh = false;
        rightNeigh = false;
    }

}
