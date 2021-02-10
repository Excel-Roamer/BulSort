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

import bulsort.school.db.SchoolDB;
import icofont.IcoMoon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import sicut.db.Preferences;
import sicut.util.EnvVariable;

/**
 *
 * @author Sicut
 */
public class Settings {
    public static final String DB_FOLDER_PATH = EnvVariable.APPDATADirectory() + "/BulSort/";
    public static final String PREF_DB_NAME = "preferences.bs";
    public static final String I18N_DB_NAME = "properties.bs";
    public static final String PREF_DB_PATH = DB_FOLDER_PATH + PREF_DB_NAME;
    public static final String I18N_DB_PATH = DB_FOLDER_PATH + I18N_DB_NAME;
    public static final String APP_NAME = "BulSort";
    public static final String APP_VERSION = "2.0.1";
    public static final String APP_YEAR = "2021";
    public static final String APP_DATE = "2021-02-10";
    public static final String APP_TITLE = APP_NAME + " " + APP_VERSION;
    public static final ArrayList<String> SUPPORTED_LANGS = new ArrayList(Arrays.asList("AR", "FR", "EN"));
    
    
    public static ResourceBundle I18N_BUNDLE;
    public static Preferences PREF_BUNDLE;
    public static SchoolDB SCHOOL_DB;
    
    public static IcoMoon icoFont = new IcoMoon();

}
