package com.ashrafzyanov.scanner.serialization;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Disk {
    private int id;                                                          
    private int id_type;
    private String name;

    private void DiskInfo(ResultSet data) {
        try {
            this.id = data.getInt(1);
            this.id_type = data.getInt(2);
            this.name = data.getString(3);
        }
        catch(SQLException e){}
    }

    public Disk(ResultSet data) {
        DiskInfo(data);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getIdType() {
        return this.id_type;
    }

    public String getName() {
        return this.name;
    }
    
}
