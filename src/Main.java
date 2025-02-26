import com.adrar.sqlcda.model.User;
import com.adrar.sqlcda.repository.UserRepository;

public class Main {
    public static void main(String[] args) {
        User newUser = new User("Mathieu", "Mithridate", "mathieu@gmail.com", "123456");
        UserRepository.save(newUser);
    }
}
