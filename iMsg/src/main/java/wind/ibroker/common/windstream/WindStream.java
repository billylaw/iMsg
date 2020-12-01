package wind.ibroker.common.windstream;


import org.apache.commons.lang3.StringUtils;
import wind.ibroker.comm.DataRow;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * WindStream数据传输结构
 * */
public class WindStream implements IWindStreamSerilize{
    private static final int MAX_PKG_SIZE = 8185;
    /**
     * windstream头
     * */
    private WindStreamHeader header = new WindStreamHeader();

    /**
     * 数据记录集合
     * */
    private List<DataRow> rows = new ArrayList<DataRow>();

    /**
     * 字段列表
     * */
    private List<WindStreamDataField> dataFields = new ArrayList<WindStreamDataField>();

    public WindStream(){
        header.setIsCompress(false);
        header.setVersion((short)(0x01 + 1 << 0x00));
        header.setSize(0);
        header.setPackName(StringUtils.EMPTY);
    }

    public WindStreamHeader getHeader() {
        return header;
    }

    public List<DataRow> getRows() {
        return rows;
    }

    /**
     * 序列化到输出流
     * @param writer 输出流
     * */
    public void serialize(DataOutputStream writer) throws IOException {
        try(ByteArrayOutputStream _os = new ByteArrayOutputStream()){
            try(DataOutputStream _writer = new DataOutputStream(_os)){

                if(this.dataFields == null || this.dataFields.size() == 0){
                    Serializer.writeInt16(_writer, (short)0);
                }else{
                    Serializer.writeInt16(_writer, (short)this.dataFields.size());

                    for(WindStreamDataField field : this.dataFields){
                        field.serialize(_writer);
                    }
                }

                if(this.rows == null || this.rows.isEmpty()){
                    Serializer.writeInt32(_writer, 0);
                }else{
                    Serializer.writeInt32(_writer, this.rows.size());
                    for(DataRow row : this.rows){
                        WindStreamSerializer.windStreamWriteObject(row, this.dataFields, _writer);
                    }
                }

                _writer.flush();
                _os.flush();
                byte[] _data = _os.toByteArray();
                if(_data.length >= MAX_PKG_SIZE){
                    byte[] _cdata = WindTools.gzip(_data);
                    this.header.setIsCompress(true);
                    this.header.setSize(_cdata.length);
                    this.header.serialize(writer);
                    writer.write(_cdata);

                    WindTools.log(this, String.format("压缩数据流，PackName: %s, 压缩前大小: %d, 压缩后大小: %d", this.header.getPackName(), _data.length, _cdata.length));
                }else{
                    this.header.setSize(_data.length);
                    this.header.serialize(writer);
                    writer.write(_data);
                }
            }
        }
        
        writer.flush();
    }

    public void setFields(WindStreamDataField[] fields) {
        this.dataFields = new ArrayList<WindStreamDataField>();
        for(int i=0; i<fields.length; i++){
            this.dataFields.add(fields[i]);
        }
    }

    /**
     * WindStream反序列化
     * @param br 输入流
     */
    public Object deserialize(DataInputStream br) throws IOException {
        ByteArrayInputStream _ms = null;
        DataInputStream _br = null;
        
        try{
            this.header.deserialize(br);
            byte[] _data = new byte[this.header.getSize()];
            
            br.read(_data, 0, _data.length);            
            if(_data.length == 0)
                return this;

            //是否压缩
            if(this.header.getIsCompress()){
                //解压
                byte[] _cdata = WindTools.ungzip(_data);
                _ms = new ByteArrayInputStream(_cdata);
            }else{
                _ms = new ByteArrayInputStream(_data);
            }

            _br = new DataInputStream(_ms);
            int len = Serializer.readInt16(_br);
            WindStreamDataField df;
            if(len <= 0)
                return this;

            for(int i=0; i<len; i++){
                df = new WindStreamDataField();
                df.deserialize(_br);
                this.dataFields.add(df);
            }

            List<Object> rt;
            len = Serializer.readInt32(_br);
            if(len <= 0)
                return this;

            for(int i=0; i<len; i++){
                rt = new ArrayList<Object>();
                for(int j=0; j<this.dataFields.size(); j++){
                    rt.add(WindStreamSerializer.windStreamReadObject(this.dataFields.get(j).getDataType(), _br));
                }
                
                Object[] objs = new Object[rt.size()];
                rt.toArray(objs);
                this.rows.add(new DataRow(objs));
            }

            return this;
        }finally{
            if(_ms != null){
                try {
                    _ms.close();
                } catch (IOException ex) {
                    WindTools.log(this, ex.toString());
                }
            }
            if(_br != null){
                try {
                    _br.close();
                } catch (IOException ex) {
                    WindTools.log(this, ex.toString());
                }
            }
        }
    }

    /**
     * 获取字段列表
     * @return 字段列表
     */
    public WindStreamDataField[] getFields() {
        WindStreamDataField[] fields = new WindStreamDataField[this.dataFields.size()];
        this.dataFields.toArray(fields);
        
        return fields;
    }

    /**
     * 添加行
     * @param objects 行数据
     * */
    public void addRow(Object[] objects) {
        DataRow row = new DataRow(objects);
        this.rows.add(row);
    }
}
