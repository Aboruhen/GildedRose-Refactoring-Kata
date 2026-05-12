package com.gildedrose;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AgedBrieTest {

    private static final String AGED_BRIE = "Aged Brie";

    private Item update(int sellIn, int quality) {
        GildedRose app = new GildedRose(new Item[]{new Item(AGED_BRIE, sellIn, quality)});
        app.updateQuality();
        return app.items[0];
    }

    @Test
    void increasesQualityByOneBeforeSellDate() {
        assertEquals(21, update(10, 20).quality);
    }

    @Test
    void decreasesSellInByOneEachDay() {
        assertEquals(9, update(10, 20).sellIn);
    }

    @Test
    void increasesQualityByTwoOnSellDate() {
        assertEquals(22, update(0, 20).quality);
    }

    @Test
    void increasesQualityByTwoAfterSellDate() {
        assertEquals(22, update(-1, 20).quality);
    }

    @Test
    void qualityNeverExceedsFifty() {
        assertEquals(50, update(10, 50).quality);
    }

    @Test
    void qualityStopsAtFiftyEvenWithPostSellDateBoost() {
        assertEquals(50, update(-1, 49).quality);
    }
}
