package uz.pdp.cinema_room_rest_service.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailSenderService {

    @Autowired
    private Configuration config;

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    public void sendToEmailText(String subject, String body, String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(to);
            message.setText(body);
            message.setSubject(subject);
            mailSender.send(message);
            System.out.println("mail successfully send");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendToEmailTemplate(String subject, String to, Map<String, Object> model) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            Template t = config.getTemplate("index.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(to);
            helper.setText(html, true);
            helper.setSubject(subject);
            helper.setFrom(fromMail);
            System.out.println("send seccess");
            mailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }

    }

    public void sendToEmailAttachment(String subject, String to, String fileName) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            helper.addAttachment(fileName, new File(fileName));
            helper.setTo("axranaeva@gmial.com");
            helper.setSubject(subject);
            helper.setFrom(fromMail);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * SEND TO EMAIL SESSION BEGINNING TIME
     *
     * @param subject
     * @param to
     * @param dateTime
     */
    public void sendMailTimer(String subject, String to, LocalDateTime dateTime, byte[] bytesMovieImage) {
        String body = "<html> <body>\n";
        if (bytesMovieImage != null) {
            byte[] encode = Base64.getEncoder().encode(bytesMovieImage);
            String image = new String(encode, StandardCharsets.UTF_8);
            body += "<img src='data:image/jpeg; base64, " + image + "/>";
        }
        body += "\n" +
                "<h3>Film Boshlanishiga qolgan vaqt quyida ko'rsatilgan</h3>" +
                "\n";
        body += String.format(
                "\n\n<img src=\"https://timer.plus/%s,%s,%s,%s,%s,%s.gif\"/>",
                dateTime.getYear(),
                dateTime.getMonth().getValue(),
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute(),
                dateTime.getSecond());
        body += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "    <tbody>\n" +
                "    <tr>\n" +
                "        <td align=\"center\">\n" +
                "            <img src=\"http://i.countdownmail.com/1oofol.gif?end_date_time="+String.format("%s-%s-%sT%s:%s",dateTime.getYear(),dateTime.getMonth().getValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute())+":56+00:00\"\n" +
                "                 style=\"display:inline-block!important;width:90%!important;max-width:544px!important;\" border=\"0\"\n" +
                "                 alt=\"countdownmail.com\"/></td>\n" +
                "    </tr>\n" +
                "    </tbody>\n" +
                "</table>";
        body += "</body></html>";
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setText(body, true);
            helper.setSubject(subject);
            helper.setFrom(fromMail);
            System.out.println("send successfully");
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public void sendmail(String to, String subject, String body, String fileToAttach) throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromMail, "mesajpmlqqcfaqox");
            }
        });
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromMail, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        msg.setSubject(subject);
        msg.setContent("Tutorials point email", "text/html");
        msg.setSentDate(new Date(System.currentTimeMillis()));

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(body, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        MimeBodyPart attachPart = new MimeBodyPart();

        attachPart.attachFile(fileToAttach);
        multipart.addBodyPart(attachPart);
        msg.setContent(multipart);
        mailSender.send(msg);
    }


    public void sendEmailAttachment(String subject, String message,
                                    String toEmailAddresses, boolean isHtmlMail, String attachment) {
        try {
            System.out.println(toEmailAddresses);
            System.out.println(subject);
            System.out.println(message);
            System.out.println(isHtmlMail);
            System.out.println(attachment);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(fromMail);
            helper.setTo(toEmailAddresses);
            helper.setSubject(subject);

            if (isHtmlMail) {
                helper.setText("<html><body>" + message + "</html></body>", true);
            } else {
                helper.setText(message);
            }

            // attach the file into email body
            FileSystemResource file = new FileSystemResource(new File(attachment));
            helper.addAttachment("Ticket.pdf", file);

            mailSender.send(mimeMessage);

            System.out.println("Email sending complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
