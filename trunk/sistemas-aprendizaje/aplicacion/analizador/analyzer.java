package analizador;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.XMLException;

public class analyzer {

	public static IOContainer AnalizarDatos(String xml) throws IOException, XMLException, OperatorException {
		Process process=null;
			System.setProperty(RapidMiner.PROPERTY_RAPIDMINER_HOME, ".");
			RapidMiner.init();
			process = new Process(xml);
			IOContainer contenedor = process.run();
			return contenedor;	
	}
}
