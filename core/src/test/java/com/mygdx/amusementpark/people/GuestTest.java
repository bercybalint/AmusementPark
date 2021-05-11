package com.mygdx.amusementpark.people;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

}