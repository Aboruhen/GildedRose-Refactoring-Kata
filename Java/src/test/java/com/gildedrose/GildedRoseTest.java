package com.gildedrose;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    private static final String AGED_BRIE      = "Aged Brie";
    private static final String SULFURAS       = "Sulfuras, Hand of Ragnaros";
    private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    private static final String CONJURED       = "Conjured Mana Cake";
    private static final String NORMAL         = "Normal Item";

    /** Convenience: create a single-item shop, run one day, return the item. */
    private Item update(String name, int sellIn, int quality) {
        GildedRose app = new GildedRose(new Item[]{new Item(name, sellIn, quality)});
        app.updateQuality();
        return app.items[0];
    }

    // -----------------------------------------------------------------------
    // Normal items
    // -----------------------------------------------------------------------
    @Nested
    class NormalItem {

        @Test
        void decreasesQualityByOneBeforeSellDate() {
            assertEquals(19, update(NORMAL, 10, 20).quality);
        }

        @Test
        void decreasesSellInByOneEachDay() {
            assertEquals(9, update(NORMAL, 10, 20).sellIn);
        }

        @Test
        void decreasesQualityByTwoOnSellDate() {
            // sellIn 0 → becomes -1 after update → double degradation applies
            assertEquals(18, update(NORMAL, 0, 20).quality);
        }

        @Test
        void decreasesQualityByTwoAfterSellDate() {
            assertEquals(18, update(NORMAL, -1, 20).quality);
        }

        @Test
        void qualityNeverNegativeBeforeSellDate() {
            assertEquals(0, update(NORMAL, 5, 0).quality);
        }

        @Test
        void qualityNeverNegativeAfterSellDate() {
            assertEquals(0, update(NORMAL, -1, 1).quality);
        }

        @Test
        void qualityNeverNegativeWhenAlreadyZeroAfterSellDate() {
            assertEquals(0, update(NORMAL, -5, 0).quality);
        }
    }

    // -----------------------------------------------------------------------
    // Aged Brie
    // -----------------------------------------------------------------------
    @Nested
    class AgedBrie {

        @Test
        void increasesQualityByOneBeforeSellDate() {
            assertEquals(21, update(AGED_BRIE, 10, 20).quality);
        }

        @Test
        void decreasesSellInByOneEachDay() {
            assertEquals(9, update(AGED_BRIE, 10, 20).sellIn);
        }

        @Test
        void increasesQualityByTwoOnSellDate() {
            assertEquals(22, update(AGED_BRIE, 0, 20).quality);
        }

        @Test
        void increasesQualityByTwoAfterSellDate() {
            assertEquals(22, update(AGED_BRIE, -1, 20).quality);
        }

        @Test
        void qualityNeverExceedsFifty() {
            assertEquals(50, update(AGED_BRIE, 10, 50).quality);
        }

        @Test
        void qualityStopsAtFiftyEvenWithPostSellDateBoost() {
            assertEquals(50, update(AGED_BRIE, -1, 49).quality);
        }
    }

    // -----------------------------------------------------------------------
    // Sulfuras
    // -----------------------------------------------------------------------
    @Nested
    class Sulfuras {

        @Test
        void qualityNeverChanges() {
            assertEquals(80, update(SULFURAS, 10, 80).quality);
        }

        @Test
        void sellInNeverChanges() {
            assertEquals(10, update(SULFURAS, 10, 80).sellIn);
        }

        @Test
        void qualityRemainsEightyPastSellDate() {
            assertEquals(80, update(SULFURAS, -1, 80).quality);
        }
    }

    // -----------------------------------------------------------------------
    // Backstage passes
    // -----------------------------------------------------------------------
    @Nested
    class BackstagePasses {

        @ParameterizedTest(name = "sellIn={0} → quality 20 becomes 21")
        @CsvSource({
            "15",   // well above the 10-day threshold
            "11",   // exact upper boundary of the +1 zone
        })
        void increasesByOneWhenSellInAboveTen(int sellIn) {
            assertEquals(21, update(BACKSTAGE_PASS, sellIn, 20).quality);
        }

        @Test
        void decreasesSellInByOneEachDay() {
            assertEquals(9, update(BACKSTAGE_PASS, 10, 20).sellIn);
        }

        @ParameterizedTest(name = "sellIn={0} → quality 20 becomes {1}")
        @CsvSource({
            "10, 22",   // exactly 10 days left  → +2
            "9,  22",   // 9 days                → +2
            "6,  22",   // 6 days                → +2
        })
        void increasesByTwoInTenDayWindow(int sellIn, int expectedQuality) {
            assertEquals(expectedQuality, update(BACKSTAGE_PASS, sellIn, 20).quality);
        }

        @ParameterizedTest(name = "sellIn={0} → quality 20 becomes {1}")
        @CsvSource({
            "5, 23",    // exactly 5 days left   → +3
            "4, 23",    // 4 days                → +3
            "1, 23",    // last day              → +3
        })
        void increasesByThreeInFiveDayWindow(int sellIn, int expectedQuality) {
            assertEquals(expectedQuality, update(BACKSTAGE_PASS, sellIn, 20).quality);
        }

        @Test
        void dropsToZeroOnSellDate() {
            // sellIn 0 → becomes -1 → concert is over
            assertEquals(0, update(BACKSTAGE_PASS, 0, 20).quality);
        }

        @Test
        void dropsToZeroAfterConcert() {
            assertEquals(0, update(BACKSTAGE_PASS, -1, 20).quality);
        }

        @ParameterizedTest(name = "sellIn={0}, quality={1} → capped at 50")
        @CsvSource({
            "10, 49",   // +2 from 49 would reach 51
            "5,  49",   // +3 from 49 would reach 52
            "5,  48",   // +3 from 48 would reach 51
        })
        void qualityNeverExceedsFifty(int sellIn, int initialQuality) {
            assertEquals(50, update(BACKSTAGE_PASS, sellIn, initialQuality).quality);
        }
    }

    // -----------------------------------------------------------------------
    // Conjured items
    // -----------------------------------------------------------------------
    @Nested
    class ConjuredItems {

        @Test
        void decreasesQualityByTwoBeforeSellDate() {
            assertEquals(18, update(CONJURED, 10, 20).quality);
        }

        @Test
        void decreasesSellInByOneEachDay() {
            assertEquals(9, update(CONJURED, 10, 20).sellIn);
        }

        @Test
        void decreasesQualityByFourOnSellDate() {
            assertEquals(16, update(CONJURED, 0, 20).quality);
        }

        @Test
        void decreasesQualityByFourAfterSellDate() {
            assertEquals(16, update(CONJURED, -1, 20).quality);
        }

        @Test
        void qualityNeverNegativeBeforeSellDate() {
            assertEquals(0, update(CONJURED, 5, 1).quality);
        }

        @Test
        void qualityNeverNegativeAfterSellDate() {
            assertEquals(0, update(CONJURED, -1, 3).quality);
        }

        @Test
        void qualityNeverNegativeWhenAlreadyZero() {
            assertEquals(0, update(CONJURED, -1, 0).quality);
        }
    }

    // -----------------------------------------------------------------------
    // Global invariants
    // -----------------------------------------------------------------------
    @Nested
    class GlobalInvariants {

        @Test
        void qualityIsNeverNegativeForAnyItemType() {
            Item[] items = {
                new Item(NORMAL,         0,  0),
                new Item(BACKSTAGE_PASS, -1, 0),
                new Item(CONJURED,       -1, 0),
            };
            new GildedRose(items).updateQuality();

            assertAll(
                () -> assertEquals(true, items[0].quality >= 0, NORMAL + " quality negative"),
                () -> assertEquals(true, items[1].quality >= 0, BACKSTAGE_PASS + " quality negative"),
                () -> assertEquals(true, items[2].quality >= 0, CONJURED + " quality negative")
            );
        }

        @Test
        void qualityNeverExceedsFiftyForNonLegendaryItems() {
            Item[] items = {
                new Item(AGED_BRIE,      5, 50),
                new Item(BACKSTAGE_PASS, 5, 50),
            };
            new GildedRose(items).updateQuality();

            assertAll(
                () -> assertEquals(true, items[0].quality <= 50, AGED_BRIE + " quality exceeded 50"),
                () -> assertEquals(true, items[1].quality <= 50, BACKSTAGE_PASS + " quality exceeded 50")
            );
        }

        @Test
        void multipleItemsAreUpdatedIndependentlyInOnePas() {
            Item[] items = {
                new Item(NORMAL,   10, 20),
                new Item(AGED_BRIE, 10, 20),
                new Item(SULFURAS,  10, 80),
            };
            new GildedRose(items).updateQuality();

            assertAll(
                () -> assertEquals(19, items[0].quality, "Normal item quality"),
                () -> assertEquals(21, items[1].quality, "Aged Brie quality"),
                () -> assertEquals(80, items[2].quality, "Sulfuras quality")
            );
        }
    }
}
