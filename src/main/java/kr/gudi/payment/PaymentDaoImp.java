package kr.gudi.payment;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.gudi.login.UserBean;

@Repository
public class PaymentDaoImp implements PaymentDao {

	@Autowired SqlSession session;

	@Override
	public Map<String, Object> getbank() {
		return session.selectOne("payment.getbank");
	}

	@Override
	public List<Map<String, Object>> pickCart(UserBean ub) {
		return session.selectList("payment.pickCart", ub);
	}

	@Override
	public int setPay(Map<String, Object> ParamMap) {
		System.out.println("결제등록 : " + ParamMap);
		return session.insert("payment.setPay",ParamMap);
	}

	@Override
	public int updateCart(Map<String, Object> ParamMap) {
		return session.update("payment.updateCart", ParamMap);
	}
}
