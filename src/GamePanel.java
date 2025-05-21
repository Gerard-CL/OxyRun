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
    private BufferedImage marcians;
    private BufferedImage meteorits;
    private BufferedImage Pastronauta;
    private BufferedImage PastronautaLeft;
    private BufferedImage PastronautaRight;
    private BufferedImage Pcientific;
    private BufferedImage Pclon;
    private BufferedImage Ship;
    private int Pescollit;
    private Font pressStartFont;
    private int nivell;
    private boolean victoryDialogShown = false;
    BufferedImage currentPlayerImage;
    private String nombreUsuraio;



    public GamePanel(int pescollit, int Nivell, String nombreUsuraio) {
        this.Pescollit = pescollit;
        this.nivell = Nivell;
        this.nombreUsuraio=nombreUsuraio;
        // Configuración inicial del panel
        setPreferredSize(new Dimension(1440, 900));
        setFocusable(true);
        addKeyListener(this);
        setDoubleBuffered(true); // Mejora el rendimiento gráfico

        // Crear posiciones Random para los objetos del juego
        Random random = new Random();
        int aleatorioAlturaP = random.nextInt(730 - 70) + 70;
        int aleatorioAlturaN = random.nextInt(730);
        int aleatorioAmpladaP = random.nextInt(700);
        int aleatorioAmpladaN = random.nextInt(1250 - 700) + 700;

        // Cargar imagenes
        loadimages();
        //Cargar fuente
        loadCustomFont();

        if (Pescollit == 0) currentPlayerImage = Pcientific;
        else if (Pescollit == 1) currentPlayerImage = Pastronauta;
        else if (Pescollit == 2) currentPlayerImage = Pclon;


        // Inicializar elementos del juego
        player = new Personatge(aleatorioAmpladaP, aleatorioAlturaP);
        ship = new Ship(aleatorioAmpladaN, aleatorioAlturaN);
        enemies = new ArrayList<>();

        // Crear bootn siguinte nivel




        this.setLayout(null); // usar layout absoluto

        // Crear enemigos aleatorios

        spawnEnemies(Nivell * 4);

        // Configurar timer del juego
        timer = new Timer(30, this);
        timer.start();
    }

    private void loadimages() {
        try {
            marcians = ImageIO.read(new File("src/img/Marcians.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }

        try {
            meteorits = ImageIO.read(new File("src/img/Meteorits.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }

        try {
            backgroundImage = ImageIO.read(new File("src/img/background.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            backgroundImage = null;
        }
        try {
            Pastronauta = ImageIO.read(new File("src/img/Astronauta.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            Pastronauta = null;
        }
        try {
            Pcientific = ImageIO.read(new File("src/img/Cientific.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            Pcientific = null;
        }

        try {
            Pclon = ImageIO.read(new File("src/img/Clon.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            Pclon = null;
        }
        try {
            Ship = ImageIO.read(new File("src/img/Ship.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            Ship = null;
        }
        try {
            PastronautaLeft = ImageIO.read(new File("src/img/AstonautaLeft.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            PastronautaLeft = null;
        }
        try {
            PastronautaRight = ImageIO.read(new File("src/img/AstonautaRight.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            PastronautaRight = null;
        }

    }



    private void loadCustomFont() {
        try {
            File fontFile = new File("src/fonts/PressStart2P-Regular.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            pressStartFont = baseFont.deriveFont(Font.PLAIN, 24); // Puedes cambiar el tamaño
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(baseFont);
        } catch (FontFormatException | IOException e) {
            System.err.println("Error al cargar la fuente: " + e.getMessage());
            pressStartFont = new Font("Monospaced", Font.BOLD, 24);
        }
    }

    private void spawnEnemies(int count) {
        Random rand = new Random();
        int width = getWidth() > 30 ? getWidth() : 1440;    // tamaño preferido o fallback
        int height = getHeight() > 30 ? getHeight() : 900;

        for (int i = 0; i < count; i++) {
            int speed = 5 + nivell;
            enemies.add(new Enemy(speed, width, height));
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibujar fondo
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        // Dibujar elementos del juego
        drawGameElements(g);
        // Dibujar HUD (información del juego)
        drawHUD(g);
        // Dibujar mensajes de fin de juego
        drawGameStatusMessages(g);
    }


    private void drawGameElements(Graphics g) {
        // Dibujar personaje

        g.drawImage(currentPlayerImage, player.x, player.y, 50, 70, this);


        // Dibujar nave
        g.drawImage(Ship,ship.x, ship.y, 200, 170, this);

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
        g.setFont(pressStartFont);
        g.setColor(Color.WHITE);

        //Nivel
        g.drawString("Nivel: "+ nivell, 600, 50);

        // Oxygen meter
        g.drawString("Oxygen: ", 20, 50);
        g.setColor(new Color(66, 172, 31));
        g.fillRect(200, 28, (int) (Personatge.oxygen * 2), 20);
        g.setColor(Color.WHITE);
        g.drawRect(200, 28, 200, 20);

        // Lives
        g.drawString("Lives: ", 20, 90);
        g.setColor(Color.RED);
        for (int i = 0; i < player.lives; i++) {
            g.fillOval(180 + i * 30, 68, 20, 20);
        }
    }
    private void showVictoryDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setUndecorated(true);
        dialog.setSize(550, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel message = new JLabel("<html><center>Nivel: " + nivell + " <span style='color:green;'>Completado</span><br><br>¿Quieres volver a empezar?</center></html>",
                SwingConstants.CENTER);
        message.setForeground(Color.BLACK);
        message.setFont(pressStartFont);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton continuar = new JButton("Continuar");
        continuar.setFocusPainted(false);
        JButton salir = new JButton("Salir");
        salir.setFocusPainted(false);

        // Estilo común
        Font font = pressStartFont;
        Color bgNormal = new Color(50, 50, 50);
        Color bgSelected = new Color(42, 126, 35);
        Color fgColor = Color.WHITE;

        continuar.setFont(font);
        continuar.setFocusable(false);

        salir.setFont(font);
        salir.setFocusable(false);

        buttonPanel.add(continuar);
        buttonPanel.add(salir);

        panel.add(message, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);

        // Control de selección
        JButton[] buttons = {continuar, salir};
        int[] selectedIndex = {0}; // mutable array para lambda

        // Función para actualizar estilos visuales
        Runnable updateButtonStyles = () -> {
            for (int i = 0; i < buttons.length; i++) {
                JButton b = buttons[i];
                b.setBackground(i == selectedIndex[0] ? bgSelected : bgNormal);
                b.setForeground(fgColor);
            }
        };

        // Inicial
        updateButtonStyles.run();

        // Acción continuar
        continuar.addActionListener(e -> {
            dialog.dispose();
            timer.stop();
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.getContentPane().removeAll();
            GamePanel nuevoNivel = new GamePanel(Pescollit, nivell + 1, nombreUsuraio);
            topFrame.add(nuevoNivel);
            topFrame.revalidate();
            topFrame.repaint();
            nuevoNivel.setFocusable(true);
            nuevoNivel.requestFocusInWindow();
            player.oxygen = 100;
        });

        // Acción salir
        salir.addActionListener(e -> {
            ConexionMySQL.insertarPuntuacion(nombreUsuraio, nivell);
            System.exit(0);
        });

        // Key Bindings
        InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        im.put(KeyStroke.getKeyStroke("ENTER"), "select");

        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIndex[0] = (selectedIndex[0] + 1) % buttons.length;
                updateButtonStyles.run();
            }
        });

        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIndex[0] = (selectedIndex[0] - 1 + buttons.length) % buttons.length;
                updateButtonStyles.run();
            }
        });

        am.put("select", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons[selectedIndex[0]].doClick();
            }
        });

        dialog.setVisible(true);
    }

    private void showDerrotaDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        dialog.setUndecorated(true);
        dialog.setSize(600, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel message = new JLabel("<html><center>Nivel: " + nivell + " <span style='color:red;'>Fallido</span><br><br>¿Quieres volver a empezar?</center></html>",
                SwingConstants.CENTER);
        message.setForeground(Color.BLACK);
        message.setFont(pressStartFont);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton continuar = new JButton("Reintentar");
        JButton salir = new JButton("Salir");

        // Estilo común
        Font font = pressStartFont;
        Color bgNormal = new Color(50, 50, 50);
        Color bgSelected = new Color(42, 126, 35);
        Color fgColor = Color.WHITE;

        continuar.setFont(font);
        continuar.setFocusable(false);

        salir.setFont(font);
        salir.setFocusable(false);

        buttonPanel.add(continuar);
        buttonPanel.add(salir);

        panel.add(message, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);

        // Control de selección
        JButton[] buttons = {continuar, salir};
        int[] selectedIndex = {0}; // mutable array para lambda

        // Función para actualizar estilos visuales
        Runnable updateButtonStyles = () -> {
            for (int i = 0; i < buttons.length; i++) {
                JButton b = buttons[i];
                b.setBackground(i == selectedIndex[0] ? bgSelected : bgNormal);
                b.setForeground(fgColor);
            }
        };

        // Inicial
        updateButtonStyles.run();

        // Acción continuar
        continuar.addActionListener(e -> {
            dialog.dispose();
            timer.stop();
            nivell = 1;
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.getContentPane().removeAll();
            GamePanel nuevoNivel = new GamePanel(Pescollit, nivell, nombreUsuraio);
            topFrame.add(nuevoNivel);
            topFrame.revalidate();
            topFrame.repaint();
            nuevoNivel.setFocusable(true);
            nuevoNivel.requestFocusInWindow();
            player.oxygen = 100;
        });

        // Acción salir
        salir.addActionListener(e -> {
            ConexionMySQL.insertarPuntuacion(nombreUsuraio, nivell);
            System.exit(0);
        });


        // Key Bindings
        InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        im.put(KeyStroke.getKeyStroke("ENTER"), "select");

        am.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIndex[0] = (selectedIndex[0] + 1) % buttons.length;
                updateButtonStyles.run();
            }
        });

        am.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedIndex[0] = (selectedIndex[0] - 1 + buttons.length) % buttons.length;
                updateButtonStyles.run();
            }
        });

        am.put("select", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttons[selectedIndex[0]].doClick();
            }
        });

        dialog.setVisible(true);
    }




    private void drawGameStatusMessages(Graphics g) {
        if (gameOver) {
            showDerrotaDialog();
        } else if (gameWon) {
            if (!victoryDialogShown) {
                victoryDialogShown = true;
                showVictoryDialog();
            }
        }
    }

    private void drawCenteredText(Graphics g, String text, Color color) {
        g.setFont(pressStartFont);
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
        if (Pescollit == 1) {
            if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) {
                currentPlayerImage = PastronautaLeft;
                dx = -7;
            }
            if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) {
                currentPlayerImage = PastronautaRight;
                dx = 7;
            }
            if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]){
                currentPlayerImage = Pastronauta;
                dy = -7;
            }
            if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]){
                currentPlayerImage = Pastronauta;
                dy = 7;
            };
        } else {
            if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) dx = -4;
            if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) dx = 4;
            if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) dy = -4;
            if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) dy = 4;
        }

        player.move(dx, dy, getWidth(), getHeight());
        long currentTime = System.currentTimeMillis();
        // Mover enemigos y detectar colisiones
        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.collidesWith(player)) {
                if (currentTime - player.lastHitTime > 1000){
                    player.lives -=1;
                    player.lastHitTime = currentTime;
                    enemy.cambiarDireccion();
                }
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
}