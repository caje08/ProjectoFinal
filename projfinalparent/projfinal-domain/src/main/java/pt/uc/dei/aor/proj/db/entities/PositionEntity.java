package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "positionentity")
@XmlRootElement
@DiscriminatorValue("positionentity")
@NamedQueries({
	@NamedQuery(name = "PositionEntity.findByVeracity", query = "SELECT p FROM PositionEntity p WHERE p.isPublic=true"),
	@NamedQuery(name = "PositionEntity.findPhoneInterviewEntity", query = "SELECT p FROM PositionEntity p WHERE p.phoneInterviewEntity.interviewId = :interviewId"),
	@NamedQuery(name = "PositionEntity.findByManager", query = "SELECT p FROM PositionEntity p WHERE p.manager = :manager"),
	@NamedQuery(name = "PositionEntity.findByManagerCanApply", query = "SELECT p FROM PositionEntity p WHERE p.manager = :manager AND p.isPublic=true AND p.closingDate > :currentDate AND p.OpeningDate < :currentDate and p.vacancies>0 and p.status like 'Open'"),
	@NamedQuery(name = "PositionEntity.findBeforeClosingDate", query = "SELECT p FROM PositionEntity p WHERE p.closingDate > :currentDate"),
	//@NamedQuery(name = "PositionEntity.findPublicBeforeClosingDate", query = "SELECT p FROM PositionEntity p WHERE p.isPublic=true AND p.closingDate > :currentDate AND p.OpeningDate < :currentDate and p.vacancies>0 and p.status like 'Open'"),
	@NamedQuery(name = "PositionEntity.findPublicBeforeClosingDate", query = "SELECT p FROM PositionEntity p WHERE p.isPublic=true AND p.closingDate > :currentDate AND p.vacancies>0 and p.status like 'OPEN'"),
	@NamedQuery(name = "PositionEntity.findPresencialInterviewEntity", query = "SELECT p FROM PositionEntity p WHERE p.presencialInterviewEntity.interviewId = :interviewId"),
	//@NamedQuery(name = "PositionEntity.doNotApplied", query = "SELECT p FROM PositionEntity p WHERE p NOT IN (SELECT a.position FROM ApplicationEntity a WHERE a.applicant.userId=:applicantId and a.isSpontaneous=false) AND p.closingDate > :currentDate AND p.OpeningDate < :currentDate  AND p.isPublic=true AND p.status like 'Open'"),
	@NamedQuery(name = "PositionEntity.doNotApplied", query = "SELECT p FROM PositionEntity p WHERE p.title NOT IN (SELECT a.position FROM ApplicationEntity a WHERE a.applicant.userId=:applicantId and a.isSpontaneous=false) AND p.closingDate > :currentDate AND p.OpeningDate < :currentDate  AND p.isPublic=true AND p.status like 'Open'"),
	@NamedQuery(name = "PositionEntity.findByCode", query = "SELECT p FROM PositionEntity p WHERE p.positionCode = :positionCode")})

public class PositionEntity implements Serializable {

	@Column(name = "openingdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Basic
	private Date OpeningDate;

	@ManyToOne(targetEntity = ManagerEntity.class)
	private ManagerEntity manager;

	@ManyToOne(targetEntity = InterviewEntity.class)
	private InterviewEntity phoneInterviewEntity;

	@ManyToOne(targetEntity = InterviewEntity.class)
	private InterviewEntity presencialInterviewEntity;

	@Column(name = "positioncode")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long positionCode;

	@Column(name = "sla", nullable = false)
	@Basic
	private int sla;

	@Column(name = "title", nullable = false)
	@Basic
	private String title;

	@Column(name = "closingdate")
	@Temporal(TemporalType.DATE)
	@Basic
	private Date closingDate;

	@Column(name = "vacancies")
	@Basic
	private int vacancies;

	@Column(name = "technicalarea")
	@Basic
	private String technicalArea;

	@Column(name = "ispublic", nullable = false)
	@Basic
	private boolean isPublic;

	@Column(name = "location")
	@Basic
	private String location;

	@Column(name = "company")
	@Basic
	private String company;

	@Column(name = "jobdescription")
	@Basic
	@Size(min = 1, max=10000)
	private String jobDescription;

	@Column(name = "publishingchannels")
	@Basic
	private String publishingChannels;

	@Column(name = "status")
	@Basic
	private String status;

	public PositionEntity() {
	}

	public Date getOpeningDate() {
		return this.OpeningDate;
	}

	public void setOpeningDate(Date OpeningDate) {
		this.OpeningDate = OpeningDate;
	}

	public ManagerEntity getManager() {
		return this.manager;
	}

	public void setManager(ManagerEntity manager) {
		this.manager = manager;
	}

	public InterviewEntity getPhoneInterviewEntity() {
		return phoneInterviewEntity;
	}

	public void setPhoneInterviewEntity(InterviewEntity phoneInterviewEntity) {
		this.phoneInterviewEntity = phoneInterviewEntity;
	}

	public InterviewEntity getPresencialInterviewEntity() {
		return presencialInterviewEntity;
	}

	public void setPresencialInterviewEntity(InterviewEntity presencialInterviewEntity) {
		this.presencialInterviewEntity = presencialInterviewEntity;
	}

	public Long getPositionCode() {
		return this.positionCode;
	}

	public void setPositionCode(Long positionCode) {
		this.positionCode = positionCode;
	}

	public int getSla() {
		return this.sla;
	}

	public void setSla(int sla) {
		this.sla = sla;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getClosingDate() {
		return this.closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public int getVacancies() {
		return this.vacancies;
	}

	public void setVacancies(int vacancies) {
		this.vacancies = vacancies;
	}

	public String getTechnicalArea() {
		return this.technicalArea;
	}

	public void setTechnicalArea(String technicalArea) {
		this.technicalArea = technicalArea;
	}

	public boolean isIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getJobDescription() {
		return this.jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getPublishingChannels() {
		return this.publishingChannels;
	}

	public void setPublishingChannels(String publishingChannels) {
		this.publishingChannels = publishingChannels;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
