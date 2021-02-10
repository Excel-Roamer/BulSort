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

import bulsort.group.Container;
import bulsort.group.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.function.Function;
import javafx.application.Platform;

/**
 *
 * @author Sicut
 */
class CustomTask extends TimerTask {
    
    private int index = 0;
    private final HashMap<String, LevelPane> subList;
    private final HashMap<String, Container> grpsContainers;
    private final Function<Boolean, Void> callback;

    public CustomTask(Function<Boolean, Void> callback, 
            HashMap<String, LevelPane> subList, HashMap<String, Container> grpsContainers) {
        this.subList = subList;
        this.grpsContainers = grpsContainers;
        this.callback = callback;
    }
    
    @Override
    public void run() {
        ArrayList<Group> grps = Settings.SCHOOL_DB.getGroups();
        if ( index < grps.size() ) {
            Group grp = grps.get(index++);
            Container c = new Container(grp.getName(), grp.getStudentsCount(), grp.getFemaleStudentsCount());
            LevelPane lp = subList.get(grp.getName().substring(0, 2));
            grpsContainers.put(grp.getName(), c);
            Platform.runLater(() -> {
                lp.getTilePane().getChildren().add(c.get());
                if ( !lp.isVisible() ) 
                    lp.show(true);
            });
        }
        else {
            this.cancel();
            callback.apply(false);
        }
    }
}