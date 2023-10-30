package Dila1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpaceInvaders extends JFrame {
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    private final int PLAYER_WIDTH = 50;
    private final int PLAYER_HEIGHT = 30;
    private final int ALIEN_WIDTH = 30;
    private final int ALIEN_HEIGHT = 30;
    private final int BULLET_WIDTH = 5;
    private final int BULLET_HEIGHT = 10;
    private final int PLAYER_SPEED = 5;
    private final int ALIEN_SPEED = 2;
    private final int BULLET_SPEED = 5;

    private boolean isGameOver = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isShooting = false;
    private int playerX, playerY;
    private List<Point> aliens;
    private List<Point> bullets;

    public SpaceInvaders() {
        setTitle("Space Invaders");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    isMovingLeft = true;
                } else if (key == KeyEvent.VK_RIGHT) {
                    isMovingRight = true;
                } else if (key == KeyEvent.VK_SPACE) {
                    isShooting = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) {
                    isMovingLeft = false;
                } else if (key == KeyEvent.VK_RIGHT) {
                    isMovingRight = false;
                } else if (key == KeyEvent.VK_SPACE) {
                    isShooting = false;
                }
            }
        });

        playerX = WINDOW_WIDTH / 2 - PLAYER_WIDTH / 2;
        playerY = WINDOW_HEIGHT - PLAYER_HEIGHT - 20;
        aliens = new ArrayList<>();
        bullets = new ArrayList<>();
        spawnAliens();

        Timer timer = new Timer(10, e -> {
            if (!isGameOver) {
                movePlayer();
                moveAliens();
                if (isShooting) {
                    shootBullet();
                }
                moveBullets();
                checkCollisions();
                repaint();
            }
        });
        timer.start();
    }

    private void spawnAliens() {
        Random random = new Random();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 10; col++) {
                int alienX = 50 + col * 70;
                int alienY = 50 + row * 50;
                aliens.add(new Point(alienX, alienY));
            }
        }
    }

    private void movePlayer() {
        if (isMovingLeft && playerX > 0) {
            playerX -= PLAYER_SPEED;
        }
        if (isMovingRight && playerX < WINDOW_WIDTH - PLAYER_WIDTH) {
            playerX += PLAYER_SPEED;
        }
    }

    private void moveAliens() {
        int direction = 1;
        for (Point alien : aliens) {
            alien.x += direction * ALIEN_SPEED;
            if (alien.x <= 0 || alien.x >= WINDOW_WIDTH - ALIEN_WIDTH) {
                direction *= -1;
                alien.y += ALIEN_HEIGHT;
            }
        }
    }

    private void shootBullet() {
        int bulletX = playerX + PLAYER_WIDTH / 2 - BULLET_WIDTH / 2;
        int bulletY = playerY - BULLET_HEIGHT;
        bullets.add(new Point(bulletX, bulletY));
    }

    private void moveBullets() {
        bullets.removeIf(bullet -> bullet.y <= 0);
        for (Point bullet : bullets) {
            bullet.y -= BULLET_SPEED;
        }
    }

    private void checkCollisions() {
        Rectangle playerRect = new Rectangle(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);
        Rectangle bulletRect;
        Rectangle alienRect;

        for (int i = bullets.size() - 1; i >= 0; i--) {
            bulletRect = new Rectangle(bullets.get(i).x, bullets.get(i).y, BULLET_WIDTH, BULLET_HEIGHT);
            for (int j = aliens.size() - 1; j >= 0; j--) {
                alienRect = new Rectangle(aliens.get(j).x, aliens.get(j).y, ALIEN_WIDTH, ALIEN_HEIGHT);
                if (bulletRect.intersects(alienRect)) {
                    bullets.remove(i);
                    aliens.remove(j);
                    break;
                }
            }
        }

        for (Point alien : aliens) {
            alienRect = new Rectangle(alien.x, alien.y, ALIEN_WIDTH, ALIEN_HEIGHT);
            if (playerRect.intersects(alienRect)) {
                isGameOver = true;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!isGameOver) {
            // Draw player
            g.setColor(Color.BLUE);
            g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);

            // Draw aliens
            g.setColor(Color.RED);
            for (Point alien : aliens) {
                g.fillRect(alien.x, alien.y, ALIEN_WIDTH, ALIEN_HEIGHT);
            }

            // Draw bullets
            g.setColor(Color.BLACK);
            for (Point bullet : bullets) {
                g.fillRect(bullet.x, bullet.y, BULLET_WIDTH, BULLET_HEIGHT);
            }
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", WINDOW_WIDTH / 2 - 100, WINDOW_HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpaceInvaders().setVisible(true));
    }
}