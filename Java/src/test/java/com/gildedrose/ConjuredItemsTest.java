package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConjuredItemsTest {

    private static final String CONJURED = "Conjured Mana Cake";

    private Item update(int sellIn, int quality) {
        GildedRose app = new GildedRose(new Item[]{new Item(CONJURED, sellIn, quality)});
        app.updateQuality();
        return app.items[0];
    }

    @Test
    void decreasesQualityByTwoBeforeSellDate() {
        assertEquals(18, update(10, 20).quality);
    }

    @Test
    void decreasesSellInByOneEachDay() {
        assertEquals(9, update(10, 20).sellIn);
    }

    @Test
    void decreasesQualityByFourOnSellDate() {
        assertEquals(16, update(0, 20).quality);
    }

    @Test
    void decreasesQualityByFourAfterSellDate() {
        assertEquals(16, update(-1, 20).quality);
    }

    @Test
    void qualityNeverNegativeBeforeSellDate() {
        assertEquals(0, update(5, 1).quality);
    }

    @Test
    void qualityNeverNegativeAfterSellDate() {
        assertEquals(0, update(-1, 3).quality);
    }

    @Test
    void qualityNeverNegativeWhenAlreadyZero() {
        assertEquals(0, update(-1, 0).quality);
    }
}
