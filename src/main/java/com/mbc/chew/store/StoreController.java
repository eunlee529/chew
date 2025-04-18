package com.mbc.chew.store;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class StoreController {
	@Autowired
	SqlSession sqls;
	String path="C:\\git\\chew\\src\\main\\webapp\\image";
@RequestMapping(value="/storein")
	public String storein()
	{
		return"storeinput";
	}
@RequestMapping(value="/storesave")
public String storesave(MultipartHttpServletRequest mul) throws IllegalStateException, IOException
{
	
	int storecode        = Integer.parseInt(mul.getParameter("storecode"));
	String storename     = mul.getParameter("storename");
	String storeaddress  = mul.getParameter("storeaddress");
	String storecategory = mul.getParameter("storecategory");
	int storelikes		 = Integer.parseInt(mul.getParameter("storelikes"));
	String storearea	 = mul.getParameter("storearea");
	MultipartFile mf 	 = mul.getFile("storeimage");
	String fname		 = mf.getOriginalFilename();
	StoreService ss 	 = sqls.getMapper(StoreService.class);
	UUID uu = UUID.randomUUID();
	fname= uu.toString()+"_"+fname;
	mf.transferTo(new File(path+"\\"+fname));
	ss.insertstore(storecode,storename,storeaddress,storecategory,storelikes,storearea,fname);
	return"redirect:/";
}
@RequestMapping(value ="/sout")
public String storeout(HttpServletRequest request,Model m)
{
	StoreService ss = sqls.getMapper(StoreService.class);
	ArrayList<StoreDTO>list = ss.outstore();
	m.addAttribute("list",list);
	return "storeout";
}
@RequestMapping(value ="/sdelete")
public String storedelete(HttpServletRequest request,Model m)
{
	int storecode        = Integer.parseInt(request.getParameter("storecode"));
	StoreService ss  	 = sqls.getMapper(StoreService.class);
	StoreDTO dto 	  	 = ss.storedelete(storecode);
	m.addAttribute("dto",dto);
	return "storedeleteview";

}
@RequestMapping(value ="/delete",method = RequestMethod.POST)
public String delete(HttpServletRequest request)
{
	int storecode   = Integer.parseInt(request.getParameter("storecode"));
	String img 		= request.getParameter("storeimage");
	StoreService ss = sqls.getMapper(StoreService.class);
	ss.delete(storecode);
	File ff 	 	= new File(path+"\\"+img);	
	ff.delete();
	return "storeout";
}
@RequestMapping(value ="/smodify")
public String ff(HttpServletRequest request,Model m)
{
	int storecode    = Integer.parseInt(request.getParameter("storecode"));
	StoreService ss  = sqls.getMapper(StoreService.class);
	StoreDTO dto 	 = ss.modify(storecode);
	m.addAttribute("dto",dto);
	return "storemodifyview";
}


@RequestMapping(value="modify")
public String gg(MultipartHttpServletRequest mul) throws IllegalStateException, IOException
{
	int storecode   	 = Integer.parseInt(mul.getParameter("storecode"));
	String storename     = mul.getParameter("storename");
	String storeaddress  = mul.getParameter("storeaddress");
	String storecategory = mul.getParameter("storecategory");
	int storelikes		 = Integer.parseInt(mul.getParameter("storelikes"));
	String storearea	 = mul.getParameter("storearea");
	MultipartFile mf 	 = mul.getFile("storeimage");
	StoreService ss 	 = sqls.getMapper(StoreService.class);
	if(mf.isEmpty()) { 
		ss.updatemodi1(storecode,storename,storeaddress,storecategory,storelikes,storearea);
	}
	else {
		String fname = mf.getOriginalFilename();
		UUID uu 	 = UUID.randomUUID();
		fname		 = uu.toString()+"_"+fname;
		mf.transferTo(new File(path+"\\"+fname));
		String img   = mul.getParameter("storeimage");
		File ff		 = new File(path+"\\"+img);	
		ff.delete();
		ss.updatemodi2(storecode,storename,storeaddress,storecategory,storelikes,storearea,fname);
	}
	
	return "storeout";
}

}