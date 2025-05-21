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

    public StartScreen(MainFrame mainFrame) {
        loadCustomFont();
        setPreferredSize(new Dimension(1440, 900));
        loadBackgroundImage();
        loadCientific();
        loadAstronauta();
        loadClon();


        JPanel charactersPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);

                if (personatgeCientific != null) {
                    g.drawImage(personatgeCientific, 320, 350, 100, 150,  this);
                } else {
                    g.setColor(Color.CYAN);
                    g.fillRect(400, 300, 100, 100);
                }
                if (personatgeAstronauta != null) {
                    g.drawImage(personatgeAstronauta, 660, 350, 100, 150,  this);
                } else {
                    g.setColor(Color.CYAN);
                    g.fillRect(660, 300, 60, 60);
                }
                if (personatgeAstronauta != null) {
                    g.drawImage(personatgeClon, 1020, 350, 100, 150,  this);
                } else {
                    g.setColor(Color.CYAN);
                    g.fillRect(920, 300, 60, 60);
                }
            }
        };
        charactersPanel.setOpaque(false);
        charactersPanel.setPreferredSize(new Dimension(1440, 700));
        add(charactersPanel);

        JLabel Titulo = new JLabel();
        Titulo.setFont(pressStartFont.deriveFont(100f));
        Titulo.setForeground(Color.WHITE);
        Titulo.setText("OxyRun");
        Titulo.setBorder(new EmptyBorder(50, 0, 0, 0));
        charactersPanel.add(Titulo);

        JButton startButton = new JButton("EMPEZAR JUEGO");
        startButton.setFont(pressStartFont.deriveFont(20f));

        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        startButton.setPreferredSize(new Dimension(400, 80));

        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(220, 220, 220)); // Gris claro al pasar el mouse
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(Color.WHITE); // Vuelve a blanco
            }
        });

        startButton.addActionListener(e -> mainFrame.startGame());

        JPanel bottomPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);
            }
        };
        bottomPanel.setOpaque(false);
        bottomPanel.add(startButton);

        add(bottomPanel, BorderLayout.SOUTH);
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
