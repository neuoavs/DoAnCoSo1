package Model;

import java.io.Serializable;

public class OS implements Serializable{
	private static final long serialVersionUID = 1L;
	private int os_id;
	private String os_name;
	
	public OS() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public OS(int os_id, String os_name) {
		super();
		this.os_id = os_id;
		this.os_name = os_name;
	}
	
	public int getOs_id() {
		return os_id;
	}
	
	public void setOs_id(int os_id) {
		this.os_id = os_id;
	}
	
	public String getOs_name() {
		return os_name;
	}
	
	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}
}
