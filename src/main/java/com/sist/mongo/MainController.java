package com.sist.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;

@Controller
public class MainController {
	@Autowired
	private BoardDAO dao;
	
	@RequestMapping("board/insert.do")
	public String board_insert(){
		
		return "board/insert";
	}
	
	@RequestMapping("board/insert_ok.do")
	public String board_insert_ok(BoardVO vo){
		
		dao.boardInsert(vo);
		return "redirect:/board/list.do";
	}
	@RequestMapping("board/list.do")
	public String board_list(String page, Model model){
		
		
		return "board/list";
	}
	
}
