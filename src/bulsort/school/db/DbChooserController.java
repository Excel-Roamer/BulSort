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

package bulsort.school.db;

import bulsort.in.massar.Db;
import bulsort.sorter.Settings;
import icofont.IcoMoon;
import java.io.File;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Sicut
 */
public class DbChooserController {

    @FXML private Button choose;
    @FXML private Label chooseDbIcon, appName;
    @FXML private ProgressIndicator loading;
    private final FileChooser fc;
    private final Db mw;
    
    public DbChooserController() {
        fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 2003", "*.xls"));
        mw = new Db();
    }
        
    public void initialize() {
        fc.setTitle(Settings.I18N_BUNDLE.getString("CHOOSE_DB_TITLE"));
        choose.setOnAction( e -> {
            File ini_db_dir = new File(Settings.PREF_BUNDLE.get("DB_DIR"));
            if ( ini_db_dir.exists() && ini_db_dir.isDirectory() )
                fc.setInitialDirectory(ini_db_dir);
            File f = fc.showOpenDialog(choose.getScene().getWindow());
            if ( f == null )
                return;
            if ( f.getParentFile() != ini_db_dir )
                Settings.PREF_BUNDLE.update("DB_DIR", f.getParent());
            
            FadeTransition ft = new FadeTransition(Duration.millis(1000), choose);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.play();
            FadeTransition ft2 = new FadeTransition(Duration.millis(1000), loading);
            ft2.setFromValue(0);
            ft2.setToValue(1);
            ft2.play();
            new Thread(() -> {
                if ( !mw.setWorkbook(f) ) {
                    ft.stop();
                    ft2.stop();
                    loading.setOpacity(0);
                    choose.setOpacity(1);
                    return;
                }
                mw.loadDB();
                mw.getStatus().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if ( newValue ) {
                        Platform.runLater(() -> {
                            ((SimpleStringProperty) choose.getScene().getUserData()).set("DB_LOADED");
                            ft.stop();
                            ft2.stop();
                            loading.setOpacity(0);
                            choose.setOpacity(1);
                        });
                    }
                });
            }).start();
        });
        
        Font fnt = (new IcoMoon(14)).getFont();
        
        chooseDbIcon.setFont(fnt);
        chooseDbIcon.setText(IcoMoon.file13);
        
        appName.setText(Settings.APP_TITLE);
    }
    
}
