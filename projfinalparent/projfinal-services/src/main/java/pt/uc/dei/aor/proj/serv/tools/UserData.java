/*
 *
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.persistence.NoResultException;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.ejb.ApplicationWebManagem;
import pt.uc.dei.aor.proj.serv.facade.UserEntityFacade;

/**
 *
 * @author
 */
@Stateful
@SessionScoped
public class UserData implements Serializable {

	private static final long serialVersionUID = -4578972853700713733L;

	@EJB
	private UserEntityFacade userEntityFacade;

	private UserEntity loggedUser;

	/**
	 *
	 * @return UserEntity, that will be the logged User during the entire time that will be on session
	 * @throws UserNotFoundException
	 * @throws UserGuideException
	 */
	public UserEntity getLoggedUser() throws UserNotFoundException, UserGuideException, NoResultException {
		//loggedUser = userGuideFacade.findUserByUsername();
		loggedUser = userEntityFacade.findUserByEmail();
		Logger.getLogger(UserData.class.getName()).log(Level.INFO, "\nLogged User = "+loggedUser.getEmail());
		return loggedUser;
	}
	
	/**
	 *
	 * @return  true if is an user that try to log in is an admin
	 */
	public boolean isAdmin() {
		try {
			return getLoggedUser() instanceof AdminEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex.getMessage());
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
			return getLoggedUser() instanceof ManagerEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex.getMessage());
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;
	}
	
	/**
	 *
	 * @return  true if is an user that try to log in is an interviewer
	 */
	public boolean isInterviewer() {
		try {
			return getLoggedUser() instanceof InterviewerEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex.getMessage());
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;
	}
	
	/**
	 *
	 * @return  true if is an user that try to log in is an Candidate
	 */
	public boolean isCandidate() {
		try {
			return getLoggedUser() instanceof ApplicantEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex.getMessage());
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;
	}

	/////////////////////Getters && Setters////////////////////

	public void setLoggedUser(UserEntity loggedUser) {
		this.loggedUser = loggedUser;
	}

	public UserEntityFacade getuserEntityFacade() {
		return userEntityFacade;
	}

	public void setuserEntityFacade(UserEntityFacade userEntityFacade) {
		this.userEntityFacade = userEntityFacade;
	}

}
