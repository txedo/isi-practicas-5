package interfaz;

import analizador.analyzer;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.gui.processeditor.ResultDisplay;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.XMLException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import utilities.FiltroDeXLS;
import utilities.FiltroDeRES;
import utilities.StreamText;
import utilities.XMLProject;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JDataMiningClassification extends javax.swing.JFrame {
	private JPanel jPanel;
	private JTextField jtxtRuta;
	private JButton jbtExaminar;
	private JTextArea jtxtAreaEstado;
	private JScrollPane jScrollPane1;
	private JButton jbtReset;
	private JTextField jtxtID;
	private JLabel jLabel7;
	private JButton jButton2;
	private JButton jButton1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JButton btnExaminarRes;
	private JTextField jtxtRes;
	private JLabel jLabel6;
	private JRadioButton jradioNO2;
	private JRadioButton jradioSI2;
	private ButtonGroup buttonGroup2;
	private JRadioButton jradio2;
	private JRadioButton jradioSi;
	private ButtonGroup buttonGroup1;
	private JTextField jtxtClase;
	private JComboBox jcbAlgoritmos;
	private JLabel jLabel1;
	private JFileChooser fc;
	
	private String rutaXLS, rutaFichero;
	private String algoritmo="ID3";
	boolean nombres, id, seleccionadoNombre, seleccionadoId;
	int clase;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JDataMiningClassification inst = new JDataMiningClassification();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public JDataMiningClassification() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setResizable(false);
			this.setTitle("CTG - Classification Tree Generator");
			{
				jPanel = new JPanel();
				getContentPane().add(jPanel, BorderLayout.CENTER);
				jPanel.setLayout(null);
				jPanel.setPreferredSize(new java.awt.Dimension(383, 542));
				{
					btnExaminarRes = new JButton();
					jPanel.add(btnExaminarRes, new AnchorConstraint(850, 375, 903, 296, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					btnExaminarRes.setText("Examinar");
					btnExaminarRes.setBounds(281, 284, 115, 26);
					btnExaminarRes.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnExaminarResActionPerformed(evt);
						}
					});
				}
				{
					jtxtRes = new JTextField();
					jPanel.add(jtxtRes, new AnchorConstraint(850, 290, 901, 8, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jtxtRes.setBounds(10, 284, 261, 26);
				}
				{
					jLabel6 = new JLabel();
					jPanel.add(jLabel6, new AnchorConstraint(807, 375, 837, 9, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jLabel6.setText("Introduzca la ruta del archivo para guardar el resultado");
					jLabel6.setBounds(10, 264, 292, 14);
				}
				{
					jradioNO2 = new JRadioButton();
					jPanel.add(jradioNO2, new AnchorConstraint(671, 129, 720, 89, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jradioNO2.setText("NO");
					jradioNO2.setBounds(358, 186, 53, 24);
					jradioNO2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jradioNO2ActionPerformed(evt);
						}
					});
				}
				{
					jradioSI2 = new JRadioButton();
					jPanel.add(jradioSI2, new AnchorConstraint(671, 88, 720, 9, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jradioSI2.setText("SI");
					jradioSI2.setBounds(311, 186, 48, 24);
					jradioSI2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jradioSI2ActionPerformed(evt);
						}
					});
				}
				{
					jLabel5 = new JLabel();
					jPanel.add(jLabel5, new AnchorConstraint(629, 376, 659, 8, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jLabel5.setText("Existe un atributo identificador de instancias en el archivo");
					jLabel5.setBounds(10, 191, 279, 15);
				}
				{
					jradio2 = new JRadioButton();
					jPanel.add(jradio2, new AnchorConstraint(565, 129, 614, 89, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jradio2.setText("NO");
					jradio2.setBounds(357, 160, 53, 24);
					jradio2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jradio2ActionPerformed(evt);
						}
					});
				}
				{
					jradioSi = new JRadioButton();
					jPanel.add(jradioSi, new AnchorConstraint(565, 71, 614, 5, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jradioSi.setText("SI");
					jradioSi.setBounds(311, 160, 49, 24);
					jradioSi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jradioSiActionPerformed(evt);
						}
					});
				}
				{
					jLabel4 = new JLabel();
					jPanel.add(jLabel4, new AnchorConstraint(523, 384, 553, 7, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jLabel4.setText("Usar la primera fila del archivo como nombres de atributos");
					jLabel4.setBounds(10, 165, 295, 15);
				}
				{
					jtxtClase = new JTextField();
					jPanel.add(jtxtClase, new AnchorConstraint(457, 90, 500, 10, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jtxtClase.setBounds(310, 133, 82, 21);
				}
				{
					jLabel3 = new JLabel();
					jPanel.add(jLabel3, new AnchorConstraint(415, 350, 444, 10, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jLabel3.setText("Introduzca el numero de columna del atributo de clasificacion");
					jLabel3.setBounds(10, 139, 348, 15);
				}
				{
					ComboBoxModel jcbAlgoritmosModel = 
						new DefaultComboBoxModel(
								new String[] { "ID3", "CHAID", "RandomTree" });
					jcbAlgoritmos = new JComboBox();
					jPanel.add(jcbAlgoritmos, new AnchorConstraint(285, 198, 330, 10, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jcbAlgoritmos.setModel(jcbAlgoritmosModel);
					jcbAlgoritmos.setBounds(208, 87, 185, 23);
					jcbAlgoritmos.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jcbAlgoritmosActionPerformed(evt);
						}
					});
				}
				{
					jLabel2 = new JLabel();
					jPanel.add(jLabel2, new AnchorConstraint(236, 272, 272, 10, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jLabel2.setText("Seleccione un algoritmo de clasificacion");
					jLabel2.setBounds(10, 89, 348, 18);
				}
				{
					jLabel1 = new JLabel();
					jPanel.add(jLabel1, new AnchorConstraint(68, 275, 105, 14, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jLabel1.setText("Selecciona el archivo XLS con los metadatos a analizar");
					jLabel1.setBounds(10, 11, 297, 18);
				}
				{
					jbtExaminar = new JButton();
					jPanel.add(jbtExaminar, new AnchorConstraint(122, 381, 175, 302, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jbtExaminar.setText("Examinar");
					jbtExaminar.setBounds(278, 29, 114, 24);
					jbtExaminar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jbtExaminarActionPerformed(evt);
						}
					});
				}
				{
					jtxtRuta = new JTextField();
					jPanel.add(jtxtRuta, new AnchorConstraint(119, 296, 175, 13, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jPanel.add(getJButton1());
					jPanel.add(getJButton2());
					jPanel.add(getJLabel7());
					jPanel.add(getJtxtID());
					jPanel.add(getJbtReset());
					jPanel.add(getJScrollPane1());
					jtxtRuta.setBounds(10, 29, 258, 24);
				}
			}
			
			this.setPreferredSize(new java.awt.Dimension(410, 620));
			buttonGroup1=getButtonGroup1();
			buttonGroup2=getButtonGroup2();
			resetGUI();
			StreamText salida = new StreamText(getJtxtAreaEstado());
			System.setOut(new PrintStream(salida));
			System.setErr(new PrintStream(salida));
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void resetGUI() {
		seleccionadoNombre=false;
		seleccionadoId=false;
		rutaXLS = "";
		rutaFichero = "";
		nombres = false;
		id= false;
		clase = 0;
		jcbAlgoritmos.setSelectedIndex(0);
		jtxtClase.setText("");
		jtxtRes.setText("");
		jtxtRuta.setText("");
		jtxtID.setText("");
		jtxtID.setEditable(false);
		buttonGroup1.clearSelection();
		buttonGroup2.clearSelection();
		jtxtAreaEstado.setText("");
	}
	
	private ButtonGroup getButtonGroup1() {
		if(buttonGroup1 == null) {
			buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(jradioSi);
			buttonGroup1.add(jradio2);
		}
		return buttonGroup1;
	}
	
	private ButtonGroup getButtonGroup2() {
		if(buttonGroup2 == null) {
			buttonGroup2 = new ButtonGroup();
			buttonGroup2.add(jradioNO2);
			buttonGroup2.add(jradioSI2);
		}
		return buttonGroup2;
	}

	private void jbtExaminarActionPerformed(ActionEvent evt) {
		fc = new JFileChooser();
		fc.setFileFilter(new FiltroDeXLS());
		fc.showOpenDialog(this);
		File file = fc.getSelectedFile();
		if (file!=null) {
			rutaXLS=file.toString();
		}
		jtxtRuta.setText(rutaXLS);
	}
	
	private void btnExaminarResActionPerformed(ActionEvent evt) {
		fc = new JFileChooser();
		fc.setFileFilter(new FiltroDeRES());
		fc.showOpenDialog(this);
		File file = fc.getSelectedFile();
		if (file!=null) {
			rutaFichero=file.toString();
		}
		jtxtRes.setText(rutaFichero);
	
	}
	
	private JButton getJButton1() {
		if(jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Comenzar");
			jButton1.setBounds(311, 555, 84, 25);
			jButton1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});
		}
		return jButton1;
	}
	
	private JButton getJButton2() {
		if(jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("Cerrar");
			jButton2.setBounds(10, 554, 71, 27);
			jButton2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jButton2ActionPerformed(evt);
				}
			});
		}
		return jButton2;
	}
	
	private void jButton1ActionPerformed(ActionEvent evt) {
		jtxtAreaEstado.setText("");
		int clase = 0;
		int nId = 0;
		String xml="";
		boolean valido=true;
		rutaXLS = jtxtRuta.getText();
		rutaFichero = jtxtRes.getText();
		
		if (rutaXLS.equals("")) {
			JOptionPane.showMessageDialog(this,"Debe introducir un archivo en formato XLS", "Error", JOptionPane.ERROR_MESSAGE);
			valido=false;
		}else if (valido && (algoritmo == null || !algoritmo.equals("ID3"))) {
			JOptionPane.showMessageDialog(this,"Solo esta soportado el algoritmo ID3", "Error", JOptionPane.ERROR_MESSAGE);
			valido=false;
		}else if (valido && !seleccionadoNombre){
			JOptionPane.showMessageDialog(this,"Debe elegir una opcion para indicar si la primera fila son nombres", "Error", JOptionPane.ERROR_MESSAGE);
			valido=false;
		}else if (valido && !seleccionadoId){
			JOptionPane.showMessageDialog(this,"Debe elegir una opcion para indicar si existe un identificador de instancias", "Error", JOptionPane.ERROR_MESSAGE);
			valido=false;
		}else if (valido && rutaFichero.equals("")){
			JOptionPane.showMessageDialog(this,"Debe introducir un archivo para guardar los resultados", "Error", JOptionPane.ERROR_MESSAGE);
			valido=false;
		}
		
		if (valido) {
			if (!rutaXLS.equals("") && !rutaXLS.endsWith(".xls"))
				rutaXLS+=".xls";		
			if (!rutaFichero.equals("") && !rutaFichero.endsWith(".res"))
				rutaFichero+=".res";

			try {
				try{
					clase=Integer.parseInt(jtxtClase.getText());
					if (clase==0) { 
						JOptionPane.showMessageDialog(this,"Debe introducir un numero entero para el atributo de clasificacion", "Error", JOptionPane.ERROR_MESSAGE);
						valido = false;
					}
					nId=Integer.parseInt(jtxtID.getText());
					if (id && nId==0) {
						valido= false;
						JOptionPane.showMessageDialog(this,"Debe introducir un numero entero para el atributo identificador", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this,"Se debe introducir un numero entero, tanto en el atributo identificador como en el de clasificacion", "Error", JOptionPane.ERROR_MESSAGE);
					valido=false;
				}
				
				if(valido) {
					xml = XMLProject.getXMLProyecto(algoritmo, rutaXLS, rutaFichero, nombres, id, clase, nId);
					IOContainer contenedor = analyzer.AnalizarDatos(xml);
					JResultados dialogo = new JResultados();
					dialogo.setResultados(contenedor);
					dialogo.setModal(true);
					dialogo.setLocationRelativeTo(this);
					dialogo.show();
					jtxtAreaEstado.append("\nCompletado.");
					
				}
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this,"Excepcion :"+e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void jButton2ActionPerformed(ActionEvent evt) {
		System.exit(0);
	}
	
	private void jradioSiActionPerformed(ActionEvent evt) {
		nombres=true;
		seleccionadoNombre=true;
	}
	
	private void jradio2ActionPerformed(ActionEvent evt) {
		nombres=false;
		seleccionadoNombre=true;
	}
	
	private void jradioNO2ActionPerformed(ActionEvent evt) {
		id=false;
		seleccionadoId=true;
		jtxtID.setText("");
		jtxtID.setEditable(false);
	}
	
	private void jradioSI2ActionPerformed(ActionEvent evt) {
		id=true;
		seleccionadoId=true;
		jtxtID.setText("");
		jtxtID.setEditable(true);
		
	}
	
	private void jcbAlgoritmosActionPerformed(ActionEvent evt) {
		algoritmo = jcbAlgoritmos.getSelectedItem().toString();
	}
	
	private JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new JLabel();
			jLabel7.setText("Introduzca el numero de columna del atributo identificador");
			jLabel7.setBounds(10, 217, 279, 14);
		}
		return jLabel7;
	}
	
	private JTextField getJtxtID() {
		if(jtxtID == null) {
			jtxtID = new JTextField();
			jtxtID.setBounds(311, 214, 81, 20);
		}
		return jtxtID;
	}
	
	private JButton getJbtReset() {
		if(jbtReset == null) {
			jbtReset = new JButton();
			jbtReset.setText("Reset");
			jbtReset.setBounds(228, 555, 77, 25);
			jbtReset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					jbtResetActionPerformed(evt);
				}
			});
		}
		return jbtReset;
	}
	
	private void jbtResetActionPerformed(ActionEvent evt) {
		resetGUI();
	}
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(10, 346, 385, 198);
			jScrollPane1.setViewportView(getJtxtAreaEstado());
		}
		return jScrollPane1;
	}
	
	private JTextArea getJtxtAreaEstado() {
		if(jtxtAreaEstado == null) {
			jtxtAreaEstado = new JTextArea();
			jtxtAreaEstado.setEditable(false);
		}
		return jtxtAreaEstado;
	}
}

