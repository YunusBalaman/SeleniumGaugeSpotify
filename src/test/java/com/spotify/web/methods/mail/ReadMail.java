package com.spotify.web.methods.mail;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Store;

import java.util.Properties;

public class ReadMail {

    public Session getSession(String mail, String password) {

        String type = "imap";
        String storeType = "imaps";
        String port = "993";
        String storeConnect = "imap";
        Properties props = new Properties();
        props.put("mail.user", mail);
        props.put("mail.password", password);
        // props.put("mail.debug", "true");
        props.put("mail." + type + ".starttls.enable", "true");
        props.put("mail." + type + ".host", storeConnect + ".gmail.com");
        props.put("mail." + type + ".port", port);
        props.put("mail." + type + ".auth", "true");
        props.setProperty("mail.store.protocol", storeType);
        //properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mail, password);
            }
        });
        return session;
    }

     public Store getStore(Session session){

        Store store = null;
         String storeType = "imaps";
         String storeConnect = "imap";
        try {
            store = session.getStore(storeType);
            store.connect(storeConnect + ".gmail.com", null, null);
            session.setDebug(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return store;
    }

}
