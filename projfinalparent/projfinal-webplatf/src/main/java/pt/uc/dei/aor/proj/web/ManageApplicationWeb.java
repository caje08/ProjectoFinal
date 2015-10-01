/*
 */
package pt.uc.dei.aor.proj.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.component.UIPanel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.primefaces.event.FileUploadEvent;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewFeedbackEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.ejb.ApplicationWebManagem;
import pt.uc.dei.aor.proj.serv.ejb.SendEmail;
import pt.uc.dei.aor.proj.serv.exceptions.BeforeDateNotBeforeClosingDate;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCVFileException;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCoverLetterException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAndPasswordNotCorrespondingToLinkedinCredentialsException;
import pt.uc.dei.aor.proj.serv.exceptions.InterviewsNotFoundToThisUserException;
import pt.uc.dei.aor.proj.serv.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.serv.exceptions.ManagerNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.NumberOfMobilePhoneDigitsException;
import pt.uc.dei.aor.proj.serv.exceptions.OpeningDateAfterAtualDate;
import pt.uc.dei.aor.proj.serv.exceptions.PhoneInterviewEntityNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.PositionNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.PositionOfAnApplicantAlreadyIntroducedOnSPonException;
import pt.uc.dei.aor.proj.serv.exceptions.PositionsNotFoundToThisUserException;
import pt.uc.dei.aor.proj.serv.exceptions.PresentialInterviewEntityNotIntroducedException;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewEntityFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewFeedbackFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewerFacade;
import pt.uc.dei.aor.proj.serv.facade.ManagerFacade;
import pt.uc.dei.aor.proj.serv.facade.PositionFacade;
import pt.uc.dei.aor.proj.serv.facade.UserEntityFacade;
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
public class ManageApplicationWeb implements Serializable {

	private static final int ONEWEEKONMS = 604800000;

	private List<PositionEntity> lstPositions;
	private List<ManagerEntity> lstManagers;
	private List<InterviewEntity> lstPhoneInterviewsInUse;
	private List<InterviewEntity> lstPresentialInterviewsInUse;
	private List<PositionEntity> lstPositionsBeforeAtualDate;
	private List<PositionEntity> lstPositionsofAManager;
	private List<InterviewFeedbackEntity> lstInterviewsOfInterviewer;
	private List<UserEntity> listOfInterviewersForAdmin;
	private List<UserEntity> listOfInterviewersForManager;
	private List<UserEntity> listOfManagersForAdmin;
	
	private ApplicationEntity selectedApplication;
	private PositionEntity position;
	private UIPanel panelGroup;
	private PositionEntity selectedPosition;
	private UserEntity selectedManager;
	private InterviewEntity selectedInterviewGuide;
	private UploadedFiles uploadedFiles;
	private ApplicantEntity applicant;
	private ApplicationEntity application;
	private String cvname;
	private String clname;

	@EJB
	private UserData userData;
	@EJB
	private StatefulPosition statefulPosition;
	@EJB
	private InterviewEntityFacade interviewGuideFacade;
	@EJB
	private InterviewFeedbackFacade interviewFeedbackFacade;
	@EJB
	private ManagerFacade managerFacade;
	@EJB
	private InterviewerFacade interviewerFacade;
	@EJB
	private UserEntityFacade userEntityFacade;
	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private PositionFacade positionFacade;
	@EJB
	private SendEmail mail;
	@Inject
	private ActiveSession activePosition;
	@Inject
	private UserCheck activeUser;
	/**
	 * Creates a new instance of PositionViewBean
	 */
	public ManageApplicationWeb() {
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
	 * 
	 * @return
	 */
	public String addPosition() {
		position = activePosition.getActivePosition();
		selectedManager = position.getManager();
		Logger.getLogger(ManageApplicationWeb.class.getName()).log(
				Level.INFO,
				"addPosition() --> Before creating Position = "
						+ position.getTitle() + " and manager ="
						+ selectedManager.getEmail());
		try {
			if (positionFacade.createPosition(selectedManager, position,
					ONEWEEKONMS)) {
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.INFO,
						"addPosition() --> Position = " + position.getTitle()
								+ " has been created successfully!");
				JSFUtil.addSuccessMessage("addPosition() --> Position = "
						+ position.getTitle()
						+ " has been created successfully!");
				// stopShowPanel();
				position = new PositionEntity();
				activePosition.setActivePosition(position);
			} else {
				Logger.getLogger(ManageApplicationWeb.class.getName())
						.log(Level.SEVERE,
								"addPosition() --> Position = "
										+ position.getTitle()
										+ " was unable to be created! Fix the issue and try again.");
				JSFUtil.addErrorMessage("addPosition() --> Position = "
						+ position.getTitle()
						+ " was unable to be created! Please, fix the issue and try again.");
			}
			return "searchpositions.xhtml?faces-redirect=true";
		} catch (ManagerNotIntroducedException
				| PhoneInterviewEntityNotIntroducedException
				| PresentialInterviewEntityNotIntroducedException
				| BeforeDateNotBeforeClosingDate | OpeningDateAfterAtualDate ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}
	}

	/**
	 * Edit PositionEntity
	 * 
	 * @return
	 */
	public String savePosition() {
		try {
			selectedPosition = activePosition.getActivePosition();
			if (positionFacade.editAndSavePosition(selectedPosition)) {
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.INFO,
						"savePosition() --> Position = "
								+ selectedPosition.getTitle()
								+ " has been saved successfully!");
				JSFUtil.addSuccessMessage("savePosition() --> Position = "
						+ selectedPosition.getTitle()
						+ " has been saved successfully!");
				stopShowPanel();
				position = new PositionEntity();
				activePosition.setActivePosition(position);

			} else {
				Logger.getLogger(ManageApplicationWeb.class.getName())
						.log(Level.SEVERE,
								"savePosition() --> Position = "
										+ selectedPosition.getTitle()
										+ " was unable to be saved! Fix the issue and try again.");
				JSFUtil.addErrorMessage("savePosition() --> Position = "
						+ selectedPosition.getTitle()
						+ " was unable to be saved! Please, fix the issue and try again.");
			}
			return "searchpositions?faces-redirect=true";
		} catch (ManagerNotIntroducedException
				| PhoneInterviewEntityNotIntroducedException
				| PresentialInterviewEntityNotIntroducedException
				| BeforeDateNotBeforeClosingDate ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}
	}

	/**
	 * Associates an ApplicationEntity to a PositionEntity
	 * 
	 * @return
	 */
	public String associateApplicationToPosition() {
		try {
			//selectedPosition=position;
			Logger.getLogger(ManageApplicationWeb.class.getName())
					.log(Level.INFO,
							"Inside associateApplicationToPosition() with selectedApplication.getApplicant().getEmail()="
									+ selectedApplication.getApplicant()
											.getEmail()
									+ "selectedPosition.getTitle="
									);
			positionFacade.associateApplicationToPosition(selectedApplication,
					selectedPosition);
			return "nonSpontaneousApplications?faces-redirect=true";
		} catch (PositionNotIntroducedException
				| PositionOfAnApplicantAlreadyIntroducedOnSPonException ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (EJBException ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
			return null;
		}
	}

	/**
	 * Uploads CV
	 * 
	 * @param event
	 */
	public void uploadCV1(FileUploadEvent event) {
		try {
			uploadedFiles.upload(event, "cv");
			this.cvname = event.getFile().getFileName();
			JSFUtil.addSuccessMessage("Success!! " + cvname + " was uploaded.");
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.INFO, "Inside uploadCV1() download file= " + cvname+" ou "+ uploadedFiles.getFinalCvDestination());
		} catch (Exception e) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, "uploadCV1() error=" + e.getMessage());
		}
	}

	/**
	 * Uploads CV
	 * 
	 * @param event
	 */
	public void uploadCV(FileUploadEvent event) {
		try {
			uploadedFiles.upload(event, "cv");
			this.cvname = event.getFile().getFileName();
			JSFUtil.addSuccessMessage("Success!! " + cvname + " was uploaded.");
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.INFO, "Inside uploadCV() download file= " + cvname+" ou "+ uploadedFiles.getFinalCvDestination());
		} catch (Exception e) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, "uploadCV() error=" + e.getMessage());
		}
	}

	/**
	 * Uploads Cover Letter
	 * 
	 * @param event
	 */
	public void uploadMotivationLetter(FileUploadEvent event) {
		try {
			uploadedFiles.upload(event, "cl");
			this.clname = event.getFile().getFileName();
			JSFUtil.addSuccessMessage("Success!! " + clname + " was uploaded.");
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.INFO,
					"Inside uploadMotivationLetter() download file= " + clname+" ou "+ uploadedFiles.getFinalCoverLetterDestination());
		} catch (Exception e) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null,
					"uploadMotivationLetter() error=" + e.getMessage());
		}
	}

	/**
	 * Creates an ApplicationEntity
	 * 
	 * @return
	 */
	public String createManualApplication() {
		try {
			if(application.getSource().equals("OTHER") && !activePosition.getTexttmp().isEmpty()){
				application.setSource(activePosition.getTexttmp());
			}
			applicationFacade.createSpontaneousApplicationOfNewApplicant(
					applicant, application, uploadedFiles.getCvUploadName(),
					uploadedFiles.getClUploadName());
		} catch (InvalidAuthException | EmailAlreadyExistsException
				| NumberOfMobilePhoneDigitsException
				| DoNotUploadCVFileException | DoNotUploadCoverLetterException ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		} catch (
				EJBException
				| EmailAndPasswordNotCorrespondingToLinkedinCredentialsException ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		return "newSpontaneousApplications.xhtml?faces-redirect=true";
	}

	/**
	 * Creates an ApplicationEntity
	 * 
	 * @return
	 */
	public String createApplication() {
		try {
			selectedPosition = activePosition.getActivePosition();
			try {
				Logger.getLogger(ManageApplicationWeb.class.getName())
						.log(Level.INFO,
								"Inside createApplication() and before applicationFacade.createApplicationOfNewApplicant() where selectedPosition.getTitle="
										+ selectedPosition.getTitle());
			} catch (Exception e) {
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.SEVERE, null, e);
			}

			applicationFacade.createApplicationOfNewApplicant(applicant,
					application, uploadedFiles.getCvUploadName(),
					uploadedFiles.getClUploadName(), selectedPosition);
			Logger.getLogger(ManageApplicationWeb.class.getName())
					.log(Level.INFO,
							"Inside createApplication() and after applicationFacade.createApplicationOfNewApplicant() where selectedPosition.getTitle="
									+ selectedPosition.getTitle());
		} catch (InvalidAuthException | EmailAlreadyExistsException
				| NumberOfMobilePhoneDigitsException
				| DoNotUploadCVFileException | DoNotUploadCoverLetterException ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			System.out
					.println("\nInside PositionWebManagem.createApplication() and before returning null\n");
			return null;
		} catch (
				EJBException
				| EmailAndPasswordNotCorrespondingToLinkedinCredentialsException ex) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		System.out
				.println("\nInside PositionWebManagem.createApplication() and before returning to 'nonSpontaneousApplications.xhtml?faces-redirect=true'\n");
		Logger.getLogger(ManageApplicationWeb.class.getName())
				.log(Level.INFO,
						"Inside PositionWebManagem.createApplication() and before returning to 'nonSpontaneousApplications.xhtml?faces-redirect=true'\n");
		return "nonSpontaneousApplications.xhtml?faces-redirect=true";
	}

	/**
	 *
	 * @param otherPosition
	 * @return
	 */
	public String goToApplicationForm(PositionEntity otherPosition) {
		Logger.getLogger(ManageApplicationWeb.class.getName()).log(
				Level.INFO,
				"Inside goToApplicationForm() and otherPosition.title is="
						+ otherPosition.getTitle());
		activePosition.setActivePosition(otherPosition);
		// statefulPosition.setPosition(otherPosition);
		// position = statefulPosition.getPosition();
		position = activePosition.getActivePosition();
		Logger.getLogger(ManageApplicationWeb.class.getName())
				.log(Level.INFO,
						"Inside goToApplicationForm() and after setting position where position().getTitle() is="
								+ position.getTitle());
		// Logger.getLogger(ManageApplicationWeb.class.getName())
		// .log(Level.INFO,
		// "Inside goToApplicationForm() and after setting position where statefulPosition.getPosition().getTitle() is="
		// + statefulPosition.getPosition().getTitle());
		return "newApplicationToPosition.xhtml?faces-redirect=true";
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
		return position.getStatus().equals("OPEN")
				&& position.getVacancies() > 0;
	}

	/**
	 *
	 */
	public void openNewPosition() {
		Logger.getLogger(ManageApplicationWeb.class.getName())
				.log(Level.INFO,
						"Inside openNewPosition() and before setting activePosition=new position ");
		this.activePosition.setActivePosition(position);
		Logger.getLogger(ManageApplicationWeb.class.getName())
				.log(Level.INFO,
						"Inside openNewPosition() and after setting activePosition=new position ");

	}

	/**
	 *
	 */
	public void showPanel(PositionEntity position) {
		Logger.getLogger(ManageApplicationWeb.class.getName()).log(
				Level.INFO,
				"Inside showPanel() and position.title is="
						+ position.getTitle());
		this.selectedPosition = position;
		this.activePosition.setActivePosition(position);
		Logger.getLogger(ManageApplicationWeb.class.getName()).log(
				Level.INFO,
				"Inside showPanel() and selectedPosition.title is="
						+ selectedPosition.getTitle());
		panelGroup.setRendered(true);
	}

	public void stopShowPanel() {
		panelGroup.setRendered(false);
	}
	
	/**
	 *
	 * @return  true if is an user that try to log in is an admin
	 */
	public boolean isAdmin() {
		try {
			return userData.getLoggedUser() instanceof AdminEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex.getMessage());
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;

	}

	/**
	 *
	 * @return  true if is an user that try to log in is a manager
	 */
	public boolean isManager() {
		try {
			return userData.getLoggedUser() instanceof ManagerEntity;
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex.getMessage());
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return false;
	}
	
	/**
	 *
	 * @return  Manager's email if is an user that try to log in is a manager
	 */
	public String ManagerEmail() {
		String out="";
		try {
			out=userData.getLoggedUser().getEmail();
		} catch (UserNotFoundException | UserGuideException | NoResultException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
		return out;
	}

	/**
	 *
	 * @param position
	 * @return true if it is possible to edit PositionEntity
	 */
	public boolean canEditStatus(ApplicationEntity application) {
		boolean test = false;
		if(isAdmin()) {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.INFO,
					"Inside canEditStatus() and returning 'true' as Admin user ");
			test= true;
		}
		else if(isManager()){	
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.INFO,"Inside canEditStatus() and its a active user as a manager");
			try {
				 if(application.getPosition().getManager().getEmail().equals(ManagerEmail())){

				 Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.INFO,
						"Inside canEditStatus() and returning 'true' as Manager of position associated to this application ");
				 test= true;
				}else{
					Logger.getLogger(ManageApplicationWeb.class.getName()).log(
							Level.INFO,
							"Inside canEditStatus() and returning 'false' as its a Manager user but not not managing the position associated to the application");
					test= false;					
				}
			}catch(Exception e){
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.SEVERE,null,e.getMessage());
				test= false;
			}
			
		} else{			
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.INFO,
					"Inside canEditStatus() and returning 'false' as it isn't either an Admin or Manager user.");
			test= false;
		}
		return test;
		//return positionFacade.associateToApplication(position);
	}

	// ///////////////////Getters && Setters////////////////////

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

	public UserEntity getSelectedManager() {
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
		position = activePosition.getActivePosition();
		position.setManager(selectedManager);
		activePosition.setActivePosition(position);
	}

	public void setTheSelectedPhoneGuideInterview() {
		position = activePosition.getActivePosition();
		position.setPhoneInterviewEntity(selectedInterviewGuide);
		activePosition.setActivePosition(position);
	}

	public void setTheSelectedPresentialGuideInterview() {
		position = activePosition.getActivePosition();
		position.setPresencialInterviewEntity(selectedInterviewGuide);
		activePosition.setActivePosition(position);
	}

	public void saveTemporaryDataPosition() {
		activePosition.setActivePosition(position);
	}

	public List<InterviewEntity> getLstInterviews() {
		return interviewGuideFacade.findAll();
	}

	public InterviewEntityFacade getInterviewGuideFacade() {
		return interviewGuideFacade;
	}

	public void setInterviewGuideFacade(
			InterviewEntityFacade interviewGuideFacade) {
		this.interviewGuideFacade = interviewGuideFacade;
	}

	public List<InterviewEntity> getLstPhoneInterviewsInUse() {
		return interviewGuideFacade.lstPhoneInterviewsInUseWithQuestions();
	}

	public void setLstPhoneInterviewsInUse(
			List<InterviewEntity> lstPhoneInterviewsInUse) {
		this.lstPhoneInterviewsInUse = lstPhoneInterviewsInUse;
	}

	public List<InterviewEntity> getLstPresentialInterviewsInUse() {
		return interviewGuideFacade.lstPresentialInterviewsInUseWithQuestions();
	}

	public void setLstPresentialInterviewsInUse(
			List<InterviewEntity> lstPresentialInterviewsInUse) {
		this.lstPresentialInterviewsInUse = lstPresentialInterviewsInUse;
	}

	public List<PositionEntity> getLstPositionsBeforeAtualDate() {
		return positionFacade.lstPositionAtualDateBeforeClosingDate(new Date());
	}

	public void setLstPositionsBeforeAtualDate(
			List<PositionEntity> lstPositionsBeforeAtualDate) {
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

	public ActiveSession getActivePosition() {
		return activePosition;
	}

	public void setActivePosition(ActiveSession activePosition) {
		this.activePosition = activePosition;
	}

	public List<PositionEntity> getLstPositionsofAManager() throws PositionsNotFoundToThisUserException {
		try {
			try {
				Logger.getLogger(PositionWebManagem.class.getName()).log(
						Level.INFO, "Inside getLstPositionsofAManager() and before positionFacade.lstPositionsOfManager() with email="+activeUser.getName());
				lstPositionsofAManager = positionFacade
						.lstPositionsOfManager((UserEntity) userData
								.getLoggedUser());
			} catch (UserNotFoundException
					| pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(PositionWebManagem.class.getName()).log(
						Level.SEVERE, null, e.getMessage());
			}
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			Logger.getLogger(PositionWebManagem.class.getName()).log(
					Level.SEVERE, null, ex.getMessage());
		}
		return lstPositionsofAManager;
	}

	public void setLstPositionsofAManager(
			List<PositionEntity> lstPositionsofAManager) {
		this.lstPositionsofAManager = lstPositionsofAManager;
	}
	
	public List<InterviewFeedbackEntity> getLstInterviewsOfInterviewer() throws InterviewsNotFoundToThisUserException {
	  try {
		try {
			Logger.getLogger(PositionWebManagem.class.getName()).log(
					Level.INFO, "Inside getLstPositionsofAManager() and before positionFacade.lstPositionsOfManager() with email="+activeUser.getName());
			lstInterviewsOfInterviewer = interviewFeedbackFacade
					.lstInterviewsWithThatInterviewer((InterviewerEntity) userData
							.getLoggedUser());
		} catch (NoResultException
				| pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException
				| pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, e);
			JSFUtil.addErrorMessage(e.getMessage());
		}
	  } catch (Exception ex) {
			// TODO Auto-generated catch block
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex.getMessage());
		}
		return lstInterviewsOfInterviewer;
	}
	
	public List<UserEntity> collectListOfInterviewers(){
		List<UserEntity> listToUse=new ArrayList<>();
		List<InterviewerEntity> tmplistToUse=new ArrayList<>();
		UserEntity manager = null;
		
		if(isAdmin()){
			listToUse=userEntityFacade.lstUserEntitiesAvailable(Role.INTERVIEWER);

		} else if(isManager()){
			try {
				manager = userData.getLoggedUser();
			} catch (NoResultException e) {
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.SEVERE, null, "collectListOfInterviewers() - No result to get a manager from userData.getLoggedUser()"+e.getMessage());
			} catch (UserNotFoundException e) {
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.SEVERE, null, "collectListOfInterviewers() - No user has been found "+e.getMessage());
			} catch (UserGuideException e) {
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.SEVERE, null, "collectListOfInterviewers() - No UserEntity found "+e.getMessage());
			}
			tmplistToUse = interviewerFacade.findAll();
			listToUse.addAll(tmplistToUse);
			listToUse.add(manager);
		} else {
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, "collectListOfInterviewers() - User is an Interviewer and was supposed to not access to this function. Check it!");
		} 
		return listToUse;
	}
	
	/*public List<PositionEntity> getLstPositionsofAManager() throws PositionsNotFoundToThisUserException {
		try {
			try {
				
				lstPositionsofAManager = positionFacade
						.lstPositionsOfManager((UserEntity) userData
								.getLoggedUser());
//				lstPositionsofAManager = positionFacade
//						.lstPositionsOfManager((ManagerEntity) userData
//								.getLoggedUser());
			} catch (UserNotFoundException
					| pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(ManageApplicationWeb.class.getName()).log(
						Level.SEVERE, null, e);
			}
		} catch (NoResultException ex) {
			// TODO Auto-generated catch block
			Logger.getLogger(ManageApplicationWeb.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		return lstPositionsofAManager;
	}

	public void setLstPositionsofAManager(
			List<PositionEntity> lstPositionsofAManager) {
		this.lstPositionsofAManager = lstPositionsofAManager;
	}*/

	public String getCvname() {
		return cvname;
	}

	public void setCvname(String cvname) {
		this.cvname = cvname;
	}

	public String getClname() {
		return clname;
	}

	public void setClname(String clname) {
		this.clname = clname;
	}

	public UserCheck getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(UserCheck activeUser) {
		this.activeUser = activeUser;
	}

	public List<UserEntity> getListOfInterviewersForAdmin() {
		
		return listOfInterviewersForAdmin;
	}

	public void setListOfInterviewersForAdmin(
			List<UserEntity> listOfInterviewersForAdmin) {
		this.listOfInterviewersForAdmin = listOfInterviewersForAdmin;
	}

	public List<UserEntity> getListOfInterviewersForManager() {
		return listOfInterviewersForManager;
	}

	public void setListOfInterviewersForManager(
			List<UserEntity> listOfInterviewersForManager) {
		this.listOfInterviewersForManager = listOfInterviewersForManager;
	}

	public List<UserEntity> getListOfManagersForAdmin() {
		return listOfManagersForAdmin;
	}

	public void setListOfManagersForAdmin(List<UserEntity> listOfManagersForAdmin) {
		this.listOfManagersForAdmin = listOfManagersForAdmin;
	}

	public void setLstInterviewsOfInterviewer(
			List<InterviewFeedbackEntity> lstInterviewsOfInterviewer) {
		this.lstInterviewsOfInterviewer = lstInterviewsOfInterviewer;
	}
	

}
