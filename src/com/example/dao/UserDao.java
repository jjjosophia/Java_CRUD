package com.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.User;


public class UserDao {
	private String jdbcURL = "jdbc:mysql://localhost:3306/cake_data?serverTimezone=UTC&useSSL=false&characterEncoding=UTF-8";
    private String jdbcUsername = "cake_data";
    private String jdbcPass = "cake_data";
    private static final String INSERT_USER_SQL = "INSERT INTO users (name, email, created_at) VALUES (?, ?, ?);";
    private static final String SELECT_ALL_USERS = "select * from users order by `created_at` desc";
    private static final String SELECT_USER = "select * from users where id=?";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ? ,updated_at= ? where id = ?;";
	public UserDao() {
		super();
		// TODO Auto-generated constructor stub
	}
	protected Connection getConnection(){
        Connection connection = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPass);

        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return connection;
    }
	public List< User > selectAllUsers(){
        List< User > users = new ArrayList<>();

        try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String created_at = rs.getString("created_at");
                String updated_at = rs.getString("updated_at");
                users.add(new User(id, name, email,created_at,updated_at));
            }

        }catch(SQLException e){
            e.printStackTrace();;
        }
        return users;
    }
	public void insertUser(User user) throws SQLException {
		 try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);){
			 preparedStatement.setString(1, user.getName()); //對應欄位取得User model中的name
	            preparedStatement.setString(2, user.getEmail());//對應欄位取得User model中的email
	            preparedStatement.setString(3, user.getCreated_at());//對應欄位取得User model中的created_at
	            preparedStatement.executeUpdate(); //執行insert的sql語法
	        } catch (SQLException e) {
	           e.printStackTrace();
	        }
	    }
	public User selectUser(int id) {
		User user=null;
		 try(Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER);){
			 preparedStatement.setInt(1,id);
			 ResultSet rs = preparedStatement.executeQuery();
			 while(rs.next()){	             
	                String name = rs.getString("name");
	                String email = rs.getString("email");
	                String created_at = rs.getString("created_at");
	                String updated_at = rs.getString("updated_at");
	                user=new User(id, name, email,created_at,updated_at);
	            }

	        }catch(SQLException e){
	            e.printStackTrace();
	        }
	        return user;
	    }
	 public boolean updateUser(User user) throws SQLException{
	        boolean rowUpdated;
	        try( Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);){
	            statement.setString(1, user.getName());
	            statement.setString(2, user.getEmail());
	            statement.setString(3, user.getUpdated_at());
	            statement.setInt(4, user.getId());
	            rowUpdated = statement.executeUpdate() > 0;
	        }
	        return rowUpdated;
	    }
	 public boolean deleteUser(int id) throws SQLException{
	        boolean rowDeleted;
	        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);){
	            statement.setInt(1, id);
	            rowDeleted = statement.executeUpdate() > 0;
	        }
	        return rowDeleted;
	    }
	}

