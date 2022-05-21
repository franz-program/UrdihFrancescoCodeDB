import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ResultTable extends JFrame {

    private JPanel mainPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public ResultTable(String[] columnNames, List<Object[]> rows, int[] size){
        mainPanel = new JPanel(null);
        this.setSize(size[0], size[1]);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableModel = new DefaultTableModel();

        for(String columnName : columnNames)
            tableModel.addColumn(columnName);


        table = new JTable(tableModel);

        for(Object[] row : rows)
            tableModel.addRow(row);

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(5, 10, 550, 150);
        scrollPane.setVisible(true);
        mainPanel.add(scrollPane);

        this.add(mainPanel);

    }

}
