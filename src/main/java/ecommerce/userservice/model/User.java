package ecommerce.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity(name="users")
public class User extends BaseModel{

    private String username;
    private String password;
    private String email;
    @ManyToMany
    private List<Role>userRoles;
    private boolean isVerified;
}
