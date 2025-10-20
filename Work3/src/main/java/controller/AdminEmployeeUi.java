package controller;

import po.Employee;
import po.service.EmployeeService;
import po.service.impl.EmployeeServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminEmployeeUi extends JPanel {

    private EmployeeService employeeService = new EmployeeServiceImpl();
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtEmployeeId, txtName, txtImage;
    private JComboBox<String> cboArea;

    public AdminEmployeeUi() {
        setLayout(new BorderLayout());

        // ===== 上方 JTable =====
        tableModel = new DefaultTableModel(new String[]{"外送員ID","名字","圖片路徑","地區"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== 下方輸入欄 =====
        JPanel bottom = new JPanel(new GridLayout(2,1));

        JPanel input = new JPanel(new FlowLayout());
        txtEmployeeId = new JTextField(8);
        txtName = new JTextField(10);
        txtImage = new JTextField(15);
        cboArea = new JComboBox<>(new String[]{
                "台北市","新北市","桃園市","台中市","台南市","高雄市","基隆市","新竹市","嘉義市"
        });

        input.add(new JLabel("外送員ID:")); input.add(txtEmployeeId);
        input.add(new JLabel("姓名:")); input.add(txtName);
        input.add(new JLabel("圖片路徑:")); input.add(txtImage);
        input.add(new JLabel("地區:")); input.add(cboArea);

        bottom.add(input);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("新增");
        JButton btnUpdate = new JButton("修改");
        JButton btnDelete = new JButton("刪除");
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete);
        bottom.add(btnPanel);

        add(bottom, BorderLayout.SOUTH);

        // 載入資料
        loadEmployees();

        // ===== 表格選擇事件 =====
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtEmployeeId.setText(tableModel.getValueAt(row,0).toString());
                txtName.setText(tableModel.getValueAt(row,1).toString());
                txtImage.setText(tableModel.getValueAt(row,2).toString());
                cboArea.setSelectedItem(tableModel.getValueAt(row,3));
            }
        });

        // ===== 按鈕事件 =====
        btnAdd.addActionListener(e -> {
            try {
                Employee emp = new Employee(
                        txtEmployeeId.getText().trim(),
                        txtName.getText().trim(),
                        txtImage.getText().trim(),
                        cboArea.getSelectedItem().toString()
                );
                employeeService.addEmployee(emp);
                JOptionPane.showMessageDialog(this,"新增成功！");
                loadEmployees();
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,"新增失敗："+ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                Employee emp = new Employee(
                        txtEmployeeId.getText().trim(),
                        txtName.getText().trim(),
                        txtImage.getText().trim(),
                        cboArea.getSelectedItem().toString()
                );
                employeeService.updateEmployee(emp);
                JOptionPane.showMessageDialog(this,"修改成功！");
                loadEmployees();
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,"修改失敗："+ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row < 0){
                JOptionPane.showMessageDialog(this,"請先選擇要刪除的外送員");
                return;
            }
            String id = tableModel.getValueAt(row,0).toString();
            try {
                employeeService.deleteEmployee(id);
                JOptionPane.showMessageDialog(this,"刪除成功！");
                loadEmployees();
            } catch (Exception ex){
                JOptionPane.showMessageDialog(this,"刪除失敗："+ex.getMessage());
            }
        });
    }

    private void loadEmployees() {
        try {
            tableModel.setRowCount(0);
            List<Employee> employees = employeeService.getAllEmployees();
            for (Employee e : employees) {
                tableModel.addRow(new Object[]{
                        e.getEmployeeid(), e.getName(), e.getImage(), e.getArea()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "載入外送員失敗：" + ex.getMessage());
        }
    }
}
