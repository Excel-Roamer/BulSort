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

package bulsort.group;

import bulsort.sorter.Settings;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 *
 * @author Sicut
 */
public class Container {

    private ContainerController cntrl;
    private VBox p;
    
    public Container(String grp, int size) {
        FXMLLoader fl = new FXMLLoader(getClass().getResource("container.fxml"), Settings.I18N_BUNDLE);
        cntrl = null;
        try {
            p = fl.load();
            cntrl = fl.getController();
            cntrl.setGroup(grp);
            cntrl.setSize(size);
        }
        catch ( IOException ex ) {
//            System.out.println(ex);
        }
    }
    public Container(String grp, int sizeAll, int girls) {
        FXMLLoader fl = new FXMLLoader(getClass().getResource("container.fxml"), Settings.I18N_BUNDLE);
        cntrl = null;
        try {
            p = fl.load();
            cntrl = fl.getController();
            cntrl.setGroup(grp);
            cntrl.setSize(sizeAll, girls);
        }
        catch ( IOException ex ) {
//            System.out.println(ex);
        }
    }
    
    public Node get(){
        return p;
    }
    
    public void setHasPDF(boolean b) {
        cntrl.setHasPDF(b);
        if ( !b ) {
            setIsTemp(false);
            setIsSorted(false);
        }
    }
    
    public void setHasPV(boolean b) {
        cntrl.setHasPV(b);
    }
    
    public void setPDF(String infos) {
        cntrl.setPDF(infos);
    }
    
    public void setPV(String infos) {
        cntrl.setPV(infos);
    }
    
    public void setIsTemp(boolean b) {
        cntrl.setTemp(b);
    }
    
    public void setIsSorted(boolean b) {
        cntrl.setSorted(b);
    }
    
    public void setIsProcessing(boolean b) {
        cntrl.setProgress(b);
    }
}
