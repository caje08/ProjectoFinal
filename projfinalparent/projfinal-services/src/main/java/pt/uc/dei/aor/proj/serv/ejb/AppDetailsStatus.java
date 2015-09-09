package pt.uc.dei.aor.proj.serv.ejb;

/*
 */
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewFeedbackEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.exceptions.UserGuideException;
import pt.uc.dei.aor.proj.db.exceptions.UserNotFoundException;
import pt.uc.dei.aor.proj.serv.facade.ApplicantFacade;
import pt.uc.dei.aor.proj.serv.facade.ApplicationFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewFeedbackFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.MaintainSession;
import pt.uc.dei.aor.proj.serv.tools.UserData;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class AppDetailsStatus implements Serializable {

	private List<ApplicationEntity> lstApplicationsOfAnApplicant;
	private List<InterviewFeedbackEntity> lstInterviewsOfInterviewerOfApplication;

	private String cvPath;
	private String coverLetterPath;
	private String imgPath;
	private ApplicantEntity applicant;
	private ApplicationEntity application;

	@EJB
	private UserData userData;
	@EJB
	private ApplicantFacade applicantFacade;
	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private InterviewFeedbackFacade interviewFeedbackFacade;

	public AppDetailsStatus() {
	}

	/**
	 *
	 */
	 @PostConstruct
	 public void init() {

		 Map<String, String> mapList = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		 if (mapList.containsKey("applicantid") && mapList.containsKey("applicationid")) {
			 //parse to get Long applicant id
			 Long aId = Long.parseLong(mapList.get("applicantid"));
			 //parse to get Long application id
			 Long b = Long.parseLong(mapList.get("applicationid"));
			 begin(aId, b);
			 //Naintain session of applicantid and application id
			 MaintainSession.setSessionMapValue("applicantid", aId);
			 MaintainSession.setSessionMapValue("applicationid", b);
		 } else if (MaintainSession.isKeyInSessionMap("applicantid") && MaintainSession.isKeyInSessionMap("applicationid")) {
			 begin((Long) MaintainSession.getSessionMapValue("applicantid"), (Long) MaintainSession.getSessionMapValue("applicationid"));
		 }

	 }

	 /**
	  *
	  * @param aId
	  * @param b
	  */
	 public void begin(Long aId, Long b) {
		 //find applicant by id
		 ApplicantEntity a = applicantFacade.find(aId);
		 //find application by id
		 ApplicationEntity app = applicationFacade.find(b);
		 if (a != null && app != null && app.getApplicant().equals(a)) {
			 applicant = a;
			 application = app;
			 //set cvPath
			 this.cvPath = "/CV/" + application.getCv();
			 //set Cover Letter Path
			 this.coverLetterPath = "/CL/" + application.getCoverLetter();
		 }
	 }

	 /////////////////////Getters && Setters////////////////////
	 public List<InterviewFeedbackEntity> getLstInterviewsOfInterviewerOfApplication() {
		 try {
			 lstInterviewsOfInterviewerOfApplication = interviewFeedbackFacade.lstInterviewsWithInterviewerOfAnApplication((InterviewerEntity) userData.getLoggedUser(), application);
		 } catch (UserNotFoundException | UserGuideException ex) {
			 Logger.getLogger(ApplicationWebManagem.class.getName()).log(Level.SEVERE, null, ex);
			 JSFUtil.addErrorMessage(ex.getMessage());
		 }
		 return lstInterviewsOfInterviewerOfApplication;
	 }

	 public void setLstInterviewsOfInterviewerOfApplication(List<InterviewFeedbackEntity> lstInterviewsOfInterviewerOfApplication) {
		 this.lstInterviewsOfInterviewerOfApplication = lstInterviewsOfInterviewerOfApplication;
	 }

	 /**
	  *
	  * @return List of Applications of an applicant
	  */
	 public List<ApplicationEntity> getLstApplicationsOfAnApplicant() {
		 lstApplicationsOfAnApplicant = applicationFacade.getUserApplications(applicant);
		 return lstApplicationsOfAnApplicant;
	 }

	 public void setLstApplicationsOfAnApplicant(List<ApplicationEntity> lstApplicationsOfAnApplicant) {
		 this.lstApplicationsOfAnApplicant = lstApplicationsOfAnApplicant;
	 }

	 public String getCvPath() {
		 return cvPath;
	 }

	 public void setCvPath(String cvPath) {
		 this.cvPath = cvPath;
	 }

	 public String getCoverLetterPath() {
		 return coverLetterPath;
	 }

	 public void setCoverLetterPath(String coverLetterPath) {
		 this.coverLetterPath = coverLetterPath;
	 }

	 public String getImgPath() {
		 return imgPath;
	 }

	 public void setImgPath(String imgPath) {
		 this.imgPath = imgPath;
	 }

	 public ApplicantEntity getApplicant() {
		 return applicant;
	 }

	 public void setApplicant(ApplicantEntity applicant) {
		 this.applicant = applicant;
	 }

	 public ApplicantFacade getApplicantFacade() {
		 return applicantFacade;
	 }

	 public void setApplicantFacade(ApplicantFacade applicantFacade) {
		 this.applicantFacade = applicantFacade;
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

}