package com.sfccn.pdftool.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class FileUtil {

    public static int BUFFER_SIZE = 2048;
    public static List<String> unZip(File zipfile, String destDir)
            throws Exception {
        if (StringUtils.isBlank(destDir)) {
            destDir = zipfile.getParent();
        }
        destDir = destDir.endsWith(File.separator) ? destDir : destDir
                + File.separator;
        ZipArchiveInputStream is = null;
        List<String> fileNames = new ArrayList<String>();

        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(
                    new FileInputStream(zipfile), BUFFER_SIZE));
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                fileNames.add(entry.getName());
                if (entry.isDirectory()) {
                    File directory = new File(destDir, entry.getName());
                    directory.mkdirs();
                } else {
                    OutputStream os = null;
                    try {
                        os = new BufferedOutputStream(new FileOutputStream(
                                new File(destDir, entry.getName())),
                                BUFFER_SIZE);
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }

        return fileNames;
    }

    public static List<String> unZip(String zipfile, String destDir)
            throws Exception {
        File zipFile = new File(zipfile);
        return unZip(zipFile, destDir);
    }

    public static void zip(File[] files, String zipFilePath) {
        if (files != null && files.length > 0) {
            ZipArchiveOutputStream zaos = null;
            File f = new File(zipFilePath);
            if(f.isDirectory())
            {
                System.out.println("isDirectory");
                return;
            }
            if(f.exists())
            {
                f.delete();
            }
            try {
                File zipFile = new File(zipFilePath);
                zaos = new ZipArchiveOutputStream(zipFile);
                zaos.setUseZip64(Zip64Mode.AsNeeded);
                int index = 0;
                for (File file : files) {
                    if (file != null) {
//                        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(
//                                file, new File(file.getParent()).getName()+File.separator+file.getName());
                        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(
                                file, file.getName());
                        zaos.putArchiveEntry(zipArchiveEntry);
                        InputStream is = null;
                        try {
                            is = new BufferedInputStream(new FileInputStream(
                                    file));
                            byte[] buffer = new byte[1024 * 5];
                            int len = -1;
                            while ((len = is.read(buffer)) != -1) {
                                // 把缓冲区的字节写入到ZipArchiveEntry
                                zaos.write(buffer, 0, len);
                            }
                            // Writes all necessary data for this entry.
                            zaos.closeArchiveEntry();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            if (is != null)
                                is.close();
                        }

                    }

                }

                zaos.finish();

            } catch (Exception e) {

                throw new RuntimeException(e);

            } finally {

                try {

                    if (zaos != null) {

                        zaos.close();

                    }

                } catch (IOException e) {

                    throw new RuntimeException(e);

                }

            }

        }

    }

    public static void main(String[] args) throws Exception {


        String f  = "C:/20161202/notepad_cache/test/notepad_cache - 副本";
        File file = new File(f);

        File[] files = new File[100];
        int index = 0;
        for(String ff : file.list())
        {
            files[index++] = new File(f+File.separator+ff);

        }
        zip(files, f+".zip");
        /*
        String f  = "C:/20161202/notepad_cache/test/notepad_cache - 副本";
        unZip(f+"1.zip", "C:/1202/notepad_cache/test/");*/
    }



}
