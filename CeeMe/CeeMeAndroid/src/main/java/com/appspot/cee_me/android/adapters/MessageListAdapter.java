package com.appspot.cee_me.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.appspot.cee_me.android.DateUtils;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.sync.model.Message;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Adapter for directory search results (list of Devices)
 */
public class MessageListAdapter extends ArrayAdapter<Message> {

    Context context;
    int layoutResourceId;
    List<Message> messageList = null;

    public MessageListAdapter(Context context, int layoutResourceId, List<Message> messageList) {
        super(context, layoutResourceId, messageList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        clear();
        this.messageList = messageList;
        addAll(messageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MessageHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MessageHolder();
            holder.fromName = (TextView)row.findViewById(R.id.message_result_from_textView);
            holder.messageText = (TextView)row.findViewById(R.id.message_result_msgText_textView);
            holder.url = (TextView)row.findViewById(R.id.message_result_url_textView);
            holder.date = (TextView)row.findViewById(R.id.message_result_date_textView);
            holder.publicId = (TextView)row.findViewById(R.id.message_result_publicid_textView);
            holder.message = null;

            row.setTag(holder);
        }
        else
        {
            holder = (MessageHolder)row.getTag();
        }

        List<Message> Messages = getMessageList();
        if (Messages != null) {
            Message message = Messages.get(position);

            DateTime creationDate = DateUtils.getDateTimeFromJSONObject(message.getCreationDate());
            String dateText = creationDate.toString(DateUtils.getFormatter());

            holder.fromName.setText(message.getFromUser().getAccountName());
            holder.messageText.setText(message.getText());
            holder.url.setText(message.getUrlData());
            holder.date.setText(dateText);
            holder.publicId.setText(message.getFromDevice().getPublicId());
            holder.message = message; // TODO: Check this!!
        }
        return row;
    }

    static class MessageHolder
    {
        // ImageView imgIcon;
        TextView fromName;
        TextView messageText;
        TextView url;
        TextView date;
        TextView publicId;

        Message message;
    }
}
