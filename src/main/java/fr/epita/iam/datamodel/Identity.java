package fr.epita.iam.datamodel;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.epita.iam.services.DateFormatManager;

@Entity
@Table(name="Identities")
public class Identity {
  // Properties
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="UId")
  private long uid;
  
  @Column(name="DisplayName")
  private String displayName;

  @Column(name="Email")
  private String email;
  
  @Column(name="Birthday")
  private Date birthdate;
  
//  @OneToMany(mappedBy="identity", cascade = CascadeType.ALL)
//  private List<Address> addresses;
  
  //Constructors
  public Identity (){
    //Mandatory constructor
  }
  
  public Identity( String displayName, String email, Date birthdate, List<Address> addresses){
    this.displayName = displayName;
    this.email = email;
    this.birthdate = birthdate;
    //this.addresses = addresses;
  }
  
  // Getters & Setters
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public long getUid() {
    return uid;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public Date getBirthdate() {
    return birthdate;
  }
  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }
  
//  public List<Address> getAddresses() {
//    return addresses;
//  }

  
  @Override
  public String toString()
  {
    DateFormatManager dfm = new DateFormatManager();
    String identity = "Identity [ uid = " + this.uid + ", displayName = " + this.displayName +  
        ", email = " + this.email + ", birthdate = " + dfm.stringFromDate(this.birthdate);
//    if(!this.addresses.isEmpty()){
//      for(Address addr : this.addresses)
//      {
//        identity += ", Addresses: " + addr.toString();
//      }
//    }
//    
    return identity + "]";
        
  }
}
