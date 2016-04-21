package ru.stanislavkulikov.energoplan;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomesteadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomesteadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomesteadFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_PARAM_ID = "param_id";

    private int paramId;
    private SimpleCursorAdapter scAdapter;
    private ListView lvData;
    private DataBase myDataBase;
    private OnFragmentInteractionListener mListener;

    public HomesteadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramId Parameter ID.
     * @return A new instance of fragment HomesteadFragment.
     */
    public static HomesteadFragment newInstance(int paramId) {
        HomesteadFragment fragment = new HomesteadFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_ID, paramId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramId = getArguments().getInt(ARG_PARAM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homestead, container, false);

        // формируем столбцы сопоставления
        String[] from = new String[] { DataBase.COUNTER_NAME_COLUMN };
        int[] to = new int[] { R.id.counterNameTextView };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(getContext(), R.layout.counter_list_item, null, from, to, 0);
        lvData = (ListView) view.findViewById(R.id.counterListView);
        lvData.setAdapter(scAdapter);

        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().restartLoader(1, null, this);

        HomesteadModel homesteadModel = myDataBase.getHomesteadRec(paramId);
        TextView fioTextView = (TextView) view.findViewById(R.id.homesteadFioText);
        fioTextView.setText(homesteadModel.getFioColumn());
        TextView numTextView = (TextView) view.findViewById(R.id.homesteadNumText);
        numTextView.setText(homesteadModel.getHomesteadNumberColumn());
        Button addCounterButton = (Button) view.findViewById(R.id.homesteadAddCounter);
        addCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CounterModel counterModel = new CounterModel();
                counterModel.setName("Test" + paramId);
                counterModel.setHomesteadId(paramId);
                myDataBase.addCounterRec(counterModel);
                getActivity().getSupportLoaderManager().getLoader(1).forceLoad();
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    mListener.onFragmentBackPressed();
                    return true;
                }
                return false;
            }
        } );
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        MainListActivity activity = (MainListActivity)getActivity();
        this.myDataBase = activity.myDataBase;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentBackPressed();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new CounterCursorLoader(getContext(), myDataBase, paramId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class CounterCursorLoader extends CursorLoader {

        DataBase myDataBase;
        int paramId;

        public CounterCursorLoader(Context context, DataBase myDataBase, int paramId) {
            super(context);
            this.myDataBase = myDataBase;
            this.paramId = paramId;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = myDataBase.getAllCounterData(paramId);
            return cursor;
        }

    }
}
