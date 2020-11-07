package com.example.getsetgoal;

import java.io.Serializable;

public class MilestoneModel implements Serializable {

    int milestoneNumber;
    int milstonedays;
    int milestone_iscomplete;
    String milestoneText,milestoneStartdate,milestoneEnddate;

    boolean isPlayed=false;

    public boolean isPlayed() {
        return isPlayed;
    }
    public void setPlayed(boolean played) {
        isPlayed = played;
    }


    public MilestoneModel(int milestoneNumber, int milstonedays, String milestoneText, String milestoneStartdate, String milestoneEnddate,int milestone_iscomplete) {
        this.milestoneNumber = milestoneNumber;
        this.milstonedays = milstonedays;
        this.milestoneText = milestoneText;
        this.milestoneStartdate = milestoneStartdate;
        this.milestoneEnddate = milestoneEnddate;
        this.milestone_iscomplete=milestone_iscomplete;
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

    public int getMilestone_iscomplete() {
        return milestone_iscomplete;
    }

    public void setMilestone_iscomplete(int milestone_iscomplete) {
        this.milestone_iscomplete = milestone_iscomplete;
    }
}
