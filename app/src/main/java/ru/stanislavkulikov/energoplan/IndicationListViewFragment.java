package ru.stanislavkulikov.energoplan;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndicationListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndicationListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndicationListViewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String ARG_COUNTER_ID = "counter_id";
    private static final String ARG_HOMESTEAD_ID = "homestead_id";

    private int counterId;
    private int homesteadId;
    private SimpleCursorAdapter scAdapter;
    private ListView lvData;
    private DataBase myDataBase;

    private OnFragmentInteractionListener mListener;

    public IndicationListViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param counterId Parameter ID.
     * @return A new instance of fragment IndicationListViewFragment.
     */
    public static IndicationListViewFragment newInstance(int counterId, int homesteadId) {
        IndicationListViewFragment fragment = new IndicationListViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNTER_ID, counterId);
        args.putInt(ARG_HOMESTEAD_ID, homesteadId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counterId = getArguments().getInt(ARG_COUNTER_ID);
            homesteadId = getArguments().getInt(ARG_HOMESTEAD_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_indication_list_view, container, false);

        // формируем столбцы сопоставления
        String[] from = new String[] { DataBase.INDICATIONS_COLUMN };
        int[] to = new int[] { R.id.indicationItemTextView };

        // создааем адаптер и настраиваем список
        scAdapter = new SimpleCursorAdapter(getContext(), R.layout.indications_list_item, null, from, to, 0);
        lvData = (ListView) view.findViewById(R.id.indicationsListView);
        lvData.setAdapter(scAdapter);

        // создаем лоадер для чтения данных
        getActivity().getSupportLoaderManager().restartLoader(2, null, this);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.indication_add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentIndicationListAddButton(counterId, homesteadId);
                getActivity().getSupportLoaderManager().getLoader(2).forceLoad();
            }
        });

        return view;
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
        void onFragmentIndicationListAddButton(int counterId, int homesteadId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new IndicationsCursorLoader(getContext(), myDataBase, counterId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class IndicationsCursorLoader extends CursorLoader {

        DataBase myDataBase;
        int paramId;

        public IndicationsCursorLoader(Context context, DataBase myDataBase, int paramId) {
            super(context);
            this.myDataBase = myDataBase;
            this.paramId = paramId;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = myDataBase.getAllIndicationsData(paramId);
            return cursor;
        }

    }
}
