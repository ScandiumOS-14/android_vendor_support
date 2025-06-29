/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scandium.home.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import com.scandium.home.R;

public class PackageListAdapter extends BaseAdapter implements Runnable {
    private PackageManager mPm;
    private LayoutInflater mInflater;
    private final List<PackageItem> mInstalledPackages = new LinkedList<PackageItem>();

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            PackageItem item = (PackageItem) msg.obj;
            int index = Collections.binarySearch(mInstalledPackages, item);
            if (index < 0) {
                mInstalledPackages.add(-index - 1, item);
            }
            notifyDataSetChanged();
        }
    };

    public static class PackageItem implements Comparable<PackageItem> {
        public final String packageName;
        public final CharSequence title;
        public final Drawable icon;

        PackageItem(String packageName, CharSequence title, Drawable icon) {
            this.packageName = packageName;
            this.title = title;
            this.icon = icon;
        }

        @Override
        public int compareTo(PackageItem another) {
            int result = title.toString().compareToIgnoreCase(another.title.toString());
            return result != 0 ? result : packageName.compareTo(another.packageName);
        }
    }

    public PackageListAdapter(Context context) {
        mPm = context.getPackageManager();
        mInflater = LayoutInflater.from(context);
        reloadList();
    }

    @Override
    public int getCount() {
        synchronized (mInstalledPackages) {
            return mInstalledPackages.size();
        }
    }

    @Override
    public PackageItem getItem(int position) {
        synchronized (mInstalledPackages) {
            return mInstalledPackages.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        synchronized (mInstalledPackages) {
            // packageName is guaranteed to be unique in mInstalledPackages
            return mInstalledPackages.get(position).packageName.hashCode();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.applist_preference_icon, null, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.title = (TextView) convertView.findViewById(com.android.internal.R.id.title);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
        }

        PackageItem applicationInfo = getItem(position);
        holder.title.setText(applicationInfo.title);
        holder.icon.setImageDrawable(applicationInfo.icon);

        return convertView;
    }

    private void reloadList() {
        mInstalledPackages.clear();
        new Thread(this).start();
    }

    @Override
    public void run() {
        List<ApplicationInfo> installedAppsInfo = mPm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : installedAppsInfo) {
            if (appInfo.icon != 0) {
                final PackageItem item = new PackageItem(appInfo.packageName,
                        appInfo.loadLabel(mPm), appInfo.loadIcon(mPm));
                mHandler.obtainMessage(0, item).sendToTarget();
            }
        }
    }

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }
}
