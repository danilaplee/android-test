package layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundbrenner.testapp.R;

import org.w3c.dom.Text;

public class DiscoveryCard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String title = "title";
    private static final String text = "text";
    private static final String bitmap = "bitmap";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Bitmap bmp;

    private OnFragmentInteractionListener mListener;

    public DiscoveryCard() {
        // Required empty public constructor
    }

    public static DiscoveryCard newInstance(String param1, String param2, byte[] param3) {
        DiscoveryCard fragment = new DiscoveryCard();
        Bundle args = new Bundle();
        args.putString(title, param1);
        args.putString(text, param2);
        args.putByteArray(bitmap, param3);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("_CARD_ON_CREATE", savedInstanceState.toString());

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("SETTING_PARAMS", savedInstanceState.toString());
        mParam1 = savedInstanceState.getString(title);
        mParam2 = savedInstanceState.getString(text);
        byte[] bytes    = savedInstanceState.getByteArray(bitmap);
        bmp             = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        TextView    titleView   = (TextView) view.findViewById(R.id.discovery_title);
                    titleView.setText(mParam1);

        TextView    textView    = (TextView) view.findViewById(R.id.discovery_text);
                    textView.setText(mParam2);

        ImageView   imageView   = (ImageView) view.findViewById(R.id.discovery_image);
                    imageView.setImageBitmap(bmp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_discovery_card, container, false);
        onViewCreated(layout, savedInstanceState);
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
