package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NormalItemTest {

    private static final String NORMAL = "Normal Item";

    private Item update(int sellIn, int quality) {
        GildedRose app = new GildedRose(new Item[]{new Item(NORMAL, sellIn, quality)});
        app.updateQuality();
        return app.items[0];
    }

    @Test
    void decreasesQualityByOneBeforeSellDate() {
        assertEquals(19, update(10, 20).quality);
    }

    @Test
    void decreasesSellInByOneEachDay() {
        assertEquals(9, update(10, 20).sellIn);
    }

    @Test
    void decreasesQualityByTwoOnSellDate() {
        assertEquals(18, update(0, 20).quality);
    }

    @Test
    void decreasesQualityByTwoAfterSellDate() {
        assertEquals(18, update(-1, 20).quality);
    }

    @Test
    void qualityNeverNegativeBeforeSellDate() {
        assertEquals(0, update(5, 0).quality);
    }

    @Test
    void qualityNeverNegativeAfterSellDate() {
        assertEquals(0, update(-1, 1).quality);
    }

    @Test
    void qualityNeverNegativeWhenAlreadyZeroAfterSellDate() {
        assertEquals(0, update(-5, 0).quality);
    }
}
