package eu.areamobile.android.apps.amrevolution.bean;

public class AMBeanNews {
	private int id;
	private long timestamp;
	private String title;
	private String body;
	private String img_url;
	private String web_url;
	
	/**
	 * @param id
	 * @param timestamp
	 * @param title
	 * @param body
	 * @param img_url
	 * @param web_url
	 */
	public AMBeanNews(int id, long timestamp, String title, String body, String img_url, String web_url) {
		this.id = id;
		this.timestamp = timestamp;
		this.title = title;
		this.body = body;
		this.img_url = img_url;
		this.web_url = web_url;
	}
	
	public AMBeanNews() {
		this.id = -1;
		this.timestamp = -1;
		this.title = "";
		this.body = "";
		this.img_url = "";
		this.web_url = "";
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
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * @return the img_url
	 */
	public String getImg_url() {
		return img_url;
	}

	/**
	 * @param img_url the img_url to set
	 */
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	/**
	 * @return the web_url
	 */
	public String getWeb_url() {
		return web_url;
	}

	/**
	 * @param web_url the web_url to set
	 */
	public void setWeb_url(String web_url) {
		this.web_url = web_url;
	}
	
	
	@Override
	public String toString() {
		String metaCharSeparator = "|";
		
		StringBuilder myObjStringBuilder = new StringBuilder();
		
		myObjStringBuilder.append(this.id);
		
		myObjStringBuilder.append(metaCharSeparator + this.timestamp);
		
		if(this.title != null) {
			myObjStringBuilder.append(metaCharSeparator + this.title);
		}
		else {
			myObjStringBuilder.append(metaCharSeparator + "NULL");
		}
		
		if(this.body != null) {
			myObjStringBuilder.append(metaCharSeparator + this.body);
		}
		else {
			myObjStringBuilder.append(metaCharSeparator + "NULL");
		}
		
		if(this.img_url != null) {
			myObjStringBuilder.append(metaCharSeparator + this.img_url);
		}
		else {
			myObjStringBuilder.append(metaCharSeparator + "NULL");
		}
		
		if(this.web_url != null) {
			myObjStringBuilder.append(metaCharSeparator + this.web_url);
		}
		else {
			myObjStringBuilder.append(metaCharSeparator + "NULL");
		}
		
		return myObjStringBuilder.toString();
	}
}
