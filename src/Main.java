import com.adrar.sqlcda.model.Roles;
import com.adrar.sqlcda.model.User;
import com.adrar.sqlcda.repository.UserRepository;

public class Main {
    public static void main(String[] args) {
        //ajout d'un User avec un Roles
        Roles roles = new Roles("User");
        User user = new User();
        user.setFirstname("Pole-numérique");
        user.setLastname("Adrar");
        user.setEmail("pole-numerique@adrar-numerique.com");
        user.setPassword("12345");
        user.setRoles(roles);

        //Ajout en BDD
        UserRepository.saveWithRoles(user);

    }
}
