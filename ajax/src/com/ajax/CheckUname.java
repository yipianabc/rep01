package com.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckUname
 */
@WebServlet("/CheckUname")
public class CheckUname extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static List<String> list = new ArrayList<>();
	static {
		list.add("admin");
		list.add("xmetc");
		list.add("itit");
	}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUname() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");//������Ӧ�����ݼ�����
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String uname=request.getParameter("uname");
		String sql = "select * from manager where managerName='"+uname+"'";
		String res;
		//����û�������
		if(!JDBCUtil.query1(sql)) {
			res="用户名可用";
		}else {
			res="用户名存在";
		}
		PrintWriter out=response.getWriter();
		out.print(res);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
