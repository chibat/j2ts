package app;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import app.component.Config;

@ComponentScan(basePackageClasses = Config.class)
@EnableAutoConfiguration
@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);

        val url = "http://localhost:8080";
        System.setProperty("java.awt.headless", "false");
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException ignore) {
            log.warn("Can not opened Web browser.", ignore);
        }
    }
}
