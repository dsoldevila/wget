package Wget;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The program Wget download resources from URLs 
 * and applies filters needed.
 */
public class Wget {
	private static PageDownload pageDownload;
	private static final int START_INDEX_EXTRA_PARAMS = 2;
	private static final int NUMBER_EXTRA_PARAMS = 3;
	static final String[] info = {"[Info]", "[Error]", "[Downl]"};
	
	Wget(){}
	
	/**
	 * Main method to read the file "urls.txt" that contains the URLs
	 * to use in the program.
	 * @param arguments
	 */
	public static void main(String args[]) {
		String[] extraParameters;
		try {
			boolean checked = checkParameters(args);
			if(checked){
				System.out.println(info[0]+" Correct parameters.");
				extraParameters = getExtraParams(args);
				
			}else{
				throw new IllegalArgumentException();
			}	
			FileReader fileReader;
			fileReader = new FileReader(args[1]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String currentURL;
			PageDownload.setExtraParameters(extraParameters);
			int countID=0;
			while ((currentURL = bufferedReader.readLine()) != null) {
				pageDownload = new PageDownload(currentURL, countID++);
				pageDownload.start();		
			}
			fileReader.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println(info[1]+" URLs file not found.");
		} catch (IOException e) {
			System.out.println(info[1]+" Input/output problems.occured.");
		} catch (IllegalArgumentException e){
			System.out.print(info[1]+" Invalid arguments");
		}
	}
	
	/**
	 * Check if the parameters -f file exists
	 * and check optional parameters.
	 * @param arguments
	 * @return checked ok?
	 */
	public static boolean checkParameters(String _args[]){
		if(_args.length >= START_INDEX_EXTRA_PARAMS){
			if(!(_args[0].equals("-f")) && (_args[1].substring(_args[1].lastIndexOf("."))) != ".txt"){
				return false;
			}
			if (_args.length >START_INDEX_EXTRA_PARAMS){
				for(int i=START_INDEX_EXTRA_PARAMS; i<=_args.length-1;i++){
					if(!(_args[i].equals("-a")) && !(_args[i].equals("-z")) && !(_args[i].equals("-gz"))){
						return false;
					}
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * Get the extra params.
	 * @params arguments
	 * @return String[] extra params
	 */
	public static String[] getExtraParams(String _args[]){
		String[] processedParams = new String[NUMBER_EXTRA_PARAMS];
		if(_args.length > START_INDEX_EXTRA_PARAMS){
			for(int i=2; i<=_args.length-1;i++){
				processedParams[i-START_INDEX_EXTRA_PARAMS] = _args[i];
			}
		}
		return processedParams;
	}
		
}
