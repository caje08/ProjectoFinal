package pt.uc.dei.aor.web.pub;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@Named("pubLoginMB")
@SessionScoped
public class PubLoginMB implements Serializable {

	private static final long serialVersionUID = -6202006843421064331L;
	private static Logger logger = LoggerFactory.getLogger(PubLoginMB.class);
	// The credentials to search for
	private String email;
	private String password;
	private String errorMessage;
	private String activeuser;

	@EJB
	private UserFacade userFacade;

	@EJB
	private PasswordEJB pw;
//	@EJB
//	private EncryptPassword pw;
	@Inject
	private PubUserSession userSession;

	@Inject
	private PubActiveSession session;

	/**
	 * Creates a new instance of PubLoginMB
	 */
	public PubLoginMB() {
	}

	public PasswordEJB getPw() {
		return pw;
	}
//	public EncryptPassword getPw() {
//		return pw;
//	}

	public void setPw(PasswordEJB pw) {
		this.pw = pw;
	}
	
//	public void setPw(EncryptPassword pw) {
//		this.pw = pw;
//	}

	public PubUserSession getuserSession() {
		return userSession;
	}

	public void setuserSession(PubUserSession userSession) {
		this.userSession = userSession;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getActiveuser() {
		return activeuser;
	}

	public void setActiveuser(String activeuser) {
		this.activeuser = activeuser;
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

	public UserFacade getuserFacade() {
		return userFacade;
	}

	public void setuserFacade(UserFacade userFacade) {
		this.userFacade = userFacade;
	}

	public String searchUser() {

		logger.info("Entrou no PubLoginMB.searchUser()");
		String passw = pw.encrypt(password);
		UserEntity tmp = null;
		try {
			tmp = userFacade.findByEmailPass(email, passw);
			if (tmp == null) {
				this.errorMessage = "Email/Password combination not found! Please try again";
				this.password = "";
				this.email = "";
				logger.error(this.errorMessage);
				return "/publogin";
			}
		} catch (Exception pe) {
			// System.out.println(pe.getMessage());
			logger.error(pe.getMessage());
			this.errorMessage = "Email/Password combination not found! Please try again";
			this.password = "";
			this.email = "";
			return "/publogin";
		}

		if (!(tmp.equals(null))) {
			userSession.setLoggedUser(tmp);
			if (userSession.getLoggedUser() != null) {
				logger.info("Logged_user= " + this.email);
				session.init(tmp);
				// retmpect();
				// doLogin(0);
				logger.info("\nLogged_user profile= " + tmp.getRole());
				System.out.println("\nLogged_user profile= " + tmp.getRole());
				if (tmp.getRole().equals(Role.CANDIDATE)) {
					logger.info("\nROLE.CANDIDATE Loading main page = /pages/candidate/indexmainuser.xhtml?faces-redirect=true");
					return "/pages/candidate/indexmainuser.xhtml?faces-redirect=true";
				} else if (tmp.getRole().equals(Role.USERPUBLIC)) {
					logger.info("\nRole.USERPUBLIC Loading main page = /pages/candidate/indexmainuser.xhtml?faces-redirect=true");
					return "/pages/candidate/indexmainuser.xhtml?faces-redirect=true";
				} else {
					logger.info("\nPUB USER UNKNOWN Loading login page = /publogin.xhtml?faces-redirect=true \n");
					return "/publogin.xhtml?faces-redirect=true";
				}
			} else {
				this.errorMessage = "Email/Password combination not found! Please try again";
				logger.error(this.errorMessage);
				return "/publogin";
			}
		} else {
			this.errorMessage = "Email/Password combination not found! Please try again";
			logger.error(this.errorMessage);
			return "/publogin";
		}

	}

	public String refreshUser() {
		userSession.setLoggedUser(userFacade.findByEmailPass(userSession
				.getLoggedUser().getEmail(), userSession.getLoggedUser()
				.getPassword()));
		if (userSession.getLoggedUser() != null) {

			return "index";
		} else {
			this.errorMessage = "Email/Password combination not found! Please try again";
			return "login";
		}
	}

	public void logged() throws IOException {
		if (userSession.getLoggedUser() != null) {
			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/pages/candidate/indexmainuser.xhtml");
		}
	}

	public void notLogged() throws IOException {
		if (userSession.getLoggedUser() == null) {
			ExternalContext ec = FacesContext.getCurrentInstance()
					.getExternalContext();
			ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
		}
	}

	public String oldlogout() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSession(false);

		return "login.xhtml?faces-redirect=true";
	}

	public UserEntity getLoggedUser() {
		return userSession.getLoggedUser();
	}

	public void doLogin() {
		searchUser();

	}

	private void redirect() {

		String redirect = "index.xhtml";

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		try {
			response.sendRedirect(redirect);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String login() {
		String webout = "";
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		try {
			logger.info("Email = " + email + ", has requested to be logged");
//			String testpw=pw.encrypt(password);
//			request.login(email, testpw);
			request.login(email, password);
			// request.getSession().setAttribute(activeuser,
			// userSession.getLoggedUser().getName());
			webout = searchUser();
		} catch (ServletException e) {

			logger.error("Wrong Email = " + email +","+password+ " and passwd combination");
			this.errorMessage = "Email/Password combination not found! Please try again";
			return "/publogin";
		}
		logger.info("\nWithin PubLoginMB.login(), it's going to redirect to webpage --> \n"
				+ webout);
		return webout;
		// return "/projfinal-webplatf/login.xhtml";
	}

	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		try {
			request.getSession().invalidate();
			request.logout();
			return "/publogin.xhtml?faces-redirect=true";
		} catch (ServletException e) {
			context.addMessage(null, new FacesMessage("logout has failed!"));
		}
		return null;
	}

}
