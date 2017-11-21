package com.kk.utils.iot;

import io.netty.util.internal.logging.AbstractInternalLogger;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import tracetool.SendMode;
import tracetool.TTrace;
import tracetool.TraceDisplayFlags;
import tracetool.TraceNode;
import tracetool.TraceNodeEx;
import tracetool.TraceTable;
import tracetool.Utility;
import tracetool.WinTrace;
import tracetool.WinWatch;

public class Log {
    
    private static final String LOGGER_NAME = "traccar";
    
    private static Logger logger = null;
    
    public static void setupLogger(Properties properties) throws IOException {

        Layout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %5p: %m%n");

        Appender appender = new DailyRollingFileAppender(
                layout, properties.getProperty("logger.file"), "'.'yyyyMMdd");
        
        
      //  logger.
        LogManager.resetConfiguration();
        logger = Logger.getLogger(LOGGER_NAME);
        
        logger.addAppender(appender);
        logger.setLevel(Level.ALL);
      // log4j.appender.R.encoding=UTF8

        // Workaround for "Bug 745866 - (EDG-45) Possible netty logging config problem"
        InternalLoggerFactory.setDefaultFactory(new InternalLoggerFactory() {
            @Override
            public InternalLogger newInstance(String string) {
                return new NettyInternalLogger(string);
            }
        });
    }

    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(LOGGER_NAME);
            logger.setLevel(Level.OFF);
        }
        return logger;
    }
    
    public static void logSystemInfo() {
        try {
            OperatingSystemMXBean operatingSystemBean = ManagementFactory.getOperatingSystemMXBean();
            Log.info("Operating System" +
                " name: " + operatingSystemBean.getName() +
                " version: " + operatingSystemBean.getVersion() +
                " architecture: " + operatingSystemBean.getArch());

            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            Log.info("Java Runtime" +
                " name: " + runtimeBean.getVmName() +
                " vendor: " + runtimeBean.getVmVendor() +
                " version: " + runtimeBean.getVmVersion());

            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            Log.info("Memory Limit" +
                " heap: " + memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024) + "mb" +
                " non-heap: " + memoryBean.getNonHeapMemoryUsage().getMax() / (1024 * 1024) + "mb");
        } catch (Exception e) {
            Log.warning("Failed to get system info");
        }
    }
    
    public static void error(String msg) {
        getLogger().error(msg);
        TTrace.debug().send(msg);
    }

    public static void warning(String msg) {
        getLogger().warn(msg);
        TTrace.debug().send(msg);
    }

    public static void warning(Throwable exception) {
        warning(null, exception);
    }
    
    
    public static void error(Throwable exception){
    	getLogger().error(null,exception);
    }
    

    public static void warning(String msg, Throwable exception) {
        StringBuilder s = new StringBuilder();
        if (msg != null) {
            s.append(msg);
            s.append(" - ");
        }
        if (exception != null) {
            String exceptionMsg = exception.getMessage();
            if (exceptionMsg != null) {
                s.append(exceptionMsg);
                s.append(" - ");
            }
            s.append(exception.getClass().getName());
            StackTraceElement[] stack = exception.getStackTrace();
            if (stack.length > 0) {
                s.append(" (");
                s.append(stack[0].getFileName());
                s.append(":");
                s.append(stack[0].getLineNumber());
                s.append(")");
            }
        }
        getLogger().warn(s.toString());
        TTrace.debug().send(s.toString());
    }

    public static void info(String msg) {
        getLogger().info(msg);
        TTrace.debug().send(msg);
    }

    public static void debug(String msg) {
        getLogger().debug(msg);
        TTrace.debug().send(msg);
    }

    /**
     * Netty logger implementation
     */
    private static class NettyInternalLogger extends AbstractInternalLogger {



    	
		protected NettyInternalLogger(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void debug(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void debug(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void info(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isDebugEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isErrorEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isInfoEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isTraceEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isWarnEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void trace(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void trace(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0, Object arg1) {
			// TODO Auto-generated method stub
			getLogger().warn("netty warning: " + arg0);
		}

		@Override
		public void warn(String arg0, Object... arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0, Throwable arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void warn(String arg0, Object arg1, Object arg2) {
			// TODO Auto-generated method stub
			
		}


	

    }

}
