package com.hngc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan   //让拦截器生效
@EnableTransactionManagement    //让事务生效
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启权限控制

public class ReggieApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功...");
    }

}
