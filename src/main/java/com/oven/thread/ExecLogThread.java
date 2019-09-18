package com.oven.thread;

import com.oven.constant.AppConst;
import com.oven.util.QueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 异步记录日志线程
 *
 * @author Oven
 */
@Slf4j
public class ExecLogThread implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                String info = QueueUtils.getInstance().poll();
                if (StringUtils.isEmpty(info)) {
                    Thread.sleep(1000);
                    continue;
                }
                log.info(AppConst.INFO_LOG_PREFIX + "从队列中获取到的信息：{}", info);
            } catch (Exception e) {
                log.error(AppConst.ERROR_LOG_PREFIX + "异步记录日志线程异常，异常信息：", e);
            }
        }
    }

}
