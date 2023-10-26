package com.nsw;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author nsw
 * @date 2018/9/16 21:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j  //lombok插件提供的注解，可以直接使用log
public class LoggerTest {

    @Test
    public void test1(){
        String name = "nsw";
        String password = "123456";
        log.debug("debug...");
        log.info("info...");
        log.info("name: {}, password: {}", name, password);
        log.error("error...");
        log.warn("warn...");

    }
}
