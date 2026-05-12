package com.gildedrose.strategy;

import com.gildedrose.Item;
import com.gildedrose.ItemUpdater;

public class SulfurasUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        // Legendary item: quality is always 80, sellIn never moves
    }
}
