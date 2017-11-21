package com.kk.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * <table width="100%" border="1px">
 * <tr>
 * <td width="20%">作者</td><td width="80%" colspan="2">sam</td>
 * </tr>
 * <tr>
 * <td width="20%">版本</td><td width="80%" colspan="2">v1.0</td>
 * </tr>
 * <tr>
 * <td width="20%">创建日期</td><td width="80%" colspan="2">2013-06-24</td>
 * </tr>
 * <tr>
 * <td width="100%" colspan="3">修订记录:</td>
 * <tr>
 * <td width="20%">修改日期</td><td width="20%">修改人</td><td width="60%">修改记录</td>
 * </tr>
 * <tr>
 * <td width="20%">-------</td><td width="20%">-------</td><td width="60%">--------------</td>
 * </tr>
 * <tr>
 * <td width="20%">描述信息</td><td width="80%" colspan="2"> 这个类提供了一些根据类的class文件位置来定位的方法。</td>
 * </tr>
 * </tr>
 * </table>
 */
public class PathUtil {
	/**
	 * 描述信息:获取一个类的class文件所在的绝对路径。 这个类可以是JDK自身的类，也可以是用户自定义的类，或者是第三方开发包里的类。
	 * 				只要是在本程序中可以被加载的类，都可以定位到它的class文件的绝对路径。
	 * 
	 * @param cls
	 *            一个对象的Class属性
	 * @return 这个类的class文件位置的绝对路径。 如果没有这个类的定义，则返回null。
	 */
	public static String getPathFromClass(Class cls) throws IOException {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			path = file.getCanonicalPath();
		}
		return path;
	}

	/**
	 * 描述信息:这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。
	 *				通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。
	 *				比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt，
	 *				那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class)
	 *				得到的结果是txt文件的在系统中的绝对路径。
	 * 
	 * @param relatedPath
	 *				相对路径
	 * @param cls
	 *				用来定位的类
	 * @return 相对路径所对应的绝对路径
	 * @throws IOException
	 *				因为本方法将查询文件系统，所以可能抛出IO异常
	 */
	public static String getFullPathRelateClass(String relatedPath, Class cls)
			throws IOException {
		String path = null;
		if (relatedPath == null) {
			throw new NullPointerException();
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		String tempPath = clsFile.getParent() + File.separator + relatedPath;
		File file = new File(tempPath);
		path = file.getCanonicalPath();
		return path;
	}

	/**
	 * 描述信息:获取类的class文件位置的URL。这个方法是本类最基础的方法，供其它方法调用。
	 */
	private static URL getClassLocationURL(final Class cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/')
				.concat(".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			if (cs != null)
				result = cs.getLocation();

			if (result != null) {
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar")
								|| result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:"
									.concat(result.toExternalForm())
									.concat("!/").concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory())
							result = new URL(result, clsAsResource);
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}

		if (result == null) {
			// Try to find 'cls' definition as a resource;
			// this is not
			// document．d to be legal, but Sun's
			// implementations seem to //allow this:
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource)
					: ClassLoader.getSystemResource(clsAsResource);
		}
		return result;
	}
	
	/**
	 * 
	 * 描述信息:	获得web工程WEB-INF的所在路径
	 * 
	 * @return
	 * 				web工程WEB-INF的所在路径
	 */
	public static String getWebInfPath(){  
        URL url = PathUtil.class.getProtectionDomain().getCodeSource().getLocation();  
        String path = url.toString();  
        int index = path.indexOf("WEB-INF");  
          
        if(index == -1){  
            index = path.indexOf("classes");  
        }  
          
        if(index == -1){  
            index = path.indexOf("bin");  
        }  
          
        path = path.substring(0, index);  
          
        if(path.startsWith("zip")){//当class文件在war中时，此时返回zip:D:/...这样的路径  
            path = path.substring(4);  
        }else if(path.startsWith("file")){//当class文件在class文件中时，此时返回file:/D:/...这样的路径  
            path = path.substring(6);  
        }else if(path.startsWith("jar")){//当class文件在jar文件里面时，此时返回jar:file:/D:/...这样的路径  
            path = path.substring(10);   
        }  
        try {  
            path =  URLDecoder.decode(path, "UTF-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        String os = System.getProperties().getProperty("os.name");
        if(os.startsWith("win") || os.startsWith("Win") ){
        	return path;  
        }else{
        	return "/"+path;  
        }
    }  

	public static void main(String[] args) {
		try {
			System.out.println(getPathFromClass(PathUtil.class));
			System.out.println(getFullPathRelateClass("../test/abc/..",
					PathUtil.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}