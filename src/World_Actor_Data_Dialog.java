import java.awt.Component;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Makli
 */

public class World_Actor_Data_Dialog extends javax.swing.JDialog {
    /**
     * Creates new form World_Actor_Data_Dialog
     * @param parent
     * @param modal
     */
    public World_Actor_Data_Dialog(World_Map_Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        setVisible(false);
    }
    
    public void Data_Populate_Table(Map<Integer, World_Actor> actor_map) {
        DefaultTableModel Data_Model = (DefaultTableModel) Data_Table.getModel();
        for (Integer num : actor_map.keySet()) {
            for ( String name : actor_map.get(num).building_map.keySet() ) {
                if ( actor_map.get(num).building_map.get(name).getOrder() != -1 ) {
                    Data_Model.addRow(new Object[]{ num, 
                                                    "", 
                                                    "(" + actor_map.get(num).getLocation().x + ", " + actor_map.get(num).getLocation().y + ")", 
                                                    name, 
                                                    "(" + actor_map.get(num).getBuilding(name).getCoordinates().x + ", " + actor_map.get(num).getBuilding(name).getCoordinates().y + ")", 
                                                    Integer.toString(actor_map.get(num).getBuilding(name).getOrder()), 
                                                    actor_map.get(num).getBuilding(name).isVisited()});
                    
                    Data_Table.getColumnModel().getColumn(1).setCellRenderer(new Data_Dialog_Renderer());
                }   
            }
        }
    }
    
    public void Data_Clear_Table() {
        ((DefaultTableModel)Data_Table.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Data_ScrollPane = new javax.swing.JScrollPane();
        Data_Table = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Data_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Actor", "Color", "Position", "Building", "Coordinates", "Order", "Visited"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Data_ScrollPane.setViewportView(Data_Table);
        if (Data_Table.getColumnModel().getColumnCount() > 0) {
            Data_Table.getColumnModel().getColumn(0).setResizable(false);
            Data_Table.getColumnModel().getColumn(1).setResizable(false);
            Data_Table.getColumnModel().getColumn(2).setResizable(false);
            Data_Table.getColumnModel().getColumn(3).setResizable(false);
            Data_Table.getColumnModel().getColumn(4).setResizable(false);
            Data_Table.getColumnModel().getColumn(5).setResizable(false);
            Data_Table.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Data_ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Data_ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane Data_ScrollPane;
    private javax.swing.JTable Data_Table;
    // End of variables declaration//GEN-END:variables
}
