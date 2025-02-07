package com.example.StockStick.Model;
import com.example.StockStick.AllException.CustomException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private Long contactNo;
    private String emailAddress;
    private String password;
    private String confirmPassword;
    private String company;
    private String state;
    private String country;
    @JsonProperty("Bio")
    private String Bio;
    @JsonProperty("Usertype")
    private String Usertype;
    private static final Logger log = LoggerFactory.getLogger(UserDetails.class);
    public boolean NullChecker(Object obj)
    {
        if(obj==null)
        {
            return true;
        }
        for (Field field:obj.getClass().getDeclaredFields())
        {
            try {
                if (field.get(obj) != null) {

                    return false;
                }
            }
            catch (IllegalAccessException e)
            {
                log.error("Error accessing field: {}", field.getName(), e);
                throw new CustomException("Please check your internet connectivity. If the connection is correct, the issue might be on our side. Please wait for 10 minutes or try again later!!!");
            }
        }
        return true;
    }
}
