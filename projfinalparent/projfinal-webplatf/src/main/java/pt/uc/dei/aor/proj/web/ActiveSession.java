package pt.uc.dei.aor.proj.web;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.ejb.UserEJB;

@Named
@SessionScoped
public class ActiveSession implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(ActiveSession.class);

	private HttpSession session;

	private UserEntity activeUser;
	private UserEntity temporaryUser;
	private PositionEntity activePosition;
	private PositionEntity temporaryPosition;
	private ApplicationEntity activeApplication;
	private String texttmp;

	private String search;

	private Part file;

	private String mensagem;

	public ActiveSession() {
	}

	public void init(UserEntity user) {
		logger.info("ActiveSession.init() - before startsession() with user.getEmail="+user.getEmail());
		startSession();

		this.activeUser = user;

		this.mensagem = "";

		UserEJB.increaseUserCount(activeUser);
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public UserEntity getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(UserEntity activeUser) {
		this.activeUser = activeUser;
	}
	
	public UserEntity getTemporaryUser() {
		return temporaryUser;
	}

	public void setTemporaryUser(UserEntity temporaryUser) {
		this.temporaryUser = temporaryUser;
	}	

	public PositionEntity getActivePosition() {
		return activePosition;
	}

	public void setActivePosition(PositionEntity activePosition) {
		this.activePosition = activePosition;
	}

	public PositionEntity getTemporaryPosition() {
		return temporaryPosition;
	}

	public void setTemporaryPosition(PositionEntity temporaryPosition) {
		this.temporaryPosition = temporaryPosition;
	}
	
	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public ApplicationEntity getActiveApplication() {
		return activeApplication;
	}

	public void setActiveApplication(ApplicationEntity activeApplication) {
		this.activeApplication = activeApplication;
	}
	
	public String getTexttmp() {
		return texttmp;
	}

	public void setTexttmp(String texttmp) {
		this.texttmp = texttmp;
	}

	// Logout
	public void logout() {
		UserEJB.decreaseUserCount(activeUser);

		logger.info("ActiveSession.logout() - antes de fazlogout()");
		fazlogout();

		this.activeUser = null;

		logger.info("No ActiveSession.logout() antes do endSession()");

		endSession();
		redirect();
	}

	public String fazlogout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		try {
			request.logout();

			logger.info("No ActiveSession.fazlogout() depois de 'request.logout()'");
			return "/login.xhtml";
		} catch (ServletException e) {

		}
		logger.info("No ActiveSession.fazlogout() antes do return '/login'");
		return "/login.xhtml?faces-redirect=true";
	}

	private void redirect() {
		String redirect = "/projfinal-webplatf/login.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		logger.info("No ActiveSession.redirect() antes do response.sendRedirect '/projfinal-webplatf/login.xhtml'");
		try {
			response.sendRedirect(redirect);
			logger.info("No ActiveSession.redirect() depois do response.sendRedirect '/projfinal-webplatf/login.xhtml'");
		} catch (IOException e) {
			// e.printStackTrace();
			logger.info("No ActiveSession.redirect() deu excepção = "
					+ e.getMessage());
		}
	}

	// Logout

	// In�cio e Fim da sess�o http
	public void startSession() {

		this.session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		this.session.setAttribute("sessionLoggedIn", true);
	}

	public void endSession() {

		if (this.session != null) {
			this.session.invalidate();
		}

		logger.info("Em ActiveSession.endSession() no final da endSession()");
	}
	// In�cio e Fim da sess�o http
}
