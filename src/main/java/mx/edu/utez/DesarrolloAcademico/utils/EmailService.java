package mx.edu.utez.DesarrolloAcademico.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.InputStream;
import java.util.Properties;

public class EmailService {

    private String user;
    private String password;

    public EmailService() {
        this.user = System.getenv("MAIL_USER");
        this.password = System.getenv("MAIL_PASSWORD");

        if (this.user == null || this.password == null) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("credentials.properties")) {
                Properties props = new Properties();
                if (in != null) {
                    props.load(in);
                    this.user = props.getProperty("mail.user");
                    this.password = props.getProperty("mail.password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // TIMEOUTS: Evitan que la aplicación se quede congelada respondiendo
        props.put("mail.smtp.connectiontimeout", "5000"); // 5 segundos para conectar
        props.put("mail.smtp.timeout", "5000");           // 5 segundos para recibir respuesta
        props.put("mail.smtp.writetimeout", "5000");       // 5 segundos para enviar datos

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }

    public boolean enviarCodigoRecuperacion(String destEmail, String codigo) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destEmail));
            message.setSubject("Código de Recuperación de Contraseña");

            String htmlContent = "<h2>Recuperación de contraseña</h2>"
                    + "<p>Has solicitado recuperar tu contraseña. Usa el siguiente código de 6 caracteres:</p>"
                    + "<h1 style='color: #00847b;'>" + codigo + "</h1>"
                    + "<p>O ingresa al sistema y escribe tu código.</p>"
                    + "<br><p>Si no fuiste tú, ignora este mensaje.</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Correo de recuperación enviado exitosamente a: " + destEmail);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo con JavaMail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarConfirmacionCambio(String destEmail) {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destEmail));
            message.setSubject("Contraseña actualizada exitosamente");

            String htmlContent = "<h2>¡Contraseña actualizada!</h2>"
                    + "<p>Te confirmamos que tu contraseña ha sido cambiada exitosamente.</p>"
                    + "<p>Ya puedes iniciar sesión con tu nueva contraseña.</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Correo de confirmación enviado exitosamente a: " + destEmail);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar confirmación con JavaMail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}