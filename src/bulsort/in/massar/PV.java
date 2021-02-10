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
import java.util.HashMap;
import org.apache.poi.hssf.util.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Surfer
 * @updated by Sicut
 */
public final class PV {
    private final String codeClmn, dataSheetName, s1Clmn, s2Clmn, locClmn, regClmn;
    private String decisionClmn, avgClmn;
    private final String[] yearClmns;
    private final String groupCell, yearCell, directionCell, academyCell, levelCell, schoolCell;
    private final int firstRow;
        
    private XSSFWorkbook wb;
    private XSSFSheet sht;
    private String group, year, level, direction, academy, school;
    private final HashMap<String, PVRow> students;
    private boolean valid;
            
    public PV(){
        yearClmns = new String[]{"E", "F", "G"};
        codeClmn = "B";
        s1Clmn = "H";
        s2Clmn = "I";
        locClmn = "J";
        regClmn = "K";
        firstRow = 12;
        dataSheetName = "Data";
        groupCell = "J7";
        yearCell = "K5";
        directionCell = "F5";
        academyCell = "C5";
        levelCell = "F7";
        schoolCell = "C7";
        students = new HashMap();
        valid = false;
        sht = null;
    }
    
    public PV(File f){
        this();
        setWorkbook(f);
    }
    
    public boolean setWorkbook(File f){
        boolean res = false;
        try {
            wb = new XSSFWorkbook(f);
            sht = wb.getSheet(dataSheetName);
            if ( isItFromMassar() ) {
                group = getStringValue(sht, groupCell);
                year = getStringValue(sht, yearCell);
                level = getStringValue(sht, levelCell);
                direction = getStringValue(sht, directionCell);
                academy = getStringValue(sht, academyCell);
                school = getStringValue(sht, schoolCell);
                if ( group.startsWith("3A") ) {
                    decisionClmn = "O";
                    avgClmn = "L";
                }
                else {
                    decisionClmn = "K";
                    avgClmn = "J";
                }
                load();
                res = true;
            }
            wb.close();
        }
        catch(IOException | InvalidFormatException e){
            wb = null;
        }
        valid = res;
        return res;
    }
    
    public boolean isItFromMassar(){
        return ( sht == null ) ? false : (getStringValue(sht, "E2").contains("قرار مجلس القسم") && getStringValue(sht, "B10").contains("رقم التلميذ") && 
                getStringValue(sht, "I7").contains("القسم") && getStringValue(sht, "I5").contains("السنة الدراسية") &&
                getStringValue(sht, "C10").contains("الاسم و النسب"));
    }

    public boolean isValid() {
        return valid;
    }
    
    public String getGroup(){
        return group;
    }

    public String getYear() {
        return year;
    }

    public String getLevel() {
        return level;
    }

    public String getDirection() {
        return direction;
    }

    public String getAcademy() {
        return academy;
    }

    public String getSchool() {
        return school;
    }

    public HashMap<String, PVRow> getStudents() {
        return students;
    }
    
    public int getStudentsCount(){
        return students.size();
    }
    
    public boolean isNull(){
        return (wb == null);
    }
    
    private String getStringValue(XSSFSheet sht, String cellRef){
        CellReference cr;
        DataFormatter df;
        cr = new CellReference(cellRef);
        df = new DataFormatter();
        return df.formatCellValue(sht.getRow(cr.getRow()).getCell(cr.getCol()));
    }
    
    private Double getNumericValue(XSSFSheet sht, String cellRef){
        CellReference cr = new CellReference(cellRef);
        return sht.getRow(cr.getRow()).getCell(cr.getCol()).getNumericCellValue();
    }
    
    private void load() {
        String code;
        int r = firstRow;
        while( true ) {
            try{
                if ( !( code = getStringValue(sht, codeClmn + r)).isEmpty() ) {
                    PVRow pvr = new PVRow();
                    pvr.setS1(getNumericValue(sht, s1Clmn + r));
                    pvr.setS2(getNumericValue(sht, s2Clmn + r));
                    if ( group.startsWith("3A") ) {
                        pvr.setLoc(getNumericValue(sht, locClmn + r));
                        pvr.setReg(getNumericValue(sht, regClmn + r));
                    }
                    else {
                        pvr.setLoc(-1.0);
                        pvr.setReg(-1.0);
                    }
                    pvr.setAvg(getNumericValue(sht, avgClmn + r));
                    pvr.setYears(new String[] {getStringValue(sht, yearClmns[0] + r), getStringValue(sht, yearClmns[1] + r), getStringValue(sht, yearClmns[2] + r)});
                    pvr.setDecision(getStringValue(sht, decisionClmn + r));
                    students.put(code, pvr);
                    r++;
                }
                else 
                    break;
            }
            catch(NullPointerException e){
                break;
            }
        }
    }
    
}
