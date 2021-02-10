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

package bulsort.sorter.options;

import bulsort.sorter.Settings;
import icofont.IcoMoon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Sicut
 */
public class GeneralOptionsController {

    @FXML private CheckBox prepend, merge, open, useStamp;
    @FXML private Label alert;
    @FXML private ComboBox lang;
    @FXML private Button importStamp, removeStamp;
    @FXML private ImageView stamp;
    @FXML private TextField stampPath;
    @FXML private Spinner stampX, stampY, stampH, stampW;
    
    private boolean ini_prepend, ini_merge, ini_open, ini_use_stamp;
    private Integer ini_stamp_x, ini_stamp_y, ini_stamp_h, ini_stamp_w;
    private int ini_lang;
    private final FileChooser fc;
    private final Image dummy;
    private final IntegerSpinnerValueFactory isvfx, isvfy, isvfh, isvfw;
    private final StringConverter conv;

    public GeneralOptionsController() {
        
        fc = new FileChooser();
        fc.setTitle(Settings.I18N_BUNDLE.getString("CHOOSE_STAMP"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(Settings.I18N_BUNDLE.getString("IMAGES"), "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.tiff"));
        dummy = new Image(getClass().getResourceAsStream("/bulsort/resources/images/empty-stamp.jpg"));
        conv = new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object + "";
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }
        };
        isvfx = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 8);
        isvfx.setConverter(conv);
        isvfy = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 8);
        isvfy.setConverter(conv);
        isvfh = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 8);
        isvfh.setConverter(conv);
        isvfw = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 8);
        isvfw.setConverter(conv);
    }
    
    public void initialize() {
        ini_prepend = Settings.PREF_BUNDLE.get("PREPEND_TITLE_PAGE").equals("Y");
        prepend.setSelected(ini_prepend);
        ini_merge = Settings.PREF_BUNDLE.get("MERGE_BULLETINS").equals("Y");
        merge.setSelected(ini_merge);
        ini_open = Settings.PREF_BUNDLE.get("OPEN_DEST_FOLDER").equals("Y");
        open.setSelected(ini_open);
        ini_use_stamp = Settings.PREF_BUNDLE.get("USE_STAMP").equals("Y");
        useStamp.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            disableStampSection(oldValue);
        });
        useStamp.setSelected(ini_use_stamp);
        disableStampSection(!ini_use_stamp);
        try {
            ini_stamp_x = Integer.parseInt(Settings.PREF_BUNDLE.get("STAMP_X"));
        } catch( NumberFormatException nfe ) {
            ini_stamp_x = 0;
        }
        try {
            ini_stamp_y = Integer.parseInt(Settings.PREF_BUNDLE.get("STAMP_Y"));
        } catch( NumberFormatException nfe ) {
            ini_stamp_y = 0;
        }
        try {
            ini_stamp_h = Integer.parseInt(Settings.PREF_BUNDLE.get("STAMP_H"));
        } catch( NumberFormatException nfe ) {
            ini_stamp_h = 0;
        }
        try {
            ini_stamp_w = Integer.parseInt(Settings.PREF_BUNDLE.get("STAMP_W"));
        } catch( NumberFormatException nfe ) {
            ini_stamp_w = 0;
        }
        
        stampH.setValueFactory(isvfh);
        stampW.setValueFactory(isvfw);
        stampY.setValueFactory(isvfy);
        stampX.setValueFactory(isvfx);
        
        stampX.getEditor().setOnAction(action -> {
            handle(isvfx, stampX);
        });
        stampY.getEditor().setOnAction(action -> {
            handle(isvfy, stampY);
        });
        stampH.getEditor().setOnAction(action -> {
            handle(isvfh, stampH);
        });
        stampW.getEditor().setOnAction(action -> {
            handle(isvfw, stampW);
        });
        
        isvfx.setValue(ini_stamp_x);
        isvfy.setValue(ini_stamp_y);
        isvfw.setValue(ini_stamp_w);
        isvfh.setValue(ini_stamp_h);
        
        alert.setFont(Settings.icoFont.getFont(14));
        alert.setText(IcoMoon.warning);
        
        lang.getItems().add(Settings.I18N_BUNDLE.getString(Settings.SUPPORTED_LANGS.get(0)));
        lang.getItems().add(Settings.I18N_BUNDLE.getString(Settings.SUPPORTED_LANGS.get(1)));
        lang.getItems().add(Settings.I18N_BUNDLE.getString(Settings.SUPPORTED_LANGS.get(2)));
        ini_lang = Settings.SUPPORTED_LANGS.indexOf(Settings.PREF_BUNDLE.get("LANGUAGE"));
        lang.getSelectionModel().select(ini_lang);
        stamp.setImage(dummy);
        setStamp(new File(Settings.PREF_BUNDLE.get("STAMP")));
        
        importStamp.setFont(Settings.icoFont.getFont(12));
        importStamp.setText(IcoMoon.link);
        importStamp.setOnAction(evt -> {
            File ini_stamp_dir = new File(Settings.PREF_BUNDLE.get("STAMP_DIR"));
            if ( ini_stamp_dir.exists() && ini_stamp_dir.isDirectory() )
                fc.setInitialDirectory(ini_stamp_dir);
            File f = fc.showOpenDialog(importStamp.getScene().getWindow());
            if ( f == null )
                return;                
            setStamp(f);
            try {
                Image img = new Image(new FileInputStream(f));
                isvfw.setValue((new Double(img.getWidth())).intValue());
                isvfh.setValue((new Double(img.getHeight())).intValue());
            } catch (FileNotFoundException ex) { }
        });
        
        removeStamp.setFont(Settings.icoFont.getFont(12));
        removeStamp.setText(IcoMoon.cancel);
        removeStamp.setOnAction(evt -> {
            stampPath.setText("");
            Settings.PREF_BUNDLE.update("STAMP", "");
            stamp.setImage(dummy);
        });
    }
    
    public void saveOnClose() {
        if ( ini_prepend != prepend.isSelected() ) 
            Settings.PREF_BUNDLE.update( "PREPEND_TITLE_PAGE", prepend.isSelected() ? "Y" : "N" );
        if ( ini_merge != merge.isSelected() ) 
            Settings.PREF_BUNDLE.update( "MERGE_BULLETINS", merge.isSelected() ? "Y" : "N" );
        if ( ini_open != open.isSelected() ) 
            Settings.PREF_BUNDLE.update( "OPEN_DEST_FOLDER", open.isSelected() ? "Y" : "N" );
        if ( ini_use_stamp != useStamp.isSelected() ) 
            Settings.PREF_BUNDLE.update( "USE_STAMP", useStamp.isSelected() ? "Y" : "N" );
        if ( !ini_stamp_x.equals(isvfx.getValue()) )
            Settings.PREF_BUNDLE.update( "STAMP_X", isvfx.getValue().toString() );
        if ( !ini_stamp_y.equals(isvfy.getValue()) )
            Settings.PREF_BUNDLE.update( "STAMP_Y", isvfy.getValue().toString() );
        if ( !ini_stamp_w.equals(isvfw.getValue()) )
            Settings.PREF_BUNDLE.update( "STAMP_W", isvfw.getValue().toString() );
        if ( !ini_stamp_h.equals(isvfh.getValue()) )
            Settings.PREF_BUNDLE.update( "STAMP_H", isvfh.getValue().toString() );
        if ( ini_lang != lang.getSelectionModel().getSelectedIndex() ) 
            Settings.PREF_BUNDLE.update( "LANGUAGE", Settings.SUPPORTED_LANGS.get(lang.getSelectionModel().getSelectedIndex()) );
    }
    
    private void setStamp(File f) {
        if ( f.exists() ) {
            try {
                String path = f.getPath();
                stamp.setImage(new Image(new FileInputStream(f)));
                Settings.PREF_BUNDLE.update("STAMP_DIR", f.getParent());
                Settings.PREF_BUNDLE.update("STAMP", path);
                stampPath.setText(path);
            } catch( FileNotFoundException exc ) { }
        }
    }
    
    private void disableStampSection(Boolean disable){
        stampX.setDisable(disable);
        stampY.setDisable(disable);
        stampH.setDisable(disable);
        stampW.setDisable(disable);
        importStamp.setDisable(disable);
        removeStamp.setDisable(disable);
        stampPath.setDisable(disable);
    }
    
    public void handle(IntegerSpinnerValueFactory svf, Spinner s) {
        String text = s.getEditor().getText();
        Integer enterValue = svf.getConverter().fromString(text);
        if ( svf.getMax() < enterValue ) {
            svf.setValue(svf.getMax());
        }
        else if ( svf.getMin() > enterValue ) {
            svf.setValue(svf.getMin());
        }
        else {
            svf.setValue(enterValue);
        }
    }
}
