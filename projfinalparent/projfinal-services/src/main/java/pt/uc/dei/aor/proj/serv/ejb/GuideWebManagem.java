/*
 */
package pt.uc.dei.aor.proj.serv.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.ReorderEvent;

import pt.uc.dei.aor.proj.db.entities.InterviewEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewQuestionEntity;
import pt.uc.dei.aor.proj.db.tools.AnswerType;
import pt.uc.dei.aor.proj.db.tools.InterviewType;
import pt.uc.dei.aor.proj.serv.exceptions.InterviewEntityNameException;
import pt.uc.dei.aor.proj.serv.exceptions.InterviewQuestionNameAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.MustIntroduceInterviewQuestionException;
import pt.uc.dei.aor.proj.serv.facade.InterviewEntityFacade;
import pt.uc.dei.aor.proj.serv.facade.InterviewQuestionFacade;
import pt.uc.dei.aor.proj.serv.facade.PositionFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;

/**
 *
 * @author
 */
@Named
@ViewScoped
public class GuideWebManagem implements Serializable {

	private List<InterviewEntity> lstInterviews;
	private List<InterviewEntity> lstInterviewsInUse;
	private List<InterviewQuestionEntity> lstInterviewQuestionEntity;

	private InterviewEntity selectedInterviewEntity;
	private String guideName;
	private InterviewType guideType;
	private String questionName;
	private UIPanel panelGroup;
	private UIPanel panelInterviewQuestions;
	private InterviewQuestionEntity selectedInterviewQuestionEntity;

	@EJB
	private InterviewEntityFacade interviewEntityFacade;
	@EJB
	private InterviewQuestionFacade interviewQuestionFacade;
	@EJB
	private SendEmail mail;
	@EJB
	private PositionFacade positionFacade;
	private AnswerType answerType;

	public GuideWebManagem() {
	}

	/**
	 *
	 */
	@PostConstruct
	public void init() {
		this.lstInterviewQuestionEntity = new ArrayList<>();
	}

	/**
	 * Create Guide Interview
	 */
	public void createGuide() {
		try {
			interviewEntityFacade.createGuide(guideName, guideType);
		} catch (InterviewEntityNameException ex) {
			Logger.getLogger(GuideWebManagem.class.getName()).log(Level.SEVERE,
					null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
	}

	/**
	 * Edit Use of Interview Guide
	 */
	public void editGuide() {
		if (selectedInterviewEntity.getInUse() == true) {
			selectedInterviewEntity.setInUse(Boolean.FALSE);
		} else {
			selectedInterviewEntity.setInUse(Boolean.TRUE);
		}
		interviewEntityFacade.edit(selectedInterviewEntity);
	}

	/**
	 * Remove a select Interview Guide
	 */
	public void removeGuide() {
		interviewEntityFacade.remove(selectedInterviewEntity);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Interview Guide removed with sucess"));

	}

	/**
	 *
	 * @param interviewGuide
	 * @return true if is possible to remove a Interview Guide
	 */
	public boolean isPossibleToRemoveGuide(InterviewEntity interviewGuide) {
		return positionFacade.getInterviewEntity(
				interviewGuide.getInterviewId()).isEmpty()
				&& positionFacade.getPresentialInterviewEntity(
						interviewGuide.getInterviewId()).isEmpty();

	}

	/**
	 * Reorder number of interview Questions in an interview guide
	 *
	 * @param event
	 */
	public void onRowReorder(ReorderEvent event) {

		for (int i = 0; i < lstInterviewQuestionEntity.size(); i++) {
			lstInterviewQuestionEntity.get(i).setQuestionNumber(i + 1);
			interviewQuestionFacade.edit(lstInterviewQuestionEntity.get(i));
		}
		lstInterviewQuestionEntity = interviewQuestionFacade
				.getInterviewQuestionEntityByInterview(selectedInterviewEntity
						.getInterviewId());

		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Row Moved with succes", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 *
	 * @return true if is possible to add more questions of a selected interview
	 *         guide
	 */
	public boolean couldAddQuestions() {
		return positionFacade.getInterviewEntity(
				selectedInterviewEntity.getInterviewId()).isEmpty();

	}

	/**
	 * Create Interview Questions to a selected Interview Guide
	 */
	public void createQuestionsForGuide() {
		try {
			InterviewQuestionEntity interviewQuestion = interviewQuestionFacade
					.createQuestionForGuide(questionName,
							selectedInterviewEntity, answerType);
			lstInterviewQuestionEntity = interviewQuestionFacade
					.getInterviewQuestionEntityByInterview(selectedInterviewEntity
							.getInterviewId());
			interviewEntityFacade.mergeQuestionsInGuide(
					selectedInterviewEntity, interviewQuestion);
		} catch (EJBException ex) {
			Logger.getLogger(ApplicationWebManagem.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (InterviewQuestionNameAlreadyExistsException
				| MustIntroduceInterviewQuestionException ex) {
			Logger.getLogger(GuideWebManagem.class.getName()).log(Level.SEVERE,
					null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
		}
	}

	/**
	 * Edit question of a select Interview Question
	 */
	public void editQuestion() {
		try {
			interviewQuestionFacade
					.editInterviewQuestionEntity(selectedInterviewQuestionEntity);
		} catch (InterviewQuestionNameAlreadyExistsException
				| MustIntroduceInterviewQuestionException ex) {
			Logger.getLogger(GuideWebManagem.class.getName()).log(Level.SEVERE,
					null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			lstInterviewQuestionEntity = interviewQuestionFacade
					.getInterviewQuestionEntityByInterview(selectedInterviewEntity
							.getInterviewId());
		}
	}

	/**
	 *
	 */
	public void showSecondPanel() {
		panelGroup.setRendered(false);
		// Initalizes to false
		panelInterviewQuestions.setRendered(true);
	}

	/**
	 *
	 * @param interviewGuide
	 */
	public void showPanel(InterviewEntity interviewGuide) {
		selectedInterviewEntity = interviewGuide;
		lstInterviewQuestionEntity = interviewQuestionFacade
				.getInterviewQuestionEntityByInterview(selectedInterviewEntity
						.getInterviewId());
		panelGroup.setRendered(true);
		panelInterviewQuestions.setRendered(false);
	}

	// ///////////////////Getters && Setters////////////////////

	public List<InterviewEntity> getLstInterviews() {
		return interviewEntityFacade.findAll();
	}

	public void setLstInterviews(List<InterviewEntity> lstInterviews) {
		this.lstInterviews = lstInterviews;
	}

	public List<InterviewEntity> getLstInterviewsInUse() {
		return interviewEntityFacade.findInterviewsInUse();
	}

	public void setLstInterviewsInUse(List<InterviewEntity> lstInterviewsInUse) {
		this.lstInterviewsInUse = lstInterviewsInUse;
	}

	public InterviewEntity getSelectedInterviewEntity() {
		return selectedInterviewEntity;
	}

	public void setSelectedInterviewEntity(
			InterviewEntity selectedInterviewEntity) {
		this.selectedInterviewEntity = selectedInterviewEntity;
	}

	public String getGuideName() {
		return guideName;
	}

	public void setGuideName(String guideName) {
		this.guideName = guideName;
	}

	public InterviewType getGuideType() {
		return guideType;
	}

	public void setGuideType(InterviewType guideType) {
		this.guideType = guideType;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public InterviewEntityFacade getInterviewEntityFacade() {
		return interviewEntityFacade;
	}

	public void setInterviewEntityFacade(
			InterviewEntityFacade interviewEntityFacade) {
		this.interviewEntityFacade = interviewEntityFacade;
	}

	public InterviewQuestionFacade getInterviewQuestionFacade() {
		return interviewQuestionFacade;
	}

	public void setInterviewQuestionFacade(
			InterviewQuestionFacade interviewQuestionFacade) {
		this.interviewQuestionFacade = interviewQuestionFacade;
	}

	public SendEmail getMail() {
		return mail;
	}

	public void setMail(SendEmail mail) {
		this.mail = mail;
	}

	public PositionFacade getPositionFacade() {
		return positionFacade;
	}

	public void setPositionFacade(PositionFacade positionFacade) {
		this.positionFacade = positionFacade;
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	public UIPanel getPanelGroup() {
		return panelGroup;
	}

	public void setPanelGroup(UIPanel panelGroup) {
		this.panelGroup = panelGroup;
	}

	public List<InterviewQuestionEntity> getLstInterviewQuestionEntity() {
		return lstInterviewQuestionEntity;
	}

	public void setLstInterviewQuestionEntity(
			List<InterviewQuestionEntity> lstInterviewQuestionEntity) {
		this.lstInterviewQuestionEntity = lstInterviewQuestionEntity;
	}

	public InterviewQuestionEntity getSelectedInterviewQuestionEntity() {
		return selectedInterviewQuestionEntity;
	}

	public void setSelectedInterviewQuestionEntity(
			InterviewQuestionEntity selectedInterviewQuestionEntity) {
		this.selectedInterviewQuestionEntity = selectedInterviewQuestionEntity;
	}

	public UIPanel getPanelInterviewQuestions() {
		return panelInterviewQuestions;
	}

	public void setPanelInterviewQuestions(UIPanel panelInterviewQuestions) {
		this.panelInterviewQuestions = panelInterviewQuestions;
	}

}
