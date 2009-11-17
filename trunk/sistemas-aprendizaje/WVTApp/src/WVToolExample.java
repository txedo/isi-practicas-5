import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Vector;
import edu.udo.cs.wvtool.config.WVTConfiguration;
import edu.udo.cs.wvtool.config.WVTConfigurationFact;
import edu.udo.cs.wvtool.generic.output.WordVectorWriter;
import edu.udo.cs.wvtool.generic.stemmer.DummyStemmer;
import edu.udo.cs.wvtool.generic.vectorcreation.TFIDF;
import edu.udo.cs.wvtool.generic.vectorcreation.TermOccurrences;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTFileInputList;
import edu.udo.cs.wvtool.main.WVTInputList;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.main.WVTool;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

/**
 * An example program on how to use the Word Vector Tool.
 * 
 * @author Michael Wurst
 * 
 */
public class WVToolExample {
	public static void main(String[] args) throws Exception {
		// EXAMPLE HOW TO CALL THE PROGRAM FROM JAVA
		// Initialize the WVTool
		WVTool wvt = new WVTool(true);
		// Initialize the configuration
		WVTConfiguration config = new WVTConfiguration();
		config.setConfigurationRule(WVTConfiguration.STEP_STEMMER,
				new WVTConfigurationFact(new DummyStemmer()));
		// Initialize the input list with two classes
		WVTFileInputList list = new WVTFileInputList(2);
		// Add entries
		list.addEntry(new WVTDocumentInfo("data/38851", "txt", "",
				"english", 0));
		list.addEntry(new WVTDocumentInfo("data/38856", "txt",
				"", "english", 1));
		// Generate the word list
		WVTWordList wordList = wvt.createWordList(list, config);
		// Prune the word list
		wordList.pruneByFrequency(2, 5);
		// Store the word list in a file
		wordList.storePlain(new FileWriter("wordlist.txt"));
		// Alternatively: read an already created word list from a file
		// WVTWordList wordList2 =
		// new WVTWordList(
		// new FileReader("/home/wurst/tmp/wordlisttest.txt"));
		// Create the word vectors
		// Set up an output filter (write sparse vectors to a file)
		FileWriter outFile = new FileWriter("wv.txt");
		WordVectorWriter wvw = new WordVectorWriter(outFile, true);
		config.setConfigurationRule(WVTConfiguration.STEP_OUTPUT,
				new WVTConfigurationFact(wvw));
		config.setConfigurationRule(WVTConfiguration.STEP_VECTOR_CREATION,
				new WVTConfigurationFact(new TFIDF()));
		// Create the vectors
		wvt.createVectors(list, config, wordList);
		// Alternatively: create word list and vectors together
		// wvt.createVectors(list, config);
		// Close the output file
		wvw.close();
		outFile.close();
		// Just for demonstration: Create a vector from a String
		WVTWordVector q = wvt.createVector("cmu harvard net", wordList);
	}
}
