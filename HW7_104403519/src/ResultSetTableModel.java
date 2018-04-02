/*** 
 * @Author �J�͵�
 * �Ǹ�: 104403519
 * �t��: ���3A
 * HW7: �q�T��
***/

import java.sql.*;
import java.util.*;

import javax.swing.table.AbstractTableModel;

public class ResultSetTableModel extends AbstractTableModel {

    private Connection connection;
    private Statement statement;
    private static ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int numberOfRows, Rows;
    private final String DEFAULT_QUERY = "SELECT * FROM people"; //�w�]���q�X�Ҧ����
    private List<Person> AllPersonList; //�s�d�ߥX�Ӫ��Ҧ�Person���O
    private boolean connectedToDatabase = false;

    public ResultSetTableModel(String url, String username, String password, String query)
            throws SQLException, ClassNotFoundException {

        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//�]�wresultSet�u��Ū
        connectedToDatabase = true;
        setQuery(query);
    }//end constructor

    public void setQuery(String query) throws SQLException, IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // specify query and execute it
        resultSet = statement.executeQuery(query);
        // obtain meta data for ResultSet
        metaData = resultSet.getMetaData();
        // determine number of rows in ResultSet
        resultSet.last(); // move to last row
        numberOfRows = resultSet.getRow();
        // ���o�ثe��row�ƶq  
        updateAllPersonList();
        // notify JTable that model has changed
        fireTableStructureChanged();
    } // end method setQuery

    // ���oColumn��ClassType
    public Class getColumnClass(int column) throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        try {
            String className = metaData.getColumnClassName(column + 1);
            return Class.forName(className);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return Object.class; // if problems occur above, assume type Object
    } // end method getColumnClass

    // ���oColumn��
    public int getColumnCount() throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // determine number of columns
        try {
            return metaData.getColumnCount();
        } // end try
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch

        return 0; // if problems occur above, return 0 for number of columns
    } // end method getColumnCount

    // ���oColumn�W��
    public String getColumnName(int column) throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // determine column name
        try {
            return metaData.getColumnName(column + 1);
        } // end try
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch

        return ""; // if problems, return empty string for column name
    } // end method getColumnName

    // ���orow��
    public int getRowCount() throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        return numberOfRows;
    } // end method getRowCount

    // ���oResultSet�̪����e(�Ʀr)
    public Object getValueAt(int row, int column) throws IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // obtain a value at specified ResultSet row and column
        try {
            resultSet.absolute(row + 1); //��pointer���V row+1
            return resultSet.getObject(column + 1); //���o�ƭ�
        } // end try
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch

        return ""; // if problems, return empty string object
    } // end method getValueAt

    // update Query
    public void setQueryUpdate(String query) throws SQLException, IllegalStateException {
        // ensure database connection is available
        if (!connectedToDatabase) {
            throw new IllegalStateException("Not Connected to Database");
        }

        // specify query and execute it
        statement.executeUpdate(query);
        setQuery(DEFAULT_QUERY);
        updateAllPersonList();
    } // end method setQuery

    // update�˩Ҧ��H��List
    public void updateAllPersonList() {
        AllPersonList = new ArrayList<Person>(); //�ŧi�@��ArrayList
        AllPersonList.clear();

        try {
            for (int i = 1; i <= numberOfRows; i++) {
                resultSet.absolute(i);

                Person person = new Person(
                        (Integer) resultSet.getObject(1), //memberID
                        (String) resultSet.getObject(2), //name
                        (String) resultSet.getObject(3), //phone
                        (String) resultSet.getObject(4), //email
                        (String) resultSet.getObject(5) //sex
                );
                AllPersonList.add(person); //�[�JArrayList
            }
        } catch (SQLException e) {
        }
    }//end method updatePersonList()

    //�^�ǿ����Person���O
    public Person getPerson(int selectedRow) {
        return AllPersonList.get(selectedRow);
    }

    //�^�ǩҦ��H��List
    public List<Person> getAllPersonList() {
        AllPersonList = new ArrayList<Person>();
        AllPersonList.clear();

        try {
            for (int i = 1; i <= numberOfRows; i++) {
                resultSet.absolute(i);
                Person person = new Person(
                        (Integer) resultSet.getObject(1), //memberID
                        (String) resultSet.getObject(2), //name
                        (String) resultSet.getObject(3), //phone
                        (String) resultSet.getObject(4), //mail
                        (String) resultSet.getObject(5));//sex

                AllPersonList.add(person);
            }
        } catch (SQLException e) {
        }
        return AllPersonList;
    }//end method getAllPersonList()

    //������PDatabase�_�u
    public void disconnectFromDatabase() {
        if (!connectedToDatabase) {
            return;
        }

        // close Statement and Connection            
        try {
            statement.close();
            connection.close();
        } // end try                                 
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } // end catch                               
        finally // update database connection status
        {
            connectedToDatabase = false;
        } // end finally                             
    } // end method disconnectFromDatabase

}//end ResultSetTableModel
