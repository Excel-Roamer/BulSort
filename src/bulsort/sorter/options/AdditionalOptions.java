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
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Sicut
 */
public class AdditionalOptions {
    
    private Stage stage;
    
    public AdditionalOptions(Stage parent) {
        try {
            
            FXMLLoader fl = new FXMLLoader(getClass().getResource("additionalOptions.fxml"), Settings.I18N_BUNDLE);
            Parent root = fl.load();
            Scene scene = new Scene(root);
            stage = new Stage();
            scene.setNodeOrientation( parent.getScene().getNodeOrientation() );
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);
            stage.setResizable(true);
            stage.setOnCloseRequest(evt -> {
                ((AdditionalOptionsController) fl.getController()).saveOnClose();
            });
        }
        catch(IOException ex) {}
    }
    
    public void show() {
        stage.show();
    }
    
}
