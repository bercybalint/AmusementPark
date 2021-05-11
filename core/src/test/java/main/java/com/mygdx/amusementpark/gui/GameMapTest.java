package main.java.com.mygdx.amusementpark.gui;

import com.mygdx.amusementpark.buildable.Tiles;
import com.mygdx.amusementpark.gui.GameMap;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class GameMapTest extends TestCase
{

    GameMap map = new GameMap(1000,1200, true);
    boolean[][] visited = new boolean[100][200];


    @Test
    public void testGetLong()
    {
        Assert.assertTrue(true);
    }

    @Test
    public void testClearVisited()
    {
        map.clearVisited();
        Assert.assertFalse(visited[0][0]);
    }

    @Test
    public void testPathFinderVisited()
    {
        map.pathFinderVisited(0,0);
        Assert.assertFalse(visited[0][0]);
    }

    @Test
    public void testMapInit()
    {
        map.mapInit();
        Assert.assertTrue(map.terrain.get(10).get(0) == Tiles.EMPTY);
        Assert.assertTrue(map.terrain.get(10).get(1) == Tiles.ROAD);

        Assert.assertTrue(map.terrain.get(8).get(1) == Tiles.BORDER);
        Assert.assertTrue(map.terrain.get(12).get(1) == Tiles.BORDER);

        Assert.assertTrue(map.terrain.get(9).get(1) == Tiles.EMPTY);
        Assert.assertTrue(map.terrain.get(11).get(1) == Tiles.EMPTY);
    }

}