package wilcox.maze.gui;

import wilcox.maze.*;
import wilcox.maze.MazeAI.SearchNode;

import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel implements MazeGenerationUpdatable, MazeSearchUpdatable{

    private static final int SIZE = 800;

    MazeGUI gui;
    Maze maze = null;
    MazeAI ai = null;

    public MazePanel(MazeGUI gui){
        setPreferredSize(new Dimension(SIZE, SIZE));
    }

    @Override
    public void update(){
        //System.out.println("about to call repaint");
        repaint();
        //System.out.println("done!");
    }

    public void setMaze(Maze m){
        this.maze = m;
    }

    public void setMazeAI(MazeAI ai){
        this.ai = ai;
    }

    @Override
    public void paintComponent(Graphics g){
        //super.paintComponent(g);

        //System.out.println("here");
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, SIZE, SIZE);

        g.setColor(Color.BLACK);
        if (maze == null){
            g.drawString("Press \"Generate Maze\" to see a maze here!", 20, 20);
            return;
        }

        int size = maze.getSize();
        int cellWidth = SIZE / size;

        if (ai != null){
            g.setColor(Color.red);
            paintSearchNode(ai.bestNode(), cellWidth, g);
        }

        g.setColor(Color.black);
        Cell[][] data = maze.getData();
        for (int y = 0; y < size; y++){
            for (int x = 0; x < size; x++){
                Cell c = data[y][x];
                boolean[] walls = c.getWalls();
                int rX = x * cellWidth, rY = y * cellWidth; // rX and rY mean real x and real y, as in the actual values of where to draw stuff
                if (walls[Cell.TOP]) g.drawLine(rX, rY, rX + cellWidth, rY);
                if (walls[Cell.LEFT]) g.drawLine(rX, rY, rX, rY + cellWidth);
            }
        }
        g.drawLine(SIZE, 0, SIZE, SIZE);
        g.drawLine(0, SIZE, SIZE, SIZE);
        //System.out.println(System.currentTimeMillis());
    }

    public void paintSearchNode(SearchNode node, int cellWidth, Graphics g){
        if (node == null) return;
        g.fillRect(node.getCell().getX() * cellWidth, node.getCell().getY()* cellWidth, cellWidth, cellWidth);
        paintSearchNode(node.getParent(), cellWidth, g);
    }
}
