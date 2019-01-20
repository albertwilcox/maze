package wilcox.maze;

import java.util.Random;

public class Maze {
    private Cell[][] data;
    private int size;

    public Maze(int size){
        this(size, true);
    }

    public Maze(int size, boolean generated){
        this.size = size;
        data = new Cell[size][size];

        /*
         * Populate maze with walled off cells
         */
        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++){
                Cell c = new Cell(this, x, y);
                data[y][x] = c;
                if (x > 0){
                    Cell left = data[y][x-1];
                    left.setNeighbor(Cell.RIGHT, c);
                    c.setNeighbor(Cell.LEFT, left);
                }
                if (y > 0){
                    Cell top = data[y-1][x];
                    top.setNeighbor(Cell.BOTTOM, c);
                    c.setNeighbor(Cell.TOP, top);
                }
            }
        }

        if (generated) {
            generate();
        }
    }

    public void generate(){
        generate(null, 0);
    }

    public void generate(MazeGenerationUpdatable updatable, int delay){
        Random r = new Random();
        int x = r.nextInt(size), y = r.nextInt(size);
        data[y][x].dig(r, updatable, delay);
    }

    public String testString(){
        String out = "";
        for (Cell[] row: data){
            String ceiling = "", walls = "";
            for (Cell c: row){
                boolean[] wls = c.getWalls();
                ceiling += (wls[Cell.TOP]) ? "+----" : "+    ";
                walls += (wls[Cell.LEFT]) ? "|    " : "     ";
            }
            ceiling += "+\n";
            walls += "|\n";
            out = out + ceiling + walls;
        }
        for (int i = 0; i < size; i++){
            out +="+----";
        }
        out += "+";
        return out;
    }

    public Cell[][] getData(){
        return data;
    }

    public int getSize(){
        return size;
    }
}
