/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.aor.web.pub;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.ejb.PasswordEJB;
import pt.uc.dei.aor.proj.serv.facade.UserFacade;

/**
 *
 * @author
 */
@Named("pubRegistryMB")
@RequestScoped
public class PubRegistryMB {

	static Logger logger = LoggerFactory.getLogger(PubRegistryMB.class);
	@Inject
	private PubLoginMB loginMB;
	// New User data
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	private String confirmPassword;
	private String pwEncrypted;
	private String datanascimento;
	@EJB
	private PasswordEJB pw;
	private String errorMessage;
	// New User
	private UserEntity newUser;
	@EJB
	private UserFacade userFacade;

	/**
	 * Creates a new instance of PubRegistryMB
	 */
	public PubRegistryMB() {
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPwEncrypted() {
		return pwEncrypted;
	}

	public void setPwEncrypted(String pwEncrypted) {
		this.pwEncrypted = pwEncrypted;
	}

	public String getDatanascimento() {
		return datanascimento;
	}

	public void setDatanascimento(String datanascimento) {
		this.datanascimento = datanascimento;
	}

	public PasswordEJB getPw() {
		return pw;
	}

	public void setPw(PasswordEJB pw) {
		this.pw = pw;
	}

	public PubLoginMB getLoginMB() {
		return loginMB;
	}

	public void setLoginMB(PubLoginMB loginMB) {
		this.loginMB = loginMB;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public UserFacade getUserFacade() {
		return userFacade;
	}

	public void setUserFacade(UserFacade userFacade) {
		this.userFacade = userFacade;
	}

	public String submit() {
		if (!this.email.equals("") && !this.firstname.equals("")
				&& !this.password.equals("")) {
			if (this.password.equals(this.confirmPassword)) {
				this.newUser = new UserEntity();
				this.newUser.setEmail(email);
				this.newUser.setFirstName(firstname);
				this.newUser.setRole(Role.USERPUBLIC);
				this.newUser.setPassword(pw.encrypt(password));
				this.newUser.setLastName(lastname);
				try {
					this.userFacade.create(newUser);
					loginMB.setEmail(email);
					loginMB.setPassword(password);
					logger.info("Em PubRegistryMB(), Utilizador com email="+email+"  criado com sucesso na BD!!");
					//loginMB.doLogin();
					loginMB.login();
				} catch (Exception e) {
					errorMessage = "Could not create user due to: "
							+ e.getCause().getMessage();
					logger.error(this.errorMessage);
					return "registry";

				}
				System.out.println("Vai para a page do login");
				return "/pages/index"; //não corre este comando pois o "searchUser()" faz login (criando a sessão) e retorna à pág. "index"
			} else {
				this.errorMessage = "Passwords don't match!";
				logger.error(this.errorMessage);
				return "registry";
			}
		} else {
			this.errorMessage = "Registry user fields must not be empty before form submission!";
			logger.error(this.errorMessage);
			return "registry";
		}
	}

	public String cancel() {
		return "login";
	}

	public UserEntity getNewUser() {
		return newUser;
	}

	public void setNewUser(UserEntity newUser) {
		this.newUser = newUser;
	}
}
