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
                    "VALUES (?, ?, ?, (SELECT id FROM users WHERE firstname = ? AND lastname = ?))";
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
            String sql = "INSERT INTO task_category(category_id, task_id) VALUES";
            for (Category category : task.getCategories()) {
                sql += " (" + category.getId() + ", " + task.getId() + ")";
                sql += category.getId() != task.getCategories().getLast().getId()?" ,":"";
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

    //Méthode pour vérifier si une tache existe
    public static boolean isExist(Task task) {
       boolean  exist = false;
       try {
           String sql = "SELECT t.id FROM task as t WHERE t.title = ? AND" +
                   " date(t.create_at) = ?";
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           preparedStatement.setString(1, task.getTitle());
           preparedStatement.setString(2, task.getCreateAt().toString());
           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()) {
               exist = true;
           }
       }
       catch (Exception e) {
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
            if(resultSet.next()) {
                getTask = task;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return getTask;
    }

    //Méthode pour récupérer la liste des taches

}
