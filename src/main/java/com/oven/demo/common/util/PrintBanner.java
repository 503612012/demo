package com.oven.demo.common.util;

import lombok.Builder;
import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * 自定义springboot项目启动打印
 *
 * @author Oven
 */
@Builder
public class PrintBanner implements Banner {

    private static final String MSG = "\n\n   ___   ____   ____  ________  ____  _____  \n" +
            " .'   `.|_  _| |_  _||_   __  ||_   \\|_   _| \n" +
            "/  .-.  \\ \\ \\   / /    | |_ \\_|  |   \\ | |   \n" +
            "| |   | |  \\ \\ / /     |  _| _   | |\\ \\| |   \n" +
            "\\  `-'  /   \\ ' /     _| |__/ | _| |_\\   |_  \n" +
            " `.___.'     \\_/     |________||_____|\\____| \n\n";

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
        printStream.println(AnsiOutput.toString(AnsiColor.RED, MSG));
        printStream.println();
    }

}
