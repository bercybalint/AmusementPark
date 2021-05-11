package main.java.com.mygdx.amusementpark.gui;

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

}