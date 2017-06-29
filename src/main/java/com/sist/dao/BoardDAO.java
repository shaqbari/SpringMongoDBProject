package com.sist.dao;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.derby.impl.services.bytecode.BCJava;
import org.springframework.stereotype.Repository;

import com.mongodb.*;

@Repository
public class BoardDAO {
	/*
	 * application-context.xml에서 아래와 같이 설정하면 아래 생성자까지 안써도 된다.
	 * 	<mongo:mongo-client
		host="211.238.142.104"
		id="mc"
		port="27017"
		/>
	 * 	<bean id="mongoTemplate"
		class="org.springframework.data.mongodb.core.MongoTemplate">
		<constructor-arg ref="mc"/>
		</bean>
	 * */
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
	
	public BoardVO boardUpdateData(int no) {
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
	
	public boolean boardUpdate(BoardVO vo){
		boolean bCheck=false;
		System.out.println("no는 "+vo.getNo());
		try {
			BasicDBObject where=new BasicDBObject();
			where.put("no", vo.getNo());
			
			BasicDBObject obj=(BasicDBObject) dbc.findOne(where);//BasicDBObject는 where에 해당하는 {}(블록하나)에 해당
			/*	no가 1이라면 다음을 가져온다.
			 * {
				    "_id" : ObjectId("59532821e4b0b1fc28793d6a"),
				    "no" : 1,
				    "name" : "심청",
				    "subject" : "아이들의 통학로 및 건강안전, 학습권 보장",
				    "content" : "안녕하세요 서울대방초등학교 학부형입니다\r\n공사장에 둘러싸인 아이들을 지켜주세요\r\n6/8일SK사업 설명회가 대방초에서 있었습니다\r\n\r\n1. 서울 대방초등학교 정문/후문 통학로에 대한 시스템적인 안전성 보장요청했습니다\r\nsk모델하우스 앞 출입구와 아이들의 통학로가 맞닿아 있습니다. 그럼에도 신호수 1인이면 만사 OK라며 뭐가 문제냐고 생색내며 얘기합니다.\r\n공사차량 출입구 신호수 배치는 당연한거 아닙니까? 일단 신호수 1인으로 진행해보다가 나중에 문제가 생기면 그때가서 추가 보완을 생각해보던가...라고 시공사에서 답변합니다. 내 자식 아니니깐 아이들의 생명을 담보로 통학을 하든 말든 상관없다는 겁니까? 시공사의 이러한 대응에 함께 참석중인 학교측, 조합측, 심지어 지역구의원으로 참석한 신경민 의원조차 아무런 대응도 하지 않습니다.\r\n\r\n2. 책임있는 답변과 학부모들의 요청사항을 구체적으로 수용하고 대응할 수 있는 대표단과 커뮤니케이션 할수 있는 방안제시\r\n학부모들의 항의가 이어지자 시공사 대표로 오셨다는 분들이 우리도 일게 월급쟁이일뿐이다 오늘 이런 얘기 처음듣는다 라고 얘기하며 정작 본인들의 책임은 회피하면서 100여명의 학부모전체를 상대하긴 어려움이 있으니 학부모 대표단을 구성하라고 말합니다.\r\n저는 제 가족의 대표로 참석하였고, 어제 모인 100여명의 학부모 또한 하나하나 각 가정의 대표로 참석한 사람들입니다.\r\n누가 누구의 대표성을 따지는건지...대표단을 요청할거면 설명회자리는 왜 마련한건지 이해를 할수가 없네요.\r\n그리고 신길 5구역 sk뷰 공사로 인한 대방초등학교의 안전문제가 sk시공사 측만의 문제가 아니거늘 시공사, 조합, 학교측, 관할 행정기관 어디도 제대로 된 책임있는 답변을 하는 사람이 아무도 없습니다.\r\n\r\n정당한 세금을 내고 시민의 의무를 다하고 있는 사람입니다. 저는 저의 의무를 다 하며 제 권리를 통해 요청하고 있습니다. 그럼 이러한 사태에 대해 책임지고 대응해야될 사람들은 무엇을 하고 계신건가요?\r\n\r\n3. 스쿨버스 및 스쿨워킹, 통학로 주변 안전교통단속 등 통학로 안전 등하교를 위한 여러가지 대책에 대해 빠른 시일내에 시행촉구.\r\n대방초등학교에서 뒤늦게 교육청에 스쿨버스 지원을 요청했고 늦은 신청이었음에도 학교주변의 위험성이 인정되어 추가 1대 배정이 가능하게 되었다고 들었습니다. 전교생의 수가 440여명이고 지원받은 스쿨버스는 32인승 1대 입니다. 등교시간 차량 1대로 2회 교대 운행하고 있다보니 1코스 타는 아이들은 학교에 8시 25분에 도착하고 2코스 타는 아이들은 학교에 9시 간당간당 도착합니다.\r\n학교측에선 일찍 도착한 아이들은 아침돌봄 또는 도서관에서 관리한다고 하지만 아침돌봄 교사1명과 도서관 관리 1명이 과연다 관리가될지..\r\n스쿨버스가 추가 증원이 되면 학교 등교시간을 너무 빠르게 또는 너무 늦게 맞추지 않아도 되고, 2코스 운행으로 64명이 아니라 더 많은 아이들을 수용할 수 있을터인데 예산문제를 들먹이며 서로 발빼기 바쁜 상황입니다.\r\n스쿨버스를 이용하지 않는 아이들은 더 많은 위험에 노출되므로 스쿨워킹과 같은 제도를 빠르게 시행해 주었으면 좋겠습니다. 이미 한학기가 지났습니다.\r\n4. 학습권 보장\r\n삼면이 공사장으로 둘러쌓여 있어서 공사장의 비산먼지 등으로 인하여 학교 운동장 및 외부 활동에 심각한 문제가 있다고 시공사측에 얘기했습니다.\r\n그랬더니 왜 우리가 공사하는데 학생들이 운동장을 사용하지 못하고 창문을 못여는거냐, 왜 학습권이 침해되는거냐 라고 말합니다.\r\n\r\n5. 아이들의 건강보호 및 공사장 관리감독 철저와 위반시 제재 필요.\r\n공사구역 철거당시 철거반은 온갖 보호장비를 두루고 작업할때 바로 옆으로 우리 아이들은 아무런 보호조치 하나 없이 그대로 통학하며 비산먼지와 석면가루들을 그냥 흡입하며 다녔습니다.\r\n아이들이 공사장 먼지를 마시지 않도록 실내 모든 공간에 공기청정기 및 필터방충망 요청했습니다. 해당 부분에 대해 학교측과 협의가 필요하다고 합니다 학교장과 학교감이 그 자리에 있었는데, 왜 따로 협의를 해야 되나요?\r\n\r\n게다가 공사장 임시통학로라고 만든 펜스 아래로 소주병이 굴러다닙니다. 학교 주변 유해업소까지 제약하고 있는 판국에 아이들이 걸어다니는 통학로 지척에 소주병이 굴러다닌다는게 말이 되나요? 1200 세대가 들어서는 대단위 공사현장입니다. 아이들이 다니는 학교가 공사장 한복판이란 말입니다\r\n\r\n학교 등하교 시간에 공사를 안하기로 했음에도 살수차없이 공사를 지속하는 행위 단속 및 위반시 제재.\r\n공사분진으로 인해 공기오염수치가 기준치 초과시 공기오염도가 안정화될때까지 공사중단 할것에 대한 내용.\r\n공사소음이 기준 데시벨을 초과할 경우에도 공사중단할 것에 대한 내용.\r\n아이들의 건강상 안전을 위해 모든 실내에 규격에 맞는 공기청정기 배치 및 필터방충망 요청\r\n그외 시공사가 아이들의 안전을 위해 하기로 했던 부분에 대해 지켜지지 않았을 경우 어떻게 위반사항을 관리하고 대책을 논할 것인지에 대해 답변해주셨으면 좋겠습니다.\r\n\r\n*학보모들이 개인의 이익을 위해 떠드는 것이 아닙니다.\r\n당장 내 아이가 다니고 있고, 내 아이의 안전이 위협받고 있기 때문에 요구하는 것입니다.\r\n\r\n신길 5구역 시공사 SK건설, 신길 5구역 조합, 관할 행정기관, 학교, 학부모 모두에게 책임이 있는 문제입니다.\r\n그래서 요청합니다. 저는 아이의 학부모로서 우리 아이가 최소한의 안전과 학습권이 보장받기를 원합니다. 이문제에 책임이 있는 모든 기관은 본 사항에 대한 해결책을 빠른 시일안에 제공해주시기 바랍니다. 하루, 이틀에 끝나는 공사가 아닙니다. 무려 3년이나 되는 공사입니다. 순차적으로 즉시 진행되어야 되는 내용이 있고 당장 진행할 수 없는 어려움이 있다면 우선 적용할 부분들과 선진행할 수 있는 부분들을 추려 진행하고 나머지 부분들은 단계적으로 시행해도 되는 부분입니다.\r\n이러는 사이에도 우리 아이들은 오늘도 아침에 등교를 하고 오후면 하교를 합니다.\r\n매일매일이 안전과 생명이 담보로 잡혀 있단 말입니다.\r\n제발 내 아이의 안전과 생명을 지켜달란 말입니다.",
				    "pwd" : "1234",
				    "regdate" : "2017-06-28",
				    "hit" : 0
				}
			 * 
			 * subject:{a:{b:[{}, {}, {}]}}
			 * 
			 * */
			String db_pwd=obj.getString("pwd");
			if (db_pwd.equals(vo.getPwd())) {
				bCheck=true;
				BasicDBObject up=new BasicDBObject();
				up.put("name", vo.getName());
				up.put("subject", vo.getSubject());
				up.put("content", vo.getContent());
				dbc.update(where, new BasicDBObject("$set", up));
				//dbc.remove(where);
				
			}else{
				bCheck=false;
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return bCheck;
	}
	
	public boolean boardDelete(int no, String pwd){
		boolean bCheck=false;
		System.out.println("no는 "+no);
		try {
			BasicDBObject where=new BasicDBObject();
			where.put("no", no);
			
			BasicDBObject obj=(BasicDBObject) dbc.findOne(where);//BasicDBObject는 where에 해당하는 {}(블록하나)에 해당
			/*	no가 1이라면 다음을 가져온다.
			 * {
				    "_id" : ObjectId("59532821e4b0b1fc28793d6a"),
				    "no" : 1,
				    "name" : "심청",
				    "subject" : "아이들의 통학로 및 건강안전, 학습권 보장",
				    "content" : "안녕하세요 서울대방초등학교 학부형입니다\r\n공사장에 둘러싸인 아이들을 지켜주세요\r\n6/8일SK사업 설명회가 대방초에서 있었습니다\r\n\r\n1. 서울 대방초등학교 정문/후문 통학로에 대한 시스템적인 안전성 보장요청했습니다\r\nsk모델하우스 앞 출입구와 아이들의 통학로가 맞닿아 있습니다. 그럼에도 신호수 1인이면 만사 OK라며 뭐가 문제냐고 생색내며 얘기합니다.\r\n공사차량 출입구 신호수 배치는 당연한거 아닙니까? 일단 신호수 1인으로 진행해보다가 나중에 문제가 생기면 그때가서 추가 보완을 생각해보던가...라고 시공사에서 답변합니다. 내 자식 아니니깐 아이들의 생명을 담보로 통학을 하든 말든 상관없다는 겁니까? 시공사의 이러한 대응에 함께 참석중인 학교측, 조합측, 심지어 지역구의원으로 참석한 신경민 의원조차 아무런 대응도 하지 않습니다.\r\n\r\n2. 책임있는 답변과 학부모들의 요청사항을 구체적으로 수용하고 대응할 수 있는 대표단과 커뮤니케이션 할수 있는 방안제시\r\n학부모들의 항의가 이어지자 시공사 대표로 오셨다는 분들이 우리도 일게 월급쟁이일뿐이다 오늘 이런 얘기 처음듣는다 라고 얘기하며 정작 본인들의 책임은 회피하면서 100여명의 학부모전체를 상대하긴 어려움이 있으니 학부모 대표단을 구성하라고 말합니다.\r\n저는 제 가족의 대표로 참석하였고, 어제 모인 100여명의 학부모 또한 하나하나 각 가정의 대표로 참석한 사람들입니다.\r\n누가 누구의 대표성을 따지는건지...대표단을 요청할거면 설명회자리는 왜 마련한건지 이해를 할수가 없네요.\r\n그리고 신길 5구역 sk뷰 공사로 인한 대방초등학교의 안전문제가 sk시공사 측만의 문제가 아니거늘 시공사, 조합, 학교측, 관할 행정기관 어디도 제대로 된 책임있는 답변을 하는 사람이 아무도 없습니다.\r\n\r\n정당한 세금을 내고 시민의 의무를 다하고 있는 사람입니다. 저는 저의 의무를 다 하며 제 권리를 통해 요청하고 있습니다. 그럼 이러한 사태에 대해 책임지고 대응해야될 사람들은 무엇을 하고 계신건가요?\r\n\r\n3. 스쿨버스 및 스쿨워킹, 통학로 주변 안전교통단속 등 통학로 안전 등하교를 위한 여러가지 대책에 대해 빠른 시일내에 시행촉구.\r\n대방초등학교에서 뒤늦게 교육청에 스쿨버스 지원을 요청했고 늦은 신청이었음에도 학교주변의 위험성이 인정되어 추가 1대 배정이 가능하게 되었다고 들었습니다. 전교생의 수가 440여명이고 지원받은 스쿨버스는 32인승 1대 입니다. 등교시간 차량 1대로 2회 교대 운행하고 있다보니 1코스 타는 아이들은 학교에 8시 25분에 도착하고 2코스 타는 아이들은 학교에 9시 간당간당 도착합니다.\r\n학교측에선 일찍 도착한 아이들은 아침돌봄 또는 도서관에서 관리한다고 하지만 아침돌봄 교사1명과 도서관 관리 1명이 과연다 관리가될지..\r\n스쿨버스가 추가 증원이 되면 학교 등교시간을 너무 빠르게 또는 너무 늦게 맞추지 않아도 되고, 2코스 운행으로 64명이 아니라 더 많은 아이들을 수용할 수 있을터인데 예산문제를 들먹이며 서로 발빼기 바쁜 상황입니다.\r\n스쿨버스를 이용하지 않는 아이들은 더 많은 위험에 노출되므로 스쿨워킹과 같은 제도를 빠르게 시행해 주었으면 좋겠습니다. 이미 한학기가 지났습니다.\r\n4. 학습권 보장\r\n삼면이 공사장으로 둘러쌓여 있어서 공사장의 비산먼지 등으로 인하여 학교 운동장 및 외부 활동에 심각한 문제가 있다고 시공사측에 얘기했습니다.\r\n그랬더니 왜 우리가 공사하는데 학생들이 운동장을 사용하지 못하고 창문을 못여는거냐, 왜 학습권이 침해되는거냐 라고 말합니다.\r\n\r\n5. 아이들의 건강보호 및 공사장 관리감독 철저와 위반시 제재 필요.\r\n공사구역 철거당시 철거반은 온갖 보호장비를 두루고 작업할때 바로 옆으로 우리 아이들은 아무런 보호조치 하나 없이 그대로 통학하며 비산먼지와 석면가루들을 그냥 흡입하며 다녔습니다.\r\n아이들이 공사장 먼지를 마시지 않도록 실내 모든 공간에 공기청정기 및 필터방충망 요청했습니다. 해당 부분에 대해 학교측과 협의가 필요하다고 합니다 학교장과 학교감이 그 자리에 있었는데, 왜 따로 협의를 해야 되나요?\r\n\r\n게다가 공사장 임시통학로라고 만든 펜스 아래로 소주병이 굴러다닙니다. 학교 주변 유해업소까지 제약하고 있는 판국에 아이들이 걸어다니는 통학로 지척에 소주병이 굴러다닌다는게 말이 되나요? 1200 세대가 들어서는 대단위 공사현장입니다. 아이들이 다니는 학교가 공사장 한복판이란 말입니다\r\n\r\n학교 등하교 시간에 공사를 안하기로 했음에도 살수차없이 공사를 지속하는 행위 단속 및 위반시 제재.\r\n공사분진으로 인해 공기오염수치가 기준치 초과시 공기오염도가 안정화될때까지 공사중단 할것에 대한 내용.\r\n공사소음이 기준 데시벨을 초과할 경우에도 공사중단할 것에 대한 내용.\r\n아이들의 건강상 안전을 위해 모든 실내에 규격에 맞는 공기청정기 배치 및 필터방충망 요청\r\n그외 시공사가 아이들의 안전을 위해 하기로 했던 부분에 대해 지켜지지 않았을 경우 어떻게 위반사항을 관리하고 대책을 논할 것인지에 대해 답변해주셨으면 좋겠습니다.\r\n\r\n*학보모들이 개인의 이익을 위해 떠드는 것이 아닙니다.\r\n당장 내 아이가 다니고 있고, 내 아이의 안전이 위협받고 있기 때문에 요구하는 것입니다.\r\n\r\n신길 5구역 시공사 SK건설, 신길 5구역 조합, 관할 행정기관, 학교, 학부모 모두에게 책임이 있는 문제입니다.\r\n그래서 요청합니다. 저는 아이의 학부모로서 우리 아이가 최소한의 안전과 학습권이 보장받기를 원합니다. 이문제에 책임이 있는 모든 기관은 본 사항에 대한 해결책을 빠른 시일안에 제공해주시기 바랍니다. 하루, 이틀에 끝나는 공사가 아닙니다. 무려 3년이나 되는 공사입니다. 순차적으로 즉시 진행되어야 되는 내용이 있고 당장 진행할 수 없는 어려움이 있다면 우선 적용할 부분들과 선진행할 수 있는 부분들을 추려 진행하고 나머지 부분들은 단계적으로 시행해도 되는 부분입니다.\r\n이러는 사이에도 우리 아이들은 오늘도 아침에 등교를 하고 오후면 하교를 합니다.\r\n매일매일이 안전과 생명이 담보로 잡혀 있단 말입니다.\r\n제발 내 아이의 안전과 생명을 지켜달란 말입니다.",
				    "pwd" : "1234",
				    "regdate" : "2017-06-28",
				    "hit" : 0
				}
			 * 
			 * subject:{a:{b:[{}, {}, {}]}}
			 * 
			 * */
			String db_pwd=obj.getString("pwd");
			if (db_pwd.equals(pwd)) {
				bCheck=true;
				dbc.remove(where);
				
			}else{
				bCheck=false;
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return bCheck;
	}
}



