package mindgo.rooms;

import arc.util.Timer;
import mindgo.Main;
import mindgo.logic.PlayerData;
import mindgo.scene.Shop;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.Iconc;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

public class UnitRoom extends CostRoom {

    public UnitType type;
    char icon;

    public UnitRoom(UnitType type, int cost, int tx, int ty) {
        super(cost, tx, ty);
        this.type = type;
        getIcon();
    }

    @Override
    public boolean canBuy(PlayerData pd) {
        return pd.data.type != type && pd.data.money >= cost;
    }

    @Override
    public void onBuy(PlayerData pd) {
        pd.data.type = type;
    }

    @Override
    public String getLabel() {
        return icon + ": " + cost + Iconc.itemSurgeAlloy;
    }

    @Override
    public void create() {
        super.create();
        Timer.schedule(() -> {
            if ((Main.ME.currentScene instanceof Shop)) {
                Unit unit = type.create(Team.sharded);
                unit.set(drawx, drawy);
                unit.add();
            }
        }, 1);
    }

    void getIcon() {
        if (type == UnitTypes.dagger) icon = Iconc.unitDagger;
        else if (type == UnitTypes.nova) icon = Iconc.unitNova;
        else if (type == UnitTypes.mace) icon = Iconc.unitMace;
    }
}
