package com.richards.mealsapp.mail;

import com.richards.mealsapp.MealsAppApplication;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Properties;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor

public class JavaMailServiceImpl implements JavaMailService {
    @Value("${spring.company.email}")
    private String companyEmail;

    private final JavaMailSender javaMailSender;
    private static final Logger LOGGER = LoggerFactory.getLogger(MealsAppApplication.class);
    private static final Marker IMPORTANT = MarkerFactory.getMarker("IMPORTANT");

    @Override
    public ResponseEntity<BaseResponse<String>> sendMailAlt(String receiverEmail, String subject, String text) {

        if (!isValidEmail(receiverEmail))
            new ResponseEntity<>("Email is not valid", HttpStatus.BAD_REQUEST);

        isEmailDomainValid(receiverEmail);

        MimeMessagePreparator messagePreparatory = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(companyEmail);
            message.setTo(receiverEmail);
            message.setSubject(subject);
            message.setText(text, true);
//            message.addInline("logo", new ClassPathResource("img/logo.gif"));
//            message.addAttachment("myDocument.pdf", new ClassPathResource("uploads/document.pdf"));
        };

        try {
            LOGGER.info("Beginning of log *********");
            LOGGER.info(IMPORTANT, "Sending mail to: " + receiverEmail);
            javaMailSender.send(messagePreparatory);
            return new ResponseEntity(new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Sent"), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(IMPORTANT, e.getMessage());
        }

        return new ResponseEntity<>(new BaseResponse<>(ResponseCodeEnum.ERROR, "Error sending mail"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public boolean isValidEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }
    public void isEmailDomainValid(String email) {
        try {
            String domain = email.substring(email.lastIndexOf('@') + 1);
            int result = doLookup(domain);
            LOGGER.info("Domain: " + domain);
            LOGGER.info("Result of domain: " + result);
        } catch (NamingException e) {
            LOGGER.error(e.getMessage());
        }
    }
    public int doLookup(String hostName) throws NamingException {
        Hashtable<String, String> env = new Hashtable<>();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = new InitialDirContext( env );
        Attributes attrs = ictx.getAttributes( hostName, new String[] { "MX" });
        Attribute attr = attrs.get( "MX" );
        if( attr == null ) return( 0 );
        return( attr.size() );
    }

}
