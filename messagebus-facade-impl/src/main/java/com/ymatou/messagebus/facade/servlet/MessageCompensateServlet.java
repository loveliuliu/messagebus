/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.messagebus.facade.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ymatou.messagebus.domain.task.MessageCompensateTaskManager;


/**
 * 消息补偿定时任务
 * 
 * @author wangxudong 2016年7月27日 下午4:48:31
 *
 */
public class MessageCompensateServlet extends HttpServlet {

    private static Logger logger = LoggerFactory.getLogger(MessageCompensateServlet.class);

    /**
     * 序列化版本
     */
    private static final long serialVersionUID = 1L;

    // 补单任务管理器
    private MessageCompensateTaskManager taskManager = new MessageCompensateTaskManager();


    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            taskManager.initCompensateTask();
            logger.info("message compensate servlet init.");
        } catch (Exception ex) {
            logger.error("message compensate task start failed", ex);
            throw new ServletException("message compensate task start failed", ex);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out;
        resp.setContentType("text/html;charset=UTF-8");
        out = resp.getWriter();

        String path = req.getPathInfo();
        out.print("compensate task control: " + path + "<br/>");

        String result = execute(path);

        out.print(result);
        out.close();
    }

    /**
     * 执行路径对应的命令
     * 
     * @param path
     * @return
     */
    private String execute(String path) {
        String command = "status";
        if (!StringUtils.isEmpty(path)) {
            command = path.trim();
            if (path.startsWith("/")) {
                command = command.substring(1);
            }
        }

        try {
            switch (command) {
                case "stop":
                    return stop();
                case "start":
                    return start();
                default:
                    return "invalid command.";
            }
        } catch (Exception e) {
            logger.error("execute compensate task control failed.", e);
            return "execute fail with ex: " + e.getMessage();
        }
    }

    /**
     * 停止任务
     * 
     * @param out
     */
    private String stop() throws Exception {
        if (taskManager.isStarted()) {
            taskManager.stopAll();

            logger.info("compensate task all stop success.");
            return "stop success!";
        } else {
            return "task allready stop.";
        }
    }

    /**
     * 启动任务
     * 
     * @param out
     */
    private String start() throws Exception {
        if (taskManager.isStarted()) {
            return "task allready start.";
        } else {
            taskManager.startAll();
            logger.info("compensate task all start success.");
            return "start success!";
        }
    }
}
