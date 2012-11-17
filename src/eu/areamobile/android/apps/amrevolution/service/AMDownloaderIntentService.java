package eu.areamobile.android.apps.amrevolution.service;

import java.util.ArrayList;

import com.google.gson.Gson;

import eu.areamobile.android.apps.amrevolution.bean.AMBeanCodesnippet;
import eu.areamobile.android.apps.amrevolution.bean.AMBeanNews;
import eu.areamobile.android.apps.amrevolution.bean.AMNewsRequest;
import eu.areamobile.android.apps.amrevolution.bean.AMNewsResponse;
import eu.areamobile.android.apps.amrevolution.bean.AMSnippetRequest;
import eu.areamobile.android.apps.amrevolution.bean.AMSnippetResponse;
import eu.areamobile.android.apps.amrevolution.provider.AMRevolutionContract;
import eu.areamobile.android.apps.amrevolution.utils.Constants;
import eu.areamobile.android.apps.amrevolution.utils.NET.Http;
import eu.areamobile.android.apps.amrevolution.utils.NET.Http.Request;
import eu.areamobile.android.apps.amrevolution.utils.NET.Http.Requests;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AMDownloaderIntentService extends IntentService {
	
	public AMDownloaderIntentService() {
        super("AMDownloaderIntentService");
    }

	@Override
	protected void onHandleIntent(Intent intent) {
		String section = "";
		String modality = "";
		String custom_value = "";
		
		Bundle intentBundle = intent.getExtras();
		
		if(intentBundle != null) {
			if(intent.hasExtra(Constants.SECTION_KEY)) {
				section = intentBundle.getString(Constants.SECTION_KEY);
			}
			
			if(intent.hasExtra(Constants.MODALITY_KEY)) {
				modality = intentBundle.getString(Constants.MODALITY_KEY);
			}
			
			if(intent.hasExtra(Constants.CUSTOMVALUE_KEY)) {
				custom_value = intentBundle.getString(Constants.CUSTOMVALUE_KEY);
			}
		}
		
		if(section.compareToIgnoreCase(Constants.SECTION_NEWS) == 0) {
			performNewsRequest(modality, custom_value);
		}
		// fetch all news which timestamps are bigger than timestamp passed as custom_value
		else if(section.compareToIgnoreCase(Constants.SECTION_SNIPPET) == 0) {
			performSnippetRequest(modality, custom_value);
		}
	}

	
	public static void startDownloaderService(Context packageContext, String section, String modality, String custom_value) {
    	Intent msgIntent = new Intent(packageContext, AMDownloaderIntentService.class);
    	msgIntent.putExtra(Constants.SECTION_KEY, section);
    	msgIntent.putExtra(Constants.MODALITY_KEY, modality);
    	msgIntent.putExtra(Constants.CUSTOMVALUE_KEY, custom_value);
    	
    	packageContext.startService(msgIntent);
    }
	
	
	private void performNewsRequest(String modality, String custom_value) {
		Gson gson = new Gson();
		
		Integer custom_value_integer = Integer.parseInt(custom_value);
		
		AMNewsRequest myAMNewsRequest = new AMNewsRequest();
		
		// fetch news matching the id passed as custom_value
		if(modality.compareToIgnoreCase(Constants.MODALITY_ID) == 0) {
			myAMNewsRequest.setId(custom_value_integer.intValue());
		}
		// fetch all news which timestamps are bigger than timestamp passed as custom_value
		else if(modality.compareToIgnoreCase(Constants.MODALITY_TIMESTAMP) == 0) {
			myAMNewsRequest.setTimestamp_lastDownload(custom_value_integer.intValue());
		}
		
		String newsRequestJsonFormat = gson.toJson(myAMNewsRequest);
		
		String myResponse = Requests.json(Constants.NEWS_SERVICE, newsRequestJsonFormat).asString();
		
		if(myResponse != null) {
			AMNewsResponse myAMNewsResponse = gson.fromJson(myResponse, AMNewsResponse.class);
			Log.i("AMDownloaderIntentService", "News service: " + myAMNewsResponse.toString());
			
			// loop through all the news
			ArrayList<AMBeanNews> myAMBeanNewsList = myAMNewsResponse.getBeanNewsList();
			for(AMBeanNews myAMBeanNews : myAMBeanNewsList) {
				insertToNews(myAMBeanNews);
			}
		}
		else {
			Log.i("AMDownloaderIntentService", "Cannot cast string downloaded into AMNewsResponse");
		}
	}
	
	private void performSnippetRequest(String modality, String custom_value) {
		Gson gson = new Gson();
		
		Integer custom_value_integer = Integer.parseInt(custom_value);
		
		AMSnippetRequest myAMSnippetRequest = new AMSnippetRequest();
		
		// fetch snippets matching the id passed as custom_value
		if(modality.compareToIgnoreCase(Constants.MODALITY_ID) == 0) {
			myAMSnippetRequest.setId(custom_value_integer.intValue());
		}
		// fetch all snippets which timestamps are bigger than timestamp passed as custom_value
		else if(modality.compareToIgnoreCase(Constants.MODALITY_TIMESTAMP) == 0) {
			myAMSnippetRequest.setTimestamp_lastDownload(custom_value_integer.intValue());
		}
		
		String snippetRequestJsonFormat = gson.toJson(myAMSnippetRequest);
		
		String myResponse = Requests.json(Constants.SNIPPET_SERVICE, snippetRequestJsonFormat).asString();
		
		if(myResponse != null) {
			AMSnippetResponse myAMSnippetResponse = gson.fromJson(myResponse, AMSnippetResponse.class);
			Log.i("AMDownloaderIntentService", "Snippet service: " + myAMSnippetResponse.toString());
			
			// loop through all the news
			ArrayList<AMBeanCodesnippet> myAMBeanCodesnippetList = myAMSnippetResponse.getSnippetList();
			for(AMBeanCodesnippet myAMBeanCodesnippet : myAMBeanCodesnippetList) {
				insertToSnippets(myAMBeanCodesnippet);
			}
		}
		else {
			Log.i("AMDownloaderIntentService", "Cannot cast string downloaded into AMSnippetResponse");
		}
	}
	
	
	private void insertToNews(AMBeanNews myAMBeanNews) {
		// insert data through content provider
		ContentResolver myContentResolver = this.getContentResolver();
		
		ContentValues myContentValues = AMRevolutionContract.News.values(myAMBeanNews.getId(),
				myAMBeanNews.getTimestamp(), myAMBeanNews.getTitle(), myAMBeanNews.getBody(),
				myAMBeanNews.getImg_url(), myAMBeanNews.getWeb_url());
		
		myContentResolver.insert(AMRevolutionContract.News.CONTENT_URI, myContentValues);
	}
	
	
	private void insertToSnippets(AMBeanCodesnippet myAMBeanCodesnippet) {
		// insert data through content provider
		ContentResolver myContentResolver = this.getContentResolver();
		
		ContentValues myContentValues = AMRevolutionContract.Snippets.values(myAMBeanCodesnippet.getId(),
				myAMBeanCodesnippet.getTimestamp(), myAMBeanCodesnippet.getLanguage_type(), myAMBeanCodesnippet.getTitle(), 
				myAMBeanCodesnippet.getCode());
		
		myContentResolver.insert(AMRevolutionContract.Snippets.CONTENT_URI, myContentValues);
	}
}