package wind.ibroker.comm;

import wind.ibroker.util.BaseLog;
import wind.ibroker.common.DataField;
import wind.ibroker.common.DataType;
import wind.ibroker.common.StoreBase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RecordSet implements Serializable {
    /// <summary>
    /// 超过8K就压缩
    /// </summary>
    private static final  int byte_MAXLENGTH = 8096;

    public RecordSetHeader getHeader() {
        return header;
    }

    public void setHeader(RecordSetHeader header) {
        this.header = header;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<DataField> getFields() {
        return fields;
    }

    public void setFields(List<DataField> fields) {
        this.fields = fields;
    }

    public List<DataRow> getRows() {
        return rows;
    }

    public void setRows(List<DataRow> rows) {
        this.rows = rows;
    }

    public byte[] getIndicatorRights() {
        return indicatorRights;
    }

    public void setIndicatorRights(byte[] indicatorRights) {
        this.indicatorRights = indicatorRights;
    }

    public byte[] getWindCodeRights() {
        return windCodeRights;
    }

    public void setWindCodeRights(byte[] windCodeRights) {
        this.windCodeRights = windCodeRights;
    }

    /// <summary>
    /// 头
    /// </summary>
    private RecordSetHeader header;
    /// <summary>
    /// RecordSet版本号
    /// </summary>
    private int version;
    /// <summary>
    /// 数据对象定义
    /// </summary>
    private List<DataField> fields;
    /// <summary>
    /// 数据对象
    /// </summary>
    private List<DataRow> rows;
    /// <summary>
    /// 指标的权限
    /// </summary>
    private byte[] indicatorRights;
    /// <summary>
    /// WindCode的权限
    /// </summary>
    private byte[] windCodeRights;
    /// <summary>
    /// 数据字节流
    /// 缺省支持第三版本
    /// </summary>
    public byte[] serialize() {
        ByteArrayOutputStream byteOS = null;
        DataOutputStream writer = null;
        try {
            byteOS = new ByteArrayOutputStream();
            writer = new DataOutputStream(byteOS);
            byte[] data_bytes = dataSerialize();
            if(data_bytes == null){
                this.header.setOk(false);
                this.header.setSize(0);
                RecordSetHeader.serialize(writer, this.header);
                return byteOS.toByteArray();
            }
            this.header.setSize(data_bytes.length);
            RecordSetHeader.serialize(writer, this.header);
            writer.write(data_bytes);
            return byteOS.toByteArray();
        }catch (IOException ex){
            BaseLog.logError(this.getClass(), "Recordset serialize failed",ex);
            this.header = new RecordSetHeader();
            this.header.setOk(false);
            this.header.setCompressed(false);
            this.header.setInformation(ex.toString());
            this.header.setText(false);
            this.header.setSize(10);
            return this.header.serializeSelf();
        }finally {
            if(byteOS != null) {
                try {
                    byteOS.close();
                } catch (IOException e) {
                   BaseLog.logError(RecordSet.class,"close stream failed",e);
                }
            }
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    BaseLog.logError(RecordSet.class,"close stream failed",e);
                }
            }
        }
    }

    public byte[] dataSerialize() throws IOException {
        ByteArrayOutputStream byteOS = null;
        //DataOutputStream writer = null;
        try {
            byteOS = new ByteArrayOutputStream();
            //writer = new DataOutputStream(byteOS);
            ReportSerializer.writeInt(byteOS, this.version);
            ReportSerializer.writeInt(byteOS,rows.size());
            ReportSerializer.writeInt(byteOS, fields.size());
            for(int i=0;i<fields.size(); ++i){
                fields.get(i).serialize(byteOS);
            }
            ReportSerializer.writeByte(byteOS, DataType._STOP);
            for (DataRow row:this.rows) {
                row.serialize(byteOS, fields);
            }
            return byteOS.toByteArray();
        }catch (IOException ex){
            BaseLog.logError(this.getClass(), "recordset dataSerialize failed",ex);
        }finally {
            if(byteOS != null){
                byteOS.close();
            }
        }
        return null;
    }

    /// <summary>
    /// 新增一个DataRow
    /// </summary>
    /// <param name="row">DataRow对象</param>
    public void add(DataRow row){
        if(row == null || row.size() !=  this.fields.size())
            return;
        rows.add(row);
    }

    /// <summary>
    /// 构造函数
    /// </summary>
    /// <param name="fields">field的数据定义</param>
    public RecordSet(List<DataField> fields, RecordSetHeader header) throws Exception {
        if(fields == null || fields.size() == 0)
            throw new Exception("不能创建一个没有定义的RecordSet");
        if(header == null)
            throw new Exception("不能创建一个没有头定义的RecordSet");
        this.fields = fields;
        this.rows = new ArrayList<DataRow>();
        this.header = header;
        this.version = 3;
    }

    /// <summary>
    /// Wuds 反序列化
    /// </summary>
    /// <param name="reportBytes"></param>
    public RecordSet(byte[] reportBytes) throws Exception {
        if (reportBytes == null || reportBytes.length == 0)
            throw new Exception("不能创建一个没有数据流的RecordSet");
//        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
//        DataOutputStream writer = new DataOutputStream(byteOS);
        ByteArrayInputStream byteIS = new ByteArrayInputStream(reportBytes);
        DataInputStream reader = new DataInputStream(byteIS);

        DataField field;
        this.version = 3;
        this.header = new RecordSetHeader();
        this.header.setVersion(ReportSerializer.readShort(reader));
        this.header.setFormat(ReportSerializer.readByte(reader));
        this.header.setCompressed(ReportSerializer.readBool(reader));
        this.header.setOk(ReportSerializer.readBool(reader));
        this.header.setText(ReportSerializer.readBool(reader));
        this.header.setSize(ReportSerializer.readInt(reader));
        if(this.header.isText()){
            this.header.setInformation(ReportSerializer.readString(reader));
            return;
        }

        int reportVersion = ReportSerializer.readInt(reader);
        //报表查询结果记录数
        int recCount = ReportSerializer.readInt(reader);
        //报表查询结果列数
        int columnCount = ReportSerializer.readInt(reader);

        fields = new ArrayList<DataField>(columnCount);
        rows = new ArrayList<DataRow>(recCount);
        //反序列化列
        for(int i=0; i<columnCount; ++i){
            field = new DataField();
            field.setFieldName(ReportSerializer.readString(reader));
            field.setDataType(ReportSerializer.readByte(reader));
            fields.add(field);
        }
        byte stop = ReportSerializer.readByte(reader);
        if(fields == null || fields.size() == 0)
            throw new  Exception("不能创建一个没有数据结构的RecordSet");
        //序列化行
        DataRow dr;
        List<Object> objs;
        for(int j=0; j<recCount; ++j){
            objs = new ArrayList<Object>();
            for (DataField item: fields
                 ) {
                objs.add((ReportSerializer.wudsReadObject(reader, item)));
            }
            dr = new DataRow(objs);
            rows.add(dr);
        }
    }

    /// <summary>
    /// 取第一个数据对象Row转化成StoreBase对象
    /// </summary>
    /// <returns></returns>
    public StoreBase toStoreBaseOne(){
        if(fields == null || fields.size() == 0)
            return null;
        if(rows == null || rows.size() == 0 || this.rows.get(0).getData() == null)
            return null;
        if(fields.size() !=  this.rows.get(0).getData().size())
            return null;
        StoreBase rt = new StoreBase();
        for(int i=0; i< this.fields.size(); ++i)
            rt.set(this.fields.get(i).getFieldName(),this.rows.get(0).get(i));
        return rt;
    }

    /// <summary>
    /// Row转化成StoreBase对象
    /// </summary>
    /// <returns></returns>
    public StoreBase[] toStoreBase(){
        if(fields == null || fields.size() == 0)
            return null;
        if(rows == null || rows.size() == 0 || this.rows.get(0).getData() == null)
            return null;
        if(fields.size() !=  this.rows.get(0).getData().size())
            return null;
        List<StoreBase> lsSB = new ArrayList<StoreBase>(rows.size());
        StoreBase sb;

        int filedSize = fields.size();
        //提前将字段转为大写
        List<String> fieldNameList = new ArrayList<>(filedSize);
        for(int i=0;i<filedSize;i++)
        {
            fieldNameList.add(fields.get(i).getFieldName().trim().toUpperCase());
        }

        for (DataRow dr: rows) {
            sb = new StoreBase();
            for(int i=0; i< filedSize; ++i){
                sb.set(fieldNameList.get(i), dr.get(i));
            }
            lsSB.add(sb);
        }
        return lsSB.toArray(new StoreBase[lsSB.size()]);
    }

}
