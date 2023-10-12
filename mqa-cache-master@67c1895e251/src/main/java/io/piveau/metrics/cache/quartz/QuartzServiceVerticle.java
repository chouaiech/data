package io.piveau.metrics.cache.quartz;

import io.piveau.metrics.cache.ApplicationConfig;
import io.piveau.metrics.cache.dqv.DqvProvider;
import io.piveau.metrics.cache.persistence.DatabaseProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.serviceproxy.ServiceBinder;
import org.h2.tools.RunScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class QuartzServiceVerticle extends AbstractVerticle {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void start(Promise<Void> startPromise) {

        initH2();

        DatabaseProvider databaseProvider = DatabaseProvider.createProxy(vertx, DatabaseProvider.SERVICE_ADDRESS);

        DeliveryOptions options1 = new DeliveryOptions().setSendTimeout(3000000);
        DqvProvider dqvProvider = DqvProvider.createProxy(vertx, DqvProvider.SERVICE_ADDRESS, options1);

        QuartzService.create(VertxJobFactory.create(vertx), databaseProvider, dqvProvider, ready -> {
            if (ready.succeeded()) {
                new ServiceBinder(vertx).setAddress(QuartzService.SERVICE_ADDRESS).register(QuartzService.class, ready.result());
                startPromise.complete();
            } else {
                startPromise.fail(ready.cause());
            }
        });
    }

    private void initH2() {
        String pw = config().getString(ApplicationConfig.ENV_H2_PASSWORD, ApplicationConfig.DEFAULT_H2_PASSWORD);
        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./db/quartzdb", "sa", pw)) {
            ResultSet rset = connection.getMetaData().getTables(null, null, "QRTZ_TRIGGERS", null);
            if (!rset.next()) {
                try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("tables_h2.sql")) {
                    if (inputStream != null) {
                        RunScript.execute(connection, new InputStreamReader(inputStream));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Init H2 db", e);
        }
    }

}
