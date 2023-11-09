import java.sql.*;
import java.util.Properties;

public class JDB {

    private static final String dbClassname = "com.mysql.cj.jdbc.Driver";

    private static final String CONNECTION = "jdbc:mysql://127.0.0.1/sys";

    public static void main(String[] args) throws ClassNotFoundException, SQLException
    
    
    //public methods
    //SELECT METHOD
    //INSRT INTO TABLE 
    //UPDATE
    //DELETE
    //CREATE TABLE 
    //ALTER TABLE 
    //JOIN 
    //GROUP BY
    //ORDER BY 
    

    {
        Properties p = new Properties();

        p.put("user", "root");
        p.put("password", "root");

        Connection c = DriverManager.getConnection(CONNECTION,p);
        Statement stmt = c.createStatement();

        System.out.println("It works");

        String selectDB = "USE persondb;";
        String showDataInStudent = "SELECT * FROM student;";
        ResultSet rs; //this is the data in the DB query
        stmt.executeUpdate(selectDB);
        System.out.println("Selecting DB");
        rs = stmt.executeQuery(showDataInStudent);
        System.out.println("Quering Student Data");
        while(rs.next()) {
            String id = rs.getString("id");
            System.out.println(id);
        }
        c.close();




    }

}
