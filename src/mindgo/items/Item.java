package mindgo.items;

import arc.func.Cons;
import mindgo.logic.PlayerData;
import mindustry.Vars;
import mindustry.gen.Iconc;

public class Item {
    public static final float MAX_DIST = Vars.tilesize * 20 * Vars.tilesize * 20;
    public String name;
    public char icon;
    public Cons<PlayerData> use;
    public int maxStuck;

    public Item(String name, char icon, int maxStuck, Cons<PlayerData> use) {
        this.name = name;
        this.icon = icon;
        this.use = use;
        this.maxStuck = maxStuck;
    }
}
