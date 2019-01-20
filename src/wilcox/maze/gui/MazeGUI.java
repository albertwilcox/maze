package wilcox.maze.gui;

import wilcox.maze.Maze;
import wilcox.maze.MazeAI;

import javax.swing.*;

public class MazeGUI extends JFrame {

    private int mazeSize = 100;

    MazePanel panel;
    JButton generate, solve, reset;
    JCheckBox animatedGenerate, animatedSolve;
    JLabel size;
    JRadioButton ten, twentyFive, fifty, oneHundred;
    ButtonGroup sizeGroup;

    Maze maze;

    public MazeGUI(){
        super("Maze GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new MazePanel(this);

        generate = new JButton("Generate Maze");
        solve = new JButton("Solve Maze");
        solve.setEnabled(false);
        animatedGenerate = new JCheckBox("Animated");
        animatedSolve = new JCheckBox("Animated");
        reset = new JButton("Reset");

        size = new JLabel("Size");
        sizeGroup = new ButtonGroup();
        ten = new JRadioButton("Ten");
        ten.addActionListener(e -> {
            mazeSize = 10;
        });
        sizeGroup.add(ten);
        twentyFive = new JRadioButton("Twenty Five");
        twentyFive.addActionListener(e -> {
            mazeSize = 25;
        });
        sizeGroup.add(twentyFive);
        fifty = new JRadioButton("Fifty");
        fifty.addActionListener(e -> {
            mazeSize = 50;
        });
        sizeGroup.add(fifty);
        oneHundred = new JRadioButton("One Hundred");
        oneHundred.addActionListener(e -> {
            mazeSize = 100;
        });
        oneHundred.setSelected(true);
        sizeGroup.add(oneHundred);

        generate.addActionListener(e -> {
            setMazeAI(null);
            if (animatedGenerate.isSelected()){
                Thread t = new Thread(() -> {
                    solve.setEnabled(false);
                    maze = new Maze(mazeSize, false);
                    setMaze(maze);
                    maze.generate(panel, 2);
                    solve.setEnabled(true);
                });
                t.start();
            } else {
                maze = new Maze(mazeSize);
                setMaze(maze);
                solve.setEnabled(true);
            }
        });
        solve.addActionListener(e -> {
            if (animatedSolve.isSelected()){
                Thread t = new Thread(() -> {
                    MazeAI ai = new MazeAI(maze);
                    setMazeAI(ai);
                    ai.search(panel, 3);
                });
                t.start();
            } else {
                MazeAI ai = new MazeAI(maze);
                ai.search();
                setMazeAI(ai);
            }
        });
        reset.addActionListener(e -> {
            solve.setEnabled(false);
            setMazeAI(null);
            setMaze(null);
        });

        JPanel p = new JPanel();
        GroupLayout gl = new GroupLayout(p);
        gl.setHorizontalGroup(gl.createSequentialGroup()
            .addComponent(panel)
            .addGroup(gl.createParallelGroup()
                .addComponent(generate)
                .addComponent(animatedGenerate)
                .addComponent(size)
                .addComponent(ten)
                .addComponent(twentyFive)
                .addComponent(fifty)
                .addComponent(oneHundred)
                .addComponent(solve)
                .addComponent(animatedSolve)
                .addComponent(reset)));
        gl.setVerticalGroup(gl.createParallelGroup()
            .addComponent(panel)
            .addGroup(gl.createSequentialGroup()
                .addComponent(generate)
                .addComponent(animatedGenerate)
                .addComponent(size)
                .addComponent(ten)
                .addComponent(twentyFive)
                .addComponent(fifty)
                .addComponent(oneHundred)
                .addGap(20)
                .addComponent(solve)
                .addComponent(animatedSolve)
                .addGap(20)
                .addComponent(reset)));
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);
        p.setLayout(gl);

        setResizable(false);
        getContentPane().add(p);
        pack();
        setVisible(true);
    }

    public void setMaze(Maze m){
        panel.setMaze(m);
//        long t = System.currentTimeMillis();
        panel.update();
        //System.out.println(System.currentTimeMillis() - t);
    }

    public void setMazeAI(MazeAI ai){
        panel.setMazeAI(ai);
        panel.update();
    }
}
