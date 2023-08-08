package com.like4u.papermanager.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/6 13:28
 */
@Configuration
public class CaptchaConfig {

    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean() throws IOException{
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties =new Properties();
       // FileInputStream is = new FileInputStream("src/main/resources/captcha/captcha.properties");

        InputStream is = getClass().getClassLoader().getResourceAsStream("captcha/captcha.properties");
        properties.load(is);
        Config config=new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath() throws IOException {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties =new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("captcha/captchaMath.properties")) {
            properties.load(inputStream);
            properties.load(inputStream);
            Config config=new Config(properties);
            defaultKaptcha.setConfig(config);

        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
        return defaultKaptcha;

   //     properties.load(Files.newInputStream(Paths.get("src/main/resources/captcha/captchaMath.properties")));

        //Properties properties = new Properties();
        //try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("captcha/captchaMath.properties")) {
        //    properties.load(inputStream);
        //} catch (IOException e) {
        //    // 处理异常
        //}
    }


}
