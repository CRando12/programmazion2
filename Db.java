package monkey;
import java.io.IOException;
import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Db
{
    private static String HOST;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DBNAME;
    private static String CONN_STRING;
    private Connection conn;
    Statement stmt;
    
    public Db() 
    {
        conn = null;
        
        try {
            File configFile = new File("src/monkey/config.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = null;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            Document doc = null;
            try {
                doc = dBuilder.parse(configFile);
            } catch (SAXException ex) {
                Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            }

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("config");

            for (int temp = 0; temp < nodeList.getLength(); temp++)
            {
                Node node = nodeList.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                    HOST = element.getElementsByTagName("host").item(0).getTextContent();
                    USERNAME = element.getElementsByTagName("username").item(0).getTextContent();
                    PASSWORD = element.getElementsByTagName("password").item(0).getTextContent();
                    DBNAME = element.getElementsByTagName("dbname").item(0).getTextContent();
                    
                    // Usa i valori estratti come desideri
                    System.out.println("Host: " + HOST);
                    System.out.println("Username: " + USERNAME);
                    System.out.println("Password: " + PASSWORD);
                    System.out.println("DB Name: " + DBNAME);
                }
            }
            
            CONN_STRING = "jdbc:mysql://" + HOST + "/" + DBNAME;
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        
        } catch (SQLException e) {
            System.err.println("Connessione al database fallita.");
            System.err.println("Controlla di aver aperto XAMPP o il server MySQL!");
            System.out.println(e);
        }
    }
    
    public Connection GetConn(){
        return conn;
    }
    
    public static Object getColFromPlayers(String column, String nickname) throws SQLException
    {
        Connection connex = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        
        String query = "SELECT * FROM players WHERE nickname = '" + nickname + "'";
        Statement stmt = (Statement) connex.createStatement();
        ResultSet result = stmt.executeQuery(query);

        if(result.next())
        {
            return result.getInt(column);
        }
        else 
        {
            System.err.println("Errore nella query getColFromPlayers, parametro: "+ column);
            return null;
        }
    }
    
    public static Object updateColForPlayers(String column, Object value, String nickname) throws SQLException
    {
        Connection connex = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        
        String query = "UPDATE players SET " + column + " = '" + value + "' WHERE nickname = '" + nickname + "'";
        int result = connex.createStatement().executeUpdate(query);

        if(result > 0)
        {
            return true;
        }
        else 
        {
            System.err.println("Errore nella query updateColForPlayers, parametro: "+ column);
            return false;
        }
    }
    
    public static Object incrementColForPlayers(String column, int value, String nickname) throws SQLException
    {
        Connection connex = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        
        String query = "UPDATE players SET " + column + " = " + column + " + " + value + " WHERE nickname = '" + nickname + "'";
        int result = connex.createStatement().executeUpdate(query);

        if(result > 0)
        {
            return true;
        }
        else 
        {
            System.err.println("Errore nella query updateColForPlayers, parametro: "+ column);
            return false;
        }
    }
        
}