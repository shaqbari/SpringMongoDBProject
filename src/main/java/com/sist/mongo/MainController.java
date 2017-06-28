package com.sist.mongo;

import java.util.List;

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
		if (page==null) {
			page="1";
		}
		int curpage=Integer.parseInt(page);
		List<BoardVO> list= dao.boardAllData(curpage);
		
		model.addAttribute("list", list);		
		return "board/list";
	}
	
	
	
}
