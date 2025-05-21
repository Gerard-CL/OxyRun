import java.awt.*;

public class Ship {
    int x, y;

    public Ship(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean reached(Personatge p) {
        Rectangle shipBounds = new Rectangle(this.x + 30, this.y + 50, 130, 70);
        Rectangle playerBounds = new Rectangle(p.x, p.y, 40, 70);
        return shipBounds.intersects(playerBounds);
    }

}
