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
    public void run() { // 这里如果不想用while true的方式，可将生产的消息放入消息中间件中，例如kafka，然后这里开启一个监听，消费消息即可。
        String info;
        while (true) {
            try {
                info = QueueUtils.getInstance().poll();
                log.info(AppConst.INFO_LOG_PREFIX + "从队列中获取到的信息：{}", info);
                // do somthing
            } catch (Exception e) {
                log.error(AppConst.ERROR_LOG_PREFIX + "异步记录日志线程异常，异常信息：", e);
            }
        }
    }

}
