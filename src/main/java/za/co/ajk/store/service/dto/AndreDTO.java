package za.co.ajk.store.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Andre entity.
 */
public class AndreDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5, max = 45)
    private String user_name;

    private String passwd;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AndreDTO andreDTO = (AndreDTO) o;

        if ( ! Objects.equals(id, andreDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AndreDTO{" +
            "id=" + id +
            ", user_name='" + user_name + "'" +
            ", passwd='" + passwd + "'" +
            '}';
    }
}
