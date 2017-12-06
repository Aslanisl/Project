package com.livetyping.moydom.ui.fragment.mainScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livetyping.moydom.R;
import com.livetyping.moydom.apiModel.energy.CurrentEnergy;
import com.livetyping.moydom.data.Prefs;
import com.livetyping.moydom.data.repository.EnergyRepository;
import com.livetyping.moydom.ui.activity.settings.EnergySwitchModel;
import com.livetyping.moydom.ui.activity.settings.SettingsActivity;
import com.livetyping.moydom.ui.activity.settings.SettingsSwitchModel;
import com.livetyping.moydom.ui.adapter.EnergyMyHomeAdapter;
import com.livetyping.moydom.ui.fragment.BaseFragment;
import com.livetyping.moydom.utils.HelpUtils;
import com.livetyping.moydom.utils.NetworkUtil;
import com.livetyping.moydom.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class MyHomeFragment extends BaseFragment implements EnergyRepository.EnergyCallback{
    public static final String TAG = MyHomeFragment.class.getSimpleName();

    private static final int SETTINGS_REQUEST_CODE = 2;

    @BindView(R.id.fragment_my_home_cameras_recycler) RecyclerView mCamerasRecycler;

    @BindView(R.id.fragment_my_home_energy_recycler) RecyclerView mEnergyRecycler;
    private EnergyMyHomeAdapter mEnergyAdapter;

    private enum NetworkState{
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }

    private NetworkState mNetworkState;

    private static final int EXPANDED_ANIMATION_DURATION = 700;
    private static final int CONNECTED_ANIMATION_DELAYED = 1000;
    private Handler mAnimationHandler;
    private Runnable mInternetDisconnected;
    private Runnable mInternetConnected;
    @BindView(R.id.fragment_my_home_internet_container) RelativeLayout mNoInternetContainer;
    @BindView(R.id.fragment_my_home_internet_text) TextView mNoInternetTitle;

    private EnergyRepository mEnergyRepository;

    private CompositeDisposable mCompositeDisposable;
    private Prefs mPrefs;

    private Unbinder mUnbinder;

    private BroadcastReceiver mConnectedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = NetworkUtil.isConnected(getContext());
            mNetworkState = isConnected ? NetworkState.CONNECTED : NetworkState.DISCONNECTED;
            changeNoInternetViews();
        }
    };

    public static MyHomeFragment newInstance() {
        MyHomeFragment fragment = new MyHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAnimationHandler = new Handler();
        mInternetConnected = () -> {
            if (mNoInternetContainer != null) {
                ViewUtils.collapse(mNoInternetContainer);
            }
        };
        mInternetDisconnected = () -> {
            if (mNoInternetContainer != null) {
                ViewUtils.expand(mNoInternetContainer);
            }
        };
        mPrefs = Prefs.getInstance();
        mCompositeDisposable = new CompositeDisposable();
        mEnergyRepository = EnergyRepository.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_home, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mEnergyRepository.setEnergyCallback(this);
        mEnergyRepository.getEnergy();

        initEnergyView();

        getContext().registerReceiver(mConnectedReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initEnergyView(){
        mEnergyAdapter = new EnergyMyHomeAdapter(getContext());
        mEnergyRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mEnergyRecycler.setAdapter(mEnergyAdapter);
        mEnergyRecycler.setNestedScrollingEnabled(false);
        mCompositeDisposable.add(Observable.create((ObservableOnSubscribe<List<EnergySwitchModel>>) e -> {
            e.onNext(mPrefs.getEnergyModels());
            e.onComplete();
        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mEnergyAdapter::addEnergyModels,
                        throwable -> showToast(throwable.getMessage()))
        );
    }

    @Override
    public void onCurrentEnergyResponse(CurrentEnergy energy) {
        showToast(energy.getTariffId());
    }

    @Override
    public void onError(String message) {

    }

    private void changeNoInternetViews(){
        switch (mNetworkState){
            case CONNECTED:
                mNoInternetTitle.setText(R.string.connected_with_internet);
                mNoInternetContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.internet_connected_color));
                mAnimationHandler.postDelayed(mInternetConnected, CONNECTED_ANIMATION_DELAYED);
                break;
            case DISCONNECTED:
                mNoInternetTitle.setText(R.string.no_connection_with_internet);
                mNoInternetContainer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.internet_not_connected_color));
                mAnimationHandler.postDelayed(mInternetDisconnected, 0);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK){
            //TODO apply new settings
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAnimationHandler != null) {
            mAnimationHandler.removeCallbacks(mInternetDisconnected);
            mAnimationHandler.removeCallbacks(mInternetConnected);
        }
        mEnergyRepository.removeEnergyCallback();
        mUnbinder.unbind();
        getContext().unregisterReceiver(mConnectedReceiver);
    }
}
