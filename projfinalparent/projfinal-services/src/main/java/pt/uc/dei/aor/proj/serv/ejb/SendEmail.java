/*
 */
package pt.uc.dei.aor.proj.serv.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author
 */
@Stateless
public class SendEmail {

	private SimpleEmail email;


	public SendEmail() {
	}

	@PostConstruct
	private void init() {
		email = new SimpleEmail();
		config();
	}

	/**
	 * Email Configurations
	 */
	public void config() {
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(465);
		email.setDebug(true);
		email.setAuthentication("acertarorumoamj@gmail.com", "acertar2015");
		email.setSSL(true);
	}

	/**
	 * Send email from host email, to selected mail, which contains a subject
	 * and a message
	 *
	 * @param from
	 * @param subject
	 * @param msg
	 * @param to
	 */
	@Asynchronous
	public void sendEMail(String from, String subject, String msg, String to) {
		try {
			email.setFrom(from);
			email.setSubject(subject);
			email.setMsg(msg);
			email.addTo(to);
			email.setTLS(true);
			email.send();
			System.out.println("Mail sent!");
		} catch (EmailException e) {
			Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/////////////////////Getters && Setters////////////////////
	public SimpleEmail getEmail() {
		return email;
	}

	public void setEmail(SimpleEmail email) {
		this.email = email;
	}

}
