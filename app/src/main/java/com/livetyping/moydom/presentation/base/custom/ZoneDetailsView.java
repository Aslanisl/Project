package com.livetyping.moydom.presentation.features.base.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.energy.model.GraphEnergyModel;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by XlebNick for MoyDom.
 */

public class ZoneDetailsView extends LinearLayout {


    @BindView(R.id.zone_container)
    ViewGroup mZone;
    @BindView(R.id.zone_title)
    TextView mZoneTitle;
    @BindView(R.id.zone_time)
    TextView mZoneTime;
    @BindView(R.id.zone_average_cost)
    TextView mZoneAverageCost;
    @BindView(R.id.zone_average_value)
    TextView mZoneAverageValue;
    @BindView(R.id.zone_total_cost)
    TextView mZoneTotalCost;
    @BindView(R.id.zone_total_value)
    TextView mZoneTotalValue;
    @BindView(R.id.zone_color)
    View mZoneColor;
    private Unbinder mUnbinder;

    public ZoneDetailsView(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        inflate(getContext(), R.layout.view_zone_details, this);

        mUnbinder = ButterKnife.bind(this, this);

    }

    public ZoneDetailsView(Context context, GraphEnergyModel.ZoneSummary summary){
        super(context);
        init(summary);
    }

    private void init(GraphEnergyModel.ZoneSummary summary) {
        init();

        mZoneColor.setBackgroundResource(summary.color);
        mZoneTitle.setText(
                String.format("%s%s",
                        summary.name.substring(0, 1).toUpperCase(),
                        summary.name.substring(1)));
        mZoneTime.setText(summary.time);
        mZoneTotalValue.setText(String.format(getContext().getString(R.string.short_energy_measure),
                summary.totalEnergy));
        mZoneTotalCost.setText(String.format(getContext().getString(R.string.short_rub_measure),
                summary.totalEnergyCost));
        mZoneAverageValue.setText(String.format(Locale.US,
                getContext().getString(R.string.energy_measure),
                summary.totalEnergy / summary.entriesCount));
        mZoneAverageCost.setText(String.format(getContext().getString(R.string.short_rub_measure),
                summary.totalEnergyCost / summary.entriesCount));
    }

    public ZoneDetailsView(Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public ZoneDetailsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


}
