package presentacion;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
public class Telepuntero extends JPanel {
	private static Telepuntero instancia = null;
	
	private String propietario;
	private String accion;
	
	private JLabel lblCursor;
	private JLabel lblPropietario;
	private JLabel lblAccion;

	protected Telepuntero () {
		super();
		this.propietario = "";
		this.accion = "";
		initGUI();
	}
	
	protected Telepuntero (String p, String a) {
		this.getTelepuntero();
		this.propietario = p;
		this.accion = a;
	}
	
	public static Telepuntero getTelepuntero() {
		if(instancia == null) {
			instancia = new Telepuntero();
		}
		return instancia;
	}
	
	public static Telepuntero getTelepuntero(String p, String a) {
		if(instancia == null) {
			instancia = new Telepuntero(p, a);
		}
		return instancia;
	}
	
	private void initGUI() {
		try {
			{
				this.setOpaque(false);
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.0, 0.0};
				thisLayout.rowHeights = new int[] {40, 40};
				thisLayout.columnWeights = new double[] {0.0, 0.1};
				thisLayout.columnWidths = new int[] {67, 7};
				this.setLayout(thisLayout);
				this.setPreferredSize(new java.awt.Dimension(163, 67));
				{
					lblCursor = new JLabel();
					this.add(lblCursor, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					lblCursor.setPreferredSize(new java.awt.Dimension(80, 6));
					lblCursor.setLayout(null);
				}
				{
					lblPropietario = new JLabel();
					this.add(lblPropietario, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					lblPropietario.setText("propietario");
				}
				{
					lblAccion = new JLabel();
					this.add(lblAccion, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					lblAccion.setText("accion");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
