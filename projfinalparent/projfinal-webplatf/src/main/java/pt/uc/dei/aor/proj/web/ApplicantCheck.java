/*
 */
package pt.uc.dei.aor.proj.web;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;
import pt.uc.dei.aor.proj.db.tools.StatusApplicant;
import pt.uc.dei.aor.proj.serv.facade.ApplicantFacade;
import pt.uc.dei.aor.proj.serv.tools.ApplicantData;

/**
 *
 * @author
 */
@Named
@RequestScoped
public class ApplicantCheck {

	private List<ApplicantEntity> lstApplicant;

	private ApplicantEntity selectedApplicant;

	@EJB
	private ApplicantFacade applicantFacade;
	@EJB
	private ApplicantData applicantData;


	/**
	 * Creates a new instance of ApplicantViewBean
	 */
	public ApplicantCheck() {
	}

	/**
	 *
	 * @return to page "applicants" if applicant is send to BlackList
	 */
	public String sendApplicantToBlackList() {
		try {
			applicantFacade.toBlackList(applicantData.getApplicant());
			return "applicants.xhtml?faces-redirect=true";
		} catch (EJBException ex) {
			Logger.getLogger(ApplicantCheck.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}

	}
	/**
	 *
	 * @param applicant
	 * @return true if applicant status is not rejected
	 */

	public boolean isRejected(ApplicantEntity applicant){
		return !applicant.getStatus().equals(StatusApplicant.REJECTED);
	}
	/**
	 *
	 * @param applicant
	 * @return to blacklist page
	 */
	public String goToblacklist(ApplicantEntity applicant) {
		applicantData.setApplicant(applicant);
		return "blacklist.xhtml?faces-redirect=true";
	}


	/////////////////////Getters && Setters////////////////////

	public List<ApplicantEntity> getLstApplicant() {
		return applicantFacade.findAll();
	}

	public void setLstApplicant(List<ApplicantEntity> lstApplicant) {
		this.lstApplicant = lstApplicant;
	}

	public ApplicantEntity getSelectedApplicant() {
		if(selectedApplicant!=null){
			applicantData.setApplicant(selectedApplicant);
		}
		return selectedApplicant;
	}

	public void setSelectedApplicant(ApplicantEntity selectedApplicant) {
		this.selectedApplicant = selectedApplicant;
	}

	public ApplicantFacade getApplicantFacade() {
		return applicantFacade;
	}

	public void setApplicantFacade(ApplicantFacade applicantFacade) {
		this.applicantFacade = applicantFacade;
	}

	public ApplicantData getApplicantData() {
		return applicantData;
	}

	public void setApplicantData(ApplicantData applicantData) {
		this.applicantData = applicantData;
	}

}
