import com.adrar.sqlcda.model.Task;
import com.adrar.sqlcda.model.User;
import com.adrar.sqlcda.repository.TaskRepository;
import com.adrar.sqlcda.repository.UserRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Task> tasks = TaskRepository.findAll();
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
}
