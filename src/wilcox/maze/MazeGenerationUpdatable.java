package wilcox.maze;

/*
 * I added this interface so that I could have the cell update the GUI as it generates
 * the maze without creating a weird circular dependency of the cell on the GUI
 */
public interface MazeGenerationUpdatable {
    void update();
}
