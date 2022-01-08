package mindgo.items;

import mindustry.entities.Units;
import mindustry.gen.Call;
import mindustry.gen.Iconc;

public class Items {
    public static Item granade, flash;

    public static void load() {
        granade = new Item("granade", Iconc.statusBurning, (pd) -> {
            float mx = pd.player.unit().aimX();
            float my = pd.player.unit().aimY();
            Units.nearbyEnemies(pd.data.team, mx - 20, my - 20, 20, 20, (u) -> {
                u.damage(u.dst(mx, my));
            });
        });
    }
}
