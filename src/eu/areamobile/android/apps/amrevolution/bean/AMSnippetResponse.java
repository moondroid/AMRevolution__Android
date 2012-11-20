package eu.areamobile.android.apps.amrevolution.bean;

import java.util.ArrayList;

public class AMSnippetResponse {
	private ArrayList<AMBeanCodesnippet> snippetList;

	/**
	 * @param snippetList
	 */
	public AMSnippetResponse(ArrayList<AMBeanCodesnippet> snippetList) {
		this.snippetList = snippetList;
	}
	
	public AMSnippetResponse() {
		this.snippetList = new ArrayList<AMBeanCodesnippet>();
	}

	
	/**
	 * @return the snippetList
	 */
	public ArrayList<AMBeanCodesnippet> getSnippetList() {
		return snippetList;
	}

	/**
	 * @param snippetList the snippetList to set
	 */
	public void setSnippetList(ArrayList<AMBeanCodesnippet> snippetList) {
		this.snippetList = snippetList;
	}

	public void addSnippet(AMBeanCodesnippet mySnippet) {
		snippetList.add(mySnippet);
	}
	
	
	@Override
	public String toString() {
		String metaCharSeparator = "\n";
		
		StringBuilder myObjStringBuilder = new StringBuilder();
		
		if(this.snippetList != null) {
			if(this.snippetList.size() < 1) {
				myObjStringBuilder.append("Empty snippetList");
			}
			else {
				for(int i = 0; i < snippetList.size(); i++) {
					AMBeanCodesnippet currentSnippet = snippetList.get(i);
					
					String myRowValue = "Pos " + i + "=> " + currentSnippet.toString();
					
					myObjStringBuilder.append(metaCharSeparator + myRowValue);
				}
			}
		}
		else {
			myObjStringBuilder.append("NULL snippetList");
		}
		
		
		return myObjStringBuilder.toString();
	}
}
