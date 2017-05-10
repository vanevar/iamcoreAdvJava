package fr.epita.iam.services;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.epita.iam.datamodel.Address;
import fr.epita.iam.datamodel.Identity;
import fr.epita.iam.iamcore.test.TestHibernateDAO;

public class WhereBuilder {
  
  private static final Logger LOGGER = LogManager.getLogger(TestHibernateDAO.class);
  
//  public String getWhereClause(Object obj) throws IllegalArgumentException, IllegalAccessException
//  {
//    String result ="";
//    Field[] fields = obj.getClass().getFields();
//    for(int i = 0; i<fields.length; i++){
//      Field field = fields[i];
//      field.setAccessible(true);
//      LOGGER.info(fields[i].toString());
//      LOGGER.info(field.get(obj));
//      LOGGER.info(field.getName());
//      //LOGGER.info(fieldValue);
//      //String fieldWhereClause = field.getName() + " = " + fieldValue;
//      //result += fieldWhereClause;
//    }
////    if(!Whereclass.isEmpty)
////    {
////      result = "where "+ whereClause;
////    }
//    return result;
//  }

  public Query identityWhere(Session session, Identity identity)
  {
    Map<String, Object> values = new LinkedHashMap<String, Object>();
    String queryString = "from Identity as i where 1 = 1 ";
    if(0L != identity.getUid())
    {
      queryString += " and i.Uid = :uid ";
      values.put("uid", identity.getUid());
    }
    
    if(identity.getDisplayName() != null)
    {
      queryString+=" and lower(i.displayName) like lower(:displayName) ";
      values.put("displayName", identity.getDisplayName());
    }
    
    if(identity.getEmail() != null)
    {
      queryString+=" and lower(i.Email) like lower(:email) ";
      values.put("email", identity.getEmail());
    }
      
    if(identity.getBirthdate() != null)
    {
      queryString+=" and i.birthdate = :birthday ";
      values.put("birthday", identity.getBirthdate());
    }
    LOGGER.info("Build where: {}", queryString);
    Query query = session.createQuery(queryString);
    String[] namedParameters = query.getNamedParameters();
    for (String parameter : namedParameters) {
      if(parameter.equals("birthday"))
      {
        query.setDate(parameter, (Date) values.get(parameter));
      }
      else
      {
      query.setParameter(parameter, "%"+values.get(parameter)+"%");
      }
    }
    return query;
  }

  public Query addrWhere(Session session, Address addr)
  {
    Map<String, Object> values = new LinkedHashMap<String, Object>();
    String queryString = "from Address as a where 1 = 1 ";
    if(0L != addr.getaId())
    {
      queryString += " and a.aId = :aid ";
      values.put("aid", addr.getaId());
    }
    
    if(addr.getFirstLine() != null)
    {
      queryString+=" and lower(a.firstLine) like lower(:firstLine) ";
      values.put("firstLine", addr.getFirstLine());
    }
      
    if(addr.getSecondLine() != null)
    {
      queryString+=" and lower(a.secondLine) like lower(:secondLine) ";
      values.put("secondLine", addr.getSecondLine());
    }
    
    if(addr.getCity() != null)
    {
      queryString+=" and lower(a.city) like lower(:city) ";
      values.put("city", addr.getCity());
    }
    
    if(addr.getPostalCode() != null)
    {
      queryString+=" and lower(a.postalCode) like lower(:postalCode) ";
      values.put("postalCode", addr.getPostalCode());
    }
    
    if(addr.getCountry() != null)
    {
      queryString+=" and lower(a.country) like lower(:country) ";
      values.put("country", addr.getCountry());
    }
    
    if(addr.getIdentity() != null)
    {
      queryString+=" and a.id_Id = :id_Id) ";
      values.put("id_Id", addr.getIdentityId());
    }
    LOGGER.info("Build where: {}", queryString);
    Query query = session.createQuery(queryString);
    String[] namedParameters = query.getNamedParameters();
    for (String parameter : namedParameters) {
      if(parameter.equals("aid") || parameter.equals("id_Id") )
      {
        query.setParameter(parameter, values.get(parameter));
      }
      else
      {
      query.setParameter(parameter, "%"+values.get(parameter)+"%");
      }
    }
    return query;
  }

}
