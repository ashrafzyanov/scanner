package com.ashrafzyanov.scanner.ui;

import java.sql.*;

public class FormatSize {

    public static String formatSize(long byteSize) {
        if (byteSize >= 1073741824) {
            return (byteSize / 1073741824) + " ГБ";
        } else if (byteSize >= 1048576) {
            return (byteSize / 1048576) + " МБ";
        } else if (byteSize >= 1024) {
            return (byteSize / 1024) + " КБ";
        }
        return "1 КБ";
    }

    public static String Path_(Connection con, int id_disk, int id) throws SQLException {
        Statement stm = con.createStatement();
        ResultSet result = stm.executeQuery("Select id_parrent, name from treefiles where id_disk = " + id_disk + " and id = " + id + ";");
        String res = "";
        result.next();
        int idNew = result.getInt(1);
        while (idNew != 0) {
            result = stm.executeQuery("Select id_parrent, name from treefiles where id_disk = " + id_disk + " and id = " + idNew + ";");
            result.next();
            idNew = result.getInt(1);
            res = result.getString(2) + "/" + res;
        }
        return res;
    }
}
