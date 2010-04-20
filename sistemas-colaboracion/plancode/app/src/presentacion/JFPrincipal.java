package presentacion;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

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
	private JPanel jPesta�aChat;
	private JPanel jPnlUsuarios;
	private PanelPaint panelPaint1;
	private JTextArea jTxtChat;
	private JScrollPane jScrollChat;
	private JTextArea jTxtLog;
	private JScrollPane jScrollLog;
	private JPanel jPesta�aLog;
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
				jPnlToolBoox.setBounds(0, 31, 195, 330);
				jPnlToolBoox.setLayout(null);
			}
			{
				jTabbedPane = new JTabbedPane();
				getContentPane().add(jTabbedPane);
				jTabbedPane.setBounds(0, 367, 876, 168);
				{
					jPesta�aChat = new JPanel();
					jTabbedPane.addTab("Chat", null, jPesta�aChat, null);
					jPesta�aChat.setLayout(null);
					{
						jScrollChat = new JScrollPane();
						jPesta�aChat.add(jScrollChat);
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
					jPesta�aLog = new JPanel();
					jTabbedPane.addTab("Log", null, jPesta�aLog, null);
					jPesta�aLog.setLayout(null);
					{
						jScrollLog = new JScrollPane();
						jPesta�aLog.add(jScrollLog);
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
				jPnlUsuarios.setBounds(673, 31, 195, 324);
				jPnlUsuarios.setLayout(null);
			}
			{
				panelPaint1 = new PanelPaint();
				getContentPane().add(panelPaint1);
				panelPaint1.modoPintar();
				panelPaint1.setBackground(Color.white);
				panelPaint1.setBounds(201, 31, 461, 331);
			}
			pack();
			setSize(400, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
