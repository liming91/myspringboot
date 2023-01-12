package com.ming.controller.ws;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.ming.entities.YbWardCallRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(value = "/webSocket/{wardCode}")
public class MyWebSocket {

    // 用来存放每个客户端对应的MyWebSocket对象
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    // 用来记录sessionId和该session进行绑定
    private static Map<String, Session> map = Maps.newHashMap();

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;

    /**
     * 连接建立成功调用的方法
     * @param wardCode
     * @param session
     */
    @OnOpen
    public void onOpen (@PathParam("wardCode") String wardCode, Session session) {
        this.session = session;
        // 如果map中存在key，则移除之前的session，重新赋值
        if (map.containsKey(wardCode)) {
            for(MyWebSocket webSocket: webSocketSet) {
                if (map.get(wardCode).equals(webSocket.session)) {
                    webSocketSet.remove(webSocket);
                }
            }
        }
        // 给病区重新赋值
        map.put(wardCode, session);
        // 加入set
        webSocketSet.add(this);
        log.info("有新连接加入！病区编码为： {} ,  当前在线人数为: {} ", wardCode, webSocketSet.size());
        // this.session.getAsyncRemote().sendText("连接成功频道号为: "+wardCode+"  当前在线人数为: "+ webSocketSet.size());
    }

    /**
     * 连接关闭
     * @param wardCode
     * @param session
     */
    @OnClose
    public void onClose (@PathParam("wardCode") String wardCode, Session session) {
        this.session = session;
        // 从map中删除
        map.remove(wardCode);
        // 从set中删除
        webSocketSet.remove(this);
        log.info("频道: {} 连接关闭, 当前在线人数为: {}", wardCode, webSocketSet.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息（json格式）
     */
    @OnMessage
    public void onMessage (String message, Session session) throws IOException {
        if (!message.equals("123456789")) {
            // 南昌修改
            YbWardCallRecord ybWardCallRecord = JSON.parseObject(message, YbWardCallRecord.class);
            // 获取病区号
            String wardCode = ybWardCallRecord.getWardCode();
            if(isStringEmpty(wardCode)){
                wardCode ="zhkb";
            }
            session = map.get(wardCode);
            if(session != null) {
                session.getBasicRemote().sendText(message);
                log.info("~~~~~病区：{} 消息发送成功!~~~~~~~", wardCode);
            }
        } else {
            log.info("心跳消息: {}, 当前在线：{} 个病区。", message, webSocketSet.size());
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 连接错误
     * @param session
     * @param throwable
     */
    @OnError
    public void onError (Session session, Throwable throwable) {
        log.error("webSocket连接发生错误: {} ", throwable.getMessage());
    }

    /**
     * 判断string是否为空(null不为空)
     * @param	string
     * @return	boolean
     */
    public static boolean isStringEmpty(String string) {
        boolean bool = true;
        String emptyString = "null";
        if(!"".equalsIgnoreCase(string) && string != null){
            if(!emptyString.equals(string)){
                bool = false;
            }
        }
        return bool;
    }

}
