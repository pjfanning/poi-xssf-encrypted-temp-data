package com.github.poi;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadPasswordProtectedXlsx {

    public static void main(String[] args) {
        try {
            if(args.length != 2) {
                throw new Exception("Expected 2 params: filename and password");
            }
            String filename = args[0];
            String password = args[1];
            try(FileInputStream fis = new FileInputStream(filename);
                    InputStream unencryptedStream = XlsxUtils.decrypt(fis, password);
                    AesZipFileZipEntrySource source = AesZipFileZipEntrySource.createZipEntrySource(unencryptedStream);
                    OPCPackage pkg = OPCPackage.open(source);
                    XSSFWorkbook workbook = new XSSFWorkbook(pkg)) {
                System.out.println("sheet count: " + workbook.getNumberOfSheets());
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }


}
