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
import icofont.IcoMoon;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

/**
 *
 * @author Sicut
 */
public class ContainerController {
    
    @FXML private Label group, pdf, pv, temp, sorted, size;
    @FXML private ProgressIndicator progress;
    @FXML private Tooltip pvToolTip, pdfToolTip, sizeToolTip;
    
    private final Font fnt;
    private final String ln = "\n";

    public ContainerController() {
        fnt = (new IcoMoon(18)).getFont();
    }
         
    public void initialize() {
        pdf.setFont(fnt);
        pdf.setText(IcoMoon.file_pdf);
        pv.setFont(fnt);
        pv.setText(IcoMoon.file_excel);
        temp.setFont(fnt);
        temp.setText(IcoMoon.clock);
        sorted.setFont(fnt);
        sorted.setText(IcoMoon.sort_numeric_asc);
        size.setText("***");
        group.setText("***");
    }
    
    public void setGroup(String grp) {
        group.setText(grp);
    }
    
    public void setHasPDF(boolean b) {
        pdf.setVisible(b);
    }
    
    public void setPDF(String infos) {
        pdf.setVisible(true);
        pdfToolTip.setText(infos);
    }
    
    public boolean hasPDF() {
        return pdf.isVisible();
    }
    
    public void setHasPV(boolean b) {
        pv.setVisible(b);
    }
    
    public void setPV(String infos) {
        pv.setVisible(true);
        pvToolTip.setText(infos);   
    }
    
    public boolean hasPV() {
        return pv.isVisible();
    }
    
    public void setSorted(boolean b) {
        sorted.setVisible(b);
    }
    
    public boolean isSorted() {
        return sorted.isVisible();
    }
    
    public void setTemp(boolean b) {
        temp.setVisible(b);
    }
    
    public boolean isTemp() {
        return temp.isVisible();
    }
    
    public void setSize(int n) {
        size.setText(n + " " + Settings.I18N_BUNDLE.getString("STUDENTS"));
    }
    
    public void setSize(int all, int girls) {
        if ( all == 0 ) {
            size.setText(Settings.I18N_BUNDLE.getString("ZERO_STUDENT"));
        }
        else if ( all == 1 ) {
            size.setText(Settings.I18N_BUNDLE.getString("ONE_STUDENT"));
        }
        else if ( all == 2 ) {
            size.setText(Settings.I18N_BUNDLE.getString("TWO_STUDENTS"));
        }
        else if ( all <= 10 ) {
            size.setText(all + " " + Settings.I18N_BUNDLE.getString("STUDENTS_UNDER_10"));
        }
        else if ( all % 100 == 0 ) {
            size.setText(all + " " + Settings.I18N_BUNDLE.getString("STUDENTS_DIV_100"));
        }
        else {
            size.setText(all + " " + Settings.I18N_BUNDLE.getString("STUDENTS"));
        }
        sizeToolTip.setFont((new IcoMoon(16)).getFont());
        sizeToolTip.setText(IcoMoon.female + ": " + girls + "\n" + IcoMoon.male + ": " + ( all - girls ) );
    }
    
    public void setProgress(boolean b) {
        progress.setVisible(b);
    }
}
