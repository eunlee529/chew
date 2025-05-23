package com.mbc.chew.review;



import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ReviewController {
	@Autowired
	SqlSession sqlSession;
	
	@RequestMapping(value="/submitReview", method = RequestMethod.POST)
	public String submitReview(HttpServletRequest request) {
	    ReviewDTO dto = new ReviewDTO();
	    dto.setId(request.getParameter("id"));
	    dto.setStorecode(Integer.parseInt(request.getParameter("storecode")));
	    dto.setContent(request.getParameter("content"));
	    dto.setStars(Integer.parseInt(request.getParameter("stars")));
	    dto.setTitle(request.getParameter("title"));
    
	    ReviewService rs = sqlSession.getMapper(ReviewService.class);
	    rs.insertReview(dto);     
	   

	    return "redirect:/detailview?storecode=" + dto.getStorecode();
	}
	
	@RequestMapping(value="/reviews")
	public String Reviewlist(Model model, HttpServletRequest request) {
	    int storecode = Integer.parseInt(request.getParameter("storecode"));
	    ReviewService rs = sqlSession.getMapper(ReviewService.class);
	    ArrayList<ReviewDTO> list = rs.reviewout(storecode);
	    
	    model.addAttribute("list", list);
	    model.addAttribute("storecode", storecode);

	    
	    return "detailview"; 
	}

	@RequestMapping(value="/deleteReview", method = RequestMethod.POST)
	public String deleteReview(HttpServletRequest request) {
	    String sessionId = (String) request.getSession().getAttribute("id");
	    int storecode = Integer.parseInt(request.getParameter("storecode"));

	    ReviewService rs = sqlSession.getMapper(ReviewService.class);
	    rs.deleteReview(sessionId, storecode); // 세션 ID 사용

	    return "redirect:/detailview?storecode=" + storecode;
	}


	@RequestMapping(value="/editReview", method = RequestMethod.POST)
	public String editReview(HttpServletRequest request) {
	    String sessionId = (String) request.getSession().getAttribute("id"); // 세션에서 가져온 로그인 사용자 ID
	    int storecode = Integer.parseInt(request.getParameter("storecode"));

	    ReviewDTO dto = new ReviewDTO();
	    dto.setId(sessionId); // 세션 ID 사용
	    dto.setStorecode(storecode);
	    dto.setTitle(request.getParameter("title"));
	    dto.setContent(request.getParameter("content"));

	    ReviewService rs = sqlSession.getMapper(ReviewService.class);
	    rs.updateReview(dto);

	    return "redirect:/detailview?storecode=" + storecode;
	}

	
}
