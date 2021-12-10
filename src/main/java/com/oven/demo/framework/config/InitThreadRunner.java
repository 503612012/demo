package com.oven.demo.framework.config;

import com.oven.demo.core.thread.ExecLogThread;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * 初始化队列线程开始工作
 *
 * @author Oven
 */
@Component
public class InitThreadRunner implements CommandLineRunner {

    @Resource(name = "asyncExecLogExecutor")
    private Executor execute;

    @Override
    public void run(String... args) {
        execute.execute(new ExecLogThread());
    }

}
