package wind.ibroker.comm;


import wind.ibroker.common.DataField;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WindStream数据记录
 * */
public class DataRow {
    /**
     * 数据列表
     * */
    public List<Object> data;

    public DataRow(List<Object> obj) {
        this.data = obj;
    }

    public DataRow(Object[] obj){
        data = new ArrayList<Object>(Arrays.asList(obj));
    }

    /**
     * 获取数据列数
     * @return 数据列数
     * */
    public int size() {
        return this.data == null ? 0 : this.data.size();
    }

    /**
     * 获取列数据
     * @param i 位置
     * @return 数据
     * */
    public Object get(int i) {
        return data.get(i);
    }

    /**
     * 获取数据
     */
    public List<Object> getData() {
        return this.data;
    }

    public void serialize(OutputStream os, List<DataField> fields) throws IOException {
        if(data == null || data.size() == 0)
            return;
        ReportSerializer.wudsWriteObject(os, data, fields);
    }
}
