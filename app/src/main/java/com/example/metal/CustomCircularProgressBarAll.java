package com.example.metal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
public class CustomCircularProgressBarAll extends View {
    private Paint paint;

    private float paidPercentage;
    private float unpaidPercentage;
    private float usersWithLoansPercentage;
    private float usersWithoutLoansPercentage;
    private float pendingPercentage;
    private float completePercentage;
    private float rejectedPercentage;
    private float pastDuePercentage;
    private float acceptedPercentage;

    public CustomCircularProgressBarAll(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public void setPaidPercentage(float percentage) {
        this.paidPercentage = percentage;
        invalidate();
    }

    public void setUnpaidPercentage(float percentage) {
        this.unpaidPercentage = percentage;
        invalidate();
    }

    public void setUsersWithLoansPercentage(float percentage) {
        this.usersWithLoansPercentage = percentage;
        invalidate();
    }

    public void setUsersWithoutLoansPercentage(float percentage) {
        this.usersWithoutLoansPercentage = percentage;
        invalidate();
    }

    public void setPendingPercentage(float percentage) {
        this.pendingPercentage = percentage;
        invalidate();
    }

    public void setCompletePercentage(float percentage) {
        this.completePercentage = percentage;
        invalidate();
    }

    public void setRejectedPercentage(float percentage) {
        this.rejectedPercentage = percentage;
        invalidate();
    }

    public void setPastDuePercentage(float percentage) {
        this.pastDuePercentage = percentage;
        invalidate();
    }

    public void setAcceptedPercentage(float percentage) {
        this.acceptedPercentage = percentage;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = Math.min(centerX, centerY) - 10;

        // Draw paid segment
        paint.setColor(Color.parseColor("#4CAF50")); // Green
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90, paidPercentage * 3.6f, true, paint);

        // Draw unpaid segment
        paint.setColor(Color.parseColor("#F44336")); // Red
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f), unpaidPercentage * 3.6f, true, paint);

        // Draw users with loans segment
        paint.setColor(Color.parseColor("#2196F3")); // Blue
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f + unpaidPercentage * 3.6f), usersWithLoansPercentage * 3.6f, true, paint);

        // Draw users without loans segment
        paint.setColor(Color.parseColor("#9E9E9E")); // Gray
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f + unpaidPercentage * 3.6f + usersWithLoansPercentage * 3.6f),
                usersWithoutLoansPercentage * 3.6f, true, paint);

        // Draw pending loans segment
        paint.setColor(Color.parseColor("#FFEB3B")); // Yellow
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f + unpaidPercentage * 3.6f + usersWithLoansPercentage * 3.6f + usersWithoutLoansPercentage * 3.6f),
                pendingPercentage * 3.6f, true, paint);

        // Draw complete loans segment
        paint.setColor(Color.parseColor("#00BCD4")); // Cyan
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f + unpaidPercentage * 3.6f + usersWithLoansPercentage * 3.6f + usersWithoutLoansPercentage * 3.6f + pendingPercentage * 3.6f),
                completePercentage * 3.6f, true, paint);

        // Draw rejected loans segment
        paint.setColor(Color.parseColor("#E91E63")); // Magenta
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f + unpaidPercentage * 3.6f + usersWithLoansPercentage * 3.6f + usersWithoutLoansPercentage * 3.6f + pendingPercentage * 3.6f + completePercentage * 3.6f),
                rejectedPercentage * 3.6f, true, paint);

        // Draw past due loans segment
        paint.setColor(Color.parseColor("#FF9800")); // Orange
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f + unpaidPercentage * 3.6f + usersWithLoansPercentage * 3.6f + usersWithoutLoansPercentage * 3.6f + pendingPercentage * 3.6f + completePercentage * 3.6f + rejectedPercentage * 3.6f),
                pastDuePercentage * 3.6f, true, paint);

        // Draw accepted loans segment
        paint.setColor(Color.parseColor("#607D8B")); // Light Gray
        canvas.drawArc(new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius),
                -90 + (paidPercentage * 3.6f + unpaidPercentage * 3.6f + usersWithLoansPercentage * 3.6f + usersWithoutLoansPercentage * 3.6f + pendingPercentage * 3.6f + completePercentage * 3.6f + rejectedPercentage * 3.6f + pastDuePercentage * 3.6f),
                acceptedPercentage * 3.6f, true, paint);
    }
}



