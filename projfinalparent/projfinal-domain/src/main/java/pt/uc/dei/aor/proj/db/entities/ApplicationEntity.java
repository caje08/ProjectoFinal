package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.aor.proj.db.tools.RejectionMotive;
import pt.uc.dei.aor.proj.db.tools.StatusApplication;

@Entity
@Table(name = "applicationentity")
@XmlRootElement
@DiscriminatorValue("applicationentity")
@NamedQueries({

	    @NamedQuery(name = "ApplicationEntity.findBySpontaneousApplications", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = true and a.applicant.status!=pt.uc.dei.aor.proj.db.tools.StatusApplicant.REJECTED)"),
	    @NamedQuery(name = "ApplicationEntity.findBySpontaneousApplicationsOfManager", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = true and a.position.manager=:manager and a.applicant.status!=pt.uc.dei.aor.proj.db.tools.StatusApplicant.REJECTED "),
	    @NamedQuery(name = "ApplicationEntity.findByNonSpontaneousApplications", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = false and a.applicant.status!=pt.uc.dei.aor.proj.db.tools.StatusApplicant.REJECTED"),
	    @NamedQuery(name = "ApplicationEntity.findByNonSpontaneousApplicationsOfManager", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = false and a.position.manager=:manager and a.applicant.status!=pt.uc.dei.aor.proj.db.tools.StatusApplicant.REJECTED"),

	//@NamedQuery(name = "ApplicationEntity.findBySpontaneousApplications", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = true and not(a.applicant.status=:REJECTED)"),

//	@NamedQuery(name = "ApplicationEntity.findBySpontaneousApplicationsOfManager", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = true and a.position.manager=:manager and not(a.applicant.status=:REJECTED) "),
//	@NamedQuery(name = "ApplicationEntity.findByNonSpontaneousApplications", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = false and not(a.applicant.status=:REJECTED)"),
//	@NamedQuery(name = "ApplicationEntity.findByNonSpontaneousApplicationsOfManager", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = false and a.position.manager=:manager and not(a.applicant.status=:REJECTED)"),
	@NamedQuery(name = "ApplicationEntity.findByUser", query = "SELECT a FROM ApplicationEntity a WHERE a.applicant = :applicant"),
	@NamedQuery(name = "ApplicationEntity.findByDate", query = "SELECT a FROM ApplicationEntity a WHERE a.applicationDate = :applicationDate"),
	@NamedQuery(name = "ApplicationEntity.findSpontaneousByDate", query = "SELECT a FROM ApplicationEntity a WHERE a.applicationDate = :applicationDate AND a.isSpontaneous = true"),
	@NamedQuery(name = "ApplicationEntity.findByPosition", query = "SELECT a FROM ApplicationEntity a WHERE a.position = :position"),
	@NamedQuery(name = "ApplicationEntity.findPositionsInApplication", query = "SELECT a.position FROM ApplicationEntity a WHERE a.position = :position"),
	@NamedQuery(name = "ApplicationEntity.findPositionsInApplicant", query = "SELECT a.position FROM ApplicationEntity a WHERE a.applicant=:applicant"),
	@NamedQuery(name = "ApplicationEntity.findByApplicantAndPosition", query = "SELECT a FROM ApplicationEntity a WHERE a.position = :position and a.applicant=:applicant"),
	@NamedQuery(name = "ApplicationEntity.findByApplicantAndPositionAndNonSpontaneous", query = "SELECT a FROM ApplicationEntity a WHERE a.position = :position and a.applicant=:applicant and a.isSpontaneous = false"),
	@NamedQuery(name = "ApplicationEntity.findByRejectionMotiveCV", query = "SELECT a FROM ApplicationEntity a WHERE a.motive = pt.uc.dei.aor.proj.db.tools.RejectionMotive.CV"),
	@NamedQuery(name = "ApplicationEntity.findByRejectionMotiveNEGOTIATION", query = "SELECT a FROM ApplicationEntity a WHERE a.motive = pt.uc.dei.aor.proj.db.tools.RejectionMotive.NEGOTIATION"),
	@NamedQuery(name = "ApplicationEntity.findByRejectionMotivePERSONALITY", query = "SELECT a FROM ApplicationEntity a WHERE a.motive = pt.uc.dei.aor.proj.db.tools.RejectionMotive.PERSONALITY"),
	@NamedQuery(name = "ApplicationEntity.findByStatusHIRED", query = "SELECT a FROM ApplicationEntity a WHERE a.status = pt.uc.dei.aor.proj.db.tools.StatusApplication.HIRED"),
	@NamedQuery(name = "ApplicationEntity.findByStatusINTERVIEWING", query = "SELECT a FROM ApplicationEntity a WHERE a.status = pt.uc.dei.aor.proj.db.tools.StatusApplication.INTERVIEWING"),
	@NamedQuery(name = "ApplicationEntity.findByStatusNEGOTIATION", query = "SELECT a FROM ApplicationEntity a WHERE a.status = pt.uc.dei.aor.proj.db.tools.StatusApplication.NEGOTIATION"),
	@NamedQuery(name = "ApplicationEntity.findApplicantByPosition", query = "SELECT a.applicant FROM ApplicationEntity a WHERE a.position = :position"),
	@NamedQuery(name = "ApplicationEntity.findApplicantByDate", query = "SELECT a.applicant FROM ApplicationEntity a WHERE a.applicationDate BETWEEN :startDate AND :endDate"),
	@NamedQuery(name = "ApplicationEntity.findByDateSpontaneous", query = "SELECT a FROM ApplicationEntity a WHERE a.isSpontaneous = true AND a.applicationDate BETWEEN :startDate AND :endDate")})
public class ApplicationEntity implements Serializable {

	@Column(name = "cv")
	@Basic
	private String cv;

	@Column(name = "coverLetter")
	@Basic
	private String coverLetter;

	@Column(name = "source")
	@Basic
	private String source;

	@Column(name = "applicationid")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicationId;

	@Column(name = "status")
	@Basic
	private StatusApplication status;

	@Column(name = "applicationdate")
	@Temporal(TemporalType.DATE)
	@Basic
	private Date applicationDate;

	@Column(name = "hiringdate")
	@Temporal(TemporalType.TIMESTAMP)
	@Basic
	private Date hiringDate;

	@Column(name = "isSpontaneous")
	@Basic
	private boolean isSpontaneous;

	@Column(name = "rejectionmotive")
	@Basic
	private RejectionMotive motive;

	@ManyToOne(targetEntity = PositionEntity.class)
	private PositionEntity position;
	
	@ManyToOne(targetEntity = InterviewerEntity.class)
	private InterviewerEntity interviewer;

	@ManyToOne(targetEntity = ApplicantEntity.class)
	private ApplicantEntity applicant;

	@OneToMany(targetEntity = InterviewFeedbackEntity.class)
	private Collection<InterviewFeedbackEntity> interviewFeedbackEntitys;

	public ApplicationEntity() {
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public String getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public PositionEntity getPosition() {
		return this.position;
	}

	public void setPosition(PositionEntity position) {
		this.position = position;
	}

	public Long getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public StatusApplication getStatus() {
		return status;
	}

	public void setStatus(StatusApplication status) {
		this.status = status;
	}

	public ApplicantEntity getApplicant() {
		return this.applicant;
	}

	public void setApplicant(ApplicantEntity applicant) {
		this.applicant = applicant;
	}

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public boolean isIsSpontaneous() {
		return isSpontaneous;
	}

	public void setIsSpontaneous(boolean isSpontaneous) {
		this.isSpontaneous = isSpontaneous;
	}

	public Date getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(Date hiringDate) {
		this.hiringDate = hiringDate;
	}

	public RejectionMotive getMotive() {
		return motive;
	}

	public void setMotive(RejectionMotive motive) {
		this.motive = motive;
	}

	public InterviewerEntity getInterviewer() {
		return interviewer;
	}

	public void setInterviewer(InterviewerEntity interviewer) {
		this.interviewer = interviewer;
	}

	public Collection<InterviewFeedbackEntity> getInterviewFeedbackEntitys() {
		return interviewFeedbackEntitys;
	}

	public void setInterviewFeedbackEntitys(Collection<InterviewFeedbackEntity> interviewFeedbacks) {
		this.interviewFeedbackEntitys = interviewFeedbacks;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + Objects.hashCode(this.position);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ApplicationEntity other = (ApplicationEntity) obj;
		if (!Objects.equals(this.position, other.position)) {
			return false;
		}
		return true;
	}

}
