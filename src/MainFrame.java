import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;

    public MainFrame() {
        setTitle("OxyRun");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // Crear pantallas
        PantallaInicialPanel pantallaInicial = new PantallaInicialPanel(this);
        StartScreen startScreen = new StartScreen(this);

        // Añadir pantallas al contenedor
        container.add(pantallaInicial, "pantallaInicial");
        container.add(startScreen, "start");

        // Configurar ventana
        add(container);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Mostrar pantalla inicial
        cardLayout.show(container, "pantallaInicial");
    }

    public void showStartScreen(String nombre) {
        cardLayout.show(container, "start");
        StartScreen startScreen = (StartScreen) getStartScreen();
        if (startScreen != null) {
            startScreen.setNombre(nombre);  // Aquí le pasas el nombre al panel
        }

        SwingUtilities.invokeLater(() -> {
            if (startScreen != null) {
                startScreen.requestFocusInWindow(); // fuerza el foco
            }
        });
    }

    private Component getStartScreen() {
        for (Component comp : container.getComponents()) {
            if (comp instanceof StartScreen) {
                return comp;
            }
        }
        return null;
    }

    public void startGame(int pescollit, int Nivell, String nombreUsuario) {
        getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(pescollit, Nivell, nombreUsuario);
        add(gamePanel);
        pack();
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
    }
}
