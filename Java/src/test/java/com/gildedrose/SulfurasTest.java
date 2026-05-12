package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SulfurasTest {

    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";

    private Item update(int sellIn, int quality) {
        GildedRose app = new GildedRose(new Item[]{new Item(SULFURAS, sellIn, quality)});
        app.updateQuality();
        return app.items[0];
    }

    @Test
    void qualityNeverChanges() {
        assertEquals(80, update(10, 80).quality);
    }

    @Test
    void sellInNeverChanges() {
        assertEquals(10, update(10, 80).sellIn);
    }

    @Test
    void qualityRemainsEightyPastSellDate() {
        assertEquals(80, update(-1, 80).quality);
    }
}
