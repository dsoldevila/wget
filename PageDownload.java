package Wget;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.IOException;

/**
 * Downloads a page and writes to disk.
 */
public class PageDownload extends Thread{
	private volatile int countID;
	private static boolean a,z,gz;
	private String url;
	private String fileName;
	private InputStream IS;
	private OutputStream OS;
	private GZIPOutputStream tmpGZOS;
	
	public PageDownload(String _url, int _countID){
		countID = _countID;
		url = _url;		
	}

	/**
	 * Method to run threads (PageDownload threads)
	 */
	@Override
	public void run(){
		
		download2Disk(url);		
	}
	
	/** 
	 * Receive a url, connect to them, receive an IS and write 
	 * to the OS byte to byte.
	 * @param _url The URL to open a connection
	 */
	public synchronized void download2Disk(String _url) {
		try {
			URL url;
			url = new URL(_url);
			fileName = getNameFile(url.getFile());	
			URLConnection urlConnection;
			urlConnection = url.openConnection();
			IS = getInputStream(urlConnection);
			OS = getOutputStream();
			int readByte;
			while((readByte = IS.read()) != -1){
				OS.write(readByte);
			}
			IS.close();
			OS.close();	
			System.out.println(this.toString());
		} catch (MalformedURLException e) {
			System.out.println(Wget.info[1]+" "+_url+" malformed.");
		} catch(IOException e){
			System.out.println(Wget.info[1]+" "+_url+" was not found.");
		}
	}
	
	/**
	 * Returns an InputStream, depending on input extraParameters HTML code will be filtered or not
	 * @param _urlConnection 
	 * @return (url) InputStream
	 * @throws IOException
	 */
	private synchronized InputStream getInputStream(URLConnection _urlConnection) throws IOException{
		IS = _urlConnection.getInputStream();
		if((a) && (_urlConnection.getContentType().contains("text/html"))){
			IS = new Html2AsciiInputStream(IS);
			fileName= fileName+Html2AsciiInputStream.extension;	
		}
		return IS;
	}
	
	/**
	 * It creates an OuputStream depending on input extraParameters
	 * @return OutputStream
	 * @throws IOException
	 */
	private synchronized OutputStream getOutputStream() throws IOException{
		String ZipEntryName = fileName;
		if(!z && !gz){
			OS = new FileOutputStream(fileName);
		}else if(z && gz){
			fileName= fileName+".zip.gz";
			tmpGZOS = new GZIPOutputStream(new FileOutputStream(fileName));	
			OS = new ZipOutputStream(tmpGZOS);
			ZipEntry ze= new ZipEntry(ZipEntryName);
	    	((ZipOutputStream) OS).putNextEntry(ze);
		}else if(z && !gz){
			fileName= fileName+".zip";
			OS = new ZipOutputStream(new FileOutputStream(fileName));
			ZipEntry ze= new ZipEntry(ZipEntryName);
	    	((ZipOutputStream) OS).putNextEntry(ze);	
		}else if(gz && !z){
			fileName= fileName+".gz";
			OS = new GZIPOutputStream(new FileOutputStream(fileName));
		}
		return OS;
	}
	
	/**
	 * Translate a URL to a file String Wget nomenclature.
	 * @param URL needed to translate  
	 * @return  filename of the file
	 */
	private String getNameFile(String _url){
		String saveFile; 
		String name;
		String extension;
		_url = _url.substring(_url.lastIndexOf("/")+1);
		if(_url != ""){
			name = _url.substring(0, _url.lastIndexOf(".")); 
			extension = _url.substring(_url.lastIndexOf("."));
			saveFile = name + countID + extension;
		}else{
			saveFile = "index"+countID+".html";
		}
		countID++;
		return saveFile;
	}
	
	@Override
	public String toString(){
		return Wget.info[2]+" "+url+" -> "+fileName+".";
	}

	/**
	 * Set the extra parameters needed for the filters
	 * @param extra parameters
	 */
	public static void setExtraParameters(String[] _extraParameters) {
		for(String extraParameter:_extraParameters){
			if(extraParameter!=null && extraParameter.equals("-a")){
				a = true;
			}else if(extraParameter!=null &&extraParameter.equals("-z")){
				z = true;
			}else if(extraParameter!=null &&extraParameter.equals("-gz")){
				gz = true;
			}
		}
	}
	
	
}
