package com.sist.dao;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.stereotype.Repository;

import com.mongodb.*;

@Repository
public class BoardDAO {
	private MongoClient mc; //jdbc에서 Connection에 해당 db연결
	private DB db; //ORCL(database)에해당
	private DBCollection dbc, dbc1; //table에 해당 
	
	public BoardDAO(){
		try {
			//mc=new MongoClient("localhost")
			mc=new MongoClient(new ServerAddress(new InetSocketAddress("211.238.142.104", 27017)));//원격서버연결
			db=mc.getDB("mydb");
			dbc=db.getCollection("board");//임의로 지정 가능 없으면 만들어진다.
			dbc1=db.getCollection("member");//임의로 지정 가능 없으면 만들어진다.
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void boardInsert(BoardVO vo) {
		
		try {
			DBCursor cursor=dbc.find();
			/*
			 * DBCursor ==> ResultSet에 해당
			 * dbc.find() => Select * from table명 에 해당
			 * ==>SQL /NoSQL(SQL대신 함수를 이용)
			 * */
			
			int no=0;
			//cursor.count(); select count(*) from table에 해당
			while(cursor.hasNext()){//rs.next()에 해당
				// {no:1, name:"", subject:""...} => BasicDBOjbect
				BasicDBObject obj=(BasicDBObject) cursor.next();
				int max=obj.getInt("no");
				if (no<max) {
					no=max; //Select MAX(no) From table 최대값 가지고 오기 우리가 다 만들어야 한다.
				}
				
			}
			cursor.close();
			
			//mybatis에서 간단하게 할 수 있다.
			BasicDBObject insertObj=new BasicDBObject();
			insertObj.put("no", no+1);//max값 가져와서 no값증가하는 자동sequence만들기 
			insertObj.put("name", vo.getName());//max값 가져와서 sequence만들기 
			insertObj.put("subject", vo.getSubject());//max값 가져와서 sequence만들기 
			insertObj.put("content", vo.getContent());//max값 가져와서 sequence만들기 
			insertObj.put("pwd", vo.getPwd());//max값 가져와서 sequence만들기 
			insertObj.put("regdate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));//max값 가져와서 sequence만들기 
			insertObj.put("hit", 0);//max값 가져와서 sequence만들기 
			dbc.insert(insertObj);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
																					
	}
	
	
	
}



