package sgo.filtros;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;



public final class RequestWrapper extends HttpServletRequestWrapper {
	private static Logger logger = Logger.getLogger(RequestWrapper.class);
	
	/*public RequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}*/
	
	private String body;
	 public RequestWrapper(HttpServletRequest request) throws IOException {
	   super(request);
	   StringBuilder stringBuilder = new StringBuilder();
	   BufferedReader bufferedReader = null;
	   try {
	     InputStream inputStream = request.getInputStream();
	     if (inputStream != null) {
	       bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	       char[] charBuffer = new char[128];
	       int bytesRead = -1;
	       while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	         stringBuilder.append(charBuffer, 0, bytesRead);
	       }
	     } else {
	       stringBuilder.append("");
	     }
	   } catch (IOException ex) {
	       throw ex;
	   } finally {
	     if (bufferedReader != null) {
	       try {
	         bufferedReader.close();
	       } catch (IOException ex) {
	         throw ex;
	       }
	     }
	   }
	//   System.out.println("body old = "+stringBuilder.toString());
	   
	   body = cleanXSS(stringBuilder.toString());
	   
	//   System.out.println("body new = "+body);
	 }

	 @Override
	 public ServletInputStream getInputStream() throws IOException {
	   final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
	   ServletInputStream servletInputStream = new ServletInputStream() {
	     public int read() throws IOException {
	       return byteArrayInputStream.read();
	     }

		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setReadListener(ReadListener arg0) {
			// TODO Auto-generated method stub
			
		}
	   };
	   return servletInputStream;
	 }

	 @Override
	 public BufferedReader getReader() throws IOException {
	   return new BufferedReader(new InputStreamReader(this.getInputStream()));
	 }

	 public String getBody() {
	   return cleanXSS(this.body);
	 }
	 
	 
	 
	 @Override
	public String[] getParameterValues(String parameter) {
		//logger.info("InarameterValues .. parameter .......");
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = cleanXSS(values[i]);
			//System.out.println("encodedValues["+i+"]"+encodedValues[i]);
		}
		return encodedValues;
	}

	 @Override
	public String getParameter(String parameter) {
		//logger.info("Inarameter .. parameter .......");
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		
		if (!this.body.isEmpty()){
			String s = cleanXSS(getBody());
			this.body = s;
		}
		//System.out.println("value P: "+this.body);
		//System.out.println("value P: "+value);
		//logger.info("Inarameter RequestWrapper ........ value .......");
		return cleanXSS(value);
	}

	 /*@Override
	public String getHeader(String name) {
		logger.info("Ineader .. parameter .......");
		String value = super.getHeader(name);
		if (value == null)
			return null;
		logger.info("Ineader RequestWrapper ........... value ....");
		System.out.println("value H:"+value);
		return cleanXSS(value);
		return value;
	}*/

	private String cleanXSS(String value) {
		// You'll need to remove the spaces from the html entities below
		//logger.info("InnXSS RequestWrapper ..............." + value);
		
		//value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
		//value = value.replaceAll("'", "& #39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

		value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
		value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
		value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
		value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
		value = value.replaceAll("<", "").replaceAll(">", "");
		value = value.replaceAll("<script>", "");
		value = value.replaceAll("</script>", "");
		//logger.info("OutnXSS RequestWrapper ........ value ......." + value);
		return value;
	}
	
	
	
}