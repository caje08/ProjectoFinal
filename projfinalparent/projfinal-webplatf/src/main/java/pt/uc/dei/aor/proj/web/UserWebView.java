/*
 */
package pt.uc.dei.aor.proj.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIPanel;
import javax.inject.Named;
import javax.persistence.NoResultException;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.db.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.ejb.UserEJBLocal;
import pt.uc.dei.aor.proj.serv.exceptions.EmailDoesNotExistsException;
import pt.uc.dei.aor.proj.serv.facade.UserEntityFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.UserData;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class UserWebView implements Serializable {

	@EJB
	private UserEJBLocal userEJB;
	@EJB
	private UserData loggedUser;
	@EJB
	private UserEntityFacade userEntityFacade;
	private UserEntity userGuide;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String username;
	private List<UserEntity> lstUsers;
	private List<Role> userroles;
	private UserEntity selectedUser;
	private Boolean testrender;
	private UIPanel panelGroup;

	public UserWebView() {
		this.testrender=false;
	}

//	@PostConstruct
//	public void init() {
//		this.userGuide = new UserEntity();
////		this.panelGroup = new UIPanel();
////		this.panelGroup.setRendered(false);
//	}
	
	public UserData getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(UserData loggedUser) {
		this.loggedUser = loggedUser;
	}

	public UserEntityFacade getUserGuideFacade() {
		return userEntityFacade;
	}

	public void setUserGuideFacade(UserEntityFacade userEntityFacade) {
		this.userEntityFacade = userEntityFacade;
	}

	public List<UserEntity> getUsersList() {
		return userEJB.getUsers();
	}
	
	public Collection<Role> getUserRoles() {
		String useremail= selectedUser.getEmail();
		System.out.println("\n Inside UserWebView.getUserRoles()  useremail="+useremail);
		return userEJB.getUserListOfRoles(useremail);
	}

	public String editUser() {
		try {
			if (loggedUser.getLoggedUser() instanceof AdminEntity) {
				userEntityFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, password);
				return "/pages/admin/successeditedUser.xhtml?faces-redirect=true";
			} else if (loggedUser.getLoggedUser() instanceof ManagerEntity) {
				userEntityFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, password);
				return "/pages/manager/successeditedUser.xhtml?faces-redirect=true";
			} else {
				userEntityFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, password);
				return "/pages/interviewer/successeditedUser.xhtml?faces-redirect=true";
			}

		} catch (UserGuideException | UserNotFoundException | InvalidAuthException | EmailAlreadyExistsException ex) {
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException ex) {
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}

	}
	
	public String showUserProfileDetails(UserEntity userSelected){
		this.selectedUser=userSelected;
		System.out.println("\n Selected User="+this.selectedUser.getEmail());
		return "/pages/admin/editUserProfile.xhtml?faces-redirect=true";
	}

	public String editUserProfile() {
		try {
			if (loggedUser.getLoggedUser() instanceof AdminEntity) {
				userEntityFacade.updateUserProfile(selectedUser, firstName, lastName,email, password, userroles);
				return "/pages/admin/successeditedUser.xhtml?faces-redirect=true";
			}else{
				Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE,"As "+loggedUser.getLoggedUser()+"You are not allowed to execute this profile change ");
				JSFUtil.addErrorMessage("As "+loggedUser.getLoggedUser()+"You are not allowed to execute this profile change ");
				return null;
			}

		} catch (UserGuideException | UserNotFoundException | InvalidAuthException | EmailDoesNotExistsException ex) {
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException ex) {
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
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
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 *
	 * @return  true if is an user that try to log in is an admin
	 */
	public boolean isAdmin() {
		try {
			return loggedUser.getLoggedUser() instanceof AdminEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;

	}
	
	public String getUsername() {
		try {
			username = loggedUser.getLoggedUser().getUsername();
		} catch (UserNotFoundException | UserGuideException ex) {
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserEntity getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(UserEntity selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public void showPanel() {
		System.out.println("UserWebView.showPanel() --> panelGroup.getRendererType="+panelGroup.getRendererType());
		this.panelGroup.setRendered(true);
		this.testrender=true;
	}
	
	public void showRender() {
		System.out.println("UserWebView.showRender() --> testrender antes="+testrender);
	
		this.testrender=true;
		System.out.println("UserWebView.showRender() --> testrender depois="+testrender);
	}

	public UIPanel getPanelGroup() {
		System.out.println("UserWebView.getPanelGroup() --> panelGroup.getClientId="+panelGroup);
		return panelGroup;
	}

	public void setPanelGroup(UIPanel panelGroup) {
		this.panelGroup = panelGroup;
	}

	public List<UserEntity> getLstUsers() {
		return userEntityFacade.findAll();
	}

	public void setLstUsers(List<UserEntity> lstUsers) {
		this.lstUsers = lstUsers;
	}

	public boolean isTestrender() {
		return testrender;
	}

	public void setTestrender(boolean testrender) {
		this.testrender = testrender;
	}
	
}
