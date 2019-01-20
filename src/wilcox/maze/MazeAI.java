package wilcox.maze;

import java.util.ArrayList;

public class MazeAI {

    ArrayList<SearchNode> queue = new ArrayList<>(){
        @Override
        public boolean add(SearchNode e){
            int index = size();
            int est = e.costEstimate;
            while (index > 0 && get(index-1).costEstimate < est){
                index--;
            }
            super.add(index, e);
            return true;
        }
    };
    Maze maze;
    boolean[][] visited;
    SearchNode out = null;

    public MazeAI(Maze maze){
        this.maze = maze;

        int size = maze.getSize();
        visited = new boolean[size][size];

        SearchNode start = new SearchNode(maze.getData()[size-1][0]);
        queue.add(start);
    }

    public SearchNode bestNode(){
        if (out != null) return out;
        return queue.get(queue.size()-1);
    }

    public void search(){
        search(null, 0);
    }

    public void search (MazeSearchUpdatable updatable, int delay){
        //System.out.println("starting search...");
        while (out == null){
          //  System.out.println("searching...");
            SearchNode next = queue.get(queue.size()-1);
            //System.out.println(next.getCell().getX() + ", " + next.getCell().getY());
            queue.remove(next);
            out = next.expand();
            try{
                if (delay > 0){
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e){e.printStackTrace();}
            if (updatable != null) updatable.update();
        }

    }

    public class SearchNode {

        SearchNode parent;
        Cell cell;
        Path path;

        int cost, costEstimate;

        public SearchNode(Cell cell){
            this.parent = null;
            this.cell = cell;
            this.path = null;

            cost = 0;
            costEstimate = cost + heuristic();
        }

        public SearchNode(SearchNode parent, Cell cell, int direction){
            this.parent = parent;
            this.cell = cell;
            this.path = new Path((direction + 2) % 4, parent.path);

            cost = parent.cost + 1;
            costEstimate = cost + heuristic();
        }

        /*
         * An optimistic estimate for remaining distance from start to end
         */
        public int heuristic(){
            return cell.getY() + (maze.getSize() - cell.getX() - 1);
        }

        /*
         * Whether this node is at the goal
         */
        public boolean isGoal(){
            return heuristic() == 0;
        }

        public Cell getCell(){
            return cell;
        }

        public SearchNode getParent(){
            return parent;
        }

        /*
         * Check whether this node meets the goal, and if it doesn't, expand it
         */
        public SearchNode expand(){
            if (isGoal()){
                return this;
            }
            for (int dir = 0; dir < 4; dir++){
                Cell c = cell.getNeighbors()[dir];
                if (c != null && !visited[c.getY()][c.getX()] && !c.getWalls()[(dir + 2)%4]){
                    SearchNode newNode = new SearchNode(this, c, dir);
                    queue.add(newNode);
                    visited[c.getY()][c.getX()] = true;
                }
            }
            return null;
        }
    }

    public class Path{
        int direction;
        Path next;

        public Path(int direction){
            this.direction = direction;
            this.next = null;
        }

        public Path(int direction, Path next){
            this.direction = direction;
            this.next = next;
        }

//        public void pinToEnd(Path end){
//            if (next == null){
//                next = end;
//            } else {
//                next.pinToEnd(end);
//            }
//        }
    }
}
