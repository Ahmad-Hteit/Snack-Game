import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnackGame extends JPanel implements ActionListener, KeyListener{

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    private class Tile{
        int x;
        int y;
        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int titleSize = 25;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;

    Random random;

    //Game Logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    public SnackGame(int boardHeight, int boardWidth){
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Grid
//        for(int i = 0; i < boardWidth / titleSize; i++){
//            g.drawLine(i * titleSize, 0, i * titleSize, boardHeight);
//            g.drawLine(0, i * titleSize, boardWidth, i * titleSize);
//        }

        //Food
        g.setColor(Color.RED);
//        g.fillRect(food.x * titleSize, food.y * titleSize, titleSize, titleSize);
        g.fill3DRect(food.x * titleSize, food.y * titleSize, titleSize, titleSize, true);

        //SnakeHead
        g.setColor(Color.GREEN);
//        g.fillRect(snakeHead.x * titleSize, snakeHead.y * titleSize, titleSize, titleSize);
        g.fill3DRect(snakeHead.x * titleSize, snakeHead.y * titleSize, titleSize, titleSize,true);

        //SnakeBody
        for (Tile snakePart : snakeBody){
//            g.fillRect(snakePart.x * titleSize, snakePart.y * titleSize, titleSize, titleSize);
            g.fill3DRect(snakePart.x * titleSize, snakePart.y * titleSize, titleSize, titleSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Game Over: " + snakeBody.size(), titleSize - 16, titleSize);
        } else {
            g.drawString("Score: " + snakeBody.size(), titleSize - 16, titleSize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth / titleSize);
        food.y = random.nextInt(boardHeight / titleSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    public void move(){
        // Eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Move snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game Over: snake hits itself
        for (Tile snakePart : snakeBody){
            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        // Game Over: snake hits the border
        if(snakeHead.x < 0 || snakeHead.x >= boardWidth / titleSize ||
                snakeHead.y < 0 || snakeHead.y >= boardHeight / titleSize){
            gameOver = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }
}
