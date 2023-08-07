package com.like4u.papermanager.Service;

import com.like4u.papermanager.common.RedisCache;
import com.like4u.papermanager.common.utlis.StringUtils;
import com.like4u.papermanager.config.LoginConfig;
import com.like4u.papermanager.domain.LoginUser;
import com.like4u.papermanager.exception.SendMailFailException;
import com.like4u.papermanager.pojo.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private RedisCache redisCache;


    /**
     *
     * @param to 收件人
     * @param subject 标题
     * @param content 正文
     * @param file 附件
     */
    public void sendAttachFileMail( String to,
                                   String subject, String content, File file) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            if (file!=null){
                helper.addAttachment(file.getName(), file);
            }
            // 最后通过 addAttachment 方法添加附件

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMailFailException(e.getMessage());
        }
    }


    public void sendHtmlMail( String to,
                                    String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new SendMailFailException(e.getMessage());
        }
    }
    @Async
    public void sendRegisterMail(User user){
        Context context = new Context();

        log.info("发送邮件：使用线程为{}",Thread.currentThread().getName());
        String verityCode = StringUtils.generateVerificationCode(6);
        redisCache.setCacheObjectAndTTL(LoginConfig.MAIL_CODE_KEY+user.getEmail(),verityCode,5, TimeUnit.MINUTES);

        context.setVariable("user",user);
        context.setVariable("verityCode",verityCode);

        String mail = templateEngine.process("registerMail.html", context);

        sendHtmlMail(user.getEmail(),"欢迎",mail);

    }


    /**
     * 用户登录状态做出重要更改的时候需要给他发邮件进行确认
     * */

    public void sendVerityMail(String message){
        Context context = new Context();

        //从上下文中获取用户
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = (LoginUser) authentication.getPrincipal();

        String verityCode = StringUtils.generateVerificationCode(6);
        redisCache.setCacheObjectAndTTL(LoginConfig.MAIL_CODE_KEY+user.getUid(),verityCode,1, TimeUnit.MINUTES);

    /*  context.setVariable("user",user.getUsername());
        context.setVariable("verityCode",verityCode);
        context.setVariable("message",message);*/

        //TODO：未来创建一个新的模板，进行邮件发送

        String mail = templateEngine.process("registerMail.html", context);

        sendAttachFileMail(user.getUser().getEmail(),"请确认您的操作",mail,null);

    }
}

