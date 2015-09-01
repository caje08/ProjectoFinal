/*
 *
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.GroupsEntity;
import pt.uc.dei.aor.proj.db.entities.GroupsHasUserEntity;
import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;
import pt.uc.dei.aor.proj.db.entities.ManagerEntity;
import pt.uc.dei.aor.proj.db.exceptions.InvalidAuthException;

/**
 * @author
 */
@Stateless
public class AdminFacade extends AbstractFacade<AdminEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@Resource
	SessionContext ctx;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public AdminFacade() {
		super(AdminEntity.class);
	}

	/**
	 * Persist a manager in GroupsHasUserguide entity
	 * @param m
	 * @throws InvalidAuthException
	 */
	public void createManager(ManagerEntity m) throws InvalidAuthException {
		em.persist(m);
		GroupsHasUserEntity groupsHasUserGuide = new GroupsHasUserEntity();
		groupsHasUserGuide.setUsername(m.getUsername());
		groupsHasUserGuide.setUserGuide(m);
		groupsHasUserGuide.setGroups(findGroupByName("manager"));
		em.persist(groupsHasUserGuide);
	}

	/**
	 * Persist new InterviewerEntity in GroupsHasUserguide entity
	 *
	 * @param i
	 * @throws InvalidAuthException
	 */
	public void createInterviewer(InterviewerEntity i) throws InvalidAuthException {
		em.persist(i);
		GroupsHasUserEntity groupsHasUserGuide = new GroupsHasUserEntity();
		groupsHasUserGuide.setUsername(i.getUsername());
		groupsHasUserGuide.setUserGuide(i);
		groupsHasUserGuide.setGroups(findGroupByName("interviewer"));
		em.persist(groupsHasUserGuide);
	}

	/**
	 * Persist new Admin in GroupsHasUserguide entity
	 *
	 * @param a
	 * @throws InvalidAuthException
	 */
	public void createAdmin(AdminEntity a) throws InvalidAuthException {
		em.persist(a);
		GroupsHasUserEntity groupsHasUserGuide = new GroupsHasUserEntity();
		groupsHasUserGuide.setUsername(a.getUsername());
		groupsHasUserGuide.setUserGuide(a);
		groupsHasUserGuide.setGroups(findGroupByName("adminguide"));
		em.persist(groupsHasUserGuide);
	}

	/**
	 *
	 * @param groupname
	 * @return Group corresponding to groupname introduced
	 * @throws InvalidAuthException
	 */
	public GroupsEntity findGroupByName(String groupname) throws InvalidAuthException {
		try {
			Query query = em.createNamedQuery("GroupsEntity.findByGroupName", GroupsEntity.class);
			query.setParameter("groupname", groupname);
			return (GroupsEntity) query.getSingleResult();

		} catch (NoResultException ex) {
			Logger.getLogger(AdminEntity.class.getName()).log(Level.SEVERE, "Error finding group", ex);
			throw new InvalidAuthException();
		}
	}
}
