package mindgo.items;

import mindgo.logic.PlayerData;

public class ItemStack {
    public Item item;
    public int maxCount;
    public int count;

    public ItemStack(Item item, int maxCount) {
        this.item = item;
        this.maxCount = maxCount;
        this.count = 0;
    }

    public boolean canAdd() {
        return count < maxCount;
    }

    public void add() {
        count++;
    }

    public void useItem(PlayerData pd) {
        count--;
        item.use.get(pd);
    }
}
