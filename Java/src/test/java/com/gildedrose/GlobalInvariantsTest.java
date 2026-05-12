package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalInvariantsTest {

    private static final String NORMAL         = "Normal Item";
    private static final String AGED_BRIE      = "Aged Brie";
    private static final String SULFURAS       = "Sulfuras, Hand of Ragnaros";
    private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    private static final String CONJURED       = "Conjured Mana Cake";

    @Test
    void qualityIsNeverNegativeForAnyItemType() {
        Item[] items = {
            new Item(NORMAL,         0,  0),
            new Item(BACKSTAGE_PASS, -1, 0),
            new Item(CONJURED,       -1, 0),
        };
        new GildedRose(items).updateQuality();

        assertAll(
            () -> assertTrue(items[0].quality >= 0, NORMAL + " quality negative"),
            () -> assertTrue(items[1].quality >= 0, BACKSTAGE_PASS + " quality negative"),
            () -> assertTrue(items[2].quality >= 0, CONJURED + " quality negative")
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
            () -> assertTrue(items[0].quality <= 50, AGED_BRIE + " quality exceeded 50"),
            () -> assertTrue(items[1].quality <= 50, BACKSTAGE_PASS + " quality exceeded 50")
        );
    }

    @Test
    void multipleItemsAreUpdatedIndependentlyInOnePass() {
        Item[] items = {
            new Item(NORMAL,    10, 20),
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
