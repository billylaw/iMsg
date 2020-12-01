/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wind.ibroker.common;

import org.apache.commons.lang3.StringUtils;
import wind.ibroker.common.windstream.Serializer;
import wind.ibroker.common.windstream.WindStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 与客户端通信的基础类
 * @author nbwu
 */
public class CommonMessage {
    public static final byte ERROR_CODE_SUCCESS = 0;
    
    public static final byte ERROR_CODE_REDIRECT = 4;
    
    /**
     * 数据
     */
    private final List<WindStream> data;
    
    /**
     * 错误代码
     */
    private byte errorCode;
    
    /**
     * 错误消息
     */
    private String errorMsg;
    
    /**
     * 响应数据（预先序列化）
     */
    private final List<byte[]> streamBytes;
    
    public CommonMessage(){
        data = new ArrayList<WindStream>();
        streamBytes = new ArrayList<byte[]>();
        errorCode = ERROR_CODE_SUCCESS;
        errorMsg = StringUtils.EMPTY;
    }

    public CommonMessage(byte[] input) throws IOException {
        this();
        
        try(ByteArrayInputStream ms = new ByteArrayInputStream(input)){
            try(DataInputStream br = new DataInputStream(ms)){
                errorCode = Serializer.readByte(br);
                errorMsg = Serializer.readString(br);
                
                byte count = Serializer.readByte(br);
                for(int i=0; i<count; i++){
                    WindStream ws = new WindStream();
                    ws.deserialize(br);
                    
                    data.add(ws);
                }
            }
        }
    }
    
    /**
     * 添加WindStream
     * @param ws WindStream
     */
    public void addData(WindStream ws) {
        data.add(ws);
    }

    /**
     * 序列化消息
     * @throws IOException
     * @return 序列化结果
     */
    public byte[] serialize() throws IOException {
        byte[] output;
        
        try(ByteArrayOutputStream ms = new ByteArrayOutputStream()){
            try(DataOutputStream bw = new DataOutputStream(ms)){
                Serializer.writeByte(bw, errorCode);
                Serializer.writeString(bw, errorMsg);
                Serializer.writeByte(bw, (byte)(this.data.size() + streamBytes.size()));
                
                for(WindStream stream : this.data){
                    stream.serialize(bw);
                }
                
                for(byte[] bytes : streamBytes){
                    bw.write(bytes);
                }
                
                output = ms.toByteArray();
            }
        }
        
        return output;
    }

    public byte getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public WindStream getData(int i) {
        return this.data.get(i);
    }
    
    public List<WindStream> getData(){
        return this.data;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }

    public void addStreamBytes(byte[] bytes) {
        this.streamBytes.add(bytes);
    }
}
