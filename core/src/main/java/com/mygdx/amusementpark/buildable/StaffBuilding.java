package main.java.com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;

public class StaffBuilding extends Buildable
{

    public StaffBuilding(Texture texture)
    {
        super(texture);
    }

    /**
     *
     * @param x- x koordináta
     * @param y- y koordiánat
     * @param width- szélesség
     * @param height-magasság
     * @param texture-textúrája
     * @param prizeToBuild- mennyibeerül, a megépítése.
     * @param type
     */
    public StaffBuilding(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type)
    {
        super(x, y, width, height, texture, prizeToBuild, type,0);
    }
}
