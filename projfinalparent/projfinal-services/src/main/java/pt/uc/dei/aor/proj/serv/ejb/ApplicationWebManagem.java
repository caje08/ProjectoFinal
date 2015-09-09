/*
 */
package pt.uc.dei.aor.proj.serv.ejb;

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
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;

import pt.uc.dei.aor.proj.db.entities.AnswerEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewFeedbackEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.tools.Outcome;
import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplication;
import pt.uc.dei.aor.proj.serv.exceptions.FirstInterviewAfterAtualDateException;
import pt.uc.dei.aor.proj.serv.exceptions.InterviewerSameDateException;
import pt.uc.dei.aor.proj.serv.exceptions.MustIntroduceInterviewerException;
import pt.uc.dei.aor.proj.serv.exceptions.SecondInterviewAfterFirstInterviewException;
import pt.uc.dei.aor.proj.serv.exceptions.SubmitFeedbackBeforeInterviewDateException;
import pt.uc.dei.aor.proj.serv.facade.AnswerFacade;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewEntityFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewFeedbackFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewerFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.StatefulApplication;
import pt.uc.dei.aor.proj.serv.tools.UploadedFiles;
import pt.uc.dei.aor.proj.serv.tools.UserData;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class ApplicationWebManagem implements Serializable {

	private List<ApplicationEntity> lstSpontaneousApplication;
	private List<ApplicationEntity> lstNonSpontaneousApplication;
	private List<InterviewFeedbackEntity> lstInterviews;
	private List<InterviewFeedbackEntity> lstInterviewsOfInterviewer;

	private List<InterviewerEntity> lstInterviewers;
	private List<AnswerEntity> lstAnswers;
	private List<AnswerEntity> lstAnswersWithFeedback;
	private RejectionMotive[] lstRejectionMotives;
	private Outcome[] lstOutcomes;
	private StatusApplication[] lstStatusApplication;
	private List<ApplicationEntity> nonSpontaneousOfManager;

	private UIPanel panelGroup;
	private UIPanel panelInterviews;
	private ApplicationEntity application;
	private ApplicationEntity selectedApplication;
	private boolean spontaneous;
	private String cvPath;
	private String sourceLetterPath;
	private InterviewFeedbackEntity selectedInterview;
	private InterviewerEntity selectedInterviewer;
	private Date interviewDate;
	private UploadedFiles uploadedFiles;

	@EJB
	private AnswerFacade answerFacade;
	@EJB
	private InterviewEntityFacade interviewGuideFacade;
	@EJB
	private InterviewFeedbackFacade interviewFeedbackFacade;
	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private StatefulApplication statefulApplication;
	@EJB
	private InterviewerFacade interviewerFacade;
	@EJB
	private UserData userData;

	/**
	 *
	 */
	public ApplicationWebManagem() {
	}

	/**
	 * Init application and Upload Files
	 */
	@PostConstruct
	public void init() {
		this.application = new ApplicationEntity();
		this.uploadedFiles = new UploadedFiles();
	}

	/**
	 *
	 * @param a
	 * @return to page applicationDetails
	 */
	public String goTo(ApplicationEntity a) {
		selectedApplication = a;
		statefulApplication.setApplication(selectedApplication);
		return "applicationDetails.xhtml?applicantid=" + selectedApplication.getApplicant().getUserId() + "&applicationid" + selectedApplication.getApplicationId();
	}

	/**
	 *
	 * @param application
	 */
	public void showPanel(ApplicationEntity application) {
		selectedApplication = application;
		//list of interviews of an application
		lstInterviews = interviewFeedbackFacade.listInterviews(statefulApplication.getApplication().getApplicationId());
		panelGroup.setRendered(true);
		panelInterviews.setRendered(false);
	}

	/**
	 * If number of interviews is 0, create a new Interview, with a phone
	 * interview guide. Else, create new interview with a presential Interview
	 * Guide
	 */
	public void createNewInterview() {
		if (interviewFeedbackFacade.numberOfInterviews(statefulApplication.getApplication().getApplicationId()) == 0) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.INFO, "Inside createNewInterview() with numberOfInterviews=0");
			try {
				interviewFeedbackFacade.createInterview(interviewDate, selectedInterviewer, statefulApplication.getApplication().getPosition().getPhoneInterviewEntity(), statefulApplication.getApplication(), uploadedFiles.getCVDESTINATION());
			} catch (FirstInterviewAfterAtualDateException | InterviewerSameDateException | SecondInterviewAfterFirstInterviewException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage(ex.getMessage());
			} catch (MessagingException | EJBException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage("Error sending email");
			} catch (MustIntroduceInterviewerException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			}

		} else {
			try {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.INFO, "Inside createNewInterview() with numberOfInterviews!=0");
				interviewFeedbackFacade.createInterview(interviewDate, selectedInterviewer, (statefulApplication.getApplication().getPosition()).getPresencialInterviewEntity(), statefulApplication.getApplication(), uploadedFiles.getCVDESTINATION());
			} catch (FirstInterviewAfterAtualDateException | InterviewerSameDateException | SecondInterviewAfterFirstInterviewException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage(ex.getMessage());
			} catch (MessagingException | EJBException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage("Error sending email");
			} catch (MustIntroduceInterviewerException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage(cvPath);
			}
		}
	}

	/**
	 *
	 * @return true if interview date of selectedInterview is after of current
	 * date
	 */
	public boolean adminCanSubmitFeedback() {
		return selectedInterview.getInterviewDate().after(new Date());
	}

	/**
	 *
	 * @param interviewFeedback
	 * @return true if interview date is after current date
	 */
	public boolean possibleToSubmitFeedback(InterviewFeedbackEntity interviewFeedback) {
		return interviewFeedback.getInterviewDate().after(new Date());
	}

	/**
	 * Create Interview
	 */
	public void createNewInterviewByInterviewer() {
		if (interviewFeedbackFacade.numberOfInterviews(statefulApplication.getApplication().getApplicationId()) == 0) {
			try {
				try {
					interviewFeedbackFacade.createInterview(interviewDate, (InterviewerEntity) userData.getLoggedUser(), statefulApplication.getApplication().getPosition().getPhoneInterviewEntity(), statefulApplication.getApplication(), uploadedFiles.getCVDESTINATION());
				} catch (NoResultException e) {
					// TODO Auto-generated catch block
					Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
					JSFUtil.addErrorMessage(e.getMessage());
				} catch (pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException e) {
					// TODO Auto-generated catch block
					Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
					JSFUtil.addErrorMessage(e.getMessage());
				} catch (pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
					// TODO Auto-generated catch block
					Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
					JSFUtil.addErrorMessage(e.getMessage());
				}
			} catch (FirstInterviewAfterAtualDateException | InterviewerSameDateException | SecondInterviewAfterFirstInterviewException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage(ex.getMessage());
			} catch (MessagingException | EJBException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage("Error sending email");
			} catch (MustIntroduceInterviewerException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage(ex.getMessage());
			}
		} else {
			try {
				try {
					interviewFeedbackFacade.createInterview(interviewDate, (InterviewerEntity) userData.getLoggedUser(), statefulApplication.getApplication().getPosition().getPresencialInterviewEntity(), statefulApplication.getApplication(), uploadedFiles.getCVDESTINATION());
				} catch (NoResultException e) {
					// TODO Auto-generated catch block
					Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
					JSFUtil.addErrorMessage(e.getMessage());
				} catch (pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException e) {
					// TODO Auto-generated catch block
					Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
					JSFUtil.addErrorMessage(e.getMessage());
				} catch (pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
					// TODO Auto-generated catch block
					Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
					JSFUtil.addErrorMessage(e.getMessage());
				}
			} catch (FirstInterviewAfterAtualDateException | InterviewerSameDateException | SecondInterviewAfterFirstInterviewException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage(ex.getMessage());
			} catch (MessagingException | EJBException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage("Error sending email");
			} catch (MustIntroduceInterviewerException ex) {
				Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
				JSFUtil.addErrorMessage(ex.getMessage());
			}
		}
	}

	/**
	 * Edit application
	 */
	public void editApplication() {
		statefulApplication.getApplication().setStatus(selectedApplication.getStatus());
		statefulApplication.getApplication().setMotive(selectedApplication.getMotive());
		applicationFacade.edit(selectedApplication);
	}

	/**
	 *
	 * @param application
	 * @return true if status of an application is SUBMITTED
	 */
	public boolean alreadyInInterviewProcess(ApplicationEntity application) {
		return application.getStatus().equals(StatusApplication.SUBMITTED);
	}

	/**
	 *
	 * @param application
	 * @return true if application status is REJECTED and interviews feedbacks
	 * of an application is empty
	 */
	public boolean isRejected(ApplicationEntity application) {
		return application.getStatus().equals(StatusApplication.REJECTED) && application.getInterviewFeedbackEntitys().isEmpty();
	}

	/**
	 * Set ApplicationEntity status to rejected and rejection motive will be CV
	 */
	public void setRejectStatusBeforePhoneInterview() {
		selectedApplication.setStatus(StatusApplication.REJECTED);
		selectedApplication.setMotive(RejectionMotive.CV);
		applicationFacade.edit(selectedApplication);
	}

	/**
	 *
	 * @param application
	 * @return true if an application is in negotiation process
	 */
	public boolean isOfferProcess(ApplicationEntity application) {
		if (application.getInterviewFeedbackEntitys().size() == 2) {
			return application.getStatus().equals(StatusApplication.NEGOTIATION);
		} else {
			return false;
		}
	}

	/**
	 *
	 * @return true if is possible to add more interviews
	 */
	public boolean isPossibleToAdd() {
		if (interviewFeedbackFacade.numberOfInterviews(selectedApplication.getApplicationId()) == 0) {
			return true;
		} else {
			return interviewFeedbackFacade.numberOfInterviews(selectedApplication.getApplicationId()) < 2 && interviewFeedbackFacade.knowIfAPhoneInterviewFeedbackHasAcceptedFeedback(selectedApplication);
		}

	}

	/**
	 *
	 * @param interviewFeedback
	 * @return true if feedback was already given
	 */
	public boolean feedbackGiven(InterviewFeedbackEntity interviewFeedback) {
		return interviewFeedbackFacade.withFeedbackGiven(interviewFeedback);
	}

	/**
	 *
	 * @param interviewFeedback
	 * @return true if is possible to submit feedback
	 */
	public boolean canSubmitFeedback(InterviewFeedbackEntity interviewFeedback) {
		//if the select interview has the same interviewer as the logged user and don ´t have any feedback the interviewer logged user can submit feedback
		if(interviewFeedback!=null){
		try {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.INFO, "Inside canSubmitFeedback() interviewFeedback="+interviewFeedback.getInterviewType());
			return interviewFeedbackFacade.knowIfIsTheCurrentInterviewer((InterviewerEntity) userData.getLoggedUser()) && !feedbackGiven(interviewFeedback);
		} catch (NoResultException
				| pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException
				| pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
		} else {
			return false;
		}

	}

	/**
	 * Submit feedback of a selected interview
	 */
	public void addFeedbak() {
		try {
			answerFacade.submitFeedbackEntity(selectedInterview, lstAnswers);
		} catch (SubmitFeedbackBeforeInterviewDateException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
	}

	/**
	 * List of ansers of an interview
	 *
	 * @param interview
	 */
	public void appointAnswers(InterviewFeedbackEntity interview) {
		selectedInterview = interview;
		lstAnswers = answerFacade.setAnswersOfAnInterview(selectedInterview);
	}

	/////////////////////Getters && Setters////////////////////
	public List<InterviewFeedbackEntity> getLstInterviewsOfInterviewer() {
		try {
			lstInterviewsOfInterviewer=interviewFeedbackFacade.lstInterviewsWithThatInterviewer((InterviewerEntity) userData.getLoggedUser());
		} catch (NoResultException
				| pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException
				| pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
			JSFUtil.addErrorMessage(e.getMessage());
		}
		return lstInterviewsOfInterviewer;
	}

	public void setLstInterviewsOfInterviewer(List<InterviewFeedbackEntity> lstInterviewsOfInterviewer) {
		this.lstInterviewsOfInterviewer = lstInterviewsOfInterviewer;
	}

	public List<AnswerEntity> getLstAnswers() {
		return lstAnswers;
	}

	public void setLstAnswers(List<AnswerEntity> lstAnswers) {
		this.lstAnswers = lstAnswers;
	}

	public Outcome[] getLstOutcomes() {
		return lstOutcomes = Outcome.values();
	}

	public void setLstOutcomes(Outcome[] lstOutcomes) {
		this.lstOutcomes = lstOutcomes;
	}

	public RejectionMotive[] getLstRejectionMotives() {
		return lstRejectionMotives = RejectionMotive.values();
	}

	public void setLstRejectionMotives(RejectionMotive[] lstRejectionMotives) {
		this.lstRejectionMotives = lstRejectionMotives;
	}

	public StatusApplication[] getLstStatusApplication() {
		return lstStatusApplication = StatusApplication.values();
	}

	public void setLstStatusApplication(StatusApplication[] lstStatusApplication) {
		this.lstStatusApplication = lstStatusApplication;
	}

	public List<ApplicationEntity> getLstSpontaneousApplication() {
		return applicationFacade.lstSpontaneousApplications();
	}

	public List<InterviewFeedbackEntity> getLstInterviews() {
		return interviewFeedbackFacade.listInterviews(statefulApplication.getApplication().getApplicationId());
	}

	public void setLstInterviews(List<InterviewFeedbackEntity> lstInterviews) {
		this.lstInterviews = lstInterviews;
	}

	public void setLstSpontaneousApplication(List<ApplicationEntity> lstSpontaneousApplication) {
		this.lstSpontaneousApplication = lstSpontaneousApplication;
	}

	public List<ApplicationEntity> getLstNonSpontaneousApplication() {
		return applicationFacade.lstNonSpontaneousApplications();
	}

	public void setLstNonSpontaneousApplication(List<ApplicationEntity> lstNonSpontaneousApplication) {
		this.lstNonSpontaneousApplication = lstNonSpontaneousApplication;
	}

	public UIPanel getPanelGroup() {
		return panelGroup;
	}

	public void setPanelGroup(UIPanel panelGroup) {
		this.panelGroup = panelGroup;
	}

	public UIPanel getPanelInterviews() {
		return panelInterviews;
	}

	public void setPanelInterviews(UIPanel panelInterviews) {
		this.panelInterviews = panelInterviews;
	}

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
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
		if (selectedApplication != null) {
			statefulApplication.setApplication(selectedApplication);
		}
		this.selectedApplication = selectedApplication;
	}

	public boolean isSpontaneous() {
		return spontaneous;
	}

	public void setSpontaneous(boolean spontaneous) {
		this.spontaneous = spontaneous;
	}

	public String getCvPath() {
		return cvPath;
	}

	public void setCvPath(String cvPath) {
		this.cvPath = cvPath;
	}

	public String getSourceLetterPath() {
		return sourceLetterPath;
	}

	public void setSourceLetterPath(String sourceLetterPath) {
		this.sourceLetterPath = sourceLetterPath;
	}

	public StatefulApplication getStatefulApplication() {
		return statefulApplication;
	}

	public void setStatefulApplication(StatefulApplication statefulApplication) {
		this.statefulApplication = statefulApplication;
	}

	public InterviewFeedbackFacade getInterviewFeedbackFacade() {
		return interviewFeedbackFacade;
	}

	public void setInterviewFeedbackFacade(InterviewFeedbackFacade interviewFeedbackFacade) {
		this.interviewFeedbackFacade = interviewFeedbackFacade;
	}

	public InterviewFeedbackEntity getSelectedInterview() {
		return selectedInterview;
	}

	public void setSelectedInterview(InterviewFeedbackEntity selectedInterview) {
		this.selectedInterview = selectedInterview;
	}

	public List<InterviewerEntity> getLstInterviewers() {
		return interviewerFacade.findAll();
	}

	public void setLstInterviewers(List<InterviewerEntity> lstInterviewers) {
		this.lstInterviewers = lstInterviewers;
	}

	public InterviewerFacade getInterviewerFacade() {
		return interviewerFacade;
	}

	public void setInterviewerFacade(InterviewerFacade interviewerFacade) {
		this.interviewerFacade = interviewerFacade;
	}

	public InterviewerEntity getSelectedInterviewer() {
		return selectedInterviewer;
	}

	public void setSelectedInterviewer(InterviewerEntity selectedInterviewer) {
		this.selectedInterviewer = selectedInterviewer;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public AnswerFacade getAnswerFacade() {
		return answerFacade;
	}

	public void setAnswerFacade(AnswerFacade answerFacade) {
		this.answerFacade = answerFacade;
	}

	public List<AnswerEntity> getLstAnswersWithFeedback() {
		if (selectedInterview != null) {
			return (List<AnswerEntity>) selectedInterview.getAnswer();
		} else {
			return new ArrayList<>();
		}
	}

	public void setLstAnswersWithFeedback(List<AnswerEntity> lstAnswersWithFeedback) {
		this.lstAnswersWithFeedback = lstAnswersWithFeedback;
	}

	public InterviewEntityFacade getInterviewEntityFacade() {
		return interviewGuideFacade;
	}

	public void setInterviewEntityFacade(InterviewEntityFacade interviewGuideFacade) {
		this.interviewGuideFacade = interviewGuideFacade;
	}

	public UploadedFiles getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(UploadedFiles uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<ApplicationEntity> getNonSpontaneousOfManager() {
		try {
			nonSpontaneousOfManager = applicationFacade.lstNonSpontaneousApplicationsOfManager((ManagerEntity) userData.getLoggedUser());
		} catch (NoResultException
				| pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException
				| pt.uc.dei.aor.proj.db.exceptions.UserGuideException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, e);
			JSFUtil.addErrorMessage(e.getMessage());
		}
		return nonSpontaneousOfManager;
	}

	public void setNonSpontaneousOfManager(List<ApplicationEntity> nonSpontaneousOfManager) {
		this.nonSpontaneousOfManager = nonSpontaneousOfManager;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

}
