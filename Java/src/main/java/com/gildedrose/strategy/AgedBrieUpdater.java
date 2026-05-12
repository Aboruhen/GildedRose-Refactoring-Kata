package com.gildedrose.strategy;

import com.gildedrose.Item;
import com.gildedrose.ItemUpdater;

public class AgedBrieUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        item.sellIn--;
        int increase = item.sellIn < 0 ? 2 : 1;
        item.quality = Math.min(50, item.quality + increase);
    }
}
