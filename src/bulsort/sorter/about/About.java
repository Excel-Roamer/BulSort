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

package bulsort.sorter.about;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sicut
 */
public class About {
    
    private Stage stg;
    
    public About(Stage stage) {
        try {
            FXMLLoader fl = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent root = fl.load();
            Scene scene = new Scene(root);
            stg = new Stage();
            stg.setScene(scene);
            stg.initStyle(StageStyle.UTILITY);
            stg.initModality(Modality.WINDOW_MODAL);
            stg.initOwner(stage);
            stg.setResizable(false);
            scene.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/bulsort/resources/images/64x64.png")));
        }
        catch(IOException ex) {
            //System.out.println(ex);
        }
    }
    
    public void show() {
        stg.show();
    }
    
}
