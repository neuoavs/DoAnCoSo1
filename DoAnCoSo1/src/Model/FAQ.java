package Model;

import java.io.Serializable;

public class FAQ implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id_faq;
	private String question;
	private String answer;
	
	public FAQ() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FAQ(int id_faq, String question, String answer) {
		super();
		this.id_faq = id_faq;
		this.question = question;
		this.answer = answer;
	}

	public int getId_faq() {
		return id_faq;
	}

	public void setId_faq(int id_faq) {
		this.id_faq = id_faq;
	}

	public String getQuestion() {
		return question;
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

	@Override
	public String toString() {
		return "FAQ:\nid_faq=" + id_faq + "\nquestion=" + question + "\nanswer=" + answer + "\n";
	}
}
