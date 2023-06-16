// Generated by view binder compiler. Do not edit!
package com.example.pawfect.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.pawfect.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentHomeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView animalsRecycler;

  @NonNull
  public final Chip chipCat;

  @NonNull
  public final Chip chipDog;

  @NonNull
  public final ChipGroup chipGroupFilter;

  @NonNull
  public final Chip chipOthers;

  @NonNull
  public final FloatingActionButton fabAddAnimal;

  @NonNull
  public final ShimmerFrameLayout shimmerAnimal;

  @NonNull
  public final SwipeRefreshLayout swipeRefresh;

  @NonNull
  public final Toolbar toolbar;

  private FragmentHomeBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView animalsRecycler, @NonNull Chip chipCat, @NonNull Chip chipDog,
      @NonNull ChipGroup chipGroupFilter, @NonNull Chip chipOthers,
      @NonNull FloatingActionButton fabAddAnimal, @NonNull ShimmerFrameLayout shimmerAnimal,
      @NonNull SwipeRefreshLayout swipeRefresh, @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.animalsRecycler = animalsRecycler;
    this.chipCat = chipCat;
    this.chipDog = chipDog;
    this.chipGroupFilter = chipGroupFilter;
    this.chipOthers = chipOthers;
    this.fabAddAnimal = fabAddAnimal;
    this.shimmerAnimal = shimmerAnimal;
    this.swipeRefresh = swipeRefresh;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentHomeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_home, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentHomeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.animals_recycler;
      RecyclerView animalsRecycler = ViewBindings.findChildViewById(rootView, id);
      if (animalsRecycler == null) {
        break missingId;
      }

      id = R.id.chip_cat;
      Chip chipCat = ViewBindings.findChildViewById(rootView, id);
      if (chipCat == null) {
        break missingId;
      }

      id = R.id.chip_dog;
      Chip chipDog = ViewBindings.findChildViewById(rootView, id);
      if (chipDog == null) {
        break missingId;
      }

      id = R.id.chip_group_filter;
      ChipGroup chipGroupFilter = ViewBindings.findChildViewById(rootView, id);
      if (chipGroupFilter == null) {
        break missingId;
      }

      id = R.id.chip_others;
      Chip chipOthers = ViewBindings.findChildViewById(rootView, id);
      if (chipOthers == null) {
        break missingId;
      }

      id = R.id.fab_add_animal;
      FloatingActionButton fabAddAnimal = ViewBindings.findChildViewById(rootView, id);
      if (fabAddAnimal == null) {
        break missingId;
      }

      id = R.id.shimmer_animal;
      ShimmerFrameLayout shimmerAnimal = ViewBindings.findChildViewById(rootView, id);
      if (shimmerAnimal == null) {
        break missingId;
      }

      id = R.id.swipe_refresh;
      SwipeRefreshLayout swipeRefresh = ViewBindings.findChildViewById(rootView, id);
      if (swipeRefresh == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new FragmentHomeBinding((ConstraintLayout) rootView, animalsRecycler, chipCat, chipDog,
          chipGroupFilter, chipOthers, fabAddAnimal, shimmerAnimal, swipeRefresh, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
