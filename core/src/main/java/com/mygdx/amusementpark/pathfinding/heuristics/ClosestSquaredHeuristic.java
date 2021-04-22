package com.mygdx.amusementpark.pathfinding.heuristics;

import com.mygdx.amusementpark.pathfinding.AStarHeuristic;
import com.mygdx.amusementpark.pathfinding.Mover;
import com.mygdx.amusementpark.pathfinding.TileBasedMap;


/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile. In this case the sqrt is removed
 * and the distance squared is used instead
 * 
 * @author Kevin Glass
 */
public class ClosestSquaredHeuristic implements AStarHeuristic {

	/**
	 * @see AStarHeuristic#getCost(TileBasedMap, Mover, int, int, int, int)
	 */
	public float getCost(TileBasedMap map, Mover mover, int x, int y, int tx, int ty) {		
		float dx = tx - x;
		float dy = ty - y;
		
		return ((dx*dx)+(dy*dy));
	}

}
