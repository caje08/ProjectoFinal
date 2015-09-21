/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplication;
import pt.uc.dei.aor.proj.serv.ejb.SendEmail;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCVFileException;
import pt.uc.dei.aor.proj.serv.exceptions.DoNotUploadCoverLetterException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.EmailAndPasswordNotCorrespondingToLinkedinCredentialsException;
import pt.uc.dei.aor.proj.serv.exceptions.InvalidAuthException;
import pt.uc.dei.aor.proj.serv.exceptions.NumberOfMobilePhoneDigitsException;

/**
 *
 * @author
 */
@Stateless
public class ApplicationFacade extends AbstractFacade<ApplicationEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@EJB
	private ApplicantFacade applicantFacade;
	@EJB
	private SendEmail sendEmail;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public ApplicationFacade() {
		super(ApplicationEntity.class);
	}

	/**
	 *
	 * @param applicant
	 * @return List of ApplicationEntity of logged in ApplicantEntity
	 */
	public List<ApplicationEntity> getUserApplications(ApplicantEntity applicant) {
		Query query = em.createNamedQuery("ApplicationEntity.findByUser", ApplicationEntity.class);
		query.setParameter("applicant", applicant);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of Spontaneous Applications
	 */
	public List<ApplicationEntity> lstSpontaneousApplications() {
		Query query = em.createNamedQuery("ApplicationEntity.findBySpontaneousApplications", ApplicationEntity.class);
		List<ApplicationEntity> applications = query.getResultList();
		return applications;
	}

	/**
	 *
	 * @param manager
	 * @return List of Spontaneous Applications of a logged in manager
	 */
	public List<ApplicationEntity> lstSpontaneousApplicationsOfManager(ManagerEntity manager) {
		Query query = em.createNamedQuery("ApplicationEntity.findBySpontaneousApplicationsOfManager", ApplicationEntity.class);
		query.setParameter("manager", manager);
		List<ApplicationEntity> applications = query.getResultList();
		return applications;
	}

	/**
	 *
	 * @param manager
	 * @return List of Non Spontaneous Applications of a logged in manager
	 */
	public List<ApplicationEntity> lstNonSpontaneousApplicationsOfManager(ManagerEntity manager) {
		Query query = em.createNamedQuery("ApplicationEntity.findByNonSpontaneousApplicationsOfManager", ApplicationEntity.class);
		query.setParameter("manager", manager);
		List<ApplicationEntity> applications = query.getResultList();
		return applications;
	}

	/**
	 *
	 * @return List of Non Spontaneous Applications
	 */
	public List<ApplicationEntity> lstNonSpontaneousApplications() {
		Query query = em.createNamedQuery("ApplicationEntity.findByNonSpontaneousApplications", ApplicationEntity.class);
		List<ApplicationEntity> applications = query.getResultList();
		return applications;
	}

	/**
	 *
	 * @param position
	 * @return List Of Applications of a selected position
	 */
	public List<ApplicationEntity> lstApplicationsByPosition(PositionEntity position) {
		Query query = em.createNamedQuery("ApplicationEntity.findByPosition", ApplicationEntity.class);
		query.setParameter("position", position);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of rejected applications due to CV
	 */
	public List<ApplicationEntity> lstApplicationsByRejectionMotiveCV() {
		Query query = em.createNamedQuery("ApplicationEntity.findByRejectionMotiveCV", ApplicationEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of rejected applications due to negotiation
	 */
	public List<ApplicationEntity> lstApplicationsByRejectionMotiveNEGOTIATION() {
		Query query = em.createNamedQuery("ApplicationEntity.findByRejectionMotiveNEGOTIATION", ApplicationEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @return List of rejected applications due to personality
	 */
	public List<ApplicationEntity> lstApplicationsByRejectionMotivePERSONALITY() {
		Query query = em.createNamedQuery("ApplicationEntity.findByRejectionMotivePERSONALITY", ApplicationEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @param rejectionMotive
	 * @return list of rejected applications due to given Rejection Motive
	 */
	public List<ApplicationEntity> lstApplicationsByRejectionMotive(RejectionMotive rejectionMotive) {
		if (rejectionMotive == RejectionMotive.CV) {
			Query query = em.createNamedQuery("ApplicationEntity.findByRejectionMotiveCV", ApplicationEntity.class);
			return query.getResultList();
		} else if (rejectionMotive == RejectionMotive.NEGOTIATION) {
			Query query = em.createNamedQuery("ApplicationEntity.findByRejectionMotiveNEGOTIATION", ApplicationEntity.class);
			return query.getResultList();
		} else {
			Query query = em.createNamedQuery("ApplicationEntity.findByRejectionMotivePERSONALITY", ApplicationEntity.class);
			return query.getResultList();
		}
	}

	/**
	 *
	 * @return List of Applications with status equals to interviewing
	 */
	public List<ApplicationEntity> lstApplicationsStatusInterviewing() {
		Query query = em.createNamedQuery("ApplicationEntity.findByStatusINTERVIEWING", ApplicationEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @return
	 */
	public List<ApplicationEntity> lstApplicationsStatusHired() {
		Query query = em.createNamedQuery("ApplicationEntity.findByStatusHIRED", ApplicationEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @param position
	 * @return List of applicants of given position
	 */
	public List<ApplicantEntity> lstApplicantsByPosition(PositionEntity position) {
		Query query = em.createNamedQuery("ApplicationEntity.findApplicantByPosition", ApplicantEntity.class);
		query.setParameter("position", position);
		return query.getResultList();
	}

	/**
	 *
	 * @param begin
	 * @param end
	 * @return List of applicants of have made an application between begin and end dates
	 */
	public List<ApplicantEntity> lstApplicantsPerTime(Date begin, Date end) {
		Query query = em.createNamedQuery("ApplicationEntity.findApplicantByDate", ApplicantEntity.class);
		query.setParameter("startDate", begin);
		query.setParameter("endDate", end);
		return query.getResultList();
	}

	/**
	 *
	 * @param begin
	 * @param end
	 * @return List of applicants of have made a spontaneous application between begin and end dates
	 */
	public List<ApplicationEntity> lstSpontaneousApplicationsPerTime(Date begin, Date end) {
		Query query = em.createNamedQuery("ApplicationEntity.findByDateSpontaneous", ApplicationEntity.class);
		query.setParameter("startDate", begin);
		query.setParameter("endDate", end);
		return query.getResultList();
	}

	/**
	 * Create a new ApplicationEntity of a new ApplicantEntity
	 * @param applicant
	 * @param isLinkedin
	 * @param application
	 * @param cvUploadName
	 * @param clUploadName
	 * @param position
	 * @throws InvalidAuthException
	 * @throws EmailAlreadyExistsException
	 * @throws NumberOfMobilePhoneDigitsException
	 * @throws DoNotUploadCVFileException
	 * @throws DoNotUploadCoverLetterException
	 * @throws EJBException
	 * @throws EmailAndPasswordNotCorrespondingToLinkedinCredentialsException
	 */
	public void createApplicationOfNewApplicant(ApplicantEntity applicant,  ApplicationEntity application, String cvUploadName, String clUploadName, PositionEntity position) throws InvalidAuthException, EmailAlreadyExistsException, NumberOfMobilePhoneDigitsException, DoNotUploadCVFileException, DoNotUploadCoverLetterException, EJBException, EmailAndPasswordNotCorrespondingToLinkedinCredentialsException {
		applicantFacade.createApplicant(applicant);
		Logger.getLogger(ApplicationFacade.class.getName()).log(Level.INFO, "inside createApplicationOfNewApplicant() and before setting (application) properties ");
		if (cvUploadName != null && clUploadName != null) {
			application.setApplicant(applicant);
			application.setCv(cvUploadName);
			application.setCoverLetter(clUploadName);
			//application.setIsSpontaneous(false);
			application.setStatus(StatusApplication.SUBMITTED);
			application.setIsSpontaneous(false);
			application.setApplicationDate(new Date());
			if(position!=null){
			 application.setPosition(position); // IMPORTANT TO ACTIVATE THIS LINE
			 Logger.getLogger(ApplicationFacade.class.getName()).log(Level.INFO, "inside createApplicationOfNewApplicant() and before create(application) with application.getPosition().getTitle()= "+application.getPosition().getTitle());
			}
			Logger.getLogger(ApplicationFacade.class.getName()).log(Level.INFO, "inside createApplicationOfNewApplicant() and before create(application) with application.getApplicant().getEmail()= "+application.getApplicant().getEmail());
			create(application);
			Logger.getLogger(ApplicationFacade.class.getName()).log(Level.INFO, "inside createApplicationOfNewApplicant() and before sending email stating  New ApplicationEntity has been made by " + applicant.getFirstName() + applicant.getLastName() + " to the PositionEntity " + application.getPosition().getTitle(), application.getPosition().getManager().getEmail());
			
			//send an email to new ApplicantEntity --->  IMPORTANT TO ACTIVATE NEXT LINE
			sendEmail.sendEMail("acertarrumo2015@gmail.com", "New application has been made", "New Application has been made by " + applicant.getFirstName() + applicant.getLastName() + " to the Position " + application.getPosition().getTitle(), application.getPosition().getManager().getEmail());
		} else {
			if (cvUploadName == null) {
				throw new DoNotUploadCVFileException();
			} else {
				throw new DoNotUploadCoverLetterException();
			}
		}
	}

	/**
	 * Create Spontaneous Applications of a new ApplicantEntity
	 * @param applicant
	 * @param isLinkedin
	 * @param application
	 * @param cvUploadName
	 * @param clUploadName
	 * @throws InvalidAuthException
	 * @throws EmailAlreadyExistsException
	 * @throws NumberOfMobilePhoneDigitsException
	 * @throws DoNotUploadCVFileException
	 * @throws DoNotUploadCoverLetterException
	 * @throws EJBException
	 * @throws EmailAndPasswordNotCorrespondingToLinkedinCredentialsException
	 */
	public void createSpontaneousApplicationOfNewApplicant(ApplicantEntity applicant,  ApplicationEntity application, String cvUploadName, String clUploadName) throws InvalidAuthException, EmailAlreadyExistsException, NumberOfMobilePhoneDigitsException, DoNotUploadCVFileException, DoNotUploadCoverLetterException, EJBException, EmailAndPasswordNotCorrespondingToLinkedinCredentialsException {
		applicantFacade.createApplicant(applicant);
		
		if (cvUploadName != null && clUploadName != null) {
			application.setApplicant(applicant);
			application.setCv(cvUploadName);
			application.setCoverLetter(clUploadName);
			application.setIsSpontaneous(false);
			application.setStatus(StatusApplication.SUBMITTED);
			application.setIsSpontaneous(true);
			application.setApplicationDate(new Date());

			create(application);
		} else {
			if (cvUploadName == null) {
				throw new DoNotUploadCVFileException();
			} else {
				throw new DoNotUploadCoverLetterException();
			}
		}
	}

	/**
	 *
	 * @param application
	 * @return false if the actual date has more 24 hours then application date,
	 */
	public boolean edit24hApplication(ApplicationEntity application) {
		Date actual = new Date();
		Date appDate = application.getApplicationDate();
		//transform actual and application date into a Gregorian Calendar
		Calendar actualCalendar = new GregorianCalendar();
		actualCalendar.setTime(actual);
		Calendar applicationCalendar = new GregorianCalendar();
		applicationCalendar.setTime(appDate);
		// get hours
		double hours = (actualCalendar.getTimeInMillis() - applicationCalendar.getTimeInMillis()) / 1000 / 60 / 60;
		return hours <= 24;
	}

	/**
	 *
	 * @param application
	 * @param cvUploadName
	 * @param clUploadName
	 * @param position
	 * @param source
	 * @param loggedUser
	 * @throws DoNotUploadCoverLetterException
	 * @throws DoNotUploadCVFileException
	 * @throws EJBException
	 */
	public void createUserApplications(ApplicationEntity application, String cvUploadName, String clUploadName, PositionEntity position, String source, UserEntity loggedUser) throws DoNotUploadCoverLetterException, DoNotUploadCVFileException, EJBException {
		if (cvUploadName != null && clUploadName != null) {
			application.setApplicant((ApplicantEntity) loggedUser);
			application.setCv(cvUploadName);
			application.setCoverLetter(clUploadName);
			application.setStatus(StatusApplication.SUBMITTED);
			application.setApplicationDate(new Date());
			application.setSource(source);
			application.setIsSpontaneous(false);
			//	application.setPosition(position); --> IMPORTANT TO ACTIVATE THIS AND NEXT LINE
			//	sendEmail.sendEMail("acertarrumo2015@gmail.com", "New Application has been made", "New Application has been made by " + loggedUser.getFirstName() + loggedUser.getLastName() + " to the Position " + application.getPosition().getTitle(), application.getPosition().getManager().getEmail());
			create(application);
		} else {
			if (cvUploadName == null) {
				throw new DoNotUploadCVFileException();
			} else {
				throw new DoNotUploadCoverLetterException();
			}
		}
	}

	/**
	 *
	 * @param application
	 * @param cvUploadName
	 * @param clUploadName
	 * @param source
	 * @param loggedUser
	 * @throws DoNotUploadCoverLetterException
	 * @throws DoNotUploadCVFileException
	 * @throws EJBException
	 */
	public void createManualUserApplications(ApplicationEntity application, String cvUploadName, String clUploadName, String source, UserEntity loggedUser) throws DoNotUploadCoverLetterException, DoNotUploadCVFileException, EJBException {
		if (cvUploadName != null && clUploadName != null) {
			application.setApplicant((ApplicantEntity) loggedUser);
			application.setCv(cvUploadName);
			application.setCoverLetter(clUploadName);
			application.setStatus(StatusApplication.SUBMITTED);
			application.setApplicationDate(new Date());
			application.setSource(source);
			application.setIsSpontaneous(true);
			create(application);
		} else {
			if (cvUploadName == null) {
				throw new DoNotUploadCVFileException();
			} else {
				throw new DoNotUploadCoverLetterException();
			}
		}
	}

	/**
	 *
	 * @return List of Applications that are in Negotiation Process
	 */
	public List<ApplicationEntity> lstApplicationInNegotiationProcess() {
		Query query = em.createNamedQuery("ApplicationEntity.findByStatusNEGOTIATION", ApplicationEntity.class);
		return query.getResultList();

	}

	/**
	 *
	 * @param applicationDate
	 * @return List of Applications of chosen date
	 */
	public List<ApplicationEntity> lstApplicationByDate(Date applicationDate) {
		Query query = em.createNamedQuery("ApplicationEntity.findByDate", ApplicationEntity.class);
		query.setParameter("applicationDate", applicationDate);
		return query.getResultList();
	}

	/**
	 *
	 * @param date
	 * @return List of Spontaneous Applications of chosen date
	 */
	public List<ApplicationEntity> lstSpontaneousApplicationsByDate(Date date) {
		Query query = em.createNamedQuery("ApplicationEntity.findSpontaneousByDate", ApplicationEntity.class);
		query.setParameter("applicationDate", date);
		return query.getResultList();
	}

	/**
	 *
	 * @return average time between application date and hiring date
	 * @throws java.lang.Exception
	 */
	public Double avgTimeToHire() throws Exception, NoResultException {
		String mysql = "select AVG (DATE_PART('day',applicationentity.hiringDate-applicationentity.applicationDate))\n"
				+ "FROM applicationentity;";
		Query query = em.createNativeQuery(mysql);
		double result;
		if (query.getSingleResult() != null) {
			Double bd = (Double) query.getSingleResult();
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

	public ApplicantFacade getApplicantFacade() {
		return applicantFacade;
	}

	public void setApplicantFacade(ApplicantFacade applicantFacade) {
		this.applicantFacade = applicantFacade;
	}

	public SendEmail getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(SendEmail sendEmail) {
		this.sendEmail = sendEmail;
	}

}
