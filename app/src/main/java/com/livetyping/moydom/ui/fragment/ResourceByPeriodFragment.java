package com.livetyping.moydom.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.livetyping.moydom.ui.custom.SwipableTextView;
import com.livetyping.moydom.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XlebNick for MoyDom.
 */

public class ResourceByPeriodFragment extends BaseFragment implements OnChartValueSelectedListener, EnergyRepository.EnergyGraphCallback, SwipableTextView.SwipeListener {

    private static final String ARGS_PERIOD_TYPE = "period_type";
    private final static String[] MONTH = {"Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"};
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
    SwipableTextView mHeader;
    @BindView(R.id.subheader)
    TextView mSubheader;
    @BindView(R.id.header_container)
    ViewGroup mHeaderContainer;
    @BindView(R.id.header)
    ViewGroup mHeaderRoot;

    @BindView(R.id.share)
    View mShare;

    private Unbinder mUnbinder;
    private int periodType;

    private Date date;

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
        if (getArguments() != null) { periodType = getArguments().getInt(ARGS_PERIOD_TYPE); }
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        initData();

        View view = inflater.inflate(R.layout.fragment_resourse_by_period, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mSubheader.setVisibility(
                periodType == EnergySwitchModel.ENERGY_TYPE_TODAY ? View.VISIBLE : View.GONE);
        mSubheader.setText(CalendarUtils.getCurrentDate());
        switch (periodType) {
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                mHeader.setText("Сегодня");
                mSubheader.setVisibility(View.VISIBLE);
                mSubheader.setText(CalendarUtils.getCurrentDateShortText());
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                mHeader.setText(String.format(Locale.US, "%s, %d",
                        MONTH[CalendarUtils.getCurrentMonth() - 1],
                        CalendarUtils.getCurrentYear()));
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                mHeader.setText(String.format(Locale.US,
                        "%d",
                        CalendarUtils.getCurrentYear()));
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
            if (periodType == EnergySwitchModel.ENERGY_TYPE_TODAY) {
                String result = String.format(Locale.US, "%.0f:00", value);
                if (result.length() < 5) { result = "0" + result; }
                return result;
            }
            if (periodType == EnergySwitchModel.ENERGY_TYPE_WEEK) {
                return CalendarUtils.getDayOfWeek(value);
            }
            if (periodType == EnergySwitchModel.ENERGY_TYPE_YEAR) {
                return MONTH[(int) value].substring(0, 1);
            }
            return String.format(Locale.US, "%.0f", value);
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
        mChart.setRenderer(new ChartDataRenderer(mChart,
                mChart.getAnimator(),
                mChart.getViewPortHandler()));

        float leftOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                11,
                getResources().getDisplayMetrics());
        float rightOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50,
                getResources().getDisplayMetrics());

        mChart.setViewPortOffsets(leftOffset, 0, rightOffset, 0);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setMarker(new MyMarkerView(getContext(), R.layout.chart_marker));

        mHeader.setOnSwipeListener(this);

        load();
        return view;
    }

    private void initData() {
        switch (periodType){
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                Calendar calendar = Calendar.getInstance();
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                date = calendar.getTime();
                break;
            default:
                date = new Date();
                break;
        }
    }

    private void load() {

        mContent.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);

        switch (periodType) {
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                EnergyRepository.getInstance().getDayGraphEnergy(this, date);
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                EnergyRepository.getInstance().getWeekGraphEnergy(this, date);
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                EnergyRepository.getInstance().getMonthGraphEnergy(this, date);
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                EnergyRepository.getInstance().getYearGraphEnergy(this, date);
                break;

        }
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        switch (periodType) {
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

        if (indicator.getVisibility() != View.VISIBLE) { indicator.setVisibility(View.VISIBLE); }
        if (label.getVisibility() != View.VISIBLE) { label.setVisibility(View.VISIBLE); }

        if (mHeaderContainer.getVisibility() != View.GONE) {
            mHeaderContainer.setVisibility(View.GONE);
        }

        ((MyMarkerView) mChart.getMarker()).setHeight((int) (h.getYPx()));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) indicator.getLayoutParams();
        lp.width = Math.round(h.getXPx()) + 1;
        indicator.setLayoutParams(lp);
        indicator.invalidate();

        labelCost.setText(getString(R.string.rub_measure,
                mChart.getBarData()
                        .getDataSetForEntry(e)
                        .getEntryForXValue(e.getX(), 0)
                        .getPositiveSum()));
        switch (periodType) {
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                labelData.setText(String.format(Locale.getDefault(),
                        "%d %s\n%.0f-%.0f ч.",
                        CalendarUtils.getCurrentDay(),
                        CalendarUtils.getCurrentMonthShortText(),
                        e.getX(),
                        e.getX() + 1));
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                labelData.setText(String.format(Locale.getDefault(), "%.0f %s\n%d", e.getX(),
                        CalendarUtils.getCurrentMonthShortText(), CalendarUtils.getCurrentYear()));
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                labelData.setText(String.format(Locale.getDefault(),
                        "%s\n%d",
                        MONTH[(int) e.getX()],
                        CalendarUtils.getCurrentYear()));
        }
        lp = (RelativeLayout.LayoutParams) label.getLayoutParams();
        int margin = Math.round(h.getXPx()) - label.getWidth() / 2;

        int marginMin = getResources().getDimensionPixelOffset(R.dimen.padding_normal);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int marginMax = displayMetrics.widthPixels - label.getWidth() - marginMin;

        lp.leftMargin = Math.min(Math.max(margin, marginMin), marginMax);
        label.setLayoutParams(lp);
        label.invalidate();
    }

    @Override
    public void onNothingSelected() {
        if (indicator.getVisibility() != View.GONE) { indicator.setVisibility(View.GONE); }
        if (label.getVisibility() != View.GONE) { label.setVisibility(View.GONE); }
        if (mHeaderContainer.getVisibility() != View.VISIBLE) {
            mHeaderContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGraphResponse(GraphEnergyModel energy) {

        getActivity().runOnUiThread(() -> {

            if (periodType == EnergySwitchModel.ENERGY_TYPE_WEEK) {
                mHeader.setText(energy.getWeekDate());
            }
            mChart.setData(energy.getGraphData(periodType));

            if (periodType == EnergySwitchModel.ENERGY_TYPE_YEAR) {
                mChart.getXAxis().setLabelCount(mChart.getBarData().getEntryCount());
            }

            mChart.notifyDataSetChanged();
            mChart.invalidate();

            mTotalCost.setText(String.format(Locale.US,
                    getString(R.string.short_rub_measure),
                    energy.getTotalPowerCost()));
            mTotalValue.setText(String.format(Locale.US,
                    getString(R.string.short_energy_measure),
                    energy.getTotalPower()));
            mAverageCost.setText(String.format(Locale.US,
                    periodType == EnergySwitchModel.ENERGY_TYPE_YEAR ?
                            getString(R.string.short_rub_measure) :
                            getString(R.string.short_rub_measure),
                    energy.getAveragePowerCost()));
            mAverageValue.setText(String.format(Locale.US,
                    periodType == EnergySwitchModel.ENERGY_TYPE_YEAR ?
                            getString(R.string.short_energy_measure) :
                            getString(R.string.energy_measure),
                    energy.getAveragePower()));


            mProgressBar.setVisibility(View.GONE);
            mError.setVisibility(View.GONE);
            mContent.setVisibility(View.VISIBLE);

            mShare.setOnClickListener((view) -> {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        getResources().getString(R.string.share_usage));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, energy.getDataText());
                startActivity(Intent.createChooser(sharingIntent,
                        getResources().getString(R.string.share_usage)));
            });

            List<GraphEnergyModel.ZoneSummary> zoneSummaries = energy.getZones();

            for (GraphEnergyModel.ZoneSummary summary : zoneSummaries) {

                if (summary.id == GraphEnergyModel.ZONE_PEAK) {

                    mFirstZone.setVisibility(View.VISIBLE);
                    mFirstZoneTitle.setText(
                            String.format("%s%s",
                                    summary.name.substring(0, 1).toUpperCase(),
                                    summary.name.substring(1)));
                    mFirstZoneTime.setText(summary.time.replace(" ", ""));
                    mFirstZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
                            summary.totalEnergy));
                    mFirstZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
                            summary.totalEnergyCost));
                    mFirstZoneAverageValue.setText(String.format(getString(R.string.energy_measure),
                            summary.totalEnergy / summary.entriesCount));
                    mFirstZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
                            summary.totalEnergyCost / summary.entriesCount));
                } else if (summary.id == GraphEnergyModel.ZONE_NIGHT) {

                    mSecondZone.setVisibility(View.VISIBLE);
                    mSecondZoneTitle.setText(
                            String.format("%s%s",
                                    summary.name.substring(0, 1).toUpperCase(),
                                    summary.name.substring(1)));
                    mSecondZoneTime.setText(summary.time.replace(" ", ""));
                    mSecondZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
                            summary.totalEnergy));
                    mSecondZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
                            summary.totalEnergyCost));
                    mSecondZoneAverageValue.setText(String.format(getString(R.string.energy_measure),
                            summary.totalEnergy / summary.entriesCount));
                    mSecondZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
                            summary.totalEnergyCost / summary.entriesCount));
                } else if (summary.id == GraphEnergyModel.ZONE_SEMIPEAK) {

                    mThirdZone.setVisibility(View.VISIBLE);
                    mThirdZoneTitle.setText(
                            String.format("%s%s",
                                    summary.name.substring(0, 1).toUpperCase(),
                                    summary.name.substring(1)));
                    mThirdZoneTime.setText(summary.time.replace(" ", ""));
                    mThirdZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
                            summary.totalEnergy));
                    mThirdZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
                            summary.totalEnergyCost));
                    mThirdZoneAverageValue.setText(String.format(getString(R.string.energy_measure),
                            summary.totalEnergy / summary.entriesCount));
                    mThirdZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
                            summary.totalEnergyCost / summary.entriesCount));
                }
            }
        });

    }

    @Override
    public void onError(int code, String message) {

        mProgressBar.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.GONE);
        mError.setText("Произошла ошибка: " + message);
    }

    @Override
    public void onSwipe(boolean isSwipeOnLeft) {

        int value = isSwipeOnLeft ? -1 : 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (periodType){
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                calendar.add(Calendar.DAY_OF_YEAR, value);
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                calendar.add(Calendar.DAY_OF_YEAR, 7 * value);
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                calendar.add(Calendar.DAY_OF_MONTH, value);
                break;
            default:
                calendar.add(Calendar.YEAR, value);
        }

        date = calendar.getTime();
        load();
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        View view;
        MyGestureDetector(View view){
            this.view = view;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                view.setTranslationX(e2.getX());
//                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                    return false;
//                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

    }
}
