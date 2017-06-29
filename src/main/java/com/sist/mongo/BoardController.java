package com.sist.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;

//수정삭제같이 비밀번호 일치여부에 따라 다르게 반응해야 할경우 RestController를 쓴다. ajax로 보낼때 많이 쓴다.
@RestController
public class BoardController {
	/* 메모리할당
	 * @Comonenet
	 * @Repository
	 * @Service
	 * @Controller
	 * @RestController
	 * */
	
	@Autowired
	private BoardDAO dao;
	
	@RequestMapping("board/update_ok.do")
	public String board_update_ok(BoardVO vo){
		System.out.println("pwd는"+vo.getPwd());
		boolean bCheck=dao.boardUpdate(vo);
		
		String sendData="";
		if (bCheck==true) {
			sendData="<script>"
					+ "location.href=\"content.do?no="+vo.getNo()+"\";"
					+ "</script>";			
			
		}else{
			sendData="<script>"
					+ "alert(\"비밀번호가 틀립니다.\");"
					+ "history.back();"
					+ "</script>";			
			
		}
		
		return sendData;		
	} 
	
	@RequestMapping("board/delete_ok.do")
	public String board_update_ok(int no, String pwd){
		System.out.println("pwd는"+pwd);
		boolean bCheck=dao.boardDelete(no, pwd);
		
		String sendData="";
		if (bCheck==true) {
			sendData="<script>"
					+ "location.href=\"list.do\";"
					+ "</script>";			
			
		}else{
			sendData="<script>"
					+ "alert(\"비밀번호가 틀립니다.\");"
					+ "history.back();"
					+ "</script>";			
			
		}
		
		return sendData;		
	} 
	
}




