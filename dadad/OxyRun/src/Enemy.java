import java.util.Random;

public class Enemy {
    int x, y;
    int speedX, speedY;  // velocidades independientes en X e Y
    Random rand = new Random();

    public Enemy(int speed, int screenWidth, int screenHeight) {
        this.x = rand.nextInt(screenWidth - 30); // Considera el ancho del enemigo
        this.y = rand.nextInt(screenHeight - 30);
        this.speedX = getRandomSpeed();
        this.speedY = getRandomSpeed();
    }

    // Genera una velocidad aleatoria entre -3 y 3 excluyendo 0
    private int getRandomSpeed() {
        int s = 0;
        while (s == 0) {
            s = rand.nextInt(7) - 3; // genera valores de -3 a 3
        }
        return s;
    }

    public void move() {
        // Cambiar de dirección aleatoriamente con poca probabilidad
        if (rand.nextInt(100) < 80) { // 5% de probabilidad cada movimiento
            speedX = getRandomSpeed();
            speedY = getRandomSpeed();
        }

        // Actualizar posición
        x += speedX;
        y += speedY;

        // Mantener dentro de los límites 0-800 en X y 0-600 en Y, y rebotar si toca borde
        if (x < 0) {
            x = 0;
            speedX = -speedX;
        } else if (x > 780) {  // 800 - ancho enemigo (20)
            x = 780;
            speedX = -speedX;
        }

        if (y < 0) {
            y = 0;
            speedY = -speedY;
        } else if (y > 580) {  // 600 - alto enemigo (20)
            y = 580;
            speedY = -speedY;
        }
    }

    public boolean collidesWith(Personatge p) {
        return Math.abs(p.x - x) < 30 && Math.abs(p.y - y) < 30;
    }
}
