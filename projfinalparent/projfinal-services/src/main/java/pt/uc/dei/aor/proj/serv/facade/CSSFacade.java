/*
 */
package pt.uc.dei.aor.proj.serv.facade;

import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import pt.uc.dei.aor.proj.db.entities.CSS;
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
	 */
	public void editInUseCss(CSS css) {
		getCSSInUse().setInUse(false);
		css.setInUse(true);
		edit(css);
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
				css.setLogoPath("logo1.png");
			}
			css.setInUse(false);
			css.setName(cssIntroduced.getName());
			css.setPhone(cssIntroduced.getPhone());
			css.setLatitudeCoordinate(cssIntroduced.getLatitudeCoordinate());
			css.setLongitudeCoordinate(cssIntroduced.getLongitudeCoordinate());
			css.setCreationDate(new Date());
			css.setBackgroundButtonColor(cssIntroduced.getBackgroundButtonColor());
			css.setBackgroundApplicantContentTemplateColor(cssIntroduced.getBackgroundApplicantContentTemplateColor());
			css.setBackgroundBottomApplicantColor(cssIntroduced.getBackgroundBottomApplicantColor());
			css.setBackgroundPlatformContentTemplateColor(cssIntroduced.getBackgroundPlatformContentTemplateColor());
			css.setBackgroundTopApplicantColor(cssIntroduced.getBackgroundTopApplicantColor());
			css.setBackgroundTopPlatformTemplateColor(cssIntroduced.getBackgroundTopPlatformTemplateColor());
			css.setLabelApplicantTemplateColor(cssIntroduced.getLabelApplicantTemplateColor());
			css.setLabelCommandLink(cssIntroduced.getLabelCommandLink());
			css.setLabelPlatformTemplateColor(cssIntroduced.getLabelPlatformTemplateColor());
			css.setLabelButtonColor(cssIntroduced.getLabelButtonColor());

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
	 */
	public CSS getCSSInUse() {
		TypedQuery<CSS> typed = em.createQuery(
				"SELECT c FROM CSS c WHERE c.inUse=true", CSS.class);
		return typed.getSingleResult();
	}

}
