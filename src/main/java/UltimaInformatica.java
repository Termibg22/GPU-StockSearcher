import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.util.ArrayList;


public class UltimaInformatica
{
    private ArrayList<GPU> GPUs;
    private ArrayList<Group> Groups;
    public String URLs_individual;
    public String URLs_groups;
    private GUI GUI;

    public UltimaInformatica() {
        this.GPUs = new ArrayList<GPU>();
        this.Groups = new ArrayList<Group>();
        this.URLs_individual = "UltimaInformatica/URLs_individual.txt";
        this.URLs_groups = "UltimaInformatica/URLs_groups.txt";
    }

    public void individual() {
        String CurrentLine = "";
        try {
            final InputStream fis = new FileInputStream(this.URLs_individual);
            final InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            final BufferedReader br = new BufferedReader(isr);
            try {
                while ((CurrentLine = br.readLine()) != null) {
                    this.actualizar_lista(this.extract_GPU(CurrentLine));
                }
            }
            catch (IllegalArgumentException e2) {
                this.GUI.ErrorAccessingWeb(CurrentLine);
            }
            catch (IOException e3) {
                this.GUI.ErrorAccessingWeb(CurrentLine);
            }
            fis.close();
            isr.close();
            br.close();
        }
        catch (FileNotFoundException e4) {
            this.GUI.Filenotfound("URLs_individual: " + this.URLs_individual);
        }
        catch (IOException e) {
            this.GUI.ErrorGeneral(e.getMessage());
        }
    }

    public void groups() {
        try {
            final InputStream fis = new FileInputStream(this.URLs_groups);
            final InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            final BufferedReader br = new BufferedReader(isr);
            String type = "";
            int i = 0;
            String CurrentLine;
            while ((CurrentLine = br.readLine()) != null) {
                if (i % 2 == 0) {
                    type = CurrentLine;
                }
                else if (this.stock(CurrentLine, "\nRec\u00edbelo ")) {
                    this.actualizar_lista_groups(new Group(type, "STOCK", "UltimaInformatica", CurrentLine));
                }
                else {
                    this.actualizar_lista_groups(new Group(type, "NO STOCK", "UltimaInformatica", CurrentLine));
                }
                ++i;
            }
            fis.close();
            isr.close();
            br.close();
        }
        catch (FileNotFoundException e2) {
            this.GUI.Filenotfound("URLs_groups: " + this.URLs_groups);
        }
        catch (IOException e) {
            this.GUI.ErrorGeneral(e.getMessage());
        }
    }

    private void actualizar_lista(final GPU gpu) {
        boolean encontrada = false;
        for (int i = 0; !encontrada && i < this.GPUs.size(); ++i) {
            final GPU cache_gpu = this.GPUs.get(i);
            if (cache_gpu.getNombre().equals(gpu.getNombre())) {
                encontrada = true;
                if (!cache_gpu.getDisponibilidad().equals(gpu.getDisponibilidad())) {
                    cache_gpu.setDisponibilidad(gpu.getDisponibilidad());
                    cache_gpu.setPrecio(gpu.getPrecio());
                    this.GUI.actualizar_gpu(gpu);
                }
            }
        }
        if (!encontrada) {
            this.GPUs.add(gpu);
            this.GUI.add_gpu(gpu);
        }
    }

    private void actualizar_lista_groups(final Group group) {
        boolean encontrada = false;
        for (int i = 0; !encontrada && i < this.Groups.size(); ++i) {
            final Group cache_group = this.Groups.get(i);
            if (cache_group.getTipo().equals(group.getTipo())) {
                encontrada = true;
                if (!cache_group.getDisponibilidad().equals(group.getDisponibilidad())) {
                    cache_group.setDisponibilidad(group.getDisponibilidad());
                    this.GUI.actualizar_group(group);
                }
            }
        }
        if (!encontrada) {
            this.Groups.add(group);
            this.GUI.add_group(group);
        }
    }

    private boolean stock(final String web, final String clave) throws IOException {
        String html = "";
        html = Jsoup.connect(web).get().html();
        return html.contains(clave);
    }

    private GPU extract_GPU(final String web) throws IOException {
        String html = "";
        final Document doc = Jsoup.connect(web).get();
        final String nombre = doc.title();
        final Elements price = doc.select("meta[property=product:price:amount]");
        final String p = price.attr("content");
        final double precio = Double.parseDouble(p);
        html = doc.html();
        String disponibilidad = StringUtils.substringBetween(html, "data-button-action=\"add-to-cart\" type=\"submit\">", "</button>");
        final String tienda = "UltimaInformatica";
        if (disponibilidad != null) {
            disponibilidad = "STOCK";
        }
        else {
            disponibilidad = "NO STOCK";
        }
        return new GPU(nombre, precio, disponibilidad, tienda, web);
    }

    public void setGUI(final GUI g) {
        this.GUI = g;
    }
}