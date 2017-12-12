package com.sist.manager;

import java.util.*;
import java.text.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//  http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=20171205
/*
  	<td class="title">
		<div class="tit5">
			<a href="/movie/bi/mi/basic.nhn?code=17421" title="쇼생크 탈출">쇼생크 탈출</a>
		</div>
	</td>

		
		<div class="info_spec2">
					<dl class="step1">
						<dt>감독</dt>
						<dd>
							
				
								<a href="/movie/bi/pi/basic.nhn?code=4188">장항준</a>
								
			</dd>
			</dl>
			
			<dl class="step2">
				<dt>출연</dt>
				<dd>
					
			
								
								<a href="/movie/bi/pi/basic.nhn?code=140581">강하늘<span>(진석)</span></a>,
 */

import com.sist.dao.MovieDAO;
import com.sist.dao.MovieVO;
public class MovieManager {
	
	public static void main(String[] args) {
		
		MovieManager m=new MovieManager();
		//m.movieLinkData();
		m.movieDetailData();
		System.out.println("저장완료!!");
	}
	public List<String> movieLinkData()
	{
		List<String> list=new ArrayList<String>();
		try
		{	
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			int k=1;
			for(int i=1; i<=40; i++)
			{
				Document doc=Jsoup.connect("http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date="+sdf.format(date)+"&page="+i).get(); //소스보기에서 데이터 가져오기
				Elements elem=doc.select("td.title div.tit5 a");//여러태그를 가져올때
				for(int j=0; j<elem.size(); j++)
				{
					Element code=elem.get(j); //하나태그를 가져올때
					
					//System.out.println(k+":"+code.attr("href"));
					list.add("http://movie.naver.com"+code.attr("href"));
					k++;
				}
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return list;
	}
	public List<MovieVO> movieDetailData()
	{
		List<MovieVO> list=new ArrayList<MovieVO>();
		MovieDAO dao=new MovieDAO();
		
		try
		{
			List<String> linkList=movieLinkData();
			for(int i=0; i<linkList.size(); i++)
			{
				try 
				{
					String link=linkList.get(i);
					Document doc=Jsoup.connect(link).get();
					Element title=doc.select("div.mv_info h3.h_movie a").first();
					Element director=doc.select("div.info_spec2 dl.step1 dd a").first();
					Element actor=doc.select("div.info_spec2 dl.step2 dd a").first();
					Elements temp=doc.select("p.info_spec span");//태그가 여러개있다 Elements<<
					Element genre=temp.get(0);
					Element time=temp.get(2);
					Element regdate=temp.get(3);
					Element grade=temp.get(4);
					Element poster=doc.select("div.poster a img").first();
					Element story=doc.selectFirst("div.story_area p.con_tx"); //Element story=doc.select("").first();동일코드
					System.out.println((i+1)+":"+title.text());
					MovieVO vo=new MovieVO();
					vo.setMno(i+1);
					vo.setTitle(title.text());
					vo.setDirector(director.text());
					vo.setActor(actor.text());
					vo.setPoster(poster.attr("src"));
					vo.setGenre(genre.text());
					vo.setGrade(grade.text());
					vo.setTime(time.text());
					vo.setRegdate(regdate.text());
					String s=story.text();
					s=s.replace("'", "");
					s=s.replace("\"", "");
					vo.setStory(s);
					dao.movieInsert(vo);
					list.add(vo);
				
				}catch(Exception ex) {}
				
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return list;
	}
}


















