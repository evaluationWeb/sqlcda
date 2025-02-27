package com.adrar.sqlcda.repository;

import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Category;
import com.adrar.sqlcda.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TaskRepository {

    private static final Connection connection = Bdd.getConnection();

    //Ajouter une Tache avec l'utilisateur
    public static Task save(Task task) {
        Task savedTask = null;
        try {
            String sql = "INSERT INTO task(title, content, end_date, users_id)" +
                    "VALUES (?, ?, ?, (SELECT id FROM uses WHERE firstname = ? AND lastname = ?))";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getContent());
            preparedStatement.setString(3, task.getEndDate().toString());
            preparedStatement.setString(4, task.getUser().getFirstname());
            preparedStatement.setString(5, task.getUser().getLastname());

            if(preparedStatement.executeUpdate() > 0) {
                savedTask = task;
            }
        }
        catch (Exception e) {
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    //Méthode pour ajouter des catégories à une tache
    public static Task saveCategory(Task task) {
        Task savedTask = null;
        try {
            String sql = "INSERT INTO category(category_id, task_id) VALUES";
            for (Category category : task.getCategories()) {
                sql += " (" + category.getId() + ", " + task.getId() + ")";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (preparedStatement.executeUpdate(sql) > 0) {
                savedTask = task;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return savedTask;
    }
}
