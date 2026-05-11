package com.gildedrose;

class NormalItemUpdater implements ItemUpdater {

    @Override
    public void update(Item item) {
        item.sellIn--;
        int degradation = item.sellIn < 0 ? 2 : 1;
        item.quality = Math.max(0, item.quality - degradation);
    }
}
