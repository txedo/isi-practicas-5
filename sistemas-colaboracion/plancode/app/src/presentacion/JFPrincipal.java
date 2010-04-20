package presentacion;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

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
public class JFPrincipal extends javax.swing.JFrame {
	private JPanel jPnlToolBoox;
	private JPanel jPestañaChat;
	private JPanel jPnlUsuarios;
	private JPanel panelPaint;
	private JButton jButton1;
	private JButton Dibujar;
	private CanvasPaint canvasPaint;
	private JTextArea jTxtChat;
	private JScrollPane jScrollChat;
	private JTextArea jTxtLog;
	private JScrollPane jScrollLog;
	private JPanel jPestañaLog;
	private JTabbedPane jTabbedPane;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFPrincipal inst = new JFPrincipal();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public JFPrincipal() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			setMinimumSize(new Dimension(892,600));
			this.setTitle("PlanCoDE");
			{
				jPnlToolBoox = new JPanel();
				getContentPane().add(jPnlToolBoox);
				jPnlToolBoox.setBounds(6, 25, 195, 336);
				jPnlToolBoox.setLayout(null);
				jPnlToolBoox.setBorder(BorderFactory.createTitledBorder("Toolbox"));
				{
					jButton1 = new JButton();
					jPnlToolBoox.add(jButton1);
					jButton1.setText("Eliminar");
					jButton1.setBounds(62, 123, 77, 30);
					jButton1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jButton1ActionPerformed(evt);
						}
					});
				}
				{
					Dibujar = new JButton();
					jPnlToolBoox.add(Dibujar);
					Dibujar.setText("Dibujar");
					Dibujar.setBounds(62, 164, 77, 26);
					Dibujar.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							DibujarActionPerformed(evt);
						}
					});
				}
			}
			{
				jTabbedPane = new JTabbedPane();
				getContentPane().add(jTabbedPane);
				jTabbedPane.setBounds(0, 367, 876, 168);
				{
					jPestañaChat = new JPanel();
					jTabbedPane.addTab("Chat", null, jPestañaChat, null);
					jPestañaChat.setLayout(null);
					{
						jScrollChat = new JScrollPane();
						jPestañaChat.add(jScrollChat);
						jScrollChat.setBounds(0, 7, 871, 133);
						jScrollChat.setPreferredSize(new java.awt.Dimension(436, 23));
						{
							jTxtChat = new JTextArea();
							jScrollChat.setViewportView(jTxtChat);
							jTxtChat.setEditable(false);
						}
					}
				}
				{
					jPestañaLog = new JPanel();
					jTabbedPane.addTab("Log", null, jPestañaLog, null);
					jPestañaLog.setLayout(null);
					{
						jScrollLog = new JScrollPane();
						jPestañaLog.add(jScrollLog);
						jScrollLog.setBounds(0, 6, 871, 134);
						{
							jTxtLog = new JTextArea();
							jScrollLog.setViewportView(jTxtLog);
							jTxtLog.setEditable(false);
						}
					}
				}
			}
			{
				jPnlUsuarios = new JPanel();
				getContentPane().add(jPnlUsuarios);
				jPnlUsuarios.setBounds(673, 25, 195, 336);
				jPnlUsuarios.setLayout(null);
				jPnlUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios"));
			}
			{
				panelPaint = new JPanel();
				getContentPane().add(panelPaint);
				panelPaint.setBounds(207, 25, 460, 336);
				panelPaint.setBorder(BorderFactory.createTitledBorder("Área de trabajo"));
				panelPaint.setLayout(null);
				{
					canvasPaint = new CanvasPaint();
					panelPaint.add(canvasPaint);
					canvasPaint.modoPintarTrazo();
					canvasPaint.setBackground(Color.white);
					canvasPaint.setBounds(5, 21, 450, 310);
				}
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButton1ActionPerformed(ActionEvent evt) {
		canvasPaint.modoEliminarTrazo();
	}
	
	private void DibujarActionPerformed(ActionEvent evt) {
		canvasPaint.modoPintarTrazo();
	}

}
