package utilities;

import java.io.OutputStream;

import javax.swing.JTextArea;

public class StreamText extends OutputStream{
	
	private JTextArea text;
	
	public StreamText(JTextArea salida){
		this.text = salida;
	}
	
	public void write(int aByte){
		char aChar = (char)aByte;
		text.append(""+aChar);
	}
	 
}
