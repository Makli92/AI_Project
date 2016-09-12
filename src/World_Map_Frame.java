import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Makli
 */
public class World_Map_Frame extends javax.swing.JFrame {
    private Color[] colors_available;
    public Map<Integer, World_Actor> current_actor_map;
    JMenuBar WMMenu;
    
    Pattern number_pattern;
    Pattern building_pattern;
    Pattern end_pattern;
    
    World_Actor_Data_Dialog data_DLG;
    
    /**
     * Creates new form World_Map_Frame
     * @throws java.io.IOException
     */
    public World_Map_Frame() throws IOException {
        initComponents();
        
        /// Initialize Actor map
        current_actor_map = new HashMap<>();
        data_DLG = new World_Actor_Data_Dialog(this, false);
        
        /// Define colors
        Initialize_Actor_Color();
        
        /// Define actor file patterns
        Initialize_Actor_Pattern();
        
        /// Define menu bar and items
        Initialize_Menu();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setJMenuBar(WMMenu);
        pack();
        setVisible(true);
    }
    
    private void Initialize_Actor_Color() {
        /// Define colors_available
        colors_available = new Color[10];
        
        colors_available[0] = Color.RED;
        colors_available[1] = Color.GREEN;
        colors_available[2] = Color.BLUE;
        colors_available[3] = Color.WHITE;
        colors_available[4] = Color.YELLOW;
        colors_available[5] = Color.PINK;
        colors_available[6] = Color.ORANGE;
        colors_available[7] = Color.MAGENTA;
        colors_available[8] = Color.CYAN;
        colors_available[9] = Color.BLACK;
    }

    private void Initialize_Actor_Pattern() {
        // Regular expressions
        String number = "\\d";
        String building = "^(.*?[A-Z])";
        String end = "END";
        
        // Initialize Patterns
        number_pattern = Pattern.compile(number);
        building_pattern = Pattern.compile(building);
        end_pattern = Pattern.compile(end);
    }
    
    private void Initialize_Menu() {
        WMMenu = new JMenuBar();
        
        JMenu WMMFile = new JMenu("File");
        JMenuItem WMMLoad_Map = new JMenuItem("Load Map");
        JMenuItem WMMLoad_Actor = new JMenuItem("Load Actor");
        JMenuItem WMMExit = new JMenuItem("Exit");
        
        JMenu WMMView = new JMenu("View");
        JMenuItem WMMPlay = new JMenuItem("Play");
        WMMPlay.setEnabled(false);
        JMenuItem WMMPause = new JMenuItem("Pause");
        WMMPause.setEnabled(false);
        JMenuItem WMMCancel = new JMenuItem("Cancel");
        WMMCancel.setEnabled(false);
        
        JMenu WMMData = new JMenu("Data");
        JMenuItem WMMShowActorsData = new JMenuItem("Show Actors Data");
        WMMShowActorsData.setEnabled(false);
        JMenuItem WMMStatistics = new JMenuItem("Statistics");
        WMMStatistics.setEnabled(false);
        
        
        WMMFile.add(WMMLoad_Map);
        WMMFile.add(WMMLoad_Actor);
        WMMFile.add(WMMExit);
        
        WMMView.add(WMMPlay);
        WMMView.add(WMMPause);
        WMMView.add(WMMCancel);
        
        WMMData.add(WMMShowActorsData);
        WMMData.add(WMMStatistics);
        
        WMMenu.add(WMMFile);
        WMMenu.add(WMMView);
        WMMenu.add(WMMData);
        
        /// Define list of menuitems that need to be enabled or disabled accordingly
        JMenuItem[] view_items = {WMMPlay, WMMPause, WMMCancel};
        JMenuItem[] show_items = {WMMShowActorsData, WMMStatistics};
        
        /// Create and set new Listener
        World_Map_Menu_Listener WMMListener = new World_Map_Menu_Listener(this, view_items, show_items);
        
        WMMLoad_Map.addActionListener( WMMListener );
        WMMLoad_Actor.addActionListener( WMMListener );
        WMMExit.addActionListener( WMMListener );
        WMMPlay.addActionListener( WMMListener );
        WMMPause.addActionListener( WMMListener );
        WMMCancel.addActionListener( WMMListener );
        WMMShowActorsData.addActionListener( WMMListener );
        WMMStatistics.addActionListener( WMMListener );
    }
    
    public boolean Create_Actors(String actor_file_location) throws IOException {
        try { 
            BufferedReader file_reader = new BufferedReader(new FileReader(actor_file_location));
            String file_next_line = file_reader.readLine();
            
            if (file_next_line.contains("*")) {
                JOptionPane.showMessageDialog(null, "Προέκυψε σφάλμα κατά τη φόρτωση των πρακτόρων : Μη έγκυρο αρχείο.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            int current_num = -1;
            int current_sum_actor = 0;
            int current_order = -1;
            
            while ( file_next_line != null ) {
                if ( number_pattern.matcher(file_next_line).find() ) {
                    current_num = Integer.parseInt(file_next_line);
                    current_sum_actor++;
                    current_order = 1;
                    current_actor_map.put(current_num, new World_Actor(colors_available[current_sum_actor - 1], current_num));
                } else if ( end_pattern.matcher(file_next_line).find() ) {
                    current_actor_map.get(current_num).addBuilding(new World_Building(Integer.toString(current_num), current_order));
                    current_num = -1;
                    current_order = -1;
                } else if ( building_pattern.matcher(file_next_line).find() ) {
                    current_actor_map.get(current_num).addBuilding(new World_Building(file_next_line, current_order));
                    current_order++;
                } 
                
                file_next_line = file_reader.readLine();
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Προέκυψε σφάλμα κατά τη φόρτωση των πρακτόρων : " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
        
        return true;
    }
    
    public boolean Clear_Extra_Actors() {
        int[] actor_to_delete = new int[current_actor_map.size()];
        int index = -1;
        for (Integer num : current_actor_map.keySet()) {
            Point current_actor_position = ((World_Map_Panel)this.getContentPane()).World_Map_Get_Position(Integer.toString(num));
            
            if ( current_actor_position.equals(new Point(-1, -1)) ) {
                index++;
                actor_to_delete[index] = num;
            }
        }
        
        for ( int array_index = 0; array_index <= index; array_index++) {
            current_actor_map.remove(actor_to_delete[array_index]);
        }
        
        /// Set data
        Set_Actor_Map_Data();
        
        return true;
    }
    
    public void Set_Actor_Map_Data() {
        for (Integer num : current_actor_map.keySet()) {
            Point current_actor_position = ((World_Map_Panel)this.getContentPane()).World_Map_Get_Position(Integer.toString(num));
            
            current_actor_map.get(num).setCurrentPosition(current_actor_position);
            current_actor_map.get(num).setPreviousPosition(current_actor_position);
            current_actor_map.get(num).setSize(((World_Map_Panel)this.getContentPane()).WMPD);
            current_actor_map.get(num).getBuilding(Integer.toString(num)).setCoordinates(current_actor_position);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(World_Map_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(World_Map_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(World_Map_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(World_Map_Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new World_Map_Frame().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(World_Map_Frame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}