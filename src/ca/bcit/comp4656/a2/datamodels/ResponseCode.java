package ca.bcit.comp4656.a2.datamodels;

public class ResponseCode {
	private int responseCode;
	private String description;
	private String note;
	
	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public ResponseCode() {
		
	}

}
