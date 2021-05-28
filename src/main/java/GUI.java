import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;

class GUI implements ActionListener {
    private PCComponentes PCComponentes;
    private Amazon Amazon;
    private UltimaInformatica UltimaInformatica;
    TelegramBot bot = new TelegramBot();
    private JTextField bot_text = new JTextField();
    private final TableModel_individual model = new TableModel_individual();
    private final Table_individual table_individual;
    private final TableModel_groups model_groups;
    private final Table_groups table_groups;
    private HashMap alarmas;
    private final JFrame frame;
    private final JFrame frame_bot;
    JMenu menu;
    private final JCheckBox check1;
    private final JCheckBox check2;
    private final JCheckBox check3;
    private final JCheckBox check4;
    private int time;
    JFormattedTextField time_io;
    JFormattedTextField alarma_modelo;
    JFormattedTextField alarma_precio;
    private final JCheckBox check5;
    JButton update;
    Color c;
    private JButton telegram;
    private JButton autoupdate;
    private boolean pressed;
    private JLabel status;
    private Timer tm;
    private final TrayIconDemo td;

    public GUI(PCComponentes pc, UltimaInformatica u, Amazon a) {
        this.table_individual = new Table_individual(this.model);
        this.model_groups = new TableModel_groups();
        this.table_groups = new Table_groups(this.model_groups);
        this.alarmas = new HashMap();
        this.frame = new JFrame("STOCK SEARCHER");
        this.frame_bot = new JFrame("TELEGRAM BOT");
        this.menu = new JMenu("Opciones");
        this.check1 = new JCheckBox("PCComponentes individual");
        this.check2 = new JCheckBox("PCComponentes modelos");
        this.check3 = new JCheckBox("UltimaInformatica individual");
        this.check4 = new JCheckBox("Amazon individual");
        this.time = 10;
        this.time_io = new JFormattedTextField(this.time);
        this.alarma_modelo = new JFormattedTextField();
        this.alarma_precio = new JFormattedTextField();
        this.check5 = new JCheckBox("Alarma por stock general");
        this.telegram = new JButton("Bot Telegram");
        this.update = new JButton("Actualizar");
        this.c = this.update.getBackground();
        this.autoupdate = new JButton("Actualizar automáticamente");
        this.pressed = false;
        this.status = new JLabel("");
        this.td = new TrayIconDemo();
        this.PCComponentes = pc;
        this.UltimaInformatica = u;
        this.Amazon = a;
        this.frame.setDefaultCloseOperation(3);
        this.frame.setSize(1280, 720);
        this.frame.setExtendedState(6);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(0);

        int i;
        for(i = 0; i < this.table_individual.getColumnCount(); ++i) {
            this.table_individual.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        for(i = 0; i < this.table_groups.getColumnCount(); ++i) {
            this.table_groups.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JPanel p_principal = new JPanel();
        this.barra_superior(p_principal);
        this.barra_inferior(p_principal);
        JScrollPane sc = new JScrollPane();
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, 1));
        JPanel box = new JPanel();
        box.setBackground(this.c);
        box.setMaximumSize(new Dimension(10000, 10));
        JPanel box2 = new JPanel();
        box2.setBackground(this.c);
        box2.setMaximumSize(new Dimension(10000, 20));
        p.add(box, new BoxLayout(box, 1));
        p.add(this.table_groups, new BoxLayout(this.table_groups, 1));
        p.add(box2, new BoxLayout(box2, 1));
        p.add(this.table_individual, new BoxLayout(this.table_individual, 1));
        sc.setViewportView(p);
        this.frame.getContentPane().add("Center", sc);
        this.frame.setVisible(true);
        this.telegram_bot();
    }

    private void barra_superior(JPanel p_principal) {
        JPanel p_superior = new JPanel();
        JMenuBar mb = new JMenuBar();
        JMenuBar mb3 = new JMenuBar();
        JMenuItem pc_i = new JMenuItem("Añadir fichero con urls individuales PCComponentes");
        JMenuItem pc_g = new JMenuItem("Añadir fichero con urls modelos PCComponentes");
        JMenuItem ui_i = new JMenuItem("Añadir fichero con urls individuales UltimaInformatica");
        JMenuItem a_i = new JMenuItem("Añadir fichero con urls individuales Amazon");
        pc_i.setActionCommand("pc_i");
        pc_g.setActionCommand("pc_g");
        ui_i.setActionCommand("ui_i");
        a_i.setActionCommand("a_i");
        pc_i.addActionListener(this);
        pc_g.addActionListener(this);
        ui_i.addActionListener(this);
        a_i.addActionListener(this);
        JLabel remaining = new JLabel("");
        JLabel t = new JLabel("     Tiempo (minutos):   ");
        this.time_io.setMaximumSize(new Dimension(40, 50));
        JLabel a = new JLabel("ALARMAS            ");
        JLabel a_p = new JLabel("            Alarma personalizada :     ");
        JLabel m = new JLabel("Modelo: ");
        this.alarma_modelo.setMaximumSize(new Dimension(100, 50));
        JLabel p_m = new JLabel("Precio máximo: ");
        this.alarma_precio.setMaximumSize(new Dimension(100, 50));
        JButton alarma_aceptar = new JButton("Añadir");
        JButton alarma_borrar = new JButton("Borrar");
        JButton alarma_ver = new JButton("Ver alarmas");
        alarma_aceptar.setActionCommand("alarma_aceptar");
        alarma_borrar.setActionCommand("alarma_borrar");
        alarma_ver.setActionCommand("alarma_ver");
        alarma_aceptar.addActionListener(this);
        alarma_borrar.addActionListener(this);
        alarma_ver.addActionListener(this);
        this.telegram.setActionCommand("telegram");
        this.update.setActionCommand("Actualizar");
        this.autoupdate.setActionCommand("Auto actualizar");
        this.time_io.setActionCommand("Tiempo");
        this.telegram.addActionListener(this);
        this.update.addActionListener(this);
        this.autoupdate.addActionListener(this);
        this.time_io.addActionListener(this);
        this.menu.add(a_i);
        this.menu.add(pc_i);
        this.menu.add(ui_i);
        this.menu.add(pc_g);
        mb.add(this.menu);
        mb.add(this.telegram);
        mb.add(this.update);
        mb.add(this.autoupdate);
        mb.add(t);
        mb.add(this.time_io);
        mb.add(this.status);
        mb.add(remaining);
        mb3.add(a);
        mb3.add(this.check5);
        mb3.add(a_p);
        mb3.add(m);
        mb3.add(this.alarma_modelo);
        mb3.add(p_m);
        mb3.add(this.alarma_precio);
        mb3.add(alarma_aceptar);
        mb3.add(alarma_borrar);
        mb3.add(alarma_ver);
        p_principal.setLayout(new GridLayout(2, 1));
        p_superior.setLayout(new GridLayout(1, 2));
        p_superior.add(mb);
        p_superior.add(mb3);
        p_principal.add(p_superior);
        this.frame.getContentPane().add("North", p_principal);
    }

    private void barra_inferior(JPanel p_principal) {
        JPanel p_inferior = new JPanel();
        JMenuBar mb2 = new JMenuBar();
        p_inferior.setLayout(new GridLayout(1, 1));
        p_inferior.add(mb2);
        this.check1.setSelected(false);
        this.check2.setSelected(false);
        this.check3.setSelected(false);
        this.check4.setSelected(false);
        mb2.add(this.check2);
        mb2.add(this.check4);
        mb2.add(this.check1);
        mb2.add(this.check3);
        p_inferior.setLayout(new GridLayout(1, 1));
        p_inferior.add(mb2);
        p_principal.add(p_inferior);
    }

    private void telegram_bot() {
        this.frame_bot.setLayout(new BorderLayout());
        this.frame_bot.setSize(500, 220);
        this.frame_bot.setResizable(false);
        JPanel p = new JPanel(new BorderLayout());
        JLabel info = new JLabel("Añada el bot utilizando el siguiente enlace 't.me/gpusearcher_bot'");
        JLabel t = new JLabel("Nombre de usuario de Telegram: ");
        this.bot_text.setMaximumSize(new Dimension(300, 50));
        JButton aceptar = new JButton("Aceptar");
        p.add(t, "North");
        p.add(this.bot_text, "Center");
        this.frame_bot.add(info, "North");
        this.frame_bot.add(p, "Center");
        this.frame_bot.add(aceptar, "South");
        aceptar.setActionCommand("bot_aceptar");
        aceptar.addActionListener(this);
    }

    public void add_gpu(GPU gpu) {
        this.model.visible = true;
        this.model.add(gpu);
        this.table_individual.packAll();
        if (gpu.getDisponibilidad().equals("STOCK") && this.check5.isSelected()) {
            this.td.displayTray(gpu.getTienda() + " " + gpu.getNombre(), " STOCK\r\nweb: " + gpu.getWeb());
            String mensaje = gpu.getTienda() + " " + gpu.getNombre() + "\r\nEN STOCK\r\nweb: " + gpu.getWeb();
            this.bot.notification(mensaje);
        } else {
            Iterator it = this.alarmas.entrySet().iterator();

            while(it.hasNext()) {
                Entry pair = (Entry)it.next();
                if (gpu.getNombre().contains((String)pair.getKey()) && gpu.getPrecio() <= (Double)pair.getValue()) {
                    this.td.displayTray(gpu.getTienda() + " " + gpu.getNombre(), "STOCK");
                    String mensaje = gpu.getTienda() + " " + gpu.getNombre() + "\r\nEN STOCK";
                    this.bot.notification(mensaje);
                }
            }
        }

    }

    public void actualizar_gpu(GPU gpu) {
        for(int i = 0; i < this.model.getColumnCount(); ++i) {
            if (this.model.getValueAt(i, 0).equals(gpu.getNombre())) {
                this.model.setValueAt(gpu.getPrecio(), i, 2);
                this.model.setValueAt(gpu.getDisponibilidad(), i, 4);
                i = this.model.getColumnCount();
            }
        }

        this.table_individual.packAll();
        if (gpu.getDisponibilidad().equals("STOCK") && this.check5.isSelected()) {
            this.td.displayTray(gpu.getTienda() + " " + gpu.getNombre(), "STOCK\r\nweb: " + gpu.getWeb());
            String mensaje = gpu.getTienda() + " " + gpu.getNombre() + "\r\nEN STOCK\r\nweb: " + gpu.getWeb();
            this.bot.notification(mensaje);
        } else {
            Iterator it = this.alarmas.entrySet().iterator();

            while(it.hasNext()) {
                Entry pair = (Entry)it.next();
                if (gpu.getNombre().contains((String)pair.getKey()) && gpu.getPrecio() <= (Double)pair.getValue()) {
                    this.td.displayTray(gpu.getTienda() + " " + gpu.getNombre(), "STOCK\r\nweb: " + gpu.getWeb());
                    String mensaje = gpu.getTienda() + " " + gpu.getNombre() + "\r\nEN STOCK\r\nweb: " + gpu.getWeb();
                    this.bot.notification(mensaje);
                }
            }
        }

    }

    public void add_group(Group group) {
        this.model_groups.visible = true;
        this.model_groups.add(group);
        this.table_groups.packAll();
    }

    public void actualizar_group(Group group) {
        for(int i = 0; i < this.model.getColumnCount(); ++i) {
            if (this.model.getValueAt(i, 0).equals(group.getTipo())) {
                this.model.setValueAt(group.getDisponibilidad(), i, 4);
                i = this.model.getColumnCount();
            }
        }

        this.table_groups.packAll();
    }

    public void actionPerformed(ActionEvent e) {
        String mensaje;
        if (e.getActionCommand().equals("telegram")) {
            mensaje = this.bot_text.getText();
            if(this.bot.get_idfound())
                this.InfoWindow("Ya existe el usuario " + mensaje + ", tenga en cuenta que será reemplazado");
            this.frame_bot.setVisible(true);
        }
        if (e.getActionCommand().equals("bot_aceptar")) {
            mensaje = this.bot_text.getText();

            if (this.bot.add_id(mensaje)) {
                this.InfoWindow("Usuario " + mensaje + " encontrado");
                this.frame_bot.setVisible(false);
            } else {
                this.ErrorGeneral("El usuario no ha sido encontrado");
            }
        } else if (e.getActionCommand().equals("Actualizar") && !this.pressed) {
            if (this.check1.isSelected() || this.check2.isSelected() || this.check3.isSelected() || this.check4.isSelected()) {
                this.setButtonsEnabled(false);
            }

            if (this.check1.isSelected()) {
                this.update_pccomp_i();
            }

            if (this.check2.isSelected()) {
                this.update_pccomp_g();
            }

            if (this.check3.isSelected()) {
                this.update_ui_i();
            }

            if (this.check4.isSelected()) {
                this.update_a_i();
            }

            if (!this.check1.isSelected() && !this.check2.isSelected() && !this.check3.isSelected() && !this.check4.isSelected()) {
                this.ErrorGeneral("Selecciona qué quieres actualizar en las checkbox");
            }
        } else if (e.getActionCommand().equals("Auto actualizar")) {
            if (!this.pressed) {
                if (this.check1.isSelected() || this.check2.isSelected() || this.check3.isSelected() || this.check4.isSelected()) {
                    this.setButtonsEnabled(false);
                    this.autoupdate.setEnabled(true);
                }

                if (this.check1.isSelected()) {
                    this.autoupdate.setBackground(Color.LIGHT_GRAY);
                    this.time_io.setEditable(false);
                    this.time = Integer.parseInt(this.time_io.getText());
                    this.tm = new Timer(this.time * 1000 * 60, (e1) -> {
                        (new Thread(() -> {
                            SwingUtilities.invokeLater(this::autoupdate_pccomp_i);
                        })).start();
                    });
                    this.tm.start();
                    this.autoupdate.getModel().setPressed(true);
                    this.pressed = true;
                }

                if (this.check2.isSelected()) {
                    this.autoupdate.setBackground(Color.LIGHT_GRAY);
                    this.time_io.setEditable(false);
                    this.time = Integer.parseInt(this.time_io.getText());
                    this.tm = new Timer(this.time * 1000 * 60, (e1) -> {
                        (new Thread(() -> {
                            SwingUtilities.invokeLater(this::autoupdate_pccomp_g);
                        })).start();
                    });
                    this.tm.start();
                    this.autoupdate.getModel().setPressed(true);
                    this.pressed = true;
                }

                if (this.check3.isSelected()) {
                    this.autoupdate.setBackground(Color.LIGHT_GRAY);
                    this.time_io.setEditable(false);
                    this.time = Integer.parseInt(this.time_io.getText());
                    this.tm = new Timer(this.time * 1000 * 60, (e1) -> {
                        (new Thread(() -> {
                            SwingUtilities.invokeLater(this::autoupdate_ui_i);
                        })).start();
                    });
                    this.tm.start();
                    this.autoupdate.getModel().setPressed(true);
                    this.pressed = true;
                }

                if (this.check4.isSelected()) {
                    this.autoupdate.setBackground(Color.LIGHT_GRAY);
                    this.time_io.setEditable(false);
                    this.time = Integer.parseInt(this.time_io.getText());
                    this.tm = new Timer(this.time * 1000 * 60, (e1) -> {
                        (new Thread(() -> {
                            SwingUtilities.invokeLater(this::autoupdate_a_i);
                        })).start();
                    });
                    this.tm.start();
                    this.autoupdate.getModel().setPressed(true);
                    this.pressed = true;
                }

                if (!this.check1.isSelected() && !this.check2.isSelected() && !this.check3.isSelected() && !this.check4.isSelected()) {
                    this.ErrorGeneral("Selecciona qué quieres actualizar en las checkbox");
                }
            } else {
                this.autoupdate.setBackground(this.c);
                this.time_io.setEditable(true);
                this.tm.stop();
                this.pressed = false;
                this.setButtonsEnabled(true);
            }
        } else if (e.getActionCommand().equals("Tiempo")) {
            this.time = Integer.parseInt(this.time_io.getText());
        } else if (e.getActionCommand().equals("pc_i")) {
            mensaje = this.FileChooserPath();
            if (mensaje != null) {
                this.PCComponentes.URLs_individual = mensaje;
            }
        } else if (e.getActionCommand().equals("pc_g")) {
            mensaje = this.FileChooserPath();
            if (mensaje != null) {
                this.PCComponentes.URLs_groups = mensaje;
            }
        } else if (e.getActionCommand().equals("ui_i")) {
            mensaje = this.FileChooserPath();
            if (mensaje != null) {
                this.UltimaInformatica.URLs_individual = mensaje;
            }
        } else if (e.getActionCommand().equals("a_i")) {
            mensaje = this.FileChooserPath();
            if (mensaje != null) {
                this.Amazon.URLs_individual = mensaje;
            }
        } else if (e.getActionCommand().equals("alarma_aceptar")) {
            mensaje = this.alarma_modelo.getText();
            String precio = this.alarma_precio.getText();
            if (mensaje.equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "Debe escribir el modelo");
            } else {
                try {
                    this.alarmas.put(mensaje, Double.parseDouble(precio));
                    JOptionPane.showMessageDialog(new JFrame(), "Alarma añadida");
                } catch (NumberFormatException var5) {
                    JOptionPane.showMessageDialog(new JFrame(), "Debe escribir el precio correctamente");
                }
            }
        } else if (e.getActionCommand().equals("alarma_borrar")) {
            mensaje = this.alarma_modelo.getText();
            if (mensaje.equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "Debe escribir el modelo");
            } else if (this.alarmas.remove(mensaje) == null) {
                JOptionPane.showMessageDialog(new JFrame(), "No existía ninguna alarma con ese modelo");
            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Alarma borrada");
            }
        } else if (e.getActionCommand().equals("alarma_ver")) {
            mensaje = "";

            Entry pair;
            for(Iterator it = this.alarmas.entrySet().iterator(); it.hasNext(); mensaje = mensaje + "Modelo: " + pair.getKey() + " -> Precio: " + pair.getValue() + "€\n") {
                pair = (Entry)it.next();
            }

            if (mensaje.equals("")) {
                JOptionPane.showMessageDialog(new JFrame(), "No existen alarmas");
            } else {
                JOptionPane.showMessageDialog(new JFrame(), mensaje);
            }
        }

    }

    private void update_pccomp_i() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.PCComponentes.individual();
                this.status.setText("Actualizado");
                this.setButtonsEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    private void update_pccomp_g() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.PCComponentes.groups();
                this.status.setText("Actualizado");
                this.setButtonsEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    private void update_ui_i() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.UltimaInformatica.individual();
                this.status.setText("Actualizado");
                this.setButtonsEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    private void update_a_i() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.Amazon.individual();
                this.status.setText("Actualizado");
                this.setButtonsEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    private void autoupdate_pccomp_i() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.PCComponentes.individual();
                this.status.setText("Actualizado");
                this.autoupdate.setEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    private void autoupdate_pccomp_g() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.PCComponentes.groups();
                this.status.setText("Actualizado");
                this.autoupdate.setEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    private void autoupdate_ui_i() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.UltimaInformatica.individual();
                this.status.setText("Actualizado");
                this.autoupdate.setEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    private void autoupdate_a_i() {
        this.status.setText("Actualizando...");
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                this.Amazon.individual();
                this.status.setText("Actualizado");
                this.autoupdate.setEnabled(true);
            });
        })).start();
        (new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    var2.printStackTrace();
                }

                this.status.setText("");
            });
        })).start();
    }

    public void Filenotfound(String file) {
        JOptionPane.showMessageDialog(new JFrame(), "File " + file + " not found", "File not found", 0);
    }

    public void ErrorAccessingWeb(String web) {
        JOptionPane.showMessageDialog(new JFrame(), "Error accessing " + web, "Error accessing web", 0);
    }

    public void InfoWindow(String info) {
        JOptionPane.showMessageDialog(new JFrame(), info, "Telegram bot", 1);
    }

    public void ErrorGeneral(String error) {
        JOptionPane.showMessageDialog(new JFrame(), "Error: " + error, "Error", 0);
    }

    private String FileChooserPath() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select file");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt file", new String[]{"txt"});
        jfc.addChoosableFileFilter(filter);
        int returnValue = jfc.showOpenDialog((Component)null);
        return returnValue == 0 ? (this.PCComponentes.URLs_individual = jfc.getSelectedFile().getPath()) : null;
    }

    private void setButtonsEnabled(boolean enable) {
        this.menu.setEnabled(enable);
        this.update.setEnabled(enable);
        this.autoupdate.setEnabled(enable);
        this.check1.setEnabled(enable);
        this.check2.setEnabled(enable);
        this.check3.setEnabled(enable);
        this.check4.setEnabled(enable);
    }
}
