package com.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterUser
 */
@WebServlet("/registeruser")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("utf-8");
		String uname = request.getParameter("uname");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String res = null;
		if(password.equals(password2)) {
		String sql="insert into manager(managerName,password) values(?,?)";
		int i=JDBCUtil.addGetAutoId(sql, uname,password);
		if(i!=0) {
			res="注册成功";
		}else {
			res="注册失败";
		}
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(res);
		out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doGet(request, response);
	}

}
