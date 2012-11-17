package eu.areamobile.android.apps.amrevolution.bean;

public class AMSnippetRequest {
	private int id;
	private long timestamp_lastDownload;
	
	
	/**
	 * @param id
	 * @param timestamp_lastDownload
	 */
	public AMSnippetRequest(int id, long timestamp_lastDownload) {
		this.id = id;
		this.timestamp_lastDownload = timestamp_lastDownload;
	}
	
	public AMSnippetRequest() {
		this.id = -1;
		this.timestamp_lastDownload = -1;
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
	 * @return the timestamp_lastDownload
	 */
	public long getTimestamp_lastDownload() {
		return timestamp_lastDownload;
	}
	/**
	 * @param timestamp_lastDownload the timestamp_lastDownload to set
	 */
	public void setTimestamp_lastDownload(long timestamp_lastDownload) {
		this.timestamp_lastDownload = timestamp_lastDownload;
	}
	
}
