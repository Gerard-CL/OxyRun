import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

public class StartScreen extends JPanel {
    private BufferedImage backgroundImage;
    private BufferedImage personatgeCientific;
    private BufferedImage personatgeAstronauta;
    private BufferedImage personatgeClon;
    private Font pressStartFont;
    private int Pescollit = 0;
    private final int CHARACTER_WIDTH = 100;
    private final int CHARACTER_HEIGHT = 150;
    private final int[] CHARACTER_X_POSITIONS = {320, 660, 1020};
    private final int CHARACTER_Y = 350;
    public String nombre;
    private JLabel Titulo;
    private JLabel Titulo2;
    public String nombreUsuario;

    public StartScreen(MainFrame mainFrame) {
        loadCustomFont();
        setPreferredSize(new Dimension(1440, 900));
        loadBackgroundImage();
        loadCientific();
        loadAstronauta();
        loadClon();

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        Pescollit = (Pescollit - 1 + 3) % 3;
                        repaint();
                        break;
                    case KeyEvent.VK_RIGHT:
                        Pescollit = (Pescollit + 1) % 3 ;
                        repaint();
                        break;
                    case KeyEvent.VK_ENTER:
                        mainFrame.startGame(Pescollit, 1, nombreUsuario);
                        break;
                }
            }
        });


        JPanel charactersPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);

                // Dibujar personajes con borde si están seleccionados
                drawCharacterWithBorder(g, personatgeCientific, 0, CHARACTER_X_POSITIONS[0]);
                drawCharacterWithBorder(g, personatgeAstronauta, 1, CHARACTER_X_POSITIONS[1]);
                drawCharacterWithBorder(g, personatgeClon, 2, CHARACTER_X_POSITIONS[2]);

                // Mensaje de instrucciones
                g.setColor(Color.WHITE);
                g.setFont(pressStartFont.deriveFont(20f));
                String instructions = "← → para seleccionar, ENTER para confirmar";
                int instructionsWidth = g.getFontMetrics().stringWidth(instructions);
                g.drawString(instructions, (getWidth() - instructionsWidth) / 2, 600);

                if (personatgeCientific != null) {
                    g.drawImage(personatgeCientific, 320, 350, 100, 150, this);
                } else {
                    g.setColor(Color.CYAN);
                    g.fillRect(400, 300, 100, 100);
                }
                if (personatgeAstronauta != null) {
                    g.drawImage(personatgeAstronauta, 660, 350, 100, 150, this);
                } else {
                    g.setColor(Color.CYAN);
                    g.fillRect(660, 300, 60, 60);
                }
                if (personatgeAstronauta != null) {
                    g.drawImage(personatgeClon, 1020, 350, 100, 150, this);
                } else {
                    g.setColor(Color.CYAN);
                    g.fillRect(920, 300, 60, 60);
                }
            }

            private void drawCharacterWithBorder(Graphics g, BufferedImage character, int index, int x) {
                if (character != null) {
                    if (index == Pescollit) {
                        // Dibujar borde blanco
                        g.setColor(Color.WHITE);
                        g.fillRect(x - 5, CHARACTER_Y - 5,
                                CHARACTER_WIDTH + 10, CHARACTER_HEIGHT + 10);
                    }
                    g.drawImage(character, x, CHARACTER_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT, this);
                } else {
                    g.setColor(Color.CYAN);
                    g.fillRect(x, CHARACTER_Y, CHARACTER_WIDTH, CHARACTER_HEIGHT);
                }
            }
        };

        charactersPanel.setOpaque(false);
        charactersPanel.setPreferredSize(new Dimension(1440, 700));
        charactersPanel.setLayout(new BoxLayout(charactersPanel, BoxLayout.Y_AXIS));
        add(charactersPanel);

        Titulo = new JLabel();
        Titulo2 = new JLabel();
        Titulo.setFont(pressStartFont.deriveFont(60f));
        Titulo.setForeground(Color.WHITE);
        Titulo.setBorder(new EmptyBorder(50, 0, 0, 0));
        Titulo2.setFont(pressStartFont.deriveFont(30f));
        Titulo2.setForeground(Color.WHITE);
        Titulo2.setBorder(new EmptyBorder(100, 0, 0, 0));
        Titulo2.setText("Elige un personaje:");
        Titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        Titulo2.setAlignmentX(Component.CENTER_ALIGNMENT);

        charactersPanel.add(Titulo);
        charactersPanel.add(Titulo2);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        nombreUsuario = nombre;
        Titulo.setText("Hola " + nombre);
        repaint();
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

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/img/background.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }

    private void loadCientific() {
        try {
            personatgeCientific = ImageIO.read(new File("src/img/Cientific.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }
    private void loadAstronauta() {
        try {
            personatgeAstronauta = ImageIO.read(new File("src/img/Astronauta.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }
    private void loadClon() {
        try {
            personatgeClon = ImageIO.read(new File("src/img/Clon.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
        }
    }

    private void drawBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
    }
}