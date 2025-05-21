public class Ship {
    int x, y;

    public Ship(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean reached(GamePanel.Personatge p) {
        return Math.abs(x - p.x) < 50 && Math.abs(y - p.y) < 50;
    }
}
