package UI;


import Helper.DBHelper;
import Models.Attribute;
import Models.Business;
import Models.BusinessCategories;
import Models.BusinessSubCategories;
import Models.User;
import Models.Reviews;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import javax.swing.table.DefaultTableModel;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ayushi
 */
public class HW3 extends javax.swing.JFrame {
    private DBHelper db;
    private JFrame frame;
    
    ArrayList<String> selectedCategories = new ArrayList<String>();
    ArrayList<String> selectedSubCategories = new ArrayList<String>();
    ArrayList<String> selectedAttributes = new ArrayList<String>();
    ArrayList<String> generatedBusinessIds = new ArrayList<String>();
    ArrayList<String> generatedUserIds = new ArrayList<String>();
    

    /**
     * Creates new form NewJFrame
     */
    public HW3() {
        db = new DBHelper();
        db.DBConnect();
        
        frame = new JFrame();
        frame.setPreferredSize(new java.awt.Dimension(1600, 900));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        initComponents();
        
        loadCategories();
    }
    
    private void loadCategories() {
        b_cat_panel.removeAll();
        ArrayList<BusinessCategories> cats = db.getAllCategories();
        for(int i=0; i < cats.size(); i++){
            JCheckBox mycheckbox = new JCheckBox();
            mycheckbox.setSize(10,10);
            mycheckbox.setText(cats.get(i).getCategoryName());
            mycheckbox.setForeground(Color.BLACK);
            mycheckbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    Object source = e.getItemSelectable();
                    JCheckBox checkbox = (JCheckBox) source;
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        System.out.println("selected");
                        System.out.println(checkbox.getText());
                        selectedCategories.add(checkbox.getText());

                    } else {
                        System.out.println("unselected");
                        System.out.println(checkbox.getText());
                        selectedCategories.remove(checkbox.getText());
                    }
                    loadSubCategories();
                    updateData();
                    if(selectedCategories.size() <  1) {
                        b_subcat_panel.removeAll();
                        b_subcat_panel.repaint();

                        b_attr_panel.removeAll();
                        b_attr_panel.repaint();

                        DefaultTableModel tmodel = new DefaultTableModel();
                        b_table.removeAll();
                        b_table.setModel(tmodel);
                        tmodel.addColumn("Business");
                        tmodel.addColumn("City");
                        tmodel.addColumn("State");
                        tmodel.addColumn("Stars");
                    }
                }
            });
            b_cat_panel.add(mycheckbox);
            frame.pack();
        }
        b_cat_panel.repaint();
     }
     
    private void loadSubCategories(){
        String condition = b_and_or_cond.getSelectedItem().toString();
        b_subcat_panel.removeAll();
        ArrayList<BusinessSubCategories> subs = db.getSubCategories( selectedCategories, condition);
        
        for(int i = 0; i < subs.size(); i++) {
            JCheckBox mycheckbox = new JCheckBox();
            mycheckbox.setSize(10,10);
            mycheckbox.setText(subs.get(i).getSubCategoryName());
            mycheckbox.setForeground(Color.BLACK);
            mycheckbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    Object source = e.getItemSelectable();
                    JCheckBox checkbox = (JCheckBox) source;
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        System.out.println("selected " + checkbox.getText());
                        selectedSubCategories.add(checkbox.getText());
                    } else {
                        System.out.println("unselected");
                        System.out.println(checkbox.getText());
                        selectedSubCategories.remove(checkbox.getText());
                    }
                    loadAttributes();
                    if (selectedSubCategories.size() < 1) {
                        b_attr_panel.removeAll();
                        b_attr_panel.repaint();
                    }
                    updateData();
                }
            });
            b_subcat_panel.add(mycheckbox);
            frame.pack();
        }
        b_subcat_panel.repaint();
    }
     
    public void loadAttributes(){
        String condition = b_and_or_cond.getSelectedItem().toString();
        
        b_attr_panel.removeAll();
        ArrayList<Attribute> attrs = db.getAttributes( selectedSubCategories, selectedCategories, condition);
        for(int i=0; i < attrs.size(); i++) {
            JCheckBox mycheckbox = new JCheckBox();
            mycheckbox.setSize(10, 10);
            mycheckbox.setText(attrs.get(i).getAttribute_name());
            mycheckbox.setForeground(Color.BLACK);
            mycheckbox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    Object source = e.getItemSelectable();
                    JCheckBox checkbox = (JCheckBox) source;
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        System.out.println("selected " + checkbox.getText());
                        selectedAttributes.add(checkbox.getText());

                    } else {
                        System.out.println("unselected " + checkbox.getText());
                        selectedAttributes.remove(checkbox.getText());
                    }
                   updateData();
                }
            });
            b_attr_panel.add(mycheckbox);
            frame.pack();
        }
        b_attr_panel.repaint();
    } 
    
    public void updateData(){

        b_display_query.removeAll();
        DefaultTableModel tmodel = new DefaultTableModel();
        b_table.removeAll();
        b_table.setModel(tmodel);
        tmodel.addColumn("Business");
        tmodel.addColumn("City");
        tmodel.addColumn("State");
        tmodel.addColumn("Stars");

        String condition = b_and_or_cond.getSelectedItem().toString();

        ArrayList<Business> businesses = new ArrayList<Business>();
        DateFormat df = new SimpleDateFormat("dd-MMM-yy");
        String date1 = "";
        if (b_dateChooser_from.getDate() != null) {
            date1 = df.format(b_dateChooser_from.getDate());
        }
        String date2 = "";
        if (b_dateChooser_to.getDate() != null) {
            date2 = df.format(b_dateChooser_to.getDate());
        }
        businesses = db.businessFilterQuery(selectedCategories, selectedSubCategories, selectedAttributes, date1, date2, 
                b_stars_condition.getSelectedItem().toString(), b_star_value.getText(), b_votes_condition.getSelectedItem().toString(), b_votes_value.getText(), condition);
        System.out.println("===db.finalQuery===========" + db.finalQuery);
        b_display_query.setText(db.finalQuery);

        generatedBusinessIds = new ArrayList<String>();

        System.out.println("row count :" + businesses.size());
        System.out.println("=businesses.size()===="+businesses.size());
        for (int i = 0; i < businesses.size(); i++) {
            Business business = businesses.get(i);
            tmodel.addRow(new Object[]{business.getName(), business.getCity(), business.getState(), business.getStars()});
            generatedBusinessIds.add(business.getBusinessId());
        }
        
        frame.pack();
        
    }

    public void updateUserData() {
        u_display_query.removeAll();
        String condition = u_condition.getSelectedItem().toString();
        DefaultTableModel tmodel = new DefaultTableModel();
        u_table.removeAll();
        u_table.setModel(tmodel);
        tmodel.addColumn("User_Name");
        tmodel.addColumn("Member_Since");
        tmodel.addColumn("Average Stars");
        
        DateFormat df = new SimpleDateFormat("dd-MMM-yy");
        String yelping_since = "";
        if (u_yelping_since.getDate() != null) {
            yelping_since = df.format(u_yelping_since.getDate());
        }
        
        ArrayList<User> users = new ArrayList<User>();
        
        users = db.userFilterQuery(yelping_since, u_review_cond.getSelectedItem().toString(), u_reviews_val.getText(), u_friends_cond.getSelectedItem().toString(), 
                u_friends_val.getText(), u_stars_cond.getSelectedItem().toString(), u_stars_val.getText(), 
                u_votes_cond.getSelectedItem().toString(), u_votes_val.getText(), condition);
        
        generatedUserIds =  new ArrayList<String>();
        u_display_query.setText(db.userFinalQuery);
        
        System.out.println("row count :" + users.size());
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            tmodel.addRow(new Object[]{user.getName(), user.getYelpingSince(), user.getAverageStars()});
            generatedUserIds.add(user.getUserId());
        }
        
        frame.pack();
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        b_cat_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        b_table = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        b_subcat_panel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        b_attr_panel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        b_dateChooser_from = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        b_dateChooser_to = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        b_stars_condition = new javax.swing.JComboBox<>();
        b_votes_condition = new javax.swing.JComboBox<>();
        b_star_value = new javax.swing.JTextField();
        b_votes_value = new javax.swing.JTextField();
        b_and_or_cond = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        b_execute_query = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        b_display_query = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        b_review_table = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        u_table = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        u_review_table = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        u_yelping_since = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        u_review_cond = new javax.swing.JComboBox<>();
        u_friends_cond = new javax.swing.JComboBox<>();
        u_stars_cond = new javax.swing.JComboBox<>();
        u_votes_cond = new javax.swing.JComboBox<>();
        u_reviews_val = new javax.swing.JTextField();
        u_friends_val = new javax.swing.JTextField();
        u_stars_val = new javax.swing.JTextField();
        u_votes_val = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        u_display_query = new javax.swing.JTextArea();
        u_execute_query = new javax.swing.JButton();
        u_condition = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.FlowLayout());

        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane2.setFocusable(false);

        b_cat_panel.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane2.setViewportView(b_cat_panel);

        b_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Business", "City", "State", "Stars"
            }
        ));
        b_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(b_table);

        b_subcat_panel.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane4.setViewportView(b_subcat_panel);

        b_attr_panel.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane5.setViewportView(b_attr_panel);

        jLabel1.setText("From");

        b_dateChooser_from.setDateFormatString("dd MMM, yy");

        jLabel2.setText("To");

        jLabel3.setText("Stars");

        jLabel4.setText("Votes");

        b_stars_condition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<", ">" }));

        b_votes_condition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<", ">" }));

        b_and_or_cond.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AND", "OR" }));

        jLabel10.setText("AND/OR");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(b_votes_condition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b_votes_value))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(b_stars_condition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b_star_value, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b_and_or_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(b_dateChooser_from, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel2)
                                .addGap(4, 4, 4)
                                .addComponent(b_dateChooser_to, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_and_or_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_dateChooser_to, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_dateChooser_from, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(b_stars_condition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_star_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(b_votes_condition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_votes_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        b_execute_query.setText("Execute Query");
        b_execute_query.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b_execute_queryMouseClicked(evt);
            }
        });

        b_display_query.setEditable(false);
        b_display_query.setColumns(20);
        b_display_query.setLineWrap(true);
        b_display_query.setRows(5);
        b_display_query.setWrapStyleWord(true);
        b_display_query.setAutoscrolls(false);
        jScrollPane6.setViewportView(b_display_query);

        b_review_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Author", "Review Text"
            }
        ));
        jScrollPane7.setViewportView(b_review_table);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(148, 148, 148)
                                .addComponent(b_execute_query)))
                        .addGap(0, 46, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane7))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_execute_query)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 14, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Business", jPanel4);

        u_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "User Name", "Yelping Since", "Average Stars"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        u_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                u_tableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(u_table);

        u_review_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Business", "Review"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(u_review_table);

        jLabel5.setText("Yelping since");

        jLabel6.setText("Review Count");

        jLabel7.setText("No. of Friends");

        jLabel8.setText("Average Stars");

        jLabel9.setText("No. of Votes");

        u_review_cond.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<", ">" }));

        u_friends_cond.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<", ">" }));

        u_stars_cond.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<", ">" }));

        u_votes_cond.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "<", ">" }));

        u_display_query.setEditable(false);
        u_display_query.setColumns(20);
        u_display_query.setLineWrap(true);
        u_display_query.setRows(5);
        u_display_query.setWrapStyleWord(true);
        jScrollPane9.setViewportView(u_display_query);

        u_execute_query.setText("Execute Query");
        u_execute_query.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                u_execute_queryMouseClicked(evt);
            }
        });

        u_condition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AND", "OR" }));

        jLabel11.setText("AND/OR");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(177, 177, 177)
                                .addComponent(u_execute_query))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(u_condition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(u_review_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_friends_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_stars_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_votes_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(u_friends_val, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_reviews_val, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_stars_val, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_votes_val, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(u_yelping_since, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(u_condition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(u_yelping_since, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(u_review_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_reviews_val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(u_friends_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_friends_val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(u_stars_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(u_stars_val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(u_votes_cond, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(u_votes_val, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(2, 2, 2)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(u_execute_query)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("User", jPanel7);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 973, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2))
        );

        getContentPane().add(jPanel3);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_tableMouseClicked
        // TODO add your handling code here:
        int row = b_table.rowAtPoint(evt.getPoint());
        if (!(generatedBusinessIds.size() < row)) {
            frame.setEnabled(false);
            b_table.setOpaque(false);
            DefaultTableModel tmodel = new DefaultTableModel();
                b_review_table.setModel(tmodel);
                tmodel.addColumn("Author");
                tmodel.addColumn("Review Text");
                
                DateFormat df = new SimpleDateFormat("dd-MMM-yy");
                String date1 = "";
                if (b_dateChooser_from.getDate() != null) {
                    date1 = df.format(b_dateChooser_from.getDate());
                }
                String date2 = "";
                if (b_dateChooser_to.getDate() != null) {
                    date2 = df.format(b_dateChooser_to.getDate());
                }

                ArrayList<Reviews> reviews = db.getBusinessReviews(generatedBusinessIds.get(row), date1, date2, 
                b_stars_condition.getSelectedItem().toString(), b_star_value.getText(), b_votes_condition.getSelectedItem().toString(), b_votes_value.getText());
                b_display_query.setText(db.finalQuery);
                
                System.out.println("row count :" + reviews.size());
                for(int i = 0; i < reviews.size(); i++){
                    Reviews review = reviews.get(i);
                    tmodel.addRow(new Object[]{review.getUserId(), review.getText()});
                }
            frame.setEnabled(true);
        }
        frame.pack();
    }//GEN-LAST:event_b_tableMouseClicked

    private void b_execute_queryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_execute_queryMouseClicked
        // TODO add your handling code here:
        updateData(); 
    }//GEN-LAST:event_b_execute_queryMouseClicked

    private void u_execute_queryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_u_execute_queryMouseClicked
        // TODO add your handling code here:
        updateUserData();
    }//GEN-LAST:event_u_execute_queryMouseClicked

    private void u_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_u_tableMouseClicked
        // TODO add your handling code here:
        int row = u_table.rowAtPoint(evt.getPoint());
        if (!(generatedUserIds.size() < row)) {
            frame.setEnabled(false);
            u_table.setOpaque(false);
            DefaultTableModel tmodel = new DefaultTableModel();
                u_review_table.setModel(tmodel);
                tmodel.addColumn("Author");
                tmodel.addColumn("Review Text");

                ArrayList<Reviews> reviews = db.getUserReviews(generatedUserIds.get(row));
                u_display_query.setText(db.userFinalQuery);
                
                System.out.println("row count :" + reviews.size());
                for(int i = 0; i < reviews.size(); i++){
                    Reviews review = reviews.get(i);
                    tmodel.addRow(new Object[]{review.getBusinessId(), review.getText()});
                }
            frame.setEnabled(true);
        }
        frame.pack();
    }//GEN-LAST:event_u_tableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HW3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HW3().setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> b_and_or_cond;
    private javax.swing.JPanel b_attr_panel;
    private javax.swing.JPanel b_cat_panel;
    private com.toedter.calendar.JDateChooser b_dateChooser_from;
    private com.toedter.calendar.JDateChooser b_dateChooser_to;
    private javax.swing.JTextArea b_display_query;
    private javax.swing.JButton b_execute_query;
    private javax.swing.JTable b_review_table;
    private javax.swing.JTextField b_star_value;
    private javax.swing.JComboBox<String> b_stars_condition;
    private javax.swing.JPanel b_subcat_panel;
    public static javax.swing.JTable b_table;
    private javax.swing.JComboBox<String> b_votes_condition;
    private javax.swing.JTextField b_votes_value;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JComboBox<String> u_condition;
    private javax.swing.JTextArea u_display_query;
    private javax.swing.JButton u_execute_query;
    private javax.swing.JComboBox<String> u_friends_cond;
    private javax.swing.JTextField u_friends_val;
    private javax.swing.JComboBox<String> u_review_cond;
    private javax.swing.JTable u_review_table;
    private javax.swing.JTextField u_reviews_val;
    private javax.swing.JComboBox<String> u_stars_cond;
    private javax.swing.JTextField u_stars_val;
    private javax.swing.JTable u_table;
    private javax.swing.JComboBox<String> u_votes_cond;
    private javax.swing.JTextField u_votes_val;
    private com.toedter.calendar.JDateChooser u_yelping_since;
    // End of variables declaration//GEN-END:variables
}
