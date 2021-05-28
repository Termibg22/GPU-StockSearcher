import java.net.URISyntaxException;
import java.net.URI;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class TableModel_individual extends AbstractTableModel
{
    private final String[] column;
    private final ArrayList<GPU> GPUs;
    public boolean visible;

    public TableModel_individual() {
        this.column = new String[] { "NOMBRE", "WEB", "PRECIO", "TIENDA", "DISPONIBILIDAD" };
        this.visible = false;
        this.GPUs = new ArrayList<GPU>();
    }

    @Override
    public int getRowCount() {
        if (this.visible) {
            return this.GPUs.size() + 1;
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return this.column.length;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (rowIndex == 0) {
            return this.column[columnIndex];
        }
        switch (columnIndex) {
            case 0: {
                return this.GPUs.get(rowIndex - 1).getNombre();
            }
            case 1: {
                try {
                    return new URI(this.GPUs.get(rowIndex - 1).getWeb());
                }
                catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            case 2: {
                return this.GPUs.get(rowIndex - 1).getPrecio() + " \u20ac";
            }
            case 3: {
                return this.GPUs.get(rowIndex - 1).getTienda();
            }
            case 4: {
                return this.GPUs.get(rowIndex - 1).getDisponibilidad();
            }
            default: {
                return null;
            }
        }
    }

    public void add(final GPU gpu) {
        this.GPUs.add(gpu);
        final int row = this.GPUs.indexOf(gpu);
        this.fireTableRowsInserted(row, row);
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 2:
            case 3:
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