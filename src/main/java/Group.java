import java.math.BigDecimal;
import java.math.RoundingMode;

public class Group {
    private String tipo;
    private double precio;
    private String disponibilidad;
    private String tienda;
    private String web;

    public Group(String tipo, String disponibilidad, String tienda, String web) {
        this.tipo = tipo;
        this.precio = this.precio;
        this.disponibilidad = disponibilidad;
        this.tienda = tienda;
        this.web = web;
    }

    public String getTipo() {
        return this.tipo;
    }

    public double getPrecio() {
        return this.precio;
    }

    public String getDisponibilidad() {
        return this.disponibilidad;
    }

    public String getTienda() {
        return this.tienda;
    }

    public String getWeb() {
        return this.web;
    }

    public void setNombre(String tipo) {
        this.tipo = tipo;
    }

    public void setDisponibilidad(String disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}