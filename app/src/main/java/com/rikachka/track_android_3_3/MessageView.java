package com.rikachka.track_android_3_3;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MessageView extends ListView {
    public MessageView(Context context) {
        super(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void reflesh() {
        getRootView().invalidate();
    }
}
