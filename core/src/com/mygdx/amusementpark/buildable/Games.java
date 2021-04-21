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
     * @maxHealth - maximum ennyi hp-ja van a játéknak.
     * @healthPoint - ennyi életerejet van a játéknak, ha lemegy 0-ra, elromlik.
     * @workingTexture - ez a kép van beálltva ha működik a játék
     * @brokenTexture - ez a kép van beállítva ha elromlott.
     */

    public int healthPoints=10;
    public int maxHealth=10;
    public Texture workingTexture;
    public Texture brokenTexture;

    public Games(int x, int y, int width, int height, Texture texture, int prizeToBuild, Tiles type, int timeToUse,Texture working, Texture broken)
    {
        super(x-width, y-height, width*3, height*3, texture, prizeToBuild, type, timeToUse);
        this.workingTexture=working;
        this.brokenTexture=broken;
    }

    /**
     * Ha használja egy vendég a játékot, akkor kopik a játék.
     */
    public void takeDmg()
    {
        Random rand = new Random();
        this.healthPoints-=rand.nextInt(3);
        if(healthPoints<=0)
        {
            this.healthPoints=0;
            this.working=false;
            this.texture=brokenTexture;
        }
    }

    /**
     * Ha elromlott a játék, a szerelő tudja meghívni ezt a függvényt.
     */
    public void fixed()
    {
        this.healthPoints=maxHealth;
        this.texture=workingTexture;
        this.working=true;
    }
}
