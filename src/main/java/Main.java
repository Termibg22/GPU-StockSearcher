public class Main
{
    public static void main(final String[] args) {
        final PCComponentes pccomp = new PCComponentes();
        final Amazon amazon = new Amazon();
        final UltimaInformatica ui = new UltimaInformatica();
        final GUI gui = new GUI(pccomp, ui, amazon);
        pccomp.setGUI(gui);
        amazon.setGUI(gui);
        ui.setGUI(gui);
    }
}