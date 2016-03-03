package com.project.sysumobilelibrary.utils;

import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MyVolley {
	private static final String TAG = "MyVolley";
	
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	
	public MyVolley(Context context) {
		requestQueue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
	}
	
	public void addPostStringRequest(String url, Listener<String> listener, ErrorListener errorListener, final Map<String, String> map, String tag){
		StringRequest stringRequest = new StringRequest(Method.POST, url, listener, errorListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return map;
			}
		};
		stringRequest.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		requestQueue.add(stringRequest);
	}
	public void addGetStringRequest(String url, Listener<String> listener, ErrorListener errorListener, final Map<String, String> map, String tag){
		StringRequest stringRequest = new StringRequest(Method.GET, url, listener, errorListener){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return map;
			}
		};
		stringRequest.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		requestQueue.add(stringRequest);
	}
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	
	
	
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		requestQueue.add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}

}
