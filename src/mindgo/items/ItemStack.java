package mindgo.items;

import mindgo.logic.PlayerData;

public class ItemStack {
    public Item item;
    public int maxCount;
    public int count;

    public ItemStack(Item item) {
        this.item = item;
        this.maxCount = item.maxStuck;
        this.count = 0;
        if (item == Items.nil) count = 99999;
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

    public boolean empty() {
        return count <= 0;
    }
}
