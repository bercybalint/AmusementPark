package main.java.com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;
import main.java.com.mygdx.amusementpark.buildable.Buildable;
import org.w3c.dom.Text;

public class TrashCan extends Buildable
{
    public TrashCan(Texture texture)
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
    public TrashCan(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type)
    {
        super(x, y, width, height, texture, 0, type,0);
    }
}