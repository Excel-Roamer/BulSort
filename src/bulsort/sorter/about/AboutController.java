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

import bulsort.sorter.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Sicut
 */
public class AboutController {
    
    @FXML private Label appName, buildInfos;
    

    public AboutController() {
        
    }
    
    public void initialize() {
        appName.setText(Settings.APP_TITLE + " (" + Settings.APP_YEAR + ")");
        buildInfos.setText(buildInfos.getText().replace("%version%", Settings.APP_VERSION).replace("%date%", Settings.APP_DATE));
    }
    
}
