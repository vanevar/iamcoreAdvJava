package fr.epita.iam.iamcore.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.services.DateFormatManager;
import fr.epita.iam.services.IdentityJDBCDAO;


public class TestJDBC {
  
  private static final Logger LOGGER = LogManager.getLogger(TestLog4j2Usage.class);

  @BeforeClass
  public static void globalSetup() throws SQLException{
    LOGGER.info("Before class initialization.");
    
    Connection connection = DriverManager.getConnection(
        "jdbc:derby:memory:IAM;create=true", "admin", "admin");
    PreparedStatement pstmt = connection.prepareStatement("CREATE TABLE Identities( "
        + "UId INT NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT Identity_PK PRIMARY KEY,"
        + "DisplayName VARCHAR(255),"
        + "Email VARCHAR(255),"
        + "Birthday DATE"
        + ")");
    
    pstmt.execute();
    connection.commit();
    pstmt.close();
    connection.close();
    
    LOGGER.info("Before class initialization DONE.");
  }
  
  @Before
  public void setUp()
  {
    LOGGER.info("Before test setup.");
  }
  
  @Test
  public void TestJDBCConnection(){
    IdentityJDBCDAO dao = new IdentityJDBCDAO();
    Assert.assertNotNull(dao);
  }
  
  @Test
  public void testWriteIdentity(){
    DateFormatManager dfm = new DateFormatManager();
    Identity id = new Identity("Carlos Diez", "2", "cdm@gmailcom", dfm.dateFromString("1980-12-24"));
    IdentityJDBCDAO idJDBC = new IdentityJDBCDAO();
    idJDBC.writeIdentity(id);
    
    List<String> names = new ArrayList<String>();
    for(Identity i : idJDBC.readAllIdentities()){
      names.add(i.getDisplayName());
    }
    Assert.assertTrue(names.contains(id.getDisplayName()));
  }
  
  @Test
  public void testReadAllIdentities(){
    IdentityJDBCDAO idJDBC = new IdentityJDBCDAO();
    Assert.assertFalse(idJDBC.readAllIdentities().isEmpty());
  }
  //@After
}
