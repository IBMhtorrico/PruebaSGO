package sgo.utilidades;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * @author Administrador
 *
 */
public class ObjectMail {

	private String nameHost 	= "mail.smtp.host";
	private String mailPort 	= "mail.smtp.port";
	private String mailAuth 	= "mail.smtp.auth";
	private String mailStarttls = "mail.smtp.starttls.enable";
	private String charSet 		= "ISO-8859-1";
	private String nuevaPropiedad		= "mail.smtp.starttls.enable";

	private String subject;				  	//asunto
	private String to;					  	//solo 1 receptor 	TO
	private String from;				  	//emisor			FROM
	private ArrayList<String> SeveralTo;  	//varios Para     	TO
	private ArrayList<String> copyes;	  	//con copia       	CC
	private ArrayList<String> hiddenCopyes; //copia oculta  	OCC
	private String texto;
	private Boolean html;
	private ArrayList<String> files;

	public ObjectMail() {
		this.copyes = null;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public ArrayList<String> getSeveralTo() {
		return SeveralTo;
	}

	public void setSeveralTo(ArrayList<String> severalTo) {
		SeveralTo = severalTo;
	}

	public ArrayList<String> getCopyes() {
		return copyes;
	}

	public void setCopyes(ArrayList<String> copyes) {
		this.copyes = copyes;
	}

	public ArrayList<String> getHiddenCopyes() {
		return hiddenCopyes;
	}

	public void setHiddenCopyes(ArrayList<String> hiddenCopyes) {
		this.hiddenCopyes = hiddenCopyes;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public ArrayList<String> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<String> files) {
		this.files = files;
	}

	public Boolean getHtml() {
		return html;
	}

	public void setHtml(Boolean html) {
		this.html = html;
	}

	public boolean enviarMail() {   
		boolean retorno = false;
	     // Get system properties
	    Properties properties = System.getProperties();

	    // Setup mail server
	    properties.setProperty(nameHost, ConstanteMail.MAIL_SERVIDOR);
	    properties.setProperty(mailPort, ConstanteMail.MAIL_PUERTO);
	    properties.setProperty(mailAuth, "true");
	    //properties.setProperty(mailStarttls, "true");
	    //properties.setProperty(mailStarttls, "false");
	    //properties.setProperty(mailStarttls, "enable");
	    properties.setProperty(nuevaPropiedad, "true");
	    
	    // Get the default Session object.
	    Session session = Session.getDefaultInstance(properties, null);
        try{
        	// Se compone la parte del texto
            MimeBodyPart texto = new MimeBodyPart();
            if(this.getHtml()){
            	texto.setText(this.getTexto(), this.charSet, "html");
            }else{
            	texto.setText(this.getTexto());
            }

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            if (this.getFiles() != null) {
	            for(String file : this.getFiles()){
	                BodyPart adjunto = new MimeBodyPart();
	                adjunto.setDataHandler(new DataHandler(new FileDataSource(file)));
	                File name = new File(file);
	                adjunto.setFileName(name.getName());
	            	multiParte.addBodyPart(adjunto);
	            }
            }

	        // Create a default MimeMessage object.
	        MimeMessage message = new MimeMessage(session);
	        //message.setFrom(new InternetAddress("chramosvega@gmail.com"));
	        //message.setFrom(new InternetAddress("navarrobenites@gmail.com"));
	        message.setFrom(new InternetAddress(this.getFrom()));

	        //para asignar los correos Para
	        for(String v: this.getSeveralTo()){
            	if(isValidEmailAddress(v)){
	                message.addRecipient(Message.RecipientType.TO, new InternetAddress(v));
            	}
            }
	        
	        // Esto es cuando s�lo hay un emisor
	        //message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.getTo()));
	        
	        //Para asignar los correos CC
	        if ( this.getCopyes() != null && this.getCopyes().size() > 0 ) {
	            for ( String cc : this.getCopyes() ) {
	            	message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
	            }
	        }
	        
	        //Para asignar los correos ocultos OCC
	        if ( this.getHiddenCopyes() != null && this.getHiddenCopyes().size() > 0 ) {
	            for ( String bcc : this. getHiddenCopyes() ) {
	            	message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
	            }
	        }

	        // Set Subject: header field
	        message.setSubject(this.getSubject());
	        message.setContent(multiParte);

	        // Se envia el correo.
	        Transport t = session.getTransport("smtp");
	        t.connect(ConstanteMail.MAIL_USUARIO, ConstanteMail.MAIL_PASSWORD);
	        t.sendMessage(message, message.getAllRecipients());
	        t.close();
	        retorno = true;
        } catch (MessagingException mex) {
	         mex.printStackTrace();
	         retorno = false;
	    }
        return retorno;
	}
	
	/*IAC-29012013*/
	public boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;		      
	   }
	   System.out.println("InternetAddress.Validate ===> Validacion email "+email+" ==> "+result);
	   return result;
	}
	/*IAC*/
} 
