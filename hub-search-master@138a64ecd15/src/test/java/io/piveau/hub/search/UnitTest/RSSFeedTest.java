package io.piveau.hub.search.UnitTest;


import io.piveau.hub.search.util.feed.rss.RSSFeed;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing the RssFeed Class")
@ExtendWith(VertxExtension.class)
public class RSSFeedTest {
    private final String date = "2020-08-26T08:46:43Z";

    @Test
    @DisplayName("Testing add item without initializing")
    void testAddRssItem(Vertx vertx, VertxTestContext testContext) {

        new RSSFeed().addItem("guid", "title", "link", "description ", date);

        testContext.completeNow();
    }


    @Test
    @DisplayName("Testing creating new Enclosure with null parameters")
    void testNewEnclosureWithNull(Vertx vertx, VertxTestContext testContext) {

        RSSFeed.Enclosure enclosure = new RSSFeed.Enclosure(null, null, null);
        assertNull(enclosure.getLength());
        assertNull(enclosure.getType());
        assertNull(enclosure.getUrl());

        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing creating new Enclosure with null parameters")
    void testNewEnclosure(Vertx vertx, VertxTestContext testContext) {

        RSSFeed.Enclosure enclosure = new RSSFeed.Enclosure("url", "application/pdf", 3000);
        assertNotNull(enclosure.getLength());
        assertEquals("3000", enclosure.getLength());
        assertNotNull(enclosure.getType());
        assertNotNull(enclosure.getUrl());

        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing creating new Item with null parameters")
    void testNewItemWithNull(Vertx vertx, VertxTestContext testContext) {
        RSSFeed.Item item = new RSSFeed.Item(null, null, null, null, null);
        assertNull(item.getDescription());
        assertNull(item.getGuid());
        assertNull(item.getLink());
        assertNull(item.getPubDate());
        assertNull(item.getTitle());
        assertNotNull(item.getEnclosures());
        assertTrue(item.getEnclosures().isEmpty());
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing creating new Item with null parameters except the date")
    void testNewItemWithNullAndDate(Vertx vertx, VertxTestContext testContext) {

        RSSFeed.Item item = new RSSFeed.Item(null, null, null, null, date);
        assertNull(item.getDescription());
        assertNull(item.getGuid());
        assertNull(item.getLink());
        assertNotNull(item.getPubDate());
        assertNull(item.getTitle());
        assertNotNull(item.getEnclosures());
        assertTrue(item.getEnclosures().isEmpty());
        testContext.completeNow();
    }

    @Test
    @DisplayName("Testing creating new Item")
    void testNewItem(Vertx vertx, VertxTestContext testContext) {

        RSSFeed.Item item = new RSSFeed.Item("guid", "title", "link", "description", date);

        assertNotNull(item.getDescription());
        assertEquals("description", item.getDescription());
        assertNotNull(item.getGuid());
        assertNotNull(item.getLink());
        assertNotNull(item.getPubDate());
        assertNotNull(item.getTitle());
        assertNotNull(item.getEnclosures());
        assertTrue(item.getEnclosures().isEmpty());
        testContext.completeNow();
    }
}
