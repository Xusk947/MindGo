package mindgo.rooms;

import mindgo.logic.PlayerData;
import mindustry.gen.Call;
import mindustry.gen.Iconc;
import mindustry.gen.Player;

public class CostRoom extends Room {
    public int cost;

    public CostRoom(int cost, int tx, int ty) {
        super(tx, ty);
        this.cost = cost;
    }

    @Override
    public void update(PlayerData pd) {
        Player player = pd.player;
        float mx = player.mouseX;
        float my = player.mouseY;

        if (canBuy(pd) && intersect(mx, my)) {
            if (pd.wantBuy) {
                onBuy(pd);
                pd.data.money -= cost;
                pd.wantBuy = false;
                Call.label(player.con(), "[red]" + Iconc.statusBurning + "[white] purchased", 1.5f, drawx, edy);
            } else {
                Call.label(player.con(), "are you [green]sure[white]? \nclick again to confirm", 1.5f, drawx, drawy);
                pd.wantBuy = true;
            }
        }
    }

    @Override
    public void label() {
        Call.label(getLabel(), 1f, drawx, sdy);
    }

    public String getLabel() {
        return "[white]@CostRoom : [acccent]" + cost;
    }

    public boolean canBuy(PlayerData pd) {
        return pd.data.money >= cost;
    }
    public void onBuy(PlayerData pd) {

    }
}
