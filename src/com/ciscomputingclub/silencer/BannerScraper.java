/** BannerScraper.java */
package com.ciscomputingclub.silencer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.util.Log;

/****************************************************************
 * com.ciscomputingclub.silencer.BannerScraper
 * 
 * @author GVSU Computing Club
 * @version 1.0
 ***************************************************************/
public class BannerScraper {
	static final String URL = "https://mybanner.gvsu.edu/PROD/twbkwbis.P_ValLogin";
	static final String weekURL = "https://mybanner.gvsu.edu/PROD/bwskcrse.P_CrseSchdDetl";

	Document landingPage;
	String userGNumber;
	String userPassword;

	String sessionId;

	BannerScraper(String gNumber, String password) {
		userGNumber = gNumber;
		userPassword = password;
	}

	private HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params,
				HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	private String fetchSessionId() {
		// InputStream content = null;
		try {

			HttpClient httpclient = createHttpClient();
			HttpPost httppost = new HttpPost(URL);
			httppost.setHeader("Cookie", "TESTID=SET");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("sid", userGNumber));
			nameValuePairs.add(new BasicNameValuePair("PIN", userPassword));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			// content = response.getEntity().getContent();

			// Data sessionId = response.getAllHeaders()[0].getName();
			// Server sessionId +=
			// response.getAllHeaders()[1].getName();
			// Content-Length sessionId +=
			// response.getAllHeaders()[2].getName();
			sessionId = response.getAllHeaders()[3].getValue().split("=")[1];
			// Keep Alive sessionId +=
			// response.getAllHeaders()[5].getName();

		} catch (Exception e) {
			Log.v("[GET REQUEST]", "Network exception", e);
		}
		/*
		 * try { landingPage = Jsoup.parse(content, null,
		 * "https://mybanner.gvsu.edu/"); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		// return landingPage.body().text();
		return sessionId;

	}

	String fetchWeek() {
		InputStream content = null;
		try {

			HttpClient httpclient = createHttpClient();
			HttpPost httppost = new HttpPost(weekURL);
			httppost.setHeader("Cookie", "TESTID=SET;SESSID="
					+ fetchSessionId());

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("term_in", "201210"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);
			content = response.getEntity().getContent();

			// Data sessionId = response.getAllHeaders()[0].getName();
			// Server sessionId +=
			// response.getAllHeaders()[1].getName();
			// Content-Length sessionId +=
			// response.getAllHeaders()[2].getName();
			// sessionId =
			// response.getAllHeaders()[3].getValue().split("=")[1];
			// Keep Alive sessionId +=
			// response.getAllHeaders()[5].getName();

		} catch (Exception e) {
			Log.v("[GET REQUEST]", "Network exception", e);
		}

		try {
			landingPage = Jsoup.parse(content, null,
					"https://mybanner.gvsu.edu/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Elements tableRows = landingPage.select("table[summary*=BORDER]");

		return tableRows.toString();
		// return sessionId;

	}

}
