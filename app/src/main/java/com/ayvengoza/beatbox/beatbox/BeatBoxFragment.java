package com.ayvengoza.beatbox.beatbox;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.ayvengoza.beatbox.beatbox.databinding.FragmentBeatBoxBinding;
import com.ayvengoza.beatbox.beatbox.databinding.ListItemSoundBinding;

import java.util.List;

/**
 * Created by ang on 02.10.17.
 */

public class BeatBoxFragment extends Fragment {
    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstance(){
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mBeatBox = new BeatBox(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentBeatBoxBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_beat_box,container, false);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBeatBox.setRate(progress/100.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return binding.getRoot();
    }

    private class SoundHolder extends RecyclerView.ViewHolder{
        private ListItemSoundBinding mBinding;

        public SoundHolder(ListItemSoundBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new SoundViewModel(mBeatBox));
        }

        public void bind(Sound sound){
            mBinding.getViewModel().setSound(sound);
            mBinding.executePendingBindings();
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder>{
        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds){
            mSounds = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil
                    .inflate(inflater, R.layout.list_item_sound, parent, false);
            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(SoundHolder holder, int position) {
            Sound sound = mBeatBox.getSounds().get(position);
            holder.bind(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }
}
