package com.github.poi;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator;

public class LoadPasswordProtectedXlsxStreaming {
    
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
                    OPCPackage pkg = OPCPackage.open(source)) {
                XSSFReader reader = new XSSFReader(pkg);
                SheetIterator iter = (SheetIterator)reader.getSheetsData();
                int count = 0;
                while(iter.hasNext()) {
                    iter.next();
                    count++;
                }
                System.out.println("sheet count: " + count);
            }
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }
    
    
}
