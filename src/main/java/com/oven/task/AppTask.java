package com.oven.task;

import com.oven.dao.CrontabDao;
import com.oven.util.QueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Random;

/**
 * 定时任务
 *
 * @author Oven
 */
@Slf4j
@Component
public class AppTask implements SchedulingConfigurer {

    @Resource
    private CrontabDao crontabDao;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(this::doSomething, triggerContext -> {
            String cron = crontabDao.getCron("cronKey");
            if (StringUtils.isEmpty(cron)) {
                System.out.println("cron is null...");
            }
            return new CronTrigger(cron).nextExecutionTime(triggerContext);
        });
    }

    private void doSomething() {
        String info = "" + new Random().nextInt(10);
        QueueUtils.getInstance().offer(info);
        System.out.println("do something..." + new DateTime().getSecondOfMinute());
    }

}
