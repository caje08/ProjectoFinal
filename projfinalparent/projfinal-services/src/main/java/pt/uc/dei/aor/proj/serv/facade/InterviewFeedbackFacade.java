/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewFeedbackEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.tools.InterviewType;
import pt.uc.dei.aor.proj.db.tools.Outcome;
import pt.uc.dei.aor.proj.db.tools.StatusApplication;
import pt.uc.dei.aor.proj.serv.ejb.SendEmail;
import pt.uc.dei.aor.proj.serv.ejb.SendEmailAttachedFiles;
import pt.uc.dei.aor.proj.serv.exceptions.FirstInterviewAfterAtualDateException;
import pt.uc.dei.aor.proj.serv.exceptions.InterviewerSameDateException;
import pt.uc.dei.aor.proj.serv.exceptions.MustIntroduceInterviewerException;
import pt.uc.dei.aor.proj.serv.exceptions.SecondInterviewAfterFirstInterviewException;
import pt.uc.dei.aor.proj.serv.tools.BundleUtils;

/**
 * @author
 */
@Stateless
public class InterviewFeedbackFacade extends AbstractFacade<InterviewFeedbackEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@EJB
	private ApplicationFacade applicationFacade;
	@EJB
	private SendEmail mail;
	@EJB
	private SendEmailAttachedFiles emailAttachedFiled;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public InterviewFeedbackFacade() {
		super(InterviewFeedbackEntity.class);
	}

	/**
	 *
	 * @param applicationId
	 * @return List of interviews of an application
	 */
	public List<InterviewFeedbackEntity> listInterviews(Long applicationId) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findInterview", InterviewFeedbackEntity.class);
		query.setParameter("applicationId", applicationId);
		return query.getResultList();
	}

	/**
	 * Create a new Interview
	 *
	 * @param interviewDate
	 * @param interviewer
	 * @param interviewGuide
	 * @param application
	 * @param cvDestination
	 * @throws FirstInterviewAfterAtualDateException
	 * @throws InterviewerSameDateException
	 * @throws MessagingException
	 * @throws EJBException
	 * @throws SecondInterviewAfterFirstInterviewException
	 */
	public void createInterview(Date interviewDate, InterviewerEntity interviewer, InterviewEntity interviewGuide, ApplicationEntity application, String cvDestination) throws FirstInterviewAfterAtualDateException, InterviewerSameDateException, MessagingException, EJBException, SecondInterviewAfterFirstInterviewException, MustIntroduceInterviewerException {
		InterviewFeedbackEntity interviewFeedback = new InterviewFeedbackEntity();
		//em.persist(interviewFeedback);
		Logger.getLogger(InterviewFeedbackFacade.class.getName()).log(Level.INFO, "Inside createInterview()");
		System.out.println("\n Inside InterviewFeedbackFacade.createInterview() before checking recruiter availability");
		//see if intervew date is after the current date and selected interviewer is available
		if (interviewDate.after(new Date()) && checkRecruiterAvailability(interviewer, interviewDate) && interviewer != null) {
			interviewFeedback.setInterviewer(interviewer);
			interviewFeedback.setInterviewDate(interviewDate);
			//interviewFeedback.setAnswer(null);
			interviewFeedback.setInterviewEntity(interviewGuide);
			//em.persist(interviewFeedback);
			System.out.println("\n Inside InterviewFeedbackFacade.createInterview() , if statement, after checking recruiter availability");
			//if an interview feedback do not have a Interview Phone set it
			if (knowIfInterviewHasPhoneInterviewEntity(InterviewType.PHONE, application)) {
				interviewFeedback.setInterviewType(InterviewType.PHONE);
			} else {
				//else set presential
				if (dateFirstFeedbackInterview(application).before(interviewDate)) {
					interviewFeedback.setInterviewType(InterviewType.PRESENTIAL);
				} else {
					throw new SecondInterviewAfterFirstInterviewException();
				}
			}
			System.out.println("\n Inside InterviewFeedbackFacade.createInterview() before interviewFeedback.setApplication(application)");
			interviewFeedback.setApplication(application);
		//	em.persist(interviewFeedback);
			application.setInterviewer(interviewer);
			application.getInterviewFeedbackEntitys().add(interviewFeedback);
			application.setStatus(StatusApplication.INTERVIEWING);
			System.out.println("\n Inside InterviewFeedbackFacade.createInterview() before applicationFacade.edit(application)");
		//	applicationFacade.edit(application);

			InterviewEntity tmpinterv = interviewFeedback.getInterviewEntity();
			System.out.println("\n InterviewEntity = "+tmpinterv.getType());
			System.out.println("\ninterviewFeedback.getInterviewer().getEmail()-->"+interviewFeedback.getInterviewer().getEmail());
			System.out.println("\ninterviewFeedback.getApplication().getApplicant().getEmail()-->"+interviewFeedback.getApplication().getApplicant().getEmail());
			System.out.println("\n Inside InterviewFeedbackFacade.createInterview() before sending email");
			
			//em.persist(interviewFeedback);
			String[] attachFiles = new String[1];
			attachFiles[0] = cvDestination + application.getCv();

			SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String finalFormat = format1.format(interviewDate);
			String link = BundleUtils.getSettings("host") + "interviewer/applicationDetails.xhtml?applicantid=" + application.getApplicant().getUserId() + "&applicationid=" + application.getApplicationId();
			//send email with atached files - IMPORTANTE ACTIVAR PRÓXIMA LINHA
//			SendEmailAttachedFiles.sendEmailWithAttachments(interviewer.getEmail(),
//					"New Interview has been made", "You have an interview at " + finalFormat + " and the user link is:\n" + link, attachFiles);
			System.out.println("\n Inside InterviewFeedbackFacade.createInterview() before em.persist(interviewFeedback)");
			em.persist(interviewFeedback);
			//em.merge(interviewFeedback);
			
		} else if (!checkRecruiterAvailability(interviewer, interviewDate)) {
			throw new InterviewerSameDateException();
		} else if (interviewer == null) {
			throw new MustIntroduceInterviewerException();
		} else {
			throw new FirstInterviewAfterAtualDateException();
		}

	}

	/**
	 *
	 * @param interviewType
	 * @param application
	 * @return true if this aplication do not has phone Interview guide
	 */
	public boolean knowIfInterviewHasPhoneInterviewEntity(InterviewType interviewType, ApplicationEntity application) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findEnum", InterviewFeedbackEntity.class);
		query.setParameter("application", application);
		return query.getResultList().size() <= 0;
	}

	/**
	 *
	 * @param applicationId
	 * @return number of interviews of a specific application
	 */
	public int numberOfInterviews(Long applicationId) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findInterview", InterviewFeedbackEntity.class);
		query.setParameter("applicationId", applicationId);
		return query.getResultList().size();
	}

	/**
	 *
	 * @param applicationId
	 * @return List of Interview Feedback of a given application
	 */
	public List<InterviewFeedbackEntity> findInterviewByApplicationId(Long applicationId) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findInterview", InterviewFeedbackEntity.class);
		query.setParameter("applicationId", applicationId);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of  Phone Interview Feedbacks with accepted outcome
	 */
	public List<InterviewFeedbackEntity> lstInterviewFeedbackAcceptedPhone() {
		Query query = em.createNamedQuery("InterviewFeedback.findByOutcomeAcceptedPhone", InterviewFeedbackEntity.class
				);
		return query.getResultList();
	}

	/**
	 *
	 * @param interviewer
	 * @param application
	 * @return List of interviews of Logged in interviewer of an application
	 */
	public List<InterviewFeedbackEntity> lstInterviewsWithInterviewerOfAnApplication(InterviewerEntity interviewer, ApplicationEntity application) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findInterviewByInterviewer", InterviewFeedbackEntity.class);
		query.setParameter("applicationId", application.getApplicationId());
		query.setParameter("interviewer", interviewer);
		return query.getResultList();

	}

	/**
	 *
	 * @param application
	 * @return date of first interview of given application
	 */
	public Date dateFirstFeedbackInterview(ApplicationEntity application) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findDateOfFirstInterviewOfAnApplication", InterviewFeedbackEntity.class);
		query.setParameter("application", application);
		return (Date) query.getSingleResult();
	}

	/**
	 *
	 * @param application
	 * @return true if Phone Interview Feedback is accepted
	 */
	public
	boolean knowIfAPhoneInterviewFeedbackHasAcceptedFeedback(ApplicationEntity application) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findByOutcomeAcceptedPhoneOfAnApplication", InterviewFeedbackEntity.class
				);
		query.setParameter(
				"application", application);
		return !query.getResultList()
				.isEmpty();
	}

	/**
	 *
	 * @return List of Presential Interview Feedbacks with accepted outcome
	 */
	public List<InterviewFeedbackEntity> lstInterviewFeedbackAcceptedPresential() {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findByOutcomeAcceptedPresential", InterviewFeedbackEntity.class
				);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of Phone Interview Feedbacks with refused outcome
	 */
	public List<InterviewFeedbackEntity> lstInterviewFeedbackRefusedPhone() {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findByOutcomeRejectedPhone", InterviewFeedbackEntity.class
				);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of Presential Interview Feedbacks with refused outcome
	 */
	public List<InterviewFeedbackEntity> lstInterviewFeedbackRefusedPresential() {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findByOutcomeRejectedPresential", InterviewFeedbackEntity.class
				);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of Phone Interview Feedbacks
	 */
	public List<InterviewFeedbackEntity> lstInterviewFeedbackPhone() {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findByInterviewTypePHONE", InterviewFeedbackEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @param outcome
	 * @return true if interview outcome is rejected
	 */
	public boolean isRejectedFromFeedback(Outcome outcome) {
		return outcome.equals(Outcome.REJECTED);
	}

	/**
	 *
	 * @param interviewer
	 * @param date
	 * @return true if interviewer do not have a interview at the same date as
	 * interview date selected
	 */
	public boolean checkRecruiterAvailability(InterviewerEntity interviewer, Date date) {
		Calendar c1 = Calendar.getInstance();
		//set Calendar time to date
		c1.setTimeInMillis(date.getTime());
		Calendar tempC1;
		tempC1 = (Calendar) c1.clone();
		//Calendar with minus 29 minutes
		tempC1.add(Calendar.MINUTE, -29);
		Date startDate = tempC1.getTime();
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(date.getTime());
		Calendar tempC2;
		tempC2 = (Calendar) c2.clone();
		//Calendar with plus 29 minutes
		tempC2.add(Calendar.MINUTE, 29);
		Date endDate = tempC2.getTime();
		List<InterviewFeedbackEntity> interviewFeedbacks = lstInterviewsWithThatInterviewer(interviewer);
		for (int i = 0; i < interviewFeedbacks.size(); i++) {
			//if interview date is between the two dates return false
			if (interviewFeedbacks.get(i).getInterviewDate().after(startDate) && interviewFeedbacks.get(i).getInterviewDate().before(endDate)) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param interviewer
	 * @return list of interviews with selected interviewer
	 */
	public List<InterviewFeedbackEntity> lstInterviewsWithThatInterviewer(InterviewerEntity interviewer) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findByInterviewer", InterviewFeedbackEntity.class
				);
		query.setParameter(
				"interviewer", interviewer);
		return query.getResultList();
	}

	/**
	 *
	 * @param interviewFeedback
	 * @return true if an interview has already feedback given
	 */
	public boolean withFeedbackGiven(InterviewFeedbackEntity interviewFeedback) {
		return !interviewFeedback.getAnswer().isEmpty();
	}

	/**
	 *
	 * @param interviewer
	 * @return true if the the interviewer is the current interviewer
	 */
	public boolean knowIfIsTheCurrentInterviewer(InterviewerEntity interviewer) {
		Query query = em.createNamedQuery("InterviewFeedbackEntity.findByInterviewer", InterviewFeedbackEntity.class
				);
		query.setParameter(
				"interviewer", interviewer);

		return !query.getResultList().isEmpty();

	}

	/**
	 *
	 * @return @throws Exception
	 * @throws NoResultException
	 */
	public Double avgTimeToInterview() throws Exception, NoResultException {
		String mysql = "select AVG (DATEDIFF(interviewFeedback.interviewDate, application.applicationDate))\n"
				+ "FROM interviewFeedback, application\n"
				+ "WHERE interviewfeedback.APPLICATION_applicationid = application.applicationid and interviewType = 'PHONE';";
		Query query = em.createNativeQuery(mysql);
		double result;
		if (query.getSingleResult() != null) {
			BigDecimal bd = (BigDecimal) query.getSingleResult();
			result = bd.doubleValue();
		} else {
			throw new NoResultException();
		}
		return result;
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

	public SendEmail getMail() {
		return mail;
	}

	public void setMail(SendEmail mail) {
		this.mail = mail;
	}

	public SendEmailAttachedFiles getEmailAttachedFiled() {
		return emailAttachedFiled;
	}

	public void setEmailAttachedFiled(SendEmailAttachedFiles emailAttachedFiled) {
		this.emailAttachedFiled = emailAttachedFiled;
	}

}
