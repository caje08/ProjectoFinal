/*
 */
package pt.uc.dei.aor.proj.serv.ejb;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.db.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.facade.UserEntityFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.UserData;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class UserViewBB implements Serializable {

	@EJB
	private UserData loggedUser;
	@EJB
	private UserGuideFacade userGuideFacade;
	private UserEntity userGuide;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String username;

	public UserViewBB() {
	}

	public UserData getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(UserData loggedUser) {
		this.loggedUser = loggedUser;
	}

	public UserGuideFacade getUserGuideFacade() {
		return userGuideFacade;
	}

	public void setUserGuideFacade(UserGuideFacade userGuideFacade) {
		this.userGuideFacade = userGuideFacade;
	}



	public String editUser() {
		try {
			if (loggedUser.getLoggedUser() instanceof AdminEntity) {
				userGuideFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, password);
				return "/faces/admin/successeditedUser.xhtml?faces-redirect=true";
			} else if (loggedUser.getLoggedUser() instanceof ManagerEntity) {
				userGuideFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, password);
				return "/faces/manager/successeditedUser.xhtml?faces-redirect=true";
			} else {
				userGuideFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, password);
				return "/faces/interviewer/successeditedUser.xhtml?faces-redirect=true";
			}

		} catch (UserGuideException | UserNotFoundException | InvalidAuthException | EmailAlreadyExistsException ex) {
			Logger.getLogger(UserViewBB.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException ex) {
			Logger.getLogger(UserViewBB.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}

	}

	public UserEntity getUserGuide() {
		return userGuide;
	}

	public void setUserGuide(UserEntity userGuide) {
		this.userGuide = userGuide;
	}

	public String getFirstName() {
		try {
			return loggedUser.getLoggedUser().getFirstName();
		} catch (UserNotFoundException | UserGuideException ex) {
			Logger.getLogger(UserViewBB.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		try {
			lastName = loggedUser.getLoggedUser().getLastName();
		} catch (UserNotFoundException | UserGuideException ex) {
			Logger.getLogger(UserViewBB.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		try {
			email = loggedUser.getLoggedUser().getEmail();
		} catch (UserNotFoundException | UserGuideException ex) {
			Logger.getLogger(UserViewBB.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		try {
			password = loggedUser.getLoggedUser().getPassword();
		} catch (UserNotFoundException | UserGuideException ex) {
			Logger.getLogger(UserViewBB.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		try {
			username = loggedUser.getLoggedUser().getUsername();
		} catch (UserNotFoundException | UserGuideException ex) {
			Logger.getLogger(UserViewBB.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
