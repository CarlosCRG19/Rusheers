package com.hackmty.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hackmty.models.ClassRoom;

public class PageAdapter extends FragmentStateAdapter
{
    public static final int NUM_PAGES = 4;
    ClassRoom room;

    public PageAdapter(@NonNull FragmentActivity fa, ClassRoom room)
    {
        super(fa);
        this.room = room;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        switch (position)
        {
            case 0:
                //return new ChatFragment(room);
            case 1:
                //return new TimerFragment(room);
            case 2:
                //return new ListFragment(room);
            case 3:
                //return new MusicFragment(room);
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount()
    {
        return NUM_PAGES;
    }
}
