import java.awt.*;
import java.util.Random;

public class Enemy {
    int x, y;
    int dx, dy;
    int speed = 5;  // velocidades independientes en X e Y
    Random rand = new Random();

    public Enemy(int speed, int screenWidth, int screenHeight) {
        this.x = rand.nextInt(screenWidth - 30); // Considera el ancho del enemigo
        this.y = rand.nextInt(screenHeight - 30);
        cambiarDireccion();
    }

    public void cambiarDireccion() {
        dx = rand.nextInt(3) - 1; // -1, 0, 1
        dy = rand.nextInt(3) - 1;
    }

    public void move() {
        x += dx * speed;
        y += dy * speed;

        // Rebote o cambio de dirección si toca borde
        if (x < 0 || x > 1440 - 30 || y < 0 || y > 900 - 30) {
            cambiarDireccion();
        }

        // Limitar dentro del panel
        x = Math.max(0, Math.min(x, 1440 - 30));
        y = Math.max(0, Math.min(y, 900 - 30));

        // Cada 60 frames (o X tiempo), cambiar dirección
        if (rand.nextInt(100) < 5) {
            cambiarDireccion();
        }
    }

    public boolean collidesWith(Personatge p) {
        Rectangle enemyBounds = new Rectangle(this.x, this.y, 30, 30);  // asegúrate de definir ancho/alto reales
        Rectangle playerBounds = new Rectangle(p.x, p.y, 40, 70); // Usa el tamaño con el que dibujas al jugador
        return enemyBounds.intersects(playerBounds);
    }

}
