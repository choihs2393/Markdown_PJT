package com.ggbg.note.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class HttpsConnection {
	
	public void postRequest(String exceptionName,String email) {
	    try {
	        //   URL 설정하고 접속하기 
	        URL url = new URL("https://meeting.ssafy.com/hooks/yoddrp4rib8edf1kw9998y4jge"); // URL 설정 

	        HttpsURLConnection http = (HttpsURLConnection) url.openConnection(); // 접속 

	        http.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
	        http.setRequestProperty("Content-Type", "application/json");
	        http.setRequestProperty("charset", "UTF-8");
	        http.setDefaultUseCaches(false);
	        http.setDoOutput(true); // 서버로 쓰기 모드 지정
	        http.setConnectTimeout(5000);
	        http.setRequestMethod("POST"); // 전송 방식은 POST
	        
	        JSONObject jsonObject = new JSONObject();
	        
	        StringWriter stringWriter = new StringWriter();
	     
	        jsonObject.put("text", "Exception : " + exceptionName + "\n" + "Account : " + email);

	        
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(http.getOutputStream());
	        outputStreamWriter.write(jsonObject.toString());
	        outputStreamWriter.flush();
	        outputStreamWriter.close();
	        http.getInputStream();
	    } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

}
