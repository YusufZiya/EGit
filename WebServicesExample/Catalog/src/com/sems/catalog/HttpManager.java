package com.sems.catalog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class HttpManager {

	public static String GetData(String Uri){
		
		AndroidHttpClient client=AndroidHttpClient.newInstance("AndroidAgent");
		HttpGet request=new HttpGet(Uri);
		HttpResponse response;
		
		try {
			response=client.execute(request);
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		finally{
			client.close();
		}
	}
}
