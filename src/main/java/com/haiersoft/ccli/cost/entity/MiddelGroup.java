package com.haiersoft.ccli.cost.entity;


import com.haiersoft.ccli.cost.entity.vo.MidGroupVo;


import java.io.Serializable;
import java.util.List;

public class MiddelGroup implements Serializable {

    private boolean isMerge;

    private List<MidGroupVo> costList;

    public MiddelGroup(boolean isMerge, List<MidGroupVo> costList) {
        this.isMerge = isMerge;
        this.costList = costList;
    }

    public MiddelGroup() {
    }

    public boolean isMerge() {
        return isMerge;
    }

    public void setMerge(boolean merge) {
        isMerge = merge;
    }

    public List<MidGroupVo> getCostList() {
        return costList;
    }

    public void setCostList(List<MidGroupVo> costList) {
        this.costList = costList;
    }

    @Override
    public String toString() {
        return "MiddelGroup{" +
                "isMerge=" + isMerge +
                ", costList=" + costList +
                '}';
    }
}
