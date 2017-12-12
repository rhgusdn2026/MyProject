package com.sist.controller;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sist.model.Model;
import com.sist.model.MovieDetail;
import com.sist.model.MovieList;

import java.util.*; // Map(��û => Ŭ����(��) ��Ī)

@WebServlet("/DispatcherServlet")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String[] strCls= {
		"com.sist.model.MovieList",
		"com.sist.model.MovieDetail",
	};
    private String[] strCmd= {
    	"list","detail"
    };
    // <bean id="list" class="com.sist.model.MovieList"/> <== �������ڵ�
    // csv => list,com.sist.model.MovieList
    /*
     *  key        value
     *	list new MovieList() Class.forName()
     *  detail new MovieDetail()
     */
    private Map clsMap=new HashMap();
    // HashMap,HashTable
	public void init(ServletConfig config) throws ServletException {
		try
		{
			for(int i=0; i<strCls.length; i++)
			{
				Class clsName=Class.forName(strCls[i]);
				Object obj=clsName.newInstance();
				clsMap.put(strCmd[i], obj);
				// Singleton
			}
			//clsMap.put("list", new MovieList());		//����for���̶� �����ڵ� ������ put�ҰԸ����� for�� �����°� ����
			//clsMap.put("detail", new MovieDetail());
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			// list.do , detail.do => movie.do?cmd=list
			String cmd=request.getRequestURI();
			// URI : ����ڰ� �ּ��Է¶��� ��û�� ����
			// http://localhost:8080/MVCProject1/list.do
			// URI : /MVCProject1/list.do
			//		 ============ ContextPath.length()+1 /�������������ʹϱ� +1�ذ�
			// ����ڰ� ��û�� ����
			cmd=cmd.substring(request.getContextPath().length()+1, cmd.lastIndexOf("."));
			// ��û�� ó�� => ��Ŭ���� (Ŭ����,�޼ҵ�)
			Model model=(Model)clsMap.get(cmd);
			// model => ������ ���Ŀ� ����� request�� ��� �޶�
			// Call By Reference => �ּҸ� �Ѱ��ְ� �ּҿ� ���� ä���
			String jsp=model.execute(request);
			// JSP�� request,session���� ����
			RequestDispatcher rd=
					request.getRequestDispatcher(jsp);
			rd.forward(request, response);
			// jsp�� _jspService()�� ȣ���Ѵ�
			/*
			 * 	service(requset,response)
			 * 	{
			 * 		_jspService(request)
			 * 	}
			 */
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}

















