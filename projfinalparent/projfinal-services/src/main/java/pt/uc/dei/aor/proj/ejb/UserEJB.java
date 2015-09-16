package pt.uc.dei.aor.proj.ejb;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.entities.Role;
import pt.uc.dei.aor.proj.db.entities.UserEntity;

/**
 * Session Bean implementation class UserEJB
 */
@Stateless
public class UserEJB implements UserEJBLocal {

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

	/*	UserEntity usertmp1 = new UserEntity("Carlos",
				"pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=",
				"carlos@gmail.com", "1970/06/13", Role.MANAGER); // pass 123
		System.out.println("Criou user " + usertmp1.getEmail()
				+ " e sizeRoles= " + usertmp1.getRoles().size());
		usertmp1.setRoles(Role.MANAGER);
		em.persist(usertmp1);
		UserEntity usertmp2 = new UserEntity("Catarina",
				"s6jg4fmrG/46NvIx9nb3i7MKUZ0rIebFMMDu6Ou0pdA=",
				"ciclapo@gmail.com", "1985/10/21", Role.INTERVIEWER); // pass
																		// 456
		System.out.println("Criou user " + usertmp2.getEmail()
				+ " e sizeRoles= " + usertmp2.getRoles().size());
		usertmp2.setRoles(Role.INTERVIEWER);
		em.persist(usertmp2);
		UserEntity usertmp3 = new UserEntity("Admin",
				"jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=", "admin@admin",
				"1985/10/21", Role.ADMIN); // pass admin
		System.out.println("Criou user " + usertmp3.getEmail()
				+ " e sizeRoles= " + usertmp3.getRoles().size());
		usertmp3.setRoles(Role.ADMIN);
		em.persist(usertmp3);*/
		// datanasc = "1970/06/13";
		// em.persist(new UserEntity("Carlos", "123", "carlosantos@gmail.com",
		// datanasc));


		password =pw.encrypt("123");
		UserEntity usertmp1= new ManagerEntity("Carlos", "Santos",password, "carlos@gmail.com",
				"carlos@gmail.com"); //pass 123
		usertmp1.setRole(Role.MANAGER);
		//usertmp1.setRoles(Role.INTERVIEWER);


		em.persist(usertmp1);
		System.out.println("Criou user "+usertmp1.getEmail()+" e sizeRoles= "+usertmp1.getRoles().size());
		password =pw.encrypt("456");
		UserEntity	usertmp2 = new InterviewerEntity("Catarina", "Lapo", password, "ciclapo@gmail.com",
				"ciclapo@gmail.com"); //pass 456
		usertmp2.setRole(Role.INTERVIEWER);
		//usertmp2.setRoles(Role.INTERVIEWER);
		em.persist(usertmp2);
		System.out.println("Criou user "+usertmp2.getEmail()+" e sizeRoles= "+usertmp2.getRoles().size());
		password =pw.encrypt("admin");
		UserEntity	usertmp3 = new AdminEntity("Admin","admin", password, "admin@admin",
				"admin@admin");
		usertmp3.setRole(Role.ADMIN); //pass admin

//		usertmp3.setRoles(Role.MANAGER);
//		usertmp3.setRoles(Role.INTERVIEWER);

		em.persist(usertmp3);
		System.out.println("Criou user "+usertmp3.getEmail()+" e sizeRoles= "+usertmp3.getRoles().size());

		//		UserEntity usertmp1= new UserEntity("Carlos", "pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=", "carlos@gmail.com",
		//				"1970/06/13", Role.MANAGER); //pass 123
		//		System.out.println("Criou user "+usertmp1.getEmail()+" e sizeRoles= "+usertmp1.getRoles().size());
		//		usertmp1.setRoles(Role.MANAGER);
		//		em.persist(usertmp1);
		//		UserEntity	usertmp2 = new UserEntity("Catarina", "s6jg4fmrG/46NvIx9nb3i7MKUZ0rIebFMMDu6Ou0pdA=", "ciclapo@gmail.com",
		//				"1985/10/21", Role.INTERVIEWER); //pass 456
		//		System.out.println("Criou user "+usertmp2.getEmail()+" e sizeRoles= "+usertmp2.getRoles().size());
		//		usertmp2.setRoles(Role.INTERVIEWER);
		//		em.persist(usertmp2);
		//		UserEntity	usertmp3 = new UserEntity("Admin", "jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=", "admin@admin",
		//				"1985/10/21", Role.ADMIN); //pass admin
		//		System.out.println("Criou user "+usertmp3.getEmail()+" e sizeRoles= "+usertmp3.getRoles().size());
		//		usertmp3.setRoles(Role.ADMIN);
		//		em.persist(usertmp3);
		//		datanasc = "1970/06/13";
		//		em.persist(new UserEntity("Carlos", "123", "carlosantos@gmail.com",
		//				datanasc));

		//
		// datanasc = "1985/10/21";
		// em.persist(new UserEntity("Duarte", "456", "duarte@gmail.com",
		// datanasc));
	}

	@Override
	public List<UserEntity> getUsers() {

		logger.info("Sample Antes info message");

		System.out.println("Antes de criar a query");

		Query q = em.createQuery("from UserEntity u");
		List<UserEntity> users = q.getResultList();

		System.out.println(users);

		System.out.println("Depois de apresentar os resultados");

		logger.info("Sample Depois info message");

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
		TypedQuery<UserEntity> q = em.createNamedQuery("UserEntity.findByEmail",
				UserEntity.class);
		q.setParameter("email", email);
		try {
			return q.getSingleResult();
		} catch (Exception e) {
			System.err.println("Single result not found: " + e);
			return null;
		}
	}	
	
	@Override
	public Collection<Role> getUserListOfRoles(String email){
		UserEntity tmpuser;
		Collection<Role> roles;
		TypedQuery<UserEntity> q = em.createNamedQuery("UserEntity.findByEmail",
				UserEntity.class);
		q.setParameter("email", email);
		try {
			tmpuser = q.getSingleResult();
		} catch (Exception e) {
			System.err.println("Single result not found: " + e);
			return null;
		}
		roles=tmpuser.getRoles();	
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

}
