/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import pt.uc.dei.aor.proj.db.entities.CSS;
import pt.uc.dei.aor.proj.serv.exceptions.CSSInUseDoesNotExistsException;
import pt.uc.dei.aor.proj.serv.exceptions.CSSNameAlreadyExistsException;

/**
 * @author
 */
@Stateless
public class CSSFacade extends AbstractFacade<CSS> {

	@PersistenceContext(unitName = "myPU")
	private EntityManager em;


	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public CSSFacade() {
		super(CSS.class);
	}

	/**
	 * Change css in use to other css chosen
	 * @param css
	 * @throws CSSInUseDoesNotExistsException 
	 */
	public void editInUseCss(CSS css){
		try {
			getCSSInUse().setInUse(false);
		} catch (CSSInUseDoesNotExistsException e) {
			Logger.getLogger(CSSFacade.class.getName()).log(Level.SEVERE,"Inside editInUseCss() with errors",e.getMessage());			
		}
		css.setInUse(true);
		edit(css);
		Logger.getLogger(CSSFacade.class.getName()).log(Level.INFO,"Inside editInUseCss() after updating CSS to css="+css.getEmail()+" and css.InUse="+css.isInUse());
	}

	/**
	 *
	 * @param cssIntroduced
	 * @throws EJBException
	 * @throws CSSNameAlreadyExistsException
	 */
	public void createCss(CSS cssIntroduced) throws EJBException, CSSNameAlreadyExistsException {
		if (!cssAlreadyExistsWithThisName(cssIntroduced)) {
			CSS css = new CSS();
			css.setAddress(cssIntroduced.getAddress());
			css.setCompanyDescription(cssIntroduced.getCompanyDescription());
			css.setEmail(cssIntroduced.getEmail());
			css.setFax(cssIntroduced.getFax());
			if (cssIntroduced.getLogoPath() != null) {
				css.setLogoPath(cssIntroduced.getLogoPath());
			} else {
				css.setLogoPath("critSW.png");
			}
			css.setInUse(false);
			css.setName(cssIntroduced.getName());
			css.setPhone(cssIntroduced.getPhone());
			css.setLatitudeCoordinate(cssIntroduced.getLatitudeCoordinate());
			css.setLongitudeCoordinate(cssIntroduced.getLongitudeCoordinate());
			css.setCreationDate(new Date());
			css.setLabelTemplateCandidateColor(cssIntroduced.getLabelTemplateCandidateColor());
			css.setLabelCommandLink(cssIntroduced.getLabelCommandLink());
			css.setLabelInternalWebTemplateColor(cssIntroduced.getLabelInternalWebTemplateColor());
			css.setLabelColorButton(cssIntroduced.getLabelColorButton());
			css.setBackgroundInternalWebContentTemplateColor(cssIntroduced.getBackgroundInternalWebContentTemplateColor());
			css.setBackgroundCandidateTopColor(cssIntroduced.getBackgroundCandidateTopColor());
			css.setBackgroundInternalWebTopTemplateColor(cssIntroduced.getBackgroundInternalWebTopTemplateColor());
			css.setBackgroundColorButton(cssIntroduced.getBackgroundColorButton());
			css.setBackgroundContentCandidateTemplateColor(cssIntroduced.getBackgroundContentCandidateTemplateColor());
			css.setBackgroundCandidateBottomColor(cssIntroduced.getBackgroundCandidateBottomColor());
			
			create(css);
		} else {
			throw new CSSNameAlreadyExistsException();
		}

	}

	/**
	 *
	 * @param css
	 * @return true is there is a Css with same name as the Css introduced
	 */
	public boolean cssAlreadyExistsWithThisName(CSS css) {
		Query query = em.createNamedQuery("Css.findByName", CSS.class);
		query.setParameter("name", css.getName());
		return !query.getResultList().isEmpty();
	}

	/**
	 *
	 * @param css
	 * @return true if can edit css
	 */
	public boolean canEditCss(CSS css) {
		return !css.isInUse() && !css.getName().equals("Coimbra") && !css.getName().equals("Porto") && !css.getName().equals("Lisboa");
	}

	/**
	 *
	 * @return css in use
	 * @throws CSSInUseDoesNotExistsException 
	 */
	public CSS getCSSInUse() throws CSSInUseDoesNotExistsException {
		CSS out = null;
		List results = null;
		try{
		TypedQuery<CSS> typed = em.createQuery(
				"SELECT c FROM CSS c WHERE c.inUse=true", CSS.class);
		results = typed.getResultList();
		out= typed.getSingleResult();
		}catch (Exception e){
			Logger.getLogger(CSSFacade.class.getName()).log(Level.SEVERE,"Inside getCSSInUse() with errors in the typed query result",e.getMessage());
			throw new CSSInUseDoesNotExistsException();
		}
		if(results.isEmpty()){
			Logger.getLogger(CSSFacade.class.getName()).log(Level.SEVERE,"Inside getCSSInUse() with empty results list");
			throw new CSSInUseDoesNotExistsException();
		} else{
			return out; 
		}		
	}

}
