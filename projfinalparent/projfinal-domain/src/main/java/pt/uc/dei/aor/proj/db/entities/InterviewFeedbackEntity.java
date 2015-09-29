package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.aor.proj.db.tools.InterviewType;
import pt.uc.dei.aor.proj.db.tools.Outcome;

@Entity
@Table(name = "interviewfeedbackentity")
@XmlRootElement
@DiscriminatorValue("interviewfeedbackentity")
@NamedQueries({
	@NamedQuery(name = "InterviewFeedbackEntity.findEnum", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.application=:application and i.interviewType =pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE"),
	@NamedQuery(name = "InterviewFeedbackEntity.findInterview", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.application.applicationId=:applicationId"),
	@NamedQuery(name = "InterviewFeedbackEntity.findInterviewByInterviewer", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.application.applicationId=:applicationId and i.interviewer=:interviewer"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByOutcomeAcceptedPhoneOfAnApplication", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.outcome = pt.uc.dei.aor.proj.db.tools.Outcome.ACCEPTED AND i.interviewType = pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE and i.application=:application"),
	@NamedQuery(name = "InterviewFeedbackEntity.countAllInterviewsOfAnApplication", query = "SELECT count(i) FROM InterviewFeedbackEntity i WHERE i.application=:application"),
	@NamedQuery(name = "InterviewFeedbackEntity.countAllAcceptedOutcomeOfAnApplication", query = "SELECT count(i) FROM InterviewFeedbackEntity i WHERE i.outcome = pt.uc.dei.aor.proj.db.tools.Outcome.ACCEPTED AND i.application=:application"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByOutcomeAcceptedPhone", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.outcome = pt.uc.dei.aor.proj.db.tools.Outcome.ACCEPTED AND i.interviewType = pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByInterviewer", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.interviewer = :interviewer"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByApplication", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.application=:application"),
	@NamedQuery(name = "InterviewFeedbackEntity.findDateOfFirstInterviewOfAnApplication", query = "SELECT i.interviewDate FROM InterviewFeedbackEntity i WHERE i.application=:application and i.interviewType =pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByOutcomeAcceptedPresential", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.outcome = pt.uc.dei.aor.proj.db.tools.Outcome.ACCEPTED AND i.interviewType = pt.uc.dei.aor.proj.db.tools.InterviewType.PRESENTIAL"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByOutcomeRejectedPhone", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.outcome = pt.uc.dei.aor.proj.db.tools.Outcome.REJECTED AND i.interviewType = pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByOutcomeRejectedPresential", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.outcome = pt.uc.dei.aor.proj.db.tools.Outcome.REJECTED AND i.interviewType = pt.uc.dei.aor.proj.db.tools.InterviewType.PRESENTIAL"),
	@NamedQuery(name = "InterviewFeedbackEntity.findByInterviewTypePHONE", query = "SELECT i FROM InterviewFeedbackEntity i WHERE i.interviewType = pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE")})

public class InterviewFeedbackEntity implements Serializable {

	@ManyToOne(targetEntity = InterviewerEntity.class)
	private InterviewerEntity interviewer;

	@OneToMany(targetEntity = AnswerEntity.class)
	private Collection<AnswerEntity> answer;

	@ManyToOne(targetEntity = ApplicationEntity.class)
	private ApplicationEntity application;

	@Column(name = "feedbackid")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedbackId;

	@Column(name = "interviewdate")
	@Temporal(TemporalType.TIMESTAMP)
	@Basic
	private Date interviewDate;

	@Column(name = "interviewType")
	@Basic
	private InterviewType interviewType;

	@Column(name = "outcome")
	@Basic
	private Outcome outcome;

	@ManyToOne(targetEntity = InterviewEntity.class)
	private InterviewEntity interviewEntity;

	public InterviewFeedbackEntity() {
	}

	public InterviewerEntity getInterviewer() {
		return this.interviewer;
	}

	public void setInterviewer(InterviewerEntity interviewer) {
		this.interviewer = interviewer;
	}

	public Collection<AnswerEntity> getAnswer() {
		return this.answer;
	}

	public void setAnswer(Collection<AnswerEntity> answer) {
		this.answer = answer;
	}

	public ApplicationEntity getApplication() {
		return this.application;
	}

	public void setApplication(ApplicationEntity application) {
		this.application = application;
	}

	public Long getFeedbackId() {
		return this.feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public Outcome getOutcome() {
		return outcome;
	}

	public void setOutcome(Outcome outcome) {
		this.outcome = outcome;
	}

	public InterviewEntity getInterviewEntity() {
		return interviewEntity;
	}

	public void setInterviewEntity(InterviewEntity interviewEntity) {
		this.interviewEntity = interviewEntity;
	}

	public InterviewType getInterviewType() {
		return interviewType;
	}

	public void setInterviewType(InterviewType interviewType) {
		this.interviewType = interviewType;
	}

}
