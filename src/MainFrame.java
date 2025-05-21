import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;
    private GamePanel gamePanel;

    public MainFrame() {
        setTitle("OxyRun");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // Crear pantallas
        StartScreen startScreen = new StartScreen(this);
        // Añadir pantallas al contenedor
        container.add(startScreen, "start");

        // Configurar ventana
        add(container);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Mostrar pantalla inicial
        cardLayout.show(container, "start");
    }

    public void startGame(int pescollit, int Nivell) {
        // Elimina el panel de inicio
        getContentPane().removeAll();

        // Crea el juego pasando el personaje seleccionado
        GamePanel gamePanel = new GamePanel(pescollit, Nivell);
        add(gamePanel);

        // Configuración de la ventana
        pack();
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow(); // Asegura que reciba eventos de teclado
    }
}
