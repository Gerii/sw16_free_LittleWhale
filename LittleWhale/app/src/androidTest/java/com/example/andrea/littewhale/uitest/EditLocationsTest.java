package com.example.andrea.littewhale.uitest;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.andrea.littewhale.EditLocations;
import com.example.andrea.littewhale.R;
import com.robotium.solo.Solo;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by clemens on 05.04.16.
 */
public class EditLocationsTest extends ActivityInstrumentationTestCase2 {

    private Solo mySolo;

    public View getViewAtIndex(final ListView listElement, final int indexInList, Instrumentation instrumentation) {
        ListView parent = listElement;
        if (parent != null) {
            if (indexInList <= parent.getAdapter().getCount()) {
                scrollListTo(parent, indexInList, instrumentation);
                int indexToUse = indexInList - parent.getFirstVisiblePosition();
                return parent.getChildAt(indexToUse);
            }
        }
        return null;
    }

    public <T extends AbsListView> void scrollListTo(final T listView,
                                                     final int index, Instrumentation instrumentation) {
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(index);
            }
        });
        instrumentation.waitForIdleSync();
    }

    public EditLocationsTest(){
        super(EditLocations.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());
    }

    public void tearDown() throws Exception {

    }
/*
    public void testPlus(){
     //   View fab = getActivity().findViewById(R.id.fabAdd);
      //  mySolo.clickOnView(fab);
    }*/

    public  void testList(){
        ListView lv = (ListView) getActivity().findViewById(R.id.listView);

        for(int i = 0; i < lv.getAdapter().getCount() ; i++){
            mySolo.clickOnView(getViewAtIndex(lv, i, getInstrumentation()));
            Log.w("w", i + "a");
            mySolo.sleep(500);
        }
    }
}