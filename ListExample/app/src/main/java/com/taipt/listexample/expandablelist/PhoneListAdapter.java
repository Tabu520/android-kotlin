package com.taipt.listexample.expandablelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taipt.listexample.R;

import java.util.List;

public class PhoneListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CompanyModel> companyModels;

    public PhoneListAdapter(Context context, List<CompanyModel> companyModels) {
        this.context = context;
        this.companyModels = companyModels;
    }

    @Override
    public int getGroupCount() {
        return companyModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return companyModels.get(groupPosition).getDevices().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return companyModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return companyModels.get(groupPosition).getDevices().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String companyName = ((CompanyModel) getGroup(groupPosition)).getName();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_company_list, null);
        }

        TextView tvName = convertView.findViewById(R.id.txt_company_name);
        ImageView ivGroupIndicator = convertView.findViewById(R.id.ivGroupIndicator);
        View upperLine = convertView.findViewById(R.id.upper_line);
        View lowerLine = convertView.findViewById(R.id.lower_line);
        tvName.setText(companyName);
        ivGroupIndicator.setSelected(isExpanded);
        if (isExpanded) {
            upperLine.setVisibility(View.VISIBLE);
            lowerLine.setVisibility(View.VISIBLE);
        } else {
            upperLine.setVisibility(View.GONE);
            lowerLine.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String deviceName = ((DeviceModel) getChild(groupPosition, childPosition)).getName();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_device_list, null);
        }

        TextView tvName = convertView.findViewById(R.id.device_name);
        tvName.setText(deviceName);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
