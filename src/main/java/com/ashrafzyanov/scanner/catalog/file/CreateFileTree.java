package com.ashrafzyanov.scanner.catalog.file;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.IOException;
import org.apache.tools.zip.*;
import java.util.Enumeration;
import java.text.*;
import java.util.Locale;
import javax.swing.filechooser.FileSystemView;
import Catalog.DataBaseJob.*;

public class CreateFileTree {

    Connection connection = null;
    Statement statement = null;

    private int id;
    private int id_disk = -1;

    public int getNumberDisk() {
        return this.id_disk;
    }

    public int getCurrID() {
        return this.id;
    }

    public CreateFileTree(Connection connection) throws SQLException {
        this.connection = connection;
        statement = this.connection.createStatement();
    }

    private String CreateQueryInsertFileObject(int id, int id_parrent, int id_disk, FileObject FileInfo) {
        String resultQuery;

        resultQuery = "INSERT INTO TreeFiles VALUES (" + id + "," + id_parrent + "," + id_disk + ",";

        int id_type = 0;
        if (FileInfo.isDirectory()) {
            id_type = 1;
        } else {
            if (FileInfo.isArchiv()) {
                id_type = 2;
            }
        }
        resultQuery = resultQuery + id_type + ",";

        resultQuery = resultQuery + "\"" + FileInfo.getName() + "\",";
        resultQuery = resultQuery + (FileInfo.getTypeFile() == null ? "null" : ("\"" + FileInfo.getTypeFile() + "\"")) + ",";

        if (FileInfo.getDateModif() == -1) {
            resultQuery = resultQuery + "null,";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            resultQuery = resultQuery + "\"" + sdf.format(new Date(FileInfo.getDateModif())) + "\",";
        }

        resultQuery = resultQuery + (FileInfo.getFileSize() == -1 ? "null" : FileInfo.getFileSize());
        resultQuery = resultQuery + ",1," + (FileInfo.isHidden() ? 1 : 0) + ");";

        return resultQuery;
    }

    private String CreateQueryInsertArhiv(int id, int id_parrent, int id_disk, String name, ZipEntry ArhivInfo,
            boolean isDir) {
        String resultQuery;
        String nameF;
        String typeF;

        if (isDir) {
            nameF = name;
            typeF = "<DIR>";
        } else {
            nameF = ParseNameFile.getNameFile(name);
            typeF = ParseNameFile.TypeFile(name);
        }

        resultQuery = "INSERT INTO TreeFiles VALUES (" + id + "," + id_parrent + "," + id_disk + ",";
        int id_type = 0;
        if (isDir) {
            id_type = 1;
        } else {
            if ((typeF != null) && ParseNameFile.TypeArchiv(typeF)) {
                id_type = 2;
            }
        }
        resultQuery = resultQuery + id_type + ",";

        resultQuery = resultQuery + "\"" + nameF + "\",";
        resultQuery = resultQuery + (typeF == null ? "null" : "\"" + typeF + "\"") + ",";

        if (ArhivInfo.getTime() == -1) {
            resultQuery = resultQuery + "null,";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            resultQuery = resultQuery + "\"" + sdf.format(new Date(ArhivInfo.getTime())) + "\",";
        }

        resultQuery = resultQuery + (ArhivInfo.getCompressedSize() == -1 ? "null" : ArhivInfo.getCompressedSize());
        resultQuery = resultQuery + ", 1, 0);";

        return resultQuery;
    }

    private int getNumberNext(String nameTabl, String nameField) throws SQLException {
        String Query;
        int numberDisk = 0;
        int res1 = 0;
        int offset = 0;
        Query = "SELECT " + nameField + " FROM " + nameTabl + " LIMIT 1 " + "OFFSET " + offset + ";";
        ResultSet data = statement.executeQuery(Query);
        if (data.next()) {
            res1 = (data.getInt(1));
            if (res1 > 0)
                return 0;
            else {
                offset++;
                Query = "SELECT " + nameField + " FROM " + nameTabl + " LIMIT 1 " + "OFFSET " + offset + ";";
                data = statement.executeQuery(Query);
                while (data.next()) {
                    int res2 = data.getInt(1);
                    if ((res2 - res1) > 1)
                        return (res1 + 1);
                    else {
                        res1 = res2;
                        offset++;
                        Query = "SELECT " + nameField + " FROM " + nameTabl + " LIMIT 1 " + "OFFSET " + offset + ";";
                        data = statement.executeQuery(Query);
                    }
                }
                data.close();
                return (res1 + 1);
            }
        }
        data.close();
        return 0;
    }

    private void ScanindMemory(int id_disk, int parrent, FileObject fo) throws SQLException {
        id++;
        statement.executeUpdate(CreateQueryInsertFileObject(id, parrent, id_disk, fo));
        if (fo.isDirectory()) {
            try {
                parrent = id;
                FileObject[] fo2 = fo.getChildren();
                for (int i = 0; i < fo2.length; i++) {
                    ScanindMemory(id_disk, parrent, fo2[i]);
                }
            } catch (NullPointerException e) {
                statement.executeUpdate(
                        "UPDATE treefiles SET readCan = 0 where id = " + id + " and id_disk = " + id_disk + ";");
            }
        } else {
            if (fo.isArchiv() && fo.getTypeFile().equalsIgnoreCase("zip")) {
                try {
                    ScanArhiv(fo, id, id_disk);
                } catch (java.io.IOException e) {
                    statement.executeUpdate(
                            "UPDATE treefiles SET readCan = 0 where id = " + id + " and id_disk = " + id_disk + ";");
                }
            }
        }
    }

    public void Scaning(File file) throws Exception {
        this.id_disk = -1;
        // file.replace('\\', '/');
        FileObject fo = new FileObject(file);
        if (fo.getFile().exists()) {
            if (fo.isDirectory()) {
                try {
                    this.id = 0;
                    FileObject[] f = fo.getChildren();
                    try {
                        int id_disk = getNumberNext("disk", "id");
                        int id_type = getDiskTypeID(fo.getFile());
                        statement.executeUpdate("INSERT INTO disk VALUES (" + id_disk + "," + id_type + ","
                                + "'My Disk Number " + id_disk + "');");
                        this.id_disk = id_disk;
                        for (int i = 0; i < f.length; i++) {
                            this.ScanindMemory(id_disk, 0, f[i]);
                        }
                    } catch (SQLException e) {
                        throw new java.lang.Exception(e.getMessage());
                    }
                } catch (NullPointerException e) {
                    throw new Exception("Нет доступа к диску");
                }
            } else
                throw new Exception("Неверные исходные данные");
        } else
            throw new Exception("Выбранный диск отстуствует");
    }

    private void ResetScanindMemory(int id_disk, int parrent, FileObject fo) throws SQLException {
        ResultSet data = statement
                .executeQuery(
                        "SELECT id FROM treefiles WHERE id_disk = " + id_disk + " and id_parrent = " + parrent
                                + " and Name = \"" + fo.getName() + "\" and id_type = "
                                + (fo.isDirectory() ? "1"
                                        : ((fo.isArchiv() ? "2" : "0") + " and rash = "
                                                + (fo.getTypeFile() == null ? "null" : "\"" + fo.getTypeFile() + "\""))
                                                + ";"));

        if (data.next()) {
            if (fo.isDirectory()) {
                parrent = data.getInt(1);
                try {
                    FileObject[] f = fo.getChildren();
                    for (int i = 0; i < f.length; i++)
                        ResetScanindMemory(id_disk, parrent, f[i]);
                } catch (NullPointerException e) {
                }
            }
        } else {
            ResultSet id_num = statement.executeQuery("Select MAX(id) FROM treefiles WHERE id_disk = " + id_disk + ";");

            if (id_num.next() && id_num.getObject(1) != null) {
                this.id = id_num.getInt(1);
            } else
                this.id = 0;

            if (fo.isDirectory()) {
                this.ScanindMemory(id_disk, parrent, fo);
            } else {
                statement.executeUpdate(CreateQueryInsertFileObject(this.id + 1, parrent, id_disk, fo));
                if (fo.isArchiv() && fo.getTypeFile().equalsIgnoreCase("zip")) {
                    try {
                        this.id++;
                        ScanArhiv(fo, id, id_disk);
                    } catch (java.io.IOException e) {
                        statement.executeUpdate("UPDATE treefiles SET readCan = 0 where id = " + id + " and id_disk = "
                                + id_disk + ";");
                        this.id--;
                    }
                }
            }
        }
    }

    public void ResetScaningDisk(String file, int id_disk) throws Exception {
        try {
            ResultSet data = statement.executeQuery("SELECT count(*) FROM disk WHERE id = " + id_disk + ";");
            data.next();

            if (data.getInt(1) != 0) {
                this.id_disk = -1;
                file.replace('\\', '/');
                FileObject fo = new FileObject(file);
                if (fo.getFile().exists()) {
                    if (fo.isDirectory()) {
                        try {
                            FileObject[] f = fo.getChildren();

                            this.id_disk = id_disk;
                            for (int i = 0; i < f.length; i++) {
                                ResetScanindMemory(id_disk, 0, f[i]);
                            }
                        } catch (NullPointerException e) {
                            throw new Exception("Нет доступа к диску");
                        }
                    } else
                        throw new Exception("Не верные входные данные");
                } else
                    throw new Exception("Выбранный диск отсуствует");
            } else
                throw new Exception("Данного диска нету в базе данных");
        } catch (SQLException e) {
            throw new Exception("Ошибка выполнения запроса к базе данных");
        }
    }

    public void ScanArhiv(FileObject f, int parrent, int id_disk) throws java.io.IOException, SQLException {
        ZipFile zf = new ZipFile(f.getFile(), "CP866");
        Enumeration e = zf.getEntries();
        ZipEntry ze;
        int buf_control;
        int getID_Par = parrent;
        while (e.hasMoreElements()) {
            ze = (ZipEntry) e.nextElement();
            String[] fileA = ze.getName().split("\\/");
            getID_Par = parrent;
            buf_control = this.id;
            for (int i = 0; i < fileA.length; i++) {
                boolean isDir = i < fileA.length - 1 || ze.isDirectory();
                getID_Par = getID(fileA[i], getID_Par, id_disk, ze, isDir);
            }
            if (buf_control == this.id) {
                String UpdateQuery = "UPDATE treefiles SET id_type = 1, rash = \"<DIR>\", dateModif = ";
                if (ze.getTime() == -1)
                    UpdateQuery = UpdateQuery + "null";
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                    UpdateQuery = UpdateQuery + "\"" + sdf.format(new Date(ze.getTime())) + "\"";
                }
                UpdateQuery = UpdateQuery + ", size = "
                        + (ze.getCompressedSize() == -1 ? "null" : ze.getCompressedSize()) + " where id_disk = "
                        + id_disk + " and id = " + getID_Par + ";";

                statement.executeUpdate(UpdateQuery);
            }
        }
    }

    private int getID(String name, int id_parrent, int id_disk, ZipEntry ze, boolean isDirectory) throws SQLException // Получение
                                                                                                                      // содержимого
                                                                                                                      // архива
    {
        String Query = "SELECT id FROM treefiles WHERE id_disk = " + id_disk + " and id_parrent = " + id_parrent
                + " and name = \"" + (isDirectory ? name : ParseNameFile.getNameFile(name)) + "\" and id_type =";

        if (isDirectory) {
            Query = Query + "1";
        } else {
            String typeF = ParseNameFile.TypeFile(name);
            if (typeF != null && ParseNameFile.TypeArchiv(typeF))
                Query = Query + "2";
            else
                Query = Query + "0";
            Query = Query + " and rash = " + (typeF == null ? "null" : "\"" + typeF + "\"");
        }
        Query = Query + ";";

        ResultSet data = statement.executeQuery(Query);

        if (data.next()) {
            return (data.getInt(1));
        }

        statement.executeUpdate(CreateQueryInsertArhiv(++this.id, id_parrent, id_disk, name, ze, isDirectory));
        return (this.id);
    }

    private byte getDiskTypeID(File in) {
        String[] TypeHDD = { "Локальный диск" };
        String[] TypeFlash = { "Съемный диск" };
        String[] TypeCD = { "CD-Дисковод" };
        String[] TypeFolder = { "Папка с файлами" };
        int type;
        FileSystemView fs = FileSystemView.getFileSystemView();
        String nameType = fs.getSystemTypeDescription(in);
        int i = 0;
        while (i < TypeHDD.length) {
            if (nameType.compareToIgnoreCase(TypeHDD[i]) == 0) {
                return 2;
            }

            i++;
        }

        i = 0;
        while (i < TypeFlash.length) {
            if (nameType.compareToIgnoreCase(TypeFlash[i]) == 0) {
                return 4;
            }
            i++;
        }

        i = 0;
        while (i < TypeCD.length) {
            if (nameType.compareToIgnoreCase(TypeCD[i]) == 0) {
                return 1;
            }
            i++;
        }

        i = 0;
        while (i < TypeFolder.length) {
            if (nameType.compareToIgnoreCase(TypeFolder[i]) == 0) {
                return 5;
            }
            i++;
        }

        return 0;
    }
}
