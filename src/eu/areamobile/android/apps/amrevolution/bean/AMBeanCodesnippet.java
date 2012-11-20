package eu.areamobile.android.apps.amrevolution.bean;

public class AMBeanCodesnippet {
	private int id;
	private long timestamp;
	private String language_type;
	private String title;
	private String code;
	
	
	/**
	 * @param id
	 * @param timestamp
	 * @param language_type
	 * @param title
	 * @param code
	 */
	public AMBeanCodesnippet(int id, long timestamp, String language_type, String title, String code) {
		this.id = id;
		this.timestamp = timestamp;
		this.language_type = language_type;
		this.title = title;
		this.code = code;
	}
	
	public AMBeanCodesnippet() {
		this.id = -1;
		this.timestamp = -1;
		this.language_type = "";
		this.title = "";
		this.code = "";
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return the language_type
	 */
	public String getLanguage_type() {
		return language_type;
	}
	/**
	 * @param language_type the language_type to set
	 */
	public void setLanguage_type(String language_type) {
		this.language_type = language_type;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		String metaCharSeparator = "|";
		
		StringBuilder myObjStringBuilder = new StringBuilder();
		
		myObjStringBuilder.append(this.id);
		
		myObjStringBuilder.append(metaCharSeparator + this.timestamp);
		
		if(this.language_type != null) {
			myObjStringBuilder.append(metaCharSeparator + this.language_type);
		}
		else {
			myObjStringBuilder.append(metaCharSeparator + "NULL");
		}
		
		if(this.title != null) {
			myObjStringBuilder.append(metaCharSeparator + this.title);
		}
		else {
			myObjStringBuilder.append(metaCharSeparator + "NULL");
		}
		
		if(this.code != null) {
			myObjStringBuilder.append(metaCharSeparator + this.code);
		}
		else {
			myObjStringBuilder.append(metaCharSeparator + "NULL");
		}
		
		return myObjStringBuilder.toString();
	}
}
