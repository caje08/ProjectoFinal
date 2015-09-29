/*
 */
package pt.uc.dei.aor.proj.web;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.mail.MessagingException;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.db.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.db.exceptions.UsernameAlreadyExists;
import pt.uc.dei.aor.proj.serv.facade.AdminFacade;
import pt.uc.dei.aor.proj.serv.facade.UserEntityFacade;
import pt.uc.dei.aor.proj.serv.tools.EncryptPassword;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;

/**
 *
 * @author
 */
@Named(value = "adminRequestBean")
@RequestScoped
public class AdminWebInfo implements Serializable {

	private UserEntity user;
	private AdminEntity admin;
	private EncryptPassword ep;
	private String userType;

	@EJB
	private AdminFacade adminEntityFacade;

	@EJB
	private UserEntityFacade userEntityFacade;

	//	@EJB
	//	private GroupsFacade groupsFacade;

	public AdminWebInfo() {
	}

	/**
	 * Initialize encrypt password and UserEntity
	 */
	@PostConstruct
	public void init() {
		this.ep = new EncryptPassword();
		this.user = new UserEntity();
	}

	public void addingAdminUserToDB(){
		//	public UserEntity(String firstName, String lastName, String password, String email, String username)
		//	UserEntity	usertmp6 = new UserEntity("Admin", "SurnameAdmin","jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=", "admin@admin",
		System.out.println("Entrou em AdminWebInfo.addingAdminUserToDB()\n");
		createGroups();

		//this.user = new UserEntity("Admin", "SurnameAdmin","admin", "admin@admin","admin@admin"); //pass admin
		//this.user = new UserEntity();
		this.user.setFirstName("Admin");
		this.user.setLastName("SurnameAdmin");
		this.user.setPassword("admin"); //pass admin
		this.user.setEmail("admin@admin");
		this.user.setUsername("admin@admin");
		this.userType="Admin";
		createNewUser();
		//	System.out.println("Em AdminWebInfo.addingAdminUserToDB() -->Criou user "+user.getEmail());
		//ManagerEntity usertmp4= new ManagerEntity("Carlos", "Santos", "pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=", 1L, "carlos@gmail.com",
		//this.user= new UserEntity("Carlos", "Santos", "123", "carlos@gmail.com", "carlos@gmail.com"); //pass 123
		this.user= new UserEntity();
		this.user.setFirstName("Carlos");
		this.user.setLastName("Santos");
		this.user.setPassword("123"); //pass 123
		this.user.setEmail("carlos@gmail.com");
		this.user.setUsername("carlos@gmail.com");
		this.userType="Manager";
		createNewUser();
		//System.out.println("Em AdminWebInfo.addingAdminUserToDB() --> Criou user "+user.getEmail());

		//InterviewerEntity	usertmp5 = new InterviewerEntity("Catarina", "Lapo", "s6jg4fmrG/46NvIx9nb3i7MKUZ0rIebFMMDu6Ou0pdA=",2L, "ciclapo@gmail.com",
		//this.user = new UserEntity("Catarina", "Lapo", "456", "ciclapo@gmail.com","ciclapo@gmail.com"); //pass 456
		this.user = new UserEntity();
		this.user.setFirstName("Catarina");
		this.user.setLastName("Lapo");
		this.user.setPassword("456"); //pass 456
		this.user.setEmail("ciclapo@gmail.com");
		this.user.setUsername("ciclapo@gmail.com");
		this.userType="Interviewer";
		createNewUser();
		//System.out.println("Em AdminWebInfo.addingAdminUserToDB() --> Criou user "+user.getEmail());
	}

	/**
	 *  Creating Initial Groups
	 *
	 */
	public void createGroups() {

		try {
			//	groupsFacade.createInitialGroups();
		} catch (Exception ex) {
			Logger.getLogger(AdminWebInfo.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage("Error creating initial groups in user");
		}
		JSFUtil.addSuccessMessage("Initial groups were created in AdminWebInfo.createGroups()");
		System.out.println("In AdminWebInfo.createGroups-->Initial groups were created in AdminWebInfo.createGroups()");
	}

	/**
	 *  Create new user
	 * @return to page "succAddUser"
	 */
	public String createNewUser() {

		try {
			//System.out.println("In AdminWebInfo.createNewuser() before creating newuser= "+user.getEmail());
			try {
				userEntityFacade.createNewUser(user, userType);
			} catch (MessagingException e) {
				Logger.getLogger(AdminWebInfo.class.getName()).log(Level.SEVERE, null, e);
				JSFUtil.addErrorMessage(e.getMessage());
				return null;
			}
			Logger.getLogger(AdminWebInfo.class.getName()).log(Level.INFO,"In AdminWebInfo.createNewuser() after creating newuser= "+user.getEmail());
			//System.out.println("Em AdminWebInfo.createNewuser() --> Criou user "+user.getEmail()+" userType= "+userType);
		} catch (UsernameAlreadyExists | EmailAlreadyExistsException ex) {
			Logger.getLogger(AdminWebInfo.class.getName()).log(Level.SEVERE, null, ex.getMessage());
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (InvalidAuthException | EJBException ex) {
			Logger.getLogger(AdminWebInfo.class.getName()).log(Level.SEVERE, null, ex.getMessage());
			JSFUtil.addErrorMessage("Error creating new user");
		}
		return "/pages/admin/searchusers.xhtml?faces-redirect=true";
	}



	/////////////////////Getters && Setters////////////////////
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public AdminEntity getAdmin() {
		return admin;
	}

	public void setAdmin(AdminEntity admin) {
		this.admin = admin;
	}

	public AdminFacade getAdminGuideFacade() {
		return adminEntityFacade;
	}

	public void setAdminGuideFacade(AdminFacade adminEntityFacade) {
		this.adminEntityFacade = adminEntityFacade;
	}

	public EncryptPassword getEp() {
		return ep;
	}

	public void setEp(EncryptPassword ep) {
		this.ep = ep;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
