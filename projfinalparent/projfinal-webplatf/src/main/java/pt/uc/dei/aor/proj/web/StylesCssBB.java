/*
 */
package pt.uc.dei.aor.proj.web;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;

import pt.uc.dei.aor.proj.db.entities.CSS;
import pt.uc.dei.aor.proj.serv.exceptions.CSSNameAlreadyExistsException;
import pt.uc.dei.aor.proj.serv.facade.CSSFacade;
import pt.uc.dei.aor.proj.serv.tools.JSFUtil;
import pt.uc.dei.aor.proj.serv.tools.UploadedFiles;

/**
 *
 * @author
 */
@Named("stylesCss")
@ViewScoped
public class StylesCssBB implements Serializable {

	private List<CSS> lstCSS;

	private CSS css;
	private CSS selectedCSS;
	private String color;

	@EJB
	private CSSFacade cSSFacade;
	private UploadedFiles uploadedFiles;

	/**
	 * Creates a new instance of CSSViewBB
	 */
	public StylesCssBB() {
	}

	@PostConstruct
	public void init() {
		this.css = new CSS();
		this.uploadedFiles = new UploadedFiles();
	}

	/**
	 * Upload logo, displaying an message saying that logo was uploaded with
	 * success
	 *
	 * @param event
	 */
	public void uploadLogo(FileUploadEvent event) {
		uploadedFiles.upload(event, "logo");
		JSFUtil.addSuccessMessage("Success!! " + event.getFile().getFileName() + " was uploaded.");
	}

	public String createCSS() {
		try {
			cSSFacade.createCss(css);
			return "css.xhtml?faces-redirect=true";
		} catch (EJBException ex) {
			Logger.getLogger(StylesCssBB.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage("Error creating Css");
			return null;
		} catch (CSSNameAlreadyExistsException ex) {
			Logger.getLogger(StylesCssBB.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage(ex.getMessage());
			return null;
		}
	}

	public void removeCSS(){
		cSSFacade.remove(selectedCSS);
	}

	public void editInUseCss(){
		cSSFacade.editInUseCss(selectedCSS);
	}

	public void editCss() {
		try {
			cSSFacade.edit(selectedCSS);
		} catch (EJBException ex) {
			Logger.getLogger(StylesCssBB.class.getName()).log(Level.SEVERE, null, ex);
			JSFUtil.addErrorMessage("Error creating Css");
		}
	}

	public boolean canEdit(CSS css) {
		return cSSFacade.canEditCss(css);
	}

	public CSS getCssInUse() {
		return cSSFacade.getCSSInUse();
	}

	public boolean inUse(CSS css) {
		return css.isInUse();
	}

	/////////////////////Getters && Setters////////////////////
	public List<CSS> getLstCSS() {
		lstCSS = cSSFacade.findAll();
		return lstCSS;
	}

	public void setLstCSS(List<CSS> lstCSS) {
		this.lstCSS = lstCSS;
	}

	public CSS getCss() {
		return css;
	}

	public void setCss(CSS css) {
		this.css = css;
	}

	public CSSFacade getcSSFacade() {
		return cSSFacade;
	}

	public void setcSSFacade(CSSFacade cSSFacade) {
		this.cSSFacade = cSSFacade;
	}

	public CSS getSelectedCSS() {
		return selectedCSS;
	}

	public void setSelectedCSS(CSS selectedCSS) {
		this.selectedCSS = selectedCSS;
	}

}
