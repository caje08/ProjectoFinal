/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.List;
<<<<<<< HEAD
import java.util.logging.Level;
import java.util.logging.Logger;
=======
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.db.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.db.exceptions.UsernameAlreadyExists;
import pt.uc.dei.aor.proj.ejb.PasswordEJB;
import pt.uc.dei.aor.proj.serv.ejb.SendEmail;
import pt.uc.dei.aor.proj.serv.exceptions.EmailDoesNotExistsException;

/**
 *
 * @author
 */
@Stateless
public class UserEntityFacade extends AbstractFacade<UserEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@EJB
	private AdminFacade adminEntityFacade;
	@EJB
	private ManagerFacade managerFacade;
	@EJB
	private InterviewerFacade interviewerFacade;
//	@EJB
//	private GmailSmtpSSL mail;
	@EJB
	private SendEmail mail;
	@EJB
	private PasswordEJB pw;
	@Resource
	SessionContext ctx;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public UserEntityFacade() {
		super(UserEntity.class);
	}

	/**
	 *
	 * @return UserEntity if is an instance of ManagerEntity, AdminEntity and
	 * InterviewerEntity, and if it is not null.
	 * @throws UserNotFoundException
	 * @throws UserGuideException (Not a instanceof "childs" of UserEntity)
	 * @throws EJBException
	 */
	public UserEntity findUserByUsername() throws UserNotFoundException, UserGuideException, EJBException, NoResultException  {
		UserEntity userEntity = (UserEntity) em.createNamedQuery("UserEntity.findByName").setParameter("username", ctx.getCallerPrincipal().getName()).getSingleResult();
		if (userEntity != null) {
			if (userEntity instanceof AdminEntity || userEntity instanceof InterviewerEntity || userEntity instanceof ManagerEntity) {
				return userEntity;
			} else {
				throw new UserGuideException();
			}
		} else {
			throw new NoResultException();
		}
	}

<<<<<<< HEAD
	/**
	 *
	 * @return UserEntity if is an instance of ManagerEntity, AdminEntity and
	 * InterviewerEntity, and if it is not null.
	 * @throws UserNotFoundException
	 * @throws UserGuideException (Not a instanceof "childs" of UserEntity)
	 * @throws EJBException
	 */
	public boolean findUserByEmailPass(String email, String pass) throws UserNotFoundException, EJBException, NoResultException  {
		try{
		   String pwencr=pw.encrypt(pass);
		   Query query = em.createNamedQuery("UserEntity.findByEmailPass", UserEntity.class);
		   query.setParameter("email",email);
		   query.setParameter("password",pwencr);
		   return !query.getResultList()
				.isEmpty();
		} catch (Exception e) {
			Logger.getLogger(UserEntityFacade.class.getName()).log(Level.SEVERE,null,e);
			throw new UserNotFoundException();				 
		}
		
	}
=======
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6

	/**
	 *
	 * @param username
	 * @return ApplicantEntity find by inputed username
	 * @throws UserNotFoundException
	 */
	public ApplicantEntity findApplicantByUsernameInput(String username) throws UserNotFoundException {
		UserEntity userEntity = (UserEntity) em.createNamedQuery("UserEntity.findByName").setParameter("username", username).getSingleResult();
		if (userEntity != null) {
			return (ApplicantEntity) userEntity;
		} else {
			throw new UserNotFoundException();
		}
	}

	/**
	 * Create a new User, acordding to type of user selected. Send an email to
	 * new user chosen with his username and password
	 *
	 * @param user
	 * @param userType
	 * @throws InvalidAuthException
	 * @throws EmailAlreadyExistsException
	 * @throws UsernameAlreadyExists
	 * @throws EJBException
	 * @throws MessagingException 
	 */
	public void createNewUser(UserEntity user, String userType) throws InvalidAuthException, EmailAlreadyExistsException, UsernameAlreadyExists, EJBException, MessagingException {
		String to = user.getEmail();
		String password = user.getPassword();
//		String passwordEncrypted = EncryptPassword.encrypt(user.getPassword());
		String passwordEncrypted = pw.encrypt(user.getPassword());
		
		user.setPassword(passwordEncrypted);
		if (knowIfUsernameAlreadyExists(user.getUsername())) {
			throw new UsernameAlreadyExists();
		} else if (mailAlreadyExists(user.getEmail())) {
			throw new EmailAlreadyExistsException();
		} else {
			switch (userType) {
			case "Admin":
//				AdminEntity adminentity = new AdminEntity();
//				adminentity.setFirstName(user.getFirstName());
//				adminentity.setLastName(user.getLastName());
//				adminentity.setEmail(user.getEmail());
//				adminentity.setUsername(user.getUsername());
//				adminentity.setPassword(user.getPassword());
				//new AdminEntity("Admin","admin", "jGl2qRg=", "admin@admin", "admin@admin");
				AdminEntity adminentity = new AdminEntity(user.getFirstName(),user.getLastName(),user.getPassword(),user.getEmail(),user.getUsername());
				adminentity.setRole(Role.ADMIN);
<<<<<<< HEAD
//				adminentity.setRole(Role.MANAGER);
//				adminentity.setRole(Role.INTERVIEWER);
=======
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6
				em.persist(adminentity);
				//adminEntityFacade.createAdmin(adminentity);
				mail.sendEMail("acertarrumo2015@gmail.com", "Chosen as new user", "Your login is " + user.getUsername() + " and your password is " + password, to);
				//mail.sendMailTo("acertarrumo2015@gmail.com", "Chosen as new user", "Your login is " + user.getUsername() + " and your password is " + password);
				break;
			case "Manager":
//				ManagerEntity manager = new ManagerEntity();
//				manager.setFirstName(user.getFirstName());
//				manager.setLastName(user.getLastName());
//				manager.setEmail(user.getEmail());
//				manager.setUsername(user.getUsername());
//				manager.setPassword(user.getPassword());
//				UserEntity usertmp1= new ManagerEntity("Carlos", "Santos","pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=", "carlos@gmail.com",
//						"carlos@gmail.com"); //pass 123
				ManagerEntity manager = new ManagerEntity(user.getFirstName(),user.getLastName(),user.getPassword(),user.getEmail(),user.getUsername());
				manager.setRole(Role.MANAGER);
<<<<<<< HEAD
				//manager.setRole(Role.INTERVIEWER);
=======
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6
				em.persist(manager);
				//adminEntityFacade.createManager(manager);
				//mail.sendEMail("acertarrumo2015@gmail.com", "Chosen as new manager", "Your login is " + user.getUsername() + " and your password is " + password, to);

				break;
			default:
//				InterviewerEntity interviewer = new InterviewerEntity();
//				interviewer.setFirstName(user.getFirstName());
//				interviewer.setLastName(user.getLastName());
//				interviewer.setEmail(user.getEmail());
//				interviewer.setUsername(user.getUsername());
//				interviewer.setPassword(user.getPassword());
//				UserEntity	usertmp2 = new InterviewerEntity("Catarina", "Lapo", "s6jg4fmrG/46NvIx9nb3i7MKUZ0rIebFMMDu6Ou0pdA=", "ciclapo@gmail.com",
//						"ciclapo@gmail.com"); //pass 456
				InterviewerEntity interviewer = new InterviewerEntity(user.getFirstName(),user.getLastName(),user.getPassword(),user.getEmail(),user.getUsername());
				interviewer.setRole(Role.INTERVIEWER);
				em.persist(interviewer);
				//adminEntityFacade.createInterviewer(interviewer);
				//mail.sendEMail("acertarrumo2015@gmail.com", "Chosen as new interviewer", "Your login is " + user.getUsername() + " and your password is " + password, to);
				break;
			}
		}
	}

	/**
	 *
	 * @param user
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @throws InvalidAuthException
	 * @throws EmailAlreadyExistsException
	 * @throws EJBException
	 */
<<<<<<< HEAD
	public void editUser(UserEntity user, String firstName, String lastName, String email, String password, String usernam) throws InvalidAuthException, EmailAlreadyExistsException, EJBException {
		if (mailAlreadyExists(user.getEmail())) {
			Logger.getLogger(UserEntityFacade.class.getName()).log(Level.INFO, "EditUser() --> Exists User email. Updating user profile for email="+email);
			//user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setUsername(usernam);
			//user.setPassword(password);
				 String passwordEncrypted = pw.encrypt(password);
				 user.setPassword(passwordEncrypted);
				 Logger.getLogger(UserEntityFacade.class.getName()).log(Level.INFO, "EditUser() --> User password has been updated to "+password+" --> "+passwordEncrypted);
//				
			edit(user);
			//ACTIVATE THE NEXT LINE TO START SENDING EMAILS TO THE USER
			mail.sendEMail("acertarrumo2015@gmail.com", "Edit user", "Your login is " + user.getEmail() + " and your password is " + user.getPassword(),user.getEmail());
=======
	public void editUser(UserEntity user, String firstName, String lastName, String email, String password) throws InvalidAuthException, EmailAlreadyExistsException, EJBException {
		if (!mailAlreadyExists(user.getUsername())) {
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			//String passwordEncrypted = EncryptPassword.encrypt(password);
			String passwordEncrypted = pw.encrypt(user.getPassword());
			user.setPassword(passwordEncrypted);
			edit(user);
			//mail.sendEMail("acertarrumo2015@gmail.com", "Edit user", "Your login is " + user.getUsername() + " and your password is " + password, user.getEmail());
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6
		} else {
			throw new EmailAlreadyExistsException();
		}
	}
	
	/**
	 *
	 * @param user
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @throws InvalidAuthException
	 * @throws EmailDoesNotExistsException
	 * @throws EJBException
	 */
	public void updateUserProfile(UserEntity user, String firstName, String lastName, String email, String password, List<Role> roles) throws InvalidAuthException, EmailDoesNotExistsException, EJBException {
<<<<<<< HEAD
		if (mailAlreadyExists(user.getEmail())) {
=======
		if (mailAlreadyExists(user.getUsername())) {
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6
			//user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			//String passwordEncrypted = EncryptPassword.encrypt(password);
			String passwordEncrypted = pw.encrypt(user.getPassword());
			user.setPassword(passwordEncrypted);
<<<<<<< HEAD
			//user.setRoles(roles);
			edit(user);
			mail.sendEMail("acertarrumo2015@gmail.com", "Edit user", "Your login is " + user.getUsername() + " and your password is " + password, user.getEmail());
			Logger.getLogger(UserEntityFacade.class.getName()).log(Level.INFO, "updateUserProfile() --> User with email= "+email+" has been updated and an email has been sent to inform about the update!");
=======
			user.setRoles(roles);
			edit(user);
			//mail.sendEMail("acertarrumo2015@gmail.com", "Edit user", "Your login is " + user.getUsername() + " and your password is " + password, user.getEmail());
>>>>>>> 5e823c2f5a61818d78e41e3ca12efeaf180278f6
		} else {
			throw new EmailDoesNotExistsException();
		}
	}

	/**
	 *
	 * @param username
	 * @return true if this username already exists in database
	 */
	public
	boolean knowIfUsernameAlreadyExists(String username) {
		Query query = em.createNamedQuery("UserEntity.findByName", UserEntity.class
				);
		query.setParameter(
				"username", username);
		return !query.getResultList()
				.isEmpty();
	}

	/**
	 *
	 * @param email
	 * @return true if this email already exists in database
	 */
	public
	boolean mailAlreadyExists(String email) {
		Query query = em.createNamedQuery("UserEntity.findByEmail", UserEntity.class
				);
		query.setParameter(
				"email", email);
		return !query.getResultList()
				.isEmpty();
	}

	/////////////////////Getters && Setters////////////////////
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public AdminFacade getAdminGuideFacade() {
		return adminEntityFacade;
	}

	public void setAdminGuideFacade(AdminFacade adminEntityFacade) {
		this.adminEntityFacade = adminEntityFacade;
	}

	public SessionContext getCtx() {
		return ctx;
	}

	public void setCtx(SessionContext ctx) {
		this.ctx = ctx;
	}

	public ManagerFacade getManagerFacade() {
		return managerFacade;
	}

	public void setManagerFacade(ManagerFacade managerFacade) {
		this.managerFacade = managerFacade;
	}

	public InterviewerFacade getInterviewerFacade() {
		return interviewerFacade;
	}

	public void setInterviewerFacade(InterviewerFacade interviewerFacade) {
		this.interviewerFacade = interviewerFacade;
	}

}
