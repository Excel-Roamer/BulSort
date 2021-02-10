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

package bulsort.in.massar;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import sicut.util.PDFDocument;
import sicut.util.Std.EXTENDED_BOOLEAN;

/**
 *
 * @author Surfer
 */
public class Bulletin extends PDFDocument {
    
    private EXTENDED_BOOLEAN sorted, temp;
    private final HashMap<Integer, Mixed> map; // student's row, [student's page, student's code]
            
    public Bulletin(){
        super();
        sorted = EXTENDED_BOOLEAN.NULL;
        temp = EXTENDED_BOOLEAN.NULL;
        map = new HashMap();
    }
    
    public Bulletin(File f){
        this();
        setFile(f);
    }
    
    public String getGroupName(String groupsPattern){
        String str;
        Pattern p;
        Matcher m;
        str = getPageContent(1);
        if ( str.isEmpty() )  return "";
        p = Pattern.compile(groupsPattern);
        m = p.matcher(str);
        return ( m.find() ) ? ( m.group().trim() ) : "";
    }
    
    public boolean isTemporal() {
        if ( temp != EXTENDED_BOOLEAN.NULL )
            return temp == EXTENDED_BOOLEAN.TRUE;
        temp = EXTENDED_BOOLEAN.FALSE;
        try {
            PDFStreamParser parser = new PDFStreamParser(getPage(1));
            parser.parse();
            if ( parser.getTokens().stream().filter((token) -> ( token instanceof Operator && ((Operator)token).getName().equalsIgnoreCase("DO") )).count() > 2 ) {
                temp = EXTENDED_BOOLEAN.TRUE;
                return true;
            }
            else    return false;
        }
        catch( IOException e ) {
            return false;
        }
    }
    
    public void makeMap(String codesPattern, ArrayList<String> codes) {
        map.clear();
        String str;
        Pattern p;
        Matcher m;
        p = Pattern.compile(codesPattern);
        Boolean sortTest = true;
        for ( int i = 0, n = getNumberOfPages(); i < n; i++ ) {
            str = getPageContent(i);
            if ( str.isEmpty() )    continue;
            m = p.matcher(str);
            if ( m.find() ) {
                map.put(codes.indexOf(m.group()), new Mixed(m.group(), i));
                sortTest &= true;
            }
            else
                sortTest = false;
        }
        sorted = sortTest ? EXTENDED_BOOLEAN.TRUE : EXTENDED_BOOLEAN.FALSE;
    }

    public HashMap<Integer, Mixed> getMap() {
        return map;
    }
    
    public boolean isSorted() {
        return sorted == EXTENDED_BOOLEAN.TRUE;
    }

    public class Mixed {
        
        private final String code;
        private final Integer page;

        public Mixed(String code, Integer page) {
            this.code = code;
            this.page = page;
        }

        public String getCode() {
            return code;
        }

        public Integer getPage() {
            return page;
        }
        
        
    }
}
