package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.scribe.model.Token;

import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplicant;


@Entity

@Table(name = "applicant")
@XmlRootElement
@DiscriminatorValue("applicant")
public class ApplicantEntity extends UserEntity implements Serializable {

	@Column(name = "country")
	@Basic
	private String country;

	@Column(name = "address")
	@Basic
	private String address;

	@Column(name = "city")
	@Basic
	private String city;

	@Column(name = "phone")
	@Basic
	private String phone;

	@Column(name = "school")
	@Basic
	private String school;

	@Column(name = "degree")
	@Basic
	private String degree;

	@Column(name = "mobile")
	@Basic
	private String mobile;

	@Column(name = "status")
	@Basic
	private StatusApplicant status;

	@Column(name = "rejectionmotive")
	@Basic
	private RejectionMotive motive;

	@Column(name = "rawresponse")
	private String rawResponse;

	@Column(name = "secretToken")
	private String secretToken;

	@Column(name = "acessToken")
	private String acessToken;

	@Column(name = "hiringdate")
	@Temporal(TemporalType.TIMESTAMP)
	@Basic
	private Date hiringDate;

	public ApplicantEntity() {

	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public StatusApplicant getStatus() {
		return status;
	}

	public void setStatus(StatusApplicant status) {
		this.status = status;
	}

	public String getAcessToken() {
		return acessToken;
	}

	public void setAcessToken(String acessToken) {
		this.acessToken = acessToken;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public RejectionMotive getMotive() {
		return motive;
	}

	public void setMotive(RejectionMotive motive) {
		this.motive = motive;
	}

	public Date getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(Date hiringDate) {
		this.hiringDate = hiringDate;
	}

	public String getRawResponse() {
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public String getSecretToken() {
		return secretToken;
	}

	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}

	public void setTokens(Token token) {
		this.acessToken = token.getToken();
		this.rawResponse = token.getRawResponse();
		this.secretToken = token.getSecret();
	}

	public Token getToken(){
		return new Token(acessToken, secretToken, rawResponse);
	}

}
