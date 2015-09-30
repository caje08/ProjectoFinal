package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.aor.proj.db.tools.InterviewType;

@Entity
@Table(name = "interviewentity")
@XmlRootElement
@DiscriminatorValue("interviewentity")
@NamedQueries({
	@NamedQuery(name = "InterviewEntity.findAll", query = "SELECT i FROM InterviewEntity i order by i asc"),
	@NamedQuery(name = "InterviewEntity.findByName", query = "SELECT i FROM InterviewEntity i WHERE i.title = :title"),
	@NamedQuery(name = "InterviewEntity.findAllPhoneGuides", query = "SELECT i FROM InterviewEntity i WHERE i.type = pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE"),
	@NamedQuery(name = "InterviewEntity.findAllPresentialGuides", query = "SELECT i FROM InterviewEntity i WHERE i.type = pt.uc.dei.aor.proj.db.tools.InterviewType.PRESENTIAL"),
	@NamedQuery(name = "InterviewEntity.findById", query = "SELECT i FROM InterviewEntity i WHERE i.interviewId = :interviewId"),
	@NamedQuery(name = "InterviewEntity.findInUsePhoneGuides", query = "SELECT i FROM InterviewEntity i WHERE i.type = pt.uc.dei.aor.proj.db.tools.InterviewType.PHONE and i.inUse = :True"),
	@NamedQuery(name = "InterviewEntity.findInUsePresentialGuides", query = "SELECT i FROM InterviewEntity i WHERE i.type = pt.uc.dei.aor.proj.db.tools.InterviewType.PRESENTIAL and i.inUse = :True"),
	@NamedQuery(name = "InterviewEntity.findInUse", query = "SELECT i FROM InterviewEntity i WHERE i.inUse = :True")})

public class InterviewEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long interviewId;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Basic
	private Date creationDate;

	@Basic
	private String title;

	@Column(name = "inUse")
	@Basic
	private Boolean inUse;

	@Column(name = "type", nullable = false)
	@Basic
	private InterviewType type;

	@OneToMany(targetEntity = InterviewQuestionEntity.class, cascade = CascadeType.ALL, mappedBy="interviewGuide")
	private Collection<InterviewQuestionEntity> interviewQuestion;

	public InterviewEntity() {
	}

	public Long getInterviewId() {
		return this.interviewId;
	}

	public void setInterviewId(Long interviewId) {
		this.interviewId = interviewId;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}

	public InterviewType getType() {
		return type;
	}

	public void setType(InterviewType type) {
		this.type = type;
	}

	public Collection<InterviewQuestionEntity> getInterviewQuestion() {
		return interviewQuestion;
	}

	public void setInterviewQuestion(Collection<InterviewQuestionEntity> interviewQuestion) {
		this.interviewQuestion = interviewQuestion;
	}

}
