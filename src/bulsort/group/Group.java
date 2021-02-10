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

import bulsort.in.massar.Bulletin;
import bulsort.in.massar.Bulletin.Mixed;
import bulsort.in.massar.PV;
import bulsort.sorter.Settings;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import sicut.util.PDFDocument;
import sicut.util.Std;

/**
 *
 * @author Sicut
 */
public class Group {

    private final ArrayList<Student> students;
    private final String name;
    private String codesPattern;
    private final ArrayList<String> codes;
    private String level;
//    private PV pv;
    private Bulletin bul;
    private int girls;
    private Boolean pvAttached;

    public Group(String name) {
        students = new ArrayList();
        codes = new ArrayList();
        this.name = name;
        codesPattern = "(";
        girls = 0;
        pvAttached = false;
        bul = null;
    }

    public void addStudent(Student stu) {
        students.add(stu);
        codesPattern += "(" + stu.getCode() + ")|";
        codes.add(stu.getCode());
        girls += stu.isGirl() ? 1 : 0;
    }

    public void sortBy(Student.MARK m) {
        switch (m) {
            case S1:
                students.sort((Student s1, Student s2) -> {
                    return s1.getS1Mark().compareTo(s2.getS1Mark());
                });
                students.get(0).setRank(1);
                for ( int i = 1, rank = 1, n = students.size(); i < n; i++ ) {
                    if ( !Objects.equals(students.get(i - 1).getS1Mark(), students.get(i).getS1Mark()) )
                        rank = i + 1;
                    students.get(i - 1).setRank(rank);
                }
                break;
            case S2:
                students.sort((Student s1, Student s2) -> {
                    return s1.getS2Mark().compareTo(s2.getS2Mark());
                });
                students.get(0).setRank(1);
                for ( int i = 1, rank = 1, n = students.size(); i < n; i++ ) {
                    if ( !Objects.equals(students.get(i - 1).getS2Mark(), students.get(i).getS2Mark()) )
                        rank = i + 1;
                    students.get(i - 1).setRank(rank);
                }
                break;
            case LOCAL_EXAM:
                students.sort((Student s1, Student s2) -> {
                    return s1.getLocExamMark().compareTo(s2.getLocExamMark());
                });
                students.get(0).setRank(1);
                for ( int i = 1, rank = 1, n = students.size(); i < n; i++ ) {
                    if ( !Objects.equals(students.get(i - 1).getLocExamMark(), students.get(i).getLocExamMark()) )
                        rank = i + 1;
                    students.get(i - 1).setRank(rank);
                }
                break;
            case REGIONAL_EXAM:
                students.sort((Student s1, Student s2) -> {
                    return s1.getRegExamMark().compareTo(s2.getRegExamMark());
                });
                students.get(0).setRank(1);
                for ( int i = 1, rank = 1, n = students.size(); i < n; i++ ) {
                    if ( !Objects.equals(students.get(i - 1).getRegExamMark(), students.get(i).getRegExamMark()) )
                        rank = i + 1;
                    students.get(i - 1).setRank(rank);
                }
                break;
            case AVERAGE:
                students.sort((Student s1, Student s2) -> {
                    return s1.getAverage().compareTo(s2.getAverage());
                });
                students.get(0).setRank(1);
                for ( int i = 1, rank = 1, n = students.size(); i < n; i++ ) {
                    if ( !Objects.equals(students.get(i - 1).getAverage(), students.get(i).getAverage()) )
                        rank = i + 1;
                    students.get(i - 1).setRank(rank);
                }
                break;
            default:
                students.sort((Student s1, Student s2) -> {
                    return s1.getNum().compareTo(s2.getNum());
                });
                students.forEach(stu -> {
                    stu.setRank(stu.getNum());
                });
                break;
        }
    }

    public int getStudentRank(int num) {
        return -1;
    }

    public void closePattern() {
        codesPattern += "(nothing))";
    }

    public String getName() {
        return this.name;
    }

    public int getStudentsCount() {
        return students.size();
    }

    public int getFemaleStudentsCount() {
        return girls;
    }

    public String findAStudentCode(String txt) {
        Pattern ptr = Pattern.compile(codesPattern);
        Matcher m = ptr.matcher(txt);
        return m.find() ? m.group(1) : null;
    }

    public int getStudentNum(String code) {
        Optional<Student> s = students.stream().filter(o -> o.getCode().equals(code)).findFirst();
        return ( s.isPresent() ) ? s.get().getNum() : null;
    }

    public int getStudentNumFromText(String text) {
        return getStudentNum(findAStudentCode(text));
    }

    public String getStudentName(int num) {
        Optional<Student> s = students.stream().filter(o -> ( o.getNum() == num ) ).findFirst();
        return ( s.isPresent() ) ? s.get().getFullName() : "";
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void loadPv(PV pv) {
        pvAttached = true;
        pv.getStudents().forEach((code, pvRow) -> {
            Student stu = getStudent(code);
            if ( stu != null ) {
                stu.setAverage(pvRow.getAvg());
                stu.setS1Mark(pvRow.getS1());
                stu.setS2Mark(pvRow.getS2());
                stu.setLocExamMark(pvRow.getLoc());
                stu.setRegExamMark(pvRow.getReg());
            }
        });
    }

    public void removePv() {
        pvAttached = false;
        students.forEach(stu -> {
            stu.setAverage(-1.0);
            stu.setS1Mark(-1.0);
            stu.setS2Mark(-1.0);
            stu.setLocExamMark(-1.0);
            stu.setRegExamMark(-1.0);
        });
    }

    public Bulletin getPdfFile() {
        return bul;
    }

    public void setPdfFile(Bulletin pf) {
        this.bul = pf;
    }

    public String getCodesPattern() {
        return codesPattern;
    }

    public void setCodesPattern(String codesPattern) {
        this.codesPattern = codesPattern;
    }

    public ArrayList<String> getCodes() {
        return codes;
    }
    
    public Student getStudentByNum(Integer num) {
        Optional<Student> s = students.stream().filter(o -> o.getNum().equals(num)).findFirst();
        return ( s.isPresent() ) ? s.get() : null;
    }
    
    public Student getStudentByCode(String text) {
        Optional<Student> s = students.stream().filter(o -> o.getCode().equals(findAStudentCode(text))).findFirst();
        return ( s.isPresent() ) ? s.get() : null;
    }
    
    public Student getStudent(String code) {
        Optional<Student> s = students.stream().filter(o -> o.getCode().equals(code)).findFirst();
        return ( s.isPresent() ) ? s.get() : null;
    }
    
    public Boolean generate() {
        if ( bul == null )
            return Boolean.FALSE;
        if ( !bul.reload() )
            return Boolean.FALSE;
        PDFDocument out = new PDFDocument();
        String dest = Settings.PREF_BUNDLE.get("DEST_DIR");
        out.create();
        if ( !generate(out) )
            return Boolean.FALSE;
        dest = ( ( new File(dest) ).exists() ? dest : bul.getLocation() );
        out.save( dest  + "/" + name + "(" + (new SimpleDateFormat(Settings.PREF_BUNDLE.get("DATE_FORMAT_OUTPUT"))).format(new Date()) + ").pdf" );
        out.close();
        bul.close();
        return Boolean.TRUE;
    }
    
    public Boolean generate(PDFDocument out) {
        Integer duplicate, usedMark;
        boolean deleteWM, coverPg, stamp, forceSort, obs;
        PDImageXObject img;
        Float x, y, w, h, obsx, obsy;
        Integer fntSz;
        int m, l, deg;
        try {
            duplicate = Integer.parseInt(Settings.PREF_BUNDLE.get("DUPLICATE_PAGES"));
        } catch( NumberFormatException nfe ) {
            duplicate = 0;
        }
        deleteWM = Settings.PREF_BUNDLE.get("DEL_WATERMARK").equals("Y");
        coverPg = Settings.PREF_BUNDLE.get("PREPEND_TITLE_PAGE").equals("Y");
        stamp = Settings.PREF_BUNDLE.get("USE_STAMP").equals("Y");
        forceSort = Settings.PREF_BUNDLE.get("SORT_BULLETINS").equals("Y");
        obs = Settings.PREF_BUNDLE.get("DISPLAY_PVS_OBSERVATIONS").equals("Y");
        usedMark = Integer.parseInt(Settings.PREF_BUNDLE.get("USED_MARK"));
        img = null;
        x = y = w = h = obsx = obsy = 0f;
        fntSz = 16;
        if ( stamp ) {
            try {
                File imgFile = new File(Settings.PREF_BUNDLE.get("STAMP"));
                if ( imgFile.exists() ) {
                    img = PDImageXObject.createFromFileByContent(imgFile, out.getDocument());
                    x = Float.parseFloat(Settings.PREF_BUNDLE.get("STAMP_X"));
                    y = Float.parseFloat(Settings.PREF_BUNDLE.get("STAMP_Y"));
                    w = Float.parseFloat(Settings.PREF_BUNDLE.get("STAMP_W"));
                    h = Float.parseFloat(Settings.PREF_BUNDLE.get("STAMP_H"));
                }
            } catch (IOException | SecurityException | NumberFormatException ex) { }
        }
        if ( obs ) {
            try {
                obsx = Float.parseFloat(Settings.PREF_BUNDLE.get("OBS_X"));
            } catch( NumberFormatException nfe ) {}
            try {
                obsy = Float.parseFloat(Settings.PREF_BUNDLE.get("OBS_Y"));
            } catch( NumberFormatException nfe ) {}
            try {
                fntSz = Integer.parseInt(Settings.PREF_BUNDLE.get("FONT_SIZE"));
            } catch( NumberFormatException nfe ) {}
        }
        switch ( duplicate ) {
            case 2:
                m = 2;
                l = 1;
                deg = 0;
                break;
            case 1:
                m = 1;
                l = 2;
                deg = -90;
                break;
            default:
                m = 1;
                l = 1;
                deg = -90;
        }
        for ( int k = 0; k < l; k++ ) {
            if( coverPg ) {
                for ( int i = 0; i < m; i++ ) {
                    PDPage pg = new PDPage(bul.getPage(0).getMediaBox());
                    out.addPage(pg);
                    out.appendText(pg, "| " + this.name, deg, 100, 100 - deg * 2, 36);
                }
            }
            if ( forceSort ) {
                HashMap<Integer, Mixed> map = bul.getMap();
                Integer[] keys = (Integer[]) map.keySet().stream().sorted().toArray(Integer[]::new);
                for ( Integer key : keys ) {
                    PDPage pg = bul.clonePage(map.get(key).getPage(), deleteWM);
                    if ( stamp && img != null ) {
                        out.appendImage(pg, img, x, y, w, h);
                    }
                    if ( obs && pvAttached ) {
                        Student stu = getStudent(map.get(key).getCode());
                        if ( stu != null ) {
                            String observation = getObservation(stu, usedMark);
                            if ( Std.isArabic(observation) ) {
                                out.appendArabicText(pg, observation, obsx, obsy, fntSz);
                            }
                            else
                                out.appendText(pg, observation, obsx, obsy, fntSz);
                        }
                    }
                    out.addPage(pg);
                    if ( duplicate == 2 )
                        out.addPage(pg);
                }
            }
            else {
                for ( int i = 0, n = bul.getNumberOfPages(); i < n; i++ ) {
                    PDPage pg = bul.clonePage(i, deleteWM);
                    if ( stamp && img != null ) {
                        out.appendImage(pg, img, x, y, w, h);
                    }
                    out.addPage(pg);
                    if ( duplicate == 2 )
                        out.addPage(pg);
                }
            }
            deleteWM = false;
        }
        return Boolean.TRUE;
    }
    
    public Boolean reloadBulletin() {
        if ( bul == null )
            return Boolean.FALSE;
        return bul.reload();
    }
    
    public void closeBulletin() {
        if ( bul != null )
            bul.close();
    }
    
    private String getObservation(Student stu, Integer usedMark) {
        String obs = "";
        Double mark = -1.0;
        String suffix = stu.isGirl() ? "_FEMALE" : "_MALE";
        switch ( usedMark ) {
            case 0:
                mark = stu.getLastMark();
                break;
            case 1:
                mark = stu.getS1Mark();
                break;
            case 2:
                mark = stu.getS2Mark();
                break;
            case 3:
                mark = stu.getAverage();
                break;
        }
        if ( mark >= 0 && mark < 5 )
            obs = Settings.PREF_BUNDLE.get("0_TO_5" + suffix);
        if ( mark >= 5 && mark < 8 )
            obs = Settings.PREF_BUNDLE.get("5_TO_8" + suffix);
        if ( mark >= 8 && mark < 10 )
            obs = Settings.PREF_BUNDLE.get("8_TO_10" + suffix);
        if ( mark >= 10 && mark < 12 )
            obs = Settings.PREF_BUNDLE.get("10_TO_12" + suffix);
        if ( mark >= 12 && mark < 14 )
            obs = Settings.PREF_BUNDLE.get("12_TO_14" + suffix);
        if ( mark >= 14 && mark < 16 )
            obs = Settings.PREF_BUNDLE.get("14_TO_16" + suffix);
        if ( mark >= 16 && mark < 18 )
            obs = Settings.PREF_BUNDLE.get("16_TO_18" + suffix);
        if ( mark >= 18 && mark <= 20 )
            obs = Settings.PREF_BUNDLE.get("18_TO_20" + suffix);
        return obs;
    }
}

/*
    Bookmarking program
    
            PDDocumentOutline outline = new PDDocumentOutline();
            doc.getDocumentCatalog().setDocumentOutline(outline);
            PDOutlineItem pagesOutline = new PDOutlineItem();
            pagesOutline.setTitle("All Pages");
            outline.addLast(pagesOutline);

            int pageNum = 1;
            for (PDPage page : doc.getPages()) {

                // write some content
                PDPageContentStream contents = new PDPageContentStream(doc, page);
                contents.beginText();
                contents.newLineAtOffset(100, 700);
                contents.setFont(PDType1Font.HELVETICA, 12);
                contents.showText("Content page " + pageNum);
                contents.endText();
                contents.close();

                PDPageDestination dest = new PDPageFitWidthDestination();
                dest.setPage(page);
                PDOutlineItem bookmark = new PDOutlineItem();
                bookmark.setDestination(dest);
                bookmark.setTitle("Page " + pageNum);
                pagesOutline.addLast(bookmark);

                pageNum++;
            }

            pagesOutline.openNode();
            outline.openNode();

            doc.save(new File("/tmp/bookmark.pdf"));
*/
