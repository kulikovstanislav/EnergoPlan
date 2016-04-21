package ru.stanislavkulikov.energoplan;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewHomesteadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewHomesteadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewHomesteadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DataBase myDataBase;
    private EditText newHomesteadNumEditText, newFIOEditText;

    public AddNewHomesteadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewHomesteadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewHomesteadFragment newInstance(String param1, String param2) {
        AddNewHomesteadFragment fragment = new AddNewHomesteadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_homestead, container, false);
        newHomesteadNumEditText = (EditText)view.findViewById(R.id.newHomesteadNumEditText);
        newFIOEditText = (EditText)view.findViewById(R.id.newFIOEditText);
        Button saveButton = (Button) view.findViewById(R.id.homesteadCreateButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    HomesteadModel homesteadModel = new HomesteadModel();
                    homesteadModel.setHomesteadNumberColumn(newHomesteadNumEditText.getText().toString());
                    homesteadModel.setFioColumn(newFIOEditText.getText().toString());
                    myDataBase.addHomesteadRec(homesteadModel);
                    mListener.onFragmentAddNewHomestead();
                }
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mListener.onFragmentBackPressed();
                    return true;
                }
                return false;
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
        void onFragmentAddNewHomestead();
        void onFragmentBackPressed();
    }
}
