public class Personatge {
    public int x, y;
    public int lives = 3;
    public static double oxygen = 100;
    public long lastHitTime = 0;

    public Personatge(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(int dx, int dy, int screenWidth, int screenHeight) {
        x = Math.max(0, Math.min(screenWidth - 30, x + dx));
        y = Math.max(0, Math.min(screenHeight - 30, y + dy));
    }
}
