package com.demo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.google.common.base.Joiner;
import com.google.common.hash.Hashing;

public class FileUtil2 {

    public static final char FILE_SEPARATOR = File.separatorChar;
    public static final Joiner FILE_SEPARATOR_JOINER = Joiner.on(FILE_SEPARATOR);
    public static final String SYSTEM_TEMPDIR_PATH = trimEndSeparator(System.getProperty("java.io.tmpdir"));
    public static final String EMPTY_BLOCK_ID = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

    public static String formatName(String name, int backupNo, boolean isBackupName) {
        if (StringUtil.isNullOrEmpty(name)) {
            return null;
        }
        int index = name.lastIndexOf('.');
        String ext = "";
        String nameWithOutExt = name;
        if (index > 0) {
            ext = name.substring(index);
            nameWithOutExt = name.substring(0, index);
        }
        if (isBackupName) {
            index = name.lastIndexOf('_');
            if (index != -1) {
                nameWithOutExt = nameWithOutExt.substring(0, index);

            }
        }
        return String.format("%s_%s%s", nameWithOutExt, backupNo, ext);
    }

    public static String trimEndSeparator(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        return path.replaceAll(StringUtil.concat("\\Q", FILE_SEPARATOR, "\\E+$"), "");
    }

    public static String concatPath(String parentPath, Object... parts) {
        if (parentPath == null || parentPath.isEmpty()) {
            return FILE_SEPARATOR_JOINER.join(parts);
        }
        parentPath = trimEndSeparator(parentPath);
        if (parts == null || parts.length <= 0) {
            return parentPath;
        }
        return StringUtil.concat(parentPath, FILE_SEPARATOR, FILE_SEPARATOR_JOINER.join(parts));
    }

    public static String encodeSha256(File file) {
        if (file != null) {
            try {
                return com.google.common.io.Files.asByteSource(file).hash(Hashing.sha256()).toString();
            } catch (IOException ioe) { // ignore ioe
            }
        }
        return "";
    }

    public static void deleteFilesRecursive(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void deleteFilesRecursiveQuietly(Path path) {
        try {
            deleteFilesRecursive(path);
        } catch (Exception ex) { // ignore ex
        }
    }

    public static void deleteFile(Path path, boolean deleteIfExist) throws IOException {
        if (deleteIfExist) {
            Files.deleteIfExists(path);
        } else {
            Files.delete(path);
        }
    }

    public static void deleteFileQuietly(Path path) {
        try {
            Files.delete(path);
        } catch (Exception ex) { // ignore ex
        }
    }

    public static void mkParentDirs(File file) {
        File parentFile = null;
        if (file != null && (parentFile = file.getParentFile()) != null && parentFile.exists() == false) {
            parentFile.mkdirs();
        }
    }

}