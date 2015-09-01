/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pt.uc.dei.aor.proj.db.entities.InterviewerEntity;

/**
 * @author
 */
@Stateless
public class InterviewerFacade extends AbstractFacade<InterviewerEntity> {
	@PersistenceContext(unitName = "myPU")
	private EntityManager em;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}


	public InterviewerFacade() {
		super(InterviewerEntity.class);
	}




}
