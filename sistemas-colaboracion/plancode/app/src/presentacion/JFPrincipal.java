package presentacion;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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

import presentacion.auxiliares.Dialogos;
import presentacion.auxiliares.handlerImagenFondoPanel;
import presentacion.auxiliares.panelConImagenFondo;

import com.cloudgarden.layout.AnchorConstraint;
import com.sun.media.jsdt.ConnectionException;
import com.sun.media.jsdt.InvalidClientException;
import com.sun.media.jsdt.NoSuchChannelException;
import com.sun.media.jsdt.NoSuchClientException;
import com.sun.media.jsdt.NoSuchConsumerException;
import com.sun.media.jsdt.NoSuchSessionException;
import com.sun.media.jsdt.PermissionDeniedException;
import com.sun.media.jsdt.TimedOutException;
import comunicaciones.EventosCanales.MensajeChatRecibidoEvent;
import comunicaciones.EventosCanales.MensajeChatRecibidoListener;
import comunicaciones.EventosCanales.MensajeListaUsuariosEvent;
import comunicaciones.EventosCanales.MensajeListaUsuariosListener;
import comunicaciones.EventosCanales.MensajeMapaEvent;
import comunicaciones.EventosCanales.MensajeMapaListener;
import comunicaciones.EventosCanales.MensajeTrazoEvent;
import comunicaciones.EventosCanales.MensajeTrazoListener;

import dominio.conocimiento.Roles;
import dominio.conocimiento.Usuario;
import dominio.control.ControladorPrincipal;
import info.clearthought.layout.TableLayout;

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
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
		}
	}


	private static final long serialVersionUID = -3368293739950370869L;
	private ControladorPrincipal controlador;
	private JPanel jPnlToolBoox;
	private JPanel jPnlUsuarios;
	private JPanel panelPaint;	
	private panelConImagenFondo jPanelFondo;
	private JButton btnCargarMapa;
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
	private JFileChooser fc;
	
	private Color colorCliente = null;
	private ImageIcon mapa = null;

	public JFPrincipal(ControladorPrincipal c) {
		super();
		controlador = c;
		initGUI();
		
		// Para el caso del servidor, como es el primer cliente que se conecta y no recibe el evento del canal
		// la primera vez, se solicia al controlador la lista de usuarios (que sólo lo contendrá a él)
		if (c.isServidor()) {
			// Establecemos el color
			setColorActual(controlador.getListaUsuarios());
			actualizarListaUsuarios(c.getListaUsuarios());
		}
		
		// Ponemos el listener a los consumidores del chat para poder recibir los mensajes de chat
		c.getConsumidorCanalChat().addMensajeChatRecibidoListener(new MensajeChatRecibidoListener() {
			public void MensajeChatRecibido(MensajeChatRecibidoEvent evt) {
				// Ponemos el mensaje en el chat, coloreandolo con el color del usuario que lo envia
				ponerMensajeChat(evt);
			}
		});
		
		// Ponemos el listener a los consumidores del canal de gestión para poder recibir la lista de usuarios conectados
		c.getConsumidorGestionListaUsuarios().addMensajeListaUsuariosListener(new MensajeListaUsuariosListener() {
			public void MensajeListaUsuarios(MensajeListaUsuariosEvent evt) {
				actualizarListaUsuarios(evt.getLista());
				setColorActual(evt.getLista());
			}
		});
		
		c.getConsumidorTrazos().addMensajeTrazoListener(new MensajeTrazoListener() {
			public void MensajeTrazo(MensajeTrazoEvent evt) {
				canvasPaint.setTrazos(evt.getInfo());;
				canvasPaint.repaint();
			}
		});
		
		c.getConsumidorMapa().addMensajeMapaListener(new MensajeMapaListener() {
			public void MensajeMapa(MensajeMapaEvent evt) {
				setMapaRecibido(evt.getMapa());
			}

		});
	}
	
	private void setMapaRecibido(ImageIcon mapa) {

		// Se crea un panel para poner el mapa recibido de fondo
		jPanelFondo = new panelConImagenFondo();
		jPanelFondo.setMapaFondoRecibido(mapa);
		// Se añade ese panel y el canvas al área de trabajo
		initAreaTrabajo();
	}
	
	private void setMapaLocal(URL urlMapa) {
		// Se crea un panel para poner el mapa recibido de fondo
		jPanelFondo = new panelConImagenFondo();
		jPanelFondo.setMapaFondoLocal(urlMapa);
		// Se añade ese panel y el canvas al área de trabajo
		initAreaTrabajo();
	}
	
	private void initAreaTrabajo() {
		// Al establecer un nuevo mapa, se elimina el panel con el mapa que hubiese cargado (si había alguno)
		panelPaint.removeAll();
		jPanelFondo.setLayout(null);
		jPanelFondo.setBounds(6, 20, 544, 302);
		// Se limpia el canvas, porque solo existe una instancia de él
		canvasPaint.clear();
    	panelPaint.add(canvasPaint);
    	panelPaint.add(jPanelFondo);
		jPanelFondo.repaint();
    	panelPaint.revalidate();
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
					jPnlToolBoox.setBounds(10, 49, 132, 329);
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
					{
						btnCargarMapa = new JButton();
						jPnlToolBoox.add(btnCargarMapa);
						btnCargarMapa.setText("CargarMapa");
						btnCargarMapa.setBounds(1, 109, 66, 23);
						btnCargarMapa.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								btnCargarMapaActionPerformed(evt);
							}
						});
					}
				}
				{
					jPnlUsuarios = new JPanel();
					TableLayout jPnlUsuariosLayout = new TableLayout(new double[][] {{0.3,0.3,0.3,0.3,0.3,0.3}, {0.2,0.2,0.2,0.2,0.2,0.2}});
					jPnlUsuariosLayout.setHGap(10);
					jPnlUsuariosLayout.setVGap(10);
					jPanel1.add(jPnlUsuarios, new AnchorConstraint(25, 17, 632, 757, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					jPnlUsuarios.setLayout(jPnlUsuariosLayout);
					jPnlUsuarios.setBorder(BorderFactory.createTitledBorder("Usuarios"));
					jPnlUsuarios.setBounds(697, 49, 160, 329);
				}
				{
					
					panelPaint = new JPanel();
					jPanel1.add(panelPaint, new AnchorConstraint(25, 744, 634, 229, AnchorConstraint.ANCHOR_ABS, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
					panelPaint.setBorder(BorderFactory.createTitledBorder("Área de trabajo"));
					panelPaint.setLayout(null);
					panelPaint.setBounds(142, 49, 555, 329);
					{
						canvasPaint = new CanvasPaint(controlador);
						canvasPaint.setOpaque(false);
						panelPaint.add(canvasPaint);
						canvasPaint.setBounds(4, 17, 548, 305);
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
								taChat.setForeground(colorCliente);
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
			Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
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
	}

	private void actualizarListaUsuarios(Hashtable<String, Usuario> lista) {
		Enumeration<String> clientesConectados = lista.keys();
		String cliente;
		int contador = 0;
		// Borramos los elementos del panel de sesiones y los volemos a dibujar
		jPnlUsuarios.removeAll();
		while (clientesConectados.hasMoreElements()) {
			JLabel icon = new JLabel();
			JLabel nombre = new JLabel();
			cliente = clientesConectados.nextElement();
			// Segun el rol, cargamos una u otra imagen
			if (lista.get(cliente).getRol().equals(Roles.Policia))
					icon.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ambulancia.GIF")));
			else if (lista.get(cliente).getRol().equals(Roles.Sanidad))
				icon.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ambulancia.GIF")));
			else
				icon.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/ambulancia.GIF")));
			// Colocamos el rol en el icono, junto al nombre
			nombre.setText(cliente);
			nombre.setForeground(lista.get(cliente).getColor());
			jPnlUsuarios.add(icon, "0, " + String.valueOf(contador));
			jPnlUsuarios.add(nombre, "2, " + String.valueOf(contador));
			contador ++;
		}
		// Esta llamada es necesaria para que los clientes uqe ya tenian la interfaz inicializada, refresquen sus paneles de sesiones
		jPnlUsuarios.revalidate();

	}

	private void btnEnviarActionPerformed(ActionEvent evt) {
		try {
			this.controlador.enviarMensajeChat(txtMensaje.getText());
		} catch (ConnectionException e) {
			Dialogos.mostrarDialogoError(this, "Error", "No se puede establecer una conexión");
		} catch (InvalidClientException e) {
			Dialogos.mostrarDialogoError(this, "Error", "Cliente de destino inválido");
		} catch (NoSuchChannelException e) {
			Dialogos.mostrarDialogoError(this, "Error", "No existe el canal");
		} catch (NoSuchClientException e) {
			Dialogos.mostrarDialogoError(this, "Error", "No se encuentra el cliente de destino");
		} catch (NoSuchSessionException e) {
			Dialogos.mostrarDialogoError(this, "Error", "No se encuentra la sesión");
		} catch (PermissionDeniedException e) {
			Dialogos.mostrarDialogoError(this, "Error", "Permiso denegado");
		} catch (TimedOutException e) {
			Dialogos.mostrarDialogoError(this, "Error", "Tiempo de espera agotado");
		}
		txtMensaje.setText("");
	}
	

	public void iniciarSesion(String login) {
		// Cuando un cliente se conecta, se notifica su entrada en el chat al resto de clientes.
		taChat.append(login + " ha iniciado sesión.\n");
		taChat.setCaretPosition(taChat.getDocument().getLength());
		// Además, el cliente que actúa como servidor envía el mapa y los trazos ya dibujados al cliente que 
		// acaba de iniciar sesión, para que esté sincronizado con el resto
		if (controlador.isServidor()) {
			if (mapa!=null) {
				try {
					controlador.enviarMapaRecienConectado(login, mapa);
				} catch (ConnectionException e) {
					Dialogos.mostrarDialogoError(this, "Error", "No se puede establecer una conexión");
				} catch (InvalidClientException e) {
					Dialogos.mostrarDialogoError(this, "Error", "Cliente de destino inválido");
				} catch (NoSuchChannelException e) {
					Dialogos.mostrarDialogoError(this, "Error", "No existe el canal");
				} catch (NoSuchClientException e) {
					Dialogos.mostrarDialogoError(this, "Error", "No se encuentra el cliente de destino");
				} catch (NoSuchSessionException e) {
					Dialogos.mostrarDialogoError(this, "Error", "No se encuentra la sesión");
				} catch (PermissionDeniedException e) {
					Dialogos.mostrarDialogoError(this, "Error", "Permiso denegado");
				} catch (TimedOutException e) {
					Dialogos.mostrarDialogoError(this, "Error", "Tiempo de espera agotado");
				} catch (NoSuchConsumerException e) {
					Dialogos.mostrarDialogoError(this, "Error", "No existe el consumidor");
				}
			}
			// TODO: no funciona bien
			/*if (!canvasPaint.getTrazos().isEmpty()) {
				controlador.enviarTrazosRecienConectado(login, canvasPaint.getTrazos());
			}*/
		}
		
	}
	
	public void notificarLogout(String login) {
		taChat.append(login + " ha dejado el chat.\n");
		taChat.setCaretPosition(taChat.getDocument().getLength());
		
	}
	
    public void setColorActual(Hashtable<String, Usuario> lista)
    {
		// Ponemos el color a este cliente
		colorCliente = lista.get(controlador.getNombreCliente()).getColor();
        canvasPaint.setColorActual(colorCliente);
    }
    
    private void btnCargarMapaActionPerformed(ActionEvent evt) {
    	fc = new JFileChooser();
    	// Se estable el filtro para mostrar sólo imagenes png, jpg, jpeg y gif
    	fc.addChoosableFileFilter(new presentacion.auxiliares.ImageFilter());
    	fc.setAcceptAllFileFilterUsed(false);
    	// Se pone un mensaje personalizado tanto al botón del fileChooser como a su título
    	int valor = fc.showDialog(this, "Cargar imagen");
    	if (valor == fc.APPROVE_OPTION) {
    	    File image = fc.getSelectedFile();	    	
	    	try {
	    		// Se toma la ruta del mapa local
		    	URL urlFondo = image.toURL();
				// Se dibuja el panel
				setMapaLocal(urlFondo);
				// Se toma el objeto ImageIcon de este mapa
				mapa = handlerImagenFondoPanel.getMapaCargado();
				// Se envia este mapa al resto de clientes conectados
				controlador.enviarMapa(mapa);
			} catch (MalformedURLException e) {
				Dialogos.mostrarDialogoError(this, "Error", e.getMessage());
			} catch (ConnectionException e) {
				Dialogos.mostrarDialogoError(this, "Error", "No se puede establecer una conexión");
			} catch (InvalidClientException e) {
				Dialogos.mostrarDialogoError(this, "Error", "Cliente de destino inválido");
			} catch (NoSuchChannelException e) {
				Dialogos.mostrarDialogoError(this, "Error", "No existe el canal");
			} catch (NoSuchClientException e) {
				Dialogos.mostrarDialogoError(this, "Error", "No se encuentra el cliente de destino");
			} catch (NoSuchSessionException e) {
				Dialogos.mostrarDialogoError(this, "Error", "No se encuentra la sesión");
			} catch (PermissionDeniedException e) {
				Dialogos.mostrarDialogoError(this, "Error", "Permiso denegado");
			} catch (TimedOutException e) {
				Dialogos.mostrarDialogoError(this, "Error", "Tiempo de espera agotado");
			}
    	}

    }

}
