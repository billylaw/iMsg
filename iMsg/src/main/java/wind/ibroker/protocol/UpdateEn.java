package wind.ibroker.protocol;

import wind.ibroker.common.StoreBase;

public class UpdateEn {

    public UpdateEn(){

    }

    public UpdateEn(StoreBase sb, CalcMessage[] batchMsg, int undoID, byte action, byte tableID, int undoCount, int socketHandle, byte undoType) {
        this.sb = sb;
        this.batchMsg = batchMsg;
        this.undoID = undoID;
        this.action = action;
        this.tableID = tableID;
        this.undoCount = undoCount;
        this.socketHandle = socketHandle;
        this.undoType = undoType;
    }

    /**
     * 数据
     */
    private StoreBase sb;

    /**
     * 批量报价包 用于批量报价顺序同步
     */
    private  CalcMessage[] batchMsg;

    /**
     * undo编号
     */
    private int undoID;

    /**
     * 动作
     */
    private byte action;

    /**
     * 标签页
     */
    private byte tableID;

    /**
     * undo数量
     */
    private  int undoCount;


    private  int socketHandle;

    /**
     * undo类型 0-进入undo列表 1-不进入undo列表
     */
    private  byte undoType;

    public StoreBase getSb() {
        return sb;
    }

    public void setSb(StoreBase sb) {
        this.sb = sb;
    }

    public CalcMessage[] getBatchMsg() {
        return batchMsg;
    }

    public void setBatchMsg(CalcMessage[] batchMsg) {
        this.batchMsg = batchMsg;
    }

    public int getUndoID() {
        return undoID;
    }

    public void setUndoID(int undoID) {
        this.undoID = undoID;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public byte getTableID() {
        return tableID;
    }

    public void setTableID(byte tableID) {
        this.tableID = tableID;
    }

    public int getUndoCount() {
        return undoCount;
    }

    public void setUndoCount(int undoCount) {
        this.undoCount = undoCount;
    }

    public int getSocketHandle() {
        return socketHandle;
    }

    public void setSocketHandle(int socketHandle) {
        this.socketHandle = socketHandle;
    }

    public byte getUndoType() {
        return undoType;
    }

    public void setUndoType(byte undoType) {
        this.undoType = undoType;
    }
}
