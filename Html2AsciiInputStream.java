package Wget;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Filters HTML tags
 */
public class Html2AsciiInputStream extends FilterInputStream{
	private static int[] beginningSymbols = {'<', '!', '-', '-'};
	private int beginningSymbolsIndex;
	private static int[] endingSymbols = {'-', '-', '>'};
	private int endingSymbolsIndex;
	
	private boolean isComentary;
	private boolean ignore = false;
	public static final String extension = ".asc";
	int character;
	
	public Html2AsciiInputStream(InputStream _in) {
		super(_in);
		beginningSymbolsIndex = 0;
		endingSymbolsIndex = 0;
		isComentary = false;
	}
	
	/*
	 * Return filtered character
	 */
	@Override
	public int read(){
		//this.in.read()
		/*int character = 0;
		try {
			// Ascii code caracter
			character = this.in.read();
			if (character == '<'){
				ignore = true;
			}else if(character == '>'){
				ignore = false;	
				character = 0;
			}
			if(ignore){
				character = 0;
			}
		} catch (IOException e) {
			System.out.println("Error filtering HTML byte");
		}
		return character;*/
		character = 0;
		try {
			
			character = this.in.read();
			
			//SE COMPRUEBA SI TENEMOS QUE COMENZAR A IGNORAR PORQUE HAY UN TAG O UN COMENTARIO
			if(character == beginningSymbols[beginningSymbolsIndex]){
				ignore = true; //Si el caracter == beginningSymbols[0] (que es igual a '<') comenzamos a ignorar
				beginningSymbolsIndex++; //sumamos uno al indice, si llega a 4 significa que habremos encontrado un comentario (<!--)
				
			}else{
				beginningSymbolsIndex = 0; //si se rompe la secuencia comenzamos de nuevo
			}
			
			if(beginningSymbolsIndex == beginningSymbols.length) //Aquí se comprueba si nos hemos encontrado un comentario
				isComentary = true;
			
			//SE COMPRUEBA SI TENEMOS QUE DEJAR DE IGNORAR
			if(isComentary == false && character == endingSymbols[endingSymbols.length-1]){
				ignore = false;
				character = 0;
			}else{
				if(character == endingSymbols[endingSymbolsIndex]){
					beginningSymbolsIndex++; 
					
				}else{
					beginningSymbolsIndex = 0;
				}
				if(endingSymbolsIndex == endingSymbols.length){
					isComentary = false;
					character = 0;
					ignore = false;
				}
				
			}
			
			if(ignore == true)
				character = 0;
			
		} catch (IOException e) {
			System.out.println("Error filtering HTML byte");
		}
		return character;
		
	}
}
