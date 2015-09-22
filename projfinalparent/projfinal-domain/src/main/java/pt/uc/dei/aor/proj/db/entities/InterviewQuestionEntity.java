package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;

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
import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.aor.proj.db.tools.AnswerType;

@Entity
@Table(name = "interviewquestionentity")
@XmlRootElement
@DiscriminatorValue("interviewquestionentity")
@NamedQueries({
	@NamedQuery(name = "InterviewQuestionEntity.findByInterviewGuideName", query = "SELECT iq FROM InterviewQuestionEntity iq WHERE iq.question = :interviewId "),
	@NamedQuery(name = "InterviewQuestionEntity.findByInterview", query = "SELECT iq FROM InterviewQuestionEntity iq WHERE iq.interviewGuide.interviewId = :interviewId order by iq.questionNumber ASC")})
public class InterviewQuestionEntity implements Serializable {

	@Column(name = "questionid")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long questionId;

	@Column(name = "question", nullable = false)
	@Basic
	private String question;

	@ManyToOne
	private InterviewEntity interviewGuide;

	@Column(name = "answertype")
	@Basic
	private AnswerType answerType;

	@Column(name = "questionNumber")
	@Basic
	private Integer questionNumber;

	public InterviewQuestionEntity() {

	}

	public Long getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public InterviewEntity getInterviewGuide() {
		return this.interviewGuide;
	}

	public void setInterviewGuide(InterviewEntity interviewGuide) {
		this.interviewGuide = interviewGuide;
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

}
