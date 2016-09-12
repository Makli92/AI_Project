import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * @author Makli
 */

public class World_Map_Menu_Listener implements ActionListener  {
    /// Variables
    static JMenuItem[] Menu_View_Item;
    static JMenuItem[] Menu_Show_Item;
    static World_Map_Frame parent_Frame;
    
    String World_Map_Location;
    String World_Actor_Location;
    
    boolean Menu_Map_Exists;
    boolean Menu_Actor_Exists;
    
    public World_Map_Menu_Listener(World_Map_Frame frame, JMenuItem[] view_items, JMenuItem[] show_items) {
        Menu_View_Item = view_items;
        Menu_Show_Item = show_items;
        parent_Frame = frame;
        Menu_Map_Exists = false;
        Menu_Actor_Exists = false;
    }
    
    public void Load_Map_File() {
        FileDialog Map_DLG = new FileDialog(new JFrame(), "Choose map file", FileDialog.LOAD);
        Map_DLG.setDirectory("C:\\");
        Map_DLG.setFile("*.txt");
        Map_DLG.setVisible(true);

        if ( Map_DLG.getFile() != null ) {
            World_Map_Panel WMP;

            try {
                WMP = new World_Map_Panel(Map_DLG.getDirectory() + Map_DLG.getFile(), Menu_Show_Item[1]);

                if ( WMP.err_exists ) {
                    return;
                } else {
                    Menu_Map_Exists = true;
                }
                
                /// Set content pane
                parent_Frame.setContentPane(WMP);
                parent_Frame.setSize(WMP.getPreferredSize());
                parent_Frame.revalidate();
                
                World_Map_Location = Map_DLG.getDirectory() + Map_DLG.getFile();
                
                if ( Menu_Actor_Exists ) {
                    /// Clear actors if more than expected and set their initial position
                    if ( parent_Frame.Clear_Extra_Actors() ) {
                        for ( JMenuItem Menu_View_Item_Iterator : Menu_View_Item ) {
                            if ( !Menu_View_Item_Iterator.getText().equals("Pause") ) {
                                Menu_View_Item_Iterator.setEnabled(true);
                            }
                        }
                    }
                    
                    Menu_Show_Item[0].setEnabled(true);
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(World_Map_Menu_Listener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void Load_Actor_File() throws IOException {
        FileDialog Actor_DLG = new FileDialog(new JFrame(), "Choose actors' file", FileDialog.LOAD);
        Actor_DLG.setDirectory("C:\\");
        Actor_DLG.setFile("*.txt");
        Actor_DLG.setVisible(true);

        if ( Actor_DLG.getFile() != null ) {
            if ( parent_Frame.Create_Actors(Actor_DLG.getDirectory() + Actor_DLG.getFile()) ) {
                if ( Menu_Map_Exists ) {
                    for ( JMenuItem Menu_View_Item_Iterator : Menu_View_Item ) {
                        if ( !Menu_View_Item_Iterator.getText().equals("Pause") ) {
                            Menu_View_Item_Iterator.setEnabled(true);
                        }
                    }
                    
                    /// Clear actors if more than expected and set their initial position
                    parent_Frame.Clear_Extra_Actors();
                    
                    Menu_Show_Item[0].setEnabled(true);
                }
                
                World_Actor_Location = Actor_DLG.getDirectory() + Actor_DLG.getFile();
                
                Menu_Actor_Exists = true;
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {    
        switch (e.getActionCommand()) {
            case "Load Map":
                /// Load Map file
                Load_Map_File();
                break;
            case "Load Actor":
                try {
                    /// Load actors' plans' file
                    Load_Actor_File();
                } catch (IOException ex) {
                    Logger.getLogger(World_Map_Menu_Listener.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "Exit":
                /// Exit program
                parent_Frame.dispatchEvent(new WindowEvent(parent_Frame, WindowEvent.WINDOW_CLOSING));
                break;
            case "Play":
                /// Start global timer 
                for ( JMenuItem Menu_View_Item_Iterator : Menu_View_Item ) {
                    if ( !Menu_View_Item_Iterator.getText().equals(e.getActionCommand()) ) {
                        Menu_View_Item_Iterator.setEnabled(true);
                    } else {
                        Menu_View_Item_Iterator.setEnabled(false);
                    }
                }
                
                ((World_Map_Panel)parent_Frame.getContentPane()).World_Paint_Timer.start();
                ((World_Map_Panel)parent_Frame.getContentPane()).World_Timer.start();
                
                break;
            case "Pause":
                /// Pause global timer
                for ( JMenuItem Menu_View_Item_Iterator : Menu_View_Item ) {
                    if ( !Menu_View_Item_Iterator.getText().equals(e.getActionCommand()) ) {
                        Menu_View_Item_Iterator.setEnabled(true);
                    } else {
                        Menu_View_Item_Iterator.setEnabled(false);
                    }
                }
                
                ((World_Map_Panel)parent_Frame.getContentPane()).World_Paint_Timer.stop();
                ((World_Map_Panel)parent_Frame.getContentPane()).World_Timer.stop();
                
                
                break;
            case "Cancel":
                /// Stop timer, reload map and actors(re-initialization)
                for ( JMenuItem Menu_View_Item_Iterator : Menu_View_Item ) {
                    if ( !Menu_View_Item_Iterator.getText().equals("Pause") ) {
                        Menu_View_Item_Iterator.setEnabled(true);
                    } else {
                        Menu_View_Item_Iterator.setEnabled(false);
                    }
                }
                
                Menu_Show_Item[1].setEnabled(false);
                
                ((World_Map_Panel)parent_Frame.getContentPane()).World_Paint_Timer.stop();
                ((World_Map_Panel)parent_Frame.getContentPane()).World_Timer.stop();
                
                try {
                    /// Re-Initialize Interface
                    World_Map_Panel WMP = new World_Map_Panel(World_Map_Location, Menu_Show_Item[1]);
                    parent_Frame.Create_Actors(World_Actor_Location);
                    
                    /// Clear extra actors
                    parent_Frame.Clear_Extra_Actors();
                    
                    /// Reset content pane
                    parent_Frame.getContentPane().removeAll();
                    parent_Frame.setContentPane(WMP);
                    parent_Frame.setSize(WMP.getPreferredSize());
                    parent_Frame.revalidate();
                    
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(World_Map_Menu_Listener.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                break;
            case "Show Actors Data":
                parent_Frame.data_DLG.Data_Clear_Table();
                parent_Frame.data_DLG.Data_Populate_Table(parent_Frame.current_actor_map);
                parent_Frame.data_DLG.setVisible(true);
                break;
            case "Statistics":
                JFileChooser Stats_DLG = new JFileChooser();
                if ( Stats_DLG.showSaveDialog(parent_Frame) == JFileChooser.APPROVE_OPTION ) {
                    PrintWriter Stats_writer = null;
                    try {
                        Stats_writer = new PrintWriter(Stats_DLG.getSelectedFile().getAbsoluteFile(), "UTF-8");
                    } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                        JOptionPane.showMessageDialog(null, "Προέκυψε σφάλμα κατά τη δημιουργία αρχείου στατιστικών : " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                    
                    Stats_writer.println( "Execution time : " + ((World_Map_Panel)(parent_Frame.getContentPane())).secs_elapsed);
                    Stats_writer.println();
                    
                    for (Integer num : parent_Frame.current_actor_map.keySet()) {
                        Stats_writer.println( "Actor : " + num );
                        Stats_writer.println( "Steps : " + parent_Frame.current_actor_map.get(num).steps );
                        Stats_writer.println( "Knowledge transactions : " + parent_Frame.current_actor_map.get(num).knowledge_transactions );
                        Stats_writer.println();
                    }
                    
                    Stats_writer.close();
                }
                break;
            default:
                break;
        }
    }
}
