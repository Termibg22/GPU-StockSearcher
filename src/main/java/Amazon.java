import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

public class Amazon {
    private GUI GUI;
    private ArrayList<GPU> GPUs = new ArrayList();
    public String URLs_individual = "Amazon/URLs_individual.txt";

    public void individual() {
        try {
            String CurrentLine = "";
            FileInputStream fis = new FileInputStream(this.URLs_individual);
            InputStreamReader isr = new InputStreamReader((InputStream)fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            try {
                while ((CurrentLine = br.readLine()) != null) {
                    this.actualizar_lista_individual(this.extract_GPU(CurrentLine));
                }
            }
            catch (IllegalArgumentException e) {
                this.GUI.ErrorAccessingWeb(CurrentLine);
            }
            catch (IOException e) {
                this.GUI.ErrorAccessingWeb(CurrentLine);
            }
            ((InputStream)fis).close();
            isr.close();
            br.close();
        }
        catch (FileNotFoundException e) {
            this.GUI.Filenotfound("URLs_individual: " + this.URLs_individual);
        }
        catch (IOException e) {
            this.GUI.ErrorGeneral(e.getMessage());
        }
    }

    private void actualizar_lista_individual(GPU gpu) {
        boolean encontrada = false;
        for (int i = 0; !encontrada && i < this.GPUs.size(); ++i) {
            GPU cache_gpu = this.GPUs.get(i);
            if (!cache_gpu.getWeb().equals(gpu.getWeb())) continue;
            encontrada = true;
            if (cache_gpu.getDisponibilidad().equals(gpu.getDisponibilidad())) continue;
            cache_gpu.setDisponibilidad(gpu.getDisponibilidad());
            cache_gpu.setPrecio(gpu.getPrecio());
            this.GUI.actualizar_gpu(gpu);
        }
        if (!encontrada) {
            this.GPUs.add(gpu);
            this.GUI.add_gpu(gpu);
        }
    }

    private GPU extract_GPU(String web) throws IOException {
        String html = "";
        double p = 0.0;
        html = Jsoup.connect((String)web).get().html();
        String nombre = StringUtils.substringBetween((String)html, (String)"<meta name=\"keywords\" content=\"", (String)"\">");
        String precio = StringUtils.substringBetween((String)html, (String)"<input type=\"hidden\" id=\"attach-base-product-price\" value=\"", (String)"\">");
        if (precio != null) {
            p = Double.parseDouble(precio);
        }
        String disponibilidad = StringUtils.substringBetween((String)html, (String)"class=\"a-button-text\" aria-hidden=\"true\"> Comprar ", (String)"</span></span></span>");
        String tienda = "Amazon";
        disponibilidad = disponibilidad != null && disponibilidad.equals("ya ") ? "STOCK" : ((disponibilidad = StringUtils.substringBetween((String)html, (String)"Disponible", (String)"trav\u00e9s de")) != null && disponibilidad.equals(" a ") ? "STOCK" : "NO STOCK");
        return new GPU(nombre, p, disponibilidad, tienda, web);
    }

    public void setGUI(GUI g) {
        this.GUI = g;
    }
}