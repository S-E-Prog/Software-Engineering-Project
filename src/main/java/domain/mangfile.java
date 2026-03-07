package domain;

import java.io.*;
import java.util.*;

public class mangfile {
    private static final String admin = "src/main/java/resources/adminslist.dat";
    private static final String customer = "src/main/java/resources/customerslist.dat";
    private static final String seller = "src/main/java/resources/sellerslist.dat";
    private static final String property = "src/main/java/resources/propertieslist.dat";

    public static void savecustomer(ArrayList<user> customlist) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(customer))) {
            out.writeObject(customlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<user> loadcustomer() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(customer))) {
            return (ArrayList<user>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveadmin(ArrayList<user> adminlist) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(admin))) {
            out.writeObject(adminlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<user> loadadmin() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(admin))) {
            return (ArrayList<user>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveseller(ArrayList<user> sellerlist) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(seller))) {
            out.writeObject(sellerlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<user> loadseller() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(seller))) {
            return (ArrayList<user>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveproperty(ArrayList<property> propertylist) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(property))) {
            out.writeObject(propertylist);
        } catch (IOException e) {
            e.printStackTrace();

}
    }

    public static ArrayList<property> loadproperty() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(property))) {
            return (ArrayList<property>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}