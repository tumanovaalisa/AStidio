package com.example.astidio;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {
    public MapFragment(){super(R.layout.map_fragment);}

    public static MapFragment newInstance(){
        return new MapFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton mapButton = view.findViewById(R.id.back_profile);
        MapView mapView = view.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(new GeoPoint(59.9401, 30.3260));

        // Добавление маркера
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(59.9401, 30.3260));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(marker);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToProfile();
            }
        });
    }
    private void backToProfile() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        // Проверяем, есть ли фрагменты в стеке
        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Удаляем текущий фрагмент из стека
            fragmentManager.popBackStack();
        } else {
            ProfileFragment profile = new ProfileFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_content, profile)
                    .commit();
        }
    }
}
