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
import bulsort.in.massar.Bulletin;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Sicut
 */
class BulletinsLoader implements Runnable {

    private final List<File> l;
    private final HashMap<String, Container> grpsContainers;
    private final SimpleStringProperty status;

    public BulletinsLoader(List<File> l, HashMap<String, Container> grpsContainers, SimpleStringProperty status) {
        this.l = l;
        this.grpsContainers = grpsContainers;
        this.status = status;
    }

    @Override
    public void run() {
        l.forEach((item) -> {
            Bulletin pf = new Bulletin(item);
            if (pf.load()) {
                String g;
                Group grp;
                Container c;
                g = pf.getGroupName(Settings.SCHOOL_DB.getGroupsPattern());
                if ( !g.isEmpty() ) {
                    grp = Settings.SCHOOL_DB.getGroup(g);
                    c = grpsContainers.get(g);
                    c.setIsProcessing(true);
                    if (grp != null) {
                        boolean sorted, temp;
                        grp.setPdfFile(pf);
                        temp = pf.isTemporal();
                        pf.makeMap(grp.getCodesPattern(), grp.getCodes());
                        sorted = pf.isSorted();
                        // Preparing a tooltip
                        c.setPDF(Settings.I18N_BUNDLE.getString("GROUP") + g + ".\n" +
                                Settings.I18N_BUNDLE.getString("PAGES_COUNT") + pf.getNumberOfPages() + ".\n" +
                                Settings.I18N_BUNDLE.getString("SORTED") + Settings.I18N_BUNDLE.getString(( sorted ?  "YES" : "NO" )) + ".\n" +
                                Settings.I18N_BUNDLE.getString("TEMPORARY") + Settings.I18N_BUNDLE.getString(( temp ?  "YES" : "NO" )) + ".\n" +
                                Settings.I18N_BUNDLE.getString("BUL_PATH") + item.getAbsolutePath());
                        c.setIsTemp(temp);
                        c.setIsSorted(sorted);
                    }
                    c.setIsProcessing(false);
                }
                pf.close();
//                if (rename && grp != null) {
//                    item.renameTo(new File(item.getParentFile().getAbsolutePath() + "\\" + grp.getName() + ".pdf"));
//                }
            }
        });
        status.set(status.get() + "Done");
    }
}
