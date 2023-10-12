package io.piveau.hub.search.util.feed.rss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@XmlRootElement(name = "rss")
public class RSSFeed {
    private static final Logger LOG = LoggerFactory.getLogger(RSSFeed.class);
    @XmlAttribute
    private final String version = "2.0";
    @XmlElement
    private Channel channel;

    public RSSFeed() {
        this.channel = new Channel();
    }

    public RSSFeed(String title, String link, String description, String language, String copyright) {
        this.channel = new Channel(title, link, description, language, copyright);
    }

    public RSSFeed.Item addItem(String guid, String title, String link, String description, String pubDate) {
        RSSFeed.Item item = new RSSFeed.Item(guid, title, link, description, pubDate);

        channel.getItems().add(item);

        return item;
    }

    public String getVersion() {
        return version;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "RSSFeed{" +
                "version='" + version + '\'' +
                ", channel=" + channel +
                '}';
    }

    public static class Enclosure {
        @XmlAttribute
        private String url;
        @XmlAttribute
        private String type;
        @XmlAttribute
        private String length;

        public Enclosure() {
            this.url = "";
            this.type = "";
            this.length = "";
        }

        public Enclosure(String url, String type, Integer length) {
            this.url = url;
            this.type = type;
            this.length = length !=null?Integer.toString(length):null;
        }

        public String getUrl() {
            return url;
        }

        public String getType() {
            return type;
        }

        public String getLength() {
            return length;
        }

        @Override
        public String toString() {
            return "Enclosure{" +
                    "url='" + url + '\'' +
                    ", type='" + type + '\'' +
                    ", length='" + length + '\'' +
                    '}';
        }
    }

    public static class Item {
        @XmlElement
        private String guid;
        @XmlElement
        private String title;
        @XmlElement
        private String link;
        @XmlElement
        private String description;
        @XmlElement(name = "enclosure")
        private List<RSSFeed.Enclosure> enclosures;
        @XmlElement
        private String pubDate;

        public Item() {
            this.guid = "";
            this.title = "";
            this.link = "";
            this.description = "";
            this.enclosures = new ArrayList<>();
            this.pubDate = "";
        }

        public Item(String guid, String title, String link, String description, String pubDate) {
            this.guid = guid;
            this.title = title;
            this.link = link;
            this.description = description;
            this.enclosures = new ArrayList<>();
            try {
                this.pubDate = pubDate != null ? new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(Date.from(Instant.parse(pubDate))) : null;
            } catch (DateTimeParseException e){
                LOG.warn("Date could not be parsed: " + pubDate, e);
                this.pubDate = pubDate;
            }

        }

        public void addEnclosure(String url, String type, Integer length) {
            enclosures.add(new RSSFeed.Enclosure(url, type, length));
        }

        public String getGuid() {
            return guid;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getDescription() {
            return description;
        }

        public List<Enclosure> getEnclosures() {
            return enclosures;
        }

        public String getPubDate() {
            return pubDate;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "guid='" + guid + '\'' +
                    ", title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", description='" + description + '\'' +
                    ", enclosures=" + enclosures +
                    ", pubDate='" + pubDate + '\'' +
                    '}';
        }
    }

    public static class Channel {
        @XmlElement
        private String title;
        @XmlElement
        private String link;
        @XmlElement
        private String description;
        @XmlElement
        private String language;
        @XmlElement
        private String copyright;
        @XmlElement
        private String pubDate;
        @XmlElement(name = "item")
        private List<RSSFeed.Item> items;

        public Channel() {
            this.title = "";
            this.link = "";
            this.description = "";
            this.language = "";
            this.copyright = "";
            this.pubDate = "";
            this.items = new ArrayList<>();
        }

        public Channel(String title, String link, String description, String language, String copyright) {
            this.title = title + " - RSS Feed";
            this.link = link;
            this.description = description;
            this.language = language;
            this.copyright = copyright;
            this.pubDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH).format(new Date());
            this.items = new ArrayList<>();
        }

        public List<Item> getItems() {
            return items;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }

        public String getDescription() {
            return description;
        }

        public String getLanguage() {
            return language;
        }

        public String getCopyright() {
            return copyright;
        }

        public String getPubDate() {
            return pubDate;
        }

        @Override
        public String toString() {
            return "Channel{" +
                    "title='" + title + '\'' +
                    ", link='" + link + '\'' +
                    ", description='" + description + '\'' +
                    ", language='" + language + '\'' +
                    ", copyright='" + copyright + '\'' +
                    ", pubDate='" + pubDate + '\'' +
                    ", items=" + items +
                    '}';
        }
    }
}
