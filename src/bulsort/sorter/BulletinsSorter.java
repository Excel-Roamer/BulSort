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

import bulsort.school.db.DbChooser;
import bulsort.school.db.SchoolDB;
import java.util.Locale;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sicut.db.Configurator;
import sicut.db.Preferences;
import sicut.db.Translator;

/**
 *
 * @author Sicut
 */
public class BulletinsSorter extends Application {
    
    private final SimpleStringProperty command;
    private DbChooser dbc;
    private MainFrame pWindow;
    private Stage stage;

    public BulletinsSorter() {
        command = new SimpleStringProperty();
        command.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            switch(newValue) {
                case "DB_LOADED":
                    if ( pWindow == null ) {
                        pWindow = new MainFrame();
                        pWindow.getScene().setUserData(command);
                    }
                    showMainFrame();
                    break;
                case "SELECT_ANOTHER_DB":
                    showDbChooser();
                    break;
            }
        });
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        (new Configurator()).prepare();
        Settings.PREF_BUNDLE = new Preferences();
        Settings.I18N_BUNDLE = new Translator();
        Settings.SCHOOL_DB = new SchoolDB();
        Locale.setDefault(new Locale(Settings.PREF_BUNDLE.get("LANGUAGE")));
        
        dbc = new DbChooser();
        pWindow = null;
        dbc.getScene().setUserData(command);
        this.stage = stage;
        stage.setTitle(Settings.APP_TITLE);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/bulsort/resources/images/256x256.png")));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/bulsort/resources/images/128x128.png")));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/bulsort/resources/images/64x64.png")));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/bulsort/resources/images/48x48.png")));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/bulsort/resources/images/32x32.png")));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/bulsort/resources/images/16x16.png")));
        stage.setOnCloseRequest(evt -> {
            Settings.PREF_BUNDLE.commit();
        });
        showDbChooser();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void showMainFrame() {
        stage.hide();
        stage.setScene(pWindow.getScene());
        stage.setResizable(pWindow.isResizable());
        stage.setMaximized(pWindow.isMaximized());
        stage.show();
    }
    
    private void showDbChooser() {
        stage.hide();
        stage.setScene(dbc.getScene());
        stage.setResizable(dbc.isResizable());
        stage.setMaximized(dbc.isMaximized());
        stage.show();
    }
    
}
