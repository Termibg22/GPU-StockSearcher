import java.net.URISyntaxException;
import java.net.URI;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class TableModel_groups extends AbstractTableModel
{
    private final String[] column;
    private final ArrayList<Group> Groups;
    public boolean visible;

    public TableModel_groups() {
        this.column = new String[] { "TIPO", "WEB", "TIENDA", "DISPONIBILIDAD" };
        this.visible = false;
        this.Groups = new ArrayList<Group>();
    }

    @Override
    public int getRowCount() {
        if (this.visible) {
            return this.Groups.size() + 1;
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return this.column.length;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (rowIndex == 0 && this.Groups.size() > 0) {
            return this.column[columnIndex];
        }
        if (this.Groups.size() > 0) {
            switch (columnIndex) {
                case 0: {
                    return this.Groups.get(rowIndex - 1).getTipo();
                }
                case 1: {
                    try {
                        return new URI(this.Groups.get(rowIndex - 1).getWeb());
                    }
                    catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                case 2: {
                    return this.Groups.get(rowIndex - 1).getTienda();
                }
                case 3: {
                    return this.Groups.get(rowIndex - 1).getDisponibilidad();
                }
            }
        }
        return "";
    }

    public void add(final Group group) {
        this.Groups.add(group);
        final int row = this.Groups.indexOf(group);
        this.fireTableRowsInserted(row, row);
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return String.class;
            }
            case 2: {
                return String.class;
            }
            case 3: {
                return String.class;
            }
            case 4: {
                return String.class;
            }
            case 1: {
                return URI.class;
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return false;
    }
}