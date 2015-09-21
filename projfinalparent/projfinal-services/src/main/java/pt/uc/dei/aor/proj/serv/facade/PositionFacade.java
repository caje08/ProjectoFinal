/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.serv.ejb.SendEmail;
import pt.uc.dei.aor.proj.serv.exceptions.BeforeDateNotBeforeClosingDate;
import pt.uc.dei.aor.proj.serv.exceptions.ManagerNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.OpeningDateAfterAtualDate;
import pt.uc.dei.aor.proj.serv.exceptions.PhoneInterviewEntityNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.PositionNotIntroducedException;
import pt.uc.dei.aor.proj.serv.exceptions.PositionOfAnApplicantAlreadyIntroducedOnSPonException;
import pt.uc.dei.aor.proj.serv.exceptions.PresentialInterviewEntityNotIntroducedException;

/**
 *
 * @author
 */
@Stateless
public class PositionFacade extends AbstractFacade<PositionEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@EJB
	private SendEmail mail;
	@EJB
	private ApplicationFacade applicationFacade;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public PositionFacade() {
		super(PositionEntity.class);
	}

	/**
	 *
	 * @return List Of Public Positions
	 */
	public List<PositionEntity> lstPublicPositions() {
		Query query = em.createNamedQuery("PositionEntity.findByVeracity", PositionEntity.class);
		return query.getResultList();
	}

	/**
	 *
	 * @param interviewId
	 * @return List of Positions of a Phone Interview Guide
	 */
	public List<PositionEntity> getInterviewEntity(Long interviewId) {
		Query query = em.createNamedQuery("PositionEntity.findPhoneInterviewEntity", PositionEntity.class);
		query.setParameter("interviewId", interviewId);
		return query.getResultList();
	}

	/**
	 *
	 * @param interviewId
	 * @return List of Positions of Presential Interview Guide
	 */
	public List<PositionEntity> getPresentialInterviewEntity(Long interviewId) {
		Query query = em.createNamedQuery("PositionEntity.findPresencialInterviewEntity", PositionEntity.class);
		query.setParameter("interviewId", interviewId);
		return query.getResultList();
	}

	/**
	 *
	 * @param positionCode
	 * @return PositionEntity by PositionEntity Code
	 */
	public PositionEntity getPositionByCode(Long positionCode) {
		Query query = em.createNamedQuery("PositionEntity.findByCode", PositionEntity.class);
		query.setParameter("positionCode", positionCode);
		return (PositionEntity) query.getSingleResult();
	}

	/**
	 * Create a new PositionEntity
	 * @param manager
	 * @param position
	 * @param onWeekOnms
	 * @throws ManagerNotIntroducedException
	 * @throws PhoneInterviewEntityNotIntroducedException
	 * @throws PresentialInterviewEntityNotIntroducedException
	 * @throws BeforeDateNotBeforeClosingDate
	 * @throws OpeningDateAfterAtualDate
	 */
	public boolean createPosition(ManagerEntity manager, PositionEntity position, int onWeekOnms) throws ManagerNotIntroducedException, PhoneInterviewEntityNotIntroducedException, PresentialInterviewEntityNotIntroducedException, BeforeDateNotBeforeClosingDate, OpeningDateAfterAtualDate {
		boolean out=false;
		Logger.getLogger(PositionFacade.class.getName()).log(
				Level.INFO,
				"createPosition() --> Before 'if' Position = "
						+ position.getTitle()
						+ " and manager ="+manager.getEmail());
		if (position.getOpeningDate().after(new Date())) {
			if (position.getClosingDate().after(position.getOpeningDate())) {
				if (position.getManager() != null && position.getPhoneInterviewEntity() != null && position.getPresencialInterviewEntity() != null) {
					
					String to = manager.getEmail();
					long slagetTime = (long) (position.getClosingDate().getTime() - position.getOpeningDate().getTime());
					//get number of weeks for a position
					int sla = (int) (slagetTime / onWeekOnms);
					position.setSla(sla);
					Logger.getLogger(PositionFacade.class.getName()).log(
							Level.INFO,	"acreatingPosition() --> after 'if' and before creating Position = "
									+ position.getTitle()
									+ " and manager ="+manager.getEmail());
					create(position);
					//send email to new manager
					mail.sendEMail("acertarrumo2015@gmail.com", "Chosen as the manager of the position " + position.getTitle(), "PositionEntity " +position.getTitle()+" was created.", to);
					out = true;
				} else if (position.getManager() == null) {
					throw new ManagerNotIntroducedException();
				} else if (position.getPhoneInterviewEntity() == null) {
					throw new PhoneInterviewEntityNotIntroducedException();
				} else if (position.getPresencialInterviewEntity() == null) {
					throw new PresentialInterviewEntityNotIntroducedException();
				}
			} else {
				throw new BeforeDateNotBeforeClosingDate();
			}
		} else {
			throw new OpeningDateAfterAtualDate();
		}
		return out;
	}

	/**
	 * Edit a position
	 * @param position
	 * @throws ManagerNotIntroducedException
	 * @throws PhoneInterviewEntityNotIntroducedException
	 * @throws PresentialInterviewEntityNotIntroducedException
	 * @throws BeforeDateNotBeforeClosingDate
	 */
	public boolean editAndSavePosition(PositionEntity position) throws ManagerNotIntroducedException, PhoneInterviewEntityNotIntroducedException, PresentialInterviewEntityNotIntroducedException, BeforeDateNotBeforeClosingDate {
		boolean out=false;
		String to = position.getManager().getEmail();
		if (position.getClosingDate().after(position.getOpeningDate())) {
			if (position.getManager() != null && position.getPhoneInterviewEntity() != null && position.getPresencialInterviewEntity() != null) {
				edit(position);
				//send email to new manager
				mail.sendEMail("acertarrumo2015@gmail.com", "Chosen as the manager of the position " + position.getTitle(), "The positon " +position.getTitle()+" was edited", to);
				out = true;
			}
		} else if (position.getManager() == null) {
			throw new ManagerNotIntroducedException();
		} else if (position.getPhoneInterviewEntity() == null) {
			throw new PhoneInterviewEntityNotIntroducedException();
		} else if (position.getPresencialInterviewEntity() == null) {
			throw new PresentialInterviewEntityNotIntroducedException();
		} else {
			throw new BeforeDateNotBeforeClosingDate();
		}
		
		return out;
	}

	/**
	 *
	 * @param currentDate
	 * @return List of position where closing date is after atual date
	 */
	public List<PositionEntity> lstPositionAtualDateBeforeClosingDate(Date currentDate) {
		/*Date today=new Date();
		Date strToDate = null;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String DateToStr = format.format(today);
		System.out.println("\nToday's date="+today);
		String data="2015-09-04";
		System.out.println(" 2. " + DateFormat.getInstance().format(today));
		// Get the default MEDIUM/SHORT DateFormat
        DateFormat dateformat = DateFormat.getDateInstance(DateFormat.SHORT);
		 // Parse the date
        try {
            today = dateformat.parse(data);
            System.out.println("Original string: " + data);
            System.out.println("Parsed date    : " + 
                 today.toString());
        }
        catch(ParseException pe) {
            System.out.println("ERROR: could not parse date in string \"" +
                data + "\"");
        }
		
		System.out.println("\n Inside PositionFacade.lstPositionAtualDateBeforeClosingDate() with current string data="+data);
		
		try {
			// News date
			strToDate = format.parse(DateToStr);
			System.out.println("Parsing to Date --> strToDate="+strToDate);

		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("Exception inside PositionFacade.lstPositionAtualDateBeforeClosingDate() with message="+e.getMessage());
		}*/
		Logger.getLogger(PositionFacade.class.getName()).log(Level.INFO,"\n Inside PositionFacade.lstPositionAtualDateBeforeClosingDate() with current date ="+currentDate+"\n");
		//System.out.println("\n Inside PositionFacade.lstPositionAtualDateBeforeClosingDate() with current date ="+currentDate+"\n");
		Query query = em.createNamedQuery("PositionEntity.findPublicBeforeClosingDate", PositionEntity.class);
		query.setParameter("currentDate", currentDate);
		Logger.getLogger(PositionFacade.class.getName()).log(Level.INFO,"\nInside PositionFacade.lstPositionAtualDateBeforeClosingDate() where list.size ="+query.getResultList().size());
		//System.out.println("\nInside PositionFacade.lstPositionAtualDateBeforeClosingDate() where list.size ="+query.getResultList().size());
		return query.getResultList();
	}

	/**
	 *
	 * @param currentDate
	 * @return List of Public Positions which actual date is before closing date
	 */
	public List<PositionEntity> lstPositionPublicAtualDateBeforeClosingDate(Date currentDate) {
		Query query = em.createNamedQuery("PositionEntity.findPublicBeforeClosingDate", PositionEntity.class);
		query.setParameter("currentDate", currentDate);
		return query.getResultList();
	}

	/**
	 *
	 * @param applicant
	 * @return
	 */
	public List<PositionEntity> lstPositionThatApplicantCanApply(ApplicantEntity applicant) {
		//query to know which opened positions were not applied by applicant
		Date date = new Date();
		Long dayafter = date.getTime()+ 86400000;
		date= new Date(dayafter);
		Query query = em.createNamedQuery("PositionEntity.doNotApplied", PositionEntity.class);
		query.setParameter("applicantId", applicant.getUserId());
		query.setParameter("currentDate", date);
		List<PositionEntity> lstApplicantPosition = query.getResultList();
		return lstApplicantPosition;
	}

	/**
	 * Associate ApplicationEntity to a specific application
	 * @param selectedApplication
	 * @param selectedPosition
	 * @throws PositionNotIntroducedException
	 * @throws PositionOfAnApplicantAlreadyIntroducedOnSPonException
	 */
	public void associateApplicationToPosition(ApplicationEntity selectedApplication, PositionEntity selectedPosition) throws PositionNotIntroducedException, PositionOfAnApplicantAlreadyIntroducedOnSPonException {
		if (selectedPosition != null && applicantChooseAPositionAlreadySelectedOnNonSpontaneousApplication(selectedApplication.getApplicant(), selectedPosition)) {
			ApplicationEntity application = new ApplicationEntity();
			application.setApplicant(selectedApplication.getApplicant());
			application.setApplicationDate(new Date());
			application.setCoverLetter(selectedApplication.getCoverLetter());
			application.setCv(selectedApplication.getCv());
			application.setIsSpontaneous(false);
			application.setPosition(selectedPosition);
			application.setStatus(selectedApplication.getStatus());
			application.setApplicant(selectedApplication.getApplicant());
			application.setSource(selectedApplication.getSource());
			em.persist(application);
		} else if (selectedPosition == null) {
			throw new PositionNotIntroducedException();
		} else {
			throw new PositionOfAnApplicantAlreadyIntroducedOnSPonException();
		}

	}

	/**
	 *
	 * @param position
	 * @return true if a position could be associate to ApplicationEntity
	 */
	public boolean associateToApplication(PositionEntity position) {
		Query query=em.createNamedQuery("ApplicationEntity.findByPosition",ApplicationEntity.class);
		query.setParameter("position", position);
		return query.getResultList().isEmpty();
	}

	/**
	 *
	 * @param applicant
	 * @param position
	 * @return true if an applicant select a position that was already selected
	 */
	public boolean applicantChooseAPositionAlreadySelectedOnNonSpontaneousApplication(ApplicantEntity applicant, PositionEntity position) {
		Query query = em.createNamedQuery("ApplicationEntity.findByApplicantAndPositionAndNonSpontaneous", ApplicationEntity.class);
		query.setParameter("applicant", applicant);
		query.setParameter("position", position);
		return query.getResultList().isEmpty();
	}

	/**
	 *
	 * @param manager
	 * @return List of position of a manager
	 */
	public List<PositionEntity> lstPositionsOfManager(ManagerEntity manager) {
		Query query = em.createNamedQuery("PositionEntity.findByManager", PositionEntity.class);
		query.setParameter("manager", manager);
		return query.getResultList();
	}

	/**
	 *
	 * @param manager
	 * @return List of position of a manager
	 */
	public List<PositionEntity> lstPositionsOfManagerThatApplicantCanApply(ManagerEntity manager) {
		Query query = em.createNamedQuery("PositionEntity.findByManagerCanApply", PositionEntity.class);
		query.setParameter("manager", manager);
		query.setParameter("currentDate", new Date());
		return query.getResultList();
	}

	/**
	 *
	 * @param search
	 * @return List of Positions that matchs the search param
	 */
	public List<PositionEntity> searchPosition(String search) {
		String[] fields = {"p.OpeningDate", "p.positionCode", "p.title", "p.location", "p.status", "p.company", "p.technicalArea"};
		String[] words = search.split("\\s+");

		String searchExpression = "SELECT p FROM PositionEntity p WHERE ";
		for (int f = 0; f < fields.length; f++) {
			for (int w = 0; w < words.length; w++) {
				if (f > 0 || w > 0) {
					searchExpression += " OR ";
				}
				String tmp = words[w].replace("%", "\\%");
				searchExpression += fields[f] + " LIKE '%" + tmp + "%'";
			}
		}
		Query query = em.createQuery(searchExpression);
		try {
			List<PositionEntity> searchResults = query.getResultList();
			return searchResults;
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 *
	 * @return List of all Positions
	 */
	public List<PositionEntity> positions() {
		Query query = em.createNamedQuery("PositionEntity.findAll", PositionEntity.class);
		return query.getResultList();
	}


	/////////////////////Getters && Setters////////////////////

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public SendEmail getMail() {
		return mail;
	}

	public void setMail(SendEmail mail) {
		this.mail = mail;
	}

	public ApplicationFacade getApplicationFacade() {
		return applicationFacade;
	}

	public void setApplicationFacade(ApplicationFacade applicationFacade) {
		this.applicationFacade = applicationFacade;
	}

}
