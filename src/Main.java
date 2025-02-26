import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.User;

public class Main {
    public static void main(String[] args) {
        //Bdd.getConnection();
        User user = new User();
        User user2 = new User("Mathieu", "Mithridate", "test@test.com" ,"1234");
        System.out.println(user);
        System.out.println(user2);
    }
}
