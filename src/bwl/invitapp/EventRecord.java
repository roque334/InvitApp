package bwl.invitapp;

public class EventRecord {
	
	private String id;
	private String name;
	private String description;
	private String datetime;
	private boolean checked;
	private int attendance;
	private int totalGuests;
	private String creatorUserID;
	private String creatorUserName;
	
	
	public EventRecord(String id, String name, String description, String date,
			int checked, int attendance, int totalGuests, String creatorUserID, String creatorUserName){
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setDatetime(date);
		if(checked==0){
			this.setChecked(false);
		}else{
			this.setChecked(true);
		}
		
		this.setAttendance(attendance);
		this.setTotalGuests(totalGuests);
		this.setCreatorUserID(creatorUserID);
		this.setCreatorUserName(creatorUserName);
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String date) {
		this.datetime = date;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getAttendance() {
		return attendance;
	}

	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}

	public int getTotalGuests() {
		return totalGuests;
	}

	public void setTotalGuests(int totalGuests) {
		this.totalGuests = totalGuests;
	}

	public String getCreatorUserID() {
		return creatorUserID;
	}

	public void setCreatorUserID(String creatorUserID) {
		this.creatorUserID = creatorUserID;
	}

	public String getCreatorUserName() {
		return creatorUserName;
	}

	public void setCreatorUserName(String creatorUserName) {
		this.creatorUserName = creatorUserName;
	}

}
