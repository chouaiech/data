package io.piveau.metrics.cache.quartz;

import io.piveau.metrics.cache.persistence.DatabaseProvider;
import io.vertx.core.Vertx;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.TriggerFiredBundle;

public class VertxJobFactory extends SimpleJobFactory {

    private final DatabaseProvider databaseProvider;

    static VertxJobFactory create(Vertx vertx) {
        DatabaseProvider databaseProvider = DatabaseProvider.createProxy(vertx, DatabaseProvider.SERVICE_ADDRESS);
        return new VertxJobFactory(databaseProvider);
    }

    private VertxJobFactory(DatabaseProvider databaseProvider) {
        this.databaseProvider = databaseProvider;
    }

    @Override
    public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
        final JobDetail jobDetail = triggerFiredBundle.getJobDetail();
        String catalog = jobDetail.getKey().getName();
        final Class<? extends Job> jobClass = jobDetail.getJobClass();
        try {
            return jobClass.getConstructor(DatabaseProvider.class, String.class).newInstance(databaseProvider, catalog);
        } catch (Exception e) {
            throw new SchedulerException("Could not create a job of type " + jobClass);
        }
    }

}
