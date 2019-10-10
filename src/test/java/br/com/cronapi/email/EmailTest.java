package br.com.cronapi.email;


import cronapi.Var;
import cronapi.email.Operations;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    private Var from;
    private Var to;
    private Var cc;
    private Var bcc;
    private Var subject;
    private Var msg;
    private Var html;
    private Var attachments;
    private Var smtpHost;
    private Var smtpPort;
    private Var login;
    private Var password;
    private Var ssl;

    @BeforeEach
    public void setUp() {
         this.from = Var.VAR_NULL;
         this.to = Var.VAR_NULL;
         this.cc = Var.VAR_NULL;
         this.bcc = Var.VAR_NULL;
         this.subject = Var.VAR_NULL;
         this.msg = Var.VAR_NULL;
         this.html = Var.VAR_NULL;
         this.attachments = Var.VAR_NULL;
         this.smtpHost = Var.VAR_NULL;
         this.smtpPort = Var.VAR_NULL;
         this.login = Var.VAR_NULL;
         this.password = Var.VAR_NULL;
         this.ssl = Var.VAR_NULL;
    }

    @AfterEach
    public void tearDown() {
    }

    private void send() {
        Operations.sendEmail(from, to, cc, bcc, subject, msg, html, attachments, smtpHost, smtpPort, login, password, ssl);
    }

    @Test
    public void sendEmailNull(){
        try {
            send();
            fail();
        } catch (Exception e){
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void sendEmailSSL(){
        try {
            this.ssl =  Var.valueOf("ssl");
            send();
            fail();
        } catch (Exception e){
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void sendEmailTLS(){
        try {
            this.ssl =  Var.valueOf("tls");
            send();
            fail();
        } catch (Exception e){
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void sendEmailPLAIN(){
        try {
            this.ssl =  Var.valueOf("plain");
            send();
            fail();
        } catch (Exception e){
            assertTrue(e instanceof RuntimeException);
        }
    }

    @Test
    public void sendEmailFrom(){
        try {
            this.from =  Var.valueOf("wesley.rover@cronapp.io");
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Invalid message supplied");
        }
    }

    @Test
    public void sendEmailMsg(){
        try {
            this.from =  Var.valueOf("wesley.rover@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailCcString(){
        try {
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.cc = Var.valueOf("email@cronapp.io");
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailCcList(){
        try {
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.cc = Var.valueOf(Arrays.asList(Var.valueOf("email@cronapp.io"), Var.valueOf("email@cronapp.io")));
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailBccString(){
        try {
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.bcc = Var.valueOf("email@cronapp.io");
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailBccList(){
        try {
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.bcc = Var.valueOf(Arrays.asList(Var.valueOf("email@cronapp.io"), Var.valueOf("email@cronapp.io")));
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailToString(){
        try {
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.to = Var.valueOf("email@cronapp.io");
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailToList(){
        try {
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.to = Var.valueOf(Arrays.asList(Var.valueOf("email@cronapp.io"), Var.valueOf("email@cronapp.io")));
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailAttachmentsString(){
        try {
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.to = Var.valueOf("email@cronapp.io");
            this.attachments = Var.valueOf("E-mail - new case test.");
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot attach file \"E-mail - new case test.\"");
        }
    }

    @Test
    public void sendEmailAttachmentsList(){
        try {
            InputStream fileInputStream = new FileInputStream(getClass().getResource("/books.json").getPath());
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.attachments = Var.valueOf(Arrays.asList(Var.valueOf(new File(getClass().getResource("/books.json").getPath()))));
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

    @Test
    public void sendEmailHtmlMsg(){
        try {
            InputStream fileInputStream = new FileInputStream(getClass().getResource("/books.json").getPath());
            this.from = Var.valueOf("email@cronapp.io");
            this.msg = Var.valueOf("E-mail - new case test.");
            this.html = Var.valueOf("E-mail - new case test.");
            send();
            fail();
        } catch (Exception e){
            assertEquals(e.getCause().getMessage(), "Cannot find valid hostname for mail session");
        }
    }

}