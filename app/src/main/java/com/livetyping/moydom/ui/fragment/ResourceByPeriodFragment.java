package com.livetyping.moydom.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.energy.model.GraphEnergyModel;
import com.livetyping.moydom.data.repository.EnergyRepository;
import com.livetyping.moydom.ui.MyMarkerView;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XlebNick for MoyDom.
 */

public class ResourceByPeriodFragment extends BaseFragment implements OnChartValueSelectedListener {

    private static final String ARGS_PERIOD_TYPE = "period_type";

    @BindView(R.id.chart)
    BarChart mChart;

    @BindView(R.id.average_cost)
    TextView mAverageCost;
    @BindView(R.id.average_value)
    TextView mAverageValue;
    @BindView(R.id.total_cost)
    TextView mTotalCost;
    @BindView(R.id.total_value)
    TextView mTotalValue;
    @BindView(R.id.first_zone_title)
    TextView mFirstZoneTitle;
    @BindView(R.id.first_zone_time)
    TextView mFirstZoneTime;
    @BindView(R.id.first_zone_average_cost)
    TextView mFirstZoneAverageCost;
    @BindView(R.id.first_zone_average_value)
    TextView mFirstZoneAverageValue;
    @BindView(R.id.first_zone_total_cost)
    TextView mFirstZoneTotalCost;
    @BindView(R.id.first_zone_total_value)
    TextView mFirstZoneTotalValue;
    @BindView(R.id.second_zone_title)
    TextView mSecondZoneTitle;
    @BindView(R.id.second_zone_time)
    TextView mSecondZoneTime;
    @BindView(R.id.second_zone_average_cost)
    TextView mSecondZoneAverageCost;
    @BindView(R.id.second_zone_average_value)
    TextView mSecondZoneAverageValue;
    @BindView(R.id.second_zone_total_cost)
    TextView mSecondZoneTotalCost;
    @BindView(R.id.second_zone_total_value)
    TextView mSecondZoneTotalValue;
    @BindView(R.id.third_zone_title)
    TextView mThirdZoneTitle;
    @BindView(R.id.third_zone_time)
    TextView mThirdZoneTime;
    @BindView(R.id.third_zone_average_cost)
    TextView mThirdZoneAverageCost;
    @BindView(R.id.third_zone_average_value)
    TextView mThirdZoneAverageValue;
    @BindView(R.id.third_zone_total_cost)
    TextView mThirdZoneTotalCost;
    @BindView(R.id.third_zone_total_value)
    TextView mThirdZoneTotalValue;

    @BindView(R.id.indicator)
    LinearLayout indicator;
    @BindView(R.id.resources_graph_label_background)
    LinearLayout label;
    @BindView(R.id.label_cost)
    TextView labelCost;
    @BindView(R.id.label_data)
    TextView labelData;


    private Unbinder mUnbinder;

    public static ResourceByPeriodFragment getInstance(int periodType) {
        ResourceByPeriodFragment fragment = new ResourceByPeriodFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PERIOD_TYPE, periodType);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resourse_by_period, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mChart.getDescription().setEnabled(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        mChart.getXAxis().setGridLineWidth(1);
        mChart.getXAxis().setGridColor(Color.parseColor("#eeeeee"));
        mChart.getXAxis().setDrawLimitLinesBehindData(true);
        mChart.getXAxis().setTextSize(14);
        mChart.getXAxis().setTextColor(Color.parseColor("#808080"));
        mChart.getXAxis().setValueFormatter((value, axis) -> {
            if (value == 1){
                return String.format(Locale.getDefault(), "%.0f дек", value);
            }
            return String.format(Locale.getDefault(), "%.0f", value);
        });
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setTextColor(Color.parseColor("#808080"));
        mChart.getAxisRight().setGridLineWidth(1);
        mChart.getAxisRight().setTextSize(12);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setGridColor(Color.parseColor("#eeeeee"));
        mChart.getAxisRight().setValueFormatter((value, axis) ->
                String.format(getString(R.string.rub_measure), value));

        mChart.getLegend().setEnabled(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setMaxVisibleValueCount(15);
        mChart.setDrawBorders(false);
        mChart.setDrawGridBackground(true);
        mChart.setGridBackgroundColor(Color.parseColor("#f5f5f5"));
        mChart.setBackgroundColor(Color.parseColor("#f5f5f5"));
        mChart.setHighlightPerTapEnabled(true);
        mChart.setHighlightPerDragEnabled(true);

        float leftOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
        float rightOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        mChart.setViewPortOffsets(leftOffset, 0, rightOffset, 0);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
//        mChart.getAxisRight().setAxisMinimum(0);

        mChart.setMarker(new MyMarkerView(getContext(), R.layout.chart_marker));

        EnergyRepository.getInstance().getMonthGraphEnergy(new EnergyRepository.MonthEnergyGraphCallback() {

            @Override
            public void onMonthGraphResponse(GraphEnergyModel energy) {
                getActivity().runOnUiThread(()-> {
                    mChart.setData(energy.getGraphData(EnergySwitchModel.ENERGY_TYPE_THIS_MONTH));
                    mChart.notifyDataSetChanged();
                    mChart.invalidate();
                    mTotalCost.setText(String.format(Locale.getDefault(), getString(R.string.rub_measure), energy.getWeekPowerCost()));
                    mTotalValue.setText(String.format(Locale.getDefault(), "%.0f кВт•ч", energy.getWeekPower()));



                });


            }

            @Override
            public void onError(String message) {

                Log.d("***", message);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        getActivity().runOnUiThread(() -> {

            if (indicator.getVisibility() != View.VISIBLE)
                indicator.setVisibility(View.VISIBLE);
            if (label.getVisibility() != View.VISIBLE)
                label.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) indicator.getLayoutParams();
            lp.width = Math.round(h.getXPx()) + 1;
            indicator.setLayoutParams(lp);
            indicator.invalidate();

            labelCost.setText(getString(R.string.rub_measure, mChart.getBarData().getDataSetForEntry(e).getEntryForXValue(e.getX(), 0).getPositiveSum()));
            labelData.setText(String.format(Locale.getDefault(), "%.0f dec\n2017", e.getX()));
            lp = (RelativeLayout.LayoutParams) label.getLayoutParams();
            int margin = Math.round(h.getXPx()) - label.getWidth() / 2;
            
            int marginMin = getResources().getDimensionPixelOffset(R.dimen.padding_normal);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int marginMax = displayMetrics.widthPixels - label.getWidth() - marginMin;
        
            lp.leftMargin = Math.min(Math.max(margin, marginMin), marginMax) ;
            label.setLayoutParams(lp);
            label.invalidate();


        });

        Log.d("***", h.getX() + " " + h.getDrawX() + " " + h.getXPx());
    }

    @Override
    public void onNothingSelected() {


        if (indicator.getVisibility() != View.GONE)
            indicator.setVisibility(View.GONE);
        if (label.getVisibility() != View.GONE)
            label.setVisibility(View.GONE);
    }
}
