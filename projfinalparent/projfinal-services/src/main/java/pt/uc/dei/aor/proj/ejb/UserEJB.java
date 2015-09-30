package pt.uc.dei.aor.proj.ejb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewQuestionEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.PositionEntity;
import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;
import pt.uc.dei.aor.proj.db.tools.AnswerType;
import pt.uc.dei.aor.proj.db.tools.InterviewType;

/**
 * Session Bean implementation class UserEJB
 */
@Stateless
public class UserEJB implements UserEJBLocal {

	private static final int ONEWEEKONMS = 604800000;

	@PersistenceContext(name = "myPU")
	private EntityManager em;

	private String datanasc;

	private String password;

	@EJB
	private PasswordEJB pw;
	// SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");

	// Number of logged users
	private static int userCount = 0;

	private static HashMap<UserEntity, Integer> loggedUsers = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(UserEJB.class);

	public UserEJB() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void populate() {
		int k = 0;

		password = pw.encrypt("123");
		UserEntity usertmp1 = new ManagerEntity("Carlos", "Santos", password,
				"carlos@gmail.com", "carlos@gmail.com"); // pass 123
		usertmp1.setRole(Role.MANAGER);
		em.persist(usertmp1);
		password = pw.encrypt("acertar2015");
		UserEntity usertmp4 = new ManagerEntity("manager2", "mng2ln", password,
				"acertarrumo2015@gmail.com", "acertarrumo2015@gmail.com"); // pass
																			// acertar2015
		usertmp4.setRole(Role.MANAGER);
		// usertmp1.setRoles(Role.INTERVIEWER);

		em.persist(usertmp4);
		System.out.println("Criou user " + usertmp1.getEmail()
				+ " e sizeRoles= " + usertmp1.getRoles().size());
		password = pw.encrypt("456");
		UserEntity usertmp2 = new InterviewerEntity("Catarina", "Lapo",
				password, "ciclapo@gmail.com", "ciclapo@gmail.com"); // pass 456
		usertmp2.setRole(Role.INTERVIEWER);
		em.persist(usertmp2);
		password = pw.encrypt("interv2");
		UserEntity usertmp5 = new InterviewerEntity("interv2", "intv2ln",
				password, "interv2@gmail.com", "interv2@gmail.com"); // pass
																		// interv2
		usertmp5.setRole(Role.INTERVIEWER);
		// usertmp2.setRoles(Role.INTERVIEWER);
		em.persist(usertmp5);
		System.out.println("Criou user " + usertmp2.getEmail()
				+ " e sizeRoles= " + usertmp2.getRoles().size());
		password = pw.encrypt("admin");
		UserEntity usertmp3 = new AdminEntity("Admin", "admin", password,
				"admin@admin", "admin@admin");
		usertmp3.setRole(Role.ADMIN); // pass admin

		em.persist(usertmp3);
		password = pw.encrypt("caje08");
		UserEntity usertmp6 = new AdminEntity("adm2", "adm2", password,
				"caje08@gmail.com", "caje08@gmail.com");
		usertmp6.setRole(Role.ADMIN); // pass adm2

		em.persist(usertmp6);
		System.out.println("Criou user " + usertmp3.getEmail()
				+ " e sizeRoles= " + usertmp3.getRoles().size());

		for (k = 0; k < 10; k++) {
			if (k % 2 == 0) {
				createInterviewsGuides(k + 1, usertmp1);
			} else {
				createInterviewsGuides(k + 1, usertmp4);
			}

//			if (k + 2 < 10) {
//				k = k + 2;
//			} else {
//				k = 10;
//			}
		}
	}

	public void createPositionsToDB(int refposition,
			UserEntity selectedManager, InterviewEntity phoneInterviewEntity,
			InterviewEntity presencialInterviewEntity, int onWeekOnms) {
		PositionEntity newPosition = new PositionEntity();
		Date openingDate = null; // pattern="MM/dd/yyyy HH:mm"
		Date closingDate = null;
		Random rd = new Random();
		int sla, dia;
		int mestoclose = 10 + rd.nextInt(3);
		int diatoclose = 10 + rd.nextInt(15);
		String title = "Position_" + refposition;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		if (userCount + 1 == 1 || (userCount + 1) % 2 == 0) {
			if ((userCount + 1) % 2 == 0) {
				dia = refposition + 10 - 1;
			} else {
				dia = refposition + 10;
			}
			userCount++;
		} else {
			dia = refposition + 10;
			userCount = 0;
		}
		String openingdateInString = "09/" + dia + "/2015 " + dia + ":20:56";
		String closingdateInString = mestoclose + "/" + diatoclose
				+ "/2015 10:20:56";

		try {
			openingDate = sdf.parse(openingdateInString);
			closingDate = sdf.parse(closingdateInString);
		} catch (ParseException e) {
			System.out.println("Error in generating Date()" + e.getMessage());
		}
		System.out.println("OpeningDate=" + openingDate + " and closingDate="
				+ closingDate); //

		newPosition.setOpeningDate(openingDate);
		newPosition.setClosingDate(closingDate);
		newPosition.setTitle(title);
		newPosition.setCompany("CRITICAL SW");
		newPosition.setIsPublic(true);
		newPosition.setJobDescription("job description for position"
				+ refposition);
		newPosition.setStatus("OPEN");
		if (refposition % 10 + 1 == 1) {
			newPosition.setLocation("COIMBRA");
			newPosition.setVacancies(1);
			newPosition.setPublishingChannels("CRITICALSWWEBSITE,LINKEDIN");
		} else if (refposition % 10 + 1 == 2) {
			newPosition.setLocation("LISBOA_OPORTO");
			newPosition.setVacancies(2);
			newPosition.setPublishingChannels("FACEBOOK");
		} else if (refposition % 10 + 1 == 3) {
			newPosition.setLocation("LISBOA_OPORTO");
			newPosition.setVacancies(2);
			newPosition.setPublishingChannels("CRITICALSWWEBSITE,LINKEDIN");
		} else if (refposition % 10 + 1 == 4) {
			newPosition.setLocation("COIMBRA_LISBON_OPORTO");
			newPosition.setVacancies(3);
			newPosition.setPublishingChannels("FACEBOOK");
		} else if (refposition % 10 + 1 == 5) {
			newPosition.setLocation("LISBOA_OPORTO");
			newPosition.setVacancies(2);
			newPosition.setPublishingChannels("GLASSDOOR");
		} else if (refposition % 10 + 1 == 6) {
			newPosition.setLocation("CLIENT");
			newPosition.setVacancies(1);
		} else if (refposition % 10 + 1 == 7) {
			newPosition.setLocation("COIMBRA_LISBON");
			newPosition.setVacancies(2);
			newPosition.setPublishingChannels("CRITICALSWWEBSITE,LINKEDIN");
		} else if (refposition % 10 + 1 == 8) {
			newPosition.setLocation("COIMBRA_OPORTO");
			newPosition.setVacancies(2);
			newPosition.setPublishingChannels("FACEBOOK");
		} else if (refposition % 10 + 1 == 9) {
			newPosition.setLocation("OPORTO");
			newPosition.setVacancies(1);
			newPosition.setPublishingChannels("GLASSDOOR");
		} else if (refposition % 10 + 1 == 10) {
			newPosition.setLocation("LISBON");
			newPosition.setVacancies(1);
			newPosition.setPublishingChannels("FACEBOOK");
		}

		if (refposition % 6 == 0) {

			newPosition.setTechnicalArea("SSPA");
		} else if (refposition % 6 == 1) {
			newPosition.setTechnicalArea("DOTNETDEVELOPMENT");
		} else if (refposition % 6 == 2) {
			newPosition.setTechnicalArea("JAVADEVELOPMENT");
		} else if (refposition % 6 == 3) {
			newPosition.setTechnicalArea("SAFETYCRITICAL");
		} else if (refposition % 6 == 4) {
			newPosition.setTechnicalArea("PROJECTMANAGEMENT");
		} else if (refposition % 6 == 5) {
			newPosition.setTechnicalArea("INTEGRATION");
		}

		long slagetTime = (long) (newPosition.getClosingDate().getTime() - newPosition
				.getOpeningDate().getTime());
		// get number of weeks for a position
		sla = (int) (slagetTime / onWeekOnms);
		newPosition.setSla(sla);
		newPosition.setManager(selectedManager);
		newPosition.setPhoneInterviewEntity(phoneInterviewEntity);
		newPosition.setPresencialInterviewEntity(presencialInterviewEntity);

		em.persist(newPosition);
	}

	public void createInterviewsGuides(int k, UserEntity manager) {
		String guideName = "", questionName = "";
		InterviewType guideType = null;
		AnswerType answerType = null;
		InterviewEntity tmpinterviewGuide = new InterviewEntity();

		for (int i = k - 1; i < k + 1; i++) {
			InterviewEntity interviewGuide = new InterviewEntity();
			interviewGuide.setCreationDate(new Date());

			if (i % 2 == 0) {
				interviewGuide.setType(guideType.PHONE);
				guideName = "PHONEGuide" + i;
			} else {
				interviewGuide.setType(guideType.PRESENTIAL);
				guideName = "PRESGuide" + i;
			}
			interviewGuide.setTitle(guideName);
			interviewGuide.setInUse(Boolean.TRUE);
			em.persist(interviewGuide);
			for (int j = 0; j < 4; j++) {
				InterviewQuestionEntity interviewQuestion = new InterviewQuestionEntity();
				if (i % 2 == 0) {
					questionName = "quest" + j + "_Phoneguide" + i;
				} else {
					questionName = "Quest" + j + "_PRESguide" + i;
				}
				interviewQuestion.setQuestion(questionName);
				interviewQuestion.setInterviewGuide(interviewGuide);
				if (j % 3 == 0) {
					answerType = answerType.BOOLEAN;
				} else if (j % 3 == 1) {
					answerType = answerType.INTEGER;
				} else if (j % 3 == 2) {
					answerType = answerType.TEXT;
				}
				interviewQuestion.setAnswerType(answerType);
				// int questionNum = j;
				// give to interview question the last interview question plus 1
				interviewQuestion.setQuestionNumber(j + 1);
				em.persist(interviewQuestion);

			}
			if (i + 1 < k + 1) {
				tmpinterviewGuide = interviewGuide;
			} else {
				createPositionsToDB(k, manager, tmpinterviewGuide,
						interviewGuide, ONEWEEKONMS);
			}
		}

	}

	@Override
	public List<UserEntity> getUsers() {

		logger.info("UserEJB.getUsers() --> before creating the query");

		Query q = em.createQuery("from UserEntity u");
		List<UserEntity> users = q.getResultList();
		logger.info("UserEJB.getUsers() --> after creating the query to get users:"
				+ users);

		return users;
	}

	public UserEntity getUserFromDBbyEmail(String email) {

		UserEntity tmpuser = null;
		Query q = em.createQuery("from UserEntity u where u.email= :" + email);
		tmpuser = (UserEntity) q.getSingleResult();
		return tmpuser;
	}

	@Override
	public UserEntity findByEmail(String email) {
		TypedQuery<UserEntity> q = em.createNamedQuery(
				"UserEntity.findByEmail", UserEntity.class);
		q.setParameter("email", email);
		try {
			return q.getSingleResult();
		} catch (Exception e) {
			System.err.println("Single result not found: " + e);
			return null;
		}
	}

	@Override
	public Collection<Role> getUserListOfRoles(String email) {
		UserEntity tmpuser;
		Collection<Role> roles;
		TypedQuery<UserEntity> q = em.createNamedQuery(
				"UserEntity.findByEmail", UserEntity.class);
		q.setParameter("email", email);
		try {
			tmpuser = q.getSingleResult();
		} catch (Exception e) {
			System.err.println("Single result not found: " + e);
			return null;
		}
		roles = tmpuser.getRoles();
		return roles;
	}

	@Override
	public UserEntity findById(long id) {
		TypedQuery<UserEntity> q = em.createNamedQuery("UserEntity.findById",
				UserEntity.class);
		q.setParameter("userId", id);
		try {
			return q.getSingleResult();
		} catch (Exception e) {
			System.err.println("Single result not found: " + e);
			return null;
		}
	}

	public UserEntity findByName(String username) {
		TypedQuery<UserEntity> q = em.createNamedQuery("UserEntity.findByName",
				UserEntity.class);
		q.setParameter("username", username);
		try {
			return q.getSingleResult();
		} catch (Exception e) {
			System.err.println("Single result not found: " + e);
			return null;
		}
	}

	public static void increaseUserCount(UserEntity user) {
		if (loggedUsers.containsKey(user)) {
			int moreSession = loggedUsers.get(user) + 1;
			loggedUsers.put(user, moreSession);
		} else {
			loggedUsers.put(user, 1);
		}

		userCount = loggedUsers.size();

	}

	public static void decreaseUserCount(UserEntity user) {
		if (loggedUsers.get(user) == 1) {
			loggedUsers.remove(user);
		} else {
			int lessSession = loggedUsers.get(user) - 1;
			loggedUsers.put(user, lessSession);
		}

		userCount = loggedUsers.size();
	}

	public static int getUserCount() {
		return userCount;
	}

	public static HashMap<UserEntity, Integer> getLoggedUsers() {
		return loggedUsers;
	}

	@Override
	public void populateCandidates() {
		System.out.println("Em UserEJB.populateCandidates() vai criar user1");
		password = pw.encrypt("123");
		System.out.println("\nPassw =" + password);
		UserEntity usertmp1 = new UserEntity("user1pub name", "user1 publast",
				password, "user1pub@gmail.com", "user1pub usernam"); // pass 123
		System.out.println("Vai colocar  usertmp1.setRole(Role.USERPUBLIC)");
		usertmp1.setRole(Role.CANDIDATE);
		usertmp1.initArray();
		// usertmp1.setRoles(Role.INTERVIEWER);
		System.out.println("Vai persistir usertmp1");
		em.persist(usertmp1);
		System.out.println("Criou user " + usertmp1.getEmail()
				+ " e sizeRoles= " + usertmp1.getRoles().size());
		password = pw.encrypt("456");
		UserEntity usertmp2 = new UserEntity("user2pub name", "user2 publast",
				password, "user2pub@gmail.com", "user2pub usernam"); // pass 456
		usertmp2.setRole(Role.CANDIDATE);
		usertmp2.initArray();
		// usertmp2.setRoles(Role.INTERVIEWER);
		em.persist(usertmp2);
		System.out.println("Criou user " + usertmp2.getEmail()
				+ " e sizeRoles= " + usertmp2.getRoles().size());
		password = pw.encrypt("789");
		UserEntity usertmp3 = new UserEntity("user3pub name", "user3 publast",
				password, "user3pub@gmail.com", "user3pub usernam");
		usertmp3.setRole(Role.CANDIDATE); // pass 789
		usertmp3.initArray();
		// usertmp3.setRoles(Role.MANAGER);
		// usertmp3.setRoles(Role.INTERVIEWER);
		em.persist(usertmp3);
		System.out.println("Criou user " + usertmp3.getEmail()
				+ " e sizeRoles= " + usertmp3.getRoles().size());

	}

	@Override
	public List<UserEntity> getCandidateUsers() {
		logger.info("UserEJB.getCandidateUsers() --> before creating the query");

		Query q = em
				.createQuery("from UserEntity u where u.role LIKE 'CANDIDATE' or u.role LIKE 'USERPUBLIC'");
		List<UserEntity> users = q.getResultList();
		logger.info("UserEJB.getUsers() --> after creating the query to get users:"
				+ users);

		return users;

	}

}
