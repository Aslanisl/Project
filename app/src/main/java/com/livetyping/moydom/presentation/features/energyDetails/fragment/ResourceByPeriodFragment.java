package com.livetyping.moydom.presentation.features.energyDetails.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.energy.model.GraphEnergyModel;
import com.livetyping.moydom.data.repository.EnergyRepository;
import com.livetyping.moydom.presentation.base.custom.ChartDataRenderer;
import com.livetyping.moydom.presentation.base.custom.MyMarkerView;
import com.livetyping.moydom.presentation.base.custom.SwipeableTextView;
import com.livetyping.moydom.presentation.base.custom.ZoneDetailsView;
import com.livetyping.moydom.presentation.features.base.fragment.BaseFragment;
import com.livetyping.moydom.presentation.features.myHomeSettings.model.EnergySwitchModel;
import com.livetyping.moydom.presentation.utils.CalendarUtils;
import com.livetyping.moydom.presentation.utils.GlideApp;

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

public class ResourceByPeriodFragment extends BaseFragment implements OnChartValueSelectedListener, EnergyRepository.EnergyGraphCallback, SwipeableTextView.SwipeListener {

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
    private static final int REQUEST_CODE_MY_PICK = 666;
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
    @BindView(R.id.indicator)
    LinearLayout indicator;
    @BindView(R.id.resources_graph_label_background)
    LinearLayout label;
    @BindView(R.id.label_cost)
    TextView labelCost;
    @BindView(R.id.label_data)
    TextView labelData;
    @BindView(R.id.progress_view)
    ImageView mProgressView;
    @BindView(R.id.error)
    TextView mError;
    @BindView(R.id.content)
    View mContent;
    @BindView(R.id.header_type)
    SwipeableTextView mHeader;
    @BindView(R.id.subheader)
    TextView mSubheader;
    @BindView(R.id.header_container)
    ViewGroup mHeaderContainer;
    @BindView(R.id.header)
    ViewGroup mHeaderRoot;

    @BindView(R.id.statistics_container)
    ViewGroup mStatisticsContainer;
    @BindView(R.id.statistics_zones_container)
    ViewGroup mStatisticsZonesContainer;

    @BindView(R.id.nothing_found)
    ViewGroup mNothingFound;

    @BindView(R.id.share)
    View mShare;

    private Unbinder mUnbinder;
    private int periodType;

    private Date date;
    private boolean wasSharePressed = false;

    public static ResourceByPeriodFragment getInstance(int periodType) {
        ResourceByPeriodFragment fragment = new ResourceByPeriodFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_PERIOD_TYPE, periodType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        removeEndpointCallbacks();
        super.onDestroyView();
    }

    private void removeEndpointCallbacks() {
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
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        h = mChart.getHighlightByTouchPoint(h.getXPx(), 0);

        if (indicator.getVisibility() != View.VISIBLE) { indicator.setVisibility(View.VISIBLE); }
        if (label.getVisibility() != View.VISIBLE) { label.setVisibility(View.VISIBLE); }

        if (mHeaderContainer.getVisibility() != View.GONE) {
            mHeaderContainer.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) indicator.getLayoutParams();
        lp.leftMargin = Math.round(h.getXPx()) - 1;
        indicator.setLayoutParams(lp);
        indicator.invalidate();
        ((MyMarkerView) mChart.getMarker()).setHeight((int) (h.getYPx()));

        labelCost.setText(String.format(Locale.US, getString(R.string.rub_measure),
                mChart.getBarData()
                        .getDataSetForEntry(e)
                        .getEntryForXValue(e.getX(), 0)
                        .getPositiveSum()));
        switch (periodType) {
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                labelData.setText(String.format(Locale.getDefault(),
                        "%d %s\n%.0f-%.0f ч.",
                        CalendarUtils.getDayFromDate(date),
                        CalendarUtils.getMonthShortTextFromDate(date),
                        e.getX(),
                        e.getX() + 1));
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, (int) e.getX() - 6);

                labelData.setText(String.format(Locale.getDefault(),
                        "%d %s\n%d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        CalendarUtils.getMonthShortTextFromDate(calendar.getTime()),
                        CalendarUtils.getYearFromDate(calendar.getTime())));
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:

                labelData.setText(String.format(Locale.getDefault(),
                        "%.0f %s\n%d",
                        e.getX(),
                        CalendarUtils.getMonthShortTextFromDate(date),
                        CalendarUtils.getYearFromDate(date)));
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                labelData.setText(String.format(Locale.getDefault(),
                        "%s\n%d",
                        MONTH[(int) e.getX()],
                        CalendarUtils.getYearFromDate(date)));
        }
        lp = (RelativeLayout.LayoutParams) label.getLayoutParams();
        int margin = Math.round(h.getXPx()) - label.getWidth() / 2;

        int marginMin = getResources().getDimensionPixelOffset(R.dimen.padding_normal);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        label.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginMax = displayMetrics.widthPixels - label.getMeasuredWidth() - marginMin;

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
        mNothingFound.setVisibility(View.GONE);
        mStatisticsContainer.setVisibility(View.VISIBLE);

        getActivity().runOnUiThread(() -> {

            BarData barData = energy.getGraphData(periodType);

            if (barData.getEntryCount() == 0) {
                showNothingFound();
                mChart.setData(null);
                mChart.setNoDataText(getString(R.string.no_data));
                mChart.notifyDataSetChanged();
                mChart.invalidate();
                return;
            }
            mChart.setData(barData);
            mChart.notifyDataSetChanged();
            mChart.invalidate();

            if (periodType == EnergySwitchModel.ENERGY_TYPE_YEAR) {
                mChart.getXAxis().setLabelCount(mChart.getBarData().getEntryCount());
            }
            mTotalCost.setText(String.format(Locale.US,
                    getString(R.string.short_rub_measure),
                    energy.getTotalPowerCost()));
            mTotalValue.setText(String.format(Locale.US,
                    getString(R.string.short_energy_measure),
                    energy.getTotalPower()));
            mAverageCost.setText(String.format(Locale.US,
                    getString(R.string.short_rub_measure),
                    ((float) Math.round(energy.getAveragePowerCost(periodType)))));
            mAverageValue.setText(String.format(Locale.US,
                    periodType == EnergySwitchModel.ENERGY_TYPE_YEAR ?
                            getString(R.string.short_energy_measure) :
                            getString(R.string.energy_measure),
                    energy.getAveragePower(periodType)));

            mProgressView.setVisibility(View.GONE);
            mError.setVisibility(View.GONE);
            mContent.setVisibility(View.VISIBLE);

            mShare.setOnClickListener((view) -> {

                if (wasSharePressed) { return; }
                wasSharePressed = true;

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        getResources().getString(R.string.share_usage));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, energy.getDataText());

                Intent intentPick = new Intent();
                intentPick.setAction(Intent.ACTION_PICK_ACTIVITY);
                intentPick.putExtra(Intent.EXTRA_TITLE, "Launch using");
                intentPick.putExtra(Intent.EXTRA_INTENT, sharingIntent);
                this.startActivityForResult(Intent.createChooser(sharingIntent,
                        "Поделиться статистикой"), REQUEST_CODE_MY_PICK);

            });

            mStatisticsZonesContainer.removeAllViews();

            List<GraphEnergyModel.ZoneSummary> zoneSummaries = energy.getZones();

            for (int i = 0; i < zoneSummaries.size(); i++) {
                GraphEnergyModel.ZoneSummary summary = zoneSummaries.get(i);
                ZoneDetailsView zoneDetailsView = new ZoneDetailsView(getContext(), summary);
                LinearLayout.LayoutParams lp =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                zoneDetailsView.setLayoutParams(lp);
                mStatisticsZonesContainer.addView(zoneDetailsView);
//
//                if (i == 0) {
//
//                    mFirstZone.setVisibility(View.VISIBLE);
//                    mFirstZoneTitle.setText(
//                            String.format("%s%s",
//                                    summary.name.substring(0, 1).toUpperCase(),
//                                    summary.name.substring(1)));
//                    mFirstZoneTime.setText(summary.time);
//                    mFirstZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
//                            summary.totalEnergy));
//                    mFirstZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost));
//                    mFirstZoneAverageValue.setText(String.format(Locale.US,
//                            getString(R.string.energy_measure),
//                            summary.totalEnergy / summary.entriesCount));
//                    mFirstZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost / summary.entriesCount));
//                } else if (i == 1) {
//
//                    mSecondZone.setVisibility(View.VISIBLE);
//                    mSecondZoneTitle.setText(
//                            String.format("%s%s",
//                                    summary.name.substring(0, 1).toUpperCase(),
//                                    summary.name.substring(1)));
//                    mSecondZoneTime.setText(summary.time);
//                    mSecondZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
//                            summary.totalEnergy));
//                    mSecondZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost));
//                    mSecondZoneAverageValue.setText(String.format(Locale.US,
//                            getString(R.string.energy_measure),
//                            summary.totalEnergy / summary.entriesCount));
//                    mSecondZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost / summary.entriesCount));
//                } else if (i == 2) {
//
//                    mThirdZone.setVisibility(View.VISIBLE);
//                    mThirdZoneTitle.setText(
//                            String.format("%s%s",
//                                    summary.name.substring(0, 1).toUpperCase(),
//                                    summary.name.substring(1)));
//                    mThirdZoneTime.setText(summary.time);
//                    mThirdZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
//                            summary.totalEnergy));
//                    mThirdZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost));
//                    mThirdZoneAverageValue.setText(String.format(Locale.US,
//                            getString(R.string.energy_measure),
//                            summary.totalEnergy / summary.entriesCount));
//                    mThirdZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost / summary.entriesCount));
//
//                }
            }

//            for (GraphEnergyModel.ZoneSummary summary : zoneSummaries) {
//
//                if (summary.id == GraphEnergyModel.ZONE_PEAK) {
//
//                    mFirstZone.setVisibility(View.VISIBLE);
//                    mFirstZoneTitle.setText(
//                            String.format("%s%s",
//                                    summary.name.substring(0, 1).toUpperCase(),
//                                    summary.name.substring(1)));
//                    mFirstZoneTime.setText(summary.time);
//                    mFirstZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
//                            summary.totalEnergy));
//                    mFirstZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost));
//                    mFirstZoneAverageValue.setText(String.format(Locale.US,
//                            getString(R.string.energy_measure),
//                            summary.totalEnergy / summary.entriesCount));
//                    mFirstZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost / summary.entriesCount));
//                } else if (summary.id == GraphEnergyModel.ZONE_NIGHT) {
//
//                    mSecondZone.setVisibility(View.VISIBLE);
//                    mSecondZoneTitle.setText(
//                            String.format("%s%s",
//                                    summary.name.substring(0, 1).toUpperCase(),
//                                    summary.name.substring(1)));
//                    mSecondZoneTime.setText(summary.time);
//                    mSecondZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
//                            summary.totalEnergy));
//                    mSecondZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost));
//                    mSecondZoneAverageValue.setText(String.format(Locale.US,
//                            getString(R.string.energy_measure),
//                            summary.totalEnergy / summary.entriesCount));
//                    mSecondZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost / summary.entriesCount));
//                } else if (summary.id == GraphEnergyModel.ZONE_SEMIPEAK) {
//
//                    mThirdZone.setVisibility(View.VISIBLE);
//                    mThirdZoneTitle.setText(
//                            String.format("%s%s",
//                                    summary.name.substring(0, 1).toUpperCase(),
//                                    summary.name.substring(1)));
//                    mThirdZoneTime.setText(summary.time);
//                    mThirdZoneTotalValue.setText(String.format(getString(R.string.short_energy_measure),
//                            summary.totalEnergy));
//                    mThirdZoneTotalCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost));
//                    mThirdZoneAverageValue.setText(String.format(Locale.US,
//                            getString(R.string.energy_measure),
//                            summary.totalEnergy / summary.entriesCount));
//                    mThirdZoneAverageCost.setText(String.format(getString(R.string.short_rub_measure),
//                            summary.totalEnergyCost / summary.entriesCount));
//
//                 }
//            }
        });

    }

    @Override
    public void onError(int code, String message) {

        if (code == 2007) {
            showNothingFound();
        } else {
            mProgressView.setVisibility(View.GONE);
            mError.setVisibility(View.VISIBLE);
            mContent.setVisibility(View.GONE);
            mError.setText(String.format("Произошла ошибка: %s", message));
        }

    }

    private void showNothingFound() {
        mContent.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mNothingFound.setVisibility(View.VISIBLE);
        mStatisticsContainer.setVisibility(View.GONE);
    }

    @Override
    public void onSwipe(boolean isSwipeOnLeft) {

        int value = isSwipeOnLeft ? - 1 : 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mHeader.setAlpha(1);
        mHeader.setTranslationX(0);

        switch (periodType) {
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                calendar.add(Calendar.DAY_OF_YEAR, value);
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                calendar.add(Calendar.DAY_OF_YEAR, 7 * value);
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:

                calendar.add(Calendar.MONTH, value);
                break;
            default:
                calendar.add(Calendar.YEAR, value);
        }

        date = calendar.getTime();

        setTitle();
        mHeader.setRightSwipeable(checkIfSwipeableRight());
        load();
    }

    private void setTitle() {
        mSubheader.setText(CalendarUtils.getCurrentDate());
        switch (periodType) {
            case EnergySwitchModel.ENERGY_TYPE_TODAY:
                mHeader.setText(CalendarUtils.getDateShortText(date));
                mSubheader.setVisibility(View.GONE);
                mSubheader.setText(CalendarUtils.getCurrentDateShortText());
                break;
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                mHeader.setText(CalendarUtils.getWeekText(date));
                break;
            case EnergySwitchModel.ENERGY_TYPE_THIS_MONTH:
                mHeader.setText(String.format(Locale.US, "%s, %d",
                        MONTH[CalendarUtils.getMonthFromDate(date) - 1],
                        CalendarUtils.getYearFromDate(date)));
                break;
            case EnergySwitchModel.ENERGY_TYPE_YEAR:
                mHeader.setText(String.format(Locale.US,
                        "%d",
                        CalendarUtils.getYearFromDate(date)));
                break;
        }
    }

    private boolean checkIfSwipeableRight() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (periodType == EnergySwitchModel.ENERGY_TYPE_WEEK) {
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            calendar.getTime();
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        return new Date().after(calendar.getTime());
    }

    private void load() {
//        ArrayList<Integer> colors = new ArrayList<>();
//        List<BarEntry> entries = new ArrayList<>();
//
//        int entriesMaxCount = 0;
//        entriesMaxCount = 24;
//
//        for (int i = 0; i < 24; i++) {
//            entries.add(new BarEntry(i, new Random().nextFloat() * 58));
//            colors.add(Color.parseColor("#ffc13c"));
//        }
//
//        Collections.sort(entries, (barEntry, t1) -> (int) (barEntry.getX() - t1.getX()));
//
//        for (int i = 0; i < entriesMaxCount; i++) {
//            if (i >= entries.size() || entries.get(i).getX() != i) {
//                entries.add(i, new BarEntry(i, 0));
//            }
//        }
//
//        BarDataSet barDataSet = new BarDataSet(entries, "rub");
//        barDataSet.setColors(colors);
//        barDataSet.setDrawValues(false);
//        barDataSet.setHighlightEnabled(true);
//        BarData barData = new BarData(barDataSet);
//        mChart.setData(barData);
//
//        mContent.setVisibility(View.VISIBLE);
//        mProgressView.setVisibility(View.GONE);
//        mError.setVisibility(View.GONE);

        removeEndpointCallbacks();

        mContent.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_MY_PICK) {
            wasSharePressed = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
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


        View view = inflater.inflate(R.layout.fragment_resourse_by_period, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initData();


        GlideApp.with(this).asGif().load(R.drawable.loader).into(mProgressView);

        label.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { mChart.highlightValue(0, - 1); }
            return false;
        });
        mStatisticsContainer.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) { mChart.highlightValue(0, - 1); }
            return false;
        });


        mSubheader.setVisibility(
                periodType == EnergySwitchModel.ENERGY_TYPE_TODAY ? View.VISIBLE : View.GONE);
        setTitle();
        mChart.setData(null);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
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
                return CalendarUtils.getStringDayOfWeek((int) value);
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
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setMarker(new MyMarkerView(getContext(), R.layout.chart_marker));

        mChart.setNoDataText(getString(R.string.no_data));
        mChart.setNoDataTextColor(Color.parseColor("#8a1a1a1a"));


        mChart.setOnTouchListener(new ChartTouchListener(mChart) {

            boolean highlighting = false;
            float startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Highlight h = mChart.getHighlightByTouchPoint(event.getX(), event.getY());
                        if (h == null) { return false; }
                        highlighting = (h.getYPx() < event.getY() && h.getY() > 0 &&
                                mChart.getContentRect().contains(h.getXPx(), event.getY()));
                        startX = event.getX();
                        if (highlighting) {
                            ResourceByPeriodFragment.this.mChart.highlightValue(
                                    ResourceByPeriodFragment.this.mChart.getHighlightByTouchPoint(
                                            event.getX(),
                                            event.getY()),
                                    true);
                        } else {
                            ResourceByPeriodFragment.this.mChart.highlightValue(0, - 1);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (highlighting) {
                            h = mChart.getHighlightByTouchPoint(event.getX(), event.getY());
                            if (h == null) { return false; }
                            if (h.getYPx() < event.getY() && h.getY() > 0 &&
                                    mChart.getContentRect().contains(h.getXPx(), event.getY())) {
                                ResourceByPeriodFragment.this.mChart.highlightValue(
                                        ResourceByPeriodFragment.this.mChart.getHighlightByTouchPoint(
                                                event.getX(),
                                                event.getY()),
                                        true);
                            }
                        } else {

                            ViewPortHandler vph = mChart.getViewPortHandler();
                            Matrix transformation = vph.getMatrixTouch();
                            transformation.postTranslate(event.getX() - startX,
                                    0); // unset the negs to make x / y inverted
                            vph.refresh(transformation, mChart, true);

                            startX = event.getX();
                        }
                        break;
                }

                return true;
            }
        });


        if (periodType == EnergySwitchModel.ENERGY_TYPE_TODAY) {
            mChart.zoom(24f / 12.5f, 1, 0, 0);
            mChart.setDragEnabled(true);
        } else if (periodType == EnergySwitchModel.ENERGY_TYPE_THIS_MONTH) {
            mChart.zoom(31 / 12.5f, 1, 0, 0);
            mChart.setDragEnabled(true);
        }
        mHeader.setOnSwipeListener(this);
        load();
        return view;
    }

    private void initData() {
        switch (periodType) {
            case EnergySwitchModel.ENERGY_TYPE_WEEK:
                Calendar calendar = Calendar.getInstance();
                while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                date = calendar.getTime();
                break;
            default:
                date = new Date();
                break;
        }

        mHeader.setRightSwipeable(checkIfSwipeableRight());
    }
}
