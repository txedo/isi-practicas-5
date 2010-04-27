package utilities;

public class XMLProject {
	
	public static String getXMLProyecto(String algoritmo, String rutaXLS, String rutaFichero, boolean nombres, boolean id, int clase, int nId) {
		String xml="<?xml version=\"1.0\" encoding=\"windows-1252\" ?>"+ 
		"<process version=\"4.6\">"+
			"<operator name=\"Root\" class=\"Process\" expanded=\"yes\">" +
			  	"<parameter key=\"logverbosity\" value=\"init\"/>" + 
			  	"<parameter key=\"random_seed\" value=\"2001\"/>" +
			  	"<parameter key=\"send_mail\" value=\"never\"/>" +
			  	"<parameter key=\"process_duration_for_mail\" value=\"30\"/>" + 
			  	"<parameter key=\"encoding\" value=\"SYSTEM\"/>" + 
				"<operator name=\"ExcelExampleSource\" class=\"ExcelExampleSource\">" +
					"<parameter key=\"excel_file\" value=\""+rutaXLS+"\" />" +
					"<parameter key=\"sheet_number\" value=\"1\" />"+ 
					"<parameter key=\"row_offset\" value=\"0\" />"+ 
					"<parameter key=\"column_offset\" value=\"0\" />"+
					"<parameter key=\"first_row_as_names\" value=\""+nombres+"\" />"+
					"<parameter key=\"create_label\" value=\"true\" />" +
					"<parameter key=\"label_column\" value=\""+clase+"\" />" +
					"<parameter key=\"create_id\" value=\""+id+"\" />";
					if (id)
						xml+="<parameter key=\"id_column\" value=\""+nId+"\" />";
 				  	xml+="<parameter key=\"decimal_point_character\" value=\".\" />"+ 
					"<parameter key=\"datamanagement\" value=\"double_array\" />"+
				"</operator>" +
				"<operator name=\"FrequencyDiscretization\" class=\"FrequencyDiscretization\">" +
					"<parameter key=\"return_preprocessing_model\" value=\"false\" />"+ 
				  	"<parameter key=\"create_view\" value=\"false\" />"+ 
				  	"<parameter key=\"use_sqrt_of_examples\" value=\"false\" />"+ 
				  	"<parameter key=\"number_of_bins\" value=\"2\" /> "+
				  	"<parameter key=\"range_name_type\" value=\"long\" />"+ 
				  	"<parameter key=\"automatic_number_of_digits\" value=\"true\" />"+ 
				  	"<parameter key=\"number_of_digits\" value=\"-1\" /> "+
				"</operator>" +
				"<operator name=\""+algoritmo+"\" class=\""+algoritmo+"\">" +
					"<parameter key=\"keep_example_set\" value=\"false\" />"+ 
				  	"<parameter key=\"criterion\" value=\"gain_ratio\" />"+ 
				  	"<parameter key=\"minimal_size_for_split\" value=\"4\" />"+ 
				  	"<parameter key=\"minimal_leaf_size\" value=\"2\" />"+ 
				  	"<parameter key=\"minimal_gain\" value=\"0.1\" />"+
				"</operator>" +
				"<operator name=\"ResultWriter\" class=\"ResultWriter\">" +
					"<parameter key=\"result_file\" value=\""+rutaFichero+"\" />" +
				"</operator>" +
			"</operator>" +		
		"</process>";
		
		return xml;
	}
	
}