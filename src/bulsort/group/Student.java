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

/**
 *
 * @author Sicut
 */
public class Student {
    private Integer num;
    private String firName, secName, gender, address, group, level, code, birthDate;
    private Double s1Mark, s2Mark, average, locExamMark, regExamMark;
//    private final Integer[] ranks;
    private Integer rank;
    public enum MARK { S1, S2, AVERAGE, LOCAL_EXAM, REGIONAL_EXAM };

    public Student() {
        num = 0;
//        ranks = new Integer[] { 1, 1, 1, 1, 1 };
        firName = "";
        secName = "";
        address = "";
        group = "";
        level = "";
        code = "";
        birthDate = "";
        s1Mark = -1.0;
        s2Mark = -1.0;
        locExamMark = -1.0;
        regExamMark = -1.0;
        average = -1.0;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

//    public Integer getRank(MARK m) {
//        switch( m ) {
//            case S1:
//                return ranks[0];
//            case S2:
//                return ranks[1];
//            case LOCAL_EXAM:
//                return ranks[3];
//            case REGIONAL_EXAM:
//                return ranks[4];
//            default:
//                return ranks[2];
//        }
//    }
    
    public Integer getRank() {
        return rank;
    }

//    public void setRank(MARK m, Integer rank) {
//        switch( m ) {
//            case S1:
//                ranks[0] = rank;
//            case S2:
//                ranks[1] = rank;
//            case LOCAL_EXAM:
//                ranks[3] = rank;
//            case REGIONAL_EXAM:
//                ranks[4] = rank;
//            default:
//                ranks[2] = rank;
//        }
//    }
    
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirName() {
        return firName;
    }

    public void setFirName(String firName) {
        this.firName = firName;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public Double getS1Mark() {
        return s1Mark;
    }

    public void setS1Mark(Double s1Mark) {
        this.s1Mark = s1Mark;
    }

    public Double getS2Mark() {
        return s2Mark;
    }

    public void setS2Mark(Double s2Mark) {
        this.s2Mark = s2Mark;
    }

    public Double getLocExamMark() {
        return locExamMark;
    }

    public void setLocExamMark(Double locExamMark) {
        this.locExamMark = locExamMark;
    }

    public Double getRegExamMark() {
        return regExamMark;
    }

    public void setRegExamMark(Double regExamMark) {
        this.regExamMark = regExamMark;
    }

    public Double getAverage() {
        return average;
    }

    public Double getLastMark() {
        if ( average >= 0 )
            return average;
        if ( s2Mark >= 0 )
            return s2Mark;
        return s1Mark;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getFullName() {
        return firName + " " + secName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    
    public Boolean isGirl() {
        return gender.matches("(F)|(Female)|(Fille)|(أنثى)");
    }
    
}
