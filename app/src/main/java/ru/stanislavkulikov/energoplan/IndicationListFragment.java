package ru.stanislavkulikov.energoplan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IndicationListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IndicationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndicationListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_ID = "param_id";

    private int paramId;

    private OnFragmentInteractionListener mListener;

    public IndicationListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param paramId Parameter ID.
     * @return A new instance of fragment IndicationListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndicationListFragment newInstance(int paramId) {
        IndicationListFragment fragment = new IndicationListFragment();
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
        View view = inflater.inflate(R.layout.fragment_indication_list, container, false);

        TabHost tabHost = (TabHost) view.findViewById(R.id.indicationTabHost);

        // инициализация
        tabHost.setup();
        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("tabSpecTagInd");
        // название вкладки
        tabSpec.setIndicator("Показания");
        // указываем id компонента из FrameLayout, он и станет содержимым
        tabSpec.setContent(R.id.tabIndicationLayout);
        // добавляем в корневой элемент
        tabHost.addTab(tabSpec);

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("tabSpecTagPay");
        // название вкладки
        tabSpec.setIndicator("Оплата");
        // указываем id компонента из FrameLayout, он и станет содержимым
        tabSpec.setContent(R.id.tabPaymentLayout);
        // добавляем в корневой элемент
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag("tabSpecTagInd");

        IndicationListViewFragment indicationListViewFragment = IndicationListViewFragment.newInstance(paramId);
        FragmentTransaction fTrans = getActivity().getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.tabIndicationLayout, indicationListViewFragment);
        fTrans.commit();

        PaymentListViewFragment paymentListViewFragment = PaymentListViewFragment.newInstance(paramId);
        fTrans = getActivity().getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.tabPaymentLayout, paymentListViewFragment);
        fTrans.commit();

        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                Toast.makeText(getActivity().getBaseContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
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
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
    }
}
