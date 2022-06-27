package com.oven.demo.core.task;

import com.oven.demo.common.requestLog.RequestLog;
import com.oven.demo.common.requestLog.RequestLogService;
import com.oven.demo.common.util.RequestLogQueueUtils;
import com.oven.demo.core.crontab.service.CrontabService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务-保存接口请求记录
 *
 * @author Oven
 */
@Slf4j
@Component
public class RequestLogIntoDbTask implements SchedulingConfigurer {

    @Resource
    private CrontabService crontabService;
    @Resource
    private RequestLogService requestLogService;

    /**
     * 保存接口访问日志到数据库
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(this::doSomething, triggerContext -> {
            String cron = crontabService.getCron("REQUEST_LOG_CRON");
            if (StringUtils.isEmpty(cron)) {
                log.error("cron is null...");
            }
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });
    }

    private void doSomething() {
        List<RequestLog> list = RequestLogQueueUtils.getInstance().drainTo(null);
        if (!CollectionUtils.isEmpty(list)) {
            String tableName = "t_request_log_" + DateTime.now().toString("yyyyMM");
            if (!requestLogService.isExist(tableName)) {
                requestLogService.createTable(tableName);
            }
            requestLogService.batchSave(list, tableName);
            log.info("成功保存{}条接口访问日志到数据库。", list.size());
        }
    }

}
