/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

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
            XlsxUtils.checkTempFiles();
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
            XlsxUtils.checkTempFiles();
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }


}
