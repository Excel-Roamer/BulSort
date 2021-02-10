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

/**
 *
 * @author Sicut
 */
public class PVRow {
        
    private String name, gender;
    private Double s1, s2, loc, reg, avg;
    private String[] years;
    private String dec;

    public PVRow() {
        years = new String[] { "0", "0", "0" };
    }

    public PVRow(Double s1, Double s2, Double loc, Double reg, Double avg, String y0, String y1, String y2, String dec, String name, String gender) {
        this();
        this.s1 = s1;
        this.s2 = s2;
        this.loc = loc;
        this.reg = reg;
        this.avg = avg;
        this.dec = dec;
        this.name = name;
        this.gender = gender;
        years[0] = y0;
        years[1] = y1;
        years[2] = y2;
    }

    public Double getS1() {
        return s1;
    }

    public void setS1(Double s1) {
        this.s1 = s1;
    }

    public Double getS2() {
        return s2;
    }

    public void setS2(Double s2) {
        this.s2 = s2;
    }

    public Double getLoc() {
        return loc;
    }

    public void setLoc(Double loc) {
        this.loc = loc;
    }

    public Double getReg() {
        return reg;
    }

    public void setReg(Double reg) {
        this.reg = reg;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public String[] getYears() {
        return years;
    }

    public void setYears(String[] years) {
        this.years = years;
    }

    public String getDecision() {
        return dec;
    }

    public void setDecision(String dec) {
        this.dec = dec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
}
