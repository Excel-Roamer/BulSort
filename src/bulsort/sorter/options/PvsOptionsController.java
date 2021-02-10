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
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Sicut
 */
public class PvsOptionsController {

    @FXML private CheckBox dispPvObs;
    @FXML private ComboBox usedMark;
    @FXML private TextArea f0t5F, f0t5M, f5t8F, f5t8M, f8t10F, f8t10M, f10t12F, f10t12M, f12t14F, f12t14M;
    @FXML private TextArea f14t16F, f14t16M, f16t18F, f16t18M, f18t20F, f18t20M;
    @FXML private Spinner pvObsX, pvObsY, fntSize;
    
    private boolean ini_disp_pv_obs;
    private Integer ini_pv_obs_x, ini_pv_obs_y, ini_font_size, ini_used_mark;
    private String ini_f0t5F, ini_f0t5M, ini_f5t8F, ini_f5t8M, ini_f8t10F, ini_f8t10M, ini_f10t12F, ini_f10t12M, ini_f12t14F, ini_f12t14M;
    private String ini_f14t16F, ini_f14t16M, ini_f16t18F, ini_f16t18M, ini_f18t20F, ini_f18t20M;
    private final IntegerSpinnerValueFactory isvfx, isvfy, isvffs;

    public PvsOptionsController() {
        isvfx = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 8);
        isvfx.setConverter(new CustomStringConverter());
        isvfy = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 8);
        isvfy.setConverter(new CustomStringConverter());
        isvffs = new SpinnerValueFactory.IntegerSpinnerValueFactory(14, 1000, 1);
        isvffs.setConverter(new CustomStringConverter());
    }
    
    public void initialize() {
        ini_disp_pv_obs = Settings.PREF_BUNDLE.get("DISPLAY_PVS_OBSERVATIONS").equals("Y");
        dispPvObs.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            disablePvSection(oldValue);
        });
        dispPvObs.setSelected(ini_disp_pv_obs);
        disablePvSection(!ini_disp_pv_obs);
        try {
            ini_pv_obs_x = Integer.parseInt(Settings.PREF_BUNDLE.get("OBS_X"));
        } catch( NumberFormatException nfe ) {
            ini_pv_obs_x = 0;
        }
        try {
            ini_pv_obs_y = Integer.parseInt(Settings.PREF_BUNDLE.get("OBS_Y"));
        } catch( NumberFormatException nfe ) {
            ini_pv_obs_y = 0;
        }
        try {
            ini_font_size = Integer.parseInt(Settings.PREF_BUNDLE.get("FONT_SIZE"));
        } catch( NumberFormatException nfe ) {
            ini_font_size = 0;
        }
        
        pvObsY.setValueFactory(isvfy);
        pvObsX.setValueFactory(isvfx);
        fntSize.setValueFactory(isvffs);
        
        pvObsX.getEditor().setOnAction(action -> {
            handle(isvfx, pvObsX);
        });
        pvObsX.getEditor().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if ( oldValue )
                handle(isvfx, pvObsX);
        });
        pvObsY.getEditor().setOnAction(action -> {
            handle(isvfy, pvObsY);
        });
        pvObsY.getEditor().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if ( oldValue )
                handle(isvfy, pvObsY);
        });
        fntSize.getEditor().setOnAction(action -> {
            handle(isvffs, fntSize);
        });
        fntSize.getEditor().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if ( oldValue )
                handle(isvffs, fntSize);
        });
        
        isvfx.setValue(ini_pv_obs_x);
        isvfy.setValue(ini_pv_obs_y);
        isvffs.setValue(ini_font_size);
        
        usedMark.getItems().add(Settings.I18N_BUNDLE.getString("LAST_MARK"));
        usedMark.getItems().add(Settings.I18N_BUNDLE.getString("S1_MARK"));
        usedMark.getItems().add(Settings.I18N_BUNDLE.getString("S2_MARK"));
        usedMark.getItems().add(Settings.I18N_BUNDLE.getString("AVERAGE_MARK"));
        try {
            ini_used_mark = Integer.parseInt(Settings.PREF_BUNDLE.get("USED_MARK"));
        } catch( NumberFormatException nfe ) {
            ini_used_mark = 0;
        }
        usedMark.getSelectionModel().select(ini_used_mark.intValue());
        
        ini_f0t5F = Settings.PREF_BUNDLE.get("0_TO_5_FEMALE");
        ini_f0t5M = Settings.PREF_BUNDLE.get("0_TO_5_MALE");
        ini_f5t8F = Settings.PREF_BUNDLE.get("5_TO_8_FEMALE");
        ini_f5t8M = Settings.PREF_BUNDLE.get("5_TO_8_MALE");
        ini_f8t10F = Settings.PREF_BUNDLE.get("8_TO_10_FEMALE");
        ini_f8t10M = Settings.PREF_BUNDLE.get("8_TO_10_MALE");
        ini_f10t12F = Settings.PREF_BUNDLE.get("10_TO_12_FEMALE");
        ini_f10t12M = Settings.PREF_BUNDLE.get("10_TO_12_MALE");
        ini_f12t14F = Settings.PREF_BUNDLE.get("12_TO_14_FEMALE");
        ini_f12t14M = Settings.PREF_BUNDLE.get("12_TO_14_MALE");
        ini_f14t16F = Settings.PREF_BUNDLE.get("14_TO_16_FEMALE");
        ini_f14t16M = Settings.PREF_BUNDLE.get("14_TO_16_MALE");
        ini_f16t18F = Settings.PREF_BUNDLE.get("16_TO_18_FEMALE");
        ini_f16t18M = Settings.PREF_BUNDLE.get("16_TO_18_MALE");
        ini_f18t20F = Settings.PREF_BUNDLE.get("18_TO_20_FEMALE");
        ini_f18t20M = Settings.PREF_BUNDLE.get("18_TO_20_MALE");
        f0t5F.setText(ini_f0t5F);
        f0t5M.setText(ini_f0t5M);
        f5t8F.setText(ini_f5t8F);
        f5t8M.setText(ini_f5t8M);
        f8t10F.setText(ini_f8t10F);
        f8t10M.setText(ini_f8t10M);
        f10t12F.setText(ini_f10t12F);
        f10t12M.setText(ini_f10t12M);
        f12t14F.setText(ini_f12t14F);
        f12t14M.setText(ini_f12t14M);
        f14t16F.setText(ini_f14t16F);
        f14t16M.setText(ini_f14t16M);
        f16t18F.setText(ini_f16t18F);
        f16t18M.setText(ini_f16t18M);
        f18t20F.setText(ini_f18t20F);
        f18t20M.setText(ini_f18t20M);
    }
    
    public void saveOnClose() {
        if ( ini_disp_pv_obs != dispPvObs.isSelected() ) 
            Settings.PREF_BUNDLE.update( "DISPLAY_PVS_OBSERVATIONS", dispPvObs.isSelected() ? "Y" : "N" );
        if ( !ini_pv_obs_x.equals(isvfx.getValue()) )
            Settings.PREF_BUNDLE.update( "OBS_X", isvfx.getValue().toString() );
        if ( !ini_pv_obs_y.equals(isvfy.getValue()) )
            Settings.PREF_BUNDLE.update( "OBS_Y", isvfy.getValue().toString() );
        if ( !ini_font_size.equals(isvffs.getValue()) )
            Settings.PREF_BUNDLE.update( "FONT_SIZE", isvffs.getValue().toString() );
        if ( ini_used_mark != usedMark.getSelectionModel().getSelectedIndex() ) 
            Settings.PREF_BUNDLE.update( "USED_MARK", usedMark.getSelectionModel().getSelectedIndex() + "" );
        if ( !ini_f0t5F.equals(f0t5F.getText()) ) 
            Settings.PREF_BUNDLE.update( "0_TO_5_FEMALE", f0t5F.getText() );
        if ( !ini_f0t5M.equals(f0t5M.getText()) ) 
            Settings.PREF_BUNDLE.update( "0_TO_5_MALE", f0t5M.getText() );
        if ( !ini_f5t8F.equals(f5t8F.getText()) ) 
            Settings.PREF_BUNDLE.update( "5_TO_8_FEMALE", f5t8F.getText() );
        if ( !ini_f5t8M.equals(f5t8M.getText()) ) 
            Settings.PREF_BUNDLE.update( "5_TO_8_MALE", f5t8M.getText() );
        if ( !ini_f8t10F.equals(f8t10F.getText()) ) 
            Settings.PREF_BUNDLE.update( "8_TO_10_FEMALE", f8t10F.getText() );
        if ( !ini_f8t10M.equals(f8t10M.getText()) ) 
            Settings.PREF_BUNDLE.update( "8_TO_10_MALE", f8t10M.getText() );
        if ( !ini_f10t12F.equals(f10t12F.getText()) ) 
            Settings.PREF_BUNDLE.update( "10_TO_12_FEMALE", f10t12F.getText() );
        if ( !ini_f10t12M.equals(f10t12M.getText()) ) 
            Settings.PREF_BUNDLE.update( "10_TO_12_MALE", f10t12M.getText() );
        if ( !ini_f12t14F.equals(f12t14F.getText()) ) 
            Settings.PREF_BUNDLE.update( "12_TO_14_FEMALE", f12t14F.getText() );
        if ( !ini_f12t14M.equals(f12t14M.getText()) ) 
            Settings.PREF_BUNDLE.update( "12_TO_14_MALE", f12t14M.getText() );
        if ( !ini_f14t16F.equals(f14t16F.getText()) ) 
            Settings.PREF_BUNDLE.update( "14_TO_16_FEMALE", f14t16F.getText() );
        if ( !ini_f14t16M.equals(f14t16M.getText()) ) 
            Settings.PREF_BUNDLE.update( "14_TO_16_MALE", f14t16M.getText() );
        if ( !ini_f16t18F.equals(f16t18F.getText()) ) 
            Settings.PREF_BUNDLE.update( "16_TO_18_FEMALE", f16t18F.getText() );
        if ( !ini_f16t18M.equals(f16t18M.getText()) ) 
            Settings.PREF_BUNDLE.update( "16_TO_18_MALE", f16t18M.getText() );
        if ( !ini_f18t20F.equals(f18t20F.getText()) ) 
            Settings.PREF_BUNDLE.update( "18_TO_20_FEMALE", f18t20F.getText() );
        if ( !ini_f18t20M.equals(f18t20M.getText()) ) 
            Settings.PREF_BUNDLE.update( "18_TO_20_MALE", f18t20M.getText() );
    }
    
    private void disablePvSection(Boolean disable){
        usedMark.setDisable(disable);
        pvObsX.setDisable(disable);
        pvObsY.setDisable(disable);
        fntSize.setDisable(disable);
        usedMark.setDisable(disable);
        f0t5F.setDisable(disable);
        f0t5M.setDisable(disable);
        f5t8F.setDisable(disable);
        f5t8M.setDisable(disable);
        f8t10F.setDisable(disable);
        f8t10M.setDisable(disable);
        f10t12F.setDisable(disable);
        f10t12M.setDisable(disable);
        f12t14F.setDisable(disable);
        f12t14M.setDisable(disable);
        f14t16F.setDisable(disable);
        f14t16M.setDisable(disable);
        f16t18F.setDisable(disable);
        f16t18M.setDisable(disable);
        f18t20F.setDisable(disable);
        f18t20M.setDisable(disable);
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
    
    private class CustomStringConverter extends StringConverter<Integer> {
        @Override
        public String toString(Integer object) {
            return object + "";
        }

        @Override
        public Integer fromString(String string) {
            return Integer.parseInt(string);
        }
    }
}
