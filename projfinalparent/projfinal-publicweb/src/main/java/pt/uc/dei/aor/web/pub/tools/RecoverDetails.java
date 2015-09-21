/* 
 */
package pt.uc.dei.aor.web.pub.tools;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.serv.exceptions.EmailOrUsernameNotFoundException;
import pt.uc.dei.aor.proj.serv.facade.ApplicantFacade;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.UserEntityFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.web.pub.PubUserSession;

/**
 *
 * @author 
 */
@Named
@RequestScoped
public class RecoverDetails {

	private List<ApplicationEntity> lstMyApplications;

	private String password;
	private String email;

	@Inject
	private PubUserSession pubUserSession;
	@EJB
	private UserEntityFacade userGuideFacade;
	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private ApplicantFacade applicantFacade;

	private static Logger log = Logger.getLogger(RecoverDetails.class.getName());

	/**
	 * Creates a new instance of RecoverDetails
	 */
	public RecoverDetails() {
	}

	/**
	 * Send an email to user, that lost his username
	 *
	 * @return to Success Recovery page
	 */
	public String usernameRecovered() {
		try {
			applicantFacade.sendEmailToApplicantThatForgetUsername(email, password);
			return "successrecovery.xhtml";
		} catch (EmailOrUsernameNotFoundException ex) {
			Logger.getLogger(RecoverDetails.class.getName()).log(Level.SEVERE, null, ex);
			//send an error message saying that email and password are incorrect
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}

	}

	/**
	 * Method to make logout
	 *
	 * @return to mainpage if is logout is made successfully. Return to
	 * loginerror page if something goes wrong
	 */
	public String logout() {
		String destination = "/faces/public/mainpage.xhtml?faces-redirect=true";

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request
		= (HttpServletRequest) context.getExternalContext().getRequest();

		//        try {
		//            //make logout successfully
		//            HttpSession session = request.getSession();
		//            session.invalidate();
		//            request.logout();
		//        } catch (ServletException e) {
		//            //catch exception
		//            log.log(Level.SEVERE, "Failed to logout user!", e);
		//            destination = "/loginerror?faces-redirect=true";
		//        }

		//make logout successfully
		HttpSession session = request.getSession();
		session.invalidate();
		((RecoverDetails) request).logout();
		return destination; // go to destination
	}

	/////////////////////Getters && Setters////////////////////
	public String getName() {
		try {
			//Name of logged user
			return pubUserSession.getLoggedUser().getUsername();

		} catch (NoResultException ex) {
			Logger.getLogger(RecoverDetails.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}
	}

	/**
	 *
	 * @return List of Applications of logged user
	 */
	public List<ApplicationEntity> getLstMyApplications() {
		lstMyApplications = applicationFacade.getUserApplications((ApplicantEntity) pubUserSession.getLoggedUser());
		return lstMyApplications;
	}

	public void setLstMyApplications(List<ApplicationEntity> lstMyApplications) {
		this.lstMyApplications = lstMyApplications;
	}

	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		RecoverDetails.log = log;
	}

	public PubUserSession getUserData() {
		return pubUserSession;
	}

	public void setUserData(PubUserSession pubUserSession) {
		this.pubUserSession = pubUserSession;
	}

	public UserEntityFacade getUserGuideFacade() {
		return userGuideFacade;
	}

	public void setUserGuideFacade(UserEntityFacade userGuideFacade) {
		this.userGuideFacade = userGuideFacade;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

	public ApplicantFacade getApplicantFacade() {
		return applicantFacade;
	}

	public void setApplicantFacade(ApplicantFacade applicantFacade) {
		this.applicantFacade = applicantFacade;
	}

}
