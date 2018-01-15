package com.live.gblive.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.gblive.R;
import com.live.gblive.base.MvpFragment;
import com.live.gblive.contract.FullRoomContract;
import com.live.gblive.model.bean.Room;
import com.live.gblive.model.bean.RoomLine;
import com.live.gblive.presenter.FullRoomPresenter;
import com.live.gblive.utils.XPicasso;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: xguobin
 * email:xguobin12@163.com
 * created on: 2018/1/15 14:41
 * description:
 */
public class FullRoomFragment extends MvpFragment<FullRoomPresenter> implements FullRoomContract.View {
    @BindView(R.id.rlAnchorInfo)
    View rlAnchorInfo;
    @BindView(R.id.civAvatar)
    ImageView civAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvFans)
    TextView tvFans;

    @BindView(R.id.ivCover)
    ImageView ivCover;
    @BindView(R.id.frameVideo)
    FrameLayout frameVideo;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.ivInput)
    ImageView ivInput;
    @BindView(R.id.ivFollow)
    ImageView ivFollow;
    @BindView(R.id.ivGift)
    ImageView ivGift;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivMessage)
    ImageView ivMessage;
    @BindView(R.id.rlRoomInfo)
    LinearLayout rlRoomInfo;
    @BindView(R.id.videoContent)
    RelativeLayout videoContent;

//    @BindView(R.id.flutteringLayout)
//    FlutteringLayout flutteringLayout;
//    @BindView(R.id.btnHeart)
//    Button btnHeart;

    private String uid;

    private String coverUrl;

    private VideoFragment videoFragment;

    public static FullRoomFragment newInstance(String uid, String coverUrl) {

        Bundle args = new Bundle();

        FullRoomFragment fragment = new FullRoomFragment();
        fragment.uid = uid;
        fragment.coverUrl = coverUrl;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootViewId() {
        return R.layout.fragment_full_room;
    }

    @Override
    public void initView() {
        tvAccount.setText(String.format(getString(R.string.qm_account), uid));
        XPicasso.load(getActivity(), coverUrl).into(ivCover);
        mPresenter = new FullRoomPresenter(this);
    }

    @Override
    public void initData() {
        mPresenter.enterRoom(uid);
    }

    @Override
    public void getRoomSuccess(Room room) {
        if (room != null) {
            rlAnchorInfo.setVisibility(View.VISIBLE);
            tvName.setText(room.getNick());
            tvFans.setText(String.format(getString(R.string.fans_num), room.getFollow()));
            XPicasso.load(getActivity(), room.getAvatar()).into(civAvatar);

            String url = null;
//                            RoomLine roomLine = room.getRoom_lines().get(0);
            RoomLine roomLine = room.getLive().getWs();

            RoomLine.FlvBean flv = roomLine.getFlv();
            if (flv != null) {
                url = flv.getValue(false).getSrc();
            } else {
                url = roomLine.getHls().getValue(false).getSrc();
            }
            playUrl(url);
        }
    }

    @Override
    public void getRoomFail(String message) {

    }

    public void playUrl(String url) {
        if (videoFragment == null) {
            videoFragment = VideoFragment.newInstance(url, true);
        }
        replaceChildFragment(R.id.frameVideo, videoFragment);
    }

    public void replaceChildFragment(@IdRes int id, Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    @OnClick({R.id.ivBack, R.id.ivGift, R.id.ivShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getActivity().finish();
                break;
            case R.id.ivGift:
                break;
            case R.id.ivShare:
                break;
        }
    }
}
