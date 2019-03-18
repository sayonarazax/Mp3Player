package utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;


public class FileUtils {
    public static String getFileNameWithoutExtension(String fileName){
        File file = new File(fileName);
        int index = file.getName().lastIndexOf('.');
        if (index > 0 && index <= file.getName().length() - 2)
            return file.getName().substring(0, index);
        return "noname";
    }

    public static String getFileExtension(File file){
        String ext = null;
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i > 0 && i < name.length()-1){
            ext = name.substring(i+1).toLowerCase();
        }
        return ext;
    }

    public static void addFileFilter(JFileChooser jfc, FileFilter ff){
        jfc.removeChoosableFileFilter(jfc.getFileFilter());
        jfc.setFileFilter(ff);
        jfc.setSelectedFile(new File(""));
    }

    public static void serialize(Object obj, String fileName){
        try {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Object deserialize(String fileName){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object ts = (Object) ois.readObject();
            fis.close();
            return ts;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
return null;
    }
}
