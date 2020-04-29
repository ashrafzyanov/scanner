package com.ashrafzyanov.scanner.ui;

import javax.swing.table.*;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import DataBaseFileSerialization.*;
import java.util.HashMap;
import java.util.Map;

public class FileTableView extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private Connection connect = null;
    private ArrayList<File> fileList = null;
    private static final int FILE_NAME_COLUMN = 0;
    private static final int FILE_TYPE_COLUMN = 1;
    private static final int FILE_DATE_COLUMN = 2;
    private static final int FILE_SIZE_COLUMN = 3;
    private static final String HEADER_NAME = "Имя файла";
    private static final String HEADER_TYPE = "Тип";
    private static final String HEADER_DATE = "Дата изменения";
    private static final String HEADE_SIZE = "Размер (Байт)";
    private static final Map<Integer, String> TABLE_HEADER = new HashMap<Integer, String>() {
        {
            put(FILE_NAME_COLUMN, HEADER_NAME);
            put(FILE_TYPE_COLUMN, HEADER_TYPE);
            put(FILE_DATE_COLUMN, HEADER_DATE);
            put(FILE_SIZE_COLUMN, HEADE_SIZE);
        }
    };

    public FileTableView(Connection connect) {
        this.connect = connect;
        fileList = new ArrayList();
    }

    public int getColumnCount() {
        return TABLE_HEADER.size();
    }

    public int getRowCount() {
        return fileList.size();
    }

    public String getColumnName(int col) {
        return TABLE_HEADER.get(col);

    }

    public void getFiles(Object obj) {
        int id_disk;
        int id_parrent;
        if (obj instanceof Disk) {
            Disk ds = (Disk) obj;
            id_disk = ds.getId();
            id_parrent = 0;
        } else {
            if (obj instanceof File) {
                File fs = (File) obj;
                id_disk = fs.getIdDisk();
                id_parrent = fs.getId();
            } else {
                return;
            }
        }
        String Query;
        Query = "SELECT * FROM treefiles WHERE id_parrent = " + id_parrent + " and id_disk = " + id_disk + ";";
        Query = Query + ";";
        try {
            Statement stm = connect.createStatement();
            ResultSet data = stm.executeQuery(Query);
            setData(data);
        } catch (SQLException e) {
        }
    }

    public File getParentFiles(File fs) {
        String Query;
        Query = "SELECT * FROM treefiles WHERE id = " + fs.getIdParrent() + " and id_disk = " + fs.getIdDisk() + ";";
        try {
            Statement stm = connect.createStatement();
            ResultSet data = stm.executeQuery(Query);
            data.next();
            return new File(data);
        } catch (SQLException e) {
        }
        return null;
    }

    public void delTable() {
        fileList.clear();
        fireTableStructureChanged();
    }

    public File getObject(int col) {
        return (fileList.get(col));
    }

    private void setData(ResultSet dat) {
        fileList.clear();
        try {
            while (dat.next()) {
                fileList.add(new File(dat));
            }
            fireTableStructureChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return fileList.get(row).getName();
            case 1: {
                if (fileList.get(row).getIdType() == 1) {
                    return "Папка с файлами";
                } else {
                    return (fileList.get(row).getExt() == null ? "Неизвестный" : "Файл  " + fileList.get(row).getExt());
                }
            }
            case 2:
                if (fileList.get(row).getDateModif() != null) {
                    java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm");
                    return f.format(fileList.get(row).getDateModif());
                }
                return "Не определено";
            case 3: {
                if (fileList.get(row).getIdType() == 1) {
                    return "<DIR>";
                } else {
                    return FormatSize.formatSize(fileList.get(row).getSize());
                }
            }
            case 4:
                return fileList.get(row).getId();

        }
        return null;
    }

    public int getID(int row) {
        return fileList.get(row).getId();
    }

}
