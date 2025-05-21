import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;

public class PantallaInicialPanel extends JPanel {

    private JTextField nombreField;
    private JTable rankingTable;
    private DefaultTableModel tableModel;
    private MainFrame mainFrame;
    private Font pressStartFont;
    private BufferedImage backgroundImage;

    public PantallaInicialPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        loadBackgroundImage();
        loadCustomFont();
        initComponents();
        cargarRankingDesdeBD();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/img/background.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
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

    private void initComponents() {
        // Panel central transparente para contenido
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("¡Bienvenido a OxyRun!");
        titulo.setFont(pressStartFont.deriveFont(60f));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(titulo);

        JPanel nombrePanel = new JPanel();
        nombrePanel.setOpaque(false);
        nombrePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel nombreLabel = new JLabel("Indica tu Nombre:");
        nombreLabel.setFont(pressStartFont);
        nombreLabel.setForeground(Color.WHITE);
        nombreField = new JTextField(10);
        nombreField.setFont(pressStartFont.deriveFont(25f));
        nombrePanel.add(nombreLabel);
        nombrePanel.add(nombreField);
        centerPanel.add(Box.createVerticalStrut(100));
        centerPanel.add(nombrePanel);

        JButton jugarButton = new JButton("Jugar");
        jugarButton.setFont(pressStartFont.deriveFont(25f));
        jugarButton.setFocusPainted(false);
        jugarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        jugarButton.addActionListener(e -> {
            if (nombreField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce tu nombre antes de jugar.", "Nombre requerido", JOptionPane.WARNING_MESSAGE);

            }else{ mainFrame.showStartScreen(nombreField.getText());}
        });
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(jugarButton);

        add(centerPanel, BorderLayout.NORTH);

        // Tabla de ranking
        String[] columnas = {"Nombre", "Record"};
        tableModel = new DefaultTableModel(columnas, 0);
        rankingTable = new JTable(tableModel);
        rankingTable.setFont(pressStartFont.deriveFont(12f));
        rankingTable.setRowHeight(30);
        rankingTable.setBackground(Color.BLACK);
        rankingTable.setForeground(Color.GREEN);
        rankingTable.setGridColor(Color.DARK_GRAY);
        rankingTable.getTableHeader().setFont(pressStartFont.deriveFont(12f));
        rankingTable.getTableHeader().setForeground(Color.WHITE);
        rankingTable.getTableHeader().setBackground(Color.DARK_GRAY);

        // Panel principal con BoxLayout para apilar verticalmente
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

// Espacio arriba para empujar la tabla hacia abajo
        add(Box.createVerticalGlue());

// Panel contenedor de la tabla para centrado y espaciado
        JPanel tablaPanel = new JPanel();
        tablaPanel.setOpaque(false);
        tablaPanel.setLayout(new BoxLayout(tablaPanel, BoxLayout.Y_AXIS));

// Scroll con estilo personalizado
        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Ranking",
                0, 0,
                pressStartFont.deriveFont(30f),
                Color.WHITE
        ));
        scrollPane.setPreferredSize(new Dimension(800, 200));
        scrollPane.setMaximumSize(new Dimension(800, 200));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

// Añadir al contenedor
        tablaPanel.add(scrollPane);
        tablaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

// Añadir el contenedor centrado al panel principal
        add(tablaPanel);

// Espacio extra abajo si quieres que esté aún más abajo
        add(Box.createVerticalStrut(50));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void cargarRankingDesdeBD() {
        tableModel.setRowCount(0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/oxyrun", "oxyrun", "123456");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT usuario, nivel_max FROM puntuaciones ORDER BY nivel_max DESC")) {

            while (rs.next()) {
                String usuario = rs.getString("usuario");
                int nivel_max = rs.getInt("nivel_max");
                tableModel.addRow(new Object[]{usuario, nivel_max});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar ranking: " + ex.getMessage());
        }
    }
}
