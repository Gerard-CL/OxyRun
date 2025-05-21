public class Personatge {
    int x, y;
    int speed = 5;
    int oxygen = 1000;
    int lives = 3;

    public Personatge(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy) {
        if (dx != 0 || dy != 0) oxygen--;
        x += dx * speed;
        y += dy * speed;
        x = Math.max(0, Math.min(770, x));
        y = Math.max(0, Math.min(570, y));
    }
}
