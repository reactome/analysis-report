package org.reactome.server.tools.pdf.exporter.playground.pdf;

import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;


/**
 * Created by Byron on 2017/10/21.
 */
public class PDFExporter {

    private static PdfWriter pdfWriter = null;

    public static void export(String token,OutputStream outputStream) throws Exception{
        try {
            pdfWriter = new PdfWriter(outputStream);
            ManipulatePDF.manipulate(token,pdfWriter);
        }catch (Exception exception){
            throw new FileNotFoundException("Could not create outputstream!");
        }
    }

    public static void export(String token, File dest) throws Exception {
        try {
            pdfWriter = new PdfWriter(dest);
            ManipulatePDF.manipulate(token,pdfWriter);
        }catch (Exception exception1){
            throw new FileNotFoundException("Could not create pdf file!");
        }
    }

    public static ByteArrayOutputStream export(String token){
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ManipulatePDF.manipulate(token,new PdfWriter(byteArrayOutputStream));
        return byteArrayOutputStream;
    }

}
