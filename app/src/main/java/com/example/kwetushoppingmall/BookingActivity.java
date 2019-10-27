package com.example.kwetushoppingmall;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingActivity extends AppCompatActivity {
    @BindView(R.id.step_View)
    StepView stepView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.txt_previous)
    Button btn_previous;
    @BindView(R.id.txt_next)
    Button btn_next;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        setUpStepView();
        setColorButton();
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
    }

    private void setColorButton() {
        if (btn_next.isEnabled())
        {
            btn_next.setBackgroundResource(R.color.fbutton_color_nephritis);
        }
        else
        {
            btn_next.setBackgroundResource(R.color.white);
        }
        if (btn_previous.isEnabled())
        {
            btn_previous.setBackgroundResource(R.color.fbutton_color_nephritis);
        }
        else
        {
            btn_previous.setBackgroundResource(R.color.white);
        }
    }

    private void setUpStepView() {
        List<String> stepList=new ArrayList<>();
        stepList.add("Salon");
        stepList.add("Barber");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
}
