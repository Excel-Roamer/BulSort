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

package bulsort.school.db;

import bulsort.group.Group;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author Sicut
 */
public class SchoolDB {
    private final ArrayList<Group> grps;
    private String grpsPtrn;
    private String school, direction, academy, year;
    
    public SchoolDB() {
        grps = new ArrayList();
        grpsPtrn = "";
    }
    
    public void reset() {
        grps.clear();
        grpsPtrn = "";
        school = "";
        direction = "";
        academy = "";
        year = "";
    }
    
    public ArrayList<Group> getGroups() {
        return grps;
    }
    
    public String getGroupsPattern() {
        if ( !grpsPtrn.isEmpty() )
            return grpsPtrn;
        grpsPtrn = "(";
        for ( Group g : grps ) {
            grpsPtrn += "(" + g.getName() + ")|";
        }
        grpsPtrn += "(nothing))\\s";
        return grpsPtrn;
    }
    
    public Group getGroup(String grpName) {
        Optional<Group> o = grps.stream().filter((g) -> g.getName().equals(grpName)).findFirst();
        return ( o.isPresent() ) ? o.get() : null;
    }
    
    public Group getGroup(int grpIndex) {
        return grps.get(grpIndex);
    }
    
    public Group addGroup(String grpName) {
        Group grp = new Group(grpName);
        grps.add(grp);
        return grp;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
    
}
