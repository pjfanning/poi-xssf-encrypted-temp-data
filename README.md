# poi-xssf-encrypted-temp-data

When you decrypt the xlsx data using the Decryptor, you can then read the unencrypted data into an OPCPackage before loading into an XSSFWorkbook. OPCPackage.open is overloaded and each approach has different implications.

https://poi.apache.org/apidocs/org/apache/poi/openxml4j/opc/OPCPackage.html#open(java.io.File)

Storing the data in a temp file and loading it from there is efficient because Poi can use the Java ZipFile class to load the data. If you use OPCPackage.open(inputStream) instead, the Poi code will load all the data and decompress it in memory. This saves on having to save the xlsx data in a temp file but at the overhead of using significant memory. This is even more significant if want to stream the data using [XSSFReader](https://poi.apache.org/apidocs/org/apache/poi/xssf/eventusermodel/XSSFReader.html).

In Apache 3.15, you can also perform OPCPackage.open with [ZipEntrySource](https://poi.apache.org/apidocs/org/apache/poi/openxml4j/util/ZipEntrySource.html). You can create a custom version of this class that can store the underlying data in encrypted format. An example appears in the test code for Poi, [AesZipFileZipEntrySource]( https://github.com/apache/poi/blob/trunk/src/ooxml/testcases/org/apache/poi/poifs/crypt/AesZipFileZipEntrySource.java).
