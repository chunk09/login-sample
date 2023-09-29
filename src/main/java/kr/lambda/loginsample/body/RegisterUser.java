package kr.lambda.loginsample.body;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUser {
    private String id;
    private String password;
    private String confirm;
    private String name;
}
