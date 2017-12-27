package com.livetyping.moydom.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;
import com.livetyping.moydom.ui.custom.ChartDataRenderer;
import com.livetyping.moydom.ui.custom.MyMarkerView;
import com.livetyping.moydom.utils.CalendarUtils;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XlebNick for MoyDom.
 */

public class ResourceByPeriodFragment extends BaseFragment implements OnChartValueSelectedListener, EnergyRepository.EnergyGraphCallback {

    private static final String ARGS_PERIOD_TYPE = "period_type";
    private final static String[] MONTH = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
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
    @BindView(R.id.first_zone_container)
    ViewGroup mFirstZone;
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
    @BindView(R.id.second_zone_container)
    ViewGroup mSecondZone;
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
    @BindView(R.id.third_zone_container)
    ViewGroup mThirdZone;
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
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.error)
    TextView mError;
    @BindView(R.id.content)
    View mContent;
    @BindView(R.id.header_type)
    TextView mHeader;
    @BindView(R.id.subheader)
    TextView mSubheader;
    @BindView(R.id.header_title)
    View mHeaderContainer;
    @BindView(R.id.share)
    View mShare;
    private Unbinder mUnbinder;
    private int periodType;
    private Date selectedPeriodStart;

    public static ResourceByPeriodFragment getInstance(int periodType) {
        ResourceByPeriodFragment fragment = new ResourceByPeriodFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PERIOD_TYPE, periodType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            periodType = getArguments().getInt(ARGS_PERIOD_TYPE);
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

        mSubheader.setVisibility(periodType == EnergySwitchModel.ENERGY_TYPE_TODAY ? View.VISIBLE : View.GONE);
        mSubheader.setText(CalendarUtils.getCurrentDate());
        switch (periodType){
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                mHeader.setText("Сегодня");
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                mHeader.setText(CalendarUtils.getCurrentMonthText());
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                mHeader.setText(CalendarUtils.getCurrentYear() + "");
                break;
        }
        mChart.getDescription().setEnabled(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        mChart.getXAxis().setGridLineWidth(1);
        mChart.getXAxis().setGridColor(Color.parseColor("#eeeeee"));
        mChart.getXAxis().setDrawLimitLinesBehindData(true);
        mChart.getXAxis().setTextSize(14);
        mChart.getXAxis().setTextColor(Color.parseColor("#808080"));
        mChart.getXAxis().setValueFormatter((value, axis) -> {
            if (periodType == EnergySwitchModel.ENERGY_TYPE_TODAY){
                String result = String.format(Locale.getDefault(), "%.0f:00", value);
                if (result.length() < 5)
                    result = "0" + result;
                return result;
            }
            if (periodType == EnergySwitchModel.ENERGY_TYPE_WEEK){
                return CalendarUtils.getDayOfWeek(value);
            }
            if (periodType == EnergySwitchModel.ENERGY_TYPE_YEAR){
                return MONTH[(int) value];
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
                String.format(getString(R.string.short_rub_measure), value));
        mChart.getLegend().setEnabled(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setMaxVisibleValueCount(15);
        mChart.setDrawBorders(false);
        mChart.setDrawGridBackground(true);
        mChart.setGridBackgroundColor(Color.parseColor("#f5f5f5"));
        mChart.setBackgroundColor(Color.parseColor("#f5f5f5"));
        mChart.setHighlightFullBarEnabled(true);

        mChart.setRenderer(new ChartDataRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler()));

        float leftOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
        float rightOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        mChart.setViewPortOffsets(leftOffset, 0, rightOffset, 0);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setMarker(new MyMarkerView(getContext(), R.layout.chart_marker));

        switch (periodType){
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                EnergyRepository.getInstance().getDayGraphEnergy(this);
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                EnergyRepository.getInstance().getWeekGraphEnergy(this);
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                EnergyRepository.getInstance().getMonthGraphEnergy(this);
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                EnergyRepository.getInstance().getYearGraphEnergy(this);
                break;

        }
        return view;
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        switch (periodType){
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                EnergyRepository.getInstance().removeDayGraphEnergy();
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                EnergyRepository.getInstance().removeWeekGraphEnergy();
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                EnergyRepository.getInstance().removeMonthGraphEnergy();
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                EnergyRepository.getInstance().removeYearGraphEnergy();
                break;

        }
        super.onDestroyView();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        h = mChart.getHighlightByTouchPoint(h.getXPx(), 0);

        if (indicator.getVisibility() != View.VISIBLE)
            indicator.setVisibility(View.VISIBLE);
        if (label.getVisibility() != View.VISIBLE)
            label.setVisibility(View.VISIBLE);

        if (mHeaderContainer.getVisibility() != View.GONE)
            mHeaderContainer.setVisibility(View.GONE);

        ((MyMarkerView) mChart.getMarker()).setHeight((int) (h.getYPx()));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) indicator.getLayoutParams();
        lp.width = Math.round(h.getXPx()) + 1;
        indicator.setLayoutParams(lp);
        indicator.invalidate();

        labelCost.setText(getString(R.string.rub_measure, mChart.getBarData().getDataSetForEntry(e).getEntryForXValue(e.getX(), 0).getPositiveSum()));
        switch (periodType){
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                labelData.setText(String.format(Locale.getDefault(), "%d dec\n%.0f-%.0f", CalendarUtils.getCurrentDay(), e.getX(), e.getX() + 1));
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                labelData.setText(String.format(Locale.getDefault(), "%.0f dec\n2017", e.getX()));
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                labelData.setText(MONTH[(int) e.getX()] + "\n2017" );


        }
        lp = (RelativeLayout.LayoutParams) label.getLayoutParams();
        int margin = Math.round(h.getXPx()) - label.getWidth() / 2;

        int marginMin = getResources().getDimensionPixelOffset(R.dimen.padding_normal);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int marginMax = displayMetrics.widthPixels - label.getWidth() - marginMin;

        lp.leftMargin = Math.min(Math.max(margin, marginMin), marginMax) ;
        label.setLayoutParams(lp);
        label.invalidate();

    }

    @Override
    public void onNothingSelected() {
        if (indicator.getVisibility() != View.GONE)
            indicator.setVisibility(View.GONE);
        if (label.getVisibility() != View.GONE)
            label.setVisibility(View.GONE);
        if (mHeaderContainer.getVisibility() != View.VISIBLE)
            mHeaderContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGraphResponse(GraphEnergyModel energy) {

        if (periodType == EnergySwitchModel.ENERGY_TYPE_WEEK){
            mHeader.setText(energy.getWeekDate());
        }
        mChart.setData(energy.getGraphData(periodType));

        if (periodType == EnergySwitchModel.ENERGY_TYPE_YEAR){
            mChart.getXAxis().setLabelCount(mChart.getBarData().getEntryCount());
        }

        mChart.notifyDataSetChanged();
        mChart.invalidate();

        mTotalCost.setText(String.format(Locale.getDefault(), getString(R.string.short_rub_measure), energy.getTotalPowerCost()));
        mTotalValue.setText(String.format(Locale.getDefault(), getString(R.string.short_energy_measure), energy.getTotalPower()));
        mAverageCost.setText(String.format(Locale.getDefault(), periodType == EnergySwitchModel.ENERGY_TYPE_YEAR ?
                getString(R.string.short_rub_measure) : getString(R.string.rub_measure), energy.getAveragePowerCost()));
        mAverageValue.setText(String.format(Locale.getDefault(), periodType == EnergySwitchModel.ENERGY_TYPE_YEAR ?
                getString(R.string.short_energy_measure) : getString(R.string.energy_measure), energy.getAveragePower()));

        getActivity().runOnUiThread(() -> {
            mProgressBar.setVisibility(View.GONE);
            mError.setVisibility(View.GONE);
            mContent.setVisibility(View.VISIBLE);
        });

        mShare.setOnClickListener((view) -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_usage));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, energy.getDataText());
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_usage)));
        });

        List<GraphEnergyModel.ZoneSummary> zoneSummaries = energy.getZones();
        if (zoneSummaries.size() == 0)
            return;
        mFirstZone.setVisibility(View.VISIBLE);
        mFirstZoneTitle.setText(zoneSummaries.get(0).name.substring(0, 1).toUpperCase() + zoneSummaries.get(0).name.substring(1));
        mFirstZoneTime.setText(zoneSummaries.get(0).time);
        mFirstZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure), zoneSummaries.get(0).totalEnergy));
        mFirstZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure), zoneSummaries.get(0).totalEnergyCost));
        mFirstZoneAverageValue.setText(String.format(getString(R.string.energy_measure), zoneSummaries.get(0).totalEnergy / zoneSummaries.get(0).entriesCount));
        mFirstZoneAverageCost.setText(String.format(getString(R.string.rub_measure), zoneSummaries.get(0).totalEnergyCost / zoneSummaries.get(0).entriesCount));
        if (zoneSummaries.size() == 1)
            return;
        mSecondZone.setVisibility(View.VISIBLE);
        mSecondZoneTitle.setText(zoneSummaries.get(1).name.substring(0, 1).toUpperCase() + zoneSummaries.get(1).name.substring(1));
        mSecondZoneTime.setText(zoneSummaries.get(1).time);
        mSecondZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure), zoneSummaries.get(1).totalEnergy));
        mSecondZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure), zoneSummaries.get(1).totalEnergyCost));
        mSecondZoneAverageValue.setText(String.format(getString(R.string.energy_measure), zoneSummaries.get(1).totalEnergy / zoneSummaries.get(1).entriesCount));
        mSecondZoneAverageCost.setText(String.format(getString(R.string.rub_measure), zoneSummaries.get(1).totalEnergyCost / zoneSummaries.get(1).entriesCount));
        if (zoneSummaries.size() == 2)
            return;
        mThirdZone.setVisibility(View.VISIBLE);
        mThirdZoneTitle.setText(zoneSummaries.get(2).name.substring(0, 1).toUpperCase() + zoneSummaries.get(2).name.substring(1));
        mThirdZoneTime.setText(zoneSummaries.get(2).time);
        mThirdZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure), zoneSummaries.get(2).totalEnergy));
        mThirdZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure), zoneSummaries.get(2).totalEnergyCost));
        mThirdZoneAverageValue.setText(String.format(getString(R.string.energy_measure), zoneSummaries.get(2).totalEnergy / zoneSummaries.get(2).entriesCount));
        mThirdZoneAverageCost.setText(String.format(getString(R.string.rub_measure), zoneSummaries.get(2).totalEnergyCost / zoneSummaries.get(2).entriesCount));

    }

    @Override
    public void onError(String message) {

        mProgressBar.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.GONE);
        mError.setText("Произошла ошибка: " + message);
    }


}
