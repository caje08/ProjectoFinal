/*
 */
package pt.uc.dei.aor.proj.tools;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.CSS;
import pt.uc.dei.aor.proj.serv.exceptions.CSSInUseDoesNotExistsException;
import pt.uc.dei.aor.proj.serv.facade.CSSFacade;

/**
 * @author
 */
@RequestScoped
@Named("CheckCssInUse")
public class CheckCssInUse {

	@EJB
	private CSSFacade cSSFacade;

	/**
	 * @return CSS in Use
	 * @throws CSSInUseDoesNotExistsException 
	 */
	public CSS getCssInUse() throws CSSInUseDoesNotExistsException, Exception{
		return cSSFacade.getCSSInUse();
	}

	public CSSFacade getcSSFacade() {
		return cSSFacade;
	}

	public void setcSSFacade(CSSFacade cSSFacade) {
		this.cSSFacade = cSSFacade;
	}
}
