package com.ycy.accessibilityservicetest;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Auther 彦辰渊 on 2018/4/27 11:40.
 */
public class MyAccessibilityService extends AccessibilityService{

    private static final String TAG = "MyAccessibilityService";

    private String nowPackageName;
    private boolean isClickSetMoney;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        nowPackageName = event.getPackageName().toString();
        if (nowPackageName.equals("com.eg.android.AlipayGphone") && MyApplication.getInstance()
                .getFlag()) {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                AccessibilityNodeInfo rootNode = this.getRootInActiveWindow();
                executeOperation(rootNode);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean executeOperation(AccessibilityNodeInfo info) {
        if (info == null) return false;
        if (info.getChildCount() == 0) {
            if (info.getText() != null) {
                if ("收钱".equals(info.getText().toString())) {
                    if ("android.widget.TextView".equals(info.getClassName())) {
                        AccessibilityNodeInfo parent = info;
                        while (parent != null) {
                            if (parent.isClickable()) {
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                break;
                            }
                            parent = parent.getParent();
                        }
                    }
                } else if ("设置金额".equals(info.getText().toString())) {
                    if ("android.widget.Button".equals(info.getClassName())) {
                        AccessibilityNodeInfo parent = info;
                        while (parent != null) {
                            if (parent.isClickable() && !isClickSetMoney) {
                                boolean b = parent.performAction(AccessibilityNodeInfo
                                        .ACTION_CLICK);
                                if (b) {
                                    isClickSetMoney = true;
                                }
                                break;
                            }
                            parent = parent.getParent();
                        }
                    }
                } else if ("金额".equals(info.getText().toString())) {
                    if ("android.widget.TextView".equals(info.getClassName())) {
                        AccessibilityNodeInfo parent = info.getParent();
                        AccessibilityNodeInfo child = parent.getChild(1);
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService
                                (CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("scb", MyApplication.getInstance()
                                .getParams());
                        clipboardManager.setPrimaryClip(clipData);
                        boolean b = child.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                        isClickSetMoney = false;
                    }
                } else if ("确定".equals(info.getText().toString())) {
                    if ("android.widget.Button".equals(info.getClassName())) {
                        AccessibilityNodeInfo parent = info;
                        while (parent != null) {
                            if (parent.isClickable()) {
                                boolean b = parent.performAction(AccessibilityNodeInfo
                                        .ACTION_CLICK);
                                return b;
                            }
                            parent = parent.getParent();
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    executeOperation(info.getChild(i));
                }
            }
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }
}
