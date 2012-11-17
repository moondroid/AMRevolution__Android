package eu.areamobile.android.apps.amrevolution.utils.NET;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Build;

/**
 * An Http client fluent api.
 * 
 * @author Andrea Tortorella
 *
 */
public abstract class Http {
	private Http(){ throw new AssertionError("This class could not be instantiated");};
	
	/**
	 * The default implementation of a request executor.
	 */
	public final static RequestExecutor DEFAULT_EXECUTOR = new URLConnectionExecutor();
	
	private volatile static ClientOptions CLIENT_OPTIONS = ClientOptions.defaults();
	
	private volatile static RequestExecutor sRequestExecutor = DEFAULT_EXECUTOR;
	
	/**
	 * Status code class of informational responses: 1xx
	 */
	public final static int STATUS_INFO = 1;
	
	/**
	 * Status code class of ok responses: 2xx
	 */
	public final static int STATUS_OK = 2;
	
	/**
	 * Status code class of redirect responses: 3xx
	 */
	public final static int STATUS_REDIRECT = 3;
	
	/**
	 * Status code class of client error responses: 4xx
	 */
	public final static int STATUS_CLIENT_ERROR = 4;
	
	/**
	 * Status code class of server error responses: 5xx
	 */
	public final static int STATUS_SERVER_ERROR = 5;
	
	
	/**
	 * This is an utility class that contains common kind of client server interactions
	 * as static methods.
	 * 
	 * Implementations are based on the building blocks provided by the Http class.
	 * You can subclass this for convenience, to collect all the requests in a common place.
	 * However this class cannot be instantiated: trying to do so will throw an AssertionError.
	 * 
	 * @author Andrea Tortorella
	 *
	 */
	public abstract static class Requests extends Http{
		private Requests(){super();}
		
		
		/**
		 * Issues a POST request to the provided url with a json body.
		 * @param url
		 * @param json
		 * @return
		 */
		public static ResponseStream json(String url,String json){
			return Http.post(url).body(ContentType.JSON,json).execute();
		}
		
		/**
		 * Uploads the at path to the url, through a POST request.
		 * See {@link #upload(String, File, boolean)}
		 * @param url
		 * @param path
		 * @param chunked
		 * @return
		 */
		public static ResponseStream upload(String url,String path,boolean chunked){
			return upload(url,new File(path),chunked);
		}
		
		/**
		 * Uploads the file at path to the url, through a POST request.
		 * If chunked is true the request will use Transfer-Encoding: chunked.
		 * Using this is usefull for big files, because the file content will
		 * not be fully buffered in memory, while building the request.
		 * @param url
		 * @param path
		 * @param chunked
		 * @return
		 */
		public static ResponseStream upload(String url,File file,boolean chunked){
			try{
				final EntityRequest req = Http.post(url);
				return (chunked?req.header(TransferEncoding.CHUNKED):req).body(file).execute();
			}catch(FileNotFoundException fnf){
				throw new RuntimeException(fnf);
			}
		}
	
		/**
		 * Issues a GET request to the url with params passed in.
		 * If the response is an OK response, the content is saved at
		 * path.
		 * If the operations succeds a new File pointing to path is returned, null otherwise.
		 * @param path
		 * @param url
		 * @param params
		 * @return
		 */
		public File download(String path,String url,String...params){
			ResponseStream s=Http.get(url,params).execute();
			if(s.statusClass==Http.STATUS_OK){
				return s.save(path);
			}else{
				return null;
			}
		}
	
	}
	
	
	
	/**
	 * Create a simple GET request line for url with eventual parameters,that can be sent synchronously to the server
	 * through {@link Request#execute()}
	 * @param url
	 * @param params
	 * @return
	 */
	public static SimpleRequest get(String url,String ...params){
		return makeRequest(Method.GET, url, params);
	}
	

	/**
	 * Create a simple GET request line for url,that can be sent synchronously to the server
	 * through {@link Request#execute()}
	 * @param url
	 * @param params
	 * @return
	 */
	public static SimpleRequest get(String url){
		return makeRequest(Method.GET, url);
	}
	
	public static SimpleRequest head(String url,String ...params){
		return makeRequest(Method.HEAD,url,params);
	}
	
	public static SimpleRequest head(String url){
		return makeRequest(Method.HEAD,url);
	}
	
	public static SimpleRequest delete(String url,String ...params){
		return makeRequest(Method.DELETE,url,params);
	}
	
	public static SimpleRequest delete(String url){
		return makeRequest(Method.DELETE,url);
	}
	
	
	
	public static EntityRequest post(String url,String ...params){
		return makeRequest(Method.POST,url,params);
	}
	
	public static EntityRequest post(String url){
		return makeRequest(Method.POST,url);
	}
	
	public static EntityRequest put(String url,String ...params){
		return makeRequest(Method.PUT,url,params);
	}
	
	public static EntityRequest put(String url){
		return makeRequest(Method.PUT,url);
	}
	
	public static EntityRequest patch(String url,String ...params){
		return makeRequest(Method.PATCH,url,params);
	}
	
	public static EntityRequest patch(String url){
		return makeRequest(Method.PATCH,url);
	}
	public static SimpleRequest connect(String url,String ...params){
		return makeRequest(Method.CONNECT, url, params);
	}
	
	public static SimpleRequest connect(String url){
		return makeRequest(Method.CONNECT, url);
	}
	
	public static EntityRequest trace(String url,String ...params){
		return makeRequest(Method.TRACE,url,params);
	}
	
	public static EntityRequest trace(String url){
		return makeRequest(Method.TRACE,url);
	}
	
	public static EntityRequest options(String url,String ...params){
		return makeRequest(Method.OPTIONS,url,params);
	}
	
	public static EntityRequest options(String url){
		return makeRequest(Method.OPTIONS,url);
	}
	
	private static RequestImpl makeRequest(Method method,String url,String ...params){
		final String prefix = url.indexOf("://")==-1?"http":"";
		final StringBuilder b = new StringBuilder(prefix).append(url);
		if(params!=null&&params.length>0){
			if(params.length%2==0){
				try {
					b.append('?').append(params[0]).append('=').append(URLEncoder.encode(params[1],"UTF-8"));
					for(int i=2;i<params.length;i+=2){
						b.append('&').append(params[i]).append('=').append(URLEncoder.encode(params[i+1], "UTF-8"));
					}
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}else{
				throw new IllegalArgumentException("Must provide an even number of params");
			}
		}
		URL cUrl;
		try {
			cUrl = new URL(b.toString());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return new RequestImpl(method, url,cUrl);
	}
	
	public static void setClientOptions(ClientOptions options){
		CLIENT_OPTIONS=options;
	}
	
	public static ClientOptions getCurrentOptions(){
		return CLIENT_OPTIONS;
	}
	
	public static RequestExecutor getRequestExecutor(){
		return sRequestExecutor;
	}
	
	public static void setRequestExecutor(RequestExecutor executor){
		sRequestExecutor=executor;
	}
	
	
	private static ResponseStream executeRequest(RequestInfo request) {
		final RequestExecutor executor = sRequestExecutor;
		final ClientOptions clientOptions = CLIENT_OPTIONS;
		return executor.executeRequest(request, clientOptions);
	}

	
	public static enum Method{
		GET("GET",true,false,true),
		POST("POST",true,true,false),
		PUT("PUT",true,true,false),
		DELETE("DELETE",true,false,false),
		HEAD("HEAD",false,false,true),
		PATCH("PATCH",true,true,false),
		OPTIONS("OPTIONS",true,true,false),
		TRACE("TRACE",true,true,false),
		CONNECT("CONNECT",true,false,false),
		;
		public final String method;
		public final boolean doInput;
		public final boolean doOutput;
		public final boolean useCaching;
		Method(String method,boolean doInput,boolean doOutput,boolean cache){
			this.method=method;
			this.doInput=doInput;
			this.doOutput=doOutput;
			this.useCaching =cache;
		}
	}
	
	public abstract static class Multipart{
		private Multipart(){};
	}
	
	public final static class ClientOptions{
		
		public String userAgent;
		public boolean keepAlive;
		public boolean followRedirects;
		ClientOptions(){
			userAgent = "Android/"+Build.VERSION.SDK_INT;
			keepAlive = false;
			followRedirects = false;
		}
		
		public static ClientOptions defaults(){
			return new ClientOptions();
		}
	}
	
	public static enum TransferEncoding{
		IDENTITY("identity",false),
		CHUNKED("chunked",false),
		GZIP("gzip",false),
		COMPRESS("compress",false),
		DEFLATE("deflate",false),
		;
		final String encoding;
		final boolean useChunked;
		private TransferEncoding(String encoding,boolean useChunked) {
			this.encoding=encoding;
			this.useChunked=useChunked;
		}
	}
	
	public static enum ContentType{
		JSON("text/json; charset=utf-8"),
		XML("text/xml; charset=utf-8"),
		TEXT("text/plain; charset=utf-8"),
		BINARY("application/octet-stream"),
		FORM("application/x-www-form-urlencoded")
		;
		final String content_type;
		ContentType(String content_type){
			this.content_type=content_type;
		}
	}
	
	public static interface RequestExecutor{
		public ResponseStream executeRequest(RequestInfo info,ClientOptions opts);
	}
	
	/**
	 * 
	 * @author Andrea Tortorella
	 *
	 */
	public static interface Request extends Callable<ResponseStream>{
		public ResponseStream execute();
		@Override
		public ResponseStream call();
	}
	
	public static interface SimpleRequest extends Request{
		public SimpleRequest header(String name,String value);
		public SimpleRequest header(ContentType content_type);
		public SimpleRequest header(TransferEncoding content_type);
		
	}
	
	
	public static interface EntityRequest extends SimpleRequest{
		@Override
		public EntityRequest header(String name,String value);
		@Override
		public EntityRequest header(ContentType content_type);
		@Override
		public EntityRequest header(TransferEncoding content_type);
		public Request body(String content_type,InputStream inputStream);
		public Request body(String content_type,long content_length,InputStream strem);
		public Request body(String textBody);
		public Request body(ContentType type,String content);
		public Request body(InputStream in);
		public Request body(String content_type,File file) throws FileNotFoundException;
		public Request body(File file) throws FileNotFoundException;
		public Request body(String paramName,String paramValue,String ...more);
		/**
		 * NOT YET IMPLEMENTED!!!! WILL THROW!!!
		 * @param multipart
		 * @return
		 */
		public Request body(Multipart multipart);
		public Request body(byte[] body);
		public Request body(String content_type,byte[] body);

	}
	
	public static class RequestInfo {

		public final HashMap<String, String> headers = new HashMap<String, String>();
		public final URL url;
		public final Method method;
		InputStream bodyStream;
		public final String baseUrl;
		
		RequestInfo(Method method,String baseUrl,URL composedUrl){
			this.method=method;
			this.url=composedUrl;
			this.baseUrl=baseUrl;
			headers.put("Referer", this.baseUrl);
			headers.put("Location", this.baseUrl);
		}
		
		public final InputStream getBodyStream(){
			return bodyStream;
		}
	}

	
	
	
	
	private static class RequestImpl extends RequestInfo implements EntityRequest{
		
		
		public RequestImpl(Method method,String baseUrl,URL composedUrl) {
			super(method, baseUrl, composedUrl);
		}
		
		@Override
		public RequestImpl header(String name, String value) {
			headers.put(name, value);
			return this;
		}


		@Override
		public RequestImpl header(ContentType content_type) {
			headers.put("Content-Type", content_type.content_type);
			return this;
		}

		

		@Override
		public EntityRequest header(TransferEncoding encoding) {
			headers.put("Transfer-Encoding",encoding.encoding);
			return this;
		}

		@Override
		public Request body(ContentType type, String content) {
			byte[] bytes= content.getBytes();
			return body(createContentType(type.content_type),bytes.length,new ByteArrayInputStream(bytes));
		}
		
		
		@Override
		public Request body(InputStream in) {
			return body(createContentType(ContentType.BINARY.content_type),-1l,in);
		}
		
		@Override
		public Request body(String textBody) {
			byte[] bytes=textBody.getBytes();
			return body(createContentType(ContentType.TEXT.content_type),bytes.length,new ByteArrayInputStream(bytes));
		}
		

		@Override
		public Request body(String paramName,String paramValue,String... more) {
			try{
				StringBuilder builder = new StringBuilder();
				builder.append(paramName).append('=').append(URLEncoder.encode(paramValue, "UTF-8"));
				if(more!=null&&more.length>0){
					if(more.length%2==1)throw new IllegalArgumentException("more parameters must be even");
					for(int i=0;i<more.length;i+=2){
						builder.append('&').append(more[i]).append('=').append(URLEncoder.encode(more[i+1],"UTF-8"));
					}
				}
				headers.put("Content-Value", ContentType.FORM.content_type);
				byte[] bytes= builder.toString().getBytes("UTF-8");
				return body(null,bytes.length,new ByteArrayInputStream(bytes));
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public Request body(String content_type,File file) throws FileNotFoundException {
			final FileInputStream fin = new FileInputStream(file);
			return body(content_type,file.length(), fin);
		}
		
		@Override
		public Request body(File file) throws FileNotFoundException {
			final FileInputStream fin = new FileInputStream(file);
			return body(createContentType(ContentType.BINARY.content_type),file.length(),fin);
		}
		
		@Override
		public Request body(byte[] body) {
			return body(createContentType(ContentType.BINARY.content_type),new ByteArrayInputStream(body));
		}

		@Override
		public Request body(String content_type, byte[] body) {
			return body(createContentType(content_type),body.length,new ByteArrayInputStream(body));
		}

		@Override
		public Request body(String content_type, InputStream inputStream) {
			return body(content_type,-1,inputStream);
		}
		@Override
		public Request body(String content_type,long content_length, InputStream inputStream) {
			if(content_type!=null){
				headers.put("Content-Type", content_type);
			}
			if(content_length!=-1){
				headers.put("Content-Length",Long.toString(content_length));
			}
			bodyStream=inputStream;
			return this;
		}
		
		private String createContentType(String content_type){
			return headers.containsKey("Content-Type")?null:content_type;
		}

		@Override
		public Request body(Multipart multipart) {
			throw new UnsupportedOperationException("Not yet implemented");
			//return this;
		}

		@Override
		public ResponseStream execute() {
			return Http.executeRequest(this);
		}
		
		@Override
		public ResponseStream call() {
			return Http.executeRequest(this);
		}

	}
	
	
	public abstract static class ResponseStream extends InputStream{
		public final int statusClass;
		public final int statusCode;
		public final String statusMessage;
		protected final InputStream mStream;
		
		
		protected ResponseStream(int statusCode,String statusMessage,InputStream in) {
			this.statusCode=statusCode;
			this.statusClass =statusCode/100;
			this.statusMessage=statusMessage;
			this.mStream=in;
		}


		public abstract long getTimestamp();

		public abstract String getHeader(String key);
		public abstract List<String> getHeaderFields(String key);
		public abstract Map<String,List<String>> getAllHeaders();
		
		public XmlPullParser asXml(){
			return asXml(false);
		}
		
		public XmlPullParser asXml(boolean checkContentType){
			if(checkContentType && !getHeader("Content-Type").startsWith("text/xml")) throw new RuntimeException();
			try {
				XmlPullParser xpp=XmlPullParserFactory.newInstance().newPullParser();
				xpp.setInput(asReader());
				return xpp;
			} catch (XmlPullParserException e) {
				throw new RuntimeException(e);
			}
		}
		
		public List<String> getCookies(){
			return getHeaderFields("Set-Cookie");
		}
		
		
		public Reader asReader(){
			String charset = getCharset();
			if(charset!=null){
				try {
					return new InputStreamReader(this, charset);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}else{
				throw new IllegalArgumentException("This seems not to be a character stream");
			}
		}
		
		
		
		public String asString(){
			StringWriter sw = new StringWriter();
			Reader reader = null;
			try{
				reader = asReader();
				char[] buff = new char[512];
				int read = -1;
				while((read = reader.read(buff))>0){
					sw.write(buff, 0, read);
				}
				return sw.toString();
			}catch(IOException e){
				throw new RuntimeException(e);
			}finally{
				try {
					this.close();
				} catch (IOException e) {}
			}
		}
		public boolean isText(){
			return getCharset()!=null;
		}
		
		public String getCharset(){
			String charset = searchCharset();
			if(charset!=null)return charset;
			String header = getHeader("Content-Type");
			if(header!=null&& header.startsWith("text/"))return "utf-8";
			return null;
			
		}
		
		private String searchCharset(){
			String charset = null;
			final String content_type = getHeader("Content-Type");
			if(content_type==null) return charset;
			final String[] splitted = content_type.replace(" ", "").split(";");
			for(String part:splitted){
				if(part.startsWith("charset=")){
					charset = part.split("=",2)[1];
				}
			}
			return charset;
		}
		
		public File save(String path){
			File f = new File(path);
			save(f);
			return f;
		}
		
		
		
		
		public void save(File path){
			if(statusCode/100==2){
				if(path.exists()){
					if(path.isDirectory())throw new RuntimeException("File: "+path.getAbsolutePath()+" is a directory");
					if(!path.canWrite())throw new RuntimeException("File: "+path.getAbsolutePath()+ " is not writable");
				}else{
					File dir =path.getParentFile();
					if(!dir.exists()){
						if(!dir.mkdirs())throw new RuntimeException("Failed to create directory: "+dir.getAbsolutePath());
					}
					try {
						path.createNewFile();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}

				FileOutputStream out = null;
				try{
					out = new FileOutputStream(path);
					byte[] buffer = new byte[1024];
					int read = -1;
					while((read=this.read(buffer))>0){
						out.write(buffer, 0, read);
					}
					
				}catch(IOException e){
					throw new RuntimeException(e);
				}finally{
					try{
						if(out!=null){
							out.close();
						}
						this.close();
					}catch (IOException e) {}
				}
				
				
			}
		}
		
	}
	
	
	
}
