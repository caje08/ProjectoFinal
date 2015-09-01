/*
 *
 */
package pt.uc.dei.aor.proj.serv.tools;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import pt.uc.dei.aor.proj.db.entities.ApplicationEntity;

/**
 *
 * @author Carlos + Catarina
 */
@SessionScoped
@Stateful
public class StatefulApplication {

	private ApplicationEntity application;
	/**
	 * Creates a new instance of StatefulApplication
	 */
	public StatefulApplication() {
	}

	public ApplicationEntity getApplication() {
		return application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

}
