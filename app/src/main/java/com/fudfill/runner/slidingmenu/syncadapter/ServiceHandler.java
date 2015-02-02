package com.fudfill.runner.slidingmenu.syncadapter;

import android.util.Log;

import com.fudfill.runner.slidingmenu.common.FudfillConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;




    public ServiceHandler() {

    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCallWithS(String url, int method,
                                  String body) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;
        HttpPut httpput;

        if(method != PUT)
        {
            Log.d("Fudfill","Valid only for PUT Requests: "+method);
            return null;
        }
        try {
            httpput = new HttpPut(url);
            StringEntity entity=null;
            if (body != null) {
                entity = new StringEntity(body, HTTP.UTF_8);
                entity.setContentType("application/json");
                httpput.setEntity(entity);
            }

            httpResponse = httpClient.execute(httpput);
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            return response;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params) {
        try {
            // http client
            DefaultHttpClient httpClient;
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, FudfillConfig.timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            HttpConnectionParams.setSoTimeout(httpParameters, FudfillConfig.timeoutSocket);

            httpClient = new DefaultHttpClient(httpParameters);

            // Checking http request method type

            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;

    }
}
