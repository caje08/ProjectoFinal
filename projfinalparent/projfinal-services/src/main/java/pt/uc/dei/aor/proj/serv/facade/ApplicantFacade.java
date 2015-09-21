/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.GroupsEntity;
import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplicant;
import pt.uc.dei.aor.proj.ejb.PasswordEJB;
import pt.uc.dei.aor.proj.serv.ejb.SendEmail;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAndPasswordNotCorrespondingToLinkedinCredentialsException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailOrUsernameNotFoundException;
import pt.uc.dei.aor.proj.serv.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.serv.exceptions.NumberOfMobilePhoneDigitsException;
import pt.uc.dei.aor.proj.serv.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.tools.AutomaticUsername;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.RandomNumber;

/**
 *
 * @author
 */
@Stateless
public class ApplicantFacade extends AbstractFacade<ApplicantEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@Resource
	SessionContext ctx;

	//    @Inject
	//    private TokenLinkedin tokenLinkedin;
	@EJB
	private SendEmail sendEmail;
	@EJB
	private GroupsHasUserGuideFacade groupsHasUserGuideFacade;
	@EJB
	private PasswordEJB pw;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public ApplicantFacade() {
		super(ApplicantEntity.class);
	}

	/**
	 *
	 * @return the applicant if the applicant is !=null. If is null
	 * @throws
	 * pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException
	 */
	public ApplicantEntity findUserByUsername() throws UserNotFoundException, EJBException {
		//query to find applicant by his username
		ApplicantEntity applicant = (ApplicantEntity) em.createNamedQuery("ApplicantEntity.findByUsername").setParameter("username", ctx.getCallerPrincipal().getName()).getSingleResult();
		if (applicant != null) {
			return applicant;
		} else {
			throw new UserNotFoundException();
		}

	}

	/**
	 *
	 * @return all Applicants with Linkedin account on databse
	 */
	public List<ApplicantEntity> applicantsWithLinkedin() {
		//find all applicants
		List<ApplicantEntity> allApplicants = findAll();
		List<ApplicantEntity> applicantsWithLinkedin = new ArrayList<>();
		for (ApplicantEntity allApplicant : allApplicants) {
			if (allApplicant.getAcessToken() != null) {
				applicantsWithLinkedin.add(allApplicant);
			}
		}
		return applicantsWithLinkedin;
	}

	/**
	 *
	 * @param email
	 * @param password
	 * @throws EmailOrUsernameNotFoundException
	 */
	public void sendEmailToApplicantThatForgetUsername(String email, String password) throws EmailOrUsernameNotFoundException {
		if (existEmail(email, password)) {
			ApplicantEntity applicant = (ApplicantEntity) findByEmailAndPassword(email, password);
			//send an email to applicant who forget username with his username
			sendEmail.sendEMail("acertarrumo2015@gmail.com", "Recovery username", "Your username " + applicant.getUsername(), applicant.getEmail());
		} else {
			throw new EmailOrUsernameNotFoundException();
		}

	}

	/**
	 *
	 * @param email
	 * @param password
	 * @return an object consisting of email and password inserted
	 */
	public Object findByEmailAndPassword(String email, String password) {
		Query query = em.createNamedQuery("ApplicantEntity.findByEmailAndPass", UserEntity.class);
		query.setParameter("email", email);
		query.setParameter("password", pw.encrypt(password));
		return query.getSingleResult();
	}

	/**
	 *
	 * @param email
	 * @param password
	 * @return true if exists an user with email and password inserted
	 */
	public boolean existEmail(String email, String password) {
		Query query = em.createNamedQuery("ApplicantEntity.findByEmailAndPass", UserEntity.class);
		query.setParameter("email", email);
		query.setParameter("password", pw.encrypt(password));
		return !query.getResultList().isEmpty();

	}

	/**
	 * Create a new ApplicantEntity
	 *
	 * @param applicant
	 * @param isLinkedin
	 * @throws InvalidAuthException
	 * @throws EmailAlreadyExistsException
	 * @throws NumberOfMobilePhoneDigitsException
	 * @throws EmailAndPasswordNotCorrespondingToLinkedinCredentialsException
	 */
	public void createApplicant(ApplicantEntity applicant) throws InvalidAuthException, EmailAlreadyExistsException, NumberOfMobilePhoneDigitsException, EmailAndPasswordNotCorrespondingToLinkedinCredentialsException {
		//if phone mobile number has more than 8 digits
		if (applicant.getMobile().length() >= 9) {
			//if user do not exists
			Logger.getLogger(ApplicantFacade.class.getName()).log(Level.INFO, "Inside ApplicantFacade.createApplicant() before checking if mailAlreadyExists("+applicant.getUsername()+")\n");
			//System.out.println("\nInside ApplicantFacade.createApplicant() before checking if mailAlreadyExists("+applicant.getUsername()+")\n");
			if (!mailAlreadyExists(applicant.getEmail())){//.getUsername())) {
				String password = applicant.getPassword();
				String encrypted = pw.encrypt(applicant.getPassword());
				//generate a username
				String automaticUserName = AutomaticUsername.getUsername(applicant.getFirstName(), applicant.getLastName(), applicant.getMobile());
				applicant.setPassword(encrypted);
				//if this username already exists add one more random number
				if (automaticUserNameAlreadyExists(automaticUserName)) {
					applicant.setUsername(automaticUserName + String.valueOf(RandomNumber.randInt(1, 10)));
				} else {
					applicant.setUsername(automaticUserName);
				}
				//set applicant status to submitted
				applicant.setStatus(StatusApplicant.SUBMITTED);
				applicant.setRole(Role.CANDIDATE);
				//System.out.println("\nInside ApplicantFacade.createApplicant() before creating applicant="+applicant.toString()+")\n");
				Logger.getLogger(ApplicantFacade.class.getName()).log(Level.INFO, "Inside ApplicantFacade.createApplicant() before creating applicant="+applicant.toString()+")\n");
				create(applicant);
				//persist applicant in GroupsHasUserguide entity

				/*try {
					groupsHasUserGuideFacade.persistApplicant(applicant);
				} catch (pt.uc.dei.aor.proj.db.exceptions.InvalidAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/

				//send email to new user
				sendEmail.sendEMail("acertarrumo2015@gmail.com", "Chosen as new user", "Your login is " + applicant.getUsername() + " and your password is " + password, applicant.getEmail());
				JSFUtil.addSuccessMessage("Applicant account has been created and an email sent with details! Please, check your email!");
				Logger.getLogger(ApplicantFacade.class.getName()).log(Level.INFO, "Inside ApplicantFacade.createApplicant() after sending email to new applicant="+applicant.getEmail()+")\n");
			} else {
				JSFUtil.addErrorMessage("Email already exists. Please Login before submitting the application.");
				throw new EmailAlreadyExistsException();
			}
		} else {
			JSFUtil.addErrorMessage("Mobile number is having more digits than expected!.");
			throw new NumberOfMobilePhoneDigitsException();
		}
	}


	/**
	 *
	 * @param groupname
	 * @return group, from groupname inserted
	 * @throws InvalidAuthException
	 */
	public GroupsEntity
	findGroupByName(String groupname) throws InvalidAuthException {
		try {
			Query query = em.createNamedQuery("GroupsEntity.findByGroupName", GroupsEntity.class
					);
			query.setParameter(
					"groupname", groupname);
			return (GroupsEntity) query.getSingleResult();

		} catch (NoResultException ex) {
			Logger.getLogger(AdminEntity.class
					.getName()).log(Level.SEVERE, "Error finding group", ex);
			throw new InvalidAuthException();
		}
	}

	/**
	 *
	 * @param username
	 * @return true if there is already a applicant with username inserted
	 */
	public boolean automaticUserNameAlreadyExists(String username) {
		Query query = em.createNamedQuery("ApplicantEntity.findByUsername", UserEntity.class
				);
		query.setParameter(
				"username", username);
		return !query.getResultList()
				.isEmpty();
	}

	/**
	 *
	 * @param email
	 * @return true if email already exists
	 */
	public boolean mailAlreadyExists(String email) {
		Query query = em.createNamedQuery("ApplicantEntity.findByEmail", UserEntity.class
				);
		query.setParameter(
				"email", email);
		return !query.getResultList()
				.isEmpty();
	}

	/**
	 * Send ApplicantEntity to blacklist. The applicant status will be rejected
	 * @param applicant
	 * @throws EJBException
	 */
	public void toBlackList(ApplicantEntity applicant) throws EJBException {
		applicant.setStatus(StatusApplicant.REJECTED);
		applicant.setMotive(RejectionMotive.CV);
		edit(applicant);
	}

	/////////////////////Getters && Setters////////////////////
	public SendEmail getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(SendEmail sendEmail) {
		this.sendEmail = sendEmail;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public SessionContext getCtx() {
		return ctx;
	}

	public void setCtx(SessionContext ctx) {
		this.ctx = ctx;
	}

	public GroupsHasUserGuideFacade getGroupsHasUserGuideFacade() {
		return groupsHasUserGuideFacade;
	}

	public void setGroupsHasUserGuideFacade(GroupsHasUserGuideFacade groupsHasUserGuideFacade) {
		this.groupsHasUserGuideFacade = groupsHasUserGuideFacade;
	}

	/* public TokenLinkedin getTokenLinkedin() {
        return tokenLinkedin;
    }

    public void setTokenLinkedin(TokenLinkedin tokenLinkedin) {
        this.tokenLinkedin = tokenLinkedin;
    }*/

}
