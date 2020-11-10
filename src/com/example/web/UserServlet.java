package com.example.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.dao.UserDao;

import com.example.model.User;

/**
 * Servlet implementation class UserServlet
 */

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao userDAO;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    public void init(){
        userDAO = new UserDao();
        //loginDAO = new LoginDAO();
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String action = request.getServletPath(); //取得網址後路徑
		try {
			switch(action) {
				case "/user/new":
	                showNewForm(request, response);
	                System.out.println("product/new");
	                break;
	            case "/user/insert":
	                insertUser(request, response); //呼叫insertUSER函式
                break;
	            case "/user/edit":
	            	 showEditForm(request, response);
                break;
	            case "/user/update":
                    updateUser(request, response);
                    break;
	           default:
					listUser(request, response);
					break;
			}
			
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
        String name = request.getParameter("name"); //取得input name欄位的資料
        String email = request.getParameter("email"); //取得input email欄位的資料
        String created_at = request.getParameter("created_at"); //取得input created_at欄位資料
        String updated_at = "null"; //因為是新增，所以更新時間設為null
        User newUser = new User(name, email, created_at, updated_at); //建立新的User資料(這裡呼叫的是User的建構子
        userDAO.insertUser(newUser); //執行UserDAO的insertUser函式
        response.sendRedirect("list"); //新增完跳回列表頁

    }
	private void updateUser (HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String created_at = "null";
        String updated_at = request.getParameter("created_at");
        User book = new User(id, name, email, created_at, updated_at);
        userDAO.updateUser(book);
        response.sendRedirect("list");

		
	}
	private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		
		List < User > listUser = userDAO.selectAllUsers();
        int totalrows = listUser.size();
        request.setAttribute("totalrows", totalrows);
        request.setAttribute("listUser", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("../backend/users/list.jsp");
        dispatcher.forward(request, response);
        System.out.println("test");
	}
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("../backend/users/form.jsp");
		
		dispatcher.forward(request, response);
		
	}
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDAO.selectUser(id);
        RequestDispatcher dispatcher = request.getRequestDispatcher("../backend/users/form.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);
		
	}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setCharacterEncoding("UTF-8");
    response.setLocale(Locale.TAIWAN);
		doGet(request, response);
	}
}
