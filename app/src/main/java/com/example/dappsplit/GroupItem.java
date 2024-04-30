package com.example.dappsplit;

public class GroupItem {

    private String groupName;
    private String amountSpiltted;
    private String amountSettled;
    private String amountPayable;
    private String recepeint;
    private String grpKey;

    public GroupItem(String groupName, String amountSpiltted, String amountSettled, String amountPayable,String recepeint,String grpKey) {
        this.groupName = groupName;
        this.amountSpiltted = amountSpiltted;
        this.amountSettled = amountSettled;
        this.amountPayable = amountPayable;
        this.recepeint = recepeint;
        this.grpKey = grpKey;
    }

    public String getGroupName() {
        return groupName;
    }
    public String getGrpKey() {
        return grpKey;
    }
    public String getrecepeint() {
        return recepeint;
    }
    public String getAmountSpiltted() {
        return amountSpiltted;
    }

    public String getAmountSettled() {
        return amountSettled;
    }

    public String getAmountPayable() {
        return amountPayable;
    }
}

