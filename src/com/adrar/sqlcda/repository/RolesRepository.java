package com.adrar.sqlcda.repository;

import com.adrar.sqlcda.db.Bdd;
import com.adrar.sqlcda.model.Roles;
import com.adrar.sqlcda.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolesRepository {
    /*
     * Attributs
     * */

    private static final Connection connection = Bdd.getConnection();

    /*
     * Méthodes (CRUD)
     * */
    //Méthode pour ajouter un Roles
    public static Roles save(Roles roles) {
        Roles rolesNew = new Roles();
       try {
           String sql = "INSERT INTO roles (role_name) VALUES (?)";
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           preparedStatement.setString(1, roles.getRolesName());
           int row  = preparedStatement.executeUpdate();
           if (row > 0) {
               rolesNew = new  Roles(roles.getRolesName());
           }
       }
       catch (Exception e) {
           e.printStackTrace();
       }
       return rolesNew;
    }

    //Méthode pour vérifier si un Roles existe
    public static boolean isExist(String rolesName) {
        boolean getRoles = false;
        try {
            String sql = "SELECT id FROM roles WHERE roles_name = ?";
            //préparer la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind le paramètre
            preparedStatement.setString(1, rolesName);
            //récupérer le resultat de la requête
            ResultSet resultSet = preparedStatement.executeQuery();

            //Vérification du résultat
            while(resultSet.next()){
                getRoles = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return getRoles;
    }

    //Méthode pour récupérer un Roles par son rolesName
    public static Roles findByEmail(String rolesName) {
        Roles findRoles = null;
        try {
            String sql = "SELECT id, roles_name FROM roles WHERE roles_name = ?";
            //Préparer la requête
            PreparedStatement prepare = connection.prepareStatement(sql);
            //Bind un paramètre
            prepare.setString(1, rolesName);
            //Exécuter la requête
            ResultSet resultSet = prepare.executeQuery();
            if(resultSet.next()){
                findRoles = new Roles(rolesName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findRoles;
    }

    //Méthode pour récupérer tous les Roles dans une List
    public static List<Roles> findAll(){
        List<Roles> findRoles = new ArrayList<>();
        try {
            String sql = "SELECT id, roles_name FROM roles";
            //Préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Exécution de la requête
            ResultSet resultSet = preparedStatement.executeQuery();
            //Ajout dans la liste des User
            while(resultSet.next()){
                Roles roles = new Roles();
                roles.setId(resultSet.getInt("id"));
                roles.setRolesName(resultSet.getString("roles_name"));
                findRoles.add(roles);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return findRoles;
    }

    //Méthode pour modifier le nom du Roles
    //Méthode qui met à jour un Roles et retourne Roles modifié
    public static Roles update(Roles roles, String rolesName){
        Roles updateRoles = null;
        try {
            String sql = "UPDATE roles SET roles_name = ? WHERE roles_names = ?";
            //préparation de la requête
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Bind des paramètres
            preparedStatement.setString(1, roles.getRolesName());
            preparedStatement.setString(2, rolesName);
            //Exécution de la requête
            int  nbrRows = preparedStatement.executeUpdate();
            if(nbrRows > 0){
                updateRoles = roles;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateRoles;
    }
}
