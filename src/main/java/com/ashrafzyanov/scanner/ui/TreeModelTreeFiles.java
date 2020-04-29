package com.ashrafzyanov.scanner.ui;

import javax.swing.tree.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DataBaseFileSerialization.Disk;
import DataBaseFileSerialization.File;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.ImageIcon;

public class TreeModelTreeFiles {

    private DefaultMutableTreeNode root = null;
    private Connection connect = null;

    public TreeModelTreeFiles(Connection connect) {
        try {
            this.connect = connect;
            root = new DefaultMutableTreeNode("Каталог");
        } catch (Exception e) {
        }
    }

    private boolean isChildDiskTreeFiles(int id_disk, int id_parrent) {
        try {
            String Query;
            Query = "SELECT count(id) FROM treefiles WHERE id_disk = " + id_disk + " and id_parrent = " + id_parrent
                    + " and (id_type = 1 or (id_type = 2 and rash = 'zip'));";
            Statement stm = connect.createStatement();
            ResultSet count = stm.executeQuery(Query);
            count.next();
            if (count.getLong(1) == 0) {
                stm.close();
                return false;
            }
            stm.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public DefaultMutableTreeNode getRootDisk() {
        try {
            root.removeAllChildren();
            String Query;
            Query = "SELECT * FROM disk;";
            Statement stm = connect.createStatement();
            ResultSet data = stm.executeQuery(Query);
            while (data.next()) {
                Disk ds = new Disk(data);
                DefaultMutableTreeNode diskNode = new DefaultMutableTreeNode(ds);
                root.add(diskNode);
                if (isChildDiskTreeFiles(ds.getId(), 0))
                    diskNode.add(new DefaultMutableTreeNode(new Boolean(true)));
            }
            stm.close();
            return root;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean getChildren(TreePath value, boolean isHidden) {
        try {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) value.getLastPathComponent();
            if (expance(child) && !child.isRoot()) {
                child.removeAllChildren();
                int id_parrent;
                int id_disk;
                if (value.getPathCount() < 3) {
                    id_parrent = 0;
                    id_disk = ((Disk) child.getUserObject()).getId();
                } else {
                    File FS = ((File) child.getUserObject());
                    id_parrent = FS.getId();
                    id_disk = FS.getIdDisk();
                }

                String Query;
                Query = "SELECT * FROM treefiles WHERE id_parrent = " + id_parrent + " and id_disk = " + id_disk
                        + " and (id_type = 1 or (id_type=2 and rash='zip'))";
                if (isHidden) {
                    Query = Query + " and isHidden = false";
                }
                Query = Query + ";";
                Statement stm = connect.createStatement();
                ResultSet data = stm.executeQuery(Query);
                while (data.next()) {
                    File fs = new File(data);
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(fs);
                    child.add(node);
                    if (isChildDiskTreeFiles(fs.getIdDisk(), fs.getId()))
                        node.add(new DefaultMutableTreeNode(new Boolean(true)));
                }
                stm.close();
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean expance(DefaultMutableTreeNode parrent) {
        DefaultMutableTreeNode flag = (DefaultMutableTreeNode) parrent.getFirstChild();
        if (flag == null)
            return false;
        Object obj = flag.getUserObject();
        if (!(obj instanceof Boolean))
            return false;
        return true;
    }

    public Object getObjectNode(TreePath tp) {
        return ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject();
    }

    public DefaultTreeModel getTreeModel() {
        return (new DefaultTreeModel(root));
    }

    public void DeleteRoot() {
        root.removeAllChildren();
    }

    DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) (path.getLastPathComponent());
    }

    public void reloadChild(TreeModel tm, TreePath tp) {
        ((DefaultTreeModel) tm).reload(getTreeNode(tp));
    }

    public void reloadChild(TreeModel tm) {
        ((DefaultTreeModel) tm).reload(root);
    }

}

class StyleTree extends DefaultTreeCellRenderer {
    public StyleTree() {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        Component result = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        setText(obj.toString());
        if (obj instanceof Boolean) {
            setText("Идёт загрузка...");
            setIcon(new ImageIcon("waiting.png"));
        } else {
            if (obj instanceof Disk) {
                Disk disk = (Disk) obj;
                switch (disk.getIdType()) {
                    case 0:
                        setIcon(new ImageIcon("dvd.png"));
                        break;
                    case 1:
                        setIcon(new ImageIcon("disk.png"));
                        break;
                    case 2:
                        setIcon(new ImageIcon("hdd.png"));
                        break;
                    case 3:
                        setIcon(new ImageIcon("floppy.png"));
                        break;
                    case 4:
                        setIcon(new ImageIcon("flash.png"));
                        break;
                    case 5:
                        setIcon(new ImageIcon("folder.png"));
                        break;
                }
            } else {
                if (obj instanceof File) {
                    File g = (File) obj;
                    if (g.getIdType() == 1) {
                        if (g.getReanCan() == true)
                            setIcon(new ImageIcon("folder.png"));
                        else
                            setIcon(new ImageIcon("not_accessible.png"));
                    } else {
                        if (g.getReanCan() == true)
                            setIcon(new ImageIcon("archive_supported.png"));
                        else
                            setIcon(new ImageIcon("archive_unsupported.png"));
                    }
                } else {
                    if (obj instanceof String) {
                        setIcon(new ImageIcon("database.png"));
                    }
                }
            }
        }
        return result;
    }

}
