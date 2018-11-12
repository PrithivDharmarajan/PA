package com.e2infosystems.activeprotective.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.input.model.DeleteDeviceEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.BeltItemListEntityRes;
import com.e2infosystems.activeprotective.services.APIRequestHandler;
import com.e2infosystems.activeprotective.ui.BeltDetails;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeltListAdapter extends RecyclerView.Adapter<BeltListAdapter.Holder> {

    private ArrayList<BeltItemListEntityRes> mBeltItemArrListRes;
    private boolean mIsDeleteBool;
    private Context mContext;
    private BaseActivity mCurrentBaseActivity;

    public BeltListAdapter(ArrayList<BeltItemListEntityRes> beltItemArrListRes, boolean isDeleteBool, BaseActivity baseActivity, Context context) {
        mBeltItemArrListRes = beltItemArrListRes;
        mIsDeleteBool = isDeleteBool;
        mCurrentBaseActivity = baseActivity;
        mContext = context;
    }

    @NonNull
    @Override
    public BeltListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adap_belt_list_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BeltListAdapter.Holder holder, int position) {
        BeltItemListEntityRes beltItemListEntityRes = mBeltItemArrListRes.get(position);

        holder.mDeleteImg.setVisibility(mIsDeleteBool ? View.VISIBLE : View.GONE);
        holder.mUserNameTxt.setText(beltItemListEntityRes.getAssignStatus() == AppConstants.SUCCESS_CODE ? beltItemListEntityRes.getUserName() : beltItemListEntityRes.getDeviceId());
        holder.mUserNameTxt.setTextColor(ContextCompat.getColor(mContext, beltItemListEntityRes.getAssignStatus() == AppConstants.SUCCESS_CODE ? R.color.blue : R.color.red));

        holder.mUserStatusTxt.setText(mContext.getString(beltItemListEntityRes.getWiFiConfiguredStatus() == AppConstants.SUCCESS_CODE ? R.string.wifi_configured : R.string.not_configured));
        holder.mUserStatusTxt.setTextColor(ContextCompat.getColor(mContext, beltItemListEntityRes.getWiFiConfiguredStatus() == AppConstants.SUCCESS_CODE ? R.color.green : R.color.blue));

        holder.mDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBeltFromBeltList(mBeltItemArrListRes.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mDeleteImg.getVisibility() == View.VISIBLE) {
                    deleteBeltFromBeltList(mBeltItemArrListRes.get(holder.getAdapterPosition()));
                } else {
                    AppConstants.BELT_DEVICE_ID = mBeltItemArrListRes.get(holder.getAdapterPosition()).getDeviceId();
                    AppConstants.IS_FROM_BELT_LIST_BOOL = true;
                    mCurrentBaseActivity.nextScreen(BeltDetails.class);
                }
            }
        });
    }

    private void deleteBeltFromBeltList(final BeltItemListEntityRes beltItemListEntityRes) {
        DialogManager.getInstance().showOptionPopup(mContext, mContext.getString(R.string.delete_msg), mContext.getString(R.string.yes), mContext.getString(R.string.no), new InterfaceTwoBtnCallback() {
            @Override
            public void onNegativeClick() {

            }

            @Override
            public void onPositiveClick() {
                ArrayList<DeleteDeviceEntity> deleteDeviceArrEntityList = new ArrayList<>();
                DeleteDeviceEntity deleteDeviceEntity = new DeleteDeviceEntity();
                deleteDeviceEntity.setDeviceId(beltItemListEntityRes.getDeviceId());
                deleteDeviceEntity.setUserId(beltItemListEntityRes.getUserId());

                deleteDeviceArrEntityList.add(deleteDeviceEntity);

                APIRequestHandler.getInstance().deleteDeviceFromBeltListAPICall(deleteDeviceArrEntityList, mCurrentBaseActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeltItemArrListRes.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.delete_img)
        ImageView mDeleteImg;

        @BindView(R.id.user_name_txt)
        TextView mUserNameTxt;

        @BindView(R.id.user_status_txt)
        TextView mUserStatusTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
