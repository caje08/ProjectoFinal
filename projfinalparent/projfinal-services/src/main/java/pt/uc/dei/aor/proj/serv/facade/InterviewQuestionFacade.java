/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.InterviewEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewQuestionEntity;
import pt.uc.dei.aor.proj.db.tools.AnswerType;
import pt.uc.dei.aor.proj.serv.exceptions.InterviewQuestionNameAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.MustIntroduceInterviewQuestionException;

/**
 * @author
 */
@Stateless
public class InterviewQuestionFacade extends AbstractFacade<InterviewQuestionEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public InterviewQuestionFacade() {
		super(InterviewQuestionEntity.class);
	}

	/**
	 *
	 * @param interviewId
	 * @return List of Interview Questions of an interview
	 */
	public List<InterviewQuestionEntity> getInterviewQuestionEntityByInterview(Long interviewId) {
		Query query = em.createNamedQuery("InterviewQuestionEntity.findByInterview", InterviewQuestionEntity.class);
		query.setParameter("interviewId", interviewId);
		return query.getResultList();
	}

	/**
	 *  Edit specific Interview Question
	 * @param interviewQuestion
	 * @throws InterviewQuestionNameAlreadyExistsException
	 * @throws MustIntroduceInterviewQuestionException
	 */
	public void editInterviewQuestionEntity(InterviewQuestionEntity interviewQuestion) throws InterviewQuestionNameAlreadyExistsException, MustIntroduceInterviewQuestionException {
		if (!existsQuestionName(interviewQuestion.getQuestion(), interviewQuestion.getInterviewGuide()) && !interviewQuestion.getQuestion().isEmpty()) {
			edit(interviewQuestion);
		} else if (existsQuestionName(interviewQuestion.getQuestion(), interviewQuestion.getInterviewGuide())) {
			throw new InterviewQuestionNameAlreadyExistsException();
		} else {
			throw new MustIntroduceInterviewQuestionException();
		}

	}

	/**
	 * Create new Question for a Interview Guide
	 * @param questionName
	 * @param selectedInterviewGuide
	 * @param answerType
	 * @return
	 * @throws InterviewQuestionNameAlreadyExistsException
	 * @throws MustIntroduceInterviewQuestionException
	 */
	public InterviewQuestionEntity createQuestionForGuide(String questionName, InterviewEntity selectedInterviewGuide, AnswerType answerType) throws InterviewQuestionNameAlreadyExistsException, MustIntroduceInterviewQuestionException {
		//if question name is not empty and  question name is not used in that Interview Guide
		if (!existsQuestionName(questionName, selectedInterviewGuide) && !questionName.isEmpty()) {
			InterviewQuestionEntity interviewQuestion = new InterviewQuestionEntity();
			interviewQuestion.setQuestion(questionName);
			interviewQuestion.setInterviewGuide(selectedInterviewGuide);
			interviewQuestion.setAnswerType(answerType);
			int questionNum = getInterviewQuestionEntityByInterview(selectedInterviewGuide.getInterviewId()).size();
			//give to interview question the last interview question plus 1
			interviewQuestion.setQuestionNumber(questionNum + 1);
			create(interviewQuestion);
			return interviewQuestion;
		} else if (existsQuestionName(questionName, selectedInterviewGuide)) {
			throw new InterviewQuestionNameAlreadyExistsException();
		} else {
			throw new MustIntroduceInterviewQuestionException();
		}
	}

	/**
	 *
	 * @param question
	 * @param interviewGuide
	 * @return true if Interview Guide has the same name has question inserted
	 */
	public boolean existsQuestionName(String question, InterviewEntity interviewGuide) {
		Query query = em.createNamedQuery("InterviewQuestionEntity.findByInterview", InterviewQuestionEntity.class);
		query.setParameter("interviewId", interviewGuide.getInterviewId());
		List<InterviewQuestionEntity> lstInterviewQuestions = query.getResultList();
		for (InterviewQuestionEntity lstInterviewQuestion : lstInterviewQuestions) {
			return lstInterviewQuestion.getQuestion().equals(question);
		}
		return false;
	}

}
