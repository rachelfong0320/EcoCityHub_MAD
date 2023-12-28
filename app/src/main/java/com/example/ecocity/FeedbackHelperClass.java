package com.example.ecocity;

public class FeedbackHelperClass {
    String category, feedback;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public FeedbackHelperClass(String feedback) {
        this.feedback = feedback;
    }
}
