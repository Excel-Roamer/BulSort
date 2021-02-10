/*
 * Copyright (C) 2020 Sicut
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package bulsort.sorter;

import bulsort.group.Container;
import bulsort.group.Group;
import bulsort.in.massar.PV;
import bulsort.sorter.about.About;
import bulsort.sorter.options.AdditionalOptions;
import icofont.IcoMoon;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.function.Function;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sicut.util.PDFDocument;

/**
 *
 * @author Sicut
 */
public class MainController {
    
    @FXML private Button addBuls, sort;
    @FXML private Label genInfos, sortIcon, addBulsIcon;
    @FXML private ProgressIndicator loading;
    @FXML private TitledPane titP1A, titP2A, titP3A, titPTC, titP1B, titP2B;
    @FXML private TilePane lst1A, lst2A, lst3A, lstTC, lst1B, lst2B;
    @FXML private FlowPane overlay;
    @FXML private MenuItem quit, addBulMenu, addPVMenu, resetMenu, newDBMenu, destMenu, supMenu, about;
    @FXML private CheckMenuItem delMenu, sortMenu;
    @FXML private RadioMenuItem assDupMenu, disDupMenu, noDupMenu;
    
    private final HashMap<String, LevelPane> subList;
    private final HashMap<String, Container> grpsContainers;
    private final FileChooser fc;
    private final DirectoryChooser dc;
    
    private final SimpleStringProperty bulsLoadingThreads;
    private short launchedThreads;
    private final Function<Boolean, Void> showSpinner;
    private AdditionalOptions ao;

    public MainController() {
        subList = new HashMap();
        grpsContainers = new HashMap();
        fc = new FileChooser();
        dc = new DirectoryChooser();
        bulsLoadingThreads = new SimpleStringProperty("");
        ao = null;
        showSpinner = show -> {
            FadeTransition ft = new FadeTransition(Duration.millis(1000), loading);
            FadeTransition ft1 = new FadeTransition(Duration.millis(1000), overlay);
            if ( show ) {
                loading.setOpacity(0);
                loading.setVisible(true);
                overlay.setOpacity(0);
                overlay.setVisible(true);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft1.setFromValue(0);
                ft1.setToValue(0.7);
            }
            else {
                ft.setFromValue(1);
                ft.setToValue(0);
                ft1.setFromValue(0.7);
                ft1.setToValue(0);
                loading.setVisible(false);
                overlay.setVisible(false);
            }
            ft1.play();
            ft.play();
            return null;
        };
    }
    
    public void initialize() {
        
        EventHandler addBulsEvent = (EventHandler<ActionEvent>) (ActionEvent e) -> {
            File ini_bul_dir = new File(Settings.PREF_BUNDLE.get("PDF_DIR"));
            fc.setTitle(Settings.I18N_BUNDLE.getString("CHOOSE_BULS"));
            if ( ini_bul_dir.exists() && ini_bul_dir.isDirectory() )
                fc.setInitialDirectory(ini_bul_dir);
            fc.getExtensionFilters().clear();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(Settings.I18N_BUNDLE.getString("PDF_FILTER"), "*.pdf"));
            List<File> f = fc.showOpenMultipleDialog(addBuls.getScene().getWindow());
            if ( f == null )
                return;
            if ( f.get(0).getParentFile() != ini_bul_dir )
                Settings.PREF_BUNDLE.update("PDF_DIR", f.get(0).getParent());
            showSpinner.apply(true);
            if ( f.size() < 3 ) {
                launchedThreads = 1;
                bulsLoadingThreads.set("");
                new Thread(new BulletinsLoader(f, grpsContainers, bulsLoadingThreads)).start();
            }
            else {
                int n = Math.round(f.size() / 3);
                launchedThreads = 3;
                bulsLoadingThreads.set("");
                new Thread(new BulletinsLoader(f.subList(0, n), grpsContainers, bulsLoadingThreads)).start();
                new Thread(new BulletinsLoader(f.subList(n, 2 * n), grpsContainers, bulsLoadingThreads)).start();
                new Thread(new BulletinsLoader(f.subList(2 * n, f.size()), grpsContainers, bulsLoadingThreads)).start();
            }
        };
        
        subList.put("1A", new LevelPane(titP1A, lst1A));
        subList.put("2A", new LevelPane(titP2A, lst2A));
        subList.put("3A", new LevelPane(titP3A, lst3A));
        subList.put("TC", new LevelPane(titPTC, lstTC));
        subList.put("1B", new LevelPane(titP1B, lst1B));
        subList.put("2B", new LevelPane(titP2B, lst2B));
        
        quit.setOnAction(e -> {
            Platform.exit();
        });
        newDBMenu.setOnAction(e -> {
            ((SimpleStringProperty) sort.getScene().getUserData()).set("SELECT_ANOTHER_DB");
        });
        resetMenu.setOnAction(e -> {
            showSpinner.apply(true);
            Settings.SCHOOL_DB.getGroups().forEach(item -> {
                item.setPdfFile(null);
                item.removePv();
                grpsContainers.get(item.getName()).setHasPDF(false);
                grpsContainers.get(item.getName()).setHasPV(false);
            });
            showSpinner.apply(false);
        });
        destMenu.setOnAction(e -> {
            File ini_dest = new File(Settings.PREF_BUNDLE.get("DEST_DIR"));
            dc.setTitle(Settings.I18N_BUNDLE.getString("CHOOSE_DEST"));
            if ( ini_dest.exists() )
                dc.setInitialDirectory(ini_dest);
            File f = dc.showDialog(addBuls.getScene().getWindow());
            if ( f == null || f.equals(ini_dest) )
                return;
            Settings.PREF_BUNDLE.update("DEST_DIR", f.getAbsolutePath());
        });
        sortMenu.setSelected(Settings.PREF_BUNDLE.get("SORT_BULLETINS").equals("Y"));
        sortMenu.setOnAction(e -> {
            Settings.PREF_BUNDLE.update("SORT_BULLETINS", sortMenu.isSelected() ? "Y" : "N");
        });
        try {
            switch ( Integer.parseInt(Settings.PREF_BUNDLE.get("DUPLICATE_PAGES")) ) {
                case 1:
                    disDupMenu.setSelected(true);
                    break;
                case 2:
                    assDupMenu.setSelected(true);
                    break;
                default:
                    noDupMenu.setSelected(true);
            }
        } catch ( NumberFormatException nfe ) {
            noDupMenu.setSelected(true);
        }
        disDupMenu.setOnAction(e -> {
            Settings.PREF_BUNDLE.update("DUPLICATE_PAGES", "1");
        });
        assDupMenu.setOnAction(e -> {
            Settings.PREF_BUNDLE.update("DUPLICATE_PAGES", "2");
        });
        noDupMenu.setOnAction(e -> {
            Settings.PREF_BUNDLE.update("DUPLICATE_PAGES", "0");
        });
        delMenu.setSelected(Settings.PREF_BUNDLE.get("DEL_WATERMARK").equals("Y"));
        delMenu.setOnAction(e -> {
            Settings.PREF_BUNDLE.update("DEL_WATERMARK", delMenu.isSelected() ? "Y" : "N");
        });
        supMenu.setOnAction(e -> {
            if ( ao == null )
                ao = new AdditionalOptions((Stage) addBuls.getScene().getWindow());
            ao.show();
        });
        addPVMenu.setOnAction(e -> {
            File ini_pv_dir = new File(Settings.PREF_BUNDLE.get("XL_DIR"));
            fc.setTitle(Settings.I18N_BUNDLE.getString("CHOOSE_PVS"));
            if ( ini_pv_dir.exists() && ini_pv_dir.isDirectory() )
                fc.setInitialDirectory(ini_pv_dir);
            fc.getExtensionFilters().clear();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(Settings.I18N_BUNDLE.getString("XLX_FILTER"), "*.xlsx"));
            List<File> f = fc.showOpenMultipleDialog(addBuls.getScene().getWindow());
            if ( f == null )
                return;
            if ( f.get(0).getParentFile() != ini_pv_dir )
                Settings.PREF_BUNDLE.update("XL_DIR", f.get(0).getParent());
            
            showSpinner.apply(true);
            new Thread(() -> {
                f.forEach((item) -> {
                    PV pv = new PV(item);
                    if ( pv.isValid() ) {
                        String g = pv.getGroup();
                        Group grp = Settings.SCHOOL_DB.getGroup(g);
                        if ( grp != null ) {
                            grp.loadPv(pv);                           
                            grpsContainers.get(g).setPV(Settings.I18N_BUNDLE.getString("GROUP") + g + ".\n" +
                                             Settings.I18N_BUNDLE.getString("STU_COUNT") + pv.getStudentsCount() + ".\n" +
                                             Settings.I18N_BUNDLE.getString("LEVEL") + pv.getLevel() + ".\n" +
                                             Settings.I18N_BUNDLE.getString("SCHOOL") + pv.getSchool() + ".\n" +
                                             Settings.I18N_BUNDLE.getString("YEAR") + pv.getYear() + ".\n" +
                                             Settings.I18N_BUNDLE.getString("DIRECTION") + pv.getDirection() + ".\n" +
                                             Settings.I18N_BUNDLE.getString("ACADEMY") + pv.getAcademy() + ".\n" +
                                             Settings.I18N_BUNDLE.getString("PV_PATH") + item.getAbsolutePath());
                        }
                    }
                });
                showSpinner.apply(false);
            }).start();
        });
        addBulMenu.setOnAction(addBulsEvent);
        addBuls.setOnAction(addBulsEvent);
        bulsLoadingThreads.addListener((ChangeListener<String>) (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if ( newValue.matches("(Done){" + launchedThreads + "}") )
                showSpinner.apply(false);
        });
        sort.setOnAction(e -> {
            showSpinner.apply(true);
            sort.setDisable(true);
            new Thread(() -> {
                String dest = Settings.PREF_BUNDLE.get("DEST_DIR");
                if ( Settings.PREF_BUNDLE.get("MERGE_BULLETINS").equals("Y") ) {
                    PDFDocument out = new PDFDocument();
                    Boolean empty = true;
                    String aKey = "";
                    Iterator<String> keys = grpsContainers.keySet().stream().sorted().iterator();
                    out.create();
                    while ( keys.hasNext() ) {
                        String key = keys.next();
                        if ( Settings.SCHOOL_DB.getGroup(key).reloadBulletin() ) {
                            Settings.SCHOOL_DB.getGroup(key).generate(out);
                            empty = false;
                            aKey = key;
                        }
                    }
                    if ( !empty ) {
                        dest = ( ( new File(dest) ).exists() ? dest : Settings.SCHOOL_DB.getGroup(aKey).getPdfFile().getLocation() );
                        out.save( dest  + "/Merged_Buls(" + (new SimpleDateFormat(Settings.PREF_BUNDLE.get("DATE_FORMAT_OUTPUT"))).format(new Date()) + ").pdf" );
                        grpsContainers.keySet().forEach(key -> {
                            Settings.SCHOOL_DB.getGroup(key).closeBulletin();
                        });
                    }
                    out.close();
                }
                else {
                    grpsContainers.keySet().forEach(key -> {
                        Settings.SCHOOL_DB.getGroup(key).generate();
                    });                
                }
                if ( Settings.PREF_BUNDLE.get("OPEN_DEST_FOLDER").equals("Y") ) {
                    try {
                        Desktop.getDesktop().open(new File(dest));
                    } catch(IOException ioe) {
                        System.out.println(ioe);
                    }
                }
                sort.setDisable(false);
                showSpinner.apply(false);
            }).start();
        });
        
        about.setOnAction(evt -> {
            (new About((Stage) sort.getScene().getWindow())).show();
        });
        
        Font fnt = (new IcoMoon(14)).getFont();
        
        sortIcon.setFont(fnt);
        sortIcon.setText(IcoMoon.arrange3);
        addBulsIcon.setFont(fnt);
        addBulsIcon.setText(IcoMoon.file10);
        load();
    }
    
    private void load() {
        CustomTask ct = new CustomTask(showSpinner, subList, grpsContainers);
        Timer tmr = new Timer();
        genInfos.setText(Settings.SCHOOL_DB.getSchool() + " | " + Settings.SCHOOL_DB.getYear());
        showSpinner.apply(true);
        
        tmr.scheduleAtFixedRate(ct, 0, 100);
    }
    
}


