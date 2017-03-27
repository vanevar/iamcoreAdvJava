package fr.epita.iam.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.epita.iam.services.IdentityJDBCDAO;

/**
 * @author vanessavargas
 * This class was created to handle the Exceptions in a customized way. 
 * With the objective of having only one type of exception to handle.
 */
public class DaoInitializationException extends RuntimeException{

    /**
   * Added auto generated
   */
  private static final long serialVersionUID = 2974147217574149198L;

  private static final Logger LOGGER = LogManager.getLogger(IdentityJDBCDAO.class);
    public DaoInitializationException()
    {
      //Exists to complete implementation
    }
    
    /**
     * To set custom exception message
     */
    public DaoInitializationException(String message)
    {
      super(message);
      LOGGER.error("Exception ocurred: ", message);
    }
}
