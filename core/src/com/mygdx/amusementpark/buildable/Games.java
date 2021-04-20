package com.mygdx.amusementpark.buildable;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;
import org.graalvm.compiler.lir.LIRInstruction;
import org.w3c.dom.Text;

import java.util.Random;

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
    public boolean working=true;
    public int healthPoints=100;
    public int maxHealth=100;
    public Texture workingTexture;
    public Texture brokenTexture;

    public Games(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type, int timeToUse,Texture working, Texture brokek)
    {
        super(x-width, y-height, width*3, height*3, texture, prizeToBuild, type, timeToUse);
    }

    public void takeDmg()
    {
        Random rand = new Random();
        this.healthPoints-=rand.nextInt(3);
        if(healthPoints<0)
        {
            this.healthPoints=0;
            this.working=false;
            this.texture=brokenTexture;
        }
    }
}
