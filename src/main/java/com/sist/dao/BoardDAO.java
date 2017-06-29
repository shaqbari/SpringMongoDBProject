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
	
	public List<BoardVO> boardAllData(int page){
		List<BoardVO> list=new ArrayList<BoardVO>();
		
		try {
			int rowSize=10;
			int skip=(page-1)*rowSize;
			/*
			 * 0~9
			 * 10~19 //2page의 경우 앞의 0~9번 10개는 skip하고 10번부터 가져온다.
			 * 20~29
			 * ...
			 * */
			BasicDBObject order=new BasicDBObject();
			order.put("no", -1); //order by (-1:DESC, 1:ASC)
			DBCursor cursor=dbc.find().sort(order).skip(skip).limit(rowSize);
			while(cursor.hasNext()){
				BasicDBObject obj=(BasicDBObject)cursor.next();
				BoardVO vo=new BoardVO();
				vo.setNo(obj.getInt("no"));
				vo.setName(obj.getString("name"));
				vo.setSubject(obj.getString("subject"));
				vo.setRegdate(obj.getString("regdate"));
				vo.setHit(obj.getInt("hit"));
				
				list.add(vo);				
			}
			cursor.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}
	
	public BoardVO boardContentData(int no) {
		BoardVO vo=new BoardVO();
		
		try {
			BasicDBObject where=new BasicDBObject();
			where.put("no", no);	//{no:1}
			//{no:{"$lt":1}} no<1
			/*
			 * 	put("no", 1) => where no=1
			 *  put("$lt", 1) <1
			 *  put("$gt", 1) >1
			 *  put("$le", 1) <=l
			 *  put("$ge", 1) >=1
			 *  put("$ne", 1) !=1
			 * */
			//{} : BasicDBObject : 블록하나
			//BasicDBObject obj=(BasicDBObject)dbc.findOne();//맨처음거만 가져온다
			BasicDBObject obj=(BasicDBObject)dbc.findOne(where);//no=1인거만 가져온다.
			
			//조회수 증가
			BasicDBObject up=new BasicDBObject();			
			up.put("hit", obj.getInt("hit")+1);
			dbc.update(where, new BasicDBObject("$set", up));//$set을 안쓰면 where가 아닌 나머지는 null이된다.
			
			obj=(BasicDBObject)dbc.findOne(where);//조회수 증가한걸 다시 가져온다.
			vo.setNo(obj.getInt("no"));
			vo.setName(obj.getString("name"));
			vo.setSubject(obj.getString("subject"));
			vo.setRegdate(obj.getString("regdate"));
			vo.setHit(obj.getInt("hit"));
			vo.setContent(obj.getString("content"));
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return vo;
	}
	
}



