import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;

public class SnakeGame extends JPanel implements KeyListener, ActionListener {
    private static final int GRID_SIZE = 20;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 15;
    private static final int INITIAL_SNAKE_SIZE = 5;
    private static final int GAME_SPEED = 100;

    private ArrayList<Point> snake;
    private Point apple;
    private int direction;
    private boolean isGameover;

    public SnakeGame() {
        snake = new ArrayList<>();
        initializeGame();
        
        // Set up the JFrame
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(GRID_WIDTH * GRID_SIZE, GRID_HEIGHT * GRID_SIZE);
        frame.getContentPane().add(this);
        frame.setVisible(true);
        frame.addKeyListener(this);

        // Start the game loop
        Timer timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    private void initializeGame() {
        snake.clear();
        direction = KeyEvent.VK_RIGHT;
        isGameover = false;

        // Initialize the snake
        int initialX = GRID_WIDTH / 2;
        int initialY = GRID_HEIGHT / 2;
        for (int i = 0; i < INITIAL_SNAKE_SIZE; i++) {
            snake.add(new Point(initialX - i, initialY));
        }

        // Initialize the apple position
        int randomX = (int) (Math.random() * GRID_WIDTH);
        int randomY = (int) (Math.random() * GRID_HEIGHT);
        apple = new Point(randomX, randomY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameover) {
            move();
            checkCollision();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) {
            direction = KeyEvent.VK_LEFT;
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) {
            direction = KeyEvent.VK_RIGHT;
        } else if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) {
            direction = KeyEvent.VK_UP;
        } else if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) {
            direction = KeyEvent.VK_DOWN;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void move() {
        Point newHead = (Point) snake.get(0).clone();

        switch (direction) {
            case KeyEvent.VK_LEFT:
                newHead.x--;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x++;
                break;
            case KeyEvent.VK_UP:
                newHead.y--;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y++;
                break;
        }

        snake.add(0, newHead);

        if (newHead.equals(apple)) {
            // Generate a new apple position
            int randomX = (int) (Math.random() * GRID_WIDTH);
            int randomY = (int) (Math.random() * GRID_HEIGHT);
            apple.setLocation(randomX, randomY);
        } else {
            // Remove the tail of the snake
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT) {
            isGameover = true;
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isGameover = true;
                return;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the grid
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(i * GRID_SIZE, j * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(i * GRID_SIZE, j * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }

        // Draw the apple
        g.setColor(Color.RED);
        g.fillOval(apple.x * GRID_SIZE, apple.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * GRID_SIZE, p.y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
        }

        if (isGameover) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game Over", GRID_WIDTH * GRID_SIZE / 4, GRID_HEIGHT * GRID_SIZE / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeGame());
    }
}
