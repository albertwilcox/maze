package wilcox.maze;

import java.util.Random;

public class Cell {
    public static final int LEFT = 0, TOP = 1, RIGHT = 2, BOTTOM = 3;

    private Maze maze;
    private int x, y;
    private Cell[] neighbors = new Cell[4];
    private boolean[] walls = {true, true, true, true};
    private boolean visited = false;

    public Cell(Maze maze, int x, int y){
        this.maze = maze;
        this.x = x;
        this.y = y;
    }

    public void setNeighbor(int loc, Cell cell){
        neighbors[loc] = cell;
    }

    public void openWall(int loc){
        walls[loc] = false;
    }

    public boolean[] getWalls(){
        return walls;
    }

    public Cell[] getNeighbors(){
        return neighbors;
    }

    public boolean isVisited(){
        return visited;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    /*
     * Method that will recursively turn this maze into a maze
     */
    public void dig(Random r, MazeGenerationUpdatable updatable, int delay){
        visited = true;
        int count;
        while ((count = unvisitedNeighborCount()) > 0){
            int[] indexes = unvisitedIndexes(count);
            int index = indexes[r.nextInt(count)];

            Cell other = neighbors[index];
            openWall(index);
            other.openWall((index + 2) % 4);
            if (updatable != null) updatable.update();
            if (delay > 0) {
                try {
                    Thread.sleep(delay);
                } catch(InterruptedException e) {e.printStackTrace();}
            }
            other.dig(r, updatable, delay);
        }
    }

    private int unvisitedNeighborCount(){
        int count = 0;
        for (Cell neighbor: neighbors)
            if (neighbor != null && !neighbor.isVisited())
                count ++;
        return count;
    }

    /*
     * Given the amount of neighboring unvisited cells (to increase efficiency pretty much) this returns
     * a list of their indexes in the neighbors array
     */
    private int[] unvisitedIndexes(int count){
        int[] out = new int[count];
        int index = 0;
        for (int i = 0; i < 4; i++){
            if (neighbors[i] != null && !neighbors[i].isVisited()){
                out[index++] = i;
            }
        }
        return out;
    }
}
