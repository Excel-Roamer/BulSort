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

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Sicut
 */
public class AdditionalOptionsController {

    @FXML private GeneralOptionsController genOpsController;
    @FXML private PvsOptionsController pvsOpsController;
    @FXML private VBox genOps, pvsOps;
    @FXML private Button showGenOps, showPvsOps;    

    public AdditionalOptionsController() {
        
    }
    
    public void initialize() {
        pvsOps.managedProperty().bind(pvsOps.visibleProperty());
        genOps.managedProperty().bind(genOps.visibleProperty());
        pvsOps.setVisible(false);
        showGenOps.getStyleClass().add("options-button");
        showGenOps.setOnAction(evt -> {
            pvsOps.setVisible(false);
            genOps.setVisible(true);
            genOps.requestFocus();
            if ( !showGenOps.getStyleClass().contains("options-button") )
                showGenOps.getStyleClass().add("options-button");
            showPvsOps.getStyleClass().remove("options-button");
        });
        showPvsOps.setOnAction(evt -> {
            genOps.setVisible(false);
            pvsOps.setVisible(true);
            pvsOps.requestFocus();
            if ( !showPvsOps.getStyleClass().contains("options-button") )
                showPvsOps.getStyleClass().add("options-button");
            showGenOps.getStyleClass().remove("options-button");
        });
        genOps.requestFocus();
    }
    
    public void saveOnClose() {
        genOpsController.saveOnClose();
        pvsOpsController.saveOnClose();
    }
}
