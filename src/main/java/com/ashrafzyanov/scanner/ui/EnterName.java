package com.ashrafzyanov.scanner.ui;

import javax.swing.UIManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnterName extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    Connection connect;
    int id_disk;

    public EnterName(java.awt.Frame parent, boolean modal, Connection connect, int id_disk) {
        super(parent, modal);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
        this.connect = connect;
        this.id_disk = id_disk;
        jTextField1.setText("Мой диск номер " + id_disk);

    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Завершение сканирования");

        jLabel1.setText("Введите имя для просканированного диска, это имя будет сообщаться в процессе работы программы");

        jLabel2.setText("Имя");

        jTextField1.setText("jTextField1");

        jButton1.setText("ОК");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTextField1.getText().length() == 0) {
            javax.swing.JOptionPane op = new javax.swing.JOptionPane();
            op.showMessageDialog(this, "Вы ничего не ввели, для продолжения работы нужно ввести имя", "Не верное имя", 0);
        } else {
            try {
                Statement statement = connect.createStatement();
                ResultSet rs = statement.executeQuery("SELECT count(*) FROM disk WHERE name = '" + jTextField1.getText() + "';");
                rs.next();
                if (rs.getInt(1) != 0) {
                    javax.swing.JOptionPane op = new javax.swing.JOptionPane();
                    op.showMessageDialog(this, "Данное имя уже существует, введите другое", "Не верное имя", 0);
                } else {
                    statement.executeUpdate("UPDATE disk SET name = '" + jTextField1.getText() + "' WHERE id = " + id_disk + ";");
                    this.dispose();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
}
