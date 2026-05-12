package com.gildedrose.strategy;

import com.gildedrose.Item;
import com.gildedrose.ItemUpdater;

public class BackstagePassUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        item.sellIn--;

        if (item.sellIn < 0) {
            item.quality = 0;
            return;
        }

        int increase = 1;
        if (item.sellIn < 10) {
            increase++;
        }
        if (item.sellIn < 5) {
            increase++;
        }

        item.quality = Math.min(50, item.quality + increase);
    }
}
