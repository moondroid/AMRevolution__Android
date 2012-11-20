package eu.areamobile.android.apps.amrevolution.utils.NET;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import eu.areamobile.android.apps.amrevolution.utils.NET.Http.RequestExecutor;
import eu.areamobile.android.apps.amrevolution.utils.NET.Http.ResponseStream;

/**
 * 
 * The default implementation of {@link Http.RequestExecutor} used by {@link Http} client api.
 * 
 * @author Andrea Tortorella
 *
 */
final class URLConnectionExecutor implements RequestExecutor {
	
	
	/**
	 * The {@link ResponseStream} implementation returned by this {@link RequestExecutor}
	 * 
	 * @author Andrea Tortorella
	 *
	 */
	private static class URLConnectionResponseStream extends Http.ResponseStream{
		final HttpURLConnection mConnection;
		
		URLConnectionResponseStream(HttpURLConnection conn, InputStream stream,
				int statusCode, String message) {
			super(statusCode,message,stream);
			mConnection = conn;
		}
		
		@Override
		public String getHeader(String key) {
			return mConnection.getHeaderField(key);
		}
		
		@Override
		public long getTimestamp() {
			return mConnection.getDate();
		}
		
		@Override
		public int read() throws IOException {
			if(mStream !=null){
				return mStream.read();
			}else{
				return -1;
			}
		}

		@Override
		public int read(byte[] buffer) throws IOException {
			if(mStream != null){
				return mStream.read(buffer);
			}else{
				return -1;
			}
		}
		
		@Override
		public int read(byte[] buffer, int offset, int length)throws IOException {
			if(mStream!=null){
				return mStream.read(buffer, offset, length);
			}else{
				return -1;
			}
		}
		
		@Override
		public int available() throws IOException {
			if(mStream!=null){
				return mStream.available();
			}else{
				return 0;
			}
		}
		
		@Override
		public long skip(long byteCount) throws IOException {
			if(mStream != null){
				return mStream.skip(byteCount);
			}else{
				return 0;
			}
		}
		
		@Override
		public void mark(int readlimit) {
			if(mStream != null){
				mStream.mark(readlimit);
			}
		}
		
		@Override
		public boolean markSupported() {
			if(mStream!=null){
				return mStream.markSupported();
			}
			return false;
		}
		
		@Override
		public synchronized void reset() throws IOException {
			if(mStream!=null){
				mStream.reset();
			}
		}
		
		@Override
		public void close() {
			if(mStream !=null){
				try{
					mStream.close();
				}catch(IOException e){
					throw new RuntimeException(e);
				}
			}
			mConnection.disconnect();
		}

		@Override
		public List<String> getHeaderFields(String key) {
			return mConnection.getHeaderFields().get(key);
		}

		@Override
		public Map<String, List<String>> getAllHeaders() {
			return mConnection.getHeaderFields();
		}
	}
	
	@Override
	public ResponseStream executeRequest(Http.RequestInfo request, Http.ClientOptions client) {
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream stream = null;
		boolean disconnect = true;
		
		try{
			URL url = request.url;
			
			conn = (HttpURLConnection)url.openConnection();
			
			conn.setDoInput(request.method.doInput);
			conn.setDoOutput(request.method.doOutput);
			conn.setRequestMethod(request.method.method);
			
			conn.setInstanceFollowRedirects(client.followRedirects);
			conn.setAllowUserInteraction(false);
			if(!client.keepAlive) conn.setRequestProperty("Connection", "Close");
			conn.setRequestProperty("User-Agent", client.userAgent);
			conn.setUseCaches(request.method.useCaching);
			boolean useChunked = "chunked".equals(request.headers.remove("Transfer-Encoding"));
			int contentLength= -1;
			if(request.headers.containsKey("Content-Length")){
				contentLength = Integer.parseInt(request.headers.remove("Content-Length"));
				
			}
			for(Map.Entry<String, String> header:request.headers.entrySet()){
				conn.setRequestProperty(header.getKey(), header.getValue());
			}
			
			if(request.bodyStream==null){
				conn.setFixedLengthStreamingMode(0);
			}else{
				if(useChunked){
					conn.setChunkedStreamingMode(0);
				}else if(contentLength!=-1){
					conn.setFixedLengthStreamingMode(contentLength);
				}
				out = conn.getOutputStream();
				byte[] buffer = new byte[1024];
				int read;
				while((read = request.bodyStream.read(buffer))>0){
					out.write(buffer,0,read);
				}
				out.flush();
			}
			
			conn.connect();
			
			final int statusCode = conn.getResponseCode();
			if(statusCode==-1)throw new RuntimeException("Error in connection");
			
			final String message = conn.getResponseMessage();
			final int statusClass = statusCode/100;
			
			switch(statusClass){
			case Http.STATUS_OK:
				if("gzip".equals(conn.getContentEncoding())){
					stream = new GZIPInputStream(conn.getInputStream());	
				}else{
					stream = conn.getInputStream();
				}
				break;
			case Http.STATUS_CLIENT_ERROR:
			case Http.STATUS_SERVER_ERROR:
				if("gzip".equals(conn.getContentEncoding())){
					stream = new GZIPInputStream(conn.getErrorStream());	
				}else{
					stream = conn.getErrorStream();
				}
				break;
			default:
				stream=null;
				
			}
			
			disconnect = false;
			
			return new URLConnectionResponseStream(conn,stream,statusCode,message);
		}catch(IOException e){
			throw new RuntimeException(e);
		}finally{
			if(out!=null){
				try{
					out.close();
				}catch(IOException e){}
			}
			if(disconnect){
				if(stream!=null){
					try{
						stream.close();
					}catch(IOException e){}
					conn.disconnect();
				}
			}
		}
	}
}