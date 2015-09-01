/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pt.uc.dei.aor.proj.db.entities.ManagerEntity;

/**
 * @author
 */
@Stateless
public class ManagerFacade extends AbstractFacade<ManagerEntity> {
	@PersistenceContext(unitName = "myPU")
	private EntityManager em;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public ManagerFacade() {
		super(ManagerEntity.class);
	}

}
