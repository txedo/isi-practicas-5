package presentacion;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import presentacion.auxiliares.Dialogos;

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
/**
 * Ventana 'Acerca de' del cliente.
 */
public class JDAcercaDe extends javax.swing.JDialog {

	private static final long serialVersionUID = -4903915570854306815L;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
		}
	}

	private JButton btnAceptar;
	private JPanel jPanel1;
	private JLabel lblTitulo2;
	private JLabel lblTitulo;
	private JTextPane txtAcercaDe;
	
	public JDAcercaDe() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			AnchorLayout thisLayout = new AnchorLayout();
			getContentPane().setLayout(thisLayout);
			this.setTitle("Acerca de PlanCode");
			{
				jPanel1 = new JPanel();
				AnchorLayout jPanel1Layout = new AnchorLayout();
				getContentPane().add(jPanel1, new AnchorConstraint(1, 1001, 1001, 1, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jPanel1.setPreferredSize(new java.awt.Dimension(394, 275));
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setName("JPAcercaDe");
				{
					lblTitulo2 = new JLabel();
					jPanel1.add(lblTitulo2, new AnchorConstraint(36, 15, 210, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblTitulo2.setText("PlanCode");
					lblTitulo2.setPreferredSize(new java.awt.Dimension(389, 26));
					lblTitulo2.setFont(new java.awt.Font("Tahoma",1,20));
					lblTitulo2.setOpaque(false);
					lblTitulo2.setFocusable(false);
					lblTitulo2.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
					lblTitulo = new JLabel();
					jPanel1.add(lblTitulo, new AnchorConstraint(12, 10, 241, 10, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS));
					lblTitulo.setText("Planificador Colaborativo para el Dise�o de Estrategias y Acciones de Emergencia");
					lblTitulo.setPreferredSize(new java.awt.Dimension(394, 19));
					lblTitulo.setOpaque(false);
					lblTitulo.setFont(new java.awt.Font("Tahoma",1,14));
					lblTitulo.setFocusable(false);
					lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
				}
				{
					txtAcercaDe = new JTextPane();
					jPanel1.add(txtAcercaDe, new AnchorConstraint(266, 965, 827, 26, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					txtAcercaDe.setPreferredSize(new java.awt.Dimension(370, 155));
					txtAcercaDe.setOpaque(false);
					txtAcercaDe.setContentType("text/html");
					txtAcercaDe.setText("Versi�n: 1.0<br>\nP�gina web: <a href=\"http://www.inf-cr.uclm.es\">ESI@UCLM</a><br><br>\nDesarrolladores:\n<ul>Juan Andrada Romero (<a href=\"mailto:juan.andrada@alu.uclm.es\">juan.andrada@alu.uclm.es</a>)<br>\nJose Domingo L�pez L�pez (<a href=\"mailto:josed.lopez1@alu.uclm.es\">josed.lopez1@alu.uclm.es</a>)<br>");
					txtAcercaDe.setEditable(false);
					txtAcercaDe.setFocusable(false);
					txtAcercaDe.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					txtAcercaDe.addHyperlinkListener(new HyperlinkListener() {
						public void hyperlinkUpdate(HyperlinkEvent evt) {
							txtAcercaDeHyperlinkUpdate(evt);
						}
					});
				}
				{
					btnAceptar = new JButton();
					jPanel1.add(btnAceptar, new AnchorConstraint(866, 14, 12, 753, AnchorConstraint.ANCHOR_NONE, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_NONE));
					btnAceptar.setText("Aceptar");
					btnAceptar.setPreferredSize(new java.awt.Dimension(83, 25));
					btnAceptar.setName("btnAceptar");
					btnAceptar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAceptarActionPerformed(evt);
						}
					});
				}
			}
			setResizable(false);
			this.setPreferredSize(new java.awt.Dimension(620, 300));
			this.setMaximumSize(new java.awt.Dimension(620, 300));
			this.setMinimumSize(new java.awt.Dimension(620, 300));
			this.setName("JDAcercaDe");
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					thisWindowClosing(evt);
				}
			});
			pack();
		} catch (Exception e) {
			Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
		}
	}
	
	//$hide>>$
	
	private void txtAcercaDeHyperlinkUpdate(HyperlinkEvent evt) {
		if(evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				Desktop.getDesktop().browse(evt.getURL().toURI());
			} catch(IOException e) {
				Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
			} catch(URISyntaxException e) {
				Dialogos.mostrarDialogoError(null, "Error", e.getLocalizedMessage());
			}
		}
	}
	
	private void btnAceptarActionPerformed(ActionEvent evt) {
		cerrarVentana();
	}
	
	private void thisWindowClosing(WindowEvent evt) {
		cerrarVentana();
	}

	private void cerrarVentana() {
		this.dispose();
	}

}
