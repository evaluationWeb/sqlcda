import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.User;
import com.adrar.sqlcda.repository.UserRepository;

public class Main {
    public static void main(String[] args) {
        User newUser = new User(
                "Mathieu",
                "Mithridate",
                "mathieu1@gmail.com",
                "123456"
        );
        boolean exist = UserRepository.isExist("mathieu1@gmail.com");
        if(exist) {
            System.out.println("Le compte existe");
        }
        else {
            System.out.println("Le compte n'existe pas");
        }
    }
}
