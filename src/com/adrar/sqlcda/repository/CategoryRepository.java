package com.adrar.sqlcda.repository;

import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Category;
import com.adrar.sqlcda.model.Roles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    /*
     * Attributs
     * */

    private static final Connection connection = Bdd.getConnection();

    /*
     * Méthodes (CRUD)
     * */
    //Méthode pour ajouter une Category
    public static Category save(Category category) {
        Category categoryNew = null;
        try {
            String sql = "INSERT INTO category (category_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, category.getCategoryName());
            int rows =   preparedStatement.executeUpdate();
            if (rows > 0) {
                categoryNew = new Category(category.getCategoryName());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return  categoryNew;
    }

    //Méthode pour vérifier si une Category existe
    public static boolean isExist(String categoryName) {
        boolean getCategory = false;
        try {
            String sql = "SELECT id FROM category WHERE category_name = ?";
            //préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind le paramètre
            preparedStatement.setString(1, categoryName);
            //récupérer le resultat de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            //Vérification du résultat
            while(resultSet.next()){
                getCategory = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return getCategory;
    }

    //Méthode pour récupérer un Roles par son categoryName
    public static Category findByName(String categoryName) {
        Category findCategory = null;
        try {
            String sql = "SELECT id, category_name FROM category WHERE category_name = ?";
            //Préparer la requête
            PreparedStatement prepare = connection.prepareStatement(sql);
            //Bind un paramètre
            prepare.setString(1, categoryName);
            //Exécuter la requête
            ResultSet resultSet = prepare.executeQuery();
            if(resultSet.next()){
                findCategory = new Category(categoryName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findCategory;
    }

    //Méthode pour récupérer tous les Category dans une List
    public static List<Category> findAll(){
        List<Category> findCategories = new ArrayList<>();
        try {
            String sql = "SELECT id, category_name FROM category";
            //Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();
            //Ajout dans la liste des User
            while(resultSet.next()){
                Category category = new Category(resultSet.getString("category_name"));
                findCategories.add(category);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return findCategories;
    }

    //Méthode qui met à jour une Category et retourne Category modifié
    public static Category update(Category category, String categoryName){
        Category updateCategory = null;
        try {
            String sql = "UPDATE category SET category_name = ? WHERE category_names = ?";
            //préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind des paramètres
            preparedStatement.setString(1, category.getCategoryName());
            preparedStatement.setString(2, categoryName);
            //Exécution de la requête
            int  nbrRows = preparedStatement.executeUpdate();
            if(nbrRows > 0){
                updateCategory = category;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateCategory;
    }
}
