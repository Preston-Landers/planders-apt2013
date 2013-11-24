package com.appspot.cee_me.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.register.model.Device;

import java.util.List;

/**
 * Adapter for directory search results (list of Devices)
 */
public class DirectoryListAdapter extends ArrayAdapter<Device> {

    Context context;
    int layoutResourceId;
    List<Device> deviceList = null;

    public DirectoryListAdapter(Context context, int layoutResourceId, List<Device> deviceList) {
        super(context, layoutResourceId, deviceList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeviceHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeviceHolder();
            holder.accountName = (TextView)row.findViewById(R.id.directory_result_accountName_textView);
            holder.deviceName = (TextView)row.findViewById(R.id.directory_result_deviceName_textView);
            holder.hwDesc = (TextView)row.findViewById(R.id.directory_result_hwdesc_textView);
            holder.comment = (TextView)row.findViewById(R.id.directory_result_comment_textView);
            holder.publicId = (TextView)row.findViewById(R.id.directory_result_publicid_textView);

            row.setTag(holder);
        }
        else
        {
            holder = (DeviceHolder)row.getTag();
        }

        Device device = deviceList.get(position);
        holder.accountName.setText(device.getOwnerAccountName());
        holder.deviceName.setText(device.getName());
        holder.hwDesc.setText(device.getHardwareDescription());
        holder.comment.setText(device.getComment());
        holder.publicId.setText(device.getPublicId());

        return row;
    }

    static class DeviceHolder
    {
        // ImageView imgIcon;
        TextView accountName;
        TextView deviceName;
        TextView hwDesc;
        TextView comment;
        TextView publicId;
    }
}
