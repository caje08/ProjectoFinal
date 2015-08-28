/*
 *
 */
package pt.uc.dei.aor.proj.webplatf.userinfo;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.persistence.NoResultException;

import pt.uc.dei.aor.proj.db.entities.UserGuide;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.facade.UserGuideFacade;

/**
 *
 * @author
 */
@Stateful
@SessionScoped
public class UserData implements Serializable {

	private static final long serialVersionUID = -4578972853700713733L;

	@EJB
	private UserGuideFacade userGuideFacade;

	private UserGuide loggedUser;

	/**
	 *
	 * @return UserGuide, that will be the logged User during the entire time that will be on session
	 * @throws UserNotFoundException
	 * @throws UserGuideException
	 */
	public UserGuide getLoggedUser() throws UserNotFoundException, UserGuideException, NoResultException {
		loggedUser = userGuideFacade.findUserByUsername();
		return loggedUser;
	}

	/////////////////////Getters && Setters////////////////////

	public void setLoggedUser(UserGuide loggedUser) {
		this.loggedUser = loggedUser;
	}

	public UserGuideFacade getUserGuideFacade() {
		return userGuideFacade;
	}

	public void setUserGuideFacade(UserGuideFacade userGuideFacade) {
		this.userGuideFacade = userGuideFacade;
	}

}
