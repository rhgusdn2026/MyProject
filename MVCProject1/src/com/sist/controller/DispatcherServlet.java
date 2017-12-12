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

import java.util.*; // Map(요청 => 클래스(모델) 매칭)

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
    // <bean id="list" class="com.sist.model.MovieList"/> <== 스프링코드
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
			//clsMap.put("list", new MovieList());		//위에for문이랑 동일코드 하지만 put할게많으면 for문 돌리는게 좋다
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
			// URI : 사용자가 주소입력란에 요청한 파일
			// http://localhost:8080/MVCProject1/list.do
			// URI : /MVCProject1/list.do
			//		 ============ ContextPath.length()+1 /슬러쉬다음부터니깐 +1준거
			// 사용자가 요청한 내용
			cmd=cmd.substring(request.getContextPath().length()+1, cmd.lastIndexOf("."));
			// 요청을 처리 => 모델클래스 (클래스,메소드)
			Model model=(Model)clsMap.get(cmd);
			// model => 실행을 한후에 결과를 request에 담아 달라
			// Call By Reference => 주소를 넘겨주고 주소에 값을 채운다
			String jsp=model.execute(request);
			// JSP에 request,session값을 전송
			RequestDispatcher rd=
					request.getRequestDispatcher(jsp);
			rd.forward(request, response);
			// jsp의 _jspService()를 호출한다
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

















