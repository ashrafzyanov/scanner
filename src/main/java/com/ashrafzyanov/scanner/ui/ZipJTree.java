package com.ashrafzyanov.scanner.ui;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ZipJTree {
  public static void main(String[] args) {
    showWindow("D:\\zipfile.zip");
  }

  private static void showWindow(String file) {
    try {
      DefaultTreeModel treeModel = createModel(new File(file));
      JTree tree = new JTree(treeModel);
      JFrame frame = new JFrame(file);
      frame.getContentPane().add(new JScrollPane(tree));
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    } catch (Exception e) {
      JOptionPane.showConfirmDialog(null, e.getMessage());
    }
  }

  public static DefaultTreeModel createModel(File file) {
    if (!file.exists() || !file.isFile()) {
      throw new IllegalArgumentException("File " + file + " does not exist or not a file");
    }
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(file);
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      DefaultTreeModel treeModel = new DefaultTreeModel(new DefaultMutableTreeNode(file.getName(), true));
      while (entries.hasMoreElements()) {
        ZipEntry zipEntry = entries.nextElement();
        addEntry(treeModel, zipEntry);
      }
      return treeModel;
    } catch (IOException e) {
      throw new RuntimeException("Error reading ZIP file", e);
    } finally {
      safeClose(zipFile);
    }
  }

  private static void addEntry(DefaultTreeModel treeModel, ZipEntry zipEntry) {
    String[] path = zipEntry.getName().split("\\/");
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getRoot();
    for (int i = 0; i < path.length; i++) {
      String name = path[i];
      boolean isDirectory = i < path.length - 1 || zipEntry.isDirectory();
      node = addNode(node, name, isDirectory);
    }
  }

  private static DefaultMutableTreeNode addNode(DefaultMutableTreeNode node, String name, boolean isDirectory) {
    Enumeration childrens = node.children();
    while (childrens.hasMoreElements()) {
      DefaultMutableTreeNode children = (DefaultMutableTreeNode) childrens.nextElement();
      if (name.equals(children.getUserObject())) {
        return children;
      }
    }
    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name, isDirectory);
    node.add(newNode);
    return newNode;
  }

  private static void safeClose(ZipFile zipFile) {
    if (zipFile != null) {
      try {
        zipFile.close();
      } catch (IOException e) {

      }
    }
  }
}