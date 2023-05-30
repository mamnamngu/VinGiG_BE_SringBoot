package com.swp.VinGiG;

import java.util.Date;  
public class User   
{  
public User(Integer id, String name, Date dob)   
{  
super();  
this.id = id;  
this.name = name;  
this.dob = dob;  
}  
private Integer id;  
private String name;  
private Date dob;  
public Integer getId()   
{  
return id;  
}  
public void setId(Integer id)   
{  
this.id = id;  
}  
public String getName()   
{  
return name;  
}  
public void setName(String name)   
{  
this.name = name;  
}  
public Date getDob()   
{  
return dob;  
}  
public void setDob(Date dob)   
{  
this.dob = dob;  
}  
@Override  
public String toString()   
{  
//return "User [id=" + id + ", name=" + name + ", dob=" + dob + "]";  
return String.format("User [id=%s, name=%s, dob=%s]", id, name, dob);  
}  
}  