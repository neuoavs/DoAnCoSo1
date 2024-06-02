package Model;

import javafx.scene.shape.Circle;

public class CheckBoxAddFunds {
	private Circle checkBox;
	
	private boolean checked;
	
	private String value;

	public CheckBoxAddFunds() {
		super();
	}

	public CheckBoxAddFunds(Circle checkBox, boolean checked, String value) {
		super();
		this.checkBox = checkBox;
		this.checked = checked;
		this.value = value;
	}

	public Circle getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(Circle checkBox) {
		this.checkBox = checkBox;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CheckBoxAddFunds [checkBox=" + checkBox + ", checked=" + checked + ", value=" + value + "]";
	}
	
	
	
	
	
}
