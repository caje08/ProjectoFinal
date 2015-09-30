/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.InterviewEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewQuestionEntity;
import pt.uc.dei.aor.proj.db.tools.InterviewType;
import pt.uc.dei.aor.proj.serv.exceptions.InterviewEntityNameException;

/**
 * @author
 */
@Stateless
public class InterviewEntityFacade extends AbstractFacade<InterviewEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@EJB
	private InterviewQuestionFacade interviewQuestionFacade;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public InterviewEntityFacade() {
		super(InterviewEntity.class);
	}

	/**
	 *
	 * @return list of InterviewEntitys in use
	 */
	public List<InterviewEntity> findInterviewsInUse() {
		Query query = em.createNamedQuery("InterviewEntity.findInUse", InterviewEntity.class);
		query.setParameter("True", true);
		return query.getResultList();
	}

	/**
	 *  Create a new Interview Guide
	 * @param guideName
	 * @param guideType
	 * @throws InterviewEntityNameException
	 */
	public void createGuide(String guideName, InterviewType guideType) throws InterviewEntityNameException {
		//if this guide name is not in database
		if (!knowIfaInterviewEntityhasTheSameName(guideName)) {
			InterviewEntity interviewGuide = new InterviewEntity();
			interviewGuide.setCreationDate(new Date());
			interviewGuide.setTitle(guideName);
			interviewGuide.setType(guideType);
			interviewGuide.setInUse(Boolean.TRUE);
			em.persist(interviewGuide);
			em.flush();
		}
		else{
			//if guide name has already been used
			throw new InterviewEntityNameException();
		}
	}

	/**
	 *
	 * @return list of phone interviews
	 */
	public List<InterviewEntity> lstPhoneInterviews() {
		Query query = em.createNamedQuery("InterviewEntity.findAllPhoneGuides", InterviewEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @return list of presential interviews
	 */
	public List<InterviewEntity> lstPresentialInterviews() {
		Query query = em.createNamedQuery("InterviewEntity.findAllPresentialGuides", InterviewEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @return list of phone interviews in use
	 */
	public List<InterviewEntity> lstPhoneInterviewsInUse() {
		Query query = em.createNamedQuery("InterviewEntity.findInUsePhoneGuides", InterviewEntity.class);
		query.setParameter("True", true);
		return query.getResultList();
	}

	/**
	 *
	 * @return list of presential interviews in use
	 */
	public List<InterviewEntity> lstPresentialInterviewsInUse() {
		Query query = em.createNamedQuery("InterviewEntity.findInUsePresentialGuides", InterviewEntity.class);
		query.setParameter("True", true);
		return query.getResultList();
	}

	/**
	 * Insert Question in interview Guide
	 * @param selectedInterviewEntity
	 * @param interviewQuestion
	 * @throws EJBException
	 */
	public void mergeQuestionsInGuide(InterviewEntity selectedInterviewEntity, InterviewQuestionEntity interviewQuestion) throws EJBException {
		selectedInterviewEntity.getInterviewQuestion().add(interviewQuestion);
		edit(selectedInterviewEntity);
		em.flush();
	}

	/**
	 *
	 * @return List of phone InterviewEntity in use with questions
	 */
	public List<InterviewEntity> lstPhoneInterviewsInUseWithQuestions() {
		List<InterviewEntity> phoneInterviewEntitys = new ArrayList<>();
		Query query = em.createNamedQuery("InterviewEntity.findInUsePhoneGuides", InterviewEntity.class);
		query.setParameter("True", true);
		//All List of phone Interviews Guides in use
		List<InterviewEntity> allInterviewEntitys = query.getResultList();
		Logger.getLogger(InterviewEntityFacade.class.getName()).log(
				Level.INFO,"Inside lstPhoneInterviewsInUseWithQuestions() with allinterviews.size="+allInterviewEntitys.size());
		for (InterviewEntity allInterviewEntity : allInterviewEntitys) {
			if (!allInterviewEntity.getInterviewQuestion().isEmpty()) {
				phoneInterviewEntitys.add(allInterviewEntity);
			}
		}
		return phoneInterviewEntitys;
	}

	/**
	 *
	 * @return List of presential InterviewEntity in use with questions
	 */
	public List<InterviewEntity> lstPresentialInterviewsInUseWithQuestions() {
		List<InterviewEntity> presentialInterviewEntitys = new ArrayList<>();
		Query query = em.createNamedQuery("InterviewEntity.findInUsePresentialGuides", InterviewEntity.class);
		query.setParameter("True", true);
		List<InterviewEntity> allInterviewEntitys = query.getResultList();
		for (InterviewEntity allInterviewEntity : allInterviewEntitys) {
			if (!allInterviewEntity.getInterviewQuestion().isEmpty()) {
				presentialInterviewEntitys.add(allInterviewEntity);
			}
		}
		return presentialInterviewEntitys;
	}

	/**
	 *
	 * @param title
	 * @return true if an Interview Guide has the same name as the Interview guide
	 */
	private boolean knowIfaInterviewEntityhasTheSameName(String title) {
		Query query = em.createNamedQuery("InterviewEntity.findByName", InterviewEntity.class);
		query.setParameter("title", title);
		return !query.getResultList().isEmpty();
	}

}
