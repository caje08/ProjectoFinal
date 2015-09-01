/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pt.uc.dei.aor.proj.db.entities.GroupsEntity;

/**
 * @author
 */
@Stateless
public class GroupsFacade extends AbstractFacade<GroupsEntity> {
	@PersistenceContext(unitName = "myPU")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public GroupsFacade() {
		super(GroupsEntity.class);
	}

	public void createInitialGroups(){
		GroupsEntity newgroup1 = new GroupsEntity();
		newgroup1.setGroupname("admin");
		em.persist(newgroup1);
		GroupsEntity newgroup2 = new GroupsEntity();
		newgroup2.setGroupname("manager");
		em.persist(newgroup2);
		GroupsEntity newgroup3 = new GroupsEntity();
		newgroup3.setGroupname("interviewer");
		em.persist(newgroup3);
		GroupsEntity newgroup4 = new GroupsEntity();
		newgroup4.setGroupname("applicant");
		em.persist(newgroup4);
	}
}
