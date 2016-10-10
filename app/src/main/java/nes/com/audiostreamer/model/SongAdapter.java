package nes.com.audiostreamer.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import nes.com.audiostreamer.R;

/**
 * Created by nesli on 09.10.2016.
 */

public class SongAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Song> songList;
    private int selectedIndex;

    public SongAdapter(Activity activity, List<Song> songList) {
        inflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.line_of_listwiev, null);
        }

        TextView titleText =
                (TextView) convertView.findViewById(R.id.title);
        TextView artistText =
                (TextView) convertView.findViewById(R.id.artist);
        TextView durationText =
                (TextView) convertView.findViewById(R.id.duration);

        final Song song = songList.get(position);
        titleText.setText(song.getTitle());
        artistText.setText(song.getArtist());
        durationText.setText(song.getDuration()+"");

        if (position == selectedIndex) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.colorPrimary));
        }

        return convertView;
    }

    public void setSelectedIndex(int selectedIndex){
        this.selectedIndex = selectedIndex;
    }
}
