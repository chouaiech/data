package io.piveau.hub.search.handler;

import io.piveau.hub.search.services.sitemaps.SitemapsService;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SitemapHandler extends ContextHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SitemapHandler.class);

    private final SitemapsService sitemapsService;

    public SitemapHandler(Vertx vertx, String address) {
        sitemapsService = SitemapsService.createProxy(vertx, address);
    }
    public void readSitemapIndex(RoutingContext context)  {
        LOG.debug("Read sitemap index, remote address: {}", context.request().connection().remoteAddress());
        sitemapsService.readSitemapIndex().onComplete(ar -> handleContextXML(context, ar));
    }

    public void readSitemap(RoutingContext context) {
        LOG.debug("Read sitemap, remote address: {}", context.request().connection().remoteAddress());
        sitemapsService.readSitemap(context.request().getParam("id")).onComplete(ar -> handleContextXML(context, ar));
    }
}
