package presentacion;
import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

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
public class JFLogin extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private JRadioButton rbBombero;
	private JButton btnConectar;
	private JTextField txtNick;
	private JTextField txtPuerto;
	private JTextField txtDireccionIP;
	private JPanel jPanel2;
	private JRadioButton rbSanidad;
	private JRadioButton rbPolicia;
	private ButtonGroup buttonGroup1;
	private JLabel lblNick;
	private JLabel lblPuerto;
	private JLabel lblDireccionIP;
	private JPanel jPanel3;
	private JCheckBox cbCrearSesion;
	private JButton btnCerrar;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFLogin inst = new JFLogin();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public JFLogin() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Inicio de sesión");
			this.setResizable(false);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				jPanel1.setPreferredSize(new java.awt.Dimension(247, 263));
				{
					lblNick = new JLabel();
					jPanel1.add(lblNick);
					lblNick.setText("Nick");
					lblNick.setBounds(10, 11, 93, 13);
				}
				{
					jPanel2 = new JPanel();
					jPanel1.add(jPanel2);
					jPanel1.add(getTxtNick());
					jPanel1.add(getBtnConectar());
					jPanel1.add(getBtnCerrar());
					jPanel1.add(getCbCrearSesion());
					jPanel1.add(getJPanel3());
					jPanel2.setBounds(10, 36, 219, 55);
					jPanel2.setBorder(BorderFactory.createTitledBorder("Rol"));
					{
						rbPolicia = new JRadioButton();
						jPanel2.add(rbPolicia);
						rbPolicia.setText("Policia");
						rbPolicia.setBounds(22, 20, 56, 25);
						getButtonGroup1().add(rbPolicia);
						rbPolicia.setSelected(true);
						getButtonGroup1().add(rbPolicia);
					}
					{
						rbBombero = new JRadioButton();
						jPanel2.add(rbBombero);
						rbBombero.setText("Bombero");
						rbBombero.setBounds(22, 41, 56, 23);
						getButtonGroup1().add(rbBombero);
						getButtonGroup1().add(rbBombero);
					}
					{
						rbSanidad = new JRadioButton();
						jPanel2.add(rbSanidad);
						rbSanidad.setText("Sanidad");
						rbSanidad.setBounds(22, 62, 59, 23);
						getButtonGroup1().add(rbSanidad);
						getButtonGroup1().add(rbSanidad);
					}
				}
			}
			pack();
			this.setSize(251, 290);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ButtonGroup getButtonGroup1() {
		if(buttonGroup1 == null) {
			buttonGroup1 = new ButtonGroup();
		}
		return buttonGroup1;
	}
	
	private JTextField getTxtDireccionIP() {
		if(txtDireccionIP == null) {
			txtDireccionIP = new JTextField();
			txtDireccionIP.setText("127.0.0.1");
			txtDireccionIP.setBounds(104, 25, 99, 20);
			txtDireccionIP.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent evt) {
					txtDireccionIPFocusGained(evt);
				}
			});
		}
		return txtDireccionIP;
	}
	
	private JTextField getTxtPuerto() {
		if(txtPuerto == null) {
			txtPuerto = new JTextField();
			txtPuerto.setText("17186");
			txtPuerto.setBounds(104, 50, 99, 20);
			txtPuerto.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent evt) {
					txtPuertoFocusGained(evt);
				}
			});
		}
		return txtPuerto;
	}
	
	private JTextField getTxtNick() {
		if(txtNick == null) {
			txtNick = new JTextField();
			txtNick.setBounds(103, 7, 110, 23);
		}
		return txtNick;
	}
	
	private JButton getBtnConectar() {
		if(btnConectar == null) {
			btnConectar = new JButton();
			btnConectar.setText("Conectar");
			btnConectar.setBounds(28, 222, 79, 23);
		}
		return btnConectar;
	}
	
	private JButton getBtnCerrar() {
		if(btnCerrar == null) {
			btnCerrar = new JButton();
			btnCerrar.setText("Cerrar");
			btnCerrar.setBounds(134, 222, 79, 23);
		}
		return btnCerrar;
	}
	
	private JCheckBox getCbCrearSesion() {
		if(cbCrearSesion == null) {
			cbCrearSesion = new JCheckBox();
			cbCrearSesion.setText("Crear una sesión nueva");
			cbCrearSesion.setBounds(10, 98, 143, 26);
			cbCrearSesion.setSelected(true);
		}
		return cbCrearSesion;
	}
	
	private JPanel getJPanel3() {
		if(jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setBounds(10, 126, 219, 83);
			jPanel3.setBorder(BorderFactory.createTitledBorder("Datos del servidor"));
			jPanel3.setLayout(null);
			{
				lblDireccionIP = new JLabel();
				jPanel3.add(lblDireccionIP);
				lblDireccionIP.setText("Dirección IP");
				lblDireccionIP.setBounds(12, 28, 56, 14);
			}
			{
				lblPuerto = new JLabel();
				jPanel3.add(lblPuerto);
				lblPuerto.setText("Puerto");
				lblPuerto.setBounds(12, 53, 32, 14);
			}
			jPanel3.add(getTxtDireccionIP());
			jPanel3.add(getTxtPuerto());
		}
		return jPanel3;
	}
	
	private void txtDireccionIPFocusGained(FocusEvent evt) {
		txtDireccionIP.selectAll();
	}
	
	private void txtPuertoFocusGained(FocusEvent evt) {
		txtPuerto.selectAll();
	}

}
