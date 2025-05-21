import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private Image backgroundImage;
    private Timer timer;
    private Personatge player;
    private Ship ship;
    private ArrayList<Enemy> enemies;
    private boolean[] keys = new boolean[256];
    private boolean gameOver = false;
    private boolean gameWon = false;
    private Font gameFont = new Font("Arial", Font.BOLD, 24);
    private Font bigFont = new Font("Arial", Font.BOLD, 48);
    private BufferedImage marcians;
    private BufferedImage meteorits;

    public GamePanel() {
        // Configuración inicial del panel
        setPreferredSize(new Dimension(1440, 900));
        setFocusable(true);
        addKeyListener(this);
        setDoubleBuffered(true); // Mejora el rendimiento gráfico

        // Cargar imagen de fondo
        loadBackgroundImage();
        loadmarcians();
        loadmeteorits();

        // Inicializar elementos del juego
        player = new Personatge(50, 500);
        ship = new Ship(700, 50);
        enemies = new ArrayList<>();

        // Crear enemigos aleatorios
        spawnEnemies(5);

        // Configurar timer del juego
        timer = new Timer(30, this);
        timer.start();
    }

    private void loadmarcians() {
        try {
            marcians = ImageIO.read(new File("src/img/Marcians.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }

    private void loadmeteorits() {
        try {
            meteorits = ImageIO.read(new File("src/img/Meteorits.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/img/background.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            backgroundImage = null;
        }
    }

    private void spawnEnemies(int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int x = rand.nextInt(Math.max(1, getWidth() - 50));  // Asegura que el límite sea ≥1
            int y = rand.nextInt(Math.max(1, getHeight() / 2));
            int speed = 2 + rand.nextInt(3);
            enemies.add(new Enemy(x, y, speed));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar fondo
        drawBackground(g);

        // Dibujar elementos del juego
        drawGameElements(g);

        // Dibujar HUD (información del juego)
        drawHUD(g);

        // Dibujar mensajes de fin de juego
        drawGameStatusMessages(g);
    }

    private void drawBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fondo degradado como alternativa
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = new Color(0, 0, 50); // Azul oscuro
            Color color2 = Color.BLACK;
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void drawGameElements(Graphics g) {
        // Dibujar personaje
        g.setColor(new Color(0, 200, 255, 200)); // Cyan semitransparente
        g.fillRect(player.x, player.y, 30, 30);
        g.setColor(Color.WHITE);
        g.drawRect(player.x, player.y, 30, 30);

        // Dibujar nave
        g.setColor(new Color(255, 255, 0, 200)); // Amarillo semitransparente
        g.fillRect(ship.x, ship.y, 40, 40);
        g.setColor(Color.WHITE);
        g.drawRect(ship.x, ship.y, 40, 40);

        // Dibujar enemigos
        for (Enemy enemy : enemies) {
            if (marcians != null) {
                g.drawImage(marcians, enemy.x, enemy.y, 30, 30, this);
            } else {
                // Fallback por si no se carga la imagen
                g.setColor(Color.RED);
                g.fillOval(enemy.x, enemy.y, 20, 20);
                g.setColor(Color.BLACK);
                g.drawOval(enemy.x, enemy.y, 20, 20);
            }
        }




    }

    private void drawHUD(Graphics g) {
        g.setFont(gameFont);
        g.setColor(Color.WHITE);

        // Oxygen meter
        g.drawString("Oxygen: " + Personatge.oxygen, 20, 30);
        g.setColor(new Color(0, 150, 255));
        g.fillRect(150, 15, (int) (Personatge.oxygen * 2), 20);
        g.setColor(Color.WHITE);
        g.drawRect(150, 15, 200, 20);

        // Lives
        g.drawString("Lives: ", 20, 70);
        g.setColor(Color.RED);
        for (int i = 0; i < player.lives; i++) {
            g.fillOval(150 + i * 30, 55, 20, 20);
        }
    }

    private void drawGameStatusMessages(Graphics g) {
        if (gameOver) {
            drawCenteredText(g, "GAME OVER", Color.RED);
        } else if (gameWon) {
            drawCenteredText(g, "YOU WIN!", new Color(50, 255, 50));
        }
    }

    private void drawCenteredText(Graphics g, String text, Color color) {
        g.setFont(bigFont);
        g.setColor(color);

        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || gameWon) return;

        // Movimiento del jugador
        int dx = 0, dy = 0;
        if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) dx = -5;
        if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) dx = 5;
        if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) dy = -5;
        if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) dy = 5;

        player.move(dx, dy);

        // Mover enemigos y detectar colisiones
        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.collidesWith(player)) {
                player.lives--;
                enemy.resetPosition();
                if (player.lives <= 0) {
                    gameOver = true;
                }
            }
        }

        // Reducir oxígeno y verificar condiciones de fin de juego
        player.oxygen -= 0.1;
        if (player.oxygen <= 0) {
            gameOver = true;
        }
        if (ship.reached(player)) {
            gameWon = true;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // Clases internas para los elementos del juego (debes tenerlas definidas en otro archivo)
    class Personatge {
        int x, y, lives = 3;
        static double oxygen = 100;

        public Personatge(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(int dx, int dy) {
            x = Math.max(0, Math.min(getWidth() - 30, x + dx));
            y = Math.max(0, Math.min(getHeight() - 30, y + dy));
        }
    }

    class Ship {
        int x, y;

        public Ship(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean reached(Personatge p) {
            return Math.abs(x - p.x) < 50 && Math.abs(y - p.y) < 50;
        }
    }

    class Enemy {
        int x, y, speed;
        int originalX, originalY;

        public Enemy(int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.originalX = x;
            this.originalY = y;
        }

        public void move() {
            x += speed;
            if (x > getWidth()) {
                x = -20;
                y = (y + 50) % (getHeight() / 2);
            }
        }

        public boolean collidesWith(Personatge p) {
            return x < p.x + 30 && x + 20 > p.x &&
                    y < p.y + 30 && y + 20 > p.y;
        }

        public void resetPosition() {
            x = originalX;
            y = originalY;
        }
    }
}