package com.adrar.sqlcda.repository;

import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Category;
import com.adrar.sqlcda.model.Roles;
import com.adrar.sqlcda.model.Task;
import com.adrar.sqlcda.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private static final Connection connection = Bdd.getConnection();

    //Ajouter une Tache avec l'utilisateur
    public static Task save(Task task) {
        Task savedTask = null;
        try {
            String sql = "INSERT INTO task(title, content, end_date, users_id)" +
                    "VALUES (?, ?, ?, (SELECT id FROM users WHERE firstname = ? AND lastname = ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getContent());
            preparedStatement.setString(3, task.getEndDate().toString());
            preparedStatement.setString(4, task.getUser().getFirstname());
            preparedStatement.setString(5, task.getUser().getLastname());

            if (preparedStatement.executeUpdate() > 0) {
                savedTask = task;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedTask;
    }

    //Méthode qui retourne la dernière tache
    public static int lastId() {
        int id = 0;
        try {
            String sql = "SELECT MAX(id) FROM task";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql);
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    //Méthode pour ajouter des catégories à une tache
    public static Task saveCategory(Task task) {
        Task savedTask = null;
        try {
            String sql = "INSERT INTO task_category(category_id, task_id) VALUES";
            for (Category category : task.getCategories()) {
                sql += " (" + category.getId() + ", " + task.getId() + ")";
                sql += category.getId() != task.getCategories().getLast().getId() ? ", " : "";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (preparedStatement.executeUpdate(sql) > 0) {
                savedTask = task;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedTask;
    }

    //Méthode pour vérifier si une tache existe
    public static boolean isExist(Task task) {
        boolean exist = false;
        try {
            String sql = "SELECT t.id FROM task as t WHERE t.title = ? AND" +
                    " date(t.create_at) = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getCreateAt().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                exist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }

    //Méthode pour récupérer une tache
    public static Task findBy(Task task) {
        Task getTask = null;
        try {
            String sql = "SELECT t.id, t.title, t.content, t.create_at u.id AS uId, u.firstname, " +
                    "u.lastname FROM task as t " +
                    "INNER JOIN users AS u ON t.users_id = u.id" +
                    "WHERE t.title = ?" +
                    "AND date(t.create_at) = ? AND users_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getCreateAt().toString());
            preparedStatement.setInt(3, task.getUser().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                getTask = task;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getTask;
    }

    //Méthode pour récupérer la liste des taches
    public static List<Task> findAll() {
        List<Task> getAll = new ArrayList<>();
        try {
            String sql = "SELECT t.id AS tId, t.title, t.content, t.create_at, t.end_date, t.`status`, \n" +
                    "u.id AS uId, u.firstname, u.lastname, r.id AS rId, r.roles_name AS rName,\n" +
                    "group_concat(c.id) AS catId,\n" +
                    "group_concat(c.category_name) AS catName \n" +
                    "FROM task AS t\n" +
                    "INNER JOIN users AS u ON t.users_id = u.id\n" +
                    "LEFT JOIN roles AS r On u.roles_id = r.id\n" +
                    "LEFT JOIN task_category AS tc ON tc.task_id = t.id\n" +
                    "LEFT JOIN category AS c ON tc.category_id = c.id\n" +
                    "GROUP BY t.id;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //Création de la Task
                Task task = new Task();
                task.setId(resultSet.getInt("tId"));
                task.setTitle(resultSet.getString("title"));
                task.setContent(resultSet.getString("content"));
                task.setCreateAt(resultSet.getDate("create_at"));
                task.setEndDate(resultSet.getDate("end_date"));
                task.setCreateAt(resultSet.getDate("create_at"));
                task.setStatus(resultSet.getBoolean("status"));
                //Si la tache posséde un User
                if (resultSet.getString("firstname") != null) {
                    //Création d'un User
                    User user = new User();
                    user.setId(resultSet.getInt("uId"));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setLastname(resultSet.getString("lastname"));
                    //Si le User possède un Role
                    if (resultSet.getString("rName") != null) {
                        //Création d'un Roles
                        Roles roles = new Roles();
                        roles.setId(resultSet.getInt("rId"));
                        roles.setRolesName(resultSet.getString("rName"));
                        user.setRoles(roles);
                        task.setUser(user);
                    }
                }
                //test si la tache posséde des Category
                if (resultSet.getString("catId") != null &&
                        resultSet.getString("catName") != null) {
                    //transformer les chaines en tableau
                    String[] catIds = resultSet.getString("catId").split(",");
                    String[] catNames = resultSet.getString("catName").split(",");
                    for (int i = 0; i < catIds.length; i++) {
                        Category category = new Category();
                        //Convertir la chaine en Int
                        category.setId(Integer.parseInt(catIds[i]));
                        category.setCategoryName(catNames[i]);
                        task.addCategory(category);
                    }
                }
                getAll.add(task);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getAll;
    }
}
