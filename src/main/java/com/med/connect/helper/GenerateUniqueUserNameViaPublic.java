package com.med.connect.helper;

import com.med.connect.payload.request.SignUpRequestPublic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenerateUniqueUserNameViaPublic {


        public String  generateUniqueUserNameViaPublic(SignUpRequestPublic signUpRequestPublic)
        {
          String uniqueUserName =   generateRandomNumber(8)
                                      + ":"+ signUpRequestPublic.getFirstname()
                                      + signUpRequestPublic.getSurname()
                                      + ":" + generateRandomNumber(3);

          log.info("Unique User Name : "   + uniqueUserName);

          return uniqueUserName;
        }




    public static String generateRandomNumber(int n)
    {
        String AlphaNumericString =  "0123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

}
