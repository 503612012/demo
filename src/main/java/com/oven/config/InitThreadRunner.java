package com.oven.config;

import com.oven.thread.ExecLogThread;
import com.oven.util.ThreadPoolUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化队列线程开始工作
 *
 * @author Oven
 */
@Component
public class InitThreadRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        ThreadPoolUtils.getInstance().execute(new ExecLogThread());
    }

}
