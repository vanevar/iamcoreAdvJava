package fr.epita.iam.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DaoInitializationException;

/**
 * @author vanessavargas
 * This class manages the Identity - DB connection and queries.
 */
public class IdentityHibernateDAO {
  
  @Inject
  @Named("sFactory")
  private SessionFactory sf;
  
  private static final Logger LOGGER = LogManager.getLogger(IdentityHibernateDAO.class);
  
  private IdentityHibernateDAO() throws SQLException {
	  
	}
  
  /**
 * Persist an identity into the DB
 * @param id
 * @throws DaoInitializationException
 */
public void writeIdentity( Identity id ) 
{
  LOGGER.debug("=> writeIdentity : tracing the input : {}", id.toString());

  Session session = null;
  try {
    session = sf.openSession();
    session.save(id);
  } catch (Exception e) {
    DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to save the identity on the database.");
    die.initCause(e);
    throw die;
  } finally {
    if (session != null)
      try {
        session.close();
      } catch (Exception e) {
        LOGGER.error("FAILED: writeIdentity Statement close. {}", e);
      }
  }
  LOGGER.debug("<= writeIdentity : Leaving method with no error.");
}
  
  
  /**
   * Read all identities persisted on the DB
   * @return
   * @throws DaoInitializationException
   */
  public List<Identity> readAllIdentities() 
  {
    LOGGER.debug("=> readAllIdentities");
    Session session = null;
    List<Identity> listId = new ArrayList<Identity>();
    try {
     session = sf.openSession();
    
     Query query = session.createQuery("from Identity");
     listId = query.list();
    } catch (Exception e) {
      DaoInitializationException die = new DaoInitializationException("A problem was found during the read of all identities.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: readAllIdentities {}", e);
        }
    }
    return LOGGER.traceExit("<= readAllIdentites : {}", listId);
  }
  
  
  
  /**
   * Read one identity from the DB based on the uid for the identity
   * @param uid 
   * @throws DaoInitializationException
   */
  public Identity readIdentity(long uid) 
  {
    LOGGER.debug("=> readIdentity : tracing the input : {}", uid);
    Identity id = new Identity();  
    Session session = null;
    
    try{
      session = sf.openSession();
      String statement = "from Identity i where i.uid = :id";
      List<Identity> identities = session.createQuery(statement).setLong("id", uid).list();
      if(identities.isEmpty())
      {
        LOGGER.info("No identity found for the given UId");
        throw new Exception("No record found in database");
      }
        id = identities.get(0);
    } catch (Exception e)
    {
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to read the identity to the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: readIdentity close connection. {}", e);
        }
    }

    return LOGGER.traceExit("<= readIdentity : {}", id);
  }

  /**
   * Find all Identities where the given display name could match
   * @param displayName 
   * @throws DaoInitializationException
   */
  public Identity findByDisplayName(String displayName) 
  {
    LOGGER.debug("=> readIdentity : tracing the input : {}", displayName);
    Identity id = new Identity();  
    Session session = null;
    
    try{
      session = sf.openSession();
      String statement = "from Identity i where lower(i.displayName) like lower(:dispName)";
      List<Identity> identities = session.createQuery(statement).setParameter("dispName", "%"+displayName+"%").list();
      if(identities.isEmpty())
      {
        LOGGER.info("No identity found containing "+ displayName +" as part of the Display Name");
        throw new Exception("No record found in database");
      }
        id = identities.get(0);
    } catch (Exception e)
    {
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to read the identity to the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
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
    Transaction tx = null;
    Session session = null;
    try{
      session = sf.openSession();
      tx = session.beginTransaction();
      session.update(id);
      tx.commit();
    } catch (Exception e) {
      if(tx != null)
      {
        tx.rollback();
      }
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to update the identity to the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
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
    Session session = null;
    Transaction tx = null;
    try{
      session = sf.openSession();
      tx = session.beginTransaction();
      session.delete(id);
      tx.commit();
    }  catch (Exception e) {
      if(tx != null)
      {
        tx.rollback();
      }
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to remove the identity from the database");
      die.initCause(e);
      throw die;
    }finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: deleteIdentity. {}", e);
        }
    }
    LOGGER.debug("<= deleteIdentity");
  }
//
//  private Boolean alreadyExists(Identity id){
//    for(Identity id2 : readAllIdentities())
//    {
//      if (id.getDisplayName().equals(id2.getDisplayName()) &&
//          id.getEmail().equals(id2.getEmail()) &&
//          id.getBirthdate().equals(id2.getBirthdate()))
//          return true;
//    }
//    return false;
//  }
}
