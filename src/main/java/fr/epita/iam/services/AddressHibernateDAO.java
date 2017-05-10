package fr.epita.iam.services;

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

import fr.epita.iam.datamodel.Address;
import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.exceptions.DaoInitializationException;

public class AddressHibernateDAO implements Dao<Address>{
  @Inject
  @Named("sFactory")
  private SessionFactory sf;
  
  private static final Logger LOGGER = LogManager.getLogger(IdentityHibernateDAO.class);

  public void write(Address addr) {
    LOGGER.debug("=> writeAddress : tracing the input : {}", addr.toString());

    Session session = null;
    try {
      session = sf.openSession();
      session.save(addr);
    } catch (Exception e) {
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to save the address on the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: writeAddress Statement close. {}", e);
        }
    }
    LOGGER.debug("<= writeAddress : Leaving method with no error.");
  }

  public void update(Address addr) {
    LOGGER.debug("=> updateAddress : tracing the input : {}", addr.toString());
    Transaction tx = null;
    Session session = null;
    try{
      session = sf.openSession();
      tx = session.beginTransaction();
      session.update(addr);
      tx.commit();
    } catch (Exception e) {
      if(tx != null)
      {
        tx.rollback();
      }
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to update the address to the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: updateAddress. {}", e);
        }
    }
    LOGGER.debug("<= readAddress : Leaving with no errors");
  }

  public void delete(Address addr) {
    LOGGER.debug("=> deleteAddress : {}", addr);
    Session session = null;
    Transaction tx = null;
    try{
      session = sf.openSession();
      tx = session.beginTransaction();
      session.delete(addr);
      tx.commit();
    }  catch (Exception e) {
      if(tx != null)
      {
        tx.rollback();
      }
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to remove the address from the database");
      die.initCause(e);
      throw die;
    }finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: deleteAddress. {}", e);
        }
    }
    LOGGER.debug("<= deleteAddress");
  }

  public List<Address> search(Address addr) {
    LOGGER.debug("=> searchAddress : tracing the input : {}", addr);
    Session session = null;
    List<Address> addressList = null;
    try{
      WhereBuilder wb = new WhereBuilder();
      session = sf.openSession();
      Query query = wb.addrWhere(session, addr);
      addressList = query.list();
      if(addressList.isEmpty())
      {
        LOGGER.info("No Address found with the given criteria.");
      }
    } catch(Exception e){
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to search the address in the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: searchAddress close connection. {}", e);
        }
    }
    LOGGER.debug("<= searchAddress : Leaving with no errors");
    return addressList;
  }

  public List<Identity> searchIdentityByAddr(Address addr) {
    LOGGER.debug("=> searchIdentityByAddr : tracing the input : {}", addr);
    Session session = null;
    List<Address> addressList = null;
    List<Identity> identities = new ArrayList<Identity>();
    try{
      WhereBuilder wb = new WhereBuilder();
      session = sf.openSession();
      Query query = wb.addrWhere(session, addr);
      addressList = query.list();
      if(addressList.isEmpty())
      {
        LOGGER.info("No Address found with the given criteria.");
      }
    } catch(Exception e){
      DaoInitializationException die = new DaoInitializationException("A problem was encountered when trying to search the address in the database.");
      die.initCause(e);
      throw die;
    } finally {
      if (session != null)
        try {
          session.close();
        } catch (Exception e) {
          LOGGER.error("FAILED: searchIdentityByAddr close connection. {}", e);
        }
    }
    LOGGER.debug("<= searchIdentityByAddr : Leaving with no errors");
    for(Address address : addressList)
    {
      if(address.getIdentity() != null)
        identities.add(address.getIdentity());
    }
    return identities;
  }
}
