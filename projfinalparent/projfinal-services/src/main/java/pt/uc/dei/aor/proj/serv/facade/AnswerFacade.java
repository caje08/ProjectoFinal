/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.AnswerEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewFeedbackEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewQuestionEntity;
import pt.uc.dei.aor.proj.db.tools.Outcome;
import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplication;
import pt.uc.dei.aor.proj.serv.exceptions.SubmitFeedbackBeforeInterviewDateException;

/**
 * @author
 */
@Stateless
public class AnswerFacade extends AbstractFacade<AnswerEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private InterviewFeedbackFacade interviewFeedbackFacade;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public AnswerFacade() {
		super(AnswerEntity.class);
	}

	/**
	 *
	 * @param interviewFeedback
	 * @return List of answers of an Interview
	 */
	public List<AnswerEntity> setAnswersOfAnInterview(InterviewFeedbackEntity interviewFeedback) {
		List<AnswerEntity> lstAnswers = new ArrayList<>();
		//Find List of Interview Question of a Interview
		Query query = em.createNamedQuery("InterviewQuestionEntity.findByInterview", InterviewQuestionEntity.class);
		query.setParameter("interviewId", interviewFeedback.getInterviewEntity().getInterviewId());
		List<InterviewQuestionEntity> lstInterviewQuestion = query.getResultList();
		for (InterviewQuestionEntity interviewQuestion : lstInterviewQuestion) {
			AnswerEntity answer = new AnswerEntity();
			//Build answer by setting interview Question types (Answer type, question name and question number) on him
			answer.setAnswerType(interviewQuestion.getAnswerType());
			answer.setQuestion(interviewQuestion.getQuestion());
			answer.setQuestionNumber(interviewQuestion.getQuestionNumber());
			//Add new answer to List of answers
			lstAnswers.add(answer);
		}
		return lstAnswers;
	}

	/**
	 *
	 * @param interviewFeedback
	 * @param lstAnswers
	 * @throws SubmitFeedbackBeforeInterviewDateException
	 */
	public void submitFeedbackEntity(InterviewFeedbackEntity interviewFeedback, List<AnswerEntity> lstAnswers) throws SubmitFeedbackBeforeInterviewDateException {
		//get application from interview feedback
		ApplicationEntity application = interviewFeedback.getApplication();

		for (AnswerEntity lstAnswer : lstAnswers) {
			AnswerEntity answer = new AnswerEntity();
			answer.setAnswer(lstAnswer.getAnswer());
			answer.setQuestionNumber(lstAnswer.getQuestionNumber());
			answer.setAnswerType(lstAnswer.getAnswerType());
			answer.setQuestion(lstAnswer.getQuestion());
			interviewFeedback.getAnswer().add(answer);
			interviewFeedback.setOutcome(interviewFeedback.getOutcome());
			//if number of interviews of a specific application is less than 2
			if (interviewFeedbackFacade.numberOfInterviews(interviewFeedback.getApplication().getApplicationId()) < 2) {
				//if outcome from interview is Accepted, the application status will be interviewing
				if (interviewFeedback.getOutcome().equals(Outcome.ACCEPTED)) {
					application.setStatus(StatusApplication.INTERVIEWING);
					//else, will be rejected and the motive will be personality
				} else {
					application.setStatus(StatusApplication.REJECTED);
					application.setMotive(RejectionMotive.PERSONALITY);
				}

			} else {
				//if outcome from second interview is Accepted the applciation status will be negotiation
				if (interviewFeedback.getOutcome().equals(Outcome.REJECTED)) { //if (interviewFeedback.getOutcome().equals(Outcome.ACCEPTED)) {
					application.setStatus(StatusApplication.REJECTED);
					application.setMotive(RejectionMotive.PERSONALITY);
					//application.setStatus(StatusApplication.NEGOTIATION);
					//else, will be rejected and the motive will be personality
				} 
//				else {
//					application.setStatus(StatusApplication.REJECTED);
//					application.setMotive(RejectionMotive.PERSONALITY);
//				}

			}
			//persist entity answer
			em.persist(answer);
			//edit interview feedback
			interviewFeedbackFacade.edit(interviewFeedback);
			//edit application
			applicationFacade.edit(application);
		}
		em.flush();

	}

	/////////////////////Getters && Setters////////////////////
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

	public InterviewFeedbackFacade getInterviewFeedbackFacade() {
		return interviewFeedbackFacade;
	}

	public void setInterviewFeedbackFacade(InterviewFeedbackFacade interviewFeedbackFacade) {
		this.interviewFeedbackFacade = interviewFeedbackFacade;
	}

}
