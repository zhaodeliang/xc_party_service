package com.daling.party;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//@EnableAspectJAutoProxy
//@EnableCircuitBreaker
//@EnableDiscoveryClient
@EnableTransactionManagement
//@EnableFeignClients(basePackages = {"com.daling.account.coin.core.feign"})
@EnableFeignClients(basePackages = {"com.daling.party.feign"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }
}
