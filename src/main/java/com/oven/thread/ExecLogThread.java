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
        String info = QueueUtils.getInstance().poll();
        while (StringUtils.isNotEmpty(info)) {
            try {
                log.info(AppConst.INFO_LOG_PREFIX + "从队列中获取到的信息：{}", info);
                // do somthing
                info = QueueUtils.getInstance().poll();
            } catch (Exception e) {
                log.error(AppConst.ERROR_LOG_PREFIX + "异步记录日志线程异常，异常信息：", e);
            }
        }
    }

}
