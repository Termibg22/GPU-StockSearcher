import java.awt.Color;
import javax.swing.JComponent;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;

public class Table_individual extends JXTable
{
    public Table_individual(final TableModel_individual t) {
        super((TableModel)t);
    }

    public Component prepareRenderer(final TableCellRenderer renderer, final int rowIndex, final int columnIndex) {
        final JComponent component = (JComponent)super.prepareRenderer(renderer, rowIndex, columnIndex);
        if (rowIndex == 0) {
            component.setFont(component.getFont().deriveFont(1));
        }
        if (this.getValueAt(rowIndex, 4).equals("STOCK") && columnIndex == 4) {
            component.setBackground(Color.GREEN);
        }
        else if (this.getValueAt(rowIndex, 4).equals("NO STOCK") && columnIndex == 4) {
            component.setBackground(Color.RED);
        }
        else {
            component.setBackground(Color.WHITE);
        }
        return component;
    }
}
