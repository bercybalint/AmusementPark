package com.mygdx.amusementpark.people;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.amusementpark.buildable.Buildable;
import com.mygdx.amusementpark.buildable.Road;
import com.mygdx.amusementpark.buildable.Tiles;
import com.mygdx.amusementpark.gui.GameMap;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuestTest
{

    Texture guest_texture;
    Texture annoyed_texture;
    Texture angry_texture;
    Texture trash_texture;
    Texture happy_texture;

    OrthographicCamera camera;
    GameMap map = new GameMap(100, 100,true);
    Guest p = new Guest(map, 0, 0, 20, 20, guest_texture, 0, 0, happy_texture, annoyed_texture, angry_texture, trash_texture);
    int m = p.getMood();

    @Test
    public void testGetLong()
    {
        Assert.assertTrue(p.getMood()<=100);

    }

    @Test
    public void testGetMood()
    {
        Assert.assertTrue(p.getMood()<=100);
    }

    @Test
    public void testGainMood()
    {
        p.gainMood(5);
        int gm = p.getMood();
        Assert.assertTrue(m <= gm);
    }

    @Test
    public void testLoseMood()
    {
        p.loseMood(5);
        int lm = p.getMood();
        Assert.assertTrue(m > lm);
    }

    @Test
    public void testMoodChange()
    {
        p.moodChange();
        int mc = p.getMood();
        Assert.assertTrue(m > mc);
    }

    @Test
    public void testStepToTrash()
    {
        p.stepToTrash();
        Assert.assertTrue(p.steppedInTrash);
    }

    @Test
    public void testIsParkNearBy()
    {
        p.isParkNearby();
        Assert.assertTrue(!p.isParkNearby());
    }

    @Test
    public void testFindCleaner()
    {
        Point pt = new Point(0,25);
        int c = p.findCleaner(pt);
        Assert.assertTrue(c == -1);
    }

    @Test
    public void testTouched()
    {
        Buildable actual = new Road(map.units.get(10).get(10).x,map.units.get(10).get(10).y,
                map.units.get(10).get(10).width,map.units.get(10).get(10).height,
                map.textures.road_down_to_up,10, Tiles.ROAD);
        map.units.get(10).set(10,actual);
        map.terrain.get(10).set(10,Tiles.ROAD);
        map.checkRoadNeighbours();

        Buildable p = new Road();
        Road c = (Road)map.units.get(10).get(10);
        Assert.assertTrue(c.upNeigh==false);
        Assert.assertTrue(c.downNeigh==false);
        Assert.assertTrue(c.leftNeigh==false);
        Assert.assertTrue(c.rightNeigh==false);

        map.units.get(10).set(11,new Road(map.units.get(10).get(11).x,map.units.get(10).get(11).y,
                map.units.get(10).get(11).width,map.units.get(10).get(11).height,
                map.textures.road_down_to_up,10, Tiles.ROAD));

        map.checkRoadNeighbours();

        Assert.assertTrue(c.upNeigh==true);
        Assert.assertTrue(c.downNeigh==false);
        Assert.assertTrue(c.leftNeigh==false);
        Assert.assertTrue(c.rightNeigh==false);

        map.units.get(10).set(9,new Road(map.units.get(10).get(9).x,map.units.get(10).get(9).y,
                map.units.get(10).get(9).width,map.units.get(10).get(9).height,
                map.textures.road_down_to_up,10, Tiles.ROAD));

        map.checkRoadNeighbours();

        Assert.assertTrue(c.upNeigh==true);
        Assert.assertTrue(c.downNeigh==true);
        Assert.assertTrue(c.leftNeigh==false);
        Assert.assertTrue(c.rightNeigh==false);

        map.units.get(9).set(10,new Road(map.units.get(9).get(10).x,map.units.get(9).get(10).y,
                map.units.get(9).get(10).width,map.units.get(9).get(10).height,
                map.textures.road_down_to_up,10, Tiles.ROAD));

        map.checkRoadNeighbours();

        Assert.assertTrue(c.upNeigh==true);
        Assert.assertTrue(c.downNeigh==true);
        Assert.assertTrue(c.leftNeigh==true);
        Assert.assertTrue(c.rightNeigh==false);

        map.units.get(11).set(10,new Road(map.units.get(11).get(10).x,map.units.get(11).get(10).y,
                map.units.get(11).get(10).width,map.units.get(11).get(10).height,
                map.textures.road_down_to_up,10, Tiles.ROAD));

        map.checkRoadNeighbours();

        Assert.assertTrue(c.upNeigh==true);
        Assert.assertTrue(c.downNeigh==true);
        Assert.assertTrue(c.leftNeigh==true);
        Assert.assertTrue(c.rightNeigh==true);

    }

}