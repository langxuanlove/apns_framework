package com.kk.aop;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.kk.utils.IPAddressUtil;

/**
 * @Description:日志记录操作,使用AOP实现
 * 
 * @author Jikey
 * 
 */
@Aspect
@Service(value="logAspect")
public class LogAspect {

	private final Logger logger =Logger.getLogger(LogAspect.class);
	private String requestPath = null; // 请求地址
	private String userName = null; // 用户名
	private String userId=null;
	private Map<?, ?> inputParamMap = null; // 传入参数
	private Map<String, Object> outputParamMap = null; // 存放输出结果
	private long startTimeMillis = 0; // 开始时间
	private long endTimeMillis = 0; // 结束时间
	/** 
     *  
     * @Title：doBefore
     * @Description: 方法调用前触发;切面的前置方法 即方法执行前拦截到的方法 记录并输出在目标方法执行之前的通知
     *  记录开始时间  
     * @author Jikey  
     * @param joinPoint 
     */  
	@Before(value = "execution(* com.gnet.action.controller.*.*(..))")
	public void doBefore(JoinPoint joinPoint) {
		startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
		logger.info("\n*********************************************************************");
		logger.info("\n开始时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis)+";开始执行方法:" + joinPoint.getTarget().getClass().getName()
				+ "." + joinPoint.getSignature().getName());
	}
	 /** 
     *  
     * @Title：doAfter
     * @Description: 方法调用后触发;切面的后置方法，不管抛不抛异常都会走此方法目标方法执行之后的通知
     *  记录结束时间 
     * @author Jikey
     * @param joinPoint 
	 * @throws IOException 
     */ 
	@After(value = "execution(* com.gnet.action.controller.*.*(..))")
	public void doAfter(JoinPoint joinPoint) throws IOException {
		endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
		this.printOptLog();
	}
	 /** 
     *  
     * @Title：afterReturn
     * @Description:  在方法正常执行通过之后执行的通知叫做返回通知可以返回到方法的返回值 在注解后加入returning
     *  在方法正常执行通过之后执行的通知叫做返回通知。此时注意，不仅仅使用JoinPoint获取连接点信息，
     *  同时要在返回通知注解里写入，resut="result"。在切面方法参数中加入Object result,用于接受返回通知
     *  的返回结果。如果目标方法方法是void返回类型则返回NULL,如果目标方法方法是String返回类型则返回实际的结果. 
     * @author Jikey
     * @param joinPoint
	 * @param result
	 * @throws IOException 
     */ 
    @AfterReturning(value="execution(* com.gnet.action.controller.*.*(..))",returning="result",argNames="joinPoint,result")
    public void afterReturn(JoinPoint joinPoint,Object result ){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String rightnow=sdf.format(date);
        System.out.println(rightnow+"执行了【"+object+"方法正常执行结束......】"+"【返回结果:"+result+"】"); 
        System.out.println("√√√√√√√√√√√√√√√√√√√√√√方法正确结束√√√√√√√√√√√√√√√√√√√√√√");
    }
    
    /** 
     *  
     * @Title：afterReturn
     * @Description:在目标方法非正常执行完成 发生异常 抛出异常的时候会走此方法
     * 获得异常可以用throwing
     * @author Jikey
     * @param joinPoint
     * @param ex
	 * @throws IOException 
     */
    @AfterThrowing(value="execution(* com.gnet.action.controller.*.*(..))",throwing="ex")
    public void afterThrowing(JoinPoint joinPoint,Exception ex ){
        Object object = joinPoint.getSignature();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String rightnow=sdf.format(date);
        logger.info(rightnow+"执行了【"+object+"方法发生异常......】"+"【异常报告:"+ex+"】"); 
        logger.info("xxxxxxxxxxxxxxxxxx方法发生异常结束xxxxxxxxxxxxxxxxxx");
    }
	 /** 
     *  
     * @Title：doAround 
     * @Description: 环绕触发  
     * @param pjp 
     * @return 
     * @throws Throwable 
     */  
    @Around(value="execution(* com.gnet.action.controller.*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {  
        /** 
         * 1.获取request信息 
         * 2.根据request获取session 
         * 3.从session中取出登录用户信息 
         */  
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;  
        HttpServletRequest request = sra.getRequest();  
        request.setCharacterEncoding("UTF-8");
        // 从session中获取用户信息  
        userId = (String) request.getSession().getAttribute("userId");
        userName = (String) request.getSession().getAttribute("userName");
        // 获取输入参数  
        inputParamMap =getNewMap(request);  
        // 获取请求地址  
        requestPath = "客户端请求IP地址:"+IPAddressUtil.getIpAddr(request)+";\n请求路径:"+request.getRequestURL();  
        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行  
        outputParamMap = new HashMap<String, Object>();  
        Object result = pjp.proceed();// result的值就是被拦截方法的返回值  
        outputParamMap.put("result",result);  
        return result;  
    }  
   
    
	/**
	 * 
	 * @Title：printOptLog
	 * @Description: 输出日志
	 * @author Jikey
	 * @throws IOException 
	 */
	private void printOptLog() throws IOException {
		String optTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimeMillis);
		logger.info("\n结束时间:"+optTime+";用户名:"+userName+";用户ID:"+userId+";url:" + requestPath+";\n"
				 + "运行时间:"+ (endTimeMillis - startTimeMillis) + "ms;\n" 
				 + "输入参数:"   + JSON.toJSON(inputParamMap) + ";\n" 
				 + "输出参数:" + JSON.toJSON(outputParamMap));
		logger.info("\n*********************************************************************");
	}
	/**
	 * @Description:解决中文乱码问题
	 * @param request
	 * @return
	 * @throws Exception
	 */
	 public Map<?, ?>  getNewMap(HttpServletRequest request) throws Exception{
   	  Map<String,String[]> map = request.getParameterMap();
   	  Map<String,String[]> newmap = new HashMap<>();     
        for(Map.Entry<String, String[]> entry : map.entrySet()){  
            String name = entry.getKey();  
            String values[] = entry.getValue();  
            if(values==null){  
                newmap.put(name, new String[]{});  
                continue;  
            }  
            String newvalues[] = new String[values.length];  
            for(int i=0; i<values.length;i++){  
                String value = values[i];  
                value = new String(value.getBytes("iso8859-1"),"UTF-8");  
                newvalues[i] = value; //解决乱码后封装到Map中  
            }  
            newmap.put(name, newvalues);  
        }  
        return newmap;
   }
}
