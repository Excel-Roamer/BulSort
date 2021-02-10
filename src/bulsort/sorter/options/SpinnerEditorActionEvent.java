/*
 * Copyright (C) 2021 Sicut
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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

/**
 *
 * @author Sicut
 */
public class SpinnerEditorActionEvent implements EventHandler<ActionEvent> {
    
    private final IntegerSpinnerValueFactory svf;
    private final Spinner s;

    public SpinnerEditorActionEvent(IntegerSpinnerValueFactory svf, Spinner s) {
        this.svf = svf;
        this.s = s;
    }
    
    @Override
    public void handle(ActionEvent event) {
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
