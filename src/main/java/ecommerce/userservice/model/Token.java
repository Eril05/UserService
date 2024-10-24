package ecommerce.userservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Token extends BaseModel{

    private String value;
    private Date expiresAt;
    @ManyToOne
    private User user;

}
