package edu.umd.cs.funanical.Objects;

/**
 * Created by apple on 4/22/18.
 */

public class Category {
    private String name;
    private double weeklyBudget, monthlyBudget, yearlyBudget, lastWeeklyBudget, nextWeeklyBudget;
    private double weeklySpending, monthlySpending, yearlySpending, lastWeeklySpending,
            nextWeeklySpending;
    private int buildingId, indicatorId;

    public Category(){

    }

    public Category(String name, double weeklyBudget, double lastWeeklyBudget,
                    double nextWeeklyBudget, double monthlyBudget, double yearlyBudget,
                    double weeklySpending, double lastWeeklySpending, double nextWeeklySpending,
                    double monthlySpending, double yearlySpending, int buildingId, int indicatorId){
        this.name = name;
        this.lastWeeklyBudget = lastWeeklyBudget;
        this.lastWeeklySpending = lastWeeklySpending;
        this.nextWeeklyBudget = nextWeeklyBudget;
        this.nextWeeklySpending = nextWeeklySpending;
        this.weeklyBudget = weeklyBudget;
        this.monthlyBudget = monthlyBudget;
        this.yearlyBudget = yearlyBudget;
        this.weeklySpending = weeklySpending;
        this.monthlySpending = monthlySpending;
        this.yearlySpending = yearlySpending;
        this.buildingId = buildingId;
        this.indicatorId = indicatorId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setWeeklySpending(double weeklySpending){
        this.weeklySpending = weeklySpending;
    }

    public double getWeeklySpending(){
        return weeklySpending;
    }

    public void setMontlySpending(double monthlySpending){
        this.monthlySpending = monthlySpending;
    }

    public double getMontlySpending(){
        return monthlySpending;
    }

    public void setYearlySpending(double yearlySpending){
        this.yearlySpending = yearlySpending;
    }

    public double getYearlySpending(){
        return yearlySpending;
    }

    public void setWeeklyBudget(double weeklyBudget){
        this.weeklyBudget = weeklyBudget;
    }

    public double getLastWeeklyBudget(){
        return lastWeeklyBudget;
    }

    public void setLastWeeklyBudget(double lastWeeklyBudget){
        this.lastWeeklyBudget = lastWeeklyBudget;
    }

    public double getNextweeklyBudget(){
        return nextWeeklyBudget;
    }

    public void setNextWeeklyBudget(double nextWeeklyBudget){
        this.nextWeeklyBudget = nextWeeklyBudget;
    }

    public double getLastWeeklySpending(){
        return lastWeeklySpending;
    }

    public void setLastWeeklySpending(double lastWeeklySpending){
        this.lastWeeklyBudget = lastWeeklyBudget;
    }

    public double getNextWeeklySpending(){
        return nextWeeklySpending;
    }

    public void setNextWeeklySpending(double nextWeeklySpending){
        this.nextWeeklySpending = nextWeeklySpending;
    }

    public double getWeeklyBudget(){
        return weeklyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget){
        this.monthlyBudget = monthlyBudget;
    }

    public double getMontlyBudget(){
        return monthlyBudget;
    }

    public void setYearlyBudget(double yearlyBudget){
        this.yearlyBudget = yearlyBudget;
    }

    public double getYearlyBudget(){
        return yearlyBudget;
    }

    public void setBuildingId(int buildingId){
        this.buildingId = buildingId;
    }

    public int getBuildingId(){
        return buildingId;
    }

    public void setIndicatorId(int indicatorId){
        this.indicatorId = indicatorId;
    }

    public int getIndicatorId(){
        return indicatorId;
    }

}
