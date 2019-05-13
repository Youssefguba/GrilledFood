package com.example.grilledfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Database.Database;
import com.example.grilledfood.Interface.ItemClickListener;
import com.example.grilledfood.Model.Category;
import com.example.grilledfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.example.grilledfood.Helper.LocaleHelper.SELECTED_LANGUAGE;


public class CategoryFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference category;
    DatabaseReference arabicCategory;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    Database localDb;

    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recycler_menu;
    View myFragment;
    String languageSelected;
    String lang;

    public static CategoryFragment newInstance() {
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        arabicCategory = database.getReference("CategoryArabic");

        lang = SELECTED_LANGUAGE;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        languageSelected = sharedPreferences.getString(SELECTED_LANGUAGE, "");
        localDb = new Database(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_category, container, false);

        //Load Menu Images and Texts..
        recycler_menu = myFragment.findViewById(R.id.recycler_menu);
        recycler_menu.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recycler_menu.getContext(),
                R.anim.layout_fall_down);
        recycler_menu.setLayoutAnimation(controller);

        //View
        swipeRefreshLayout = myFragment.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectionToInternet(getActivity())) {
                    if (languageSelected.equals("en")) {
                        loadMenu();
                    } else if (languageSelected.equals("ar")) {

                        loadMenuForArabic();
                    }

                } else {

                    Toast.makeText(getActivity(), "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //Default, Load for first time
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectionToInternet(getActivity())) {
                    if (languageSelected.equals("en")) {
                        loadMenu();
                    } else if (languageSelected.equals("ar")) {

                        loadMenuForArabic();
                    }

                } else {
                    Toast.makeText(getActivity(), "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return myFragment;
    }

    private void loadMenu() {
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int position, @NonNull Category model) {
                viewHolder.textMenuView.setText(model.getNameOfFood());
                Picasso.with(getActivity()).load(model.getImage())
                        .into(viewHolder.imageMenuView);

//                //add Favorite
//
//                if (localDb.isFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone())) {
//                    viewHolder.favoriteIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
//                }
//                // Click To change the statue of image
//
//                viewHolder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!localDb.isFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone())) {
//                            localDb.addToFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone());
//                            viewHolder.favoriteIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
//
//                            Toast.makeText(getActivity(), "" + model.getNameOfFood() + " was added to Favorite list", Toast.LENGTH_SHORT).show();
//                        } else {
//                            localDb.removeFromFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone());
//                            viewHolder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                            Toast.makeText(getActivity(), "" + model.getNameOfFood() + " was removed to Favorite list", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start Food Details Activity
                        Intent foodIntent = new Intent(getActivity(), FoodDetails.class);
                        //CategoryId is a key, so we need to get the Key of item ..
                        foodIntent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodIntent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.menu_item, viewGroup, false);
                return new MenuViewHolder(itemView);
            }
        };

        adapter.startListening();
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

        // Animation View
        recycler_menu.getAdapter().notifyDataSetChanged();
        recycler_menu.scheduleLayoutAnimation();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (languageSelected.equals("en")) {
            loadMenu();
        } else if (languageSelected.equals("ar")) {

            loadMenuForArabic();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    private void loadMenuForArabic() {
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(arabicCategory, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int position, @NonNull Category model) {
                viewHolder.textMenuView.setText(model.getNameOfFood());
                Picasso.with(getActivity()).load(model.getImage())
                        .into(viewHolder.imageMenuView);

//                //add Favorite
//
//                if (localDb.isFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone())) {
//                    viewHolder.favoriteIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
//                }
//                // Click To change the statue of image
//
//                viewHolder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!localDb.isFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone())) {
//                            localDb.addToFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone());
//                            viewHolder.favoriteIcon.setImageResource(R.drawable.ic_favorite_black_24dp);
//
//                            Toast.makeText(getActivity(), "" + model.getNameOfFood() + " was added to Favorite list", Toast.LENGTH_SHORT).show();
//                        } else {
//                            localDb.removeFromFavorites(adapter.getRef(position).getKey(), Common.mCurrentUser.getPhone());
//                            viewHolder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                            Toast.makeText(getActivity(), "" + model.getNameOfFood() + " was removed to Favorite list", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start Food Details Activity
                        Intent foodIntent = new Intent(getActivity(), FoodDetails.class);
                        //CategoryId is a key, so we need to get the Key of item ..
                        foodIntent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodIntent);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.menu_item, viewGroup, false);
                return new MenuViewHolder(itemView);
            }
        };

        adapter.startListening();
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

        // Animation View
        recycler_menu.getAdapter().notifyDataSetChanged();
        recycler_menu.scheduleLayoutAnimation();
    }

}

