package com.aep.app.sinanews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

import com.aep.app.sinanews.model.AccessorModel;

/**
 * Servlet implementation class SinaNews
 */
public class SinaNews extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final String url = "http://api.sdpaep.com/sinanews/societyfocus/v1";
    private static HashMap<String, String> m = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SinaNews() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public String  getSinaNews(String url, PrintWriter out)
    {
    	AccessorModel accessorModel = new AccessorModel();
    	GetMethod getRequest = new GetMethod(url);
    	HttpClient client = new HttpClient();
    	
    	m = accessorModel.getAuthHeader(accessorModel.getNonce(), accessorModel.getCreatedTime());
    	getRequest.addRequestHeader("Authorization", m.get("Authorization"));
    	getRequest.addRequestHeader("X-WSSE", m.get("X-WSSE"));
    	
    	/////////
    	//out.println("Authorization: " + m.get("Authorization"));
    	//out.println("X-WSSE: " + m.get("X-WSSE"));
    	/////////
    	try {
    		//out.println("Begin to client.executeMethod(getRequest)");
			client.executeMethod(getRequest);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//out.println("after executeMethod(getRequest)");
    	
    	String respStr = null;
    	try {
    		//respStr = getRequest.getResponseBodyAsString();
    		//out.println("Begin to getRequest.getResponseBodyAsStream()");
    		InputStream inputStream = getRequest.getResponseBodyAsStream();
    		//ʹ������ʽ��ȡ���ݣ����ö�ȡ����ΪUTF-8����
    		//��Ȼ�����response.setContentType("text/html;charset=UTF-8")Ҳû�á�
    		//��Ϊ�����������Ѿ������ˡ�
    		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    		StringBuffer stringBuffer = new StringBuffer();
    		String strTemp = "";
    		while ((strTemp = br.readLine())!=null)
    		{
    			stringBuffer.append(strTemp);
    		}
    		
    		respStr = stringBuffer.toString();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//out.println("respStr: " + respStr);
    	return respStr;
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*
		Enumeration em = request.getHeaderNames();
		while(em.hasMoreElements())
		{
			System.out.println(em.nextElement());
		}
		*/
		//response.setContentType("text/xml;charset=UTF-8");
		response.setContentType("text/xml;charset=utf-8");
		
		//response.setCharacterEncoding("utf-8"); 
		
		//response.getWriter().println("�Ҳ�!");
		

		//System.out.println(System.getProperty("file.encoding"));//��ǰϵͳgbk
		
		//�������ΪUTF-8����Ҫ���롣
		//getWriter һ��Ҫ��response���ñ���֮�󣡣�
		//�����response.setContentType֮ǰ����ô��ʱ��getWriter���ò���contenttype���ݵ�
		PrintWriter out = response.getWriter();
		String respStr = this.getSinaNews(url, out);
		//System.out.println(respStr);
		out.println(respStr);
		
		String path = request.getContextPath();
		//out.println("contextpath: " + path);
		//out.println("servletpath: " +request.getSession().getServletContext().getRealPath("/"));
		//out.println("after the response Str");
		//out.println(request.toString());
		
	}

}
