package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackstagePassesTest {

    private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";

    private Item update(int sellIn, int quality) {
        GildedRose app = new GildedRose(new Item[]{new Item(BACKSTAGE_PASS, sellIn, quality)});
        app.updateQuality();
        return app.items[0];
    }

    @ParameterizedTest(name = "sellIn={0} → quality 20 becomes 21")
    @CsvSource({
        "15",   // well above the 10-day threshold
        "11",   // exact upper boundary of the +1 zone
    })
    void increasesByOneWhenSellInAboveTen(int sellIn) {
        assertEquals(21, update(sellIn, 20).quality);
    }

    @Test
    void decreasesSellInByOneEachDay() {
        assertEquals(9, update(10, 20).sellIn);
    }

    @ParameterizedTest(name = "sellIn={0} → quality 20 becomes {1}")
    @CsvSource({
        "10, 22",   // exactly 10 days left  → +2
        "9,  22",   // 9 days                → +2
        "6,  22",   // 6 days                → +2
    })
    void increasesByTwoInTenDayWindow(int sellIn, int expectedQuality) {
        assertEquals(expectedQuality, update(sellIn, 20).quality);
    }

    @ParameterizedTest(name = "sellIn={0} → quality 20 becomes {1}")
    @CsvSource({
        "5, 23",    // exactly 5 days left   → +3
        "4, 23",    // 4 days                → +3
        "1, 23",    // last day              → +3
    })
    void increasesByThreeInFiveDayWindow(int sellIn, int expectedQuality) {
        assertEquals(expectedQuality, update(sellIn, 20).quality);
    }

    @Test
    void dropsToZeroOnSellDate() {
        assertEquals(0, update(0, 20).quality);
    }

    @Test
    void dropsToZeroAfterConcert() {
        assertEquals(0, update(-1, 20).quality);
    }

    @ParameterizedTest(name = "sellIn={0}, quality={1} → capped at 50")
    @CsvSource({
        "10, 49",   // +2 from 49 would reach 51
        "5,  49",   // +3 from 49 would reach 52
        "5,  48",   // +3 from 48 would reach 51
    })
    void qualityNeverExceedsFifty(int sellIn, int initialQuality) {
        assertEquals(50, update(sellIn, initialQuality).quality);
    }
}
