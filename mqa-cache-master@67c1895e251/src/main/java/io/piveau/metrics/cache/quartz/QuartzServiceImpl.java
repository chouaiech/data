package io.piveau.metrics.cache.quartz;

import io.piveau.metrics.cache.dqv.DqvProvider;
import io.piveau.metrics.cache.persistence.DatabaseProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzServiceImpl implements QuartzService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Scheduler scheduler;

    private final DatabaseProvider databaseProvider;
    private final DqvProvider dqvProvider;

    QuartzServiceImpl(JobFactory jobFactory, DatabaseProvider databaseProvider, DqvProvider dqvProvider, Handler<AsyncResult<QuartzService>> readyHandler) {
        this.databaseProvider = databaseProvider;
        this.dqvProvider = dqvProvider;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.setJobFactory(jobFactory);
            scheduler.start();
            readyHandler.handle(Future.succeededFuture(this));
        } catch (SchedulerException e) {
            log.error("Creating and starting quartz scheduler", e);
            readyHandler.handle(Future.failedFuture(e));
        }
    }

    @Override
    public Future<JsonObject> listTriggers() {
        JsonObject triggers = new JsonObject();
        try {
            scheduler.getTriggerGroupNames().forEach(group -> {
                JsonArray triggerArray = getTriggersB(group);
                triggers.put(group, triggerArray);
            });
            return Future.succeededFuture(triggers);
        } catch (SchedulerException e) {
            log.error("Get trigger key list", e);
            return Future.failedFuture(new ServiceException(500, e.getMessage()));
        }
    }

    @Override
    public Future<JsonArray> getTriggers(String catalogueId) {

        if (catalogueId.equals(QuartzService.ALL_CATALOGUES)) {
            return Future.future(promise -> {
                JsonArray triggerArray = getTriggersB(catalogueId);
                promise.complete(triggerArray);
            });
        }

        return Future.future(promise -> dqvProvider.listCatalogues(ar -> {
            if (ar.succeeded()) {
                if (ar.result().contains(catalogueId)) {
                    JsonArray triggerArray = getTriggersB(catalogueId);
                    promise.complete(triggerArray);
                } else {
                    promise.fail(new ServiceException(404, "Catalogue not found"));
                }
            } else {
                promise.fail(new ServiceException(500, ar.cause().getMessage()));
            }
        }));
    }

    @Override
    public Future<String> putTrigger(String pipeName, String triggerId, JsonObject triggerObject) {
        //TODO: For single catalogue Check this should check if the catalogue exists
        return Future.future(promise -> {
            JobKey jobKey = jobKey(pipeName, pipeName);
            try {
                String status = "created";

                Trigger trigger = createTrigger(pipeName, triggerObject);
                if (trigger != null) {
                    if (scheduler.checkExists(trigger.getKey())) {
                        scheduler.rescheduleJob(trigger.getKey(), trigger);
                        status = "updated";
                    } else if (scheduler.checkExists(jobKey)) {
                        scheduler.scheduleJob(trigger);
                    } else {
                        JobDetail detail = newJob(RefreshJob.class).withIdentity(pipeName, pipeName).build();
                        scheduler.scheduleJob(detail, trigger);
                    }
                }

                promise.complete(status);
            } catch (SchedulerException e) {
                log.error("Scheduling", e);
                promise.fail(new ServiceException(500, e.getMessage()));
            } catch (Exception e) {
                log.error("Scheduling general error", e);
                promise.fail(new ServiceException(501, e.getMessage()));
            }

        });
    }

    @Override
    public Future<Void> deleteTriggers(String catalogueId) {
        //TODO: For single catalogue Check this should check if the catalogue exists
        return Future.future(promise -> {
            JobKey jobKey = jobKey(catalogueId, catalogueId);
            try {
                if (scheduler.checkExists(jobKey)) {
                    scheduler.deleteJob(jobKey);
                }
                promise.complete();
            } catch (SchedulerException e) {
                promise.fail(new ServiceException(500, e.getMessage()));
            }

        });
    }

    @Override
    public Future<JsonObject> getTrigger(String pipeName, String triggerId) {
        //TODO: For single catalogue Check this should check if the catalogue exists
        return Future.future(promise -> {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerId, pipeName);
            try {
                if (scheduler.checkExists(triggerKey)) {
                    Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(triggerId, pipeName));
                    JsonObject triggerObject = new JsonObject(trigger.getJobDataMap().getString("triggerObject"));
                    triggerObject.put("next", trigger.getNextFireTime().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    promise.complete(triggerObject);
                } else {
                    promise.fail(new ServiceException(404, "Trigger not found"));
                }
            } catch (SchedulerException e) {
                promise.fail(new ServiceException(500, e.getMessage()));
            }

        });
    }

    @Override
    public Future<JsonObject> patchTrigger(String pipeName, String triggerId, JsonObject patch) {
        //TODO: For single catalogue Check this should check if the catalogue exists
        return Future.future(promise -> {
            try {
                TriggerKey triggerKey = TriggerKey.triggerKey(triggerId, pipeName);
                if (!scheduler.checkExists(triggerKey)) {
                    promise.fail(new ServiceException(404, "Trigger not found"));
                } else {
                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    JsonObject triggerObject = new JsonObject(trigger.getJobDataMap().getString("triggerObject"));

                    triggerObject.mergeIn(patch, true);

                    String status = triggerObject.getString("status", "enabled");

                    Trigger newTrigger = createTrigger(pipeName, triggerObject);

                    scheduler.rescheduleJob(triggerKey, newTrigger);
                    if (status.equals("disabled")) {
                        scheduler.pauseTrigger(triggerKey);
                    } else {
                        scheduler.resumeTrigger(triggerKey);
                    }
                    promise.complete(triggerObject);
                }
            } catch (SchedulerException e) {
                promise.fail(new ServiceException(500, e.getMessage()));
            }

        });
    }

    @Override
    public Future<Void> deleteTrigger(String pipeName, String triggerId) {
        //TODO: For single catalogue Check this should check if the catalogue exists
        return Future.future(promise -> {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerId, pipeName);
            try {
                if (scheduler.checkExists(triggerKey)) {
                    scheduler.unscheduleJob(triggerKey);
                    promise.complete();
                } else {
                    promise.fail(new ServiceException(404, "Trigger not found"));
                }
            } catch (SchedulerException e) {
                promise.fail(new ServiceException(500, e.getMessage()));
            }

        });
    }

    private Trigger createTrigger(String key, JsonObject triggerObject) {
        Trigger trigger = null;

        String id = triggerObject.getString("id");

        JobKey jobKey = jobKey(key, key);

        if (triggerObject.containsKey("interval")) {

            JsonObject interval = triggerObject.getJsonObject("interval");
            String unit = interval.getString("unit");
            int value = interval.getInteger("value");
            CalendarIntervalScheduleBuilder scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withInterval(value, DateBuilder.IntervalUnit.valueOf(unit));
            TriggerBuilder<CalendarIntervalTrigger> builder = newTrigger().withIdentity(id, key).forJob(jobKey).usingJobData("triggerObject", triggerObject.encodePrettily()).withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing());

            evaluateNext(triggerObject, builder);

            trigger = builder.build();
        } else if (triggerObject.containsKey("cron")) {

            String cron = triggerObject.getString("cron");
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            TriggerBuilder<CronTrigger> builder = newTrigger().withIdentity(id, key).forJob(jobKey).usingJobData("triggerObject", triggerObject.encodePrettily()).withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing());

            evaluateNext(triggerObject, builder);

            trigger = builder.build();
        } else if (triggerObject.containsKey("specific")) {
            JsonArray specifics = triggerObject.getJsonArray("specific");
            String triggerKey = id;
            int count = 1;
            for (Object specific : specifics) {
                String dateTime = specific.toString();
                TriggerBuilder<Trigger> builder = newTrigger().withIdentity(triggerKey, key).forJob(jobKey).usingJobData("triggerObject", triggerObject.encodePrettily());
                triggerKey = id + ++count;
                Date start = Date.from(ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME).toInstant());
                builder.startAt(start);
                trigger = builder.build();
            }
        } else {
            TriggerBuilder<Trigger> builder = newTrigger().withIdentity(id, key).forJob(jobKey).usingJobData("triggerObject", triggerObject.encodePrettily());
            trigger = builder.build();
        }
        return trigger;
    }

    private void evaluateNext(JsonObject triggerObject, TriggerBuilder<?> triggerBuilder) {
        Date next = triggerObject.containsKey("next") ? Date.from(ZonedDateTime.parse(triggerObject.getString("next")).toInstant()) : DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE);
        triggerBuilder.startAt(next);
    }

    private JsonArray getTriggersB(String pipeId) {
        JsonArray triggerArray = new JsonArray();
        try {
            Set<TriggerKey> groupTriggers = scheduler.getTriggerKeys(GroupMatcher.groupEquals(pipeId));
            groupTriggers.iterator().forEachRemaining(key -> {
                try {
                    Trigger trigger = scheduler.getTrigger(key);
                    JsonObject triggerObject = new JsonObject(trigger.getJobDataMap().getString("triggerObject"));
                    triggerObject.put("next", trigger.getNextFireTime().toInstant().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    triggerObject.put("status", (scheduler.getTriggerState(key) == Trigger.TriggerState.PAUSED ? "disabled" : "enabled"));
                    triggerArray.add(triggerObject);
                } catch (SchedulerException e) {
                    log.error("Get trigger from key", e);
                }
            });
            return triggerArray;
        } catch (SchedulerException e) {
            log.error("Get triggers", e);
            return triggerArray;
        }
    }

}
