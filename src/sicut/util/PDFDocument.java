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

package sicut.util;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.Matrix;

/**
 *
 * @author Sicut
 */
public class PDFDocument {
    private PDDocument document;
//    private PDPage pg;
//    private PDFTextStripper ps;
    private boolean documentLoaded;
    private final String usedFont;
    private String filePath;
    
    public PDFDocument(){
        documentLoaded = false;
        usedFont = "/bulsort/resources/fonts/times.ttf";
        filePath = "";
    }
    
    public PDFDocument(File f){
        this();
        setFile(f);
    }
    
    public PDFDocument(String filePath){
        this();
        this.filePath = filePath;
    }
    
    public PDDocument getDocument() {
        return document;
    }
    
    public final void setFile(File f){
        filePath = "";
        if ( f.exists() )
            filePath = f.getAbsolutePath();
    }
    
    public boolean load(){
        if ( documentLoaded )   return true;
        try {
            document = PDDocument.load(new File(filePath));
            documentLoaded = true;
        }
        catch(IOException e){
            documentLoaded = false;
        }
        return documentLoaded;
    }
    
    public boolean reload(){
        try {
            document = PDDocument.load(new File(filePath));
            documentLoaded = true;
        }
        catch(IOException e){
            documentLoaded = false;
        }
        return documentLoaded;
    }
    
    public void create() {
        document = new PDDocument();
    }
    
    public boolean isLoaded() {
        return documentLoaded;
    }
    
    public String getPageContent(int page){
        try {
            String cnt;
            PDFTextStripper ps = new PDFTextStripper();
            ps.setStartPage(page + 1);
            ps.setEndPage(page + 1);
            cnt = ps.getText(document);
            return cnt;
        }
        catch(IOException e){
            return "";
        }
    }
    
    public PDPage getPage(int page){
        PDPage pg = document.getPage(page);
        return pg;
    }
    
    public int getNumberOfPages(){
        try {
            int n = document.getNumberOfPages();
            return n;
        }
        catch (Exception e){
            return -1;
        }
    }
    
    public String getLocation(){
        return (new File(filePath)).getParent();
    }
    
    public String getName(){
        String name = (new File(filePath)).getName();
        return name.substring(0, name.indexOf(".pdf"));
    }
    
    public boolean appendImage(PDPage page, PDImageXObject img, float x, float y){
        try (PDPageContentStream pdpcs = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcs.drawImage(img, x, y);
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean appendImage(PDPage page, PDImageXObject img, float x, float y, float width, float height){
        try (PDPageContentStream pdpcs = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcs.drawImage(img, x, y, width, height);
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean appendText(int page, String text, float x, float y){
        if (page >= document.getNumberOfPages()) return false;
        PDPage pg = document.getPage(page);
        try (PDPageContentStream pdpcs = new PDPageContentStream(document, pg, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcs.beginText();
            pdpcs.newLineAtOffset(x, y);
            pdpcs.setFont(PDType1Font.HELVETICA, 12);
            pdpcs.showText(text);
            pdpcs.endText();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean appendText(PDPage page, String text, float x, float y){
        try (PDPageContentStream pdpcs = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcs.beginText();
            pdpcs.newLineAtOffset(x, y);
            pdpcs.setFont(PDType1Font.HELVETICA, 12);
            pdpcs.showText(text);
            pdpcs.endText();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean appendText(PDPage page, String text, float x, float y, int fntSize){
        try (PDPageContentStream pdpcs = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcs.beginText();
            pdpcs.newLineAtOffset(x, y);
            pdpcs.setFont(PDType1Font.HELVETICA, fntSize);
            pdpcs.showText(text);
            pdpcs.endText();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean appendArabicText(int page, String text, float x, float y, int fntSize){
        if (page >= document.getNumberOfPages()) return false;
        PDPage pg = document.getPage(page);
        try ( PDPageContentStream pdpcs = new PDPageContentStream(document, pg, PDPageContentStream.AppendMode.APPEND, false) ) {
            ArabicShaping aras = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
            pdpcs.beginText();
            pdpcs.newLineAtOffset(x, y);
            pdpcs.setFont(PDType0Font.load(document, getClass().getResource(usedFont).openStream()), fntSize);
            pdpcs.showText(new StringBuilder(aras.shape(text)).reverse().toString());
            pdpcs.endText();
            return true;
        }
        catch (IOException | ArabicShapingException e){
            System.out.println(e);
            return false;
        }
    }
    
    public boolean appendArabicText(PDPage page, String text, float x, float y, int fntSize){
        try ( PDPageContentStream pdpcs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false) ) {
            ArabicShaping aras = new ArabicShaping(ArabicShaping.LETTERS_SHAPE);
            pdpcs.beginText();
            pdpcs.newLineAtOffset(x, y);
            pdpcs.setFont(PDType0Font.load(document, getClass().getResource(usedFont).openStream()), fntSize);
            pdpcs.showText(new StringBuilder(aras.shape(text)).reverse().toString());
            pdpcs.endText();
            return true;
        }
        catch (IOException | ArabicShapingException e){
            System.out.println(e);
            return false;
        }
    }
    
    public boolean appendText(int page, String text, float degree, float x, float y){
        if (page >= document.getNumberOfPages()) return false;
        Matrix mx;
        double angle, cosAngle, sinAngle;
        PDPage pg = document.getPage(page);
        try (PDPageContentStream pdpcss = new PDPageContentStream(document, pg, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcss.beginText();
            pdpcss.setFont(PDType1Font.TIMES_ROMAN, 12);
            mx = new Matrix();
            angle = degree * Math.PI / 180;
            cosAngle = Math.cos(angle);
            sinAngle = Math.sin(angle);
            mx.rotate(angle);
            mx.translate((float) (x * cosAngle + y * sinAngle), (float) (y * cosAngle - x * sinAngle));
            pdpcss.setTextMatrix(mx);
            pdpcss.showText(text);
            pdpcss.endText();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean appendText(PDPage page, String text, float degree, float x, float y){
        Matrix mx;
        double angle, cosAngle, sinAngle;
        try (PDPageContentStream pdpcss = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcss.beginText();
            pdpcss.setFont(PDType1Font.TIMES_ROMAN, 12);
            mx = new Matrix();
            angle = degree * Math.PI / 180;
            cosAngle = Math.cos(angle);
            sinAngle = Math.sin(angle);
            mx.rotate(angle);
            mx.translate((float) (x * cosAngle + y * sinAngle), (float) (y * cosAngle - x * sinAngle));
            pdpcss.setTextMatrix(mx);
            pdpcss.showText(text);
            pdpcss.endText();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
    public boolean appendText(PDPage page, String text, float degree, float x, float y, int fntSize){
        Matrix mx;
        double angle, cosAngle, sinAngle;
        try (PDPageContentStream pdpcss = new PDPageContentStream(document, page, 
                PDPageContentStream.AppendMode.APPEND, false)) {
            pdpcss.beginText();
            pdpcss.setFont(PDType1Font.TIMES_ROMAN, fntSize);
            mx = new Matrix();
            angle = degree * Math.PI / 180;
            cosAngle = Math.cos(angle);
            sinAngle = Math.sin(angle);
            mx.rotate(angle);
            mx.translate((float) (x * cosAngle + y * sinAngle), (float) (y * cosAngle - x * sinAngle));
            pdpcss.setTextMatrix(mx);
            pdpcss.showText(text);
            pdpcss.endText();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }
    
//    public boolean saveAndClose(String path){
//        try {
//            document.save(path);
//            document.close();
//            documentLoaded = false;
//        }
//        catch (IOException e){
//            System.out.println("Hi am closing after saving..." + e);
//            documentLoaded = true;
//        }
//        return !documentLoaded;
//    } 
    
    public boolean save(String path){
        try {
            document.save(path);
            return true;
        }
        catch (IOException e){
           return false;
        }
    } 
    
    public void close(){
        try {
            document.close();
            documentLoaded = false;
        }
        catch (IOException e){
            documentLoaded = true;
        }
    }
    
//    public boolean isNull(){
//        return (pdfFile == null);
//    }
    
    public PDPage clonePage(int i){
        PDPage srcPg = document.getPage(i);
        PDPage np = new PDPage(srcPg.getCOSObject());
        List<PDStream> x = new ArrayList();
        Iterator<PDStream> y = srcPg.getContentStreams();
        while (y.hasNext()){
            x.add(y.next());
        }
        np.setContents(x);
        return np;
    }
    
    public PDPage clonePage(int i, boolean deleteWM){
        PDPage srcPg = document.getPage(i);
        PDPage np = new PDPage(srcPg.getCOSObject());
        PDStream newContents = new PDStream( document );
        try ( OutputStream out = newContents.createOutputStream(COSName.FLATE_DECODE) ) {
            PDFStreamParser parser = new PDFStreamParser(srcPg);
            parser.parse();
            List<Object> tokens = parser.getTokens();
            List<Object> newTokens = new ArrayList<>();
            int ttt = 0;
            for (Object token : tokens) {
                if( deleteWM && token instanceof Operator && ((Operator)token).getName().equalsIgnoreCase("DO")) {
                    if ( ++ttt > 14 )
                        newTokens.add( token );
                }
                else
                    newTokens.add( token );
            }
            ContentStreamWriter writer = new ContentStreamWriter( out );
            writer.writeTokens( newTokens );
            np.setContents( newContents );
        }
        catch (IOException ex) { }
        return np;
    }
    
    public void addPage(PDPage pg) {
        document.addPage(pg);
    }

}
