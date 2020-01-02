package in.aaaonlineservice.crimemukhbir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class HomeFragment extends Fragment {

   private TabLayout tabLayout;
   private ViewPager viewPager;
    private static int int_items = 10 ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View x =  inflater.inflate(R.layout.home_tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(10);


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                    setuuicon();
                   }
        });

        return x;


    }

    private void setuuicon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.hom);
        }

    class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
          switch (position){
              case 0 : return new Taja();
              case 1 : return new DeshVidesh();
              case 2 : return new AzabGazab();
             case 3 : return new Crime();
              case 4 : return new Prasashan();
             case 5 : return new Chetriya();
             case 6 : return new Business();
             case 7 : return new Manoranjan();
             case 8 : return new Dharam();
             case 9 : return new Filmi();

          }
        return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){

                case 1 :
                    return "देश विदेश";
                case 2 :
                    return "अजब गज़ब";
                case 3 :
                    return "क्राइम";
                case 4 :
                    return "प्रशासन";
                case 5 :
                    return "क्षेत्रीय";
                case 6 :
                    return "बिज़नेस";
                case 7 :
                    return "मनोरंजन";
                case 8 :
                    return "धर्म";
                case 9 :
                    return "फ़िल्मी दुनियां";


            }
                return null;
        }
    }

}
