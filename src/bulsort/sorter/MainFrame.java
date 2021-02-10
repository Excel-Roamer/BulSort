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

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author Sicut
 */
public class MainFrame {
    
    private Scene scene;

    public MainFrame() {
        try {
            FXMLLoader fl;
            Parent root;
            fl = new FXMLLoader(getClass().getResource("/bulsort/sorter/mainFrame.fxml"), Settings.I18N_BUNDLE);
            root = fl.load();
            scene = new Scene(root);
            scene.setNodeOrientation( ( Settings.PREF_BUNDLE.get("LANGUAGE").equals("AR") ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT ) );
        }
        catch(IOException ex) {
            System.out.println(ex);
        }
    }
    
    public Scene getScene() {
        return scene;
    }
    
    public Boolean isResizable() {
        return true;
    }
    
    public Boolean isMaximized() {
        return true;
    }
    
}
