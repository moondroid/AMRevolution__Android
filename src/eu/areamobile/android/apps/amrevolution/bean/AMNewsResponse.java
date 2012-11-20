package eu.areamobile.android.apps.amrevolution.bean;

import java.util.ArrayList;

public class AMNewsResponse {
	private ArrayList<AMBeanNews> beanNewsList;
	

	/**
	 * @param beanNewsList
	 */
	public AMNewsResponse(ArrayList<AMBeanNews> beanNewsList) {
		this.beanNewsList = beanNewsList;
	}
	
	public AMNewsResponse() {
		this.beanNewsList = new ArrayList<AMBeanNews>();
	}

	/**
	 * @return the beanNewsList
	 */
	public ArrayList<AMBeanNews> getBeanNewsList() {
		return beanNewsList;
	}

	/**
	 * @param beanNewsList the beanNewsList to set
	 */
	public void setBeanNewsList(ArrayList<AMBeanNews> beanNewsList) {
		this.beanNewsList = beanNewsList;
	}
	
	public void addNews(AMBeanNews myNews) {
		beanNewsList.add(myNews);
	}
	
	
	@Override
	public String toString() {
		String metaCharSeparator = "\n";
		
		StringBuilder myObjStringBuilder = new StringBuilder();
		
		if(this.beanNewsList != null) {
			if(this.beanNewsList.size() < 1) {
				myObjStringBuilder.append("Empty beanNewsList");
			}
			else {
				for(int i = 0; i < beanNewsList.size(); i++) {
					AMBeanNews currentNews = beanNewsList.get(i);
					
					String myRowValue = "Pos " + i + "=> " + currentNews.toString();
					
					myObjStringBuilder.append(metaCharSeparator + myRowValue);
				}
			}
		}
		else {
			myObjStringBuilder.append("NULL beanNewsList");
		}
		
		
		return myObjStringBuilder.toString();
	}
}
