// Generated by view binder compiler. Do not edit!
package com.example.pawfect.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager2.widget.ViewPager2;
import com.example.pawfect.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView locationText;

  @NonNull
  public final TextView nameText;

  @NonNull
  public final AppCompatImageView profileImg;

  @NonNull
  public final TabLayout tablayout;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final ViewPager2 viewpager;

  private FragmentProfileBinding(@NonNull ConstraintLayout rootView, @NonNull TextView locationText,
      @NonNull TextView nameText, @NonNull AppCompatImageView profileImg,
      @NonNull TabLayout tablayout, @NonNull Toolbar toolbar, @NonNull ViewPager2 viewpager) {
    this.rootView = rootView;
    this.locationText = locationText;
    this.nameText = nameText;
    this.profileImg = profileImg;
    this.tablayout = tablayout;
    this.toolbar = toolbar;
    this.viewpager = viewpager;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.location_text;
      TextView locationText = ViewBindings.findChildViewById(rootView, id);
      if (locationText == null) {
        break missingId;
      }

      id = R.id.name_text;
      TextView nameText = ViewBindings.findChildViewById(rootView, id);
      if (nameText == null) {
        break missingId;
      }

      id = R.id.profile_img;
      AppCompatImageView profileImg = ViewBindings.findChildViewById(rootView, id);
      if (profileImg == null) {
        break missingId;
      }

      id = R.id.tablayout;
      TabLayout tablayout = ViewBindings.findChildViewById(rootView, id);
      if (tablayout == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      id = R.id.viewpager;
      ViewPager2 viewpager = ViewBindings.findChildViewById(rootView, id);
      if (viewpager == null) {
        break missingId;
      }

      return new FragmentProfileBinding((ConstraintLayout) rootView, locationText, nameText,
          profileImg, tablayout, toolbar, viewpager);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}