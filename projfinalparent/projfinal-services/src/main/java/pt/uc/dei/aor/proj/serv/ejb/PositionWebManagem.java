/*
 */
package pt.uc.dei.aor.proj.serv.ejb;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIPanel;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.primefaces.event.FileUploadEvent;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.exceptions.BeforeDateNotBeforeClosingDate;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCVFileException;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCoverLetterException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAndPasswordNotCorrespondingToLinkedinCredentialsException;
import pt.uc.dei.aor.proj.serv.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.serv.exceptions.ManagerNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.NumberOfMobilePhoneDigitsException;
import pt.uc.dei.aor.proj.serv.exceptions.OpeningDateAfterAtualDate;
import pt.uc.dei.aor.proj.serv.exceptions.PhoneInterviewEntityNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.PositionNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.PositionOfAnApplicantAlreadyIntroducedOnSPonException;
import pt.uc.dei.aor.proj.serv.exceptions.PresentialInterviewEntityNotIntroducedException;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewEntityFacade;
import pt.uc.dei.aor.proj.serv.facade.ManagerFacade;
import pt.uc.dei.aor.proj.serv.facade.PositionFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.StatefulPosition;
import pt.uc.dei.aor.proj.serv.tools.UploadedFiles;
import pt.uc.dei.aor.proj.serv.tools.UserData;
/**
 *
 * @author
 */
@Named
@ViewScoped
public class PositionWebManagem implements Serializable {

	private static final int ONEWEEKONMS = 604800000;

	private List<PositionEntity> lstPositions;
	private List<ManagerEntity> lstManagers;
	private List<InterviewEntity> lstPhoneInterviewsInUse;
	private List<InterviewEntity> lstPresentialInterviewsInUse;
	private List<PositionEntity> lstPositionsBeforeAtualDate;
	private List<PositionEntity> lstPositionsofAManager;

	private ApplicationEntity selectedApplication;
	private PositionEntity position;
	private UIPanel panelGroup;
	private PositionEntity selectedPosition;
	private ManagerEntity selectedManager;
	private InterviewEntity selectedInterviewGuide;
	private UploadedFiles uploadedFiles;
	private ApplicantEntity applicant;
	private ApplicationEntity application;

	//	@Inject
	private UserData userData;
	//	@Inject
	private StatefulPosition statefulPosition;
	@EJB
	private InterviewEntityFacade interviewGuideFacade;
	@EJB
	private ManagerFacade managerFacade;
	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private PositionFacade positionFacade;
	@EJB
	private SendEmail mail;

	/**
	 * Creates a new instance of PositionViewBean
	 */
	public PositionWebManagem() {
	}

	/**
	 *
	 */
	@PostConstruct
	public void init() {
		this.uploadedFiles = new UploadedFiles();
		this.position = new PositionEntity();
		this.application = new ApplicationEntity();
		this.applicant = new ApplicantEntity();
	}

	/**
	 * Creates a new PositionEntity
	 * @return
	 */
	public String addPosition() {
		try {
			positionFacade.createPosition(selectedManager, position, ONEWEEKONMS);
			return "searchpositions?faces-redirect=true";
		} catch (ManagerNotIntroducedException | PhoneInterviewEntityNotIntroducedException | PresentialInterviewEntityNotIntroducedException | BeforeDateNotBeforeClosingDate | OpeningDateAfterAtualDate ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}
	}

	/**
	 * Edit PositionEntity
	 * @return
	 */
	public String editPosition() {
		try {
			positionFacade.editPosition(selectedPosition);
			return "searchpositions?faces-redirect=true";
		} catch (ManagerNotIntroducedException | PhoneInterviewEntityNotIntroducedException | PresentialInterviewEntityNotIntroducedException | BeforeDateNotBeforeClosingDate ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}
	}

	/**
	 * Associates an ApplicationEntity to a PositionEntity
	 * @return
	 */
	public String associateApplicationToPosition() {
		try {
			positionFacade.associateApplicationToPosition(selectedApplication, selectedPosition);
			return "nonSpontaneousApplications?faces-redirect=true";
		} catch (PositionNotIntroducedException | PositionOfAnApplicantAlreadyIntroducedOnSPonException ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	/**
	 * Uploads CV
	 * @param event
	 */
	public void uploadCV1(FileUploadEvent event) {
		uploadedFiles.upload(event, "cv");
		JSFUtil.addSuccessMessage("Success!! " + event.getFile().getFileName() + " was uploaded.");
	}

	/**
	 * Uploads CV
	 * @param event
	 */
	public void uploadCV(FileUploadEvent event) {
		uploadedFiles.upload(event, "cv");
		JSFUtil.addSuccessMessage("Success!! " + event.getFile().getFileName() + " was uploaded.");
	}

	/**
	 * Uploads Cover Letter
	 * @param event
	 */
	public void uploadMotivationLetter(FileUploadEvent event) {
		uploadedFiles.upload(event, "cl");
		JSFUtil.addSuccessMessage("Success!! " + event.getFile().getFileName() + " was uploaded.");
	}

	/**
	 * Creates an ApplicationEntity
	 * @return
	 */
	public String createManualApplication() {
		try {
			applicationFacade.createSpontaneousApplicationOfNewApplicant(applicant,application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName());
		} catch (InvalidAuthException | EmailAlreadyExistsException | NumberOfMobilePhoneDigitsException | DoNotUploadCVFileException | DoNotUploadCoverLetterException ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException | EmailAndPasswordNotCorrespondingToLinkedinCredentialsException ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "spontaneousApplications.xhtml?faces-redirect=true";
	}

	/**
	 * Creates an ApplicationEntity
	 * @return
	 */
	public String createApplication() {
		try {
			applicationFacade.createApplicationOfNewApplicant(applicant, application, uploadedFiles.getCvUploadName(), uploadedFiles.getClUploadName(), statefulPosition.getPosition());
		} catch (InvalidAuthException | EmailAlreadyExistsException | NumberOfMobilePhoneDigitsException | DoNotUploadCVFileException | DoNotUploadCoverLetterException ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException | EmailAndPasswordNotCorrespondingToLinkedinCredentialsException ex) {
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
		}
		return "nonSpontaneousApplications.xhtml?faces-redirect=true";
	}

	/**
	 *
	 * @param otherPosition
	 * @return
	 */
	public String goToApplicationForm(PositionEntity otherPosition) {
		statefulPosition.setPosition(otherPosition);
		return "apply.xhtml?faces-redirect=true";
	}

	/**
	 *
	 * @param position
	 * @return true if it is possible to edit PositionEntity
	 */
	public boolean canEditPosition(PositionEntity position) {
		return positionFacade.associateToApplication(position);
	}

	/**
	 *
	 * @param position
	 * @return true if it is possible to an ApplicationEntity to that position
	 */
	public boolean canAssociatePositionToApplication(PositionEntity position) {
		return position.getStatus().equals("OPEN") && position.getVacancies()>0;
	}

	/**
	 *
	 */
	public void showPanel() {
		panelGroup.setRendered(true);
	}

	/////////////////////Getters && Setters////////////////////

	public List<ManagerEntity> getLstManagers() {
		return managerFacade.findAll();
	}

	public void setLstManagers(List<ManagerEntity> lstManagers) {
		this.lstManagers = lstManagers;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

	public ApplicationEntity getSelectedApplication() {
		return selectedApplication;
	}

	public void setSelectedApplication(ApplicationEntity selectedApplication) {
		this.selectedApplication = selectedApplication;
	}

	public List<PositionEntity> getLstPositions() {
		return positionFacade.findAll();
	}

	public void setLstPositions(List<PositionEntity> lstPositions) {
		this.lstPositions = lstPositions;
	}

	public PositionEntity getPosition() {
		return position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public UIPanel getPanelGroup() {
		return panelGroup;
	}

	public void setPanelGroup(UIPanel panelGroup) {
		this.panelGroup = panelGroup;
	}

	public PositionEntity getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(PositionEntity selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public ManagerEntity getSelectedManager() {
		return selectedManager;
	}

	public void setSelectedManager(ManagerEntity selectedManager) {
		this.selectedManager = selectedManager;
	}

	public PositionFacade getPositionFacade() {
		return positionFacade;
	}

	public void setPositionFacade(PositionFacade positionFacade) {
		this.positionFacade = positionFacade;
	}

	public SendEmail getMail() {
		return mail;
	}

	public void setMail(SendEmail mail) {
		this.mail = mail;
	}

	public InterviewEntity getSelectedInterviewGuide() {
		return selectedInterviewGuide;
	}

	public void setSelectedInterviewGuide(InterviewEntity selectedInterviewGuide) {
		this.selectedInterviewGuide = selectedInterviewGuide;
	}

	public ManagerFacade getManagerFacade() {
		return managerFacade;
	}

	public void setManagerFacade(ManagerFacade managerFacade) {
		this.managerFacade = managerFacade;
	}

	public void setTheSelectedManager() {
		position.setManager(selectedManager);
	}

	public void setTheSelectedPhoneGuideInterview() {
		position.setPhoneInterviewEntity(selectedInterviewGuide);
	}

	public void setTheSelectedPresentialGuideInterview() {
		position.setPresencialInterviewEntity(selectedInterviewGuide);
	}

	public List<InterviewEntity> getLstInterviews() {
		return interviewGuideFacade.findAll();
	}

	public InterviewEntityFacade getInterviewGuideFacade() {
		return interviewGuideFacade;
	}

	public void setInterviewGuideFacade(InterviewEntityFacade interviewGuideFacade) {
		this.interviewGuideFacade = interviewGuideFacade;
	}

	public List<InterviewEntity> getLstPhoneInterviewsInUse() {
		return interviewGuideFacade.lstPhoneInterviewsInUseWithQuestions();
	}

	public void setLstPhoneInterviewsInUse(List<InterviewEntity> lstPhoneInterviewsInUse) {
		this.lstPhoneInterviewsInUse = lstPhoneInterviewsInUse;
	}

	public List<InterviewEntity> getLstPresentialInterviewsInUse() {
		return interviewGuideFacade.lstPresentialInterviewsInUseWithQuestions();
	}

	public void setLstPresentialInterviewsInUse(List<InterviewEntity> lstPresentialInterviewsInUse) {
		this.lstPresentialInterviewsInUse = lstPresentialInterviewsInUse;
	}

	public List<PositionEntity> getLstPositionsBeforeAtualDate() {
		return positionFacade.lstPositionAtualDateBeforeClosingDate(new Date());
	}

	public void setLstPositionsBeforeAtualDate(List<PositionEntity> lstPositionsBeforeAtualDate) {
		this.lstPositionsBeforeAtualDate = lstPositionsBeforeAtualDate;
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

	public void setUploadedFiles(UploadedFiles uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public ApplicantEntity getApplicant() {
		return applicant;
	}

	public void setApplicant(ApplicantEntity applicant) {
		this.applicant = applicant;
	}

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public List<PositionEntity> getLstPositionsofAManager() {
		try {
			try {
				lstPositionsofAManager = positionFacade.lstPositionsOfManager((ManagerEntity) userData.getLoggedUser());
			} catch (UserNotFoundException
					| pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (NoResultException ex) {
			// TODO Auto-generated catch block
			Logger.getLogger(PositionWebManagem.class.getName()).log(Level.SEVERE, null, ex);
		}
		return lstPositionsofAManager;
	}

	public void setLstPositionsofAManager(List<PositionEntity> lstPositionsofAManager) {
		this.lstPositionsofAManager = lstPositionsofAManager;
	}

}
