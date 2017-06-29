package com.sist.mongo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sist.dao.BoardDAO;
import com.sist.dao.BoardVO;
import com.sist.r.RManager;

@Controller
public class MainController {
	@Autowired
	private BoardDAO dao;
	
	@Autowired
	private RManager rm;
	
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
	
	@RequestMapping("board/content.do")
	public String board_content(int no, Model model){
		BoardVO vo=dao.boardContentData(no);
		model.addAttribute("vo", vo);
		rm.boardGraph(no);
		
		return "board/content";
	}
	
	@RequestMapping("board/update.do")
	public String board_update(int no, Model model){
		BoardVO vo=dao.boardUpdateData(no);
		model.addAttribute("vo", vo);
		
		return "board/update";
		
	}
	
	@RequestMapping("board/delete.do")
	public String board_delete(int no, Model model){
		model.addAttribute("no", no);
		
		return "board/delete";
		
	}
	
}
