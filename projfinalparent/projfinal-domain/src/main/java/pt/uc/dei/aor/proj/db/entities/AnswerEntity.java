package pt.uc.dei.aor.proj.db.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import pt.uc.dei.aor.proj.db.tools.AnswerType;

@Entity
@Table(name = "answerentity")
@XmlRootElement
@DiscriminatorValue("answerentity")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class AnswerEntity implements Serializable {

	@Column(name = "answerid")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long answerId;

	@Column(name = "question")
	@Basic
	private String question;

	@Column(name = "answer", columnDefinition = "LONGTEXT")
	@Basic
	private String answer;

	@Column(name = "answertype")
	@Basic
	private AnswerType answerType;

	@Column(name = "questionNumber")
	@Basic
	private Integer questionNumber;

	public AnswerEntity() {
	}

	public Long getAnswerId() {
		return this.answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
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
