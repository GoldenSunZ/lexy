package cn.lexy.auth.filter;


import cn.lexy.auth.page.UserPage;
import cn.lexy.auth.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * session 拦截器
 * @author Jason.Fang
 *
 */
public class SessionInterceptor implements HandlerInterceptor {
	
	private static Logger log = LoggerFactory.getLogger(SessionInterceptor.class);

	/**
	 * 完成页面的render后调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {

	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String contextPath = request.getContextPath();
		UserPage user = (UserPage) request.getSession().getAttribute(Constants.SESSION_USER);
		if (user != null) {
			String requestUri = request.getRequestURI();
			String accessUrl = requestUri.substring(contextPath.length());
			request.getSession().setAttribute(Constants.CURRENT_MENU, accessUrl);
			
			if (request.getSession().getAttribute(Constants.SESSION_MENUS)!=null) {
				Map<String, String> menus = (Map<String, String>)request.getSession().getAttribute(Constants.SESSION_MENUS);
				request.getSession().setAttribute(Constants.CURRENT_PARENT_MENU, menus.get(accessUrl));
			}
			return true;
		} else {
			response.sendRedirect(contextPath+"/login");
			return false;
		}
//		return true;
	}

}
