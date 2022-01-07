package mindgo.items;

import arc.func.Cons;
import mindgo.logic.PlayerData;
import mindustry.gen.Iconc;

public class Item {
    public String name;
    public char icon;
    public Cons<PlayerData> use;

    public Item(String name, char icon, Cons<PlayerData> use) {
        this.name = name;
        this.icon = icon;
        this.use = use;
    }
}
