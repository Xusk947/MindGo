package mindgo.items;

import mindgo.Main;
import mindgo.scene.Game;

public class GameItem {
    Item item;
    public float x;
    public float y;

    public GameItem(Item item, float x, float y) {
        this.item = item;
        this.x = x;
        this.y = y;

        if (Main.ME.currentScene instanceof Game) {

        }
    }
}
