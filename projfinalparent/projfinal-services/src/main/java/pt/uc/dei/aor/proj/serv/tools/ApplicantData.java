/*
 */
package pt.uc.dei.aor.proj.serv.tools;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

import pt.uc.dei.aor.proj.db.entities.ApplicantEntity;

/**
 *
 * @author
 */
@Stateful
@SessionScoped
public class ApplicantData implements Serializable {

	private static final long serialVersionUID = -4578972853700713733L;

	private ApplicantEntity applicant;

	/////////////////////Getters && Setters////////////////////
	public ApplicantEntity getApplicant() {
		return applicant;
	}

	public void setApplicant(ApplicantEntity applicant) {
		this.applicant = applicant;
	}

}
