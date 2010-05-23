package presentacion;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.cloudgarden.layout.AnchorConstraint;
import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;

import dominio.control.ControladorPrincipal;


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

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	private static final long serialVersionUID = -3368293739950370869L;
	private ControladorPrincipal controlador;
	private JPanel jPnlToolBoox;
	private JPanel jPnlUsuarios;
	private JPanel panelPaint;
	private JButton btnEnviar;
	private JTextField txtMensaje;
	private JMenuItem jmiAcercaDe;
	private JMenu jMenu2;
	private JTextArea taChat;
	private JTextArea taLog;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JPanel jpLog;
	private JPanel jpChat;
	private JTabbedPane jTabbedPane;
	private JLabel lblStatusBar;
	private JButton jButton3;
	private JButton jButton2;
	private JToolBar jToolBar;
	private JMenuItem jmiExit;
	private JSeparator jSeparator1;
	private JMenuItem jmiOpenMap;
	private JMenuItem jmiOpenImage;
	private JMenu jMenu1;
	private JMenuBar jMenuBar;
	private JPanel jPanel1;
	private JButton jButton1;
	private JButton Dibujar;
	private CanvasPaint canvasPaint;

	public JFPrincipal(ControladorPrincipal c) {
		super();
		controlador = c;
		initGUI();
		// Ponemos el listener a los consumidores del chat para poder recibir los mensajes de chat
		c.getConsumidorCanalChat().addMensajeChatRecibidoListener(new MensajeChatRecibidoListener() {
			public void MensajeChatRecibido(MensajeChatRecibidoEvent evt) {
				ponerMensajeChat(evt);
			}
		});
		
		// Ponemos el listener a los consumidores del canal de gestión para poder recibir la lista de usuarios conectados
		c.getConsumidorGestion().addMensajeListaUsuariosListener(new MensajeListaUsuariosListener() {
			public void MensajeListaUsuarios(MensajeListaUsuariosEvent evt) {
				System.out.println(evt.getLista());
			}
		});
		
		c.getConsumidorGestion().addMensajeRolRecibidoListener(new MensajeRolListener() {
			public void MensajeRolRecibido(MensajeRolEvent evt) {
				// Prueba
				taChat.append(evt.getRol().name());
			}
		});
	}
	
	private void ponerMensajeChat(MensajeChatRecibidoEvent evt) {
		taChat.append(evt.getNombre() + "> " + evt.getMensaje() + "\n");
		taChat.setCaretPosition(taChat.getDocument().getLength());
	}
	
	private void initGUI() {
		try {
			setLocationRelativeTo(null);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setMinimumSize(new java.awt.Dimension(875, 608));
			GridLayout thisLayout = new GridLayout(1, 1);
			thisLayout.setHgap(5);
			thisLayout.setVgap(5);
			thisLayout.setColumns(1);
			getContentPane().setLayout(thisLayout);
			this.setTitle("PlanCoDE");
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1);
				jPanel1.setLayout(null);
				jPanel1.setMaximumSize(new java.awt.Dimension(2123123892, 231236100));
				jPanel1.setMinimumSize(new java.awt.Dimension(10, 10));
				jPanel1.setPreferredSize(new java.awt.Dimension(867, 555));
				{
					jPnlToolBoox = new JPanel();
					jPanel1.add(jPnlToolBoox, new AnchorConstraint(25, 222, 634, 6, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_ABS));
					jPnlToolBoox.setLayout(null);
					jPnlToolBoox.setBorder(BorderFactory.createTitledBorder("Toolbox"));
					jPnlToolBoox.setBounds(10, 49, 73, 329);
					{
						jButton1 = new JButton();
						jPnlToolBoox.add(jButton1, new AnchorConstraint(366, 719, 455, 322, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						jButton1.setText("Borrar");
						jButton1.setBounds(16, 20, 41, 31);
						jButton1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								jButton1ActionPerformed(evt);
							}
						});
					}
					{
						Dibujar = new JButton();
						jPnlToolBoox.add(Dibujar, new AnchorConstraint(488, 719, 565, 322, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
						Dibujar.setText("Dibujar");
						Dibujar.setBounds(16, 62, 42, 28);
						Dibujar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								DibujarActionPerformed(evt);
							}
						});
					}
				}
				{
					jPnlUsuarios = new JPanel();
					jPanel1.add(jPnlUsuarios, new AnchorConstraint(25, 17, 632, 757, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jPnlUsuarios.setLayout(null);
					jPnlUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios"));
					jPnlUsuarios.setBounds(664, 49, 193, 329);
				}
				{
					panelPaint = new JPanel();
					jPanel1.add(panelPaint, new AnchorConstraint(25, 744, 634, 229, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					panelPaint.setBorder(BorderFactory.createTitledBorder("Área de trabajo"));
					panelPaint.setLayout(null);
					panelPaint.setBounds(89, 49, 569, 329);
					{
						canvasPaint = new CanvasPaint();
						panelPaint.add(canvasPaint);
						// Por defecto, el canvas no tiene ningún modo activo
						// canvasPaint.modoPintarTrazo();
						canvasPaint.setBounds(6, 20, 557, 303);
					}
				}
				{
					jToolBar = new JToolBar();
					jPanel1.add(jToolBar);
					jToolBar.setBounds(10, 11, 847, 29);
					jToolBar.setFloatable(false);
					{
						jButton2 = new JButton();
						jToolBar.add(jButton2);
						jButton2.setText("jButton2");
						jButton2.setBounds(3, 21, 75, 23);
					}
					{
						jButton3 = new JButton();
						jToolBar.add(jButton3);
						jButton3.setText("jButton3");
					}
				}
				{
					lblStatusBar = new JLabel();
					jPanel1.add(lblStatusBar);
					lblStatusBar.setBounds(10, 539, 848, 16);
					lblStatusBar.setText("texto de prueba...");
				}
				{
					jTabbedPane = new JTabbedPane();
					jPanel1.add(jTabbedPane);
					jTabbedPane.setBounds(10, 384, 847, 149);
					{
						jpChat = new JPanel();
						jTabbedPane.addTab("Chat", null, jpChat, null);
						jpChat.setLayout(null);
						jpChat.setPreferredSize(new java.awt.Dimension(825, 88));
						{
							jScrollPane1 = new JScrollPane();
							jpChat.add(jScrollPane1);
							jScrollPane1.setBounds(10, 11, 821, 74);
							{
								taChat = new JTextArea();
								jScrollPane1.setViewportView(taChat);
								taChat.setEditable(false);
								taChat.setFocusable(false);
								//taChat.setPreferredSize(new java.awt.Dimension(280, 76));
							}
						}
						{
							txtMensaje = new JTextField();
							jpChat.add(txtMensaje);
							txtMensaje.setBounds(10, 97, 283, 20);
						}
						{
							btnEnviar = new JButton();
							jpChat.add(btnEnviar);
							btnEnviar.setText("Enviar");
							btnEnviar.setBounds(303, 96, 65, 23);
							btnEnviar.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									btnEnviarActionPerformed(evt);
								}
							});
						}
					}
					{
						jpLog = new JPanel();
						jTabbedPane.addTab("Log", null, jpLog, null);
						jpLog.setLayout(null);
						{
							jScrollPane2 = new JScrollPane();
							jpLog.add(jScrollPane2);
							jScrollPane2.setBounds(10, 11, 821, 104);
							{
								taLog = new JTextArea();
								jScrollPane2.setViewportView(taLog);
							}
						}
					}
				}
			}
			{
				jMenuBar = new JMenuBar();
				setJMenuBar(jMenuBar);
				{
					jMenu1 = new JMenu();
					jMenuBar.add(jMenu1);
					jMenu1.setText("Archivo");
					{
						jmiOpenImage = new JMenuItem();
						jMenu1.add(jmiOpenImage);
						jmiOpenImage.setText("Cargar imagen local...");
					}
					{
						jmiOpenMap = new JMenuItem();
						jMenu1.add(jmiOpenMap);
						jmiOpenMap.setText("Cargar mapa...");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu1.add(jSeparator1);
					}
					{
						jmiExit = new JMenuItem();
						jMenu1.add(jmiExit);
						jmiExit.setText("Salir");
					}
				}
				{
					jMenu2 = new JMenu();
					jMenuBar.add(jMenu2);
					jMenu2.setText("Ayuda");
					{
						jmiAcercaDe = new JMenuItem();
						jMenu2.add(jmiAcercaDe);
						jmiAcercaDe.setText("Acerca de...");
					}
				}
			}
					
			pack();
			btnEnviar.setDefaultCapable(true);
			getRootPane().setDefaultButton(btnEnviar);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO: estos dos botones son provisionales
	private void jButton1ActionPerformed(ActionEvent evt) {
		canvasPaint.modoEliminarTrazo();
	}
	
	private void DibujarActionPerformed(ActionEvent evt) {
		canvasPaint.modoPintarTrazo();
	}

	public void cerrarVentana() {
		this.dispose();
	}

	public void mostrarVentana() {
		this.setVisible(true);
		this.actualizarListaUsuarios();
	}

	private void actualizarListaUsuarios() {
		// Pintar el panel de usuarios conectados
		// Utilizando controlador.getListaUsuarios()
	}

	private void btnEnviarActionPerformed(ActionEvent evt) {
		try {
			this.controlador.enviarMensajeChat(txtMensaje.getText());
		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchSessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PermissionDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimedOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtMensaje.setText("");
	}
	

	public void notificarLogin(String login) {
		taChat.append(login + " ha iniciado sesión.\n");
		taChat.setCaretPosition(taChat.getDocument().getLength());
		
	}
	
	public void notificarLogout(String login) {
		taChat.append(login + " ha dejado el chat.\n");
		taChat.setCaretPosition(taChat.getDocument().getLength());
		
	}
	
    public void setColorActual(Color colorActual)
    {
        canvasPaint.setColorActual(colorActual);
    }
    

}
