/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.uc.dei.aor.proj.db.entities.AdminEntity;
import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.entities.GroupsEntity;
import pt.uc.dei.aor.proj.db.entities.GroupsHasUserEntity;
import pt.uc.dei.aor.proj.db.exceptions.InvalidAuthException;

/**
 * @author
 */
@Stateless
public class GroupsHasUserGuideFacade extends AbstractFacade<GroupsHasUserEntity> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public GroupsHasUserGuideFacade() {
		super(GroupsHasUserEntity.class);
	}

	/**
	 * Persist applicant on GroupsHasUserEntity
	 * @param applicant
	 * @throws InvalidAuthException
	 */
	public void persistApplicant(ApplicantEntity applicant) throws InvalidAuthException {
		GroupsHasUserEntity groupsHasUserGuide = new GroupsHasUserEntity();
		groupsHasUserGuide.setUsername(applicant.getUsername());
		groupsHasUserGuide.setUserGuide(applicant);
		groupsHasUserGuide.setGroups(findGroupByName("applicant"));
		em.persist(groupsHasUserGuide);

	}

	/**
	 *
	 * @param groupname
	 * @return group find by groupname inserted
	 * @throws InvalidAuthException
	 */
	public GroupsEntity findGroupByName(String groupname) throws InvalidAuthException {
		try {
			Query query = em.createNamedQuery("GroupsEntity.findByGroupName", GroupsEntity.class);
			query.setParameter("groupname", groupname);
			return  (GroupsEntity) query.getSingleResult();

		} catch (NoResultException ex) {
			Logger.getLogger(AdminEntity.class.getName()).log(Level.SEVERE, "Error finding group", ex);
			throw new InvalidAuthException();
		}
	}

}
