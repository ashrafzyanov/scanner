package com.ashrafzyanov.scanner.serialization;

import java.sql.ResultSet;
import java.sql.SQLException;

public class File {

    private int id;
    private int id_parrent;
    private int id_disk;
    private byte id_type;
    private String name;
    private String ext;
    private Object dateModif;
    private long size;
    private boolean readCan;
    private boolean isHidden;

    private void FileInfo(ResultSet data) throws SQLException {
        this.id = data.getInt(1);
        this.id_parrent = data.getInt(2);
        this.id_disk = data.getInt(3);
        this.id_type = data.getByte(4);
        this.name = data.getString(5);
        this.ext = (data.getObject(6) == null ? null : data.getString(6));
        this.dateModif = (data.getObject(7) == null ? null : data.getObject(7));
        this.size = (data.getObject(8) == null ? -1 : data.getLong(8));
        this.readCan = data.getBoolean(9);
        this.isHidden = data.getBoolean(10);
    }

    private void FileInfo(File fs) {
        this.id = fs.id;
        this.id_parrent = fs.id_parrent;
        this.id_disk = fs.id_disk;
        this.id_type = fs.id_type;
        this.name = fs.name;
        this.ext = fs.ext;
        this.dateModif = fs.dateModif;
        this.size = fs.size;
        this.readCan = fs.readCan;
        this.isHidden = fs.isHidden;
    }

    public File(ResultSet data) throws SQLException {
        FileInfo(data);
    }

    public File(File fs) {
        FileInfo(fs);
    }

    public boolean ModifInfo(ResultSet data) {
        try {
            FileInfo(data);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getIdParrent() {
        return this.id_parrent;
    }

    public int getIdDisk() {
        return this.id_disk;
    }

    public byte getIdType() {
        return this.id_type;
    }

    public String getName() {
        return this.name;
    }

    public String getExt() {
        return this.ext;
    }

    public Object getDateModif() {
        return this.dateModif;
    }

    public long getSize() {
        return this.size;
    }

    public boolean getReanCan() {
        return this.readCan;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

}
