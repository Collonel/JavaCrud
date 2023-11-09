import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import org.apache.commons.dbutils.DbUtils;


public class Employee {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtid;
    private JScrollPane table_1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Database connection
    Connection con;
    PreparedStatement pst;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/runtimeholdings", "root", "");
            System.out.println("Connection Success");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // View Records
    void table_load() {
        try {
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();

            // Create a DefaultTableModel to hold the data
            DefaultTableModel model = new DefaultTableModel();

            // Get metadata about the ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add column names to the model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the model
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            // Set the model for your JTable
            table1.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add Records
    public Employee() {
        connect();
        table_load();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String empname, salary, mobileNo;
                empname = txtName.getText();
                salary = txtSalary.getText();
                mobileNo = txtMobile.getText();

                try {
                    pst = con.prepareStatement("insert into employee(empname, salary, mobileNo) values (?, ?, ?)");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobileNo);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Added!!!!!");
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                    table_load(); // Refresh the table after adding a record
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Search Records
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {

                    String empid = txtid.getText();

                    pst = con.prepareStatement("select empname,salary,mobileNo from employee where id = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()==true)
                    {
                        String empname = rs.getString(1);
                        String emsalary = rs.getString(2);
                        String emmobileNo = rs.getString(3);

                        txtName.setText(empname);
                        txtSalary.setText(emsalary);
                        txtMobile.setText(emmobileNo);

                    }
                    else
                    {
                        txtName.setText("");
                        txtSalary.setText("");
                        txtMobile.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Employee Number");

                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }



        });

        // Update Records
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String empid,empname,salary,mobileNo;
                empname = txtName.getText();
                salary = txtSalary.getText();
                mobileNo = txtMobile.getText();
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("update employee set empname = ?,salary = ?,mobileNo = ? where id = ?");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobileNo);
                    pst.setString(4, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Employee Record Updated");
                    //clears text fields after above action
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }

        });

        // Delete Records
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String empid;
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("delete from employee  where id = ?");

                    pst.setString(1, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Employee Record Deleted!");

                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }



            }
        });
    }
}
