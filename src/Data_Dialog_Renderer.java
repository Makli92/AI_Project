import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Makli
 */
public class Data_Dialog_Renderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;
    Color[] colors_available;
    
    public Data_Dialog_Renderer() {
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

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        Object valueAt = table.getModel().getValueAt(row, col - 1);
        c.setBackground(colors_available[Integer.parseInt(valueAt.toString()) - 1]);

        return c;
    }
}
