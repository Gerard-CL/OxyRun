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
        gamePanel = new GamePanel();

        // Añadir pantallas al contenedor
        container.add(startScreen, "start");
        container.add(gamePanel, "game");

        // Configurar ventana
        add(container);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Mostrar pantalla inicial
        cardLayout.show(container, "start");
    }

    public void startGame() {
        cardLayout.show(container, "game");
        gamePanel.requestFocusInWindow(); // Asegura que reciba eventos de teclado
    }
}
