package io.piveau.metrics.cache.quartz;


import io.piveau.metrics.cache.persistence.DatabaseProvider;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class RefreshJob implements Job {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final DatabaseProvider databaseProvider;
    private final String catalogue;

    public RefreshJob(DatabaseProvider databaseProvider, String catalogue) {
        this.databaseProvider = databaseProvider;
        this.catalogue = catalogue;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        String triggerObject = jobExecutionContext.getMergedJobDataMap().getString("triggerObject");
        log.debug("Job triggered [" + catalogue + "]: {}", triggerObject);

        if (catalogue.isEmpty() || catalogue.equals(QuartzServiceImpl.ALL_CATALOGUES)) {
            databaseProvider.refreshMetrics();
        } else {
            databaseProvider.refreshSingleMetrics(catalogue);
        }


    }

}
