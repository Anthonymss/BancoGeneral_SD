package com.gateway.config;
import com.gateway.GatewayApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRestart {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationArguments applicationArguments;

    public void restartApp() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SpringApplication.exit(applicationContext, () -> 0);
            SpringApplication.run(GatewayApplication.class, applicationArguments.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
}
