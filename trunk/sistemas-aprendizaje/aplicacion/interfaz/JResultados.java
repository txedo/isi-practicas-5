package interfaz;
import com.rapidminer.gui.processeditor.ResultDisplay;
import com.rapidminer.operator.IOContainer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


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
public class JResultados extends javax.swing.JDialog {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private ResultDisplay resultDisplay1;
	private JButton jButton1;
	
	public JResultados() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, "Center");
				jPanel1.setLayout(null);
				jPanel1.setBounds(0, 0, 1233, 894);
				{
					resultDisplay1 = new ResultDisplay();
					jPanel1.add(resultDisplay1);
					resultDisplay1.setBounds(10, 11, 1170, 717);
				}
				{
					jButton1 = new JButton();
					jPanel1.add(jButton1);
					jButton1.setText("Cerrar");
					jButton1.setBounds(1105, 739, 75, 23);
					jButton1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jButton1ActionPerformed(evt);
						}
					});
				}
			}
			
			this.setPreferredSize(new java.awt.Dimension(1200, 810));
			this.setTitle("Resultados");
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setResultados(IOContainer contenedor) {
		resultDisplay1.setData(contenedor, null);
		resultDisplay1.setVisible(true);
	}
	
	private void jButton1ActionPerformed(ActionEvent evt) {
		this.dispose();
	}

}
