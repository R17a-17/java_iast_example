package cn.org.javaweb.iast.contenxt;

import cn.org.javaweb.iast.http.IASTServletRequest;
import cn.org.javaweb.iast.http.IASTServletResponse;

import java.util.LinkedList;

/**
 * @author iiusky - 03sec.com
 */
public class HttpRequestContext {

	private final IASTServletRequest servletRequest;

	private final IASTServletResponse servletResponse;

	private LinkedList<CallChain> callChain;

	public HttpRequestContext(IASTServletRequest servletRequest, IASTServletResponse servletResponse) {
		this.servletRequest = servletRequest;
		this.servletResponse = servletResponse;
		this.callChain = new LinkedList<>();
	}


	public IASTServletRequest getServletRequest() {
		return servletRequest;
	}

	public LinkedList<CallChain> getCallChain() {
		return callChain;
	}

	public void addCallChain(CallChain callChain) {
		// 遍历之前的元素，如果有元素是enterPropagator或者leavePropagator，并且是append方法，就不添加append元素
		// 这样可以解决遍历时候还添加元素从而导致出错
		for (CallChain item: this.callChain) {
			if (item.getChainType().equals("enterPropagator") && item.getJavaMethodName().equals("append") && callChain.getJavaMethodName().equals("append"))
				return;
			if (item.getChainType().equals("leavePropagator") && item.getJavaMethodName().equals("append") && callChain.getJavaMethodName().equals("append"))
				return;
		}
		this.callChain.add(callChain);
	}

}
