package com.cwgsmart.commonutil;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;

public class FilesUtils {
    /**
     * 创建文件
     *
     * @param file 需要创建的文件
     * @return 返回创建文件的结果, 成功或者失败.其中创建文件需要一些基本的权限,
     * 如读取文件夹权限等, 如果没有权限可能会导致创建失败 ,如过文件已存在,返回true
     */
    public static boolean createFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建文件夹
     *
     * @param file 待创建的文件夹
     * @return 创建文件夹结果, 如果文件夹本身已存在, 返回true
     */
    public static boolean createDir(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return file.mkdir();
    }

    /**
     * 删除文件, 如果待删除的是文件夹,会遍历该文件夹下所有文件,逐个删除
     *
     * @param file 待删除的文件
     */
    public static void deleteFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) { //判断是否是文件夹
            File[] files = file.listFiles();//遍历文件夹里面的文件
            for (int i = 0; i < files.length; i++) {
                File subFile = files[i];
                deleteFile(subFile); //递归操作
            }
        } else {
            file.delete();
        }
    }

    /**
     * 重命名文件
     *
     * @param file        待重命名文件
     * @param newFileName 新文件名
     * @return 重命名结果
     */
    public static boolean renameFile(File file, String newFileName) {
        if (file == null || !file.exists()) {
            return false;
        }
        File parentFile = file.getParentFile();
        File newNameFile = new File(parentFile, newFileName);
        //如果重命名的文件已存在,返回false 防止对已有的文件进行误操作
        if (newNameFile.exists()) {
            return false;
        }
        return file.renameTo(newNameFile);
    }


    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(@NotNull String oldPath, @NotNull String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }


    /**
     * 使用文件通道的方式复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */

    public static void copyFile(File s, File t) {

        if (s == null || t == null) {
            return;
        }
        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);

            fo = new FileOutputStream(t);

            in = fi.getChannel();//得到对应的文件通道

            out = fo.getChannel();//得到对应的文件通道

            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                fi.close();

                in.close();

                fo.close();

                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断指定路径下是否存在 指定的名称的文件
     * 注意,Android 系统中判断文件名称是 区分大小写的,与windows不一致
     *
     * @param path     指定的路径
     * @param fileName 指定的文件名称
     * @return 指定路径下是否存在指定名称的文件
     */
    public static boolean fileExist(String path, String fileName) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }

        String[] tempList = file.list();
        for (int i = 0; i < tempList.length; i++) {
            if (fileName.equals(tempList[i])) {
                return true;
            }
        }
        return false;

    }

    /**
     * 往文件中写入字符串
     *
     * @param content  写入的字符串
     * @param file     写入的文件
     * @param isAppend 是否追加写入, 值为false 时会 覆盖原文件的内容
     */
    public static boolean writeStringToFile(String content, File file, boolean isAppend) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        try {
            FileWriter fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readLineFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            return bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从文件中读取一个 字符,  常用于读文本，数字等类型的文件
     *
     * @param file 文件
     * @return 如果文件中还有内容时, 返回 读取到的字符转为String的字符串,如果没读到,返回null
     */
    public static String readCharFromFile(File file) {
        if (file == null || !file.exists()) {
            throw new RuntimeException("file is null or file is not exists");
        }

        Reader reader = null;
        try {
            // 一次读一个字符
            reader = new InputStreamReader(new FileInputStream(file));
            int tempChar;
            if ((tempChar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
                reader.close();
                return String.valueOf((char) tempChar);
            } else {
                reader.close();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     */

    /**
     * 从文件中读取一个字节，常用于读二进制文件，如图片、声音、影像等文件。
     * @param file 文件
     * @return 如果文件中还有内容时, 返回 读取到的字节转为String的字符串,如果没读到,返回null
     */
    public static String readByteFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        InputStream in = null;
        try {
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempByte;
            if ((tempByte = in.read()) != -1) {
                in.close();
                return String.valueOf((char) tempByte);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Return whether the file exists.
     *
     * @param file The file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFileExists(Application application,final File file) {
        if (file == null) return false;
        if (file.exists()) {
            return true;
        }
        return isFileExists(application,file.getAbsolutePath());
    }

    /**
     * Return whether the file exists.
     *
     * @param filePath The path of file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFileExists(Application application,final String filePath) {
        File file = getFileByPath(filePath);
        if (file == null) return false;
        if (file.exists()) {
            return true;
        }
        return isFileExistsApi29(application, filePath);
    }

    private static boolean isFileExistsApi29(Application application,String filePath) {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                Uri uri = Uri.parse(filePath);
                ContentResolver cr = application.getContentResolver();
                AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
                if (afd == null) return false;
                try {
                    afd.close();
                } catch (IOException ignore) {
                }
            } catch (FileNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }


    /**
     * Create a file if it doesn't exist, otherwise delete old file before creating.
     *
     * @param file The file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean createFileByDeleteOldFile(final File file) {
        if (file == null) return false;
        // file exists and unsuccessfully delete then return false
        if (file.exists() && !file.delete()) return false;
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param filePath The path of file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return the file by path.
     *
     * @param filePath The path of file.
     * @return the file
     */
    public static File getFileByPath(final String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    /**
     * Notify system to scan the file.
     *
     * @param file The file.
     */
    public static void notifySystemToScan(Context context, final File file) {
        if (file == null || !file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.parse("file://" + file.getAbsolutePath()));
        context.sendBroadcast(intent);
    }


}
