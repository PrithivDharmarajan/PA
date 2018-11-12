package com.e2infosystems.activeprotective.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.input.model.AssignUnAssignBeltEntity;
import com.e2infosystems.activeprotective.main.BaseActivity;
import com.e2infosystems.activeprotective.output.model.AllUserItemListEntityRes;
import com.e2infosystems.activeprotective.services.APIRequestHandler;
import com.e2infosystems.activeprotective.utils.AppConstants;
import com.e2infosystems.activeprotective.utils.DialogManager;
import com.e2infosystems.activeprotective.utils.InterfaceTwoBtnCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllUserListAdapter extends RecyclerView.Adapter<AllUserListAdapter.Holder> {

    private ArrayList<AllUserItemListEntityRes> mAllUserItemArrListRes;
    private Context mContext;
    private BaseActivity mCurrentBaseActivity;

    public AllUserListAdapter(ArrayList<AllUserItemListEntityRes> allUserItemArrListRes, BaseActivity baseActivity, Context context) {
        mAllUserItemArrListRes = allUserItemArrListRes;
        mCurrentBaseActivity = baseActivity;
        mContext = context;
    }

    @NonNull
    @Override
    public AllUserListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adap_all_user_list_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AllUserListAdapter.Holder holder, int position) {
        AllUserItemListEntityRes allUserItemListEntityRes = mAllUserItemArrListRes.get(position);

        holder.mUserNameTxt.setText(allUserItemListEntityRes.getFirstName().isEmpty()?allUserItemListEntityRes.getUserId():allUserItemListEntityRes.getFirstName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogManager.getInstance().showOptionPopup(mContext, String.format(mContext.getString(R.string.assign_msg),holder.mUserNameTxt.getText().toString().trim()), mContext.getString(R.string.assign), mContext.getString(R.string.cancel), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick() {
                        AllUserItemListEntityRes allUserItemListEntityRes = mAllUserItemArrListRes.get(holder.getAdapterPosition());

                        AssignUnAssignBeltEntity assignBeltEntity = new AssignUnAssignBeltEntity();
                        assignBeltEntity.setCommunityId(allUserItemListEntityRes.getCommunityId());
                        assignBeltEntity.setDeviceId(AppConstants.BELT_DEVICE_ID);
                        assignBeltEntity.setUserId(allUserItemListEntityRes.getUserId());
                        assignBeltEntity.setUserName(allUserItemListEntityRes.getFirstName());

                        APIRequestHandler.getInstance().assignBeltAPICall(assignBeltEntity, mCurrentBaseActivity);
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return mAllUserItemArrListRes.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_name_txt)
        TextView mUserNameTxt;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
