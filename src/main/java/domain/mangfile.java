package domain;

import java.io.*;
import java.util.*;

public class mangfile {

    public enum FileType {
  
    CUSTOMER("src/main/java/resources/customerslist.dat"),
    PROPERTY("src/main/java/resources/propertieslist.dat"),
    APPOINTMENT("src/main/java/resources/appointmentslist.dat");

    private final String path;

    // "Constructor" للـ Enum لتحديد المسار لكل نوع
    FileType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
  public static <T> void saveToFile(FileType fileType, ArrayList<T> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileType.getPath()))) {
            out.writeObject(list);
        } catch (IOException e) {
            System.err.println("Error saving to: " + fileType.getPath());
            e.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> loadFromFile(FileType fileType) {
        File file = new File(fileType.getPath());
        if (!file.exists()) return new ArrayList<>(); // إذا لم يوجد الملف، ارجع قائمة فارغة

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from: " + fileType.getPath());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}