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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.TempFile;

public class XlsxUtils {
    public static InputStream decrypt(final InputStream inputStream, final String pwd) throws Exception {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            EncryptionInfo info = new EncryptionInfo(fs);
            Decryptor d = Decryptor.getInstance(info);
            if (!d.verifyPassword(pwd)) {
                throw new RuntimeException("incorrect password");
            }
            return d.getDataStream(fs);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public static void checkTempFiles() throws IOException {
        String tmpDir = System.getProperty(TempFile.JAVA_IO_TMPDIR) + "/poifiles";
        File tempDir = new File(tmpDir);
        if(tempDir.exists()) {
            String[] tempFiles = tempDir.list();
            if(tempFiles.length > 0) {
                System.out.println("found files in poi temp dir " + tempDir.getAbsolutePath());
                for(String filename : tempDir.list()) {
                    System.out.println("file: " + filename);
                }
            }
        } else {
            System.out.println("unable to find poi temp dir");
        }
    }
    
}
