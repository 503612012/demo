package com.oven.demo.core.task;

import com.oven.demo.common.util.LogQueueUtils;
import com.oven.demo.core.crontab.service.CrontabService;
import com.oven.demo.core.log.entity.Log;
import com.oven.demo.core.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务-保存操作日志
 *
 * @author Oven
 */
@Slf4j
@Component
public class LogIntoDbTask implements SchedulingConfigurer {

    @Resource
    private LogService logService;
    @Resource
    private CrontabService crontabService;

    /**
     * 保存操作日志到数据库
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(this::doJob, triggerContext -> {
            String cron = crontabService.getCron("LOG_CRON");
            if (StringUtils.isEmpty(cron)) {
                log.error("cron is null...");
            }
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });
    }

    private void doJob() {
        List<Log> list = LogQueueUtils.getInstance().drainTo(null);
        if (!CollectionUtils.isEmpty(list)) {
            logService.batchSave(list);
            log.info("成功保存{}条操作日志到数据库。", list.size());
        }
    }

}
