/*
 */
package pt.uc.dei.aor.proj.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIPanel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
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
@RequestScoped
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
	private String oldpassword;
	private String newpassword;
	private String username;
	private List<UserEntity> lstUsers;
	private List<Role> userroles;
	private UserEntity selectedUser;
	private Boolean testrender;
	private boolean testpw;
	private UIPanel panelGroup;
	private UIPanel panelListUsers;
	@Inject
	private ActiveSession activeUser;
	@Inject
	private UserCheck userCheck;

	public UserWebView() {
		this.testrender=false;
		this.testpw=false;
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
	
	public void checkUserPw(){
		boolean out=false;
		String emailuser=activeUser.getActiveUser().getEmail();
		try {
			Logger.getLogger(UserWebView.class.getName()).log(Level.INFO, "inside checkUserPw() to find useremail="+emailuser+","+this.oldpassword);
			if(userEntityFacade.findUserByEmailPass(emailuser, this.oldpassword)){
				out=true;
				
			}
		} catch (NoResultException e) {
			 Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, e.getMessage());
			//e.printStackTrace();
		} catch (EJBException e) {
			 Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, e.getMessage());
			//e.printStackTrace();
		} catch (UserNotFoundException e) {
			 Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, e.getMessage());
			//e.printStackTrace();
		}
		this.testpw=out;
		if(!out){
			JSFUtil.addErrorMessage("Your Old password is not matching! Try Again!");			
			  try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, e.getMessage());
				//e.printStackTrace();
			} 			
		}	
		Logger.getLogger(UserWebView.class.getName()).log(Level.INFO, "inside checkUserPw() and out=testpw="+this.testpw);
	}

	public String goBackToMain(){
		this.testpw=false;
		String out="";
		if(userCheck.isAdmin()){
		   out= "/pages/admin/mainadmin.xhtml?faces-redirect=true";
		}else if(userCheck.isManager()){
			out= "/pages/manager/mainmanager.xhtml?faces-redirect=true";
		}if(userCheck.isInterviewer()){
			out= "/pages/interviewer/maininterviewer.xhtml?faces-redirect=true";
		}else {
			Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, "User not identified correctly inside goBackToMain()");
			JSFUtil.addErrorMessage("User not identified correctly in order to redirect to the correct page!!");
		}
		return out;
	}
	
	public void refreshList(){
		panelGroup.setRendered(false);
		Logger.getLogger(UserWebView.class.getName()).log(Level.INFO, "Inside refreshList() and before returning to searchusers.xhtml page!");
		//return "/pages/admin/searchusers.xhtml";
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
		Collection<Role> userroles= new ArrayList<>();
		userroles = userEJB.getUserListOfRoles(useremail);
		return userroles;
	}

	public String editUser() {
		String out = "/pages/index.xhtml?faces-redirect=true";
		Logger.getLogger(UserWebView.class.getName()).log(Level.INFO, "0 - EditUser() --> Updating user profile for email="+email+","+newpassword+","+firstName+","+lastName+","+username);
		try {
			if (loggedUser.getLoggedUser() instanceof AdminEntity) {
				Logger.getLogger(UserWebView.class.getName()).log(Level.INFO, "EditUser() --> Updating user profile for email="+email+","+newpassword+","+firstName+","+lastName+","+username);
				userEntityFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, newpassword, username);
				out = "/pages/admin/mainadmin.xhtml?faces-redirect=true";
			} else if (loggedUser.getLoggedUser() instanceof ManagerEntity) {
				userEntityFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, newpassword, username);
				out = "/pages/manager/mainmanager.xhtml?faces-redirect=true";
			} else if (loggedUser.getLoggedUser() instanceof InterviewerEntity){
				userEntityFacade.editUser(loggedUser.getLoggedUser(), firstName, lastName, email, newpassword, username);
				out = "/pages/interviewer/maininterviewer.xhtml?faces-redirect=true";
			} else{
				JSFUtil.addErrorMessage("Logged_User is not recognized with a valid role = "+loggedUser.getLoggedUser().getEmail()+","+loggedUser.getLoggedUser().getRole());
				Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, "EditUser() --> Logged_User is not recognized with a valid role = "+loggedUser.getLoggedUser().getEmail()+","+loggedUser.getLoggedUser().getRole());
			}
			try{
			    JSFUtil.addSuccessMessage("User profile has been updated successfully!!");			
				   // thread to sleep for 1000 milliseconds
				   Thread.sleep(2000);
				   } catch (Exception e) {
				     Logger.getLogger(UserWebView.class.getName()).log(Level.SEVERE, null, e);
				     //System.out.println(e);
				   }
			
			return out;
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
		this.activeUser.setTemporaryUser(userSelected);
		System.out.println("\n Selected User="+this.selectedUser.getEmail()+"TemporaryUser="+this.activeUser.getTemporaryUser().getEmail());
		return "/pages/admin/editUserProfile.xhtml?faces-redirect=true";
	}

	public String editUserProfile() {
		this.selectedUser=this.activeUser.getTemporaryUser();
		this.firstName=this.selectedUser.getFirstName();
		this.lastName=this.selectedUser.getLastName();
		this.email=this.selectedUser.getEmail();
		this.password=this.selectedUser.getPassword();
		this.userroles=(List<Role>) this.selectedUser.getRoles();
		Logger.getLogger(UserWebView.class.getName()).log(Level.INFO, "Before updating user profile with selectedUser.email="+this.selectedUser.getEmail());
		try {
			if (loggedUser.getLoggedUser() instanceof AdminEntity) {
				userEntityFacade.updateUserProfile(selectedUser, firstName, lastName,email, password, userroles);
				return "/pages/admin/searchusers.xhtml?faces-redirect=true";
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
	
	public void showPanel(UserEntity userentity) {
		this.selectedUser=userentity;
		this.activeUser.setTemporaryUser(userentity);
		System.out.println("UserWebView.showPanel() --> panelGroup.getRendererType="+panelGroup.getRendererType());
		System.out.println("\n UserWebView.showPanel() -->Selected User="+this.selectedUser.getEmail()+"TemporaryUser="+this.activeUser.getTemporaryUser().getEmail());
		this.panelGroup.setRendered(true);
		
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
		List<UserEntity> listroles= new ArrayList<>();
		listroles = userEntityFacade.findAll();
		
		return listroles;
	}
	
	public List<UserEntity> getLstUsersExceptCandidate() {
		List<UserEntity> listroles= new ArrayList<>();
		listroles = userEntityFacade.lstUserEntitiesExceptRole(Role.CANDIDATE);
		
		return listroles;
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

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
	}

	public ActiveSession getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(ActiveSession activeUser) {
		this.activeUser = activeUser;
	}

	public boolean isTestpw() {
		return testpw;
	}

	public void setTestpw(boolean testpw) {
		this.testpw = testpw;
	}

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public UIPanel getPanelListUsers() {
		return panelListUsers;
	}

	public void setPanelListUsers(UIPanel panelListUsers) {
		this.panelListUsers = panelListUsers;
	}
	
}
