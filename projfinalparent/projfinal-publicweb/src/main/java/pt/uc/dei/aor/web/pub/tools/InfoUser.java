/* 
 */
package pt.uc.dei.aor.web.pub.tools;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.scribe.model.Token;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.serv.ejb.SendEmail;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCVFileException;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCoverLetterException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAndPasswordNotCorrespondingToLinkedinCredentialsException;
import pt.uc.dei.aor.proj.serv.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.serv.exceptions.NumberOfMobilePhoneDigitsException;
import pt.uc.dei.aor.proj.serv.facade.ApplicantFacade;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.PositionFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.StatefulPosition;
import pt.uc.dei.aor.proj.serv.tools.UploadedFiles;
import pt.uc.dei.aor.web.pub.PubActiveSession;
import pt.uc.dei.aor.web.pub.PubUserSession;

/**
 *
 * @author 
 */
@Named
@ViewScoped
public class InfoUser implements Serializable {

	private List<PositionEntity> lstOpenPositions;
	private List<PositionEntity> lstApplicantPositions;

	private PositionEntity selectedPosition;
	private ApplicationEntity selectedApplication;
	private ApplicantEntity applicant;
	private ApplicantEntity loggedApplicant;
	private ApplicationEntity application;
	private UploadedFiles uploadedFiles;
	private String signInLinkedin;
	private boolean formRendered;
	private boolean isLinkedin;
	private String emailLinkedin;
	private String passwordLinkedin;
	private String cvText;
	private String cvUploadName;
	private String clUploadName;
	private String coverLetterText;

	//    @Inject
	//    private TokenLinkedin tokenLinkedin;
	@EJB
	private StatefulPosition statefulPosition;
	@EJB
	private SendEmail sendEmail;
	@EJB
	private ApplicantFacade applicantFacade;
	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private PubUserSession loggedUser;
	@EJB
	private PositionFacade positionFacade;
	@Inject
	private PubActiveSession activePosition;
	@Inject
	private PubActiveSession activeSession;
	/**
	 * Initializes bean (InfoUser).
	 */
	@PostConstruct
	public void init() {
		this.applicant = new ApplicantEntity();
		this.application = new ApplicationEntity();
		this.uploadedFiles = new UploadedFiles();
	}

	public InfoUser() {
	}

	/**
	 * Upload cv, displaying an message saying that CV was uploaded with success
	 *
	 * @param event
	 */
	public void uploadCV(FileUploadEvent event) {
		uploadedFiles.upload(event, "cv");
		this.cvUploadName=uploadedFiles.getCvUploadName();
		Logger.getLogger(InfoUser.class.getName()).log(Level.INFO,"\nInside uploadCV(), created file "+this.cvUploadName);
		JSFUtil.addSuccessMessage("Success!! " + event.getFile().getFileName() + " was uploaded.");
		coverLetterText = event.getFile().getFileName();
	}

	/**
	 * Upload Cover Letter, displaying an message saying that Cover Letter was
	 * uploaded with success
	 *
	 * @param event
	 */
	public void uploadMotivationLetter(FileUploadEvent event) {
		uploadedFiles.upload(event, "cl");
		this.clUploadName=uploadedFiles.getClUploadName();
		Logger.getLogger(InfoUser.class.getName()).log(Level.INFO,"\ninside uploadCV(), created file "+this.clUploadName);
		JSFUtil.addSuccessMessage("Success!! " + event.getFile().getFileName() + " was uploaded.");
		cvText = event.getFile().getFileName();
		getCvText();
	}

	/**
	 * Stateful PositionEntity will be the selected position chosen on data Table
	 *
	 * @param otherPosition
	 * @return to apply page
	 */
	public String goToApplicationForm(PositionEntity otherPosition) {
		activePosition.setActivePosition(otherPosition);
		statefulPosition.setPosition(otherPosition);
		return "/pages/public/sendapplication.xhtml?faces-redirect=true";
	}

	/**
	 * Stateful PositionEntity will be the selected position chosen on data Table
	 *
	 * @param otherPosition
	 * @return to user apply
	 */
	public String goToApplicationFormRA(PositionEntity otherPosition) {
		activePosition.setActivePosition(otherPosition);
		statefulPosition.setPosition(otherPosition);
		return "submituserapplication.xhtml?faces-redirect=true";
	}

	/**
	 * Edit application selected
	 */
	public void editApplication() {
		try {
			selectedApplication.setCoverLetter(clUploadName);
			selectedApplication.setCv(cvUploadName);
			applicationFacade.edit(selectedApplication);
		} catch (EJBException e) {
			Logger.getLogger(InfoUser.class.getName()).
			log(Level.SEVERE, null, "Error editing application");
		}
	}

	/**
	 * Create String access token, secret token and raw response
	 */
	//    public void generateToken() {
	//        try {
	//            Token t = tokenLinkedin.generateAcessToken(emailLinkedin, passwordLinkedin);
	//            applicant.setTokens(t);
	//            formRendered = true;
	//            setIsLinkedin(true);
	//
	//        } catch (EmailAndPasswordNotCorrespondingToLinkedinCredentialsException ex) {
	//            Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, ex);
	//            JSFUtil.addErrorMessage(ex.getMessage());
	//        }
	//    }

	/**
	 *
	 * @return to mainpage if exception is not occurring and new
	 * ApplicationEntity is created
	 */
	public String createApplication() {
		try {
			selectedPosition=activePosition.getActivePosition();
			Logger.getLogger(InfoUser.class.getName()).log(Level.INFO,"inside createApplication() and before createApplicationOfNewApplicant() with selectedPosition.getTitle= "+selectedPosition.getTitle());
			//applicationFacade.createApplicationOfNewApplicant(applicant, application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName(), statefulPosition.getPosition());
			applicationFacade.createApplicationOfNewApplicant(applicant, application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName(), selectedPosition);
			return "/pages/publogin.xhtml?faces-redirect=true";
		} catch (InvalidAuthException | EmailAlreadyExistsException | NumberOfMobilePhoneDigitsException | DoNotUploadCVFileException | DoNotUploadCoverLetterException | EmailAndPasswordNotCorrespondingToLinkedinCredentialsException ex) {
			//if the number of digits are less than 9, the email aleady exists or CV and Cover Letter were not uploaded
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException e) {
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, "Error creating application");
			JSFUtil.addErrorMessage("Error creating application.");
			return null;
		}

	}

	/**
	 *
	 * @return to page successaplied if exception is not occurring and new
	 * Application is created
	 */
	public String createManualApplication() {
		try {
			applicationFacade.createSpontaneousApplicationOfNewApplicant(applicant, application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName());
			return "/publogin.xhtml?faces-redirect=true";
		} catch (InvalidAuthException | EmailAlreadyExistsException | NumberOfMobilePhoneDigitsException | DoNotUploadCVFileException | DoNotUploadCoverLetterException ex) {
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException | EmailAndPasswordNotCorrespondingToLinkedinCredentialsException ex) {
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}

	}

	/**
	 *
	 * @return to main page if exception is not occurring and new
	 * Application is created
	 */
	public String createUserApplications() {
		try {
			applicant = (ApplicantEntity) activeSession.getActiveUser();
			selectedPosition=activePosition.getActivePosition();
			Logger.getLogger(InfoUser.class.getName()).log(Level.INFO, "Inside createUserApplications(), with selectedPosition="+selectedPosition+" and Applicant.email="+applicant.getEmail());
			applicationFacade.createUserApplications(application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName(), selectedPosition, application.getSource(), applicant);
			//applicationFacade.createUserApplications(application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName(), statefulPosition.getPosition(), application.getSource(), loggedUser.getLoggedUser());
			return "/pages/candidate/indexmainuser.xhtml?faces-redirect=true";
		} catch (DoNotUploadCVFileException | DoNotUploadCoverLetterException ex) {
			// or CV and Cover Letter were not uploaded
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException ex) {
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, "Error creating application");
			return null;
		}

	}

	/**
	 *
	 * @return to page mainpage if exception is not occurring and new
	 * Application is created
	 */
	public String createManualUserApplications() {
		try {
			applicant = (ApplicantEntity) activeSession.getActiveUser();
			Logger.getLogger(InfoUser.class.getName()).log(Level.INFO, "Inside createManualUserApplications() loggedUser.getEmail="+applicant.getEmail());
			applicationFacade.createManualUserApplications(application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName(), application.getSource(), applicant);
			return "/pages/candidate/indexmainuser.xhtml?faces-redirect=true";
		} catch (DoNotUploadCVFileException | DoNotUploadCoverLetterException ex) {
			// or CV and Cover Letter were not uploaded
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		} catch (EJBException ex) {
			Logger.getLogger(InfoUser.class.getName()).log(Level.SEVERE, null, "Error creating application");
			return null;
		}

	}

	/**
	 *
	 * @param application
	 * @return true if the current applicant could edit a specific application,
	 * within a period of 24 hours
	 */
	public boolean couldEdit(ApplicationEntity application) {
		return applicationFacade.edit24hApplication(application);
	}

	/////////////////////Getters && Setters////////////////////
	/**
	 *
	 * @return List of Positions that loggedUser(Aplicant) can apply.
	 */
	public List<PositionEntity> getLstApplicantPositions() {
		applicant = (ApplicantEntity) activeSession.getActiveUser();
		//lstApplicantPositions = positionFacade.lstPositionThatApplicantCanApply((ApplicantEntity) loggedUser.getLoggedUser());
		lstApplicantPositions = positionFacade.lstPositionThatApplicantCanApply(applicant);
		return lstApplicantPositions;
	}

	public void setLstApplicantPositions(List<PositionEntity> lstApplicantPositions) {
		this.lstApplicantPositions = lstApplicantPositions;
	}

	/**
	 *
	 * @return List of open Positions (public positions) whom closing date is
	 * after the present date
	 *
	 */
	public List<PositionEntity> getLstOpenPositions() {
		Date date = new Date();
		lstOpenPositions = positionFacade.lstPositionPublicAtualDateBeforeClosingDate(date);
		return lstOpenPositions;
	}

	public void setLstOpenPositions(List<PositionEntity> lstOpenPositions) {
		this.lstOpenPositions = lstOpenPositions;
	}

	/**
	 *
	 * @return List of Applications made by Logged User
	 */
	public List<ApplicationEntity> getLstApplications() {
		
		//applicant = (ApplicantEntity) loggedUser.getLoggedUser();
		applicant = (ApplicantEntity) activeSession.getActiveUser();
		Logger.getLogger(InfoUser.class.getName()).log(Level.INFO,"Inside getLstApplications() and before returning list of user applications for loggeduser="+loggedUser.getLoggedUser());
		return applicationFacade.getUserApplications(applicant);
	}

	public PositionEntity getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(PositionEntity selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public PositionFacade getPositionFacade() {
		return positionFacade;
	}

	public void setPositionFacade(PositionFacade positionFacade) {
		this.positionFacade = positionFacade;
	}

	public ApplicantEntity getApplicant() {
		return applicant;
	}

	public void setApplicant(ApplicantEntity applicant) {
		this.applicant = applicant;
	}

	public ApplicantEntity getLoggedApplicant() {
		loggedApplicant = (ApplicantEntity) loggedUser.getLoggedUser();
		return loggedApplicant;
	}

	public void setLoggedApplicant(ApplicantEntity loggedApplicant) {
		this.loggedApplicant = loggedApplicant;
	}

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public SendEmail getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(SendEmail sendEmail) {
		this.sendEmail = sendEmail;
	}

	public ApplicantFacade getApplicantFacade() {
		return applicantFacade;
	}

	public void setApplicantFacade(ApplicantFacade applicantFacade) {
		this.applicantFacade = applicantFacade;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

	public StatefulPosition getStatefulPosition() {
		return statefulPosition;
	}

	public void setStatefulPosition(StatefulPosition statefulPosition) {
		this.statefulPosition = statefulPosition;
	}

	public UploadedFiles getUploadedFiles() {
		return uploadedFiles;
	}

	public ApplicationEntity getSelectedApplication() {
		return selectedApplication;
	}

	public void setSelectedApplication(ApplicationEntity selectedApplication) {
		this.selectedApplication = selectedApplication;
	}

	/*public String getSignInLinkedin() {
		return signInLinkedin;
	}

	public void setSignInLinkedin(String signInLinkedin) {
		this.signInLinkedin = signInLinkedin;
	}*/

	public boolean isFormRendered() {
		return formRendered;
	}

	public void setFormRendered(boolean formRendered) {
		this.formRendered = formRendered;
		setIsLinkedin(false);
		applicant.setTokens(new Token("", "", ""));

	}

	public boolean isIsLinkedin() {
		return isLinkedin;
	}

	public void setIsLinkedin(boolean isLinkedin) {
		this.isLinkedin = isLinkedin;
	}
//
//	public String getEmailLinkedin() {
//		return emailLinkedin;
//	}
//
//	public void setEmailLinkedin(String emailLinkedin) {
//		this.emailLinkedin = emailLinkedin;
//	}
//
//	public String getPasswordLinkedin() {
//		return passwordLinkedin;
//	}
//
//	public void setPasswordLinkedin(String passwordLinkedin) {
//		this.passwordLinkedin = passwordLinkedin;
//	}

	//    public TokenLinkedin getTokenLinkedin() {
	//        return tokenLinkedin;
	//    }
	//
	//    public void setTokenLinkedin(TokenLinkedin tokenLinkedin) {
	//        this.tokenLinkedin = tokenLinkedin;
	//    }

	public String getCvText() {
		if (cvText != null) {
			return cvText;
		} else {
			return "";
		}
	}

	public void setCvText(String cvText) {
		this.cvText = cvText;
	}

	public String getCoverLetterText() {
		if (coverLetterText != null) {
			return coverLetterText;
		} else {
			return "";
		}
	}

	public void setCoverLetterText(String coverLetterText) {
		this.coverLetterText = coverLetterText;
	}

	public String getCvUploadName() {
		return cvUploadName;
	}

	public void setCvUploadName(String cvUploadName) {
		this.cvUploadName = cvUploadName;
	}

	public String getClUploadName() {
		return clUploadName;
	}

	public void setClUploadName(String clUploadName) {
		this.clUploadName = clUploadName;
	}
	
}
