/*** 
 * @Author �J�͵�
 * �Ǹ�: 104403519
 * �t��: ���3A
 * HW7: �q�T��
***/
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.sql.SQLException;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

public class DisplayTest extends JFrame implements ActionListener {

    static final String DATABASE_URL = "jdbc:mysql://localhost/member";//�]�w��Ʈw:��Ʈwip/��Ʈw�W��
    //�b���K�X�Ҭ�java
    static final String USERNAME = "java"; 
    static final String PASSWORD = "java";
    static final String DEFAULT_QUERY = "SELECT * FROM people";
    ResultSetTableModel tableModel;
    private int selectedPersonID;
    private TitledBorder title = BorderFactory.createTitledBorder("Find a entry by a field");
    private JLabel SerByID = new JLabel("Fieled Name:");
    private String[] f = {"MemberID", "name", "phone", "email", "sex"};
    private JComboBox Fieleds = new JComboBox<String>(f);
    private JLabel IDlab = new JLabel("Member ID:");
    private JLabel namelab = new JLabel("Name:");
    private JLabel phonelab = new JLabel("Phone Number:");
    private JLabel maillab = new JLabel("Email:");
    private JLabel sexlab = new JLabel("Sex:");    

    private JTextField searchfield = new JTextField();
    private JTextField IDfield = new JTextField();
    private JTextField namefield = new JTextField();
    private JTextField phonefield = new JTextField();
    private JTextField mailfield = new JTextField();
    private JTextField sexfield = new JTextField();

    private JButton findbutton = new JButton("Find");
    private int findskey = 0;
    private JButton showall = new JButton("Browse All Entry");
    private JButton insert = new JButton("Insert New Entry");
    private JButton update = new JButton("update");
    private JButton delete = new JButton("delete");
    private JPanel Wpanel = new JPanel();
    private JPanel Spanel = new JPanel();
    private JTable resultTable;

    public DisplayTest() throws ClassNotFoundException {
        super("Member Database");
        try {
            tableModel = new ResultSetTableModel(DATABASE_URL,
                    USERNAME, PASSWORD, DEFAULT_QUERY);
            resultTable = new JTable(tableModel);
            setLayout(new BorderLayout());
            Box Npanel = Box.createHorizontalBox();
            Npanel.setBorder(title);
            add(Npanel, BorderLayout.NORTH);

            Npanel.add(SerByID);
            Npanel.add(Fieleds);
            Npanel.add(searchfield);
            Npanel.add(findbutton);
            add(Wpanel, BorderLayout.WEST);
            Wpanel.setLayout(new GridLayout(5, 2));
            Wpanel.add(IDlab);
            Wpanel.add(IDfield);
            Wpanel.add(namelab);
            Wpanel.add(namefield);
            Wpanel.add(phonelab);
            Wpanel.add(phonefield);
            Wpanel.add(maillab);
            Wpanel.add(mailfield);
            Wpanel.add(sexlab);
            Wpanel.add(sexfield);            
            IDfield.setEditable(false);
            add(new JScrollPane(resultTable), BorderLayout.CENTER);
            add(Spanel, BorderLayout.SOUTH);
            Spanel.setLayout(new GridLayout(1, 4, 5, 5));
            Spanel.add(showall);
            Spanel.add(insert);
            Spanel.add(update);
            Spanel.add(delete);

            //JTable�ϥ�RowSorter�ӱƧ�
            RowSorter<ResultSetTableModel> sorter = new TableRowSorter<ResultSetTableModel>(tableModel);
            resultTable.setRowSorter(sorter);

            ListSelectionModel rowSM = resultTable.getSelectionModel();
            rowSM.addListSelectionListener(
                    new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();

                    if (lsm.isSelectionEmpty()) {
                    } else {
                        int selectedRow = lsm.getMinSelectionIndex();
                        queryPerson(selectedRow);
                    }
                }
            });
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, sqlException.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // terminate application
        }
        insert.addActionListener(this);
        delete.addActionListener(this);
        update.addActionListener(this);
        findbutton.addActionListener(this);
        showall.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insert) { //�s�W�@��record
            if (isInCorrect()) {
                try {
                    tableModel.setQueryUpdate("INSERT INTO `people` (`name`,`phone`,`email`,`sex`) VALUES('" + namefield.getText() + "','" + phonefield.getText() + "','" + mailfield.getText() + "','" + sexfield.getText() + "')");
                    JOptionPane.showMessageDialog(null, "New Entry is inserted.");
                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(null,
                            sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                }
                clearTextfield();
            }
            

        }
        if (e.getSource() == delete) { //�R���@��record
            try {
                tableModel.setQueryUpdate("DELETE FROM `member`.`people` WHERE `MemberID`='" + IDfield.getText() + "'");
                IDfield.setText("");
                JOptionPane.showMessageDialog(null, "People deleted.");
            } catch (SQLException sqlException) {
                JOptionPane.showMessageDialog(null,
                        sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == update) { //�s��@��record
            if (isInCorrect()) {
                try {
                    tableModel.setQueryUpdate("UPDATE `people` SET `name`='" + namefield.getText() + "',`phone`='" + phonefield.getText() + "',`email`='" + mailfield.getText() + "',`sex`='" + sexfield.getText() + "' WHERE `MemberID`='" + IDfield.getText() + "'");
                    JOptionPane.showMessageDialog(null, "People updated.");
                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(null,
                            sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
                }
                clearTextfield();
            }
            

        }
        if (e.getSource() == findbutton) { //�W������r�j�M���s
            findskey = Fieleds.getSelectedIndex();

            try {
                if (searchfield.getText() == "") {
                    tableModel.setQuery("SELECT * FROM `people`");
                } else {
                    tableModel.setQuery("SELECT * FROM `people` WHERE `" + f[findskey] + "` LIKE '%" + searchfield.getText() + "%'");
                }
            } catch (SQLException sqlException) {
                JOptionPane.showMessageDialog(null,
                        sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == showall) { //load�Ҧ���ƨ�table�q�X��
            try {
                tableModel.setQuery("SELECT * FROM `people`");
            } catch (SQLException sqlException) {
                JOptionPane.showMessageDialog(null,
                        sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void clearTextfield(){
        namefield.setText("");
        phonefield.setText("");
        mailfield.setText("");
        sexfield.setText("");
        IDfield.setText("");
    }
    //�o��B�z��J�榡����
    private boolean isInCorrect() {
        if(namefield.getText().isEmpty()){ //���i����
            JOptionPane.showMessageDialog(null,
                        "�п�J�m�W!", "�榡���~", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!phonefield.getText().matches("[0-9]{4}-[0-9]{6}")) { //�ݨ̷�09xx-xxxxxx�榡
            JOptionPane.showMessageDialog(null,
                        "����榡���~!", "�榡���~", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!mailfield.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")){ //�ݦ�@��.
            JOptionPane.showMessageDialog(null,
                        "�H�c�榡���~!", "�榡���~", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(sexfield.getText().isEmpty()){ //���i����
            JOptionPane.showMessageDialog(null,
                        "�п�J�ʧO!", "�榡���~", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void queryPerson(int selectedRow) {
        Person person = tableModel.getPerson(selectedRow);

        IDfield.setText(Integer.toString(person.getMemberID())); //�Nint�ରString
        namefield.setText(person.getName());
        phonefield.setText(person.getPhone());
        mailfield.setText(person.getMail());
        sexfield.setText(person.getSex());

        selectedPersonID = person.getMemberID();
    } // end method queryPerson

    public static void main(String args[]) throws ClassNotFoundException {
        DisplayTest display = new DisplayTest();
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setSize(800, 550);
        display.setVisible(true);
    } // end main
} // end class DisplayQueryResults

