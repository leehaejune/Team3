package kr.gudi.payment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import kr.gudi.login.UserBean;

@Controller
@RequestMapping("/main")
public class PaymentController {

	@Autowired PaymentService paymentService;
	
	List<Map<String, Object>> itemlist = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> itemlist2 = new ArrayList<Map<String, Object>>();
	
	@RequestMapping(value="/bankView", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getbank(HttpSession session){
		Map<String, Object> paymentMap = new HashMap<String, Object>();
//		paymentMap = (Map<String, Object>) 
		Object obj = session.getAttribute("User");
		if(obj != null) {
			UserBean ub = (UserBean) obj;
			Map<String, Object> userInfo = new HashMap<String, Object>();
//			userInfo.remove(paymentMap);
//			userInfo.remove(paymentService.getbank());
			userInfo.put("USER", ub);
			userInfo.put("ADMIN", paymentService.getbank());
			return userInfo;
		}
		
		return null;
	}
	
	@RequestMapping(value="/itemPay", method = RequestMethod.POST)
	public @ResponseBody int itemPay(@RequestBody Map<String, Object> paramMap){
	System.out.println("paramMap : " + paramMap);
	itemlist.add(paramMap);
		return 1;
	}
	
	
	@RequestMapping(value="/itemListView", method = RequestMethod.POST)
	public String itemListView(Model model){
		List<Map<String, Object>> listItem = itemlist;
		System.out.println("아이템리스트 사이즈 :"+itemlist.size()); 
		if(listItem.size() == 1) {
			for(int i = 0; i < itemlist.size(); i++) {
				model.addAttribute("result", listItem.get(i));
				System.out.println("asdasdasdsad"+model);
			}
			itemlist.clear();
			return "main/page/payment/paymentItem";
		} 
			return "main/page/payment/error";
	}
	
	
	@RequestMapping(value="/cartListView", method = RequestMethod.POST)
	public String cartListView(HttpSession session, Model model) {
		System.out.println("전체상품결제");
		if(itemlist.size() == 0 && itemlist2.size() == 0) {
			Map<String, Object> paymentMap = new HashMap<String, Object>();
//			paymentMap = (Map<String, Object>) session.getAttribute("User");
			Object obj = session.getAttribute("User");
			if(paymentMap != null) {
				UserBean ub = (UserBean) obj;
				model.addAttribute("cartResult",paymentService.pickCart(ub));
				System.out.println("model : "+ model);
			}
//			return itemlist.size();
			return "main/page/payment/paymentCart";
		} 
//			return -1;
			return "main/page/payment/error";
	}
	
	@RequestMapping(value="/setPay", method=RequestMethod.PUT)
	public @ResponseBody int setPay(@RequestBody Map<String, Object> ParamMap) {
		return paymentService.setPay(ParamMap);
	}
	
	@RequestMapping(value="/selectCartView", method = RequestMethod.POST)
	public @ResponseBody int selectCartView(@RequestBody List<Map<String, Object>> paramMap){
		System.out.println(paramMap);
		itemlist2 = new ArrayList<Map<String,Object>>();
		for(int i = 0; i < paramMap.size(); i++) {
			itemlist2.add(paramMap.get(i));	
		}
		System.out.println("item 리스트 사이즈:"+itemlist2.size());
		return itemlist2.size();
	}
	
	@RequestMapping(value="/pickCartView", method = RequestMethod.POST)
	public String pickCartView(HttpSession session, Model model) {
		if(itemlist2.size() > 1) {
			model.addAttribute("itemList", itemlist2);
			System.out.println("model : "+ model);
			return "main/page/payment/paymentItem2";
		} else if (itemlist2.size() == 1) {
			for(int i = 0; i < itemlist2.size(); i++) {
				model.addAttribute("result", itemlist2.get(i));
			}
			return "main/page/payment/paymentItem";
		} 
			return "main/page/payment/error";
	}
	
	@RequestMapping(value="/allselectCart", method=RequestMethod.POST)
	public @ResponseBody int allselectCart() {
		itemlist.removeAll(itemlist);
		itemlist2.removeAll(itemlist2);
		return 1;
	}
	
}
