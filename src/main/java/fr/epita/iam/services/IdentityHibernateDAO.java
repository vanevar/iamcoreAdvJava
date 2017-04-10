package fr.epita.iam.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DaoInitializationException;

/**
 * @author vanessavargas
 * This class manages the Identity - DB connection and queries.
 */
public class IdentityHibernateDAO {
  
  @Inject
  @Named("dataSourceBean")
  private DataSource ds;
  
  private static final Logger LOGGER = LogManager.getLogger(IdentityHibernateDAO.class);
  
  private IdentityHibernateDAO() throws SQLException {
	  
	}
  
  /**
   * Release the connection resources
   */
  private void releaseResources(Connection conn) {
    LOGGER.debug("=> releaseResources");
    try{
      conn.close();
    }catch(Exception e){
      DaoInitializationException die = new DaoInitializationException("Unable to close the connection to the database.");
      die.initCause(e);
      throw die;
    }
    LOGGER.debug("<= releaseResources");
  }
  
  /**
   * Read all identities persisted on the DB
   * @return
   * @throws DaoInitializationException
   */
  public List<Identity> readAllIdentities() 
  {
    LOGGER.debug("=> readAllIdentities");
    Connection connection;
    List<Identity> listId = new ArrayList<Identity>();
    DateFormatManager dfm = new DateFormatManager();
    PreparedStatement statement = null;
    try {
     connection = ds.getConnection();
    
     statement = connection.prepareStatement("SELECT * from Identities");
     ResultSet result = statement.executeQuery();
    
     while(result.next()){
       String uId = result.getString("Uid");
       String displayName = result.getString("DisplayName");
       String email = result.getString("Email");
       String birthdate = result.getString("Birthday");
       Identity id = new Identity(displayName, uId, email, dfm.dateFromString(birthdate));
       listId.add(id);
        }
      this.releaseResources(connection);
    } catch (Exception e) {
      DaoInitializationException die = new DaoInitializationException("A problem was found during the read of all identities.");
      die.initCause(e);
      throw die;
    } finally {
      if (statement != null)
        try {
          statement.close();
        } catch (SQLException e) {
          LOGGER.error("FAILED: readAllIdentities {}", e);
        }
    }
    return LOGGER.traceExit("<= readAllIdentites : {}", listId);
  }
  
  /**
   * Persist an identity into the DB
   * @param id
   * @throws DaoInitializationException
   */
  public void writeIdentity( Identity id ) 
  {
    LOGGER.debug("=> writeIdentity : tracing the input : {}", id.toString());
    PreparedStatement statement = null;
    if(alreadyExists(id)){
      throw new DaoInitializationException("This identity already exists.");
    }
    
    try{
      Connection connection = ds.getConnection();
      DateFormatManager dfm = new DateFormatManager();
    
      String sql = "INSERT INTO Identities (DisplayName, Email, Birthday) VALUES ( ?, ?, ?)";
      statement = connection.prepareStatement(sql);
      statement.setString(1, id.getDisplayName());
      statement.setString(2, id.getEmail());
      statement.setString(3, dfm.stringFromDate(id.getBirthdate()));
  
      statement.execute();
      this.releaseResources(connection);
    } catch (Exception e) {
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to save the identity on the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (statement != null)
        try {
          statement.close();
        } catch (SQLException e) {
          LOGGER.error("FAILED: writeIdentity Statement close. {}", e);
        }
    }
    LOGGER.debug("<= writeIdentity : Leaving method with no error.");
  }
  
  /**
   * Read one identity from the DB based on the uid for the identity
   * @param uid 
   * @throws SQLException
   */
  public Identity readIdentity(String uid) 
  {
    LOGGER.debug("=> readIdentity : tracing the input : {}", uid);
    DateFormatManager dfm = new DateFormatManager();
    Identity id = new Identity();  
    PreparedStatement statement = null;
    
    try{
      Connection connection = ds.getConnection();
    
      statement = connection.prepareStatement("SELECT * from Identities WHERE UId = ?");
      statement.setString(1, uid);
      ResultSet result = statement.executeQuery();
    
      while(result.next()){
        String uId = result.getString("Uid");
        String displayName = result.getString("DisplayName");
        String email = result.getString("Email");
        String birthdate = result.getString("Birthday");
        id = new Identity(displayName, uId, email, dfm.dateFromString(birthdate));
      }
      this.releaseResources(connection);
    } catch (Exception e)
    {
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to read the identity to the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (statement != null)
        try {
          statement.close();
        } catch (SQLException e) {
          LOGGER.error("FAILED: readIdentity close connection. {}", e);
        }
    }

    return LOGGER.traceExit("<= readIdentity : {}", id);
  }
  
  /**
   * Update the passed identity on the DB, the update is done based on the uid
   * @param id
   * @throws DaoInitializationException
   */
  public void updateIdentity( Identity id ) 
  {
    LOGGER.debug("=> updateIdentity : tracing the input : {}", id.toString());
    PreparedStatement statement = null;
    try{
      Connection connection = ds.getConnection();
      DateFormatManager dfm = new DateFormatManager();
      String sql = "UPDATE Identities SET DisplayName = ?, Email = ?, Birthday = ? WHERE UId = ?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, id.getDisplayName());
      statement.setString(2, id.getEmail());
      statement.setString(3, dfm.stringFromDate(id.getBirthdate()));
      statement.setString(4, id.getUid());
    
      statement.execute();
      this.releaseResources(connection);
    } catch (Exception e) {
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to update the identity to the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (statement != null)
        try {
          statement.close();
        } catch (SQLException e) {
          LOGGER.error("FAILED: updateIdentity. {}", e);
        }
    }
    LOGGER.debug("<= readIdentity : Leaving with no errors");
  }
  
  /**
   * Delete the identity from the DB
   * @param id
   * @throws DaoInitializationException
   */
  public void deleteIdentity( Identity id ) 
  {
    LOGGER.debug("=> deleteIdentity : {}", id);
    PreparedStatement statement = null;
    try{
    Connection connection = ds.getConnection();
      String sql = "DELETE FROM Identities WHERE UId = ?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, id.getUid());
    
      statement.execute();
      this.releaseResources(connection);
    }  catch (Exception e) {
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to remove the identity from the database");
      die.initCause(e);
      throw die;
    }finally {
      if (statement != null)
        try {
          statement.close();
        } catch (SQLException e) {
          LOGGER.error("FAILED: deleteIdentity. {}", e);
        }
    }
    LOGGER.debug("<= deleteIdentity");
  }

  private Boolean alreadyExists(Identity id){
    for(Identity id2 : readAllIdentities())
    {
      if (id.getDisplayName().equals(id2.getDisplayName()) &&
          id.getEmail().equals(id2.getEmail()) &&
          id.getBirthdate().equals(id2.getBirthdate()))
          return true;
    }
    return false;
  }
}
