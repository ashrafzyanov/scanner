package com.ashrafzyanov.scanner.ui;

import Catalog.DataBaseJob.*;
import DataBaseFileSerialization.Disk;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.SQLException;
import DataBaseFileSerialization.File;
import javax.swing.JFileChooser;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Stack;

public class MainForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private DBWorker connect;
    private TreeModelTreeFiles treeFiles;
    private DefaultMutableTreeNode root;
    private FileTableView fileView;
    private boolean isHidden = false;
    private JPopupMenu m_popup;
    private DefaultTreeCellEditor m_editor;
    private Stack<File> historyClick = new Stack<File>();
    private Stack<File> historyClickNext = new Stack<File>();

    public MainForm() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            initComponents();
            jTree1.addTreeExpansionListener(new DirExpansionListener());
            jTree1.setShowsRootHandles(true);
            // jMenuBar1.getComponent(2).setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(rootPane, e.getMessage(), "Ошибка", JOptionPane.CLOSED_OPTION,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    class DirExpansionListener implements TreeExpansionListener {

        public void treeExpanded(TreeExpansionEvent event) {
            final TreePath t = event.getPath();
            final DefaultMutableTreeNode node = ((DefaultMutableTreeNode) event.getPath().getLastPathComponent());
            new Thread() {
                public void run() {
                    if (treeFiles.getChildren(t, isHidden)) {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                treeFiles.reloadChild(jTree1.getModel(), t);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                }
            }.start();
        }

        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new ButtonTest();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Каталог");
        setBackground(new java.awt.Color(255, 254, 254));

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerSize(2);
        jSplitPane1.setToolTipText("<->");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null },
                        { null, null, null, null }, { null, null, null, null }, { null, null, null, null } },
                new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setGridColor(new java.awt.Color(195, 196, 196));
        jTable1.setRowHeight(17);
        jTable1.setSelectionBackground(new java.awt.Color(79, 145, 210));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(100, 51));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(200, 350));

        jTree1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jTabbedPane1.addTab("Хранилище", jScrollPane1);

        jScrollPane3.setViewportView(jTree2);

        jTabbedPane1.addTab("Категории", jScrollPane3);

        jSplitPane1.setLeftComponent(jTabbedPane1);

        jButton1.setText("jButton1");

        jTextField1.setBackground(new java.awt.Color(225, 195, 247));
        jTextField1.setEditable(false);
        jTextField1.setDisabledTextColor(new java.awt.Color(1, 1, 1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel1.setText("Хранится в ->");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel2.setText("По следующему пути ->");

        jTextField2.setBackground(new java.awt.Color(225, 195, 247));
        jTextField2.setEnabled(false);

        jButton2.setText("jButton2");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout
                .setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2).addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 919,
                                                Short.MAX_VALUE)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 919,
                                                Short.MAX_VALUE))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(12, Short.MAX_VALUE).addComponent(jButton2).addContainerGap()));

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMenuBar1.setPreferredSize(new java.awt.Dimension(204, 25));

        jMenu1.setText("База данных");

        jMenuItem1.setText("Подключиться");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Настройка подключения");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Отключиться");
        jMenuItem3.setEnabled(false);
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Сканирование");

        jMenuItem4.setText("Сканировать новый носитель");
        jMenuItem4.setEnabled(false);
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Назад");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup().addGap(137, 137, 137).addComponent(jButton3)
                        .addContainerGap(940, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)));

        pack();
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            connect.getConnection().close();
            treeFiles.DeleteRoot();
            treeFiles.reloadChild(jTree1.getModel());
            fileView.delTable();
            jMenuItem1.setEnabled(true);
            jMenuItem2.setEnabled(true);
            jMenuItem3.setEnabled(false);
            jMenuItem4.setEnabled(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        int col = jTable1.convertRowIndexToModel(jTable1.getSelectedRow());
        File fs = fileView.getObject(col);
        String[] res = { "", "" };
        if (evt.getClickCount() == 2) {
            if ((fs.getIdType() == 1) || (fs.getIdType() == 2 && fs.getExt().equalsIgnoreCase("zip"))) {
                historyClick.push(new File(fs));
                fileView.getFiles(fs);
                res = getPath(fs.getIdDisk(), fs.getId());
                jButton2.setEnabled(!(historyClick.size() == 0));
            } else {
                res = getPath(fs.getIdDisk(), fs.getIdParrent());
            }
        } else {
            res = getPath(fs.getIdDisk(), fs.getIdParrent());
        }
        jTextField1.setText(res[0]);
        jTextField2.setText(res[1]);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        ParamConnection pc = new ParamConnection(this, true);
        pc.setVisible(true);
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileopen = new JFileChooser();
        fileopen.setFileSelectionMode(fileopen.DIRECTORIES_ONLY);
        if (fileopen.showOpenDialog(this) == JFileChooser.OPEN_DIALOG) {
            try {
                // PleasWait dialog = new PleasWait(null, true);
                // dialog.setVisible(true);
                Catalog.File.CreateFileTree scan = new Catalog.File.CreateFileTree(connect.getConnection());
                scan.Scaning(fileopen.getSelectedFile());
                // dialog.setVisible(false);
                EnterName en = new EnterName(this, true, connect.getConnection(), scan.getNumberDisk());
                en.setVisible(true);
                treeFiles.DeleteRoot();
                treeFiles.getRootDisk();
                treeFiles.reloadChild(jTree1.getModel());
                jTree1.setSelectionRow(0);
                fileView.delTable();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                javax.swing.JOptionPane op = new javax.swing.JOptionPane();
                op.showMessageDialog(this, e.getMessage(), "Ошибка!", 0);
            }
        }
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode dmtn = ((DefaultMutableTreeNode) evt.getPath().getLastPathComponent());
        Object obj = dmtn.getUserObject();
        String[] res = { "", "" };
        if (obj instanceof File) {
            File fs = (File) dmtn.getUserObject();
            res = getPath(fs.getIdDisk(), fs.getId());
        }
        jTextField1.setText(res[0]);
        jTextField2.setText(res[1]);
        fileView.getFiles(obj);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            connect = new DBWorker(ConnectionString.getDriver(), ConnectionString.getURL(), ConnectionString.getLogin(),
                    ConnectionString.getPassword());
            connect.openConnect();
            treeFiles = new TreeModelTreeFiles(connect.getConnection());
            treeFiles.getRootDisk();
            jTree1.setModel(treeFiles.getTreeModel());
            fileView = new FileTableView(connect.getConnection());
            jTable1.setModel(fileView);
            jMenuItem4.setEnabled(true);
            StyleTree t = new StyleTree();
            jTree1.setCellRenderer(t);
            jMenuItem3.setEnabled(true);
            jMenuItem1.setEnabled(false);
            jMenuItem2.setEnabled(false);
            m_popup = new JPopupMenu();
            jTree1.setEditable(true);
            Action a3 = new AbstractAction("Переименовать", null) {
                public void actionPerformed(ActionEvent e) {
                    jTree1.repaint();
                    TreePath path = jTree1.getSelectionPath();
                    if (path == null) {
                        return;
                    }
                    jTree1.startEditingAtPath(path);
                }
            };
            m_popup.add(a3);
            jTree1.add(m_popup);

            class PopupTrigger extends MouseAdapter {

                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        int x = e.getX();
                        int y = e.getY();
                        TreePath path = jTree1.getPathForLocation(x, y);
                        if (path == null) {
                            return;
                        }
                        DefaultMutableTreeNode dfmn = ((DefaultMutableTreeNode) path.getLastPathComponent());
                        Object obj = dfmn.getUserObject();
                        if (obj instanceof Disk) {
                            jTree1.setSelectionPath(path);
                            m_popup.show(jTree1, x, y);
                        }
                    }
                }
            }
            PopupTrigger popup = new PopupTrigger();
            jTree1.addMouseListener(popup);
        } catch (SQLException e) {
            JOptionPane op = new JOptionPane();
            op.showMessageDialog(null, "Не удалось подключиться к базе данных, проверьте параметры подключения",
                    "Ошибка подключения к базе данных", 0);
        }
    }

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {
        File fs = historyClick.pop();
        fileView.getFiles(fileView.getParentFiles(fs));
        String[] res = getPath(fs.getIdDisk(), fs.getIdParrent());
        jTextField1.setText(res[0]);
        jTextField2.setText(res[1]);

    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        File fs = historyClick.pop();
        historyClickNext.push(fs);
        fileView.getFiles(fileView.getParentFiles(fs));
        String[] res = getPath(fs.getIdDisk(), fs.getIdParrent());
        jTextField1.setText(res[0]);
        jTextField2.setText(res[1]);
        jButton2.setEnabled(!(historyClick.size() == 0));
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        File fs = historyClickNext.pop();
        fileView.getFiles(fileView.getParentFiles(fs));
        String[] res = getPath(fs.getIdDisk(), fs.getIdParrent());
        jTextField1.setText(res[0]);
        jTextField2.setText(res[1]);
        jButton2.setEnabled(!(historyClickNext.size() == 0));
    }

    public String[] getPath(int id_disk, int id_file) {
        Statement statement;
        ResultSet rs;
        String[] res = { "", "" };
        try {
            statement = this.connect.getConnection().createStatement();
            rs = statement.executeQuery(
                    "SELECT d.name, t.name FROM disk d, typedisk t WHERE d.id = " + id_disk + " and d.id_type = t.id;");
            rs.next();
            res[0] = rs.getString(2) + ": " + rs.getString(1);
            if (id_file != 0) {
                int id = id_file;
                do {
                    rs = statement.executeQuery("SELECT id_parrent, name, id_type, rash FROM treefiles WHERE id_disk = "
                            + id_disk + " and id = " + id + ";");
                    rs.next();
                    id = rs.getInt(1);
                    res[1] = rs.getString(2) + (rs.getByte(3) == 2 ? "." + rs.getString(4) : "")
                            + java.io.File.separator + res[1];
                    if (id == 0) {
                        return res;
                    }
                } while (true);
            } else {
                res[1] = "В корне";
                return res;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTree jTree1;
    private javax.swing.JTree jTree2;
    
}
