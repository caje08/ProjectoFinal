/*
 *
 */
package pt.uc.dei.aor.proj.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.facade.AdminFacade;
import pt.uc.dei.aor.proj.serv.facade.UserGuideFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.MaintainSession;
import pt.uc.dei.aor.proj.serv.tools.UserData;

/**
 *
 * @author
 */
@Named
@RequestScoped
public class UserControllerBB {

	@Inject
	private UserData userData;
	@Inject
	private AdminFacade adminGuideFacade;
	@Inject
	private UserGuideFacade userGuideFacade;

	private static final Logger log = Logger.getLogger(UserControllerBB.class.getName());
	private String errorMessage;

	/**
	 *
	 * @return to index if logout is made correctly
	 */
	public String logout() {
		String destination = "/faces/index?faces-redirect=true";

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request
		= (HttpServletRequest) context.getExternalContext().getRequest();

		HttpSession session = request.getSession();
		session.invalidate();
		MaintainSession.clear();

		((UserControllerBB) request).logout();
		//        try {
		//            HttpSession session = request.getSession();
		//            session.invalidate();
		//            MaintainSession.clear();
		//
		//            request.logout();
		//        } catch (ServletException e) {
		//            log.log(Level.SEVERE, "Failed to logout user!", e);
		//            destination = "/loginerror?faces-redirect=true";
		//        }
		return destination; // go to destination
	}

	/**
	 *
	 * @return name of logged in user
	 * @throws UserNotFoundException
	 */
	public String getName() throws UserNotFoundException {
		try {
			return userData.getLoggedUser().getUsername();
		} catch (NoResultException ex) {
			errorMessage = ex.getMessage();
			Logger.getLogger(UserControllerBB.class.getName()).log(Level.SEVERE, null, ex);
			return "/faces/login.xhtml?faces-redirect=true";
		} catch (UserGuideException ex) {
			Logger.getLogger(UserControllerBB.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	/**
	 *
	 * @return  true if is an user that try to log in is an admin
	 */
	public boolean isAdmin() {
		try {
			return userData.getLoggedUser() instanceof AdminEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(UserControllerBB.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;

	}

	/**
	 *
	 * @return  true if is an user that try to log in is a manager
	 */
	public boolean isManager() {
		try {
			return userData.getLoggedUser() instanceof ManagerEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(UserControllerBB.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;
	}

	/**
	 *
	 * @return true if is an user that try to log in is an interviewer
	 */
	public boolean isInterviewer() {
		try {
			return userData.getLoggedUser() instanceof InterviewerEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(UserControllerBB.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;
	}

	/////////////////////Getters && Setters////////////////////

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public AdminFacade getAdminGuideFacade() {
		return adminGuideFacade;
	}

	public void setAdminGuideFacade(AdminFacade adminGuideFacade) {
		this.adminGuideFacade = adminGuideFacade;
	}

	public UserGuideFacade getUserGuideFacade() {
		return userGuideFacade;
	}

	public void setUserGuideFacade(UserGuideFacade userGuideFacade) {
		this.userGuideFacade = userGuideFacade;
	}

	public static Logger getLog() {
		return log;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
