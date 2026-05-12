package com.gildedrose;

import com.gildedrose.strategy.AgedBrieUpdater;
import com.gildedrose.strategy.BackstagePassUpdater;
import com.gildedrose.strategy.ConjuredItemUpdater;
import com.gildedrose.strategy.NormalItemUpdater;
import com.gildedrose.strategy.SulfurasUpdater;

class ItemUpdaterFactory {

    private static final String SULFURAS = resolve("GILDED_ROSE_SULFURAS", "gilded.rose.sulfuras",
        "Sulfuras, Hand of Ragnaros");
    private static final String AGED_BRIE = resolve("GILDED_ROSE_AGED_BRIE", "gilded.rose.aged-brie", "Aged Brie");
    private static final String BACKSTAGE_PASS = resolve("GILDED_ROSE_BACKSTAGE_PASS", "gilded.rose.backstage-pass",
        "Backstage passes to a TAFKAL80ETC concert");
    private static final String CONJURED_PREFIX = resolve("GILDED_ROSE_CONJURED_PREFIX", "gilded.rose.conjured-prefix",
        "Conjured");

    static ItemUpdater forItem(Item item) {
        return switch (item.name) {
            case String s when s.equals(SULFURAS) -> new SulfurasUpdater();
            case String s when s.equals(AGED_BRIE) -> new AgedBrieUpdater();
            case String s when s.equals(BACKSTAGE_PASS) -> new BackstagePassUpdater();
            case String s when s.startsWith(CONJURED_PREFIX) -> new ConjuredItemUpdater();
            default -> new NormalItemUpdater();
        };
    }

    private static String resolve(String envVar, String sysProp, String defaultValue) {
        String value = System.getProperty(sysProp);
        if (value != null && !value.isBlank()) {
            return value;
        }
        value = System.getenv(envVar);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return defaultValue;
    }

}
