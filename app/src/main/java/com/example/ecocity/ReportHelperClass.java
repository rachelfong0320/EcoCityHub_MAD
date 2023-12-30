package com.example.ecocity;

public class ReportHelperClass {
    String problem;
    String des;

    public ReportHelperClass(String des, String problem) {
        this.des = des;
        this.problem=problem;
    }


    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
