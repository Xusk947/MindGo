package mindgo.rooms;

import mindgo.items.Item;
import mindgo.items.ItemStack;
import mindgo.logic.PlayerData;

public class ItemRoom extends CostRoom {
    Item item;

    public ItemRoom(Item item, int cost, int tx, int ty) {
        super(cost, tx, ty);
        this.item = item;
    }

    @Override
    public boolean canBuy(PlayerData pd) {
        return pd.data.money >= cost && pd.data.items.get(item).canAdd();
    }

    @Override
    public void onBuy(PlayerData pd) {
        super.onBuy(pd);
        if (pd.data.items.containsKey(item)) {
            pd.data.items.get(item).add();
        } else {
            pd.data.items.put(item, new ItemStack(item));
            pd.data.items.get(item).add();
            pd.needHudUpdate = true;
        }
    }
}
