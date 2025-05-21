// ConexionMySQL.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexionMySQL {
    static final String URL = "jdbc:mysql://localhost:3306/oxyrun";
    static final String USUARIO = "oxyrun";
    static final String CONTRASENA = "123456";

    public static Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
            return null;
        }
    }
    public static void insertarPuntuacion(String usuario, int nivel_max) {
        Connection conn = ConexionMySQL.obtenerConexion();
        if (conn != null) {
            try {
                String sql = "INSERT INTO puntuaciones (usuario, nivel_max) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, usuario);
                stmt.setInt(2, nivel_max);
                stmt.executeUpdate();
                System.out.println("✅ Puntuación insertada correctamente.");
                conn.close();
            } catch (SQLException e) {
                System.out.println("❌ Error al insertar puntuación: " + e.getMessage());
            }
        }
    }
}
