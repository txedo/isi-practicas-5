package utilities;
import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltroDeXLS extends FileFilter
{
	public boolean accept (File fichero)
	{
		if (fichero.toString().toLowerCase().endsWith(".xls"))
			return true;
		else
			return false;
	}
	public String getDescription()
	{
		return ("Archivos XLS");
	}
}
