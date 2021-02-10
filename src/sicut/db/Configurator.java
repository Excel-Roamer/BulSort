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

package sicut.db;

import bulsort.sorter.Settings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.pdfbox.util.Charsets;
import sicut.sqlite.Connector;

/**
 *
 * @author Sicut
 */
public final class Configurator {
    private final Connector con;
    
    public Configurator() {
        con = new Connector(Settings.PREF_DB_PATH);
    }
    
    public Configurator(Connector con) {
        this.con = con;
    }
    
    public void prepare() {
        File db = new File(Settings.PREF_DB_PATH);
        if ( !db.exists() ) {
            (new File(Settings.DB_FOLDER_PATH)).mkdir();
            createDB("/bulsort/resources/preferences.sql");
        }
        db = new File(Settings.I18N_DB_PATH);
        if ( !db.exists() ) {
            con.setDatabase(Settings.I18N_DB_PATH);
            createDB("/bulsort/resources/i18n.sql");
        }
    }
    
    private void createDB(String src) {
        String crQuery = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResource(src).openStream(), Charsets.UTF_8));
            if ( !con.connect() )   return;
            String line;
            con.prpeareBatch();
            while ((line = br.readLine()) != null) {
                if ( line.startsWith("--"))
                    continue;
                crQuery += line;
                if ( line.contains(";") ) {
                    con.batch(crQuery);
                    crQuery = "";
                }
            }
            con.runBatch();
            con.close();
        } 
        catch (IOException ex) {
        }
    }
}
