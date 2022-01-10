package mindgo.items;

import arc.graphics.Color;
import arc.util.Timer;
import mindustry.content.Fx;
import mindustry.entities.Units;
import mindustry.gen.Call;
import mindustry.gen.Iconc;
import mindustry.gen.Nulls;
import mindustry.gen.Player;

public class Items {
    public static Item nil, grenade, flash;

    public static void load() {
        nil = new Item("nil", Iconc.none, 99999, (pd) -> {
            pd.player.sendMessage("> [lime]item [white]skipped", null, "[gray]" + Iconc.admin);
        });
        grenade = new Item("granade", Iconc.statusBurning, 2, (pd) -> {
            float mx = pd.player.mouseX;
            float my = pd.player.mouseY;
            Timer.schedule(() -> {
//                Call.label(pd.player.con)
            }, 5, 1);
            Units.nearbyEnemies(pd.data.team, mx - 20, my - 20, 20, 20, (u) -> {
                u.damage(u.dst(mx, my));
            });
            Call.effect(Fx.explosion, mx, my, 0, Color.red);
            pd.player.sendMessage("> " + grenade.icon + " [white]used", null, "[gray]" + Iconc.admin);
        });
    }
}
