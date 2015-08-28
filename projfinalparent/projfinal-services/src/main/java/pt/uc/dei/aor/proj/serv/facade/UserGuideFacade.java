/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import pt.uc.dei.aor.proj.serv.tools.EncryptPassword;

/**
 *
 * @author
 */
@Stateless
public class UserGuideFacade extends AbstractFacade<UserEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@Inject
	private AdminFacade adminGuideFacade;
	@Inject
	private ManagerFacade managerFacade;
	@Inject
	private InterviewerFacade interviewerFacade;
	//	@Inject
	//	private SendEmail mail;

	@Resource
	SessionContext ctx;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public UserGuideFacade() {
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
		UserEntity userGuide = (UserEntity) em.createNamedQuery("UserEntity.findByName").setParameter("username", ctx.getCallerPrincipal().getName()).getSingleResult();
		if (userGuide != null) {
			if (userGuide instanceof ManagerEntity || userGuide instanceof AdminEntity || userGuide instanceof InterviewerEntity) {
				return userGuide;
			} else {
				throw new UserGuideException();
			}
		} else {
			throw new NoResultException();
		}
	}


	/**
	 *
	 * @param username
	 * @return ApplicantEntity find by inputed username
	 * @throws UserNotFoundException
	 */
	public ApplicantEntity findApplicantByUsernameInput(String username) throws UserNotFoundException {
		UserEntity userGuide = (UserEntity) em.createNamedQuery("UserEntity.findByName").setParameter("username", username).getSingleResult();
		if (userGuide != null) {
			return (ApplicantEntity) userGuide;
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
	 */
	public void createNewUser(UserEntity user, String userType) throws InvalidAuthException, EmailAlreadyExistsException, UsernameAlreadyExists, EJBException {
		String to = user.getEmail();
		String password = user.getPassword();
		String passwordEncrypted = EncryptPassword.encrypt(user.getPassword());
		user.setPassword(passwordEncrypted);
		if (knowIfUsernameAlreadyExists(user.getUsername())) {
			throw new UsernameAlreadyExists();
		} else if (mailAlreadyExists(user.getEmail())) {
			throw new EmailAlreadyExistsException();
		} else {
			switch (userType) {
			case "Admin":
				AdminEntity adminentity = new AdminEntity();
				adminentity.setFirstName(user.getFirstName());
				adminentity.setLastName(user.getLastName());
				adminentity.setEmail(user.getEmail());
				adminentity.setUsername(user.getUsername());
				adminentity.setPassword(user.getPassword());
				adminentity.setRole(Role.ADMIN);
				em.persist(adminentity);
				//adminGuideFacade.createAdmin(adminentity);
				//mail.sendEMail("acertarorumo@gmail.com", "Chosen as new user", "Your login is " + user.getUsername() + " and your password is " + password, to);
				break;
			case "ManagerEntity":
				ManagerEntity manager = new ManagerEntity();
				manager.setFirstName(user.getFirstName());
				manager.setLastName(user.getLastName());
				manager.setEmail(user.getEmail());
				manager.setUsername(user.getUsername());
				manager.setPassword(user.getPassword());
				manager.setRole(Role.MANAGER);
				em.persist(manager);
				//adminGuideFacade.createManager(manager);
				//mail.sendEMail("acertarorumo@gmail.com", "Chosen as new manager", "Your login is " + user.getUsername() + " and your password is " + password, to);

				break;
			default:
				InterviewerEntity interviewer = new InterviewerEntity();
				interviewer.setFirstName(user.getFirstName());
				interviewer.setLastName(user.getLastName());
				interviewer.setEmail(user.getEmail());
				interviewer.setUsername(user.getUsername());
				interviewer.setPassword(user.getPassword());
				interviewer.setRole(Role.INTERVIEWER);
				em.persist(interviewer);
				//adminGuideFacade.createInterviewer(interviewer);
				//mail.sendEMail("acertarorumo@gmail.com", "Chosen as new interviewer", "Your login is " + user.getUsername() + " and your password is " + password, to);
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
	public void editUser(UserEntity user, String firstName, String lastName, String email, String password) throws InvalidAuthException, EmailAlreadyExistsException, EJBException {
		if (!mailAlreadyExists(user.getUsername())) {
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			String passwordEncrypted = EncryptPassword.encrypt(password);
			user.setPassword(passwordEncrypted);
			edit(user);
			//mail.sendEMail("acertarorumo@gmail.com", "Edit user", "Your login is " + user.getUsername() + " and your password is " + password, user.getEmail());
		} else {
			throw new EmailAlreadyExistsException();
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
		return adminGuideFacade;
	}

	public void setAdminGuideFacade(AdminFacade adminGuideFacade) {
		this.adminGuideFacade = adminGuideFacade;
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
