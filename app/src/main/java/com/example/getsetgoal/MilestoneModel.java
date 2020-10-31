package com.example.getsetgoal;

import java.io.Serializable;

public class MilestoneModel implements Serializable {

    int milestoneNumber,milstonedays;
    String milestoneText,milestoneStartdate,milestoneEnddate;

    public MilestoneModel(int milestoneNumber, int milstonedays, String milestoneText, String milestoneStartdate, String milestoneEnddate) {
        this.milestoneNumber = milestoneNumber;
        this.milstonedays = milstonedays;
        this.milestoneText = milestoneText;
        this.milestoneStartdate = milestoneStartdate;
        this.milestoneEnddate = milestoneEnddate;
    }

    public MilestoneModel(int milstonedays, String milestoneText) {
        this.milstonedays = milstonedays;
        this.milestoneText = milestoneText;
    }

    public int getMilestoneNumber() {
        return milestoneNumber;
    }

    public void setMilestoneNumber(int milestoneNumber) {
        this.milestoneNumber = milestoneNumber;
    }

    public int getMilstonedays() {
        return milstonedays;
    }

    public void setMilstonedays(int milstonedays) {
        this.milstonedays = milstonedays;
    }

    public String getMilestoneText() {
        return milestoneText;
    }

    public void setMilestoneText(String milestoneText) {
        this.milestoneText = milestoneText;
    }

    public String getMilestoneStartdate() {
        return milestoneStartdate;
    }

    public void setMilestoneStartdate(String milestoneStartdate) {
        this.milestoneStartdate = milestoneStartdate;
    }

    public String getMilestoneEnddate() {
        return milestoneEnddate;
    }

    public void setMilestoneEnddate(String milestoneEnddate) {
        this.milestoneEnddate = milestoneEnddate;
    }
}
